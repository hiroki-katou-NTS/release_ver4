package nts.uk.ctx.at.function.infra.entity.dailyperformanceformat;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KFNMT_AUT_DAILY_ITEM")
public class KfnmtAuthorityDailyItem extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtAuthorityDailyItemPK kfnmtAuthorityDailyItemPK;
	
	@Column(name = "DISPLAY_ORDER")
	public int displayOrder;

	@Column(name = "COLUMN_WIDTH")
	public BigDecimal columnWidth;

	@Override
	protected Object getKey() {
		return this.kfnmtAuthorityDailyItemPK;
	}

}
