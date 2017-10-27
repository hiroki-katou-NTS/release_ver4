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
@Table(name = "PPEMT_PER_INFO_CTG")

public class PpemtPerInfoCtg extends UkJpaEntity implements Serializable {

	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public PpemtPerInfoCtgPK ppemtPerInfoCtgPK;

	@Basic(optional = false)
	@Column(name = "CID")
	public String cid;

	@Basic(optional = true)
	@Column(name = "CATEGORY_CD")
	public String categoryCd;

	@Basic(optional = false)
	@Column(name = "CATEGORY_NAME")
	public String categoryName;

	@Basic(optional = false)
	@Column(name = "ABOLITION_ATR")
	public int abolitionAtr;

	@Override
	protected Object getKey() {
		return ppemtPerInfoCtgPK;
	}
}
