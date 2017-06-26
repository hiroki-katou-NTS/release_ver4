package nts.uk.ctx.pr.core.dom.paymentdata;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitCode;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitName;
import nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit.CommuNoTaxLimitValue;

public class CommuNotaxLimit extends AggregateRoot {
	
	@Getter
	private CompanyCode ccd;
	
	@Getter
	private ExclusiveVersion exclusiveVersion;
	
	@Getter
	private CommuNoTaxLimitCode commuNotaxLimitCode;
	
	@Getter
	private CommuNoTaxLimitName commuNotaxLimitName;
	
	@Getter
	private CommuNoTaxLimitValue commuNotaxLimitValue;
	
	public CommuNotaxLimit(CompanyCode ccd, ExclusiveVersion exclusiveVersion, CommuNoTaxLimitCode commuNotaxLimitCode, CommuNoTaxLimitName commuNotaxLimitName,
			CommuNoTaxLimitValue commuNotaxLimitValue) {
		super();
		this.ccd = ccd;
		this.exclusiveVersion = exclusiveVersion;
		this.commuNotaxLimitCode = commuNotaxLimitCode;
		this.commuNotaxLimitName = commuNotaxLimitName;
		this.commuNotaxLimitValue = commuNotaxLimitValue;
	}

	public static CommuNotaxLimit createFromJavaType(String ccd, int exclusiveVersion, String commuNotaxLimitCode, String commuNotaxLimitName,
			BigDecimal commuNotaxLimitValue) {

		return new CommuNotaxLimit(new CompanyCode(ccd),
									new ExclusiveVersion(exclusiveVersion),
									new CommuNoTaxLimitCode(commuNotaxLimitCode),
									new CommuNoTaxLimitName(commuNotaxLimitName),
									new CommuNoTaxLimitValue(commuNotaxLimitValue));
	}
}
