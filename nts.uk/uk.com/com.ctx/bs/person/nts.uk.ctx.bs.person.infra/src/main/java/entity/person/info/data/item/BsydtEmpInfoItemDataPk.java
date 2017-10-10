package entity.person.info.data.item;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BsydtEmpInfoItemDataPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "PER_INFO_DEF_ID")
	public String perInfoDefId;

	@Basic(optional = false)
	@Column(name = "RECORD_ID")
	public String recordId;

}
