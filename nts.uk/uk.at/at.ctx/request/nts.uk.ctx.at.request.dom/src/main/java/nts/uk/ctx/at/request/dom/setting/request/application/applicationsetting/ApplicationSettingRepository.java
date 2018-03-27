package nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.request.dom.setting.company.request.approvallistsetting.AppReflectAfterConfirm;

public interface ApplicationSettingRepository {

	/**
	 * @param companyId
	 * @param appType
	 * @return
	 */
	public Optional<ApplicationSetting> getApplicationSettingByComID(String companyID);
	/**
	 * 
	 * @return
	 */
	public Optional<AppReflectAfterConfirm> getAppRef();

	/**
	 * @param applicationSetting
	 */
	void updateSingle(ApplicationSetting applicationSetting, AppReflectAfterConfirm appReflectAfterConfirm);

	/**
	 * insert application setting
	 * @param applicationSetting
	 * @author yennth
	 */
	void insert(ApplicationSetting applicationSetting, AppReflectAfterConfirm appReflectAfterConfirm);

	/**
	 * @param lstApplicationSetting
	 */
	void updateList(List<ApplicationSetting> lstApplicationSetting, AppReflectAfterConfirm appReflectAfterConfirm);

}
