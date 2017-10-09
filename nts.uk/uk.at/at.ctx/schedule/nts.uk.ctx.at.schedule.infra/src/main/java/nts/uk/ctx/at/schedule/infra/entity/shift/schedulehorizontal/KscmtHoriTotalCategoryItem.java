package nts.uk.ctx.at.schedule.infra.entity.shift.schedulehorizontal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_HORI_TOTAL_CATEGORY")
public class KscmtHoriTotalCategoryItem extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KscmtHoriTotalCategoryPK kscmtHoriTotalCategoryPK;
	/** カテゴリ名称 */
	@Column(name = "CATEGORY_NAME")
	public String categoryName;
	/** メモ */
	@Column(name = "MEMO")
	public String memo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kscmtHoriTotalCategory", orphanRemoval = true)
	public List<KscmtTotalEvalOrderItem> listTotalEvalOrder;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "kscmtHoriTotalCategory1", orphanRemoval = true)
	public KscstHoriCalDaysSetItem horiCalDaysSet;
	
	@Override
	protected Object getKey() {
		return kscmtHoriTotalCategoryPK;
	}
	
	public KscmtHoriTotalCategoryItem(KscmtHoriTotalCategoryPK kscmtHoriTotalCategoryPK){
		super();
		this.kscmtHoriTotalCategoryPK = kscmtHoriTotalCategoryPK;
	}
}
