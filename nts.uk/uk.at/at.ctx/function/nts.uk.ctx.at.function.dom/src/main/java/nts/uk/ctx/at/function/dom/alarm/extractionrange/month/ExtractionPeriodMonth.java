package nts.uk.ctx.at.function.dom.alarm.extractionrange.month;

import lombok.Getter;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.ExtractionRangeBase;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.NumberOfMonth;

/**
 * @author TruongQuocPhong
 * 抽出期間（月単位）
 */

@Getter
public class ExtractionPeriodMonth extends ExtractionRangeBase{
	
	/**開始日*/
	private StartMonth startMonth;
	
	/**終了日*/
	private EndMonth endMonth;
	
	private NumberOfMonth numberOfMonth;
	
	public ExtractionPeriodMonth(String extractionId, int extractionRange, StartMonth startMonth, EndMonth endMonth, NumberOfMonth  numberOfMonth) {
		super(extractionId, extractionRange);
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.numberOfMonth = numberOfMonth;
	}
}
