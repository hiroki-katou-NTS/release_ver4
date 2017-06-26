package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.input;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PprmtPersonResiTaxPK {
	@Column(name="CCD")
	public String companyCode;
	@Column(name="PID")
	public String personId;
	@Column(name="Y_K")
	public int yearKey;
}
