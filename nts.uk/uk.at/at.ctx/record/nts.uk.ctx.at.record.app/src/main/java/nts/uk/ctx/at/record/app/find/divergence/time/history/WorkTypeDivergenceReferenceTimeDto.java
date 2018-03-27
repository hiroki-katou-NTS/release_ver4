package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.util.Optional;

import lombok.Data;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class WorkTypeDivergenceReferenceTimeDto.
 */
@Data
public class WorkTypeDivergenceReferenceTimeDto implements WorkTypeDivergenceReferenceTimeSetMemento{
	
	/** The divergence time no. */
	private int divergenceTimeNo;
	
	/** The not use atr. */
	private int notUseAtr;
	
	/** The divergence reference time value. */
	private DivergenceReferenceTimeValueDto divergenceReferenceTimeValue;
	
	/**
	 * Instantiates a new work type divergence reference time dto.
	 */
	public WorkTypeDivergenceReferenceTimeDto() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setDivergenceTimeNo(java.lang.Integer)
	 */
	@Override
	public void setDivergenceTimeNo(Integer divergenceTimeNo) {
		this.divergenceTimeNo = divergenceTimeNo.intValue();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		//no coding
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setNotUseAtr(nts.uk.shr.com.enumcommon.NotUseAtr)
	 */
	@Override
	public void setNotUseAtr(NotUseAtr notUseAtr) {
		this.notUseAtr = notUseAtr.value;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setWorkTypeCode(nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode)
	 */
	@Override
	public void setWorkTypeCode(BusinessTypeCode workTypeCode) {
		//no coding
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		//no coding
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeSetMemento#setDivergenceReferenceTimeValue(java.util.Optional)
	 */
	@Override
	public void setDivergenceReferenceTimeValue(Optional<DivergenceReferenceTimeValue> divergenceReferenceTimeValue) {
		if (divergenceReferenceTimeValue.isPresent()){
			this.divergenceReferenceTimeValue = new DivergenceReferenceTimeValueDto(divergenceReferenceTimeValue.get().getAlarmTime().get().valueAsMinutes(),
														divergenceReferenceTimeValue.get().getErrorTime().get().valueAsMinutes());
		}
	}

}
