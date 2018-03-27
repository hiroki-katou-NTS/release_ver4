package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkTypeDivergenceRefTimeHistSaveCommand.
 */
@Getter
@Setter
public class WorkTypeDivergenceRefTimeHistSaveCommand implements WorkTypeDivergenceReferenceTimeHistoryGetMemento{
	
	/** The work type code. */
	private String workTypeCodes;
	
	/** The history id. */
	private String historyId;
	
	/** The start date. */
	private String startDate;
	
	/** The end date. */
	private String endDate;
	
	/** The is copy data. */
	private int isCopyData;
	
	public WorkTypeDivergenceRefTimeHistSaveCommand() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getWorkTypeCode()
	 */
	@Override
	public BusinessTypeCode getWorkTypeCode() {
		return new BusinessTypeCode(this.workTypeCodes);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryGetMemento#getHistoryItems()
	 */
	@Override
	public List<DateHistoryItem> getHistoryItems() {
		String histId = null;
		if(StringUtils.isEmpty(this.historyId)){
			histId = UUID.randomUUID().toString();
		} else {
			histId = this.historyId;
		}
		
		List<DateHistoryItem> list = new ArrayList<DateHistoryItem>();
		
		DatePeriod period = new DatePeriod(GeneralDate.fromString(this.startDate, "yyyy/MM/dd"), GeneralDate.fromString(this.endDate, "yyyy/MM/dd"));
		list.add(new DateHistoryItem(histId, period));
		return list;
	}
}
