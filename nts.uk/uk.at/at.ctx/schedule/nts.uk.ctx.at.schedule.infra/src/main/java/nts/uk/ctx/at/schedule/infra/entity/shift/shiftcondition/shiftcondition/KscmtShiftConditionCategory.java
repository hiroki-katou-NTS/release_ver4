package nts.uk.ctx.at.schedule.infra.entity.shift.shiftcondition.shiftcondition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_SHIFT_CONDITION_CTG")
public class KscmtShiftConditionCategory extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public KscmtShiftConditionCategoryPk pk;
	@Column(name = "CATEGORY_NAME")
	public String categoryName;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
