package nts.uk.ctx.at.record.infra.repository.shorttimework;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.shorttimework.primitivevalue.ShortWorkTimFrameNo;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
//import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiBreakTime;
import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDaiShortWorkTime;
import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDaiShortWorkTimePK;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaShortTimeOfDailyPerformanceRepo extends JpaRepository implements ShortTimeOfDailyPerformanceRepository {

//	private static final String REMOVE_BY_EMPLOYEEID_AND_DATE;
//
//	static {
//		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiShortWorkTime a ");
//		builderString.append("WHERE a.krcdtDaiShortWorkTimePK.sid = :employeeId ");
//		builderString.append("AND a.krcdtDaiShortWorkTimePK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEEID_AND_DATE = builderString.toString();
//	}

	@Override
	public Optional<ShortTimeOfDailyPerformance> find(String employeeId, GeneralDate ymd) {
		List<ShortWorkingTimeSheet> shortTimeSheets = findEntities(employeeId, ymd).getList(c -> shortWorkTime(c));
		if (!shortTimeSheets.isEmpty()) {
			return Optional.of(new ShortTimeOfDailyPerformance(employeeId, shortTimeSheets, ymd));
		}
		return Optional.empty();
	}

	private ShortWorkingTimeSheet shortWorkTime(KrcdtDaiShortWorkTime c) {
		return new ShortWorkingTimeSheet(new ShortWorkTimFrameNo(c.krcdtDaiShortWorkTimePK.shortWorkTimeFrameNo),
				EnumAdaptor.valueOf(c.childCareAtr, ChildCareAttribute.class), new TimeWithDayAttr(c.startTime),
				new TimeWithDayAttr(c.endTime), new AttendanceTime(c.deductionTime), new AttendanceTime(c.time));
	}

	@Override
	public void updateByKey(ShortTimeOfDailyPerformance shortWork) {
		if(shortWork == null){ return;}
		
		if (!shortWork.getShortWorkingTimeSheets().isEmpty()) {
			List<KrcdtDaiShortWorkTime> all = shortWork.getShortWorkingTimeSheets().stream()
					.filter(c -> c.getEndTime() != null && c.getStartTime() != null)
					.map(c -> newEntities(shortWork.getEmployeeId(), shortWork.getYmd(), c)).collect(Collectors.toList());
			List<KrcdtDaiShortWorkTime> krcdtShortTimes = findEntities(shortWork.getEmployeeId(), shortWork.getYmd()).getList();
			List<KrcdtDaiShortWorkTime> toRemove = krcdtShortTimes.stream()
					.filter(c -> !all.stream().filter(tu -> tu.krcdtDaiShortWorkTimePK.shortWorkTimeFrameNo == c.krcdtDaiShortWorkTimePK.shortWorkTimeFrameNo)
										.findFirst().isPresent())
					.collect(Collectors.toList());
			
			toRemove.stream().forEach(c -> {
				commandProxy().remove(c);
			});
			commandProxy().updateAll(all);
			this.getEntityManager().flush();
		} else {
			this.deleteByEmployeeIdAndDate(shortWork.getEmployeeId(), shortWork.getYmd());
		}
	}

	@Override
	public void insert(ShortTimeOfDailyPerformance shortWork) {
		List<KrcdtDaiShortWorkTime> entities = shortWork.getShortWorkingTimeSheets().stream()
				.map(c -> newEntities(shortWork.getEmployeeId(), shortWork.getYmd(), c)).collect(Collectors.toList());
		commandProxy().insertAll(entities);
		this.getEntityManager().flush();
	}

	private KrcdtDaiShortWorkTime newEntities(String employeeId, GeneralDate ymd, ShortWorkingTimeSheet c) {
		return new KrcdtDaiShortWorkTime(new KrcdtDaiShortWorkTimePK(employeeId, ymd, c.getShortWorkTimeFrameNo().v()),
				c.getStartTime() == null ? 0 : c.getStartTime().valueAsMinutes(),
				c.getEndTime() == null ? 0 : c.getEndTime().valueAsMinutes(), c.getChildCareAttr().value,
				c.getShortTime() == null ? 0 : c.getShortTime().valueAsMinutes(),
				c.getDeductionTime() == null ? 0 : c.getDeductionTime().valueAsMinutes());
	}

	private TypedQueryWrapper<KrcdtDaiShortWorkTime> findEntities(String employeeId, GeneralDate ymd) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT s FROM KrcdtDaiShortWorkTime s");
		query.append(" WHERE s.krcdtDaiShortWorkTimePK.sid = :employeeId");
		query.append(" AND s.krcdtDaiShortWorkTimePK.ymd = :ymd");
		query.append(" ORDER BY s.krcdtDaiShortWorkTimePK.shortWorkTimeFrameNo");
		return queryProxy().query(query.toString(), KrcdtDaiShortWorkTime.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd);
	}

	@Override
	public List<ShortTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		List<ShortTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtDaiShortWorkTime a ");
		query.append("WHERE a.krcdtDaiShortWorkTimePK.sid IN :employeeId ");
		query.append("AND a.krcdtDaiShortWorkTimePK.ymd <= :end AND a.krcdtDaiShortWorkTimePK.ymd >= :start");
		TypedQueryWrapper<KrcdtDaiShortWorkTime> tQuery=  this.queryProxy().query(query.toString(), KrcdtDaiShortWorkTime.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds)
								.setParameter("start", ymd.start())
								.setParameter("end", ymd.end()).getList().stream()
								.collect(Collectors.groupingBy(
										c -> c.krcdtDaiShortWorkTimePK.sid + c.krcdtDaiShortWorkTimePK.ymd.toString()))
								.entrySet().stream()
								.map(c -> new ShortTimeOfDailyPerformance(c.getValue().get(0).krcdtDaiShortWorkTimePK.sid,
												c.getValue().stream().map(x -> shortWorkTime(x)).collect(Collectors.toList()),
												c.getValue().get(0).krcdtDaiShortWorkTimePK.ymd))
								.collect(Collectors.toList()));
		});
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteByEmployeeIdAndDate(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAI_SHORTTIME_TS Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEEID_AND_DATE).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).executeUpdate();
//		this.getEntityManager().flush();
	}

	@Override
	public List<ShortTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<ShortTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtDaiShortWorkTime a ");
		query.append("WHERE a.krcdtDaiShortWorkTimePK.sid IN :employeeId ");
		query.append("AND a.krcdtDaiShortWorkTimePK.ymd IN :date");
		TypedQueryWrapper<KrcdtDaiShortWorkTime> tQuery=  this.queryProxy().query(query.toString(), KrcdtDaiShortWorkTime.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
								.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
								.getList().stream()
								.filter(c -> p.get(c.krcdtDaiShortWorkTimePK.sid).contains(c.krcdtDaiShortWorkTimePK.ymd))
								.collect(Collectors.groupingBy(
										c -> c.krcdtDaiShortWorkTimePK.sid + c.krcdtDaiShortWorkTimePK.ymd.toString()))
								.entrySet().stream()
								.map(c -> new ShortTimeOfDailyPerformance(c.getValue().get(0).krcdtDaiShortWorkTimePK.sid,
												c.getValue().stream().map(x -> shortWorkTime(x)).collect(Collectors.toList()),
												c.getValue().get(0).krcdtDaiShortWorkTimePK.ymd))
								.collect(Collectors.toList()));
		});
		return result;
	}

}
