package nts.uk.ctx.at.request.dom.application.common.adapter.closure;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.closure.ClosureMonth;
//現在の締め期間
@Value
public class PresentClosingPeriodImport {
	// 処理年月
	private YearMonth processingYm;

	// 締め開始日
	private GeneralDate closureStartDate;

	// 締め終了日
	private GeneralDate closureEndDate;

	//締めID
	private final int closureId;
	
	//締め日
	private final ClosureDate closureDate;
	
	public ClosureMonth toClosureMonth() {
		return new ClosureMonth(processingYm, closureId, closureDate);
	}

	}
