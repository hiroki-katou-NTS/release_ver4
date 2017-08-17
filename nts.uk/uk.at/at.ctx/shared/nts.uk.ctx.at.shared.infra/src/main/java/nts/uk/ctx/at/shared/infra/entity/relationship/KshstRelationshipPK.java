package nts.uk.ctx.at.shared.infra.entity.relationship;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.query.DBCharPaddingAs;
import nts.uk.ctx.at.shared.dom.relationship.primitives.RelationshipCode;
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KshstRelationshipPK implements Serializable{
	private static final long serialVersionUID = 1L;
	/*会社ID*/
	@Column(name = "CID")
	public String companyId;
	/*コード*/
	@Column(name = "RELATIONSHIP_CD")
	@DBCharPaddingAs(RelationshipCode.class)
	public String relationshipCd;
	
}
