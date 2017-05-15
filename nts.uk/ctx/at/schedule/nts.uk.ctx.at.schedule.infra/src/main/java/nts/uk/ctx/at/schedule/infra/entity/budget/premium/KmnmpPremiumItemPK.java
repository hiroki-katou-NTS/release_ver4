package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KmnmpPremiumItemPK {
	@Column(name="CID")
	public String companyID;
	
	@Column(name="ID")
	public BigDecimal iD;
}
