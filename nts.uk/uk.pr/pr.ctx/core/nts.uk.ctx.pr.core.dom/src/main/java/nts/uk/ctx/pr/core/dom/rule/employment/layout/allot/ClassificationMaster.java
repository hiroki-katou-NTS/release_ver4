package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.shr.com.primitive.Memo;

public class ClassificationMaster extends AggregateRoot {

	@Getter
	private final CompanyCode companyCode;

	@Getter
	private ClassificationCode classificationCode;

	@Getter
	private ClassificationName classificationName;

	@Getter
	private ClassificationOutCode classificationOutCode;

	@Getter
	private Memo memo;

	public ClassificationMaster(CompanyCode companyCode, ClassificationCode classificationCode, ClassificationName classificationName,
			ClassificationOutCode classificationOutCode, Memo memo) {
		super();
		this.companyCode = companyCode;
		this.classificationCode = classificationCode;
		this.classificationName = classificationName;
		this.classificationOutCode = classificationOutCode;
		this.memo = memo;
	}

	public static ClassificationMaster createFromJavaType(String companyCode, String classificationCode,
			String classificationName, String classificationOutCode, Memo memo) {
		return new ClassificationMaster(new CompanyCode(companyCode), new ClassificationCode(classificationCode),
				new ClassificationName(classificationName), new ClassificationOutCode(classificationOutCode), memo);
	}

}
