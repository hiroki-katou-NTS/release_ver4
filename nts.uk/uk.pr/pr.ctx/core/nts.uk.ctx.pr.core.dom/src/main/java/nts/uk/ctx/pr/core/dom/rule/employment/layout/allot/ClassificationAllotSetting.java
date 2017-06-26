package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;

public class ClassificationAllotSetting extends AggregateRoot {

	@Getter
	private final CompanyCode companyCode;

	@Getter
	private String histotyId;

	@Getter
	private ClassificationCode classificationCode;

	@Getter
	private PaymentDetailCode paymentDetailCode;

	@Getter
	private BonusDetailCode bonusDetailCode;

	public ClassificationAllotSetting(CompanyCode companyCode, String histotyId, ClassificationCode classificationCode,
			PaymentDetailCode paymentDetailCode, BonusDetailCode bonusDetailCode) {
		super();
		this.companyCode = companyCode;
		this.histotyId = histotyId;
		this.classificationCode = classificationCode;
		this.paymentDetailCode = paymentDetailCode;
		this.bonusDetailCode = bonusDetailCode;
	}

	public static ClassificationAllotSetting createFromJavaType(String companyCode, String historyId,
			String classificationCode, String bonusDetailCode, String paymentDetailCode) {
		return new ClassificationAllotSetting(new CompanyCode(companyCode), historyId,
				new ClassificationCode(classificationCode), new PaymentDetailCode(paymentDetailCode),
				new BonusDetailCode(bonusDetailCode));
	}

}
