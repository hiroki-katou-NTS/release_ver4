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
@Table(name = "KFNMT_AUT_MONTHLY_ITEM")
public class KfnmtAuthorityMonthlyItem extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtAuthorityMonthlyItemPK kfnmtAuthorityMonthlyItemPK;

	@Column(name = "DAILY_PERFORMANCE_FORMAT_NAME")
	public String dailyPerformanceFormatName;
	
	@Column(name = "DISPLAY_ORDER")
	public int displayOrder;

	@Column(name = "COLUMN_WIDTH")
	public BigDecimal columnWidth;

	@Override
	protected Object getKey() {
		return this.kfnmtAuthorityMonthlyItemPK;
	}

}
