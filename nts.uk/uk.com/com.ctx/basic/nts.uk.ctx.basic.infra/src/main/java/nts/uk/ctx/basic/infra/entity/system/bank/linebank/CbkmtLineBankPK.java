package nts.uk.ctx.basic.infra.entity.system.bank.linebank;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CbkmtLineBankPK {
	@Column(name="CCD")
	public String companyCode;
	@Column(name="LINE_BANK_CD")
	public String lineBankCode;
}
