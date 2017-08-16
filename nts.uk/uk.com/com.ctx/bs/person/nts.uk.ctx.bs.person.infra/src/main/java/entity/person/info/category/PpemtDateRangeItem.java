package entity.person.info.category;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "PPEMT_DATE_RANGE_ITEM")
public class PpemtDateRangeItem extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpemtPerInfoCtgPK ppemtPerInfoCtgPK;

	@Basic(optional = false)
	@Column(name = "START_DATE_ITEM_ID")
	public String startDateItemId;

	@Basic(optional = false)
	@Column(name = "END_DATE_ITEM_ID")
	public String endDateItemId;

	@Basic(optional = false)
	@Column(name = "DATE_RANGE_ITEM_ID")
	public String dateRangeItemId;

	@Override
	protected Object getKey() {
		return ppemtPerInfoCtgPK;
	}
}
