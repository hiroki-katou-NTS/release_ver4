package nts.uk.ctx.at.shared.infra.entity.calculation.holiday;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSHST_FLEX_SET")
public class KshstFlexSet  extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 主キー */
	@EmbeddedId
	public KshstFlexSetPK kshstFlexSetPK;
	
	/** 不足計算 */
	@Column(name = "MISS_CALC_HD")
	public int missCalcHd;
	
	/** 割増計算 */
	@Column(name = "PREMIUM_CALC_HD")
	public int premiumCalcHd;
	
	/** 不足計算*/
	@Column(name = "MISS_CALC_SUBHD")
	public int missCalcSubhd;
	
	/** 割増計算 */
	@Column(name = "PREMIUM_CALC_SUBHD")
	public int premiumCalcSubhd;
	
	@Override
	protected Object getKey() {
		return kshstFlexSetPK;
	}
}
