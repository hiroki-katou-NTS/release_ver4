package nts.uk.ctx.at.request.infra.repository.application.holidaywork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.GoBackAtr;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkClock;
import nts.uk.ctx.at.request.dom.application.holidayworktime.primitivevalue.HolidayAppPrimitiveTime;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdpApplicationPK_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdtApplication_New;
import nts.uk.ctx.at.request.infra.entity.application.holidaywork.KrqdtAppHolidayWork;
import nts.uk.ctx.at.request.infra.entity.application.holidaywork.KrqdtAppHolidayWorkPK;
import nts.uk.ctx.at.request.infra.entity.application.holidaywork.KrqdtHolidayWorkInput;
import nts.uk.ctx.at.request.infra.entity.application.holidaywork.KrqdtHolidayWorkInputPK;
import nts.uk.ctx.at.request.infra.entity.application.overtime.KrqdtAppOvertimeDetail;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
@Stateless
public class JpaAppHolidayWorkRepository extends JpaRepository implements AppHolidayWorkRepository{
	private static final String FIND_ALL = "SELECT e FROM KrqdtAppHolidayWork e";

	private static final String FIND_BY_APPID;
	private static final String FIND_BY_LIST_APPID = "SELECT a FROM KrqdtAppHolidayWork a"
			+ " WHERE a.krqdtAppHolidayWorkPK.cid = :companyID"
			+ " AND a.krqdtAppHolidayWorkPK.appId IN :lstAppID";
	static {
		StringBuilder query = new StringBuilder();
		query.append(FIND_ALL);
		query.append(" WHERE e.krqdtAppHolidayWorkPK.cid = :companyID");
		query.append(" AND e.krqdtAppHolidayWorkPK.appId = :appID");
		FIND_BY_APPID = query.toString();
	}
	@Override
	public Optional<AppHolidayWork> getAppHolidayWork(String companyID, String appID) {
		
		return this.queryProxy().query(FIND_BY_APPID, KrqdtAppHolidayWork.class)
				.setParameter("companyID", companyID).setParameter("appID", appID).getSingle( e -> convertToDomain(e));
	}
	private AppHolidayWork convertToDomain(KrqdtAppHolidayWork entity) {
		return AppHolidayWork.createSimpleFromJavaType(entity.getKrqdtAppHolidayWorkPK().getCid(),
				entity.getKrqdtAppHolidayWorkPK().getAppId(), entity.getWorkTypeCode(),
				entity.getWorkTimeCode(), entity.getWorkClockStart1(), 
				entity.getWorkClockEnd1(), 
				entity.getWorkClockStart2(), 
				entity.getWorkClockEnd2(),
				entity.getGoAtr1(),
				entity.getBackAtr1(),
				entity.getGoAtr2(),
				entity.getBackAtr2(),
				entity.getDivergenceReason(), 
				entity.getHolidayShiftNight());
	}
	@Override
	public void Add(AppHolidayWork domain) {
		this.commandProxy().insert(toEntity(domain));
	}
	private KrqdtAppHolidayWork toEntity(AppHolidayWork domain) {
		List<KrqdtHolidayWorkInput> overtimeInputs = domain.getHolidayWorkInputs().stream()
				.map(item -> {
					KrqdtHolidayWorkInputPK pk =  new KrqdtHolidayWorkInputPK(item.getCompanyID(), item.getAppID(),
							item.getAttendanceType().value, item.getFrameNo());
					return new KrqdtHolidayWorkInput(pk, item.getStartTime() == null ? null : item.getStartTime().v(), item.getEndTime() ==  null? null : item.getEndTime().v(),
							item.getApplicationTime() == null ? null : item.getApplicationTime().v());
				})
				.collect(Collectors.toList());

		return new KrqdtAppHolidayWork(new KrqdtAppHolidayWorkPK(domain.getCompanyID(), domain.getAppID()),
				domain.getVersion(), domain.getWorkTypeCode() == null ? null : domain.getWorkTypeCode().v(),
				domain.getWorkTimeCode() == null ? null : domain.getWorkTimeCode().v(),
				domain.getWorkClock1().getStartTime() == null ? null : domain.getWorkClock1().getStartTime().v(),
				domain.getWorkClock1().getEndTime() == null ? null : domain.getWorkClock1().getEndTime().v(),
				domain.getWorkClock1().getGoAtr().value,
				domain.getWorkClock1().getBackAtr().value,
				domain.getWorkClock2().getStartTime() == null ? null : domain.getWorkClock2().getStartTime().v(),
				domain.getWorkClock2().getEndTime() == null ? null : domain.getWorkClock2().getEndTime().v(),
				domain.getWorkClock2().getGoAtr().value,
				domain.getWorkClock2().getBackAtr().value,
				domain.getDivergenceReason(),
				domain.getHolidayShiftNight(), overtimeInputs,
				KrqdtAppOvertimeDetail.toEntity(domain.getAppOvertimeDetail()));
	}
	@Override
	public Optional<AppHolidayWork> getFullAppHolidayWork(String companyID, String appID) {
		Optional<KrqdtAppHolidayWork> opKrqdtAppHolidayWork = this.queryProxy().find(new KrqdtAppHolidayWorkPK(companyID, appID), KrqdtAppHolidayWork.class);
		Optional<KrqdtApplication_New> opKafdtApplication = this.queryProxy().find(new KrqdpApplicationPK_New(companyID, appID), KrqdtApplication_New.class);
		if(!opKrqdtAppHolidayWork.isPresent()||!opKafdtApplication.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppHolidayWork krqdtAppHolidaWork = opKrqdtAppHolidayWork.get();
		KrqdtApplication_New kafdtApplication = opKafdtApplication.get();
		AppHolidayWork appHolidayWork = krqdtAppHolidaWork.toDomain();
		appHolidayWork.setApplication(kafdtApplication.toDomain());
		return Optional.of(appHolidayWork);
	}
	@Override
	public void update(AppHolidayWork domain) {
		String companyID = domain.getCompanyID();
		String appID = domain.getAppID();
		Optional<KrqdtAppHolidayWork> opKrqdtAppHolidayWork = this.queryProxy().find(new KrqdtAppHolidayWorkPK(companyID, appID), KrqdtAppHolidayWork.class);
		if(!opKrqdtAppHolidayWork.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de update");
		}
		KrqdtAppHolidayWork krqdtAppHolidayWork = opKrqdtAppHolidayWork.get();
		krqdtAppHolidayWork.fromDomainValue(domain);
		this.commandProxy().update(krqdtAppHolidayWork);
		
	}
	@Override
	public void delete(String companyID, String appID) {
		Optional<KrqdtAppHolidayWork> opKrqdtAppHolidayWork = this.queryProxy().find(new KrqdtAppHolidayWorkPK(companyID, appID), KrqdtAppHolidayWork.class);
		if(!opKrqdtAppHolidayWork.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de update");
		}
		this.commandProxy().remove(KrqdtAppHolidayWork.class, new KrqdtAppHolidayWorkPK(companyID, appID));
	}
	/**
	 * get Application Holiday Work and Frame
	 * @author hoatt
	 * @param companyID
	 * @param appID
	 * @return
	 */
	@Override
	public Optional<AppHolidayWork> getAppHolidayWorkFrame(String companyID, String appID) {
		Optional<KrqdtAppHolidayWork> opKrqdtAppHolidayWork = this.queryProxy().find(new KrqdtAppHolidayWorkPK(companyID, appID), KrqdtAppHolidayWork.class);
		if(!opKrqdtAppHolidayWork.isPresent()){
			return Optional.ofNullable(null);
		}
		KrqdtAppHolidayWork krqdtAppHolidaWork = opKrqdtAppHolidayWork.get();
		AppHolidayWork appHolidayWork = krqdtAppHolidaWork.toDomain();
		return Optional.of(appHolidayWork);
	}
	/**
	 * get list Application Holiday Work and Frame
	 * @author hoatt
	 * @param companyID
	 * @param lstAppID
	 * @return map: key - appID, value - AppHolidayWork
	 */
	@Override
	public Map<String, AppHolidayWork> getListAppHdWorkFrame(String companyID, List<String> lstAppID) {
		Map<String, AppHolidayWork> lstMap = new HashMap<>();
		if(lstAppID.isEmpty()){
			return lstMap;
		}
		List<AppHolidayWork> lstHd = new ArrayList<>();
		CollectionUtil.split(lstAppID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			lstHd.addAll(this.queryProxy().query(FIND_BY_LIST_APPID, KrqdtAppHolidayWork.class)
							 .setParameter("companyID", companyID)
							 .setParameter("lstAppID", subList)
							 .getList(c -> toDomainPlus(c)));
		});
		for (AppHolidayWork hd : lstHd) {
			lstMap.put(hd.getAppID(), hd);
		}
		return lstMap;
	}
	public AppHolidayWork toDomainPlus(KrqdtAppHolidayWork entity){
		return new AppHolidayWork(null, 
				entity.getKrqdtAppHolidayWorkPK().getCid(), 
				entity.getKrqdtAppHolidayWorkPK().getAppId(), 
				entity.holidayWorkInputs.stream()
					.map(x -> x.toDomain()).collect(Collectors.toList()),
				new WorkTypeCode(entity.getWorkTypeCode()),
				entity.getWorkTimeCode() == null ? null : new WorkTimeCode(entity.getWorkTimeCode()), 
				new HolidayWorkClock(entity.getWorkClockStart1() == null ? null : new HolidayAppPrimitiveTime(entity.getWorkClockStart1()),
						entity.getWorkClockEnd1() == null? null : new HolidayAppPrimitiveTime(entity.getWorkClockEnd1()),
					EnumAdaptor.valueOf(entity.getGoAtr1(), GoBackAtr.class),
					EnumAdaptor.valueOf(entity.getBackAtr1(), GoBackAtr.class)),
				new HolidayWorkClock(entity.getWorkClockStart2() == null? null : new HolidayAppPrimitiveTime(entity.getWorkClockStart2()),
						entity.getWorkClockEnd2() == null? null : new HolidayAppPrimitiveTime(entity.getWorkClockEnd2()),
						EnumAdaptor.valueOf(entity.getGoAtr2(), GoBackAtr.class),
						EnumAdaptor.valueOf(entity.getBackAtr2(), GoBackAtr.class)), 
				entity.getDivergenceReason(),
				entity.getHolidayShiftNight(),
				entity.appOvertimeDetail == null ? Optional.empty() : Optional.of(entity.appOvertimeDetail.toDomain()));
	}
}
