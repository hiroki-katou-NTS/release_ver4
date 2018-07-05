package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.shr.com.context.AppContexts;

/**
 * 休出深夜
 * @author keisuke_hoshina
 *
 */
@Getter
@AllArgsConstructor
public class HolidayMidnightWork {
	private List<HolidayWorkMidNightTime> holidayWorkMidNightTime;
	
	/**
	 * リストの時間を時間、計算時間毎に全て加算する
	 * @return
	 */
	public TimeDivergenceWithCalculation calcTotalTime() {
		AttendanceTime calcTime = new AttendanceTime(0);
		AttendanceTime time = new AttendanceTime(0);
		for(int listNo = 1 ; listNo <= this.holidayWorkMidNightTime.size() ; listNo++) {
			time = time.addMinutes(this.holidayWorkMidNightTime.get(listNo - 1).getTime().getTime().valueAsMinutes());
			calcTime = calcTime.addMinutes(this.holidayWorkMidNightTime.get(listNo - 1).getTime().getTime().valueAsMinutes());
		}
		return TimeDivergenceWithCalculation.createTimeWithCalculation(time, calcTime);
	}
	
	/**
	 * 深夜時間のエラーチェック 
	 * @return
	 */
	public List<EmployeeDailyPerError> getErrorList(String employeeId,
			   										GeneralDate targetDate,
			   										AttendanceItemDictionaryForCalc attendanceItemDictionary,
			   										ErrorAlarmWorkRecordCode errorCode){
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		Map<StaturoryAtrOfHolidayWork,String> wordList = new HashMap<StaturoryAtrOfHolidayWork, String>();
		wordList.put(StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork, "法内休出外深夜");
		wordList.put(StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork, "法外休出外深夜");
		wordList.put(StaturoryAtrOfHolidayWork.PublicHolidayWork, "就外法外祝日深夜");
		for(HolidayWorkMidNightTime frameTime:this.holidayWorkMidNightTime) {
			if(frameTime.isOverLimitDivergenceTime()) {
				val itemId = attendanceItemDictionary.findId(wordList.get(frameTime.getStatutoryAtr()));
				if(itemId.isPresent())
					returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
			}
		}
		return returnErrorList;
	}
	
	/**
	 *乖離時間を再計算 
	 * @return
	 */
	public HolidayMidnightWork calcDiverGenceTime(){
		if(this.holidayWorkMidNightTime.isEmpty()) {
			return this;
		}
		List<HolidayWorkMidNightTime> list = new ArrayList<>();
		for(HolidayWorkMidNightTime midNightTime:this.holidayWorkMidNightTime) {
			list.add(midNightTime.calcDiverGenceTime());
		}
		return new HolidayMidnightWork(list);
	}
	
	/**
	 * 深夜時間(全List分)の計算時間合計を求める 
	 * @return
	 */
	public AttendanceTime calcAllMidCalcTime() {
		return new AttendanceTime(this.getHolidayWorkMidNightTime().stream()
												.map(tc -> tc.getTime().getCalcTime().valueAsMinutes())
												.collect(Collectors.summingInt(tc -> tc)));
	}
	
	/**
	 * 深夜時間(全List分)の時間合計を求める 
	 * @return
	 */
	public TimeDivergenceWithCalculation calcAllMidTime() {
		AttendanceTime time = new AttendanceTime(this.getHolidayWorkMidNightTime().stream()
												.map(tc -> tc.getTime().getTime().valueAsMinutes())
												.collect(Collectors.summingInt(tc -> tc)));
		AttendanceTime calcTime = new AttendanceTime(this.getHolidayWorkMidNightTime().stream()
																					  .map(tc -> tc.getTime().getCalcTime().valueAsMinutes())
																					  .collect(Collectors.summingInt(tc -> tc)));
		return TimeDivergenceWithCalculation.createTimeWithCalculation(time, calcTime);
	}
	
	public void replaceValueBypcLogInfo(List<HolidayWorkMidNightTime> pcLogInfo) {
		List<HolidayWorkMidNightTime> copyList = this.holidayWorkMidNightTime;
		pcLogInfo.forEach(tc ->{
			val getItemByAtr = copyList.stream().filter(ts -> ts.getStatutoryAtr().equals(tc.getStatutoryAtr())).findFirst();
			if(getItemByAtr.isPresent()) {
				copyList.forEach(tt ->{
					if(tc.getStatutoryAtr().equals(tt.getStatutoryAtr())) {
						tt.getTime().replaceTimeAndCalcDiv(tc.getTime().getCalcTime());
					}
				});
			}
			else {
				copyList.add(tc);
			}
		});
		this.holidayWorkMidNightTime = copyList;
	}
}
