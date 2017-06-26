package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QtxmtResimentTialTaxSlipPk {
	@Column(name="CCD")
	public String companyCode;
	
	@Column(name="RESI_TAX_CD")
	public String residentTaxCode;
	
	@Column(name="YM_K")
	public int yearMonth;
}
