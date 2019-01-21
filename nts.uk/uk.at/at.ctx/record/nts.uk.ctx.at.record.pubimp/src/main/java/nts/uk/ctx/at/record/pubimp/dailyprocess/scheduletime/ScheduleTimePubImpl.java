package nts.uk.ctx.at.record.pubimp.dailyprocess.scheduletime;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CommonCompanySettingForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerCompanySet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ProvisionalCalculationService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.requestlist.PrevisionalForImp;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.shorttimework.primitivevalue.ShortWorkTimFrameNo;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePub;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubImport;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ScheduleTimePubImpl implements ScheduleTimePub{

	@Inject
	private ProvisionalCalculationService provisionalCalculationService;
	
	@Inject
	private CommonCompanySettingForCalc commonCompanySetting;

	@Override
	public Object getCompanySettingForCalclationScheduleTimeForMultiPeople() {
		ManagePerCompanySet companySet = this.commonCompanySetting.getCompanySetting(new CalculateOption(true, true));
		MasterShareContainer<String> shareContainer = MasterShareBus.open();
		companySet.setShareContainer(shareContainer);
		return companySet;
	}

	@Override
	public void clearCompanySettingShareConainter(Object companySetObj) {
		ManagePerCompanySet companySet = (ManagePerCompanySet)companySetObj;
		companySet.getShareContainer().clearAll();
		companySet.setShareContainer(null);
	}

	
	@Override
	/**
	 * RequestList(1人用の処理)
	 */
	public ScheduleTimePubExport calculationScheduleTime(Object companySetting, ScheduleTimePubImport impTime) {
		if(impTime != null) {
			val calcValue = calclationScheduleTimeForMultiPeople(companySetting, Arrays.asList(impTime));
			if(!calcValue.isEmpty()) {
				return calcValue.stream().findFirst().get();
			}
		}
		return ScheduleTimePubExport.empty();
	}
	
	/**
	 * RequestList No 91(複数人対応版)
	 */
	@Override
	public List<ScheduleTimePubExport> calclationScheduleTimeForMultiPeople(Object companySetting, List<ScheduleTimePubImport> impList) {
		return calclationScheduleTimePassCompanyCommonSetting(impList, Optional.ofNullable((ManagePerCompanySet)companySetting));
	}
	
	@Override
	public List<ScheduleTimePubExport> calclationScheduleTimePassCompanyCommonSetting(List<ScheduleTimePubImport> impList,Optional<ManagePerCompanySet> companySetting){
		List<PrevisionalForImp> paramList = new ArrayList<>();
		for(ScheduleTimePubImport imp : impList){
			//時間帯リスト
			Map<Integer, TimeZone> timeSheets = getTimeZone(imp.getStartClock(),imp.getEndClock());
			List<BreakTimeSheet> breakTimeSheets = getBreakTimeSheets(imp.getBreakStartTime(),imp.getBreakEndTime());
			List<OutingTimeSheet> outingTimeSheets = new ArrayList<>();
			List<ShortWorkingTimeSheet> shortWorkingTimeSheets = getShortTimeSheets(imp.getChildCareStartTime(),imp.getChildCareEndTime());
			
			paramList.add(new PrevisionalForImp(imp.getEmployeeId(), imp.getTargetDate(), timeSheets, imp.getWorkTypeCode(), imp.getWorkTimeCode(), breakTimeSheets, outingTimeSheets, shortWorkingTimeSheets));
		}
		//実績計算
		List<IntegrationOfDaily> integrationOfDaily = provisionalCalculationService.calculationPassCompanyCommonSetting(paramList,companySetting);	
		
		if(!integrationOfDaily.isEmpty()) {
			return getreturnValye(integrationOfDaily);
		}
		else {
			return new ArrayList<>();				
		}
	}

	
	/**
	 * 実績からOutputクラスへ値を移行する
	 * @param integrationOfDaily 実績
	 * @return Outputクラス
	 */
	private List<ScheduleTimePubExport> getreturnValye(List<IntegrationOfDaily> integrationOfDailyList) {
		
		List<ScheduleTimePubExport> returnList = new ArrayList<>();
		
		for(IntegrationOfDaily integrationOfDaily:integrationOfDailyList) {
		
			String empId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
			GeneralDate ymd = integrationOfDaily.getAffiliationInfor().getYmd();
			
			//総労働時間
			AttendanceTime totalWorkTime = new AttendanceTime(0);
			//計画所定時間
			AttendanceTime preTime = new AttendanceTime(0);
			//実働時間
			AttendanceTime actualWorkTime = new AttendanceTime(0);
			//平日時間
			AttendanceTime weekDayTime = new AttendanceTime(0);
			//休憩時間
			AttendanceTime breakTime = new AttendanceTime(0);
			//育児時間
			AttendanceTime childTime = new AttendanceTime(0);
			//介護時間
			AttendanceTime careTime = new AttendanceTime(0);
			//フレックス時間
			AttendanceTimeOfExistMinus flexTime = new AttendanceTimeOfExistMinus(0);
			
			//人件費時間
			List<AttendanceTime> personalExpenceTime = new ArrayList<>();
		
			if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()
					&&integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() != null) {
				//割増
				personalExpenceTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getPremiumTimeOfDailyPerformance()
													.getPremiumTimes().stream().map(tc -> tc.getPremitumTime()).collect(Collectors.toList());
				//計画所定
				preTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getWorkScheduleTimeOfDaily().getSchedulePrescribedLaborTime();
			
				if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime() != null) {
					//総労働時間
					totalWorkTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalTime();
					//実働時間
					actualWorkTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getActualTime();
				
					//休憩時間
					if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily() != null) {
						breakTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily()
											   .getToRecordTotalTime().getTotalTime().getTime();
					}
					//育児介護時間
					if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily() != null) {
						if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getChildCareAttribute().isCare()) {
							childTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getTotalTime().getTotalTime().getTime();
						}
						else {
							careTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getTotalTime().getTotalTime().getTime();
						}
					}
					if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() != null
					&& integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
						flexTime = integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getCalcTime();
					}
				
					//平日時間
					weekDayTime =  new AttendanceTime(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWorkTime().valueAsMinutes()
						       					  + integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getHolidayOfDaily().calcTotalHolTime().valueAsMinutes()
						       					  - integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getVacationAddTime().valueAsMinutes());
				}
			}
			returnList.add(ScheduleTimePubExport.of(empId, ymd, totalWorkTime, preTime, actualWorkTime, weekDayTime, breakTime, childTime, careTime, flexTime, personalExpenceTime));
		}
		return returnList;
	}

	/**
	 * 短時間勤務時間端を作成する
	 * @param childCareStartTime 短時間勤務開始時刻
	 * @param childCareEndTime　短時間勤務狩猟時刻
	 * @return　短時間勤務時間帯
	 */
	private List<ShortWorkingTimeSheet> getShortTimeSheets(List<Integer> childCareStartTime,List<Integer> childCareEndTime) {
		List<ShortWorkingTimeSheet> returnList = new ArrayList<>();
		for(int shortTimeStamp = 1 ; shortTimeStamp< childCareStartTime.size();shortTimeStamp++) {
			if(shortTimeStamp <= childCareStartTime.size()
			 &&shortTimeStamp <= childCareEndTime.size()) {
				returnList.add(new ShortWorkingTimeSheet(new ShortWorkTimFrameNo(shortTimeStamp), 
													 ChildCareAttribute.CHILD_CARE,
													 new TimeWithDayAttr(childCareStartTime.get(shortTimeStamp - 1).intValue()),
													 new TimeWithDayAttr(childCareEndTime.get(shortTimeStamp - 1).intValue()),
													 new AttendanceTime(0),
													 new AttendanceTime(0)));
			}
		}
		return returnList;
	}

	/**
	 * 休憩時間帯作成
	 * @param breakStartTime 休憩開始時刻
	 * @param breakEndTime　休憩終了時刻
	 * @return　休憩時間帯
	 */
	private List<BreakTimeSheet> getBreakTimeSheets(List<Integer> breakStartTime, List<Integer> breakEndTime) {
		List<BreakTimeSheet> returnList = new ArrayList<>();
		for(int breatStampNo = 1;breatStampNo <= breakStartTime.size() ; breatStampNo++) {
			if( breatStampNo <= breakStartTime.size() 
			  &&breatStampNo <= breakEndTime.size()) {
				returnList.add(new BreakTimeSheet(new BreakFrameNo(breatStampNo),
											  new TimeWithDayAttr(breakStartTime.get(breatStampNo - 1).intValue()),
											  new TimeWithDayAttr(breakEndTime.get(breatStampNo - 1).intValue()),
											  new AttendanceTime(0)));
			}
		}
		return returnList;
	}

	/**
	 * 勤務時間帯作成
	 * @param startClock 出勤時刻
	 * @param endClock 退勤時刻
	 * @return　勤務時間帯
	 */
	private Map<Integer, TimeZone> getTimeZone(List<Integer> startClock, List<Integer> endClock) {
		Map<Integer, TimeZone> timeList = new HashMap<>();
		
		for(int workNo = 1 ; workNo <= startClock.size() ; workNo++) {
			if(startClock.size() >= workNo
			&& endClock.size() >= workNo) {
				timeList.put(workNo,new TimeZone(new TimeWithDayAttr(startClock.get(workNo - 1).intValue()),
							   		new TimeWithDayAttr(endClock.get(workNo - 1).intValue())));
			}
		}
		return timeList;
	}


}