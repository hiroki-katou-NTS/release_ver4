package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.message.KrcmtDvgcEralMsgBus;
import nts.uk.ctx.at.record.infra.entity.divergence.time.message.KrcmtDvgcEralMsgBusPK;
import nts.uk.ctx.at.record.infra.entity.divergence.time.message.KrcmtDvgcEralMsgBusPK_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.message.KrcmtDvgcEralMsgBus_;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaWorkTypeDivergenceTimeErrorAlarmMessageRepository.
 */
@Stateless
public class JpaWorkTypeDivTimeErrAlarmMsgRepo extends JpaRepository
		implements WorkTypeDivergenceTimeErrorAlarmMessageRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#getByDivergenceTimeNo(
	 * java. lang.Integer)
	 */
	@Override
	public Optional<WorkTypeDivergenceTimeErrorAlarmMessage> getByDivergenceTimeNo(Integer divergenceTimeNo,
			CompanyId cId, BusinessTypeCode workTypeCode) {
		KrcmtDvgcEralMsgBusPK pk = new KrcmtDvgcEralMsgBusPK(cId.v(), divergenceTimeNo, workTypeCode.v());

		return this.queryProxy().find(pk, KrcmtDvgcEralMsgBus.class).map(item -> this.toDomain(item));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#add(nts.uk.ctx.at.
	 * record.
	 * dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage)
	 */
	@Override
	public void add(WorkTypeDivergenceTimeErrorAlarmMessage message) {
		this.commandProxy().insert(this.toEntity(message));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#update(nts.uk.ctx.at.
	 * record
	 * .dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage)
	 */
	@Override
	public void update(WorkTypeDivergenceTimeErrorAlarmMessage message) {
		this.commandProxy().update(this.toEntity(message));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#
	 * getByDivergenceTimeNoList(java.util.List,
	 * nts.uk.ctx.at.shared.dom.common.CompanyId,
	 * nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.
	 * BusinessTypeCode)
	 */
	@Override
	public List<WorkTypeDivergenceTimeErrorAlarmMessage> getByDivergenceTimeNoList(List<Integer> divergenceTimeNoList,
			CompanyId cId, BusinessTypeCode workTypeCode) {
		
		if (CollectionUtil.isEmpty(divergenceTimeNoList)) {
			return Collections.emptyList();
		}
		
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtDvgcEralMsgBus> cq = criteriaBuilder.createQuery(KrcmtDvgcEralMsgBus.class);
		Root<KrcmtDvgcEralMsgBus> root = cq.from(KrcmtDvgcEralMsgBus.class);

		// Build query
		cq.select(root);

		List<KrcmtDvgcEralMsgBus> resultList = new ArrayList<>();

		CollectionUtil.split(divergenceTimeNoList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			// create where conditions
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.get(KrcmtDvgcEralMsgBus_.id).get(KrcmtDvgcEralMsgBusPK_.cid), cId));
			predicates.add(criteriaBuilder.equal(root.get(KrcmtDvgcEralMsgBus_.id).get(KrcmtDvgcEralMsgBusPK_.worktypeCd),
					workTypeCode));
			// dvgcTimeNo in divergenceTimeNoList
			predicates.add(
					root.get(KrcmtDvgcEralMsgBus_.id).get(KrcmtDvgcEralMsgBusPK_.dvgcTimeNo).in(splitData));

			// add where to query
			cq.where(predicates.toArray(new Predicate[] {}));

			// query data
			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// return
		return resultList.isEmpty() ? new ArrayList<WorkTypeDivergenceTimeErrorAlarmMessage>()
				: resultList.stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the divergence time error alarm message
	 */
	private WorkTypeDivergenceTimeErrorAlarmMessage toDomain(KrcmtDvgcEralMsgBus entity) {
		WorkTypeDivergenceTimeErrorAlarmMessageGetMemento memento = new JpaWorkTypeDivergenceTimeErrorAlarmMessageGetMemento(
				entity);
		return new WorkTypeDivergenceTimeErrorAlarmMessage(memento);
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the krcst dvgc time ea msg
	 */
	private KrcmtDvgcEralMsgBus toEntity(WorkTypeDivergenceTimeErrorAlarmMessage domain) {
		KrcmtDvgcEralMsgBusPK pk = new KrcmtDvgcEralMsgBusPK(domain.getCId().v(), domain.getDivergenceTimeNo(),
				domain.getWorkTypeCode().v());

		KrcmtDvgcEralMsgBus entity = this.queryProxy().find(pk, KrcmtDvgcEralMsgBus.class)
				.orElse(new KrcmtDvgcEralMsgBus());

		WorkTypeDivergenceTimeErrorAlarmMessageSetMemento memento = new JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento(
				entity);
		domain.saveToMemento(memento);

		return entity;
	}

}
