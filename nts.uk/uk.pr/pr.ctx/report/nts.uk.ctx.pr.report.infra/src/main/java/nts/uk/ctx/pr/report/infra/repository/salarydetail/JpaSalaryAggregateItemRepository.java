/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItem;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemRepository;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHead;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHeadPK;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHeadPK_;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHead_;
import nts.uk.ctx.pr.report.infra.repository.salarydetail.memento.JpaSalaryAggregateItemGetMemento;
import nts.uk.ctx.pr.report.infra.repository.salarydetail.memento.JpaSalaryAggregateItemSetMemento;

/**
 * The Class JpaSalaryAggregateItemRepository.
 */
@Stateless
public class JpaSalaryAggregateItemRepository extends JpaRepository implements SalaryAggregateItemRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#add(nts.uk.ctx.pr.report.dom.salarydetail.
	 * aggregate.SalaryAggregateItem)
	 */
	@Override
	public void add(SalaryAggregateItem aggregateItem) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#update(nts.uk.ctx.pr.report.dom.
	 * salarydetail.aggregate.SalaryAggregateItem)
	 */
	@Override
	public void update(SalaryAggregateItem aggregateItem) {
		this.commandProxy().update(this.toEntity(aggregateItem));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#remove(nts.uk.ctx.pr.report.dom.
	 * salarydetail.aggregate.SalaryAggregateItem)
	 */
	@Override
	public void remove(SalaryAggregateItem aggregateItem) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#findByCode(java.lang.String,
	 * java.lang.String)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#findByCode(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public Optional<SalaryAggregateItem> findByCode(String companyCode, String aggregateItemCode, int categoryCode) {
		return this.queryProxy().find(new QlsptPaylstAggreHeadPK(companyCode, aggregateItemCode, categoryCode),
				QlsptPaylstAggreHead.class).map(c -> this.toDomain(c));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemRepository#findAll(java.lang.String)
	 */
	@Override
	public List<SalaryAggregateItem> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QlsptPaylstAggreHead> cq = cb.createQuery(QlsptPaylstAggreHead.class);
		Root<QlsptPaylstAggreHead> root = cq.from(QlsptPaylstAggreHead.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QlsptPaylstAggreHead_.qlsptPaylstAggreHeadPK).get(QlsptPaylstAggreHeadPK_.ccd), companyCode));
		cq.where(predicateList.toArray(new Predicate[] {}));
		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the salary aggregate item
	 */
	private SalaryAggregateItem toDomain(QlsptPaylstAggreHead entity) {
		return new SalaryAggregateItem(new JpaSalaryAggregateItemGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the qlspt paylst aggre head
	 */
	private QlsptPaylstAggreHead toEntity(SalaryAggregateItem domain) {
		QlsptPaylstAggreHead entity = new QlsptPaylstAggreHead();
		domain.saveToMemento(new JpaSalaryAggregateItemSetMemento(entity));
		return entity;
	}

}
