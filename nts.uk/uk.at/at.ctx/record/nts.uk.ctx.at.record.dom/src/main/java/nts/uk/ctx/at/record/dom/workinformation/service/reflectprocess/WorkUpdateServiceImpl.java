package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.remarks.RecordRemarks;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OverTimeRecordAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
//import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class WorkUpdateServiceImpl implements WorkUpdateService{
	@Inject
	private EditStateOfDailyPerformanceRepository dailyReposiroty;
	@Inject
	private AttendanceTimeRepository attendanceTime;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDaily;
	@Inject
	private RemarksOfDailyPerformRepo remarksOfDailyRepo;
	@Inject
	private WorkTimeSettingService workTimeSetting;
	@Override
	public WorkInfoOfDailyPerformance updateWorkTimeType(ReflectParameter para, boolean scheUpdate, WorkInfoOfDailyPerformance dailyInfo) {
		WorkInformation workInfor = new WorkInformation(para.getWorkTimeCode(), para.getWorkTypeCode());
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			dailyInfo = this.dailyInfo(para.getWorkTimeCode(), para.getWorkTypeCode(), dailyInfo);
			
			dailyInfo.setScheduleInfo(workInfor);
			
			lstItem.add(2);	
			lstItem.add(1);
		} else {
			if(para.getWorkTimeCode() == null) {
				this.updateTimeNotReflect(para.getEmployeeId(), para.getDateData());
			}
			lstItem.add(29);	
			lstItem.add(28);
			dailyInfo.setRecordInfo(workInfor);
		}
		
		//日別実績の編集状態
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyInfo;
		
	}
	/**
	 * 申請の時刻がなくて実績の勤務種類区分が休日の場合
	 * 時刻反映する前に予定時間帯を追加しないはいけない
	 * @return
	 */
	private WorkInfoOfDailyPerformance dailyInfo(String workTimeCode, String workTypeCode, WorkInfoOfDailyPerformance dailyInfo) {
		String companyId = AppContexts.user().companyId();
		List<ScheduleTimeSheet> scheduleTimeSheets = new ArrayList<>();
		if(workTimeCode != null) {				
			PredetermineTimeSetForCalc predetermine = workTimeSetting.getPredeterminedTimezone(companyId, workTimeCode, workTypeCode, 1);
			List<TimezoneUse> lstTimezone = predetermine.getTimezones();
			
			lstTimezone.stream().forEach(x -> {
				ScheduleTimeSheet scheIn = new ScheduleTimeSheet(x.getWorkNo(), x.getStart().v(), x.getEnd().v());
				scheduleTimeSheets.add(scheIn);				
			});	
		}
		dailyInfo.setScheduleTimeSheets(scheduleTimeSheets);
		return dailyInfo;
	}
	/**
	 * 日別実績の編集状態
	 * @param employeeId
	 * @param dateData
	 * @param lstItem
	 */
	private void updateEditStateOfDailyPerformance(String employeeId, GeneralDate dateData, List<Integer> lstItem) {
		List<EditStateOfDailyPerformance> lstDaily = new ArrayList<>();
		lstItem.stream().forEach(z -> {
			Optional<EditStateOfDailyPerformance> optItemData = dailyReposiroty.findByKeyId(employeeId, dateData, z);
			if(optItemData.isPresent()) {
				EditStateOfDailyPerformance itemData = optItemData.get();
				EditStateOfDailyPerformance data = new EditStateOfDailyPerformance(itemData.getEmployeeId(), 
						itemData.getAttendanceItemId(), itemData.getYmd(), 
						EditStateSetting.REFLECT_APPLICATION);
				lstDaily.add(data);
			}else {
				EditStateOfDailyPerformance insertData = new EditStateOfDailyPerformance(employeeId, z, dateData, EditStateSetting.REFLECT_APPLICATION);
				lstDaily.add(insertData);
			}
		});
		
		if(!lstDaily.isEmpty()) {
			dailyReposiroty.updateByKeyFlush(lstDaily);
		}
	}

	
	@Override
	public WorkInfoOfDailyPerformance updateScheStartEndTime(TimeReflectPara para, WorkInfoOfDailyPerformance dailyPerfor) {
		if(!para.isStart()
				&& !para.isEnd()) {
			return dailyPerfor;
		}
		ScheduleTimeSheet timeSheet;
		if(dailyPerfor.getScheduleTimeSheets().isEmpty()) {
			timeSheet = new ScheduleTimeSheet(1, 
					para.isStart() && para.getStartTime() != null ? para.getStartTime(): 0,
							para.isEnd() && para.getEndTime() != null ? para.getEndTime() : 0);
		} else {
			List<ScheduleTimeSheet> lstTimeSheetFrameNo = dailyPerfor.getScheduleTimeSheets().stream()					
					.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
			if(lstTimeSheetFrameNo.isEmpty()) {
				timeSheet = new ScheduleTimeSheet(para.getFrameNo(), 
						para.isStart() ? para.getStartTime() == null ? 0 : para.getStartTime() : 0,
						para.isEnd() ? para.getEndTime() == null ? 0 : para.getEndTime() : 0);
			} else {
				ScheduleTimeSheet timeSheetFrameNo = lstTimeSheetFrameNo.get(0);
				timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(), 
						para.isStart() ? para.getStartTime() == null ? 0 : para.getStartTime() : timeSheetFrameNo.getAttendance().v(),
						para.isEnd() ? para.getEndTime() == null ? 0 : para.getEndTime() : timeSheetFrameNo.getLeaveWork().v());
				dailyPerfor.getScheduleTimeSheets().remove(timeSheetFrameNo);
			}
			
		}
		if((para.isStart()
				|| para.isEnd())
				&& (para.getStartTime() != null && para.getEndTime() != null)) {
			dailyPerfor.getScheduleTimeSheets().add(timeSheet);
		}
		//workRepository.updateByKeyFlush(dailyPerfor);
		
		
		//日別実績の編集状態
		//予定開始時刻の項目ID
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.getFrameNo() == 1) {
			if(para.isStart()) {
				lstItem.add(3);	
			}
			if(para.isEnd()) {
				lstItem.add(4);	
			}
		} else {
			if(para.isStart()) {
				lstItem.add(5);	
			}
			if(para.isEnd()) {
				lstItem.add(6);	
			}
		}
		//TODO add lstItem
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyPerfor;
	}	
	@Override
	public AttendanceTimeOfDailyPerformance reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime, 
			boolean isPre, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		
		Optional<OverTimeOfDaily> optOverTimeOfDaily = excessOfStatutory.getOverTimeWork();
		if(!optOverTimeOfDaily.isPresent()) {
			return attendanceTimeData;
		}
		OverTimeOfDaily overTimeOfDaily = optOverTimeOfDaily.get();
		List<OverTimeFrameTime> lstOverTimeWorkFrameTime = overTimeOfDaily.getOverTimeWorkFrameTime();
		if(lstOverTimeWorkFrameTime.isEmpty()) {
			return attendanceTimeData;
		}
		if(isPre) {			
			lstOverTimeWorkFrameTime.stream().forEach(x -> {
				if(mapOvertime.containsKey(x.getOverWorkFrameNo().v())) {
					x.setBeforeApplicationTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
				}
			});	
		} else {
			lstOverTimeWorkFrameTime.stream().forEach(x -> {
				if(mapOvertime.containsKey(x.getOverWorkFrameNo().v())) {
					x.getOverTimeWork().setTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
				}
			});
		}
		

		//attendanceTime.updateFlush(attendanceTimeData);
		//残業時間の編集状態を更新する
		//日別実績の編集状態  予定項目ID=残業時間(枠番)の項目ID
		List<Integer> lstOverTemp = new ArrayList<>();
		if(isPre) {
			lstOverTemp = this.lstPreOvertimeItem();
			for(int i = 1; i <= 10; i++) {
				if(!mapOvertime.containsKey(i)) {
					Integer item = this.lstPreOvertimeItem().get(i - 1); 
					lstOverTemp.remove(item);
				}
			}	
		} else {
			lstOverTemp = this.lstAfterOvertimeItem();
			for(int i = 1; i <= 10; i++) {
				if(!mapOvertime.containsKey(i)) {
					Integer item = this.lstAfterOvertimeItem().get(i - 1); 
					lstOverTemp.remove(item);
				}
			}	
		}
		
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstOverTemp);
		return attendanceTimeData;
	}
	/**
	 * 予定項目ID=残業時間(枠番)の項目ID: 事前申請
	 * @return
	 */
	private List<Integer> lstPreOvertimeItem(){
		List<Integer> lstItem = new ArrayList<Integer>();
		lstItem.add(220);
		lstItem.add(225);
		lstItem.add(230);
		lstItem.add(235);
		lstItem.add(240);
		lstItem.add(245);
		lstItem.add(250);
		lstItem.add(255);
		lstItem.add(260);
		lstItem.add(265);
		return lstItem;		
	}
	/**
	 * 予定項目ID=残業時間(枠番)の項目ID: 事後申請
	 * @return
	 */
	private List<Integer> lstAfterOvertimeItem(){
		List<Integer> lstItem = new ArrayList<Integer>();
		lstItem.add(216);
		lstItem.add(221);
		lstItem.add(226);
		lstItem.add(231);
		lstItem.add(236);
		lstItem.add(241);
		lstItem.add(245);
		lstItem.add(251);
		lstItem.add(256);
		lstItem.add(261);
		return lstItem;		
	}
	
	@Override
	public AttendanceTimeOfDailyPerformance updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre,
			AttendanceTimeOfDailyPerformance attendanceTimeData) {
		if(timeNight < 0) {
			return attendanceTimeData;
		}
		// 所定外深夜時間を反映する		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		ExcessOfStatutoryMidNightTime exMidNightTime = excessOfStatutory.getExcessOfStatutoryMidNightTime();
		ExcessOfStatutoryMidNightTime tmp = new ExcessOfStatutoryMidNightTime(exMidNightTime.getTime(), new AttendanceTime(timeNight));
		if(isPre) {
			excessOfStatutory.setExcessOfStatutoryMidNightTime(tmp);
		} else {
			excessOfStatutory.getExcessOfStatutoryMidNightTime().getTime().setTime(new AttendanceTime(timeNight));
		}
		//attendanceTime.updateFlush(attendanceTimeData);
		//所定外深夜時間の編集状態を更新する
		List<Integer> lstNightItem = new ArrayList<Integer>();//所定外深夜時間の項目ID
		if(isPre) {
			lstNightItem.add(565);
		} else {
			lstNightItem.add(563);
		}
				
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstNightItem);
		
		//休出時間(深夜)の反映
		//this.updateBreakNight(employeeId, dateData);
		return attendanceTimeData;
	}
	@Override
	public AttendanceTimeOfDailyPerformance updateBreakNight(String employeeId, GeneralDate dateData, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		// 所定外深夜時間を反映する
		ActualWorkingTimeOfDaily actualWorkingTimeOfDaily = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();		
		ExcessOfStatutoryTimeOfDaily excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		Optional<HolidayWorkTimeOfDaily> optWorkHolidayTime = excessOfStatutoryTimeOfDaily.getWorkHolidayTime();
		if(!optWorkHolidayTime.isPresent()) {
			return attendanceTimeData;
		}
		HolidayWorkTimeOfDaily workHolidayTime = optWorkHolidayTime.get();
		Finally<HolidayMidnightWork> holidayMidNightWork = workHolidayTime.getHolidayMidNightWork();
		if(!holidayMidNightWork.isPresent()) {
			return attendanceTimeData;
		}
		HolidayMidnightWork holidayWorkMidNightTime = holidayMidNightWork.get();
		List<HolidayWorkMidNightTime> lstHolidayWorkMidNightTime = holidayWorkMidNightTime.getHolidayWorkMidNightTime();
		if(lstHolidayWorkMidNightTime.isEmpty()) {
			return attendanceTimeData;
		}
		lstHolidayWorkMidNightTime.stream().forEach(x -> {
			x.getTime().setTime(new AttendanceTime(0));
		});
		
		//attendanceTime.updateFlush(attendanceTimeData);
		//休出時間(深夜)(法内)の編集状態を更新する
		List<Integer> lstItem = new ArrayList<Integer>();
		//(法定区分=法定外休出)の時間の項目ID ???
		//(法定区分=祝日休出)の時間の項目ID
		//(法定区分=法定外休出)の時間の項目ID
		lstItem.add(570);
		lstItem.add(567);
		lstItem.add(572);
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		return attendanceTimeData;
	}
	@Override
	public AttendanceTimeOfDailyPerformance updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime, boolean isPre,
			AttendanceTimeOfDailyPerformance attendanceTimeData) {
		if(flexTime < 0) {
			return attendanceTimeData;
		}
		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();		
		//ドメインモデル「日別実績の所定外時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		// ドメインモデル「日別実績の残業時間」を取得する
		Optional<OverTimeOfDaily> optOverTimeOfDaily = excessOfStatutory.getOverTimeWork();
		if(!optOverTimeOfDaily.isPresent()) {
			return attendanceTimeData;
		}
		OverTimeOfDaily workHolidayTime = optOverTimeOfDaily.get();
		FlexTime flexTimeData = workHolidayTime.getFlexTime();
		FlexTime temp = new FlexTime(flexTimeData.getFlexTime(), new AttendanceTime(flexTime));
		if(isPre) {
			workHolidayTime.setFlexTime(temp);	
		} else {
			workHolidayTime.getFlexTime().getFlexTime().setTime(new AttendanceTimeOfExistMinus(flexTime));
		}
				
		//attendanceTime.updateFlush(attendanceTimeData);
		//フレックス時間の編集状態を更新する
		//日別実績の編集状態
		List<Integer> lstItem = new ArrayList<Integer>();//フレックス時間の項目ID
		if(isPre) {
			lstItem.add(555);	
		} else {
			lstItem.add(556);
		}
		
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		return attendanceTimeData;
		
	}
	@Override
	public WorkInfoOfDailyPerformance updateRecordWorkType(String employeeId, GeneralDate dateData, String workTypeCode, boolean scheUpdate, WorkInfoOfDailyPerformance dailyPerfor) {
		//日別実績の勤務情報
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(1);
			dailyPerfor.setScheduleInfo(new WorkInformation(dailyPerfor.getScheduleInfo().getWorkTimeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTimeCode().v(), workTypeCode));
		} else {
			lstItem.add(28);
			dailyPerfor.setRecordInfo(new WorkInformation(dailyPerfor.getRecordInfo().getWorkTimeCode() == null ? null : dailyPerfor.getRecordInfo().getWorkTimeCode().v(), workTypeCode));			
		}
		//日別実績の編集状態
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		return dailyPerfor;
	}
	@Override
	public IntegrationOfDaily updateWorkTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> worktimeFrame,
			boolean isPre, IntegrationOfDaily dailyData, boolean isRec) {
		if(dailyData == null || !dailyData.getAttendanceTimeOfDailyPerformance().isPresent()) {
			return dailyData;
		}
		AttendanceTimeOfDailyPerformance attendanceTimeData = dailyData.getAttendanceTimeOfDailyPerformance().get();
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();		
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		//日別実績の休出時間
		Optional<HolidayWorkTimeOfDaily> optWorkHolidayTime = excessOfStatutory.getWorkHolidayTime();
		if(!optWorkHolidayTime.isPresent()) {
			return dailyData;
		}
		HolidayWorkTimeOfDaily workHolidayTime = optWorkHolidayTime.get();
		List<HolidayWorkFrameTime> lstHolidayWorkFrameTime = workHolidayTime.getHolidayWorkFrameTime();
		List<HolidayWorkFrameTime> lstHolidayWorkFrameTimeTmp = new ArrayList<>();		
		List<Integer> lstWorktimeFrameTemp = new ArrayList<>();
		if(isPre) {			
			lstHolidayWorkFrameTime.stream().forEach(x -> {
				if(worktimeFrame.containsKey(x.getHolidayFrameNo().v())) {
					AttendanceTime worktimeTmp = new AttendanceTime(worktimeFrame.get(x.getHolidayFrameNo().v()));
					x.setBeforeApplicationTime(Finally.of(worktimeTmp));
				}
			});	
			lstWorktimeFrameTemp = this.lstPreWorktimeFrameItem();
			for(int i = 1; i <= 10; i++) {
				if(!worktimeFrame.containsKey(i)) {
					
					Integer item = this.lstPreWorktimeFrameItem().get(i - 1); 
					lstWorktimeFrameTemp.remove(item);
				} else {
					if(lstHolidayWorkFrameTime.isEmpty()) {
						AttendanceTime worktimeTmp = new AttendanceTime(worktimeFrame.get(i));
						TimeDivergenceWithCalculation timeCalculation = TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0));
						HolidayWorkFrameTime tmpHolidayWorkFrameTime = new HolidayWorkFrameTime(new HolidayWorkFrameNo(i),
								Finally.of(timeCalculation),
								Finally.of(timeCalculation),
								Finally.of(worktimeTmp));
						lstHolidayWorkFrameTimeTmp.add(tmpHolidayWorkFrameTime);
					}
				}
			}	
		} else {
			lstHolidayWorkFrameTime.stream().forEach(x -> {
				if(worktimeFrame.containsKey(x.getHolidayFrameNo().v())) {
					Finally<TimeDivergenceWithCalculation> holidayWorkTime = x.getHolidayWorkTime();
					if(holidayWorkTime.isPresent()) {
						TimeDivergenceWithCalculation holidayWorkTimeData = holidayWorkTime.get();
						holidayWorkTimeData.setTime(new AttendanceTime(worktimeFrame.get(x.getHolidayFrameNo().v())));
					}
				}
			});
			lstWorktimeFrameTemp = this.lstAfterWorktimeFrameItem();
			for(int i = 1; i <= 10; i++) {
				if(!worktimeFrame.containsKey(i)) {					
					Integer item = this.lstAfterWorktimeFrameItem().get(i - 1); 
					lstWorktimeFrameTemp.remove(item);
				} else {
					if(lstHolidayWorkFrameTime.isEmpty()) {
						AttendanceTime worktimeTmp = new AttendanceTime(worktimeFrame.get(i));
						TimeDivergenceWithCalculation timeCalculation = TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0));
						HolidayWorkFrameTime tmpHolidayWorkFrameTime = new HolidayWorkFrameTime(new HolidayWorkFrameNo(i),
								Finally.of(timeCalculation),
								Finally.of(timeCalculation),
								Finally.of(worktimeTmp));
						lstHolidayWorkFrameTimeTmp.add(tmpHolidayWorkFrameTime);
					}
				}
			}	
		}
		if(!lstHolidayWorkFrameTimeTmp.isEmpty()) {
			attendanceTimeData
			.getActualWorkingTimeOfDaily().getTotalWorkingTime()
			.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().setHolidayWorkFrameTime(lstHolidayWorkFrameTimeTmp);
			
		}
		dailyData.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTimeData));
		//↓ fix bug 103077
		if(!isRec) {
			this.updateEditStateOfDailyPerformance(employeeId, dateData, lstWorktimeFrameTemp);	
		}
		//↑ fix bug 103077
		return dailyData;
	}
	
	private List<Integer> lstPreWorktimeFrameItem(){
		List<Integer> lstItem = new ArrayList<>();
		lstItem.add(270);
		lstItem.add(275);
		lstItem.add(280);
		lstItem.add(285);
		lstItem.add(290);
		lstItem.add(295);
		lstItem.add(300);
		lstItem.add(305);
		lstItem.add(310);
		lstItem.add(315);
		return lstItem;
	}
	private List<Integer> lstAfterWorktimeFrameItem(){
		List<Integer> lstItem = new ArrayList<>();
		lstItem.add(266);
		lstItem.add(271);
		lstItem.add(276);
		lstItem.add(281);
		lstItem.add(286);
		lstItem.add(291);
		lstItem.add(296);
		lstItem.add(301);
		lstItem.add(306);
		lstItem.add(311);
		return lstItem;
	}
	private List<Integer> lstTranfertimeFrameItem(){
		List<Integer> lstItem = new ArrayList<>();		
		lstItem.add(267);
		lstItem.add(272);
		lstItem.add(277);
		lstItem.add(282);
		lstItem.add(287);
		lstItem.add(292);
		lstItem.add(297);
		lstItem.add(302);
		lstItem.add(307);
		lstItem.add(312);
		return lstItem;
	}
	
	@Override
	public TimeLeavingOfDailyPerformance updateRecordStartEndTimeReflect(TimeReflectPara data) {
		Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(data.getEmployeeId(), data.getDateData());
		TimeLeavingOfDailyPerformance timeDaily = null;
		if(optTimeLeaving.isPresent()) {
			timeDaily = optTimeLeaving.get();
		}
		if(!data.isStart()
				&& !data.isEnd()) {
			return timeDaily;
		}
		//開始時刻を反映する
		List<TimeLeavingWork> lstTimeLeavingWorks = new ArrayList<>();
		if(timeDaily != null) {
			lstTimeLeavingWorks = timeDaily.getTimeLeavingWorks().stream()
					.filter(x -> x.getWorkNo().v() == data.getFrameNo()).collect(Collectors.toList());
		}
		TimeLeavingWork timeLeavingWork = null;
		if(lstTimeLeavingWorks.isEmpty()) {
			if(data.getStartTime() == null || data.getEndTime() == null) {
				List<Integer> lstItem = new ArrayList<Integer>();
				if(data.isStart()) {
					if(data.getFrameNo() == 1) {
						lstItem.add(31);
					} else {
						lstItem.add(41);
					}
				}
				if(data.isEnd()) {
					if(data.getFrameNo() == 1) {
						lstItem.add(34);
					} else {
						lstItem.add(44);
					}
				}
				this.updateEditStateOfDailyPerformance(data.getEmployeeId(), data.getDateData(), lstItem);
				return timeDaily;
			}
			WorkStamp workStamp = new WorkStamp(new TimeWithDayAttr(data.getStartTime()),
					new TimeWithDayAttr(data.getEndTime()),
					null,
					StampSourceInfo.GO_STRAIGHT_APPLICATION);
			TimeActualStamp timeActualStamp = new TimeActualStamp(workStamp, workStamp, 0);
			timeLeavingWork = new TimeLeavingWork(new WorkNo(1), timeActualStamp, timeActualStamp);
		} else {
			timeLeavingWork = lstTimeLeavingWorks.get(0);
		}
		Optional<TimeActualStamp> optTimeAttendanceStart = timeLeavingWork.getAttendanceStamp();
		Optional<TimeActualStamp> optTimeAttendanceEnd = timeLeavingWork.getLeaveStamp();
		if(data.isStart() && optTimeAttendanceStart.isPresent()) {
			TimeActualStamp timeAttendanceStart= optTimeAttendanceStart.get();
			Optional<WorkStamp> optStamp = timeAttendanceStart.getStamp();
			WorkStamp stampTmp = null;
			if(optStamp.isPresent()) {
				WorkStamp stamp = optStamp.get();
				stampTmp = new WorkStamp(data.getStartTime() != null ? new TimeWithDayAttr(data.getStartTime()) : null,
						data.getStartTime() != null ? new TimeWithDayAttr(data.getStartTime()) : null,
						stamp.getLocationCode().isPresent() ? stamp.getLocationCode().get() : null,
								StampSourceInfo.GO_STRAIGHT_APPLICATION);
				
			} else {
				if(data.getStartTime() != null) {
					stampTmp = new WorkStamp(new TimeWithDayAttr(data.getStartTime()),
							new TimeWithDayAttr(data.getStartTime()),
							null,
							StampSourceInfo.GO_STRAIGHT_APPLICATION);
				}
				
			}
			TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceStart.getActualStamp().isPresent() ? timeAttendanceStart.getActualStamp().get() : null,
					stampTmp,
					timeAttendanceStart.getNumberOfReflectionStamp());
			optTimeAttendanceStart = Optional.of(timeActualStam);
		}
		if(data.isEnd() && optTimeAttendanceEnd.isPresent()) {			
			TimeActualStamp timeAttendanceEnd = optTimeAttendanceEnd.get();
			Optional<WorkStamp> optStamp = timeAttendanceEnd.getStamp();
			WorkStamp stampTmp = null;
			if(optStamp.isPresent()) {				
				WorkStamp stamp = optStamp.get();
				stampTmp = new WorkStamp(data.getEndTime() != null ? new TimeWithDayAttr(data.getEndTime()) : null,
						data.getEndTime() != null ? new TimeWithDayAttr(data.getEndTime()) : null,
						stamp.getLocationCode().isPresent() ? stamp.getLocationCode().get() : null,
								StampSourceInfo.GO_STRAIGHT_APPLICATION);
			} else {
				if(data.getEndTime() != null) {
					stampTmp = new WorkStamp(new TimeWithDayAttr(data.getEndTime()),
							new TimeWithDayAttr(data.getEndTime()),
							null,
							StampSourceInfo.GO_STRAIGHT_APPLICATION);
				}
			}
			TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceEnd.getActualStamp().isPresent() ? timeAttendanceEnd.getActualStamp().get() : null,
					stampTmp,
					timeAttendanceEnd.getNumberOfReflectionStamp());
			optTimeAttendanceEnd = Optional.of(timeActualStam);
		}
		TimeLeavingWork timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(),
				optTimeAttendanceStart.get(),
				optTimeAttendanceEnd.get());
		if(!lstTimeLeavingWorks.isEmpty()) {
			timeDaily.getTimeLeavingWorks().remove(timeLeavingWork);
			timeDaily.getTimeLeavingWorks().add(timeLeavingWorkTmp);
		} else {
			timeDaily = new TimeLeavingOfDailyPerformance(data.getEmployeeId(), new WorkTimes(1), Arrays.asList(timeLeavingWorkTmp), data.getDateData());
		}
		timeLeavingOfDaily.updateFlush(timeDaily);
		//開始時刻の編集状態を更新する
		//予定項目ID=出勤の項目ID	
		List<Integer> lstItem = new ArrayList<Integer>();
		if(data.isStart()) {
			if(data.getFrameNo() == 1) {
				lstItem.add(31);
			} else {
				lstItem.add(41);
			}
		}
		if(data.isEnd()) {
			if(data.getFrameNo() == 1) {
				lstItem.add(34);
			} else {
				lstItem.add(44);
			}
		}
		this.updateEditStateOfDailyPerformance(data.getEmployeeId(), data.getDateData(), lstItem);
		return timeDaily;
	}
	@Override
	public IntegrationOfDaily updateWorkTimeTypeHoliwork(ReflectParameter para, boolean scheUpdate,
			IntegrationOfDaily dailyData) {		
		WorkInfoOfDailyPerformance dailyPerfor = dailyData.getWorkInformation();
		WorkInformation workInfor = new WorkInformation(para.getWorkTimeCode(), para.getWorkTypeCode());
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			dailyPerfor = this.dailyInfo(para.getWorkTimeCode(), para.getWorkTypeCode(), dailyPerfor);
			lstItem.add(1);
			lstItem.add(2);
			dailyPerfor.setScheduleInfo(workInfor);
			dailyData.setWorkInformation(dailyPerfor);
		} else {
			lstItem.add(28);
			lstItem.add(29);
			dailyPerfor.setRecordInfo(workInfor);
			dailyData.setWorkInformation(dailyPerfor);
		}
		//日別実績の編集状態	
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyData;
	}
	@Override
	public IntegrationOfDaily updateScheStartEndTimeHoliday(TimeReflectPara para, IntegrationOfDaily dailyData) {
		if(para.getStartTime() == null
				|| para.getEndTime() == null) {
			return dailyData;
		}		
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = dailyData.getWorkInformation();
		ScheduleTimeSheet timeSheet;
		if(dailyPerfor.getScheduleTimeSheets().isEmpty()) {
			timeSheet = new ScheduleTimeSheet(1, 
					para.getStartTime(),
					para.getEndTime());
		} else {
			List<ScheduleTimeSheet> lstTimeSheetFrameNo = dailyPerfor.getScheduleTimeSheets().stream()
					.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
			if(lstTimeSheetFrameNo.isEmpty()) {
				return dailyData;
			}
			ScheduleTimeSheet timeSheetFrameNo = lstTimeSheetFrameNo.get(0);
			
			timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(), 
					para.isStart() ? para.getStartTime() : timeSheetFrameNo.getAttendance().v(),
					para.isEnd() ? para.getEndTime() : timeSheetFrameNo.getLeaveWork().v());

			dailyPerfor.getScheduleTimeSheets().remove(timeSheetFrameNo);
		}
		
		dailyPerfor.getScheduleTimeSheets().add(timeSheet);
		dailyData.setWorkInformation(dailyPerfor);
		//workRepository.updateByKeyFlush(dailyPerfor);
		//日別実績の編集状態
		//予定開始時刻の項目ID
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.getFrameNo() == 1) {
			if(para.isStart()) {
				lstItem.add(3);	
			}
			if(para.isEnd()) {
				lstItem.add(4);	
			}
		} else {
			if(para.isStart()) {
				lstItem.add(5);	
			}
			if(para.isEnd()) {
				lstItem.add(6);	
			}
		}
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyData;
	}
	@Override
	public IntegrationOfDaily updateTimeShiftNightHoliday(String employeeId, GeneralDate dateData, Integer timeNight,
			boolean isPre, IntegrationOfDaily dailyData) {
		if(timeNight == null || timeNight < 0) {
			return dailyData;
		}
		// 所定外深夜時間を反映する		
		AttendanceTimeOfDailyPerformance attendanceTimeData = dailyData.getAttendanceTimeOfDailyPerformance().get();		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		ExcessOfStatutoryMidNightTime exMidNightTime = excessOfStatutory.getExcessOfStatutoryMidNightTime();
		ExcessOfStatutoryMidNightTime tmp = new ExcessOfStatutoryMidNightTime(exMidNightTime.getTime(), new AttendanceTime(timeNight));
		if(isPre) {
			excessOfStatutory.setExcessOfStatutoryMidNightTime(tmp);
		} else {
			excessOfStatutory.getExcessOfStatutoryMidNightTime().getTime().setTime(new AttendanceTime(timeNight));
		}
		dailyData.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTimeData));
		//attendanceTime.updateFlush(attendanceTimeData);
		//所定外深夜時間の編集状態を更新する
		List<Integer> lstNightItem = new ArrayList<Integer>();//所定外深夜時間の項目ID
		if(isPre) {
			lstNightItem.add(565);
		} else {
			lstNightItem.add(563);
		}
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstNightItem);
		return dailyData;
	}
	@Override
	public WorkInfoOfDailyPerformance updateRecordWorkTime(String employeeId, GeneralDate dateData, String workTimeCode, boolean scheUpdate,
			WorkInfoOfDailyPerformance dailyPerfor) {
		//日別実績の勤務情報
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(2);
			dailyPerfor.setScheduleInfo(new WorkInformation(workTimeCode, dailyPerfor.getScheduleInfo().getWorkTypeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTypeCode().v()));
			//workRepository.updateByKeyFlush(dailyPerfor);
		} else {
			lstItem.add(29);
			dailyPerfor.setRecordInfo(new WorkInformation(workTimeCode, dailyPerfor.getRecordInfo().getWorkTypeCode() == null ? null : dailyPerfor.getRecordInfo().getWorkTypeCode().v()));
			//workRepository.updateByKeyFlush(dailyPerfor);
		}
		//日別実績の編集状態
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		return dailyPerfor;
		
	}
	@Override
	public AttendanceTimeOfDailyPerformance updateTransferTimeFrame(String employeeId, GeneralDate dateData,
			Map<Integer, Integer> transferTimeFrame, AttendanceTimeOfDailyPerformance attendanceTimeData) {
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();		
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		//日別実績の休出時間
		Optional<HolidayWorkTimeOfDaily> optWorkHolidayTime = excessOfStatutory.getWorkHolidayTime();
		if(!optWorkHolidayTime.isPresent()) {
			return attendanceTimeData;
		}
		HolidayWorkTimeOfDaily workHolidayTime = optWorkHolidayTime.get();
		List<HolidayWorkFrameTime> lstHolidayWorkFrameTime = workHolidayTime.getHolidayWorkFrameTime();
		if(lstHolidayWorkFrameTime.isEmpty()) {
			return attendanceTimeData;
		}
		lstHolidayWorkFrameTime.stream().forEach(x -> {
			if(transferTimeFrame.containsKey(x.getHolidayFrameNo().v())) {
				Finally<TimeDivergenceWithCalculation> finTransferTime = x.getTransferTime();
				TimeDivergenceWithCalculation transferTime = finTransferTime.get();
				transferTime.setTime(new AttendanceTime(transferTimeFrame.get(x.getHolidayFrameNo().v())));
			}
		});		
		attendanceTime.updateFlush(attendanceTimeData);
		
		List<Integer> lstWorktimeFrameTemp = new ArrayList<>();
		lstWorktimeFrameTemp = this.lstTranfertimeFrameItem();
		for(int i = 1; i <= 10; i++) {
			if(!transferTimeFrame.containsKey(i)) {
				Integer item = this.lstTranfertimeFrameItem().get(i - 1); 
				lstWorktimeFrameTemp.remove(item);
			}
		}
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstWorktimeFrameTemp);
		return attendanceTimeData;
	}
	@Override
	public TimeLeavingOfDailyPerformance updateRecordStartEndTimeReflectRecruitment(TimeReflectPara data) {
		Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(data.getEmployeeId(), data.getDateData());
		TimeLeavingOfDailyPerformance timeDaily = null;
		if(optTimeLeaving.isPresent()) {
			timeDaily = optTimeLeaving.get();
		}
		if(!data.isStart()
				&& !data.isEnd()) {
			return timeDaily;
		}
		//開始時刻を反映する
		List<TimeLeavingWork> lstTimeLeavingWorks = new ArrayList<>();
		if(timeDaily != null) {
			lstTimeLeavingWorks = timeDaily.getTimeLeavingWorks().stream()
					.filter(x -> x.getWorkNo().v() == data.getFrameNo()).collect(Collectors.toList());
		}
		TimeLeavingWork timeLeavingWork = null;
		if(lstTimeLeavingWorks.isEmpty()) {
			WorkStamp workStamp = new WorkStamp(new TimeWithDayAttr(data.getStartTime()),
					new TimeWithDayAttr(data.getEndTime()),
					null,
					StampSourceInfo.GO_STRAIGHT_APPLICATION);
			TimeActualStamp timeActualStamp = new TimeActualStamp(workStamp, workStamp, 0);
			timeLeavingWork = new TimeLeavingWork(new WorkNo(1), timeActualStamp, timeActualStamp);
		} else {
			timeLeavingWork = lstTimeLeavingWorks.get(0);
		}
		Optional<TimeActualStamp> optTimeAttendanceStart = timeLeavingWork.getAttendanceStamp();
		Optional<TimeActualStamp> optTimeAttendanceEnd = timeLeavingWork.getLeaveStamp();
		if(data.isStart() && optTimeAttendanceStart.isPresent()) {
			TimeActualStamp timeAttendanceStart= optTimeAttendanceStart.get();
			Optional<WorkStamp> optStamp = timeAttendanceStart.getStamp();
			if(optStamp.isPresent()) {
				WorkStamp stamp = optStamp.get();
				WorkStamp stampTmp = new WorkStamp(new TimeWithDayAttr(data.getStartTime()),
						new TimeWithDayAttr(data.getStartTime()),
						stamp.getLocationCode().isPresent() ? stamp.getLocationCode().get() : null,
								StampSourceInfo.GO_STRAIGHT_APPLICATION);
				TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceStart.getActualStamp().isPresent() ? timeAttendanceStart.getActualStamp().get() : null,
						stampTmp,
						timeAttendanceStart.getNumberOfReflectionStamp());
				optTimeAttendanceStart = Optional.of(timeActualStam);
			}
		}
		if(data.isEnd() && optTimeAttendanceEnd.isPresent()) {			
			TimeActualStamp timeAttendanceEnd = optTimeAttendanceEnd.get();
			Optional<WorkStamp> optStamp = timeAttendanceEnd.getStamp();
			if(optStamp.isPresent()) {				
				WorkStamp stamp = optStamp.get();
				WorkStamp stampTmp = new WorkStamp(new TimeWithDayAttr(data.getStartTime()),
						new TimeWithDayAttr(data.getEndTime()),
						stamp.getLocationCode().isPresent() ? stamp.getLocationCode().get() : null,
								StampSourceInfo.GO_STRAIGHT_APPLICATION);
				TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceEnd.getActualStamp().isPresent() ? timeAttendanceEnd.getActualStamp().get() : null,
						stampTmp,
						timeAttendanceEnd.getNumberOfReflectionStamp());
				optTimeAttendanceEnd = Optional.of(timeActualStam);
			}
		}
		TimeLeavingWork timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(),
				optTimeAttendanceStart.get(),
				optTimeAttendanceEnd.get());
		if(!lstTimeLeavingWorks.isEmpty()) {
			timeDaily.getTimeLeavingWorks().remove(timeLeavingWork);
			timeDaily.getTimeLeavingWorks().add(timeLeavingWorkTmp);
		} else {
			timeDaily = new TimeLeavingOfDailyPerformance(data.getEmployeeId(), new WorkTimes(1), Arrays.asList(timeLeavingWorkTmp), data.getDateData());
		}
		timeLeavingOfDaily.updateFlush(timeDaily);
		//開始時刻の編集状態を更新する
		//予定項目ID=出勤の項目ID	
		List<Integer> lstItem = new ArrayList<Integer>();
		if(data.isStart()) {
			if(data.getFrameNo() == 1) {
				lstItem.add(31);
			} else {
				lstItem.add(41);
			}
		}
		if(data.isEnd()) {
			if(data.getFrameNo() == 1) {
				lstItem.add(34);
			} else {
				lstItem.add(44);
			}
		}
		this.updateEditStateOfDailyPerformance(data.getEmployeeId(), data.getDateData(), lstItem);
		return timeDaily;
	}

	@Override
	public void reflectReason(String sid, GeneralDate appDate, String appReason, OverTimeRecordAtr overTimeAtr) {
		//申請理由の文字の長さをチェックする
		if(appReason.length() > 50) {
			appReason = appReason.substring(0, 50);
		}
		//備考の編集状態を更新する
		List<Integer> lstItem = new ArrayList<>();
		
		int columnNo = 4;
		//残業区分をチェックする
		if(overTimeAtr == OverTimeRecordAtr.PREOVERTIME) {
			columnNo = 3;
			lstItem.add(835);
		} else {
			lstItem.add(836);	
		}
		
		//日別実績の備考を存在チェックする
		Optional<RemarksOfDailyPerform> optRemark = remarksOfDailyRepo.getByKeys(sid, appDate, columnNo);		
		if(optRemark.isPresent()) {
			RemarksOfDailyPerform remarkData = optRemark.get();
			remarkData.setRemarks(new RecordRemarks(appReason));
			//日別実績の備考を変更する
			remarksOfDailyRepo.update(remarkData);
		} else {
			RemarksOfDailyPerform remarkInfo = new RemarksOfDailyPerform(sid,
					appDate, 
					new RecordRemarks(appReason), 
					columnNo);
			//日別実績の備考を追加する
			remarksOfDailyRepo.add(remarkInfo);
		}

		this.updateEditStateOfDailyPerformance(sid, appDate, lstItem);
	}
	@Override
	public void updateTimeNotReflect(String employeeId, GeneralDate dateData) {
		Optional<TimeLeavingOfDailyPerformance> optTimeLeaving = timeLeavingOfDaily.findByKey(employeeId, dateData);
		if(!optTimeLeaving.isPresent()) {
			return;
		}
		TimeLeavingOfDailyPerformance timeDaily = optTimeLeaving.get();

		//開始時刻を反映する
		List<TimeLeavingWork> lstTimeLeavingWorks = timeDaily.getTimeLeavingWorks().stream()
					.filter(x -> x.getWorkNo().v() == 1).collect(Collectors.toList());
		TimeLeavingWork timeLeavingWork = null;
		if(!lstTimeLeavingWorks.isEmpty()) {
			timeLeavingWork = lstTimeLeavingWorks.get(0);
		} else {
			return;
		}
		Optional<TimeActualStamp> optTimeAttendanceStart = timeLeavingWork.getAttendanceStamp();
		
		Optional<TimeActualStamp> optTimeAttendanceEnd = timeLeavingWork.getLeaveStamp();
		if(optTimeAttendanceStart.isPresent()) {
			TimeActualStamp timeAttendanceStart= optTimeAttendanceStart.get();
			Optional<WorkStamp> optStamp = timeAttendanceStart.getStamp();
			WorkStamp stampTmp = null;
			if(optStamp.isPresent()) {
				TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceStart.getActualStamp().isPresent() ? timeAttendanceStart.getActualStamp().get() : null,
						stampTmp,
						timeAttendanceStart.getNumberOfReflectionStamp());
				optTimeAttendanceStart = Optional.of(timeActualStam);
			}
		}
		if(optTimeAttendanceEnd.isPresent()) {			
			TimeActualStamp timeAttendanceEnd = optTimeAttendanceEnd.get();
			Optional<WorkStamp> optStamp = timeAttendanceEnd.getStamp();
			WorkStamp stampTmp = null;
			if(optStamp.isPresent()) {				
				TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendanceEnd.getActualStamp().isPresent() ? timeAttendanceEnd.getActualStamp().get() : null,
						stampTmp,
						timeAttendanceEnd.getNumberOfReflectionStamp());
				optTimeAttendanceEnd = Optional.of(timeActualStam);
			}
		}
		TimeLeavingWork timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(),
				optTimeAttendanceStart.get(),
				optTimeAttendanceEnd.get());
		if(!lstTimeLeavingWorks.isEmpty()) {
			timeDaily.getTimeLeavingWorks().remove(timeLeavingWork);
			timeDaily.getTimeLeavingWorks().add(timeLeavingWorkTmp);
		} else {
			timeDaily = new TimeLeavingOfDailyPerformance(employeeId, new WorkTimes(1), Arrays.asList(timeLeavingWorkTmp), dateData);
		}
		timeLeavingOfDaily.updateFlush(timeDaily);
				
	}

}
