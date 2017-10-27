package entity.person.info.item;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PPEMT_PER_INFO_ITEM_ORDER")
public class PpemtPerInfoItemOrder extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpemtPerInfoItemPK ppemtPerInfoItemPK;

	@Basic(optional = false)
	@NotNull
	@Column(name = "PER_INFO_CTG_ID")
	public String perInfoCtgId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "DISPORDER")
	public int disporder;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "DISPLAY_ORDER")
	public int displayOrder;
	
	@Override
	protected Object getKey() {
		return ppemtPerInfoItemPK;
	}

}
