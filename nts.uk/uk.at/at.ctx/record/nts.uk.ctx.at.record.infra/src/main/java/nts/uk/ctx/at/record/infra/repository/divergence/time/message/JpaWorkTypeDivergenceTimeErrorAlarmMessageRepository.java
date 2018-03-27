package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstWtdvgcTimeEaMsg;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstWtdvgcTimeEaMsgPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaWorkTypeDivergenceTimeErrorAlarmMessageRepository.
 */
@Stateless
public class JpaWorkTypeDivergenceTimeErrorAlarmMessageRepository extends JpaRepository
		implements WorkTypeDivergenceTimeErrorAlarmMessageRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#getByDivergenceTimeNo(java.
	 * lang.Integer)
	 */
	@Override
	public Optional<WorkTypeDivergenceTimeErrorAlarmMessage> getByDivergenceTimeNo(Integer divergenceTimeNo,
			CompanyId cId, BusinessTypeCode workTypeCode) {
		KrcstWtdvgcTimeEaMsgPK pk = new KrcstWtdvgcTimeEaMsgPK(cId.v(), divergenceTimeNo, workTypeCode.v());

		return this.queryProxy().find(pk, KrcstWtdvgcTimeEaMsg.class).map(item -> this.toDomain(item));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#add(nts.uk.ctx.at.record.
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
	 * WorkTypeDivergenceTimeErrorAlarmMessageRepository#update(nts.uk.ctx.at.record
	 * .dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage)
	 */
	@Override
	public void update(WorkTypeDivergenceTimeErrorAlarmMessage message) {
		this.commandProxy().update(this.toEntity(message));
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the divergence time error alarm message
	 */
	private WorkTypeDivergenceTimeErrorAlarmMessage toDomain(KrcstWtdvgcTimeEaMsg entity) {
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
	private KrcstWtdvgcTimeEaMsg toEntity(WorkTypeDivergenceTimeErrorAlarmMessage domain) {
		KrcstWtdvgcTimeEaMsgPK pk = new KrcstWtdvgcTimeEaMsgPK(domain.getCId().v(), domain.getDivergenceTimeNo(),
				domain.getWorkTypeCode().v());

		KrcstWtdvgcTimeEaMsg entity = this.queryProxy().find(pk, KrcstWtdvgcTimeEaMsg.class)
				.orElse(new KrcstWtdvgcTimeEaMsg());

		WorkTypeDivergenceTimeErrorAlarmMessageSetMemento memento = new JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento(
				entity);
		domain.saveToMemento(memento);

		return entity;
	}
}
