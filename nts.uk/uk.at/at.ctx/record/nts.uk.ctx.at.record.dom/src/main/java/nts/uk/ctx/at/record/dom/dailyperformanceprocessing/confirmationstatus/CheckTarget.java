package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.YearMonth;

/**
 * @author thanhnx
 * チェック対象
 */
@Data
@AllArgsConstructor
public class CheckTarget {

	private Integer closureId;
	
	private YearMonth yearMonth;
}
