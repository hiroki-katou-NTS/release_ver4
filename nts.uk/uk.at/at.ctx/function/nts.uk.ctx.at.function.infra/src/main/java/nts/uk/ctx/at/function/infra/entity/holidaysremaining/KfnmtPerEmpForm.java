package nts.uk.ctx.at.function.infra.entity.holidaysremaining;

import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.ctx.at.function.dom.holidaysremaining.PermissionOfEmploymentForm;
import nts.uk.shr.infra.permit.data.JpaEntityOfAvailabilityPermissionBase;

@Entity
@Table(name = "KFNMT_PER_EMP_FORM")
public class KfnmtPerEmpForm extends JpaEntityOfAvailabilityPermissionBase<PermissionOfEmploymentForm> {

	@Override
	public PermissionOfEmploymentForm toDomain() {
		return new PermissionOfEmploymentForm(this);
	}

}
 