package nts.uk.ctx.at.request.infra.repository.setting.request.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;
import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.ReflectAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.NumDaysOfWeek;
import nts.uk.ctx.at.request.dom.setting.request.application.common.PriorityFLg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.ReflectionFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstApplicationSetting;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstApplicationSettingPK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaApplicationSettingRepository extends JpaRepository implements ApplicationSettingRepository {

	public final String SELECT_NO_WHERE = "SELECT c FROM KrqstApplicationSetting c";

	public final String SELECT_WITH_CID = SELECT_NO_WHERE 
			+ " WHERE c.krqstApplicationSettingPK.companyID := companyID";

	public final String SELECT_WITH_APP_TYPE = SELECT_NO_WHERE
			+ " WHERE c.krqstApplicationSettingPK.companyID := companyID"
			+ " AND c.krqstApplicationSettingPK.appType := appType ";

	/**
	 * @param entity
	 * @return
	 */
	private ApplicationSetting toDomain(KrqstApplicationSetting entity) {
		return new ApplicationSetting(entity.krqstApplicationSettingPK.companyID, 
				EnumAdaptor.valueOf(entity.appActLockFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.appEndWorkFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.appActConfirmFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.appOvertimeNightFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.appActMonthConfirmFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.requireAppReasonFlg,RequiredFlg.class),
				EnumAdaptor.valueOf(entity.displayPrePostFlg,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.displaySearchTimeFlg,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.manualSendMailAtr,UseAtr.class),
				/*承認*/
				EnumAdaptor.valueOf(entity.baseDateFlg,BaseDateFlg.class),
				// 事前申請の超過メッセージ
				EnumAdaptor.valueOf(entity.advanceExcessMessDispAtr,AppDisplayAtr.class),
				// 休出の事前申請
				EnumAdaptor.valueOf(entity.hwAdvanceDispAtr,AppDisplayAtr.class),
				// 休出の実績
				EnumAdaptor.valueOf(entity.hwActualDispAtr,AppDisplayAtr.class),
				// 実績超過メッセージ
				EnumAdaptor.valueOf(entity.actualExcessMessDispAtr,AppDisplayAtr.class),
				// 残業の事前申請
				EnumAdaptor.valueOf(entity.otAdvanceDispAtr,AppDisplayAtr.class),
				// 残業の実績
				EnumAdaptor.valueOf(entity.otActualDispAtr,AppDisplayAtr.class),
				// 申請対象日に対して警告表示
				new NumDaysOfWeek(entity.warningDateDispAtr),
				// 申請理由
				EnumAdaptor.valueOf(entity.appReasonDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.appContentChangeFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.scheReflectFlg,ReflectionFlg.class),
				EnumAdaptor.valueOf(entity.priorityTimeReflectFlg,PriorityFLg.class),
				EnumAdaptor.valueOf(entity.attendentTimeReflectFlg,ReflectionFlg.class));
	}

	private AppReflectAfterConfirm toDomainRef(KrqstApplicationSetting entity){
		return new AppReflectAfterConfirm(EnumAdaptor.valueOf(entity.scheduleConfirmedAtr, ReflectAtr.class), 
											EnumAdaptor.valueOf(entity.achievementConfirmedAtr, ReflectAtr.class));
	}
	
	/**
	 * @param domain
	 * @return
	 */
	private KrqstApplicationSetting toEntity(ApplicationSetting domain, AppReflectAfterConfirm domainRef) {
		val entity = new KrqstApplicationSetting();
		entity.krqstApplicationSettingPK = new KrqstApplicationSettingPK();
		entity.krqstApplicationSettingPK.companyID = domain.getCompanyID();
		entity.appActLockFlg = domain.getAppActLockFlg().value;
		entity.appEndWorkFlg = domain.getAppEndWorkFlg().value;
		entity.appActConfirmFlg = domain.getAppActConfirmFlg().value;
		entity.appOvertimeNightFlg = domain.getAppOvertimeNightFlg().value;
		entity.appActMonthConfirmFlg = domain.getAppActMonthConfirmFlg().value;
		entity.requireAppReasonFlg = domain.getRequireAppReasonFlg().value;
		entity.displayPrePostFlg = domain.getDisplayPrePostFlg().value;
		entity.displaySearchTimeFlg = domain.getDisplaySearchTimeFlg().value;
		entity.manualSendMailAtr = domain.getManualSendMailAtr().value;
		/*承認*/
		entity.baseDateFlg = domain.getBaseDateFlg().value;
		entity.advanceExcessMessDispAtr = domain.getAdvanceExcessMessDispAtr().value;
		entity.hwAdvanceDispAtr = domain.getHwAdvanceDispAtr().value;
		entity.hwActualDispAtr = domain.getHwActualDispAtr().value;
		entity.actualExcessMessDispAtr = domain.getActualExcessMessDispAtr().value;
		entity.otAdvanceDispAtr = domain.getOtAdvanceDispAtr().value;
		entity.otActualDispAtr = domain.getOtActualDispAtr().value;
		entity.warningDateDispAtr = domain.getWarningDateDispAtr().v();
		entity.appReasonDispAtr = domain.getAppReasonDispAtr().value;
		entity.appContentChangeFlg = domain.getAppContentChangeFlg().value;
		entity.scheReflectFlg = domain.getScheReflectFlg().value;
		entity.priorityTimeReflectFlg = domain.getPriorityTimeReflectFlg().value;
		entity.attendentTimeReflectFlg = domain.getAttendentTimeReflectFlg().value;
		entity.achievementConfirmedAtr = domainRef.getAchievementConfirmedAtr().value;
		entity.scheduleConfirmedAtr = domainRef.getScheduleConfirmedAtr().value;
		return entity;
	}
	

//	@Override
//	public List<ApplicationSetting> getApplicationSettingByCompany(String companyID) {
//		return this.queryProxy().query(SELECT_WITH_CID, KrqstApplicationSetting.class)
//				.setParameter("companyID", companyID).getList(c -> toDomain(c));
//	}


	@Override
	public void updateSingle(ApplicationSetting applicationSetting, AppReflectAfterConfirm appReflectAfterConfirm) {
		this.commandProxy().update(toEntity(applicationSetting, appReflectAfterConfirm));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.request.dom.setting.request.application.
	 * ApplicationSettingRepository#updateList(java.util.List)
	 */
	@Override
	public void updateList(List<ApplicationSetting> lstApplicationSetting, AppReflectAfterConfirm appReflectAfterConfirm) {
		List<KrqstApplicationSetting> lstEntity = new ArrayList<>();
		for (ApplicationSetting applicationSetting : lstApplicationSetting) {
			lstEntity.add(toEntity(applicationSetting, appReflectAfterConfirm));
		}
		;
		this.commandProxy().updateAll(lstEntity);
	}

	@Override
	public Optional<ApplicationSetting> getApplicationSettingByComID(String companyID) {
		 Optional<ApplicationSetting> op = this.queryProxy().find(new KrqstApplicationSettingPK(companyID), KrqstApplicationSetting.class).map(x-> toDomain(x));
		 return op;
	}
	/**
	 * insert a application setting
	 * @author yennth
	 */
	@Override
	public void insert(ApplicationSetting applicationSetting, AppReflectAfterConfirm appReflectAfterConfirm) {
		KrqstApplicationSetting appSet = toEntity(applicationSetting, appReflectAfterConfirm);
		this.commandProxy().insert(appSet);
	}

	@Override
	public Optional<AppReflectAfterConfirm> getAppRef() {
		String companyId = AppContexts.user().companyId();
		Optional<AppReflectAfterConfirm> ref = this.queryProxy().find(new KrqstApplicationSettingPK(companyId), KrqstApplicationSetting.class)
																.map(x -> toDomainRef(x));
		return ref;
	}

}
