package nts.uk.ctx.at.schedule.infra.entity.shift.schedulehorizontal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_TOTAL_EVAL_ORDER")
public class KscmtTotalEvalOrderItem extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KscmtTotalEvalOrderPK kscmtTotalEvalOrderPK;
	/** 並び順 */
	@Column(name = "DISPORDER")
	public Integer dispOrder;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "CID", referencedColumnName = "KSCMT_HORI_TOTAL_CATEGORY.CID", insertable = false, updatable = false),
			@JoinColumn(name = "CATEGORY_CD", referencedColumnName = "KSCMT_HORI_TOTAL_CATEGORY.CATEGORY_CD", insertable = false, updatable = false)
	})
	public KscmtHoriTotalCategoryItem kscmtHoriTotalCategory;
	
	@Override
	protected Object getKey() {
		return kscmtTotalEvalOrderPK;
	}
	
	public KscmtTotalEvalOrderItem(KscmtTotalEvalOrderPK kscmtTotalEvalOrderPK){
		super();
		this.kscmtTotalEvalOrderPK = kscmtTotalEvalOrderPK;
	}
}
