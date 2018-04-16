package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.Optional;

import lombok.Data;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class CompanyDivergenceReferenceTimeDto.
 */
@Data
public class CompanyDivergenceReferenceTimeDto implements CompanyDivergenceReferenceTimeSetMemento {

	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The not use atr. */
	private int notUseAtr;

	/** The divergence reference time value. */
	private DivergenceReferenceTimeValueDto divergenceReferenceTimeValue;

	/**
	 * Instantiates a new company divergence reference time dto.
	 */
	public CompanyDivergenceReferenceTimeDto() {
		super();
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
		this.divergenceTimeNo = divergenceTimeNo.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		// no coding
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
		this.notUseAtr = notUseAtr.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		// no coding
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
		if (divergenceReferenceTimeValue.isPresent()) {
			Integer alarmTime = divergenceReferenceTimeValue.get().getAlarmTime().isPresent()
					? divergenceReferenceTimeValue.get().getAlarmTime().get().valueAsMinutes()
					: null;
			Integer errorTime = divergenceReferenceTimeValue.get().getErrorTime().isPresent()
					? divergenceReferenceTimeValue.get().getErrorTime().get().valueAsMinutes()
					: null;

			this.divergenceReferenceTimeValue = new DivergenceReferenceTimeValueDto(alarmTime, errorTime);
		}
	}

}
