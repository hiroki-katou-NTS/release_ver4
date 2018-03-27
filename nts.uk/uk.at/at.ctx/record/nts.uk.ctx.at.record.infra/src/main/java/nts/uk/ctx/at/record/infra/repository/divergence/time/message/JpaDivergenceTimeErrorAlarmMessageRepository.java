package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsg;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsgPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaDivergenceTimeErrorAlarmMessageRepository.
 */
@Stateless
public class JpaDivergenceTimeErrorAlarmMessageRepository extends JpaRepository
		implements DivergenceTimeErrorAlarmMessageRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageRepository#getByDivergenceTimeNo(java.lang.
	 * Integer)
	 */
	@Override
	public Optional<DivergenceTimeErrorAlarmMessage> findByDivergenceTimeNo(CompanyId cId, Integer divergenceTimeNo) {
		KrcstDvgcTimeEaMsgPK pk = new KrcstDvgcTimeEaMsgPK(cId.v(), divergenceTimeNo);

		return this.queryProxy().find(pk, KrcstDvgcTimeEaMsg.class).map(item -> this.toDomain(item));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageRepository#add(nts.uk.ctx.at.record.dom.
	 * divergence.time.message.DivergenceTimeErrorAlarmMessage)
	 */
	@Override
	public void add(DivergenceTimeErrorAlarmMessage message) {
		this.commandProxy().insert(this.toEntity(message));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageRepository#update(nts.uk.ctx.at.record.dom.
	 * divergence.time.message.DivergenceTimeErrorAlarmMessage)
	 */
	@Override
	public void update(DivergenceTimeErrorAlarmMessage message) {
		this.commandProxy().update(this.toEntity(message));
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the divergence time error alarm message
	 */
	private DivergenceTimeErrorAlarmMessage toDomain(KrcstDvgcTimeEaMsg entity) {
		DivergenceTimeErrorAlarmMessageGetMemento memento = new JpaDivergenceTimeErrorAlarmMessageGetMemento(entity);
		return new DivergenceTimeErrorAlarmMessage(memento);
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the krcst dvgc time ea msg
	 */
	private KrcstDvgcTimeEaMsg toEntity(DivergenceTimeErrorAlarmMessage domain) {
		KrcstDvgcTimeEaMsgPK pk = new KrcstDvgcTimeEaMsgPK(domain.getCId().v(), domain.getDivergenceTimeNo());

		KrcstDvgcTimeEaMsg entity = this.queryProxy().find(pk, KrcstDvgcTimeEaMsg.class)
				.orElse(new KrcstDvgcTimeEaMsg());

		DivergenceTimeErrorAlarmMessageSetMemento memento = new JpaDivergenceTimeErrorAlarmMessageSetMemento(entity);
		domain.saveToMemento(memento);

		return entity;
	}
}
