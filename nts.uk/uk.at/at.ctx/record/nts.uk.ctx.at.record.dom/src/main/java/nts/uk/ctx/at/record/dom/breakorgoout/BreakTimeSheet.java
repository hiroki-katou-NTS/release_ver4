package nts.uk.ctx.at.record.dom.breakorgoout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.arc.layer.dom.DomainObject;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BreakClassification;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionClassification;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.WorkingBreakTimeAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
//import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

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
	private TimeWithDayAttr startTime;
	
	//終了 - 勤怠打刻(実打刻付き)
	private TimeWithDayAttr endTime;
	
	/** 休憩時間: 勤怠時間 */
	private AttendanceTime breakTime;


	public BreakTimeSheet(BreakFrameNo breakFrameNo, TimeWithDayAttr startTime, TimeWithDayAttr endTime) {
		super();
		this.breakFrameNo = breakFrameNo;
		this.startTime = startTime;
		this.endTime = endTime;
		if(startTime != null && endTime != null){
			this.breakTime = new AttendanceTime(startTime.valueAsMinutes() - endTime.valueAsMinutes());
		} else {
			this.breakTime = AttendanceTime.ZERO;
		}
	}
	
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
	
	/**
	 * 自分自身を控除項目の時間帯に変換する
	 * @return 控除項目の時間帯
	 */
	public TimeSheetOfDeductionItem toTimeSheetOfDeductionItem() {
		return TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(new TimeZoneRounding(this.startTime, this.endTime, new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)),
																			  new TimeSpanForCalc(this.startTime, this.endTime),
																			  Collections.emptyList(),
																			  Collections.emptyList(),
																			  Collections.emptyList(),
																			  Collections.emptyList(),
																			  Optional.empty(),
																			  WorkingBreakTimeAtr.NOTWORKING,
																			  Finally.empty(),
																			  Finally.of(BreakClassification.BREAK),
																			  Optional.empty(),
																			  DeductionClassification.BREAK,
																			  Optional.empty()
																			  );
	}
	
	/**
	 * 就業時間帯マスタの休憩時間帯から実績休憩時間帯への型変化
	 * @param restTimezoneSet　就業時間帯マスタの旧家時間帯
	 * @return 変換後の実績休憩時間帯
	 */
	public static List<BreakTimeSheet> covertFromFixRestTimezoneSet(List<DeductionTime> deductionList) {
		val sortedList = deductionList.stream().sorted((first,second) -> first.getStart().compareTo(second.getStart())).collect(Collectors.toList());
		
		List<BreakTimeSheet> returnList = new ArrayList<>();
		int no = 1;
		for(DeductionTime deductionTime: sortedList) {
			returnList.add(new BreakTimeSheet(new BreakFrameNo(no), deductionTime.getStart(), deductionTime.getEnd(), new AttendanceTime(0)));
			no++;
		}
		return returnList;
	}
}
