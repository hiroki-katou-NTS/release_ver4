package nts.uk.ctx.at.record.dom.worktime;

import java.util.Optional;

import lombok.Getter;
//import lombok.NoArgsConstructor;
//import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 勤怠打刻(実打刻付き)
 *
 */
@Getter
public class TimeActualStamp {
	
	private Optional<WorkStamp> actualStamp;
	
	private Optional<WorkStamp> stamp = Optional.empty();
	
	private Integer numberOfReflectionStamp;
	
	/**
	 * 打刻時間を指定時間分経過させた勤怠打刻を返す
	 * @param moveTime　指定時間
	 * @return　勤怠打刻
	 */
	public TimeActualStamp moveAheadStampTime(int moveTime) {
		WorkStamp actualWorkStamp = new WorkStamp(this.actualStamp.isPresent() && this.actualStamp.get().getAfterRoundingTime()!=null?this.actualStamp.get().getAfterRoundingTime().forwardByMinutes(moveTime):null,
				  								  this.actualStamp.isPresent() && this.actualStamp.get().getTimeWithDay()!=null?this.actualStamp.get().getTimeWithDay().forwardByMinutes(moveTime):null,
				  								  this.actualStamp.isPresent() && this.actualStamp.get().getLocationCode().isPresent() ? this.actualStamp.get().getLocationCode().get() : null,
				  								  this.actualStamp.isPresent() ? this.actualStamp.get().getStampSourceInfo():StampSourceInfo.STAMP_LEAKAGE_CORRECTION);

		WorkStamp stamp = new WorkStamp(this.stamp.isPresent() && this.stamp.get().getAfterRoundingTime()!=null?this.stamp.get().getAfterRoundingTime().forwardByMinutes(moveTime):null,
										this.stamp.isPresent() && this.stamp.get().getTimeWithDay()!=null?this.stamp.get().getTimeWithDay().forwardByMinutes(moveTime):null,
										this.stamp.isPresent() && this.stamp.get().getLocationCode().isPresent() ? this.stamp.get().getLocationCode().get() : null,
										this.stamp.isPresent() ? this.stamp.get().getStampSourceInfo() : StampSourceInfo.STAMP_LEAKAGE_CORRECTION);
		
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
		WorkStamp actualWorkStamp = new WorkStamp(this.actualStamp.isPresent() && this.actualStamp.get().getAfterRoundingTime()!=null?this.actualStamp.get().getAfterRoundingTime().backByMinutes(moveTime):null,
												  this.actualStamp.isPresent() && this.actualStamp.get().getTimeWithDay()!=null?this.actualStamp.get().getTimeWithDay().backByMinutes(moveTime):null,
												  this.actualStamp.isPresent() && this.actualStamp.get().getLocationCode().isPresent() ? this.actualStamp.get().getLocationCode().get() : null,
											      this.actualStamp.isPresent() ? this.actualStamp.get().getStampSourceInfo() : StampSourceInfo.STAMP_LEAKAGE_CORRECTION);

		WorkStamp stamp = new WorkStamp(this.stamp.isPresent() && this.stamp.get().getAfterRoundingTime()!=null?this.stamp.get().getAfterRoundingTime().backByMinutes(moveTime):null,
										this.stamp.isPresent() && this.stamp.get().getTimeWithDay()!=null?this.stamp.get().getTimeWithDay().backByMinutes(moveTime):null,
										this.stamp.isPresent() && this.stamp.get().getLocationCode().isPresent() ? this.stamp.get().getLocationCode().get() : null,
										this.stamp.isPresent() ? this.stamp.get().getStampSourceInfo() : StampSourceInfo.STAMP_LEAKAGE_CORRECTION);
		
		return new TimeActualStamp(actualWorkStamp,
								   stamp,
                				   this.numberOfReflectionStamp);
	}
	public TimeActualStamp() {
		super();
		this.actualStamp = Optional.empty();
		this.stamp = Optional.empty();
		this.numberOfReflectionStamp = 0;
	}
	public TimeActualStamp(WorkStamp actualStamp, WorkStamp stamp, Integer numberOfReflectionStamp) {
		super();
		this.actualStamp = Optional.ofNullable(actualStamp);
		this.stamp = Optional.ofNullable(stamp);
		this.numberOfReflectionStamp = numberOfReflectionStamp;
	}
	public void setPropertyTimeActualStamp(Optional<WorkStamp> actualStamp, Optional<WorkStamp> stamp, Integer numberOfReflectionStamp){
		this.actualStamp = actualStamp == null ? Optional.empty() : actualStamp;
		this.stamp = stamp == null ? Optional.empty() : stamp;
		this.numberOfReflectionStamp = numberOfReflectionStamp;
	}
	
	/** 「打刻」を削除する */
	public void removeStamp() {
		this.stamp = Optional.empty();
	}
	
	public void setStampFromPcLogOn(TimeWithDayAttr pcLogOnStamp) {
		if(this.stamp.isPresent()) {
			this.stamp.get().setStampFromPcLogOn(pcLogOnStamp);
		}
		if(this.actualStamp.isPresent()) {
			this.actualStamp.get().setStampFromPcLogOn(pcLogOnStamp);
		}
	}
	
	/**
	 * 打刻(Stamp)が計算できる状態であるか判定する
	 * (null になっていないか) 
	 * @return 計算できる状態である
	 */
	public boolean isCalcStampState() {
		if(this.getStamp() != null && this.getStamp().isPresent()) {
			return true;
		}
		return false;
	}
	
	public void updateStamp(WorkStamp stamp){
		this.stamp = Optional.ofNullable(stamp);
	}
}
