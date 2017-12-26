package nts.uk.ctx.at.record.dom.shorttimework;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.shorttimework.primitivevalue.ShortWorkTimFrameNo;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 短時間勤務時間帯
 *
 */
@Getter
public class ShortWorkingTimeSheet {

	/** 短時間勤務枠NO: 短時間勤務枠NO*/
	private ShortWorkTimFrameNo shortWorkTimeFrameNo;
	
	/** 育児介護区分: 育児介護区分*/
	private ChildCareAttribute childCareAttr;

	/** 開始: 時刻(日区分付き) */
	private TimeWithDayAttr startTime;
	
	/** 終了: 時刻(日区分付き) */
	private TimeWithDayAttr endTime;
	
	/** 控除時間: 勤怠時間 */
	private AttendanceTime deductionTime;
	
	/** 時間: 勤怠時間 */
	private AttendanceTime shortTime;
	
}
