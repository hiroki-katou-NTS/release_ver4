package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class ClassificationAllotSettingHeader extends AggregateRoot {
	@Getter
	private final CompanyCode companyCode;

	@Getter
	private String historyId;

	@Getter
	private BigDecimal startDateYM;

	@Getter
	private BigDecimal endDateYM;

	public ClassificationAllotSettingHeader(CompanyCode companyCode, String historyId, BigDecimal startDateYM,
			BigDecimal endDateYM) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startDateYM = startDateYM;
		this.endDateYM = endDateYM;
	}
	public static ClassificationAllotSettingHeader createFromJavaType (String companyCode, String historyId , BigDecimal startDateYM , BigDecimal endDateYM){
		return new ClassificationAllotSettingHeader (
				new CompanyCode(companyCode),
				historyId,
				startDateYM,
				endDateYM);
	}
}
