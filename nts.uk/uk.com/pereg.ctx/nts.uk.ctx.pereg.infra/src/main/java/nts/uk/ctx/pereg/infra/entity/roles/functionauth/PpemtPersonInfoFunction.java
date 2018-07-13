package nts.uk.ctx.pereg.infra.entity.roles.functionauth;

import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.ctx.pereg.dom.roles.functionauth.authsettingdesc.PersonInfoAuthDescription;
import nts.uk.shr.infra.permit.data.JpaEntityOfDescriptionOfAvailabilityPermissionBase;

@Table(name = "PPEMT_PER_INFO_FUNCTION")
@Entity
public class PpemtPersonInfoFunction
		extends JpaEntityOfDescriptionOfAvailabilityPermissionBase<PersonInfoAuthDescription> {

	@Override
	public PersonInfoAuthDescription toDomain() {
		return new PersonInfoAuthDescription(this);
	}

}
