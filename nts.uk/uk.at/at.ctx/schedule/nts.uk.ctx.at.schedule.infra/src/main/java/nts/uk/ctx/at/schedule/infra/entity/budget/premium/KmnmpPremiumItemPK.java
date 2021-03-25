package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KmnmpPremiumItemPK {
	@Column(name="CID")
	public String companyID;
	
	@Column(name="PREMIUM_NO")
	public Integer displayNumber;
}
