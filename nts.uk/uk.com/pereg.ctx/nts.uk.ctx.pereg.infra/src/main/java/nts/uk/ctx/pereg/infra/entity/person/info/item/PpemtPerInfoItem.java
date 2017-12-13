package nts.uk.ctx.pereg.infra.entity.person.info.item;

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
@Table(name = "PPEMT_PER_INFO_ITEM")
public class PpemtPerInfoItem extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpemtPerInfoItemPK ppemtPerInfoItemPK;

	@Basic(optional = false)
	@Column(name = "PER_INFO_CTG_ID")
	public String perInfoCtgId;

	@Basic(optional = false)
	@Column(name = "ITEM_CD")
	public String itemCd;

	@Basic(optional = false)
	@Column(name = "ITEM_NAME")
	public String itemName;

	@Basic(optional = false)
	@Column(name = "ABOLITION_ATR")
	public int abolitionAtr;

	@Basic(optional = false)
	@Column(name = "REQUIRED_ATR")
	public int requiredAtr;

	@Override
	protected Object getKey() {
		return ppemtPerInfoItemPK;
	}

}
