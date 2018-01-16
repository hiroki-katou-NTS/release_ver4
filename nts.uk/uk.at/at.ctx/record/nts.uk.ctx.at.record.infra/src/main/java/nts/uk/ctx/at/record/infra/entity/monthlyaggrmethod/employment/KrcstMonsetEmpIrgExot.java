package nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.employment;

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
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetEmpRegAggr;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetEmpRegAggrPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.regularandirregular.KrcstMonsetIrgExot;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：雇用の変形労働時間勤務の時間外超過設定
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCST_MONSET_EMP_IRG_EXOT")
@NoArgsConstructor
@AllArgsConstructor
public class KrcstMonsetEmpIrgExot extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcstMonsetEmpRegAggrPK PK;

	/** プライマリキー */
	@Embedded
	public KrcstMonsetIrgExot setValue;
	
	/** マッチング：雇用月別実績集計設定 */
	@OneToOne
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "KRCST_MONSET_EMP_REG_AGGR.CID", insertable = false, updatable = false),
		@JoinColumn(name = "EMP_CD", referencedColumnName = "KRCST_MONSET_EMP_REG_AGGR.EMP_CD", insertable = false, updatable = false)
	})
	public KrcstMonsetEmpRegAggr krcstMonsetEmpRegAggr;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {
		return this.PK;
	}
}
