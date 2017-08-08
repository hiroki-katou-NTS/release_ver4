package nts.uk.ctx.bs.person.dom.person.role.auth.item;

import java.util.List;
import java.util.Optional;

public interface PersonInfoItemAuthRepository {

	List<PersonInfoItemDetail> getAllItemDetail(String roleId, String personCategoryAuthId);

	Optional<PersonInfoItemAuth> getItemDetai(String roleId, String categoryId, String perInfoItemDefId);

	void add(PersonInfoItemAuth domain);

	void update(PersonInfoItemAuth domain);

	void delete(String roleId, String personCategoryAuthId, String personItemDefId);
}
