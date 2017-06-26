package nts.uk.ctx.basic.dom.organization.payclassification;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class PayClassification extends AggregateRoot {

	

	private Memo memo;

	private PayClassificationName payClassificationName;

	private PayClassificationCode payClassificationCode;

	private String companyCode;
	
	public PayClassification(Memo memo,
			PayClassificationName payClassificationName, PayClassificationCode payClassificationCode,
			String companyCode) {
		super();
	
		this.memo = memo;
		this.payClassificationName = payClassificationName;
		this.payClassificationCode = payClassificationCode;
		this.companyCode = companyCode;
	}

	

}