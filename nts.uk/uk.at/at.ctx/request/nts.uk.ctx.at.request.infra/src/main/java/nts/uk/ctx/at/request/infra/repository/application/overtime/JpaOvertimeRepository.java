package nts.uk.ctx.at.request.infra.repository.application.overtime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimeDetail;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimePK;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInput;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtOvertimeInputPK;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

@Stateless
public class JpaOvertimeRepository extends JpaRepository implements OvertimeRepository {
	private static final String FIND_ALL = "SELECT e FROM KrqdtAppOvertime e";

	private static final String FIND_BY_APPID;
	
	private static final String FIND_BY_ATR;
	private static final String FIND_BY_LIST_APPID = "SELECT a FROM KrqdtAppOvertime a"
			+ " WHERE a.krqdtAppOvertimePK.cid = :companyID"
			+ " AND a.krqdtAppOvertimePK.appId IN :lstAppID";
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
					return new KrqdtOvertimeInput(pk, item.getStartTime() == null ? null : item.getStartTime().v(), item.getEndTime() == null ? null : item.getEndTime().v(),
							item.getApplicationTime() == null ? null : item.getApplicationTime().v());
				})
				.collect(Collectors.toList());
		
		KrqdtAppOvertimeDetail appOvertimeDetail = KrqdtAppOvertimeDetail.toEntity(domain.getAppOvertimeDetail());

		return new KrqdtAppOvertime(new KrqdtAppOvertimePK(domain.getCompanyID(), domain.getAppID()),
				domain.getVersion(),
				domain.getOverTimeAtr().value, domain.getWorkTypeCode() == null? null :  domain.getWorkTypeCode().v(), domain.getSiftCode() == null ? null : domain.getSiftCode().v(),
				domain.getWorkClockFrom1(), domain.getWorkClockTo1(), domain.getWorkClockFrom2(),
				domain.getWorkClockTo2(), domain.getDivergenceReason(), domain.getFlexExessTime(),
				domain.getOverTimeShiftNight(), overtimeInputs, appOvertimeDetail);
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
				.setParameter("overtimeAtr", overTimeAtr.value)
				.getList(e -> convertToDomain(e));
		//List<AppOverTime> fullList = 
				appOverTimeList.stream()
				.map(x -> this.getFullAppOvertime(x.getCompanyID(), x.getAppID()).orElse(null)).collect(Collectors.toList());
		List<AppOverTime> resultList = appOverTimeList.stream()
			.filter(x -> {
				if(x==null) return false;
				Application_New app = x.getApplication();
				if(app==null) return false;
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
	/**
	 * get list Application Over Time and Frame
	 * @author hoatt 
	 * @param companyID
	 * @param lstAppID
	 * @return map: key - appID, value - AppOverTime
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Map<String, AppOverTime> getListAppOvertimeFrame(String companyID, List<String> lstAppID) {
		Map<String, AppOverTime> lstMap = new HashMap<>();
		if(lstAppID.isEmpty()){
			return lstMap;
		}
		List<AppOverTime> lstOt = new ArrayList<>();
		CollectionUtil.split(lstAppID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			lstOt.addAll(this.queryProxy().query(FIND_BY_LIST_APPID, KrqdtAppOvertime.class)
							 .setParameter("companyID", companyID)
							 .setParameter("lstAppID", subList)
							 .getList(c -> toDomainPlus(c)));
		});
		for (AppOverTime ot : lstOt) {
			lstMap.put(ot.getAppID(), ot);
		}
		return lstMap;
	}
	private AppOverTime toDomainPlus(KrqdtAppOvertime entity){
		return new AppOverTime(null, 
				entity.getKrqdtAppOvertimePK().getCid(), 
				entity.getKrqdtAppOvertimePK().getAppId(), 
				EnumAdaptor.valueOf(entity.getOvertimeAtr(), OverTimeAtr.class),
				entity.overtimeInputs.stream()
					.map(x -> x.toDomain()).collect(Collectors.toList()),
				new WorkTypeCode(entity.getWorkTypeCode()),
				new WorkTimeCode(entity.getSiftCode()), 
				entity.getWorkClockFrom1(),
				entity.getWorkClockTo1(), entity.getWorkClockFrom2(), 
				entity.getWorkClockTo2(), entity.getDivergenceReason(),
				entity.getFlexExcessTime(), entity.getOvertimeShiftNight(),
				entity.appOvertimeDetail == null ? Optional.empty() : Optional.of(entity.appOvertimeDetail.toDomain()));
	}
}
