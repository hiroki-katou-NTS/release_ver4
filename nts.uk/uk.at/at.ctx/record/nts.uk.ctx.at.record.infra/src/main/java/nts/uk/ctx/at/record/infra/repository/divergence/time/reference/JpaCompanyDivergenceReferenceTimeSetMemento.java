package nts.uk.ctx.at.record.infra.repository.divergence.time.reference;

import java.math.BigDecimal;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrt;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaCompanyDivergenceReferenceTimeSetMemento.
 */
public class JpaCompanyDivergenceReferenceTimeSetMemento implements CompanyDivergenceReferenceTimeSetMemento {

	/** The entity. */
	private KrcstDrt entity;

	/**
	 * Instantiates a new jpa company divergence reference time set memento.
	 */
	public JpaCompanyDivergenceReferenceTimeSetMemento() {
	}

	/**
	 * Instantiates a new jpa company divergence reference time set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaCompanyDivergenceReferenceTimeSetMemento(KrcstDrt entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setDivergenceTimeNo(java.lang.
	 * Integer)
	 */
	@Override
	public void setDivergenceTimeNo(Integer divergenceTimeNo) {
		this.entity.getId().setDvgcTimeNo(divergenceTimeNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		// No coding.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setNotUseAtr(nts.uk.shr.com.
	 * enumcommon.NotUseAtr)
	 */
	@Override
	public void setNotUseAtr(NotUseAtr notUseAtr) {
		this.entity.setDvgcTimeUseSet(BigDecimal.valueOf(notUseAtr.value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.entity.getId().setHistId(historyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setDivergenceReferenceTimeValue(java
	 * .util.Optional)
	 */
	@Override
	public void setDivergenceReferenceTimeValue(Optional<DivergenceReferenceTimeValue> divergenceReferenceTimeValue) {
		BigDecimal alarmTime = BigDecimal.ZERO;
		BigDecimal errorTime = BigDecimal.ZERO;

		if (divergenceReferenceTimeValue.isPresent()) {
			if (divergenceReferenceTimeValue.get().getAlarmTime().isPresent()) {
				alarmTime = BigDecimal.valueOf(divergenceReferenceTimeValue.get().getAlarmTime().get().v());
			}
			
			if (divergenceReferenceTimeValue.get().getErrorTime().isPresent()) {
				errorTime = BigDecimal.valueOf(divergenceReferenceTimeValue.get().getErrorTime().get().v());
			}
		}

		this.entity.setAlarmTime(alarmTime);
		this.entity.setErrorTime(errorTime);
	}

}
