/**
 * 
 */
package find.groupitem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import find.person.info.item.PerInfoItemDefDto;
import find.person.info.item.PerInfoItemDefFinder;
import nts.uk.ctx.bs.person.dom.person.groupitem.IPersonInfoItemGroupRepository;
import nts.uk.ctx.bs.person.dom.person.groupitem.PersonInfoItemGroup;

@Stateless
public class PersonInfoItemGroupFinder {

	@Inject
	private IPersonInfoItemGroupRepository repo;

	@Inject
	private PerInfoItemDefFinder itemDfFinder;

	/**
	 * Get All GroupItem
	 * 
	 * @return
	 */
	public List<PersonInfoItemGroupDto> getAllPersonInfoGroup() {
		List<PersonInfoItemGroupDto> list = this.repo.getAll().stream()
				.map(item -> PersonInfoItemGroupDto.fromDomain(item)).collect(Collectors.toList());
		return list;
	}

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public PersonInfoItemGroupDto getById(String groupId) {
		Optional<PersonInfoItemGroup> groupItem = this.repo.getById(groupId);

		if (groupItem.isPresent()) {
			PersonInfoItemGroup _groupItem = groupItem.get();
			// get classifications

			return PersonInfoItemGroupDto.fromDomain(_groupItem);
		} else {
			return null;
		}

	}

	/**
	 * Get All Item Difination
	 * 
	 * @return
	 */
	public List<PerInfoItemDefDto> getAllItemDf(String groupId) {
		List<String> listItemDfId = this.repo.getListItemIdByGrId(groupId);
		if (!listItemDfId.isEmpty()) {
			List<PerInfoItemDefDto> listItemDef = itemDfFinder.getPerInfoItemDefByListId(listItemDfId);
			return listItemDef;
		} else {
			return null;
		}

	}
}
