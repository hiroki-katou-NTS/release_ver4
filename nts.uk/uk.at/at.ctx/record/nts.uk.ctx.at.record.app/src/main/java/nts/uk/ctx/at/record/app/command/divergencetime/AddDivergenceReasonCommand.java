package nts.uk.ctx.at.record.app.command.divergencetime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDivergenceReasonCommand {
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
