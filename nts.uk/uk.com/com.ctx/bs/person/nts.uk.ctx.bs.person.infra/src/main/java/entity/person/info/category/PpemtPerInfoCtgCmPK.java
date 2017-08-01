package entity.person.info.category;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Embeddable
public class PpemtPerInfoCtgCmPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "CONTRACT_CD")
	public String contractCd;

	@Basic(optional = false)
	@Column(name = "CATEGORY_CD")
	public String categoryCd;

}
