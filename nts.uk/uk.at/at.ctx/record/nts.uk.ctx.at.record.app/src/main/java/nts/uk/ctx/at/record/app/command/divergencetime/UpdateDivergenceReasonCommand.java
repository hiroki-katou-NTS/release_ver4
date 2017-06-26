package nts.uk.ctx.at.record.app.command.divergencetime;

import lombok.Data;

@Data
public class UpdateDivergenceReasonCommand {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離理由コード*/
	private String divReasonCode;
	/*乖離理由*/
	private String divReasonContent;
	/*必須区分*/
	private int requiredAtr;
}
