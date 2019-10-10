package nts.uk.ctx.at.request.infra.repository.setting.request.application;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadline;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadlineRepository;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstAppDeadline;
import nts.uk.ctx.at.request.infra.entity.setting.request.application.KrqstAppDeadlinePK;

@Stateless
public class JpaApplicationDeadlineRepository extends JpaRepository implements ApplicationDeadlineRepository{
	private static final String FINDBYCLOSURE = "SELECT c "
			+ "FROM KrqstAppDeadline c "
			+ "WHERE c.krqstAppDeadlinePK.companyId = :companyId "
			+ "AND c.krqstAppDeadlinePK.closureId = :closureId";
	private static final String FINDBYCOMPANY = "SELECT c "
			+ "FROM KrqstAppDeadline c "
			+ "WHERE c.krqstAppDeadlinePK.companyId = :company ";

	/**
	 * get deadline setting by closureId
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ApplicationDeadline> getDeadlineByClosureId(String companyId, int closureId) {
		Optional<ApplicationDeadline> data = this.queryProxy()
				.query(FINDBYCLOSURE, KrqstAppDeadline.class)
				.setParameter("companyId", companyId)
				.setParameter("closureId", closureId)
				.getSingle(c -> toDomain(c));

		return data;
	}
	private ApplicationDeadline toDomain(KrqstAppDeadline c) {
		return ApplicationDeadline.createSimpleFromJavaType(c.krqstAppDeadlinePK.companyId,
				c.krqstAppDeadlinePK.closureId, 
				c.useAtr, 
				c.deadline, 
				c.deadlineCriteria);
	}
	/**
	 * get deadline setting by app type
	 */
	@Override
	public List<ApplicationDeadline> getDeadlineByAppType(String companyId, int appType) {
		List<ApplicationDeadline> data = this.queryProxy()
				.query(FINDBYCOMPANY, KrqstAppDeadline.class)
				.setParameter("companyId", companyId)
				.getList(c -> toDomain(c));

		return data;
	}
	/**
	 * convert from ApplicationDeadline domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
	private static KrqstAppDeadline toEntityDeadline(ApplicationDeadline domain){
		val entity = new KrqstAppDeadline();
		entity.krqstAppDeadlinePK = new KrqstAppDeadlinePK(domain.getCompanyId(), domain.getClosureId());
		entity.deadline = domain.getDeadline().v();
		entity.deadlineCriteria = domain.getDeadlineCriteria().value;
		entity.useAtr = domain.getUserAtr().value;
		return entity;
	}
	
	/**
	 * update deadline
	 * @author yennth
	 */
	@Override
	public void update(ApplicationDeadline appDeadline) {
		KrqstAppDeadline entity = toEntityDeadline(appDeadline);
		KrqstAppDeadline oldentity = this.queryProxy().find(entity.krqstAppDeadlinePK, KrqstAppDeadline.class).get();
		oldentity.deadline = entity.deadline;
		oldentity.deadlineCriteria = entity.deadlineCriteria;
		oldentity.useAtr = entity.useAtr;
		this.commandProxy().update(oldentity);
	}
	/**
	 * insert deadline
	 * @author yennth
	 */
	@Override
	public void insert(ApplicationDeadline appDeadline) {
		KrqstAppDeadline entity = toEntityDeadline(appDeadline);
		this.commandProxy().insert(entity);
	}
}
