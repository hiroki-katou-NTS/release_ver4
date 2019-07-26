package nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon;

import java.util.Optional;

import nts.uk.ctx.at.request.dom.application.UseAtr;

public interface GoBackDirectlyCommonSettingRepository {
	/***/
	/**
	 * @param companyID
	 * @param appID
	 * @return
	 */
	public Optional<GoBackDirectlyCommonSetting> findByCompanyID(String companyID);
	/**
	 * @param goBackDirectlyCommonSettingItem
	 */
	public void insert(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem);
	
	/**
	 * @param goBackDirectlyCommonSettingItem
	 */
	public void update(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem);
	
	/**
	 * @param goBackDirectlyCommonSettingItem
	 */
	public void delete(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem);
	
	Optional<UseAtr> getWorkChangeTimeAtr(String cid);
}
