package nts.uk.ctx.sys.assist.infra.entity.deletedata.manualsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.assist.dom.deletedata.manualsetting.CategoryDeletion;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "SSPDT_CATEGORY_DELETION")
@NoArgsConstructor
@AllArgsConstructor
public class SspdtCategoryDeletion extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    public SspdtCategoryDeletionPK sspdtCategoryDeletionPK;
	
	/** The period deletion. */
	/** 自動設定対象期間 */
	@Column(name = "PERIOD_DELETION")
	public GeneralDate periodDeletion;
	
	@Override
	protected Object getKey() {
		return sspdtCategoryDeletionPK;
	}

	public CategoryDeletion toDomain() {
		return CategoryDeletion.createFromJavatype(this.sspdtCategoryDeletionPK.delId, 
				this.sspdtCategoryDeletionPK.categoryId, this.periodDeletion);
	}

	public static SspdtCategoryDeletion toEntity(CategoryDeletion categoryDeletion) {
		return new SspdtCategoryDeletion(new SspdtCategoryDeletionPK(
				categoryDeletion.getDelId(), categoryDeletion.getCategoryId()),
				categoryDeletion.getPeriodDeletion());
	}
}
