package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimeEaMsg;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaDivergenceTimeErrorAlarmMessageGetMemento.
 */
public class JpaDivergenceTimeErrorAlarmMessageGetMemento implements DivergenceTimeErrorAlarmMessageGetMemento {

	/** The entity. */
	private KrcstDvgcTimeEaMsg entity;

	/**
	 * Instantiates a new jpa divergence time error alarm message get memento.
	 */
	public JpaDivergenceTimeErrorAlarmMessageGetMemento() {
	}

	/**
	 * Instantiates a new jpa divergence time error alarm message get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaDivergenceTimeErrorAlarmMessageGetMemento(KrcstDvgcTimeEaMsg entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageGetMemento#getCId()
	 */
	@Override
	public CompanyId getCId() {
		return new CompanyId(this.entity.getId().getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return this.entity.getId().getDvgcTimeNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageGetMemento#getAlarmMessage()
	 */
	@Override
	public Optional<ErrorAlarmMessage> getAlarmMessage() {
		return Optional.of(new ErrorAlarmMessage(this.entity.getAlarmMessage()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageGetMemento#getErrorMessage()
	 */
	@Override
	public Optional<ErrorAlarmMessage> getErrorMessage() {
		return Optional.of(new ErrorAlarmMessage(this.entity.getErrorMessage()));
	}

}
