package nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.workplace;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetWkpRegAggr;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetWkpRegAggrPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.regularandirregular.KrcstMonsetIrgExot;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：職場の変形労働時間勤務の時間外超過設定
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCST_MONSET_WKP_IRG_EXOT")
@NoArgsConstructor
@AllArgsConstructor
public class KrcstMonsetWkpIrgExot extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcstMonsetWkpRegAggrPK PK;

	/** プライマリキー */
	@Embedded
	public KrcstMonsetIrgExot setValue;
	
	/** マッチング：職場月別実績集計設定 */
	@OneToOne
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "KRCST_MONSET_WKP_REG_AGGR.CID", insertable = false, updatable = false),
		@JoinColumn(name = "WKPID", referencedColumnName = "KRCST_MONSET_WKP_REG_AGGR.WKPID", insertable = false, updatable = false)
	})
	public KrcstMonsetWkpRegAggr krcstMonsetWkpRegAggr;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {
		return this.PK;
	}
}
