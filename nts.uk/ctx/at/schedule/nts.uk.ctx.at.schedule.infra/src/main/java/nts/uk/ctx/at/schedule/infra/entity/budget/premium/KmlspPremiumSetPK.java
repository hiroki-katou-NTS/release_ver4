package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KmlspPremiumSetPK {
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="HIS_ID")
	public String historyID;
	
	@Column(name="PREMIUM_ID")
	public Integer premiumID;
}
