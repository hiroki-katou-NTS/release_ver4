package repository.roles;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import entity.roles.auth.category.PpemtPersonCategoryAuth;
import entity.roles.auth.category.PpemtPersonCategoryAuthPk;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.category.PersonInfoCategoryAuth;
import nts.uk.ctx.bs.person.dom.person.role.auth.category.PersonInfoCategoryAuthRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.category.PersonInfoCategoryDetail;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaPersonInfoCategoryAuthRepository extends JpaRepository implements PersonInfoCategoryAuthRepository {

	private final String SELECT_CATEGORY_BY_PERSON_ROLE_ID_QUERY = "SELECT DISTINCT c.ppemtPerInfoCtgPK.perInfoCtgId, c.categoryCd, c.categoryName, "
			+ " cm.categoryType, p.allowPersonRef, p.allowOtherRef, "
			+ " CASE WHEN p.ppemtPersonCategoryAuthPk.personInfoCategoryAuthId IS NULL THEN 'False' ELSE 'True' END AS IsConfig"
			+ " FROM PpemtPerInfoCtg c"
			+ " INNER JOIN PpemtPerInfoCtgCm cm"
			+ " ON c.categoryCd = cm.ppemtPerInfoCtgCmPK.categoryCd"
			+ " AND cm.ppemtPerInfoCtgCmPK.contractCd = :contractCd"
			+ " INNER JOIN PpemtPerInfoCtgOrder co"
			+ "	ON c.ppemtPerInfoCtgPK.perInfoCtgId = co.ppemtPerInfoCtgPK.perInfoCtgId"
			+ " INNER JOIN PpemtPerInfoItem i"
			+ " ON  c.ppemtPerInfoCtgPK.perInfoCtgId = i.perInfoCtgId"
			+ " LEFT JOIN PpemtPersonCategoryAuth p "
			+ " ON p.ppemtPersonCategoryAuthPk.personInfoCategoryAuthId  = c.ppemtPerInfoCtgPK.perInfoCtgId"
			+ " AND p.ppemtPersonCategoryAuthPk.roleId = :roleId"
			+ " WHERE c.cid = :companyId"
			+ " AND c.abolitionAtr = 0"
			+ "	ORDER BY co.disporder";
	
	private final String SEL_CATEGORY_BY_ROLEID = "SELECT c FROM PpemtPersonCategoryAuth c  WHERE c.ppemtPersonCategoryAuthPk.roleId =:roleId ";

	private final String SEL_CATEGORY_BY_ABOLITION_ATR = "SELECT  c.perInfoCtgId, d.categoryCd, d.categoryName, d.abolitionAtr, c.abolitionAtr, c.requiredAtr, "
			+ "CASE WHEN c.perInfoCtgId IS NULL THEN 'False' ELSE 'True' END AS IsConfig" + " FROM PpemtPerInfoCtg d "
			+ " INNER JOIN   PpemtPerInfoItem c " + " ON  d.ppemtPerInfoCtgPK.perInfoCtgId = c.perInfoCtgId"
			+ " WHERE d.cid = :CID  AND d.abolitionAtr = 0 AND c.abolitionAtr = 0";
	
	private final String SEL_ALL_CATEGORY = "SELECT c.ppemtPerInfoCtgPK.perInfoCtgId, c.categoryCd, c.categoryName, "
			+ " cm.categoryType, p.allowPersonRef, p.allowOtherRef, "
			+ "CASE WHEN p.ppemtPersonCategoryAuthPk.personInfoCategoryAuthId IS NULL THEN 'False' ELSE 'True' END AS IsConfig"
			+ " FROM PpemtPerInfoCtg c LEFT JOIN PpemtPersonCategoryAuth p "
			+ " ON p.ppemtPersonCategoryAuthPk.personInfoCategoryAuthId  = c.ppemtPerInfoCtgPK.perInfoCtgId"
			+ " AND p.ppemtPersonCategoryAuthPk.roleId = :roleId" + " LEFT JOIN PpemtPerInfoCtgCm cm"
			+ " ON c.categoryCd = cm.ppemtPerInfoCtgCmPK.categoryCd "
			+ " WHERE c.cid = :CID";

	private static PersonInfoCategoryAuth toDomain(PpemtPersonCategoryAuth entity) {
		val domain = PersonInfoCategoryAuth.createFromJavaType(entity.ppemtPersonCategoryAuthPk.roleId,
				entity.ppemtPersonCategoryAuthPk.personInfoCategoryAuthId, entity.allowPersonRef, entity.allowOtherRef,
				entity.allowOtherCompanyRef, entity.selfPastHisAuth, entity.selfFutureHisAuth, entity.selfAllowAddHis,
				entity.selfAllowDelHis, entity.otherPastHisAuth, entity.otherFutureHisAuth, entity.otherAllowAddHis,
				entity.otherAllowDelHis, entity.selfAllowAddMulti, entity.selfAllowDelMulti, entity.otherAllowAddMulti,
				entity.otherAllowDelMulti);
		return domain;
	}

	private static PersonInfoCategoryDetail toDomain(Object[] entity) {
		val domain = new PersonInfoCategoryDetail();
		domain.setCategoryId(entity[0].toString());
		domain.setCategoryCode(entity[1].toString());
		domain.setCategoryName(entity[2].toString());
		domain.setCategoryType(Integer.valueOf(entity[3].toString()));
		if (entity[4] != null) {
			domain.setAllowPersonRef(Integer.valueOf(entity[4].toString()));
		}
		if (entity[5] != null) {
			domain.setAllowOtherRef(Integer.valueOf(entity[5].toString()));
		}
		domain.setSetting(Boolean.valueOf(entity[6].toString()));
		return domain;
	}

	private static PpemtPersonCategoryAuth toEntity(PersonInfoCategoryAuth domain) {
		PpemtPersonCategoryAuth entity = new PpemtPersonCategoryAuth();
		entity.ppemtPersonCategoryAuthPk = new PpemtPersonCategoryAuthPk(domain.getRoleId(),
				domain.getPersonInfoCategoryAuthId());
		entity.allowOtherCompanyRef = domain.getAllowOtherCompanyRef().value;
		entity.allowOtherRef = domain.getAllowOtherRef().value;
		entity.allowPersonRef = domain.getAllowPersonRef().value;
		entity.otherAllowAddHis = domain.getOtherAllowAddHis().value;
		entity.otherAllowAddHis = domain.getOtherAllowAddHis().value;
		entity.selfPastHisAuth = domain.getSelfPastHisAuth().value;
		entity.selfFutureHisAuth = domain.getSelfFutureHisAuth().value;
		entity.selfAllowAddHis = domain.getSelfAllowAddHis().value;
		entity.selfAllowDelHis = domain.getSelfAllowAddHis().value;
		entity.otherPastHisAuth = domain.getOtherPastHisAuth().value;
		entity.otherFutureHisAuth = domain.getOtherFutureHisAuth().value;
		entity.selfAllowAddMulti = domain.getSelfAllowAddMulti().value;
		entity.selfAllowDelMulti = domain.getSelfAllowDelHis().value;
		entity.otherAllowAddMulti = domain.getOtherAllowAddMulti().value;
		entity.otherAllowDelMulti = domain.getOtherAllowDelMulti().value;
		return entity;

	}

	@Override
	public void add(PersonInfoCategoryAuth domain) {
		this.commandProxy().insert(toEntity(domain));
		this.getEntityManager().flush();

	}

	@Override
	public void update(PersonInfoCategoryAuth domain) {
		this.commandProxy().update(toEntity(domain));

	}

	@Override
	public void delete(String roleId, String personCategoryAuthId) {
		this.commandProxy().remove(PpemtPersonCategoryAuth.class,
				new PpemtPersonCategoryAuthPk(roleId, personCategoryAuthId));
	}

	@Override
	public Optional<PersonInfoCategoryAuth> getDetailPersonCategoryAuthByPId(String roleId,
			String personCategoryAuthId) {
		return this.queryProxy()
				.find(new PpemtPersonCategoryAuthPk(roleId, personCategoryAuthId), PpemtPersonCategoryAuth.class)
				.map(e -> {
					return Optional.of(toDomain(e));
				}).orElse(Optional.empty());
	}

	@Override
	public List<PersonInfoCategoryDetail> getAllCategory(String roleId, String contractCd,String companyId) {
		return this.queryProxy().query(SELECT_CATEGORY_BY_PERSON_ROLE_ID_QUERY, Object[].class)
				.setParameter("roleId", roleId)
				.setParameter("contractCd", contractCd)
				.setParameter("companyId", companyId)
				.getList(c -> toDomain(c));

	}

	@Override
	public List<PersonInfoCategoryAuth> getAllCategoryAuthByRoleId(String roleId) {
		return this.queryProxy().query(SEL_CATEGORY_BY_ROLEID, PpemtPersonCategoryAuth.class).setParameter("roleId", roleId)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<PersonInfoCategoryDetail> getAllCategoryInfo() {
		String companyId = AppContexts.user().companyId();
		return  this.queryProxy().query(SEL_CATEGORY_BY_ABOLITION_ATR, Object[].class)
								 .setParameter("CID", companyId)
								 .getList(c -> toDomain(c));
	}

	@Override
	public List<PersonInfoCategoryDetail> getAllCategoryByRoleId(String roleId) {
		String companyId = AppContexts.user().companyId();
		return this.queryProxy().query(SEL_ALL_CATEGORY,Object[].class)
				   .setParameter("roleId", roleId)
				   .setParameter("CID", companyId)
				   .getList(c -> toDomain(c));
	}

}
