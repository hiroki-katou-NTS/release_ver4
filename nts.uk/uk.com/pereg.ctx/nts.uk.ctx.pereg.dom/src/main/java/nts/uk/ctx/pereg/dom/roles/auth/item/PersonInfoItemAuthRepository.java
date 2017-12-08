package nts.uk.ctx.pereg.dom.roles.auth.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PersonInfoItemAuthRepository {

	List<PersonInfoItemDetail> getAllItemDetail(String roleId, String personInfoCategoryAuthId,String contractCd);
	
	List<PersonInfoItemAuth> getAllItemAuth(String roleId, String categoryId);
	
	Map<String, List<PersonInfoItemAuth>> getByRoleIdAndCategories(String roleId, List<String> categoryIdList);

	Optional<PersonInfoItemAuth> getItemDetai(String roleId, String categoryId, String perInfoItemDefId);

	void add(PersonInfoItemAuth domain);

	void update(PersonInfoItemAuth domain);

	void delete(String roleId, String personCategoryAuthId, String personItemDefId);
	
	void deleteByRoleId(String roleId);
	
	
}
