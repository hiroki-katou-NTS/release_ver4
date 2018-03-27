package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalHolidaySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfLeaveEarlySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfOverTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.LeaveAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SpecificSalaryCalAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainService;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別実績の仮計算(申請・スケからの窓口)
 * @author keisuke_hoshina
 *
 */
@Stateless
public class PrevisionalCalculationServiceImpl implements ProvisionalCalculationService{

	@Inject
	private CalculateDailyRecordService calculateDailyRecordService;
	
	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;
	
	@Inject
	private AttendanceTimeRepository attendanceTimeRepository;
	
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;
	
	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;
	
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;
	
	@Inject
	private ReflectWorkInforDomainService reflectWorkInforDomainServiceImpl;
	
	@Override
	public Optional<IntegrationOfDaily> calculation(String employeeId, GeneralDate targetDate, Map<Integer, TimeZone> timeSheets,
			WorkTypeCode workTypeCode, WorkTimeCode workTimeCode, List<BreakTimeSheet> breakTimeSheets,
			List<OutingTimeSheet> outingTimeSheets, List<ShortWorkingTimeSheet> shortWorkingTimeSheets) {
		if(workTypeCode == null)
			return Optional.empty();
		//疑似的な日別実績を作成
		val provisionalRecord = createProvisionalDailyRecord(employeeId,targetDate,workTypeCode,workTimeCode,timeSheets);
		if(!provisionalRecord.isPresent())
			return Optional.empty();
		//控除置き換え
		val provisionalDailyRecord = replaceDeductionTimeSheet(provisionalRecord.get(),breakTimeSheets,outingTimeSheets,shortWorkingTimeSheets,employeeId,targetDate);
		//ドメインモデル「日別実績の勤怠時間」を返す
		return Optional.of(calculateDailyRecordService.calculate(provisionalDailyRecord));

	}
	/**
	 *疑似的な日別実績を作成
	 */
	private Optional<IntegrationOfDaily> createProvisionalDailyRecord(String employeeId, GeneralDate ymd,WorkTypeCode workTypeCode, WorkTimeCode workTimeCode,Map<Integer, TimeZone> timeSheets) {
		//日別実績の勤務情報
		Optional<WorkInfoOfDailyPerformance> preworkInformation = workInformationRepository.find(employeeId, ymd);
		String setWorkTimeCode = null;
		if(workTimeCode != null )
			setWorkTimeCode = workTimeCode.v();
		WorkInfoOfDailyPerformance workInformation = new WorkInfoOfDailyPerformance(employeeId, 
																				   new WorkInformation(setWorkTimeCode,workTypeCode.toString()), 
																				   null, 
																				   CalculationState.No_Calculated, 
																				   NotUseAttribute.Not_use, 
																				   NotUseAttribute.Not_use, 
																				   ymd, 
																				   Collections.emptyList()); 
		//勤怠時間取得
		val attendanceTime = attendanceTimeRepository.find(employeeId, ymd);
		//日別実績の休憩時間帯
		val breakTimeSheet = breakTimeOfDailyPerformanceRepository.findByKey(employeeId, ymd);
		//日別実績の外出時間帯
		val goOutTimeSheet = outingTimeOfDailyPerformanceRepository.findByEmployeeIdAndDate(employeeId, ymd);
		//日別実績の短時間勤務時間帯
		Optional<ShortTimeOfDailyPerformance> ShortTimeOfDailyPerformance = Optional.empty();
		//日別実績の臨時出退勤
		// ;
		//日別実績の出退勤
		List<TimeLeavingWork> timeLeavingWorks = new ArrayList<>();
		for(Map.Entry<Integer, TimeZone> key : timeSheets.entrySet()) {
			WorkStamp attendance = new WorkStamp(key.getValue().getStart(),key.getValue().getStart(), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			WorkStamp leaving = new WorkStamp(key.getValue().getEnd(),key.getValue().getEnd(), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			TimeActualStamp attendanceStamp = new TimeActualStamp(attendance,attendance,key.getKey());
			TimeActualStamp leavingStamp = new TimeActualStamp(leaving,leaving,key.getKey());
			TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new WorkNo(key.getKey()),Optional.of(attendanceStamp),Optional.of(leavingStamp));
			
			timeLeavingWorks.add(timeLeavingWork);
		}
		TimeLeavingOfDailyPerformance timeAttendance = new TimeLeavingOfDailyPerformance(employeeId, new WorkTimes(timeSheets.size()),timeLeavingWorks, ymd);
		
		//日別実績の計算区分作成
		val calAttrOfDailyPerformance = new CalAttrOfDailyPerformance(employeeId,ymd,
				new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
				new AutoCalRaisingSalarySetting(SalaryCalAttr.USE, SpecificSalaryCalAttr.USE),
				new AutoCalHolidaySetting(new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
										  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT)),
				new AutoCalOfOverTime(new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
									  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
									  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
									  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
									  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT),
									  new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,TimeLimitUpperLimitSetting.NOUPPERLIMIT)),
				new AutoCalOfLeaveEarlySetting(LeaveAttr.USE,LeaveAttr.USE),
				new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.USE)
				);
		//日別実績の所属情報作成
		//日別作成側にある日別実績の所属情報を作成している所を呼び出す
		/*-----"01"について  --------*/
		//↓を使用して帰ってくるクラスにエラーメッセージが格納される場合がある。
		//エラーメッセージは日別計算で使用しないため、empCalAndSumExecLogIDに任意の物を入れている
		val employeeState = reflectWorkInforDomainServiceImpl.createAffiliationInforOfDailyPerfor(AppContexts.user().companyId(), employeeId, ymd, "01");
		if(!employeeState.getAffiliationInforOfDailyPerfor().isPresent())
			return Optional.empty();
		//return new IntegrationOfDaily(workInformation, timeAttendance, attendanceTime.get());
		return Optional.of(new IntegrationOfDaily(workInformation,
									  calAttrOfDailyPerformance,
									  employeeState.getAffiliationInforOfDailyPerfor().get(),
									  Optional.empty(),
									  Collections.emptyList(),
									  goOutTimeSheet,
									  breakTimeSheet,
									  attendanceTime,
									  Optional.empty(),
									  Optional.of(timeAttendance),
									  Optional.empty(),
									  Optional.empty(),
									  Optional.empty(),
									  Optional.empty(),
									  Collections.emptyList(),
									  Optional.empty()));
	}	

	
	private IntegrationOfDaily replaceDeductionTimeSheet(IntegrationOfDaily provisionalRecord, List<BreakTimeSheet> breakTimeSheets,
			List<OutingTimeSheet> outingTimeSheets, List<ShortWorkingTimeSheet> shortWorkingTimeSheets,
			String employeeId,GeneralDate ymd) {
		
		provisionalRecord.setOutingTime(Optional.of(new OutingTimeOfDailyPerformance(employeeId,ymd,outingTimeSheets)));
		List<BreakTimeOfDailyPerformance> addElement = new ArrayList<>();
		addElement.add(new BreakTimeOfDailyPerformance(employeeId,BreakType.REFER_WORK_TIME,breakTimeSheets,ymd));
		addElement.add(new BreakTimeOfDailyPerformance(employeeId,BreakType.REFER_WORK_TIME,breakTimeSheets,ymd));
		provisionalRecord.setBreakTime(addElement);
		provisionalRecord.setShortTime(Optional.of(new ShortTimeOfDailyPerformance(employeeId,shortWorkingTimeSheets,ymd)));
		
		return provisionalRecord;
	}
	
}

