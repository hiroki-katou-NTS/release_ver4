package nts.uk.screen.at.app.ktgwidget.find.dto;

import lombok.Setter;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;

/**
 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG001_承認すべきデータ.ユースケース.起動する.起動する
 * @author tutt
 *
 */
@Setter
public class ApprovedDataWidgetStartDto {
	
	//承認すべきデータのウィジェットを起動する
	ApprovedDataExecutionResultDto approvedDataExecutionResultDto;
	
	//ドメインモデル「３６協定運用設定」を取得する
	
	//承認処理の利用設定を取得する
	ApprovalProcessingUseSetting approvalProcessingUseSetting;

}
