package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsg;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsgPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaDivergenceTimeErrorAlarmMessageSetMemento.
 */
public class JpaDivergenceTimeErrorAlarmMessageSetMemento implements DivergenceTimeErrorAlarmMessageSetMemento {
	/** The entity. */
	private KrcstDvgcTimeEaMsg entity;

	/**
	 * Instantiates a new jpa divergence time error alarm message get memento.
	 */
	public JpaDivergenceTimeErrorAlarmMessageSetMemento() {
	}

	/**
	 * Instantiates a new jpa divergence time error alarm message get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaDivergenceTimeErrorAlarmMessageSetMemento(KrcstDvgcTimeEaMsg entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setCId(nts.uk.ctx.at.shared.dom.
	 * common.CompanyId)
	 */
	@Override
	public void setCId(CompanyId cId) {
		KrcstDvgcTimeEaMsgPK pk = new KrcstDvgcTimeEaMsgPK();
		pk.setCid(cId.v());
		this.entity.setId(pk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setDivergenceTimeNo(java.lang.
	 * Integer)
	 */
	@Override
	public void setDivergenceTimeNo(Integer divergenceTimeNo) {
		this.entity.getId().setDvgcTimeNo(divergenceTimeNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setAlarmMessage(java.util.Optional)
	 */
	@Override
	public void setAlarmMessage(Optional<ErrorAlarmMessage> alarmMessage) {
		this.entity.setAlarmMessage(alarmMessage.isPresent() ? alarmMessage.get().v() : null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setErrorMessage(java.util.Optional)
	 */
	@Override
	public void setErrorMessage(Optional<ErrorAlarmMessage> errorMessage) {
		this.entity.setErrorMessage(errorMessage.isPresent() ? errorMessage.get().v() : null);
	}

}
