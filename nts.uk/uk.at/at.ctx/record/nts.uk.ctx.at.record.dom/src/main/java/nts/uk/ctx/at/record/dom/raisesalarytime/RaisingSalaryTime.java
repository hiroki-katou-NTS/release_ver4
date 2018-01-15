package nts.uk.ctx.at.record.dom.raisesalarytime;

import java.math.BigDecimal;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.raisesalarytime.primitivevalue.RaisingSalaryTimeItemNo;

/**
 * 
 * @author nampt 加給時間
 *
 */
@Getter
public class RaisingSalaryTime {

	// 加給NO
	private RaisingSalaryTimeItemNo raisingSalaryNo;

	// 計算付き時間 - primitive value
	// 加給時間
	private TimeWithCalculation raisingSalaryTime;

	// 計算付き時間 - primitive value
	// 法定外加給時間
	private TimeWithCalculation outOfLegalRaisingSalaryTime;

	// 計算付き時間 - primitive value
	// 法定内加給時間
	private TimeWithCalculation inLegalRaisingSalaryTime;

}
