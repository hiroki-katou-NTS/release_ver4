package nts.uk.ctx.at.record.infra.repository.workinformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtDaiPerWorkInfo;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtDaiPerWorkInfoPK;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTime;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTimePK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author lamdt
 *
 */
@Stateless
public class JpaWorkInformationRepository extends JpaRepository implements WorkInformationRepository {

	private static final String DEL_BY_KEY;
	
	private static final String FIND_BY_PERIOD_ORDER_BY_YMD;
	
	private static final String FIND_BY_LIST_SID_AND_PERIOD;
	
	private static final String DEL_BY_KEY_ID;

	private static final String FIND_BY_ID = "SELECT a FROM KrcdtDaiPerWorkInfo a "
			+ " WHERE a.krcdtDaiPerWorkInfoPK.employeeId = :employeeId " + " AND a.krcdtDaiPerWorkInfoPK.ymd = :ymd ";

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiPerWorkInfo a ");
		builderString.append("WHERE a.krcdtDaiPerWorkInfoPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd = :ymd ");
		DEL_BY_KEY = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtWorkScheduleTime a ");
		builderString.append("WHERE a.krcdtWorkScheduleTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtWorkScheduleTimePK.ymd = :ymd ");
		DEL_BY_KEY_ID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiPerWorkInfo a ");
		builderString.append("WHERE a.krcdtDaiPerWorkInfoPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd >= :startDate ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd <= :endDate ");
		builderString.append("ORDER BY a.krcdtDaiPerWorkInfoPK.ymd ");
		FIND_BY_PERIOD_ORDER_BY_YMD = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiPerWorkInfo a ");
		builderString.append("WHERE a.krcdtDaiPerWorkInfoPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd >= :startDate ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd <= :endDate ");
		FIND_BY_LIST_SID_AND_PERIOD = builderString.toString();
	}

	@Override
	public Optional<WorkInfoOfDailyPerformance> find(String employeeId, GeneralDate ymd) {
		return this.queryProxy().query(FIND_BY_ID, KrcdtDaiPerWorkInfo.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(c -> c.toDomain());
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(DEL_BY_KEY_ID).setParameter("employeeId", employeeId).setParameter("ymd", ymd)
		.executeUpdate();
		
		this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("employeeId", employeeId).setParameter("ymd", ymd)
				.executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	public List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
		return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD, KrcdtDaiPerWorkInfo.class)
				.setParameter("employeeId", employeeId).setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end()).getList(f -> f.toDomain());
	}
	
	@Override
	public List<WorkInfoOfDailyPerformance> findByListEmployeeId(List<String> employeeIds, DatePeriod ymds) {
		return this.queryProxy().query(FIND_BY_LIST_SID_AND_PERIOD, KrcdtDaiPerWorkInfo.class)
				.setParameter("employeeIds", employeeIds).setParameter("startDate", ymds.start())
				.setParameter("endDate", ymds.end()).getList(f -> f.toDomain());
	}

	@Override
	public void updateByKey(WorkInfoOfDailyPerformance domain) {
		Optional<KrcdtDaiPerWorkInfo> dataOpt = this.queryProxy().query(FIND_BY_ID, KrcdtDaiPerWorkInfo.class).setParameter("employeeId", domain.getEmployeeId())
				.setParameter("ymd", domain.getYmd()).getSingle();
		KrcdtDaiPerWorkInfo data = dataOpt.isPresent() ? dataOpt.get() : new KrcdtDaiPerWorkInfo(new KrcdtDaiPerWorkInfoPK(domain.getEmployeeId(), domain.getYmd()));
		if(domain != null){
			if(data.scheduleTimes == null){
				data.scheduleTimes = new ArrayList<>();
			}
//			data.krcdtDaiPerWorkInfoPK.employeeId = domain.getEmployeeId();
//			data.krcdtDaiPerWorkInfoPK.ymd = domain.getYmd();
			if(domain.getRecordWorkInformation() != null){
				data.recordWorkWorktimeCode = domain.getRecordWorkInformation().getWorkTimeCode() == null 
						? null : domain.getRecordWorkInformation().getWorkTimeCode().v();
				data.recordWorkWorktypeCode = domain.getRecordWorkInformation().getWorkTypeCode().v();
			}
			if(domain.getScheduleWorkInformation() != null){
				data.scheduleWorkWorktimeCode = domain.getScheduleWorkInformation().getWorkTimeCode() == null 
						? null : domain.getScheduleWorkInformation().getWorkTimeCode().v();
				data.scheduleWorkWorktypeCode = domain.getScheduleWorkInformation().getWorkTypeCode().v();
			}
			data.calculationState = domain.getCalculationState().value;
			data.backStraightAttribute = domain.getBackStraightAtr().value;
			data.goStraightAttribute = domain.getGoStraightAtr().value;
			data.dayOfWeek = domain.getDayOfWeek().value;
			
			List<ScheduleTimeSheet> scheduleTimeSheets = domain.getScheduleTimeSheets();
			scheduleTimeSheets.stream().forEach(c -> {
				KrcdtWorkScheduleTime item = data.scheduleTimes.stream().filter(x -> 
					x.krcdtWorkScheduleTimePK.employeeId.equals(domain.getEmployeeId())
						&& x.krcdtWorkScheduleTimePK.ymd.equals(domain.getYmd()) 
						&& x.krcdtWorkScheduleTimePK.workNo == c.getWorkNo().v()).findFirst().orElse(null);
				
				if(item != null){
//					item.krcdtWorkScheduleTimePK.employeeId = domain.getEmployeeId();
//					item.krcdtWorkScheduleTimePK.ymd = domain.getYmd();
//					item.krcdtWorkScheduleTimePK.workNo = c.getWorkNo().v();
					item.attendance = c.getAttendance().valueAsMinutes();
					item.leaveWork = c.getLeaveWork().valueAsMinutes();
				} else {
					KrcdtWorkScheduleTime newItem = new KrcdtWorkScheduleTime(new KrcdtWorkScheduleTimePK(domain.getEmployeeId(), domain.getYmd(), c.getWorkNo().v()),
							c.getAttendance().valueAsMinutes(), c.getLeaveWork().valueAsMinutes());
					data.scheduleTimes.add(newItem);
				}
			});
			this.commandProxy().update(data);
			this.commandProxy().updateAll(data.scheduleTimes);
		}
	}

	@Override
	public void insert(WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		this.commandProxy().insert(KrcdtDaiPerWorkInfo.toEntity(workInfoOfDailyPerformance));
	}

	@Override
	public void updateByKeyFlush(WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		this.updateByKey(workInfoOfDailyPerformance);
		this.getEntityManager().flush();
		
	}

}