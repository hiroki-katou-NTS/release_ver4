package nts.uk.ctx.basic.app.find.organization.department;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationTreeDto<T> {
	
	protected List<T> children;

}
