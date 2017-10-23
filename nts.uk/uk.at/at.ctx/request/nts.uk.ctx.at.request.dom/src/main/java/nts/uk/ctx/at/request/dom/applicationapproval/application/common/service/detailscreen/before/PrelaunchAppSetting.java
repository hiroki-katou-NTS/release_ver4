package nts.uk.ctx.at.request.dom.applicationapproval.application.common.service.detailscreen.before;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.applicationapproval.application.ApplicationType;
import nts.uk.ctx.at.request.dom.applicationapproval.setting.request.application.applicationsetting.ApplicationSetting;

/**
 * 
 * @author hieult
 *
 */
@Value
@AllArgsConstructor
public class PrelaunchAppSetting {

	/** 申請共通設定 */
	ApplicationSetting appCommonSetting;

	/** 基準日 */
	GeneralDate cacheDate;

}
