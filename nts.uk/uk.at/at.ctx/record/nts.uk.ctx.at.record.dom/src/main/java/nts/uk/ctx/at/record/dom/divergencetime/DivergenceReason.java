package nts.uk.ctx.at.record.dom.divergencetime;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

@Getter
public class DivergenceReason extends AggregateRoot {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離理由コード*/
	private DiverdenceReasonCode divReasonCode;
	/*乖離理由*/
	private DivergenceReasonContent divReasonContent;
	/*必須区分*/
	private UseSetting requiredAtr;

	public DivergenceReason(String companyId, int divTimeId, DiverdenceReasonCode divReasonCode,
			DivergenceReasonContent divReasonContent, UseSetting requiredAtr) {
		super();
		this.companyId = companyId;
		this.divTimeId = divTimeId;
		this.divReasonCode = divReasonCode;
		this.divReasonContent = divReasonContent;
		this.requiredAtr = requiredAtr;
	}
	public static DivergenceReason createSimpleFromJavaType(
			String companyId,
			int divTimeId,
			String divReasonCode,
			String divReasonContent,
			int requiredAtr)
	{
		return new DivergenceReason(
				companyId,
				divTimeId,
				new DiverdenceReasonCode(divReasonCode),
				new DivergenceReasonContent(divReasonContent),
				EnumAdaptor.valueOf(requiredAtr, UseSetting.class));
				
	}
}
