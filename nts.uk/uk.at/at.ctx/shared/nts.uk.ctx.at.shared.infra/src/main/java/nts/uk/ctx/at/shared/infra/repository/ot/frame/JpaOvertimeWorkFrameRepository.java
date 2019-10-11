/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository;
import nts.uk.ctx.at.shared.infra.entity.ot.frame.KshstOvertimeFrame;
import nts.uk.ctx.at.shared.infra.entity.ot.frame.KshstOvertimeFramePK;
import nts.uk.ctx.at.shared.infra.entity.ot.frame.KshstOvertimeFramePK_;
import nts.uk.ctx.at.shared.infra.entity.ot.frame.KshstOvertimeFrame_;

/**
 * The Class JpaOvertimeWorkFrameRepository.
 */
@Stateless
public class JpaOvertimeWorkFrameRepository extends JpaRepository
	implements OvertimeWorkFrameRepository {

	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository#findOvertimeWorkFrame(nts.uk.ctx.at.shared.dom.common.CompanyId, int)
	 */
	@Override
	public Optional<OvertimeWorkFrame> findOvertimeWorkFrame(CompanyId companyId, int overtimeWorkFrNo) {
		return this.queryProxy()
				.find(new KshstOvertimeFramePK(companyId.v(), (short) overtimeWorkFrNo), KshstOvertimeFrame.class)
				.map(e -> this.toDomain(e));
	}

	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository#update(nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame)
	 */
	@Override
	public void update(OvertimeWorkFrame overtimeWorkFrame) {
		this.commandProxy().update(this.toEntity(overtimeWorkFrame));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository#getAllOvertimeWorkFrame(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<OvertimeWorkFrame> getAllOvertimeWorkFrame(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshstOvertimeFrame> cq = criteriaBuilder
			.createQuery(KshstOvertimeFrame.class);
		
		// root data
		Root<KshstOvertimeFrame> root = cq.from(KshstOvertimeFrame.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(KshstOvertimeFrame_.kshstOvertimeFramePK)
				.get(KshstOvertimeFramePK_.cid), companyId));
		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<KshstOvertimeFrame> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
			.collect(Collectors.toList());
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<OvertimeWorkFrame> getOvertimeWorkFrameByFrameNos(String companyId, List<Integer> overtimeWorkFrNos) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshstOvertimeFrame> cq = criteriaBuilder
				.createQuery(KshstOvertimeFrame.class);

		// root data
		Root<KshstOvertimeFrame> root = cq.from(KshstOvertimeFrame.class);

		// select root
		cq.select(root);

		List<KshstOvertimeFrame> resultList = new ArrayList<>();

		CollectionUtil.split(overtimeWorkFrNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT,
				splitData -> {
					// add where
					List<Predicate> lstpredicateWhere = new ArrayList<>();
					// eq company id
					lstpredicateWhere.add(
							criteriaBuilder.equal(root.get(KshstOvertimeFrame_.kshstOvertimeFramePK)
									.get(KshstOvertimeFramePK_.cid), companyId));

					lstpredicateWhere.add(root.get(KshstOvertimeFrame_.kshstOvertimeFramePK)
							.get(KshstOvertimeFramePK_.otFrNo).in(splitData));
					// set where to SQL
					cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

					resultList.addAll(em.createQuery(cq).getResultList());
				});

		// exclude select
		return resultList.stream().map(category -> toDomain(category)).collect(Collectors.toList());
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<OvertimeWorkFrame> getOvertimeWorkFrameByFrameByCom(String companyId, int useAtr) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshstOvertimeFrame> cq = criteriaBuilder
			.createQuery(KshstOvertimeFrame.class);
		
		// root data
		Root<KshstOvertimeFrame> root = cq.from(KshstOvertimeFrame.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(KshstOvertimeFrame_.kshstOvertimeFramePK)
				.get(KshstOvertimeFramePK_.cid), companyId));
		lstpredicateWhere
		.add(criteriaBuilder.equal(root.get(KshstOvertimeFrame_.useAtr), useAtr));
		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<KshstOvertimeFrame> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(category -> toDomain(category))
			.collect(Collectors.toList());
	}
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kshst overtime frame
	 */
	private KshstOvertimeFrame toEntity(OvertimeWorkFrame domain){
		KshstOvertimeFrame entity = new KshstOvertimeFrame();
		domain.saveToMemento(new JpaOvertimeWorkFrameSetMemento(entity));
		return entity;
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the overtime work frame
	 */
	private OvertimeWorkFrame toDomain(KshstOvertimeFrame entity){
		return new OvertimeWorkFrame(new JpaOvertimeWorkFrameGetMemento(entity));
	}

}
