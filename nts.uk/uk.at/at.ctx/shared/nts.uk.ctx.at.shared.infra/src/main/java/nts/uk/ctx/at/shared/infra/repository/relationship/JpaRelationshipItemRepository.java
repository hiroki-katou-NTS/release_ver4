package nts.uk.ctx.at.shared.infra.repository.relationship;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.relationship.Relationship;
import nts.uk.ctx.at.shared.dom.relationship.repository.RelationshipRepository;
import nts.uk.ctx.at.shared.infra.entity.relationship.KshstRelationshipItem;
import nts.uk.ctx.at.shared.infra.entity.relationship.KshstRelationshipPK;

@Stateless
public class JpaRelationshipItemRepository extends JpaRepository implements RelationshipRepository {

	private static final String SELECT_NO_WHERE = "SELECT c FROM KshstRelationshipItem c ";
	private static final String SELECT_ITEM = SELECT_NO_WHERE + "WHERE c.kshstRelationshipPK.companyId = :companyId";
	private static final String DELETE_GRANT = "DELETE FROM KshstGrantRelationshipItem c WHERE c.kshstGrantRelationshipPK.companyId = :companyId AND c.kshstGrantRelationshipPK.relationshipCode = :relationshipCode";
	private static final String SELECT_ITEM_WITH_SETTING_QUERY = "SELECT a.pk.relationshipCd"
			+ " FROM KshstGrantDayRelationship a" + " INNER JOIN KshstGrantDayPerRelationship b"
			+ " ON a.pk.sHolidayEventNo = b.pk.sHolidayEventNo AND a.pk.companyId = b.pk.companyId"
			+ " WHERE b.pk.companyId = :companyId" + " AND b.pk.sHolidayEventNo = :sHENo"
			+ " AND a.pk.relationshipCd IN :relpCds";

	/**
	 * change from entity to domain
	 * 
	 * @param entity
	 * @return author: Hoang Yen
	 */
	private static Relationship toDomain(KshstRelationshipItem entity) {
		Relationship domain = Relationship.createFromJavaType(entity.kshstRelationshipPK.companyId,
				entity.kshstRelationshipPK.relationshipCode, entity.relationshipName, entity.threeParentOrLess);
		return domain;
	}

	private static KshstRelationshipItem toEntity(Relationship domain) {
		val entity = new KshstRelationshipItem();
		entity.kshstRelationshipPK = new KshstRelationshipPK(domain.getCompanyId(), domain.getRelationshipCode().v());
		entity.relationshipName = domain.getRelationshipName().v();
		entity.threeParentOrLess = domain.isThreeParentOrLess();
		return entity;
	}

	/**
	 * get all data author: Hoang Yen
	 */
	@Override
	public List<Relationship> findAll(String companyId) {
		return this.queryProxy().query(SELECT_ITEM, KshstRelationshipItem.class).setParameter("companyId", companyId)
				.getList(c -> toDomain(c));
	}

	/**
	 * update relationship author: Hoang Yen
	 */
	@Override
	public void update(Relationship domain) {
		KshstRelationshipItem entity = toEntity(domain);
		KshstRelationshipItem oldEntity = this.queryProxy()
				.find(entity.kshstRelationshipPK, KshstRelationshipItem.class).get();
		oldEntity.setRelationshipName(entity.relationshipName);
		oldEntity.setThreeParentOrLess(domain.isThreeParentOrLess());
		this.commandProxy().update(oldEntity);
	}

	/**
	 * insert relationship author: Hoang Yen
	 */
	@Override
	public void insert(Relationship relationship) {
		this.commandProxy().insert(toEntity(relationship));
	}

	/**
	 * delete relation ship author: Hoang Yen
	 */
	@Override
	public void delete(String companyId, String relationshipCd) {
		KshstRelationshipPK kshstRelationshipPK = new KshstRelationshipPK(companyId, relationshipCd);
		this.commandProxy().remove(KshstRelationshipItem.class, kshstRelationshipPK);
		this.getEntityManager().createQuery(DELETE_GRANT).setParameter("companyId", companyId)
				.setParameter("relationshipCode", relationshipCd).executeUpdate();
	}

	/**
	 * get relationship by code author: Hoang Yen
	 */
	@Override
	public Optional<Relationship> findByCode(String companyId, String relationshipCd) {
		return this.queryProxy().find(new KshstRelationshipPK(companyId, relationshipCd), KshstRelationshipItem.class)
				.map(c -> toDomain(c));
	}

	@Override
	public List<String> findSettingWithCds(String companyId, int sHENo, List<String> relpCds) {
		if(CollectionUtil.isEmpty(relpCds)){
			return Collections.emptyList();
		}
		return this.queryProxy().query(SELECT_ITEM_WITH_SETTING_QUERY, Object.class)
				.setParameter("companyId", companyId).setParameter("sHENo", sHENo).setParameter("relpCds", relpCds)
				.getList(c -> String.valueOf(c));
	}

}
