package nts.uk.ctx.at.request.dom.setting.company.request;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.AppTypeSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting.ReceptionRestrictionSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.appreflect.AppReflectionSetting;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface RequestSettingRepository {
	/**
	 * ドメインモデル「承認一覧表示設定」を取得する
	 * @param companyID
	 * @return
	 */
	public Optional<RequestSetting> findByCompany(String companyID);
	/**
	 * update before and after hand restriction
	 * @param req
	 */
	public void update(List<ReceptionRestrictionSetting> receiption, List<AppTypeSetting> appType);
	
	public Optional<AppReflectionSetting> getAppReflectionSetting(String cid);
}
