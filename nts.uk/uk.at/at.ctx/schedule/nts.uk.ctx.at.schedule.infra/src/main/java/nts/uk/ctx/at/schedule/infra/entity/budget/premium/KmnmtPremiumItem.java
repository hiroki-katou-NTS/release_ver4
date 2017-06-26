package nts.uk.ctx.at.schedule.infra.entity.budget.premium;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="KMNMT_PREMIUM_ITEM")
public class KmnmtPremiumItem extends UkJpaEntity{
	@EmbeddedId
	public KmnmpPremiumItemPK kmnmpPremiumItemPK;
	
	@Column(name="ATTENDANCE_ID")
	public Integer attendanceID;
	
	@Column(name="NAME")
	public String name;
	
	@Column(name="DISPLAY_NUMBER")
	public Integer displayNumber;
	
	@Column(name="USE_ATR")
	public int useAtr;

	@Override
	protected Object getKey() {
		return kmnmpPremiumItemPK;
	}
}
