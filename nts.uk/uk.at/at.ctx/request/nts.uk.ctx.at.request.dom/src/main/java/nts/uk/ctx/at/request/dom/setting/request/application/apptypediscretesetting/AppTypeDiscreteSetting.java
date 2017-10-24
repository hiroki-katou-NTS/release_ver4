package nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting;

import javax.enterprise.inject.Model;

import lombok.Value;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.primitive.UseAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AllowAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.CheckMethod;
import nts.uk.ctx.at.request.dom.setting.request.application.common.PossibleAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RetrictDay;
import nts.uk.ctx.at.request.dom.setting.request.application.common.RetrictPreTimeDay;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.InitValueAtr;

/**
 * 申請種類別設定
 * @author ducpm
 *
 */
@Model
@Value
public class AppTypeDiscreteSetting {
	/**
	 * 会社ID
	 */
	private String companyID;
	/**
	 * 申請種類
	 */
	private ApplicationType appType;
	/**
	 * 事前事後区分の初期表示
	 */
	private InitValueAtr prePostInitFlg;
	/**
	 * 事前事後区分を変更できる
	 */
	private AppCanAtr prePostCanChangeFlg;
	/**
	 * 定型理由の表示
	 */
	private AppDisplayAtr typicalReasonDisplayFlg;
	/**
	 * 承認処理時に自動でメールを送信する
	 */
	private AppCanAtr sendMailWhenApprovalFlg;
	/**
	 * 新規登録時に自動でメールを送信する
	 */
	private AppCanAtr sendMailWhenRegisterFlg;
	/**
	 * 申請理由の表示
	 */
	private AppDisplayAtr displayReasonFlg;
	/**
	 * チェック方法
	 */
	private CheckMethod retrictPreMethodFlg;
	/**
	 * 利用する
	 */
	private UseAtr retrictPreUseFlg;
	/**
	 * 日数
	 */
	private RetrictDay retrictPreDay;
	/**
	 * 時刻
	 */
	private RetrictPreTimeDay retrictPreTimeDay;
	/**
	 * 受付可能か
	 */
	private PossibleAtr retrictPreCanAceeptFlg;
	/**
	 * 未来日許可しない
	 */
	private AllowAtr retrictPostAllowFutureFlg;

	public AppTypeDiscreteSetting(String companyID, ApplicationType appType, InitValueAtr prePostInitFlg,
			AppCanAtr prePostCanChangeFlg, AppDisplayAtr typicalReasonDisplayFlg, AppCanAtr sendMailWhenApprovalFlg,
			AppCanAtr sendMailWhenRegisterFlg, AppDisplayAtr displayReasonFlg, CheckMethod retrictPreMethodFlg,
			UseAtr retrictPreUseFlg, RetrictDay retrictPreDay, RetrictPreTimeDay retrictPreTimeDay,
			PossibleAtr retrictPreCanAceeptFlg, AllowAtr retrictPostAllowFutureFlg) {
		super();
		this.companyID = companyID;
		this.appType = appType;
		this.prePostInitFlg = prePostInitFlg;
		this.prePostCanChangeFlg = prePostCanChangeFlg;
		this.typicalReasonDisplayFlg = typicalReasonDisplayFlg;
		this.sendMailWhenApprovalFlg = sendMailWhenApprovalFlg;
		this.sendMailWhenRegisterFlg = sendMailWhenRegisterFlg;
		this.displayReasonFlg = displayReasonFlg;
		this.retrictPreMethodFlg = retrictPreMethodFlg;
		this.retrictPreUseFlg = retrictPreUseFlg;
		this.retrictPreDay = retrictPreDay;
		this.retrictPreTimeDay = retrictPreTimeDay;
		this.retrictPreCanAceeptFlg = retrictPreCanAceeptFlg;
		this.retrictPostAllowFutureFlg = retrictPostAllowFutureFlg;
	}
}
