package nts.uk.ctx.at.record.dom.breakorgoout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;

/**
 * 
 * @author nampt
 * 休憩時間帯
 *
 */
@Getter
@AllArgsConstructor
public class BreakTimeSheet extends DomainObject {
	
	//休憩枠NO
	private BreakFrameNo breakFrameNo;
	
	//開始 - 勤怠打刻(実打刻付き)
	private WorkStamp startTime;
	
	//終了 - 勤怠打刻(実打刻付き)
	private WorkStamp endTime;
	
	/**
	 * 指定された時間帯に重複する休憩時間帯の重複時間（分）を返す
	 * @param baseTimeSheet
	 * @return　重複する時間（分）　　重複していない場合は0を返す
	 */
	public int calculateMinutesDuplicatedWith(TimeSpanForCalc baseTimeSheet) {
		return baseTimeSheet.getDuplicatedWith(baseTimeSheet)
				.map(ts -> ts.lengthAsMinutes())
				.orElse(0);
	}
}
