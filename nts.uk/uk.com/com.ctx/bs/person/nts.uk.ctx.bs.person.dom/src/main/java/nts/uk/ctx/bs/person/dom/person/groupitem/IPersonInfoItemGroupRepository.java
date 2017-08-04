package nts.uk.ctx.bs.person.dom.person.groupitem;

import java.util.List;
import java.util.Optional;

public interface IPersonInfoItemGroupRepository {
	
	Optional<PersonInfoItemGroup> getById(String groupId);

	List<PersonInfoItemGroup> getAll();

}
