package repository.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import entity.roles.auth.item.PpemtPersonItemAuth;
import entity.roles.auth.item.PpemtPersonItemAuthPk;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemAuth;
import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemAuthRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemDetail;

@Stateless
public class JpaPersonInfoItemAuthRepository extends JpaRepository implements PersonInfoItemAuthRepository {

	private final String SELECT_ITEM_INFO_AUTH_BY_CATEGORY_ID_QUERY = " SELECT DISTINCT p.ppemtPersonItemAuthPk.roleId, p.ppemtPersonItemAuthPk.personInfoCategoryAuthId,"
			+ " i.ppemtPerInfoItemPK.perInfoItemDefId,"
			+ " p.selfAuthType, p.otherPersonAuth, i.itemCd, i.itemName, i.abolitionAtr, i.requiredAtr,"
			+ " CASE WHEN p.ppemtPersonItemAuthPk.personItemDefId IS NULL THEN 'False' ELSE 'True' END AS IsConfig,"
			+ " im.itemParentCd" + " FROM PpemtPerInfoItem i"
			+ " INNER JOIN PpemtPerInfoCtg c ON i.perInfoCtgId = c.ppemtPerInfoCtgPK.perInfoCtgId"
			+ " INNER JOIN PpemtPerInfoItemCm im" + " ON i.itemCd = im.ppemtPerInfoItemCmPK.itemCd"
			+ " AND im.ppemtPerInfoItemCmPK.categoryCd = c.categoryCd"
			+ " AND im.ppemtPerInfoItemCmPK.contractCd = :contractCd" + " INNER JOIN PpemtPerInfoItemOrder io"
			+ " ON i.ppemtPerInfoItemPK.perInfoItemDefId = io.ppemtPerInfoItemPK.perInfoItemDefId"
			+ " AND i.perInfoCtgId = :personInfoCategoryAuthId" + " LEFT JOIN PpemtPersonItemAuth p"
			+ " ON i.ppemtPerInfoItemPK.perInfoItemDefId = p.ppemtPersonItemAuthPk.personItemDefId"
			+ " AND p.ppemtPersonItemAuthPk.personInfoCategoryAuthId =:personInfoCategoryAuthId"
			+ " AND p.ppemtPersonItemAuthPk.roleId =:roleId" + " WHERE i.abolitionAtr = 0" + " ORDER BY io.disporder";

	private final String SEL_ALL_ITEM_AUTH_BY_ROLE_ID_CTG_ID = " SELECT c FROM PpemtPersonItemAuth c"
			+ " WHERE c.ppemtPersonItemAuthPk.roleId =:roleId"
			+ " AND c.ppemtPersonItemAuthPk.personInfoCategoryAuthId =:categoryId ";

	private final String DELETE_BY_ROLE_ID = "DELETE FROM PpemtPersonItemAuth c"
			+ " WHERE c.ppemtPersonItemAuthPk.roleId =:roleId";

	private static PpemtPersonItemAuth toEntity(PersonInfoItemAuth domain) {
		PpemtPersonItemAuth entity = new PpemtPersonItemAuth();
		entity.ppemtPersonItemAuthPk = new PpemtPersonItemAuthPk(domain.getRoleId(), domain.getPersonCategoryAuthId(),
				domain.getPersonItemDefId());
		entity.otherPersonAuth = domain.getOtherAuth().value;
		entity.selfAuthType = domain.getSelfAuth().value;
		return entity;

	}

	private static PersonInfoItemDetail toDomain(Object[] entity) {

		val domain = new PersonInfoItemDetail();

		domain.setRoleId(entity[0] == null ? "a" : entity[0].toString());

		domain.setPersonInfoCategoryAuthId(entity[1] == null ? "a" : entity[1].toString());

		domain.setPersonItemDefId(entity[2] == null ? "a" : entity[2].toString());

		domain.setSelfAuthType(entity[3] == null ? 9 : Integer.valueOf(entity[3].toString()));

		domain.setOtherPersonAuth(entity[4] == null ? 9 : Integer.valueOf(entity[4].toString()));

		domain.setItemCd(entity[5] == null ? "a" : entity[5].toString());

		domain.setItemName(entity[6] == null ? "a" : entity[6].toString());

		domain.setAbolitionAtr(entity[7] == null ? 9 : Integer.valueOf(entity[7].toString()));

		domain.setRequiredAtr(entity[8] == null ? 9 : Integer.valueOf(entity[8].toString()));

		domain.setSetting(entity[9] == null ? false : Boolean.valueOf(entity[9].toString()));

		domain.setItemParentCd(entity[10] == null ? null : entity[10].toString());

		return domain;
	}

