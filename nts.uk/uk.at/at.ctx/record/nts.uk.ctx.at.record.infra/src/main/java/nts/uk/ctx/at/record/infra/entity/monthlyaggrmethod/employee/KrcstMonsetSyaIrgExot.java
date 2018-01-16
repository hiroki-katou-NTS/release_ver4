package nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.employee;

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
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetSyaRegAggr;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetSyaRegAggrPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.regularandirregular.KrcstMonsetIrgExot;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：社員の変形労働時間勤務の時間外超過設定
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCST_MONSET_SYA_IRG_EXOT")
@NoArgsConstructor
@AllArgsConstructor
public class KrcstMonsetSyaIrgExot extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcstMonsetSyaRegAggrPK PK;

	/** プライマリキー */
	@Embedded
	public KrcstMonsetIrgExot setValue;
	
	/** マッチング：社員月別実績集計設定 */
	@OneToOne
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "KRCST_MONSET_SYA_REG_AGGR.CID", insertable = false, updatable = false),
		@JoinColumn(name = "SID", referencedColumnName = "KRCST_MONSET_SYA_REG_AGGR.SID", insertable = false, updatable = false)
	})
	public KrcstMonsetSyaRegAggr krcstMonsetSyaRegAggr;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {
		return this.PK;
	}
}
