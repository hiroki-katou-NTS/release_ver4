package nts.uk.ctx.at.record.dom.worktime;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author nampt
 * 勤怠打刻(実打刻付き)
 *
 */
@Getter
@AllArgsConstructor
public class TimeActualStamp {
	
	private WorkStamp actualStamp;
	
	private Optional<WorkStamp> stamp;
	
	private int numberOfReflectionStamp;
	
	/**
	 * 打刻時間を指定時間分経過させた勤怠打刻を返す
	 * @param moveTime　指定時間
	 * @return　勤怠打刻
	 */
	public TimeActualStamp moveAheadStampTime(int moveTime) {
		WorkStamp actualWorkStamp = new WorkStamp(this.actualStamp.getAfterRoundingTime().forwardByMinutes(moveTime),
												  this.actualStamp.getTimeWithDay().forwardByMinutes(moveTime),
												  this.actualStamp.getLocationCode(),
												  this.actualStamp.getStampSourceInfo());
		
		WorkStamp stamp = new WorkStamp(this.stamp.getAfterRoundingTime().forwardByMinutes(moveTime),
										this.stamp.getTimeWithDay().forwardByMinutes(moveTime),
										this.stamp.getLocationCode(),
										this.stamp.getStampSourceInfo());
		
		return new TimeActualStamp( actualWorkStamp,
									stamp,
									this.numberOfReflectionStamp);
	}
	
	/**
	 * 打刻時間を指定時間分戻した勤怠打刻を返す
	 * @param moveTime 指定時間
	 * @return　勤怠打刻
	 */
	public TimeActualStamp moveBackStampTime(int moveTime) {
		WorkStamp actualWorkStamp = new WorkStamp(this.actualStamp.getAfterRoundingTime().backByMinutes(moveTime),
				  this.actualStamp.getTimeWithDay().backByMinutes(moveTime),
				  this.actualStamp.getLocationCode(),
				  this.actualStamp.getStampSourceInfo());

		WorkStamp stamp = new WorkStamp(this.stamp.getAfterRoundingTime().forwardByMinutes(moveTime),
				  this.stamp.getTimeWithDay().backByMinutes(moveTime),
				  this.stamp.getLocationCode(),
				  this.stamp.getStampSourceInfo());
		
		return new TimeActualStamp(actualWorkStamp,
								   stamp,
                				   this.numberOfReflectionStamp);
	}
}
