package find.roles.auth.item;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemAuthRepository;

/**
 * The Class PersonInfoItemAuthFinder
 * 
 * @author lanlt
 *
 */
@Stateless
public class PersonInfoItemAuthFinder {
	@Inject
	private PersonInfoItemAuthRepository personItemAuthRepository;

	public List<PersonInfoItemDetailDto> getAllItemDetail(String roleId, String personCategoryAuthId) {
		return this.personItemAuthRepository.getAllItemDetail(roleId, personCategoryAuthId).stream()
				.map(item -> PersonInfoItemDetailDto.fromDomain(item)).collect(Collectors.toList());
	}

}
