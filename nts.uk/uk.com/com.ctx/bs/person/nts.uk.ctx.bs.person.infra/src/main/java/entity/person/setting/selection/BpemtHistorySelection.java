package entity.person.setting.selection;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BPSMT_HISTORY_SELECTION")
public class BpsmtHistorySelection extends UkJpaEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public BpsmtHistorySelectionPK histidPK;

	@Basic(optional = false)
	@Column(name = "SELECTION_ITEM_ID")
	public String selectionItemId;

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyCode;

	@Basic(optional = false)
	@Column(name = "START_DATE")
	public GeneralDate startDate;

	@Basic(optional = false)
	@Column(name = "END_DATE")
	public GeneralDate endDate;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return histidPK;
	}

}
