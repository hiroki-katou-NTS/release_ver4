package nts.uk.ctx.at.request.infra.repository.application.overtime;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdpApplicationPK_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdtApplication_New;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertime;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimePK;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInput;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInputPK;

@Stateless
public class JpaOvertimeRepository extends JpaRepository implements OvertimeRepository {
	private static final String FIND_ALL = "SELECT e FROM KrqdtAppOvertime e";

	private static final String FIND_BY_APPID;
	
	private static final String FIND_BY_ATR;
	static {
		StringBuilder query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" WHERE e.krqdtAppOvertimePK.cid = :companyID");
		query.append(" AND e.krqdtAppOvertimePK.appId = :appID");
		FIND_BY_APPID = query.toString();
		
		query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" WHERE e.overtimeAtr = :overtimeAtr");
		FIND_BY_ATR = query.toString();
	}

	@Override
	public Optional<AppOverTime> getAppOvertime(String companyID, String appID) {
		return this.queryProxy().query(FIND_BY_APPID, KrqdtAppOvertime.class)
				.setParameter("companyID", companyID)
				.setParameter("appID", appID)
				.getSingle(e -> convertToDomain(e));
	}

	@Override
	public void Add(AppOverTime domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public Optional<AppOverTime> getFullAppOvertime(String companyID, String appID) {
		Optional<KrqdtAppOvertime> opKrqdtAppOvertime = this.queryProxy().find(new KrqdtAppOvertimePK(companyID, appID), KrqdtAppOvertime.class);
		Optional<KrqdtApplication_New> opKafdtApplication = this.queryProxy().find(new KrqdpApplicationPK_New(companyID, appID), KrqdtApplication_New.class);
		if(!opKrqdtAppOvertime.isPresent()||!opKafdtApplication.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppOvertime krqdtAppOvertime = opKrqdtAppOvertime.get();
		KrqdtApplication_New kafdtApplication = opKafdtApplication.get();
		AppOverTime appOverTime = krqdtAppOvertime.toDomain();
		appOverTime.setApplication(kafdtApplication.toDomain());
		return Optional.of(appOverTime);
	}

	@Override
	public void update(AppOverTime appOverTime) {
		String companyID = appOverTime.getCompanyID();
		String appID = appOverTime.getAppID();
		Optional<KrqdtAppOvertime> opKrqdtAppOvertime = this.queryProxy().find(new KrqdtAppOvertimePK(companyID, appID), KrqdtAppOvertime.class);
		if(!opKrqdtAppOvertime.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de update");
		}
		KrqdtAppOvertime krqdtAppOvertime = opKrqdtAppOvertime.get();
		krqdtAppOvertime.fromDomainValue(appOverTime);
		this.commandProxy().update(krqdtAppOvertime);
	}

	@Override
	public void delete(String companyID, String appID) {
		Optional<KrqdtAppOvertime> opKrqdtAppOvertime = this.queryProxy().find(new KrqdtAppOvertimePK(companyID, appID), KrqdtAppOvertime.class);
		if(!opKrqdtAppOvertime.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de xoa");
		}
		//Delete application over time
		this.commandProxy().remove(KrqdtAppOvertime.class, new KrqdtAppOvertimePK(companyID, appID));
	}
	private KrqdtAppOvertime toEntity(AppOverTime domain) {
		List<KrqdtOvertimeInput> overtimeInputs = domain.getOverTimeInput().stream()
				.map(item -> {
					KrqdtOvertimeInputPK pk =  new KrqdtOvertimeInputPK(item.getCompanyID(), item.getAppID(),
							item.getAttendanceType().value, item.getFrameNo(),item.getTimeItemTypeAtr().value);
					return new KrqdtOvertimeInput(pk, item.getStartTime().v(), item.getEndTime().v(),
							item.getApplicationTime().v());
				})
				.collect(Collectors.toList());

		return new KrqdtAppOvertime(new KrqdtAppOvertimePK(domain.getCompanyID(), domain.getAppID()),
				domain.getVersion(),
				domain.getOverTimeAtr().value, domain.getWorkTypeCode().v(), domain.getSiftCode().v(),
				domain.getWorkClockFrom1(), domain.getWorkClockTo1(), domain.getWorkClockFrom2(),
				domain.getWorkClockTo2(), domain.getDivergenceReason(), domain.getFlexExessTime(),
				domain.getOverTimeShiftNight(), overtimeInputs);
	}

	private AppOverTime convertToDomain(KrqdtAppOvertime entity) {
		return AppOverTime.createSimpleFromJavaType(entity.getKrqdtAppOvertimePK().getCid(),
				entity.getKrqdtAppOvertimePK().getAppId(), entity.getOvertimeAtr(), entity.getWorkTypeCode(),
				entity.getSiftCode(), entity.getWorkClockFrom1(), entity.getWorkClockTo1(), entity.getWorkClockFrom2(),
				entity.getWorkClockTo2(), entity.getDivergenceReason(), entity.getFlexExcessTime(),
				entity.getOvertimeShiftNight());
	}

	@Override
	public Optional<AppOverTime> getAppOvertimeByDate(GeneralDate appDate, String employeeID, OverTimeAtr overTimeAtr) {
		List<AppOverTime> appOverTimeList = this.queryProxy().query(FIND_BY_ATR, KrqdtAppOvertime.class)
				.setParameter("overTimeAtr", overTimeAtr)
				.getList(e -> convertToDomain(e));
		List<AppOverTime> resultList = appOverTimeList.stream()
			.filter(x -> {
				Application_New app = x.getApplication();
				return app.getAppDate().equals(appDate)&&
						app.getEmployeeID().equals(employeeID)&&
						app.getPrePostAtr().equals(PrePostAtr.PREDICT);
			}).collect(Collectors.toList());
		if(CollectionUtil.isEmpty(resultList)){
			return Optional.empty();
		}
		resultList.sort(Comparator.comparing((AppOverTime x) -> {return x.getApplication().getInputDate();}).reversed());
		return Optional.of(resultList.get(0));
	}
	/**
	 * get Application Over Time and Frame
	 * @author hoatt-new
	 * @param companyID
	 * @param appID
	 * @return
	 */
	@Override
	public Optional<AppOverTime> getAppOvertimeFrame(String companyID, String appID) {
		Optional<KrqdtAppOvertime> opKrqdtAppOvertime = this.queryProxy().find(new KrqdtAppOvertimePK(companyID, appID), KrqdtAppOvertime.class);
		if(!opKrqdtAppOvertime.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppOvertime krqdtAppOvertime = opKrqdtAppOvertime.get();
		AppOverTime appOverTime = krqdtAppOvertime.toDomain();
		return Optional.of(appOverTime);
	}
}
