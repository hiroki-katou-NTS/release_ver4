package nts.uk.ctx.at.record.dom.adapter.workrule.closure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;

/**
 * The Class PresentClosingPeriodExport.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresentClosingPeriodImport {

	/** The processing ym. */
	// 処理年月
	private YearMonth processingYm;

	/** The closure start date. */
	// 締め開始日
	private GeneralDate closureStartDate;

	/** The closure end date. */
	// 締め終了日
	private GeneralDate closureEndDate;

}
