package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.ScheduleRecordClassifi;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 勤怠打刻
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).日の勤怠計算.日別勤怠.Common.勤怠打刻
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class WorkStamp extends DomainObject{

	/*
	 * 時刻
	 */
	private WorkTimeInformation timeDay;
	
	/*
	 * 場所コード
	 */
	private Optional<WorkLocationCD> locationCode;
	
	/**
	 * 自動セットで作る
	 * @param time 	時刻
	 * @return
	 */
	public static WorkStamp createByAutomaticSet(TimeWithDayAttr time) {
		
		return new WorkStamp(
				WorkTimeInformation.createByAutomaticSet(time),
				Optional.empty());
	}
	

	public WorkStamp(TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			TimeChangeMeans timeChangeMeans,EngravingMethod engravingMethod) {
		super();
		this.timeDay = new WorkTimeInformation(new ReasonTimeChange(timeChangeMeans, Optional.ofNullable(engravingMethod)), timeWithDay);
		this.locationCode = Optional.ofNullable(locationCode);
	}
	
	public WorkStamp(TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			TimeChangeMeans timeChangeMeans) {
		super();
		this.timeDay = new WorkTimeInformation(new ReasonTimeChange(timeChangeMeans, Optional.empty()), timeWithDay);
		this.locationCode = Optional.ofNullable(locationCode);
	}
	
	
	public void setPropertyWorkStamp(TimeWithDayAttr timeWithDay, WorkLocationCD locationCode,
			TimeChangeMeans timeChangeMeans){
		this.timeDay = new WorkTimeInformation(new ReasonTimeChange(timeChangeMeans, Optional.empty()), timeWithDay);
		this.locationCode = Optional.ofNullable(locationCode);
		
	}
	
	public boolean isFromSPR() {
		return this.timeDay.getReasonTimeChange().getTimeChangeMeans() == TimeChangeMeans.SPR_COOPERATION;
	}
	
	public void setStampFromPcLogOn(TimeWithDayAttr PcLogOnStamp) {
		this.timeDay.setTimeWithDay(Optional.ofNullable(PcLogOnStamp));
	}

	public WorkStamp(WorkTimeInformation timeDay, Optional<WorkLocationCD> locationCode) {
		super();
		this.timeDay = timeDay;
		this.locationCode = locationCode;
	}
	
	/**
	 * ＜
	 * @param compareValue 比較値
	 * @return
	 */
	public boolean lessThan(WorkStamp compareValue) {
		if(this.getTimeDay().getTimeWithDay().isPresent() && compareValue.getTimeDay().getTimeWithDay().isPresent()) {
			return this.getTimeDay().getTimeWithDay().get().lessThan(compareValue.getTimeDay().getTimeWithDay().get());
		}

		return false;
	}
	
	public static WorkStamp createDefault() {
		return new WorkStamp(
				new WorkTimeInformation(
						new ReasonTimeChange(TimeChangeMeans.APPLICATION, Optional.empty()), null),
				Optional.empty());
	}
	
    // 予定実績区分と 勤怠打刻をクリアする
	public void cleanStamp(ScheduleRecordClassifi classification) {
		this.locationCode = Optional.empty();
		this.timeDay.setTimeWithDay(Optional.empty());
		if (classification == ScheduleRecordClassifi.RECORD) {
			this.timeDay.getReasonTimeChange().setTimeChangeMeans(TimeChangeMeans.APPLICATION);
		}
	}
}
