package nts.uk.ctx.at.function.dom.alarm.extractionrange.month;

import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.PreviousClassification;
/**
 * 抽出期間（月単位）
 * @author phongtq
 *
 */
@Getter
public class EndMonth {
	
	/** 終了月の指定方法 */
	private SpecifyEndMonth specifyEndMonth;
	
	/** 開始月からの抽出期間 */
	private ExtractFromStartMonth extractFromStartMonth;
	
	/** 月数指定 */
	private Optional<MonthNo> endMonthNo = Optional.empty();
	

	
	public EndMonth(int specifyEndMonth) {
		this.specifyEndMonth = EnumAdaptor.valueOf(specifyEndMonth, SpecifyEndMonth.class);
	}
	
	public void setEndMonthNo(PreviousClassification monthPrevious, int endMonthNo, boolean currentMonth) {
		this.endMonthNo = Optional.of(new MonthNo(monthPrevious, endMonthNo, currentMonth));
	}

	public EndMonth(SpecifyEndMonth specifyEndMonth, ExtractFromStartMonth extractFromStartMonth,
			MonthNo endMonthNo) {
		super();
		this.specifyEndMonth = specifyEndMonth;
		this.extractFromStartMonth = extractFromStartMonth;
		this.endMonthNo = Optional.ofNullable(endMonthNo);
	}
	
	
}
