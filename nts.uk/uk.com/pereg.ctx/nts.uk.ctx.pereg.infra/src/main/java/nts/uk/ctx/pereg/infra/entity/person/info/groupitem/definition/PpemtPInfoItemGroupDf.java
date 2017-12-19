/**
 * 
 */
package nts.uk.ctx.pereg.infra.entity.person.info.groupitem.definition;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PPEMT_PINFO_ITEM_DF_GROUP")
public class PpemtPInfoItemGroupDf extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public PpemtPInfoItemGroupDfPk ppemtPInfoItemGroupDfPk;

	@Basic(optional = false)
	@Column(name = "CID")
	public String companyId;


	@Override
	protected Object getKey() {
		return this.ppemtPInfoItemGroupDfPk;
	}
}
