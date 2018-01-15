package nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.company;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetCmpRegAggr;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetCmpRegAggrPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.regularandirregular.KrcstMonsetIrgAggr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * エンティティ：会社の変形労働時間勤務の法定内集計設定
 * @author shuichi_ishida
 */
@Entity
@Table(name = "KRCST_MONSET_CMP_IRG_AGGR")
@NoArgsConstructor
public class KrcstMonsetCmpIrgAggr extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcstMonsetCmpRegAggrPK PK; 

	/** 設定値 */
	@Embedded
	public KrcstMonsetIrgAggr setValue; 
	
	/** マッチング：会社月別実績集計設定 */
	@OneToOne
	@JoinColumns({
    	@JoinColumn(name = "CID", referencedColumnName = "KRCST_MONSET_CMP_REG_AGGR.CID", insertable = false, updatable = false)
	})
	public KrcstMonsetCmpRegAggr krcstMonsetCmpRegAggr;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {
		return this.PK;
	}
}
