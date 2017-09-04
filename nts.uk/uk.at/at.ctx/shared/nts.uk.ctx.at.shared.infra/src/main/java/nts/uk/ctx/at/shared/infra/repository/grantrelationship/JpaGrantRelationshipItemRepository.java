package nts.uk.ctx.at.shared.infra.repository.grantrelationship;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.grantrelationship.GrantRelationship;
import nts.uk.ctx.at.shared.dom.grantrelationship.repository.GrantRelationshipRepository;
import nts.uk.ctx.at.shared.infra.entity.grantrelationship.KshstGrantRelationshipItem;
import nts.uk.ctx.at.shared.infra.entity.grantrelationship.KshstGrantRelationshipPK;

@Stateless
public class JpaGrantRelationshipItemRepository extends JpaRepository implements GrantRelationshipRepository{
	
	private final String SELECT_NO_WHERE = "SELECT c FROM KshstGrantRelationshipItem c ";
	private final String SELECT_ITEM = SELECT_NO_WHERE + "WHERE c.kshstGrantRelationshipPK.companyId = :companyId";
	/**
	 * change from entity to domain
	 * @param entity
	 * @return
	 * 
	 * author: Hoang Yen
	 */
	private static GrantRelationship toDomain(KshstGrantRelationshipItem entity){
		GrantRelationship domain = GrantRelationship.createFromJavaType(entity.kshstGrantRelationshipPK.companyId, 
																		entity.kshstGrantRelationshipPK.specialHolidayCode,
																		entity.kshstGrantRelationshipPK.relationshipCode,
																		entity.grantRelationshipDay,
																		entity.morningHour);
		return domain;
	}
	private static KshstGrantRelationshipItem toEntity(GrantRelationship domain){
		val entity = new KshstGrantRelationshipItem();
		entity.kshstGrantRelationshipPK = new KshstGrantRelationshipPK(domain.getCompanyId(), domain.getSpecialHolidayCode(), domain.getRelationshipCode());
		entity.grantRelationshipDay = domain.getGrantRelationshipDay().v();
		entity.morningHour = domain.getMorningHour() != null ? domain.getMorningHour().v() : null;
		return entity;
	}
	/**
	 * get all data
	 * author: Hoang Yen
	 */
	@Override
	public List<GrantRelationship> findAll(String companyId) {
		return this.queryProxy().query(SELECT_ITEM, KshstGrantRelationshipItem.class).setParameter("companyId", companyId).getList(c->toDomain(c));
	}
	/**
	 * update grant relationship
	 * author: Hoang Yen
	 */
	@Override
	public void update(GrantRelationship grantRelationship) {
		KshstGrantRelationshipPK kshstGrantRelationshipPK = new KshstGrantRelationshipPK(grantRelationship.getCompanyId(), grantRelationship.getSpecialHolidayCode(), grantRelationship.getRelationshipCode());
		KshstGrantRelationshipItem oldEntity = this.queryProxy().find(kshstGrantRelationshipPK, KshstGrantRelationshipItem.class).get();
		oldEntity.grantRelationshipDay = grantRelationship.getGrantRelationshipDay().v();
		oldEntity.morningHour = grantRelationship.getMorningHour() != null ? grantRelationship.getMorningHour().v() : null;;
		this.commandProxy().update(oldEntity);
	}
	/**
	 * insert grant relationship
	 * author: Hoang Yen
	 */
	@Override
	public void insert(GrantRelationship grantRelationship) {
		this.commandProxy().insert(toEntity(grantRelationship));
	}
	/**
	 * get grant relationship by code
	 * author: Hoang Yen
	 */
	@Override
	public Optional<GrantRelationship> findByCode(String companyId, int specialHolidayCode, String relationshipCode) {
		return this.queryProxy().find(new KshstGrantRelationshipPK(companyId, specialHolidayCode, relationshipCode), KshstGrantRelationshipItem.class).map(c-> toDomain(c));
	}
	/**
	 * delete grant relation ship
	 * author: Hoang Yen
	 */
	@Override
	public void delete(String companyId, int specialHolidayCode, String relationshipCode) {
		KshstGrantRelationshipPK kshstGrantRelationshipPK = new KshstGrantRelationshipPK(companyId, specialHolidayCode, relationshipCode);
		this.commandProxy().remove(KshstGrantRelationshipItem.class, kshstGrantRelationshipPK);
	}
}
