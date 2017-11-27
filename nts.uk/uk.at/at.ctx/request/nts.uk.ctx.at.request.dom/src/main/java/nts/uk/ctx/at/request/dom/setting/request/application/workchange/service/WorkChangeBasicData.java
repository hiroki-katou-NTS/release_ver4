package nts.uk.ctx.at.request.dom.setting.request.application.workchange.service;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReason;
import nts.uk.ctx.at.request.dom.setting.request.application.workchange.AppWorkChangeSet;

/**
 * 勤務変更申請基本データ
 */
@Getter
@Setter
public class WorkChangeBasicData {
	
	/**
	 * ドメインモデル「勤務変更申請設定」より取得する
	 */
	private Optional<AppWorkChangeSet> workChangeCommonSetting;
	/**
	 * 申請者名
	 */
	private String employeeName;
	/**
	 * 申請者社員ID
	 */
	private String sID;
	/**
	 * 定型理由のリストにセットするため
	 */
	private List<ApplicationReason> listAppReason;
	/**
	 * 申請共通設定
	 */
	private AppCommonSettingOutput appCommonSettingOutput;	
}
