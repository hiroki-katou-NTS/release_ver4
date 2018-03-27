package nts.uk.ctx.at.record.infra.repository.divergence.time.reference;

import java.math.BigDecimal;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeGetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrt;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaWorkTypeDivergenceReferenceTimeGetMemento.
 */
public class JpaWorkTypeDivergenceReferenceTimeGetMemento implements WorkTypeDivergenceReferenceTimeGetMemento {

	/** The entity. */
	private KrcstDrt entity;

	/** The work type code. */
	private BusinessTypeCode workTypeCode;

	/**
	 * Instantiates a new jpa work type divergence reference time get memento.
	 */
	public JpaWorkTypeDivergenceReferenceTimeGetMemento() {
	}

	/**
	 * Instantiates a new jpa work type divergence reference time get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWorkTypeDivergenceReferenceTimeGetMemento(KrcstDrt entity, BusinessTypeCode workTypeCode) {
		this.entity = entity;
		this.workTypeCode = workTypeCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return this.entity.getId().getDvgcTimeNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getNotUseAtr()
	 */
	@Override
	public NotUseAtr getNotUseAtr() {
		return NotUseAtr.valueOf(this.entity.getDvgcTimeUseSet().intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getWorkTypeCode()
	 */
	@Override
	public BusinessTypeCode getWorkTypeCode() {
		return this.workTypeCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.entity.getId().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getDivergenceReferenceTimeValue()
	 */
	@Override
	public Optional<DivergenceReferenceTimeValue> getDivergenceReferenceTimeValue() {
		BigDecimal alarmTime = this.entity.getAlarmTime();
		BigDecimal errorTime = this.entity.getErrorTime();

		return Optional.of(new DivergenceReferenceTimeValue(
				new DivergenceReferenceTime(alarmTime == null ? 0 : alarmTime.intValue()),
				new DivergenceReferenceTime(errorTime == null ? 0 : errorTime.intValue())));
	}

}
