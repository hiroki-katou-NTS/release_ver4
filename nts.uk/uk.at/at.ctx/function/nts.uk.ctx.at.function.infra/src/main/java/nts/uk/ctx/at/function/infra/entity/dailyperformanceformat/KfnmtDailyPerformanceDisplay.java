package nts.uk.ctx.at.function.infra.entity.dailyperformanceformat;

import java.io.Serializable;

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
@Table(name = "KFNMT_DAI_PERFORMANCE_DIS")
public class KfnmtDailyPerformanceDisplay extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtDailyPerformanceDisplayPK kfnmtDailyPerformanceDisplayPK;
	
	@Override
	protected Object getKey() {
		return this.kfnmtDailyPerformanceDisplayPK;
	}
}
