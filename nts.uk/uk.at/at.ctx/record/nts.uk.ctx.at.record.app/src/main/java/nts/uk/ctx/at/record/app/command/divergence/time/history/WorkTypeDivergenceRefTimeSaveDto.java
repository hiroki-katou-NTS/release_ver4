package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.Optional;

import lombok.Data;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeGetMemento;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class WorkTypeDivergenceRefTimeSaveDto.
 */
@Data
public class WorkTypeDivergenceRefTimeSaveDto implements WorkTypeDivergenceReferenceTimeGetMemento {

	/** The work type codes. */
	private String workTypeCodes;

	/** The divergence time no. */
	private int divergenceTimeNo;

	/** The not use atr. */
	private int notUseAtr;

	/** The history id. */
	private String historyId;

	/** The alarm time. */
	private int alarmTime;

	/** The error time. */
	private int errorTime;

	/**
	 * Instantiates a new work type divergence ref time save dto.
	 */
	public WorkTypeDivergenceRefTimeSaveDto() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return new Integer(this.divergenceTimeNo);
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
		return NotUseAtr.valueOf(this.notUseAtr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getWorkTypeCode()
	 */
	@Override
	public BusinessTypeCode getWorkTypeCode() {
		return new BusinessTypeCode(this.workTypeCodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeGetMemento#getDivergenceReferenceTimeValue
	 * ()
	 */
	@Override
	public Optional<DivergenceReferenceTimeValue> getDivergenceReferenceTimeValue() {
		DivergenceReferenceTimeValue settingValue = new DivergenceReferenceTimeValue(
				new DivergenceReferenceTime(this.alarmTime), new DivergenceReferenceTime(this.errorTime));
		return Optional.of(settingValue);
	}
}
