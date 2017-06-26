package nts.uk.ctx.at.record.app.find.divergencetime;

import lombok.Value;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReason;

@Value
public class DivergenceReasonDto {
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
	
	public static DivergenceReasonDto fromDomain(DivergenceReason domain){
		return new DivergenceReasonDto(domain.getCompanyId(),
					domain.getDivTimeId(),
					domain.getDivReasonCode().v(),
					domain.getDivReasonContent().toString(),
					domain.getRequiredAtr().value
					);
	}
}
