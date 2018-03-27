package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.Optional;

import lombok.Data;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeValue;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class ComDivergenceRefTimeSaveDto.
 */
@Data
public class ComDivergenceRefTimeSaveDto implements CompanyDivergenceReferenceTimeGetMemento{
	
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
	 * Instantiates a new com divergence ref time save dto.
	 */
	public ComDivergenceRefTimeSaveDto() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento#getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return new Integer(this.divergenceTimeNo);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento#getNotUseAtr()
	 */
	@Override
	public NotUseAtr getNotUseAtr() {
		return NotUseAtr.valueOf(this.notUseAtr);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.historyId;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeGetMemento#getDivergenceReferenceTimeValue()
	 */
	@Override
	public Optional<DivergenceReferenceTimeValue> getDivergenceReferenceTimeValue() {
		DivergenceReferenceTimeValue settingValue = new DivergenceReferenceTimeValue(new DivergenceReferenceTime(this.alarmTime),
				new DivergenceReferenceTime(this.errorTime));
		return Optional.of(settingValue);
	}
	
}