	private static PersonInfoItemAuth toDomain(PpemtPersonItemAuth entity) {

		return PersonInfoItemAuth.createFromJavaType(entity.ppemtPersonItemAuthPk.roleId,
				entity.ppemtPersonItemAuthPk.personInfoCategoryAuthId, entity.ppemtPersonItemAuthPk.personItemDefId,
				entity.selfAuthType, entity.otherPersonAuth);

	}

	@Override
	public void add(PersonInfoItemAuth domain) {
		this.commandProxy().insert(toEntity(domain));

	}

	@Override
	public void update(PersonInfoItemAuth domain) {

		Optional<PpemtPersonItemAuth> opt = this.queryProxy().find(new PpemtPersonItemAuthPk(domain.getRoleId(),
				domain.getPersonCategoryAuthId(), domain.getPersonItemDefId()), PpemtPersonItemAuth.class);

		if (opt.isPresent()) {

			this.commandProxy().update(opt.get().updateFromDomain(domain));

		}

	}

	@Override
	public void delete(String roleId, String personCategoryAuthId, String personItemDefId) {
		this.commandProxy().remove(PpemtPersonItemAuth.class,
				new PpemtPersonItemAuthPk(roleId, personCategoryAuthId, personItemDefId));

	}

	@Override
	public List<PersonInfoItemDetail> getAllItemDetail(String roleId, String personInfoCategoryAuthId,
			String contractCd) {
		List<PersonInfoItemDetail> x = this.queryProxy()
				.query(SELECT_ITEM_INFO_AUTH_BY_CATEGORY_ID_QUERY, Object[].class)
				.setParameter("personInfoCategoryAuthId", personInfoCategoryAuthId).setParameter("roleId", roleId)
				.setParameter("contractCd", contractCd).getList(c -> toDomain(c));

		return setSetitemForSetType(x);
	}

	private List<PersonInfoItemDetail> setSetitemForSetType(List<PersonInfoItemDetail> itemList) {
		List<PersonInfoItemDetail> setItemList = itemList.stream().filter(x -> x.getItemParentCd() != null)
				.collect(Collectors.toList());
		List<PersonInfoItemDetail> newItemList = itemList.stream().filter(x -> x.getItemParentCd() == null)
				.collect(Collectors.toList());

		if (!newItemList.isEmpty()) {
			setItemList.forEach(i -> {
				PersonInfoItemDetail newItem = newItemList.stream()
						.filter(ni -> ni.getItemCd().equals(i.getItemParentCd())).findFirst().get();
				if (newItem != null) {

					if (newItem.getSetItems() == null) {
						List<PersonInfoItemDetail> newList = new ArrayList<PersonInfoItemDetail>();
						newList.add(i);
						newItem.setSetItems(newList);
					} else {
						newItem.getSetItems().add(i);
					}
				}
			});
		}

		return newItemList;
	}

	@Override
	public Optional<PersonInfoItemAuth> getItemDetai(String roleId, String categoryId, String personItemDefId) {

		return this.queryProxy()
				.find(new PpemtPersonItemAuthPk(roleId, categoryId, personItemDefId), PpemtPersonItemAuth.class)
				.map(e -> {
					return Optional.of(toDomain(e));
				}).orElse(Optional.empty());
	}

	@Override
	public List<PersonInfoItemAuth> getAllItemAuth(String roleId, String categoryId) {
		return this.queryProxy().query(SEL_ALL_ITEM_AUTH_BY_ROLE_ID_CTG_ID, PpemtPersonItemAuth.class)
				.setParameter("roleId", roleId).setParameter("categoryId", categoryId).getList(c -> toDomain(c));
	}

	@Override
	public void deleteByRoleId(String roleId) {
		this.getEntityManager().createQuery(DELETE_BY_ROLE_ID).setParameter("roleId", roleId).executeUpdate();
		this.getEntityManager().flush();

	}

}
