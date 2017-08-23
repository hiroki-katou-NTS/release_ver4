package nts.uk.ctx.at.request.infra.repository.setting.request.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AprovalPersonFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.NumDaysOfWeek;
import nts.uk.ctx.at.request.dom.setting.request.application.common.PriorityFLg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.ReflectionFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RequiredFlg;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RetrictDay;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstApplicationSetting;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstApplicationSettingPK;

@Stateless
public class JpaApplicationSettingRepository extends JpaRepository implements ApplicationSettingRepository {

	public final String SELECT_NO_WHERE = "SELECT c FROM KrqstApplicationSetting c";

	public final String SELECT_WITH_CID = SELECT_NO_WHERE + " WHERE c.KrqstApplicationSettingPK.companyID := companyID";

	public final String SELECT_WITH_APP_TYPE = SELECT_NO_WHERE
			+ " WHERE c.KrqstApplicationSettingPK.companyID := companyID"
			+ " AND c.KrqstApplicationSettingPK.appType := appType ";

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
				EnumAdaptor.valueOf(entity.displayInitDayFlg,RetrictDay.class),
				/*承認*/
				EnumAdaptor.valueOf(entity.baseDateFlg,BaseDateFlg.class),
				EnumAdaptor.valueOf(entity.advanceExcessMessDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.hwAdvanceDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.hwActualDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.actualExcessMessDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.otAdvanceDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.otActualDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.warningDateDispAtr,NumDaysOfWeek.class),
				EnumAdaptor.valueOf(entity.appReasonDispAtr,AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.appContentChangeFlg,AppCanAtr.class),
				EnumAdaptor.valueOf(entity.personApprovalFlg,AprovalPersonFlg.class),
				EnumAdaptor.valueOf(entity.scheReflectFlg,ReflectionFlg.class),
				EnumAdaptor.valueOf(entity.priorityTimeReflectFlg,PriorityFLg.class),
				EnumAdaptor.valueOf(entity.attendentTimeReflectFlg,ReflectionFlg.class));
	}

	/**
	 * @param domain
	 * @return
	 */
	private KrqstApplicationSetting toEntity(ApplicationSetting domain) {
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
		entity.displayInitDayFlg = domain.getDisplayInitDayFlg().value;
		/*承認*/
		entity.baseDateFlg = domain.getBaseDateFlg().value;
		entity.advanceExcessMessDispAtr = domain.getAdvanceExcessMessDispAtr().value;
		entity.hwAdvanceDispAtr = domain.getHwActualDispAtr().value;
		entity.actualExcessMessDispAtr = domain.getActualExcessMessDispAtr().value;
		entity.otAdvanceDispAtr = domain.getOtAdvanceDispAtr().value;
		entity.otActualDispAtr = domain.getOtActualDispAtr().value;
		entity.warningDateDispAtr = domain.getWarningDateDispAtr().v();
		entity.appReasonDispAtr = domain.getAppReasonDispAtr().value;
		entity.appContentChangeFlg = domain.getAppContentChangeFlg().value;
		entity.personApprovalFlg = domain.getPersonApprovalFlg().value;
		entity.scheReflectFlg = domain.getScheReflectFlg().value;
		entity.priorityTimeReflectFlg = domain.getPriorityTimeReflectFlg().value;
		entity.attendentTimeReflectFlg = domain.getAttendentTimeReflectFlg().value;
		return entity;
	}

//	@Override
//	public Optional<ApplicationSetting> getApplicationSettingByAppType(String companyID, int appType) {
//		return this.queryProxy().query(SELECT_WITH_APP_TYPE, KrqstApplicationSetting.class)
//				.setParameter("companyID", companyID).setParameter("appID", appType).getSingle(c -> toDomain(c));
//	}
//
//	@Override
//	public List<ApplicationSetting> getApplicationSettingByCompany(String companyID) {
//		return this.queryProxy().query(SELECT_WITH_CID, KrqstApplicationSetting.class)
//				.setParameter("companyID", companyID).getList(c -> toDomain(c));
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.request.dom.setting.request.application.
	 * ApplicationSettingRepository#update(nts.uk.ctx.at.request.dom.setting.request
	 * .application.ApplicationSetting)
	 */
	@Override
	public void updateSingle(ApplicationSetting applicationSetting) {
		this.commandProxy().update(toEntity(applicationSetting));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.request.dom.setting.request.application.
	 * ApplicationSettingRepository#updateList(java.util.List)
	 */
	@Override
	public void updateList(List<ApplicationSetting> lstApplicationSetting) {
		List<KrqstApplicationSetting> lstEntity = new ArrayList<>();
		for (ApplicationSetting applicationSetting : lstApplicationSetting) {
			lstEntity.add(toEntity(applicationSetting));
		}
		;
		this.commandProxy().updateAll(lstEntity);
	}

	@Override
	public Optional<ApplicationSetting> getApplicationSettingByComID(String companyID) {
		// TODO Auto-generated method stub
		return null;
	}
}
