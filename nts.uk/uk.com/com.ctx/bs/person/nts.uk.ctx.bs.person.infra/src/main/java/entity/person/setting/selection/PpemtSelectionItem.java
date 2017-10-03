package entity.person.setting.selection;

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
@Table(name = "PPEMT_SELECTION_ITEM")
public class PpemtSelectionItem extends UkJpaEntity implements Serializable {

	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpemtSelectionItemPK selectionItemPk;

	@Basic(optional = false)
	@Column(name = "SELECTION_ITEM_NAME")
	public String selectionItemName;

	@Basic(optional = false)
	@Column(name = "CONTRACT_CD")
	public String contractCd;

	@Basic(optional = true)
	@Column(name = "INTEGRATION_CD")
	public String integrationCd;

	@Basic(optional = false)
	@Column(name = "SELECTION_ITEM_CLS_ATR")
	public int selectionItemClsAtr;

	@Basic(optional = false)
	@Column(name = "SELECTION_CD_LENGTH")
	public int selectionCd;

	@Basic(optional = false)
	@Column(name = "SELECTION_NAME_LENGTH")
	public int selectionName;

	@Basic(optional = false)
	@Column(name = "SELECTION_EXT_CD_LENGTH")
	public int selectionExtCd;

	@Basic(optional = false)
	@Column(name = "CHARACTER_TYPE_ATR")
	public int characterTypeAtr;

	@Basic(optional = true)
	@Column(name = "MEMO")
	public String memo;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return selectionItemPk;
	}

}
