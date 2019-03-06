package nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.erroralarmcheck.ErrorAlarmCheck;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.whethertocheck.WhetherToCheck;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.AnnualHolidayAlarmCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.YearlyUsageObDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.ReferenceAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.vacation.obligannleause.ObligedAnnLeaUseService;
import nts.uk.ctx.at.shared.dom.vacation.obligannleause.ObligedAnnualLeaveUse;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
/**
 * 年休の集計処理
 * @author tutk
 *
 */
@Stateless
public class TotalProcessAnnualHoliday {
	@Inject
	private AlarmCheckConditionByCategoryRepository alCheckConByCategoryRepo;
	
	@Inject
	private AnnLeaGrantRemDataRepository annLeaGrantRemDataRepository;
	
	@Inject
	private WhetherToCheck whetherToCheck;
	
	@Inject
	private ObligedAnnLeaUseService obligedAnnLeaUseService;
	
	@Inject
	private ErrorAlarmCheck errorAlarmCheck;
	
	public List<ValueExtractAlarm> totalProcessAnnualHoliday(String companyID , String  checkConditionCode,List<EmployeeSearchDto> employees){
		
		
		List<ValueExtractAlarm> listValueExtractAlarm = new ArrayList<>(); 	
		
		String companyId = AppContexts.user().companyId();
		
		//ドメインモデル「年休使用義務チェック条件」を取得する
		Optional<AlarmCheckConditionByCategory> alCheckConByCategory =   alCheckConByCategoryRepo.find(companyID, AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY.value, checkConditionCode);
		if(!alCheckConByCategory.isPresent()) {
			return Collections.emptyList();
		}
		AnnualHolidayAlarmCondition annualHolidayAlarmCondition = (AnnualHolidayAlarmCondition) alCheckConByCategory.get().getExtractionCondition();
		//年休使用義務チェック条件.年休使用義務日数
		YearlyUsageObDay yearlyUsageObDay = annualHolidayAlarmCondition.getAlarmCheckConAgr().getUsageObliDay();
			
		for(EmployeeSearchDto employee : employees) {
			//チェック対象か判断
			boolean check  = whetherToCheck.whetherToCheck(companyId, employee.getId(), alCheckConByCategory.get());
			if(!check)
				continue;
			
			//ドメインモデル「年休付与残数データ」を取得
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData =  annLeaGrantRemDataRepository.findByCheckState(employee.getId(),LeaveExpirationStatus.AVAILABLE.value);
			//sort
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingDataSort = listAnnualLeaveGrantRemainingData.stream().sorted((x,y) -> x.getGrantDate().compareTo(y.getGrantDate()))
					.collect(Collectors.toList());
			// create obligedAnnualLeaveUse
			ObligedAnnualLeaveUse obligedAnnualLeaveUse = ObligedAnnualLeaveUse.create(
					employee.getId(), 
					new AnnualLeaveUsedDayNumber(Double.valueOf(yearlyUsageObDay.v())), 
					listAnnualLeaveGrantRemainingDataSort); 
			//使用義務日数の取得(JAPAN)
			Optional<AnnualLeaveUsedDayNumber> ligedUseDays = obligedAnnLeaUseService.getObligedUseDays(
					companyId, 
					annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
					obligedAnnualLeaveUse); 
			if(!ligedUseDays.isPresent())
				continue;
			//義務日数計算期間内の年休使用数を取得(JAPAN)
			Optional<AnnualLeaveUsedDayNumber> ligedAnnLeaUseService = obligedAnnLeaUseService.getAnnualLeaveUsedDays(
					companyId, 
					employee.getId(),
					GeneralDate.today(),
					ReferenceAtr.APP_AND_SCHE,
					annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
					obligedAnnualLeaveUse); 
			if(!ligedAnnLeaUseService.isPresent())
				continue;
			
			//エラーアラームチェック
			boolean checkErrorAlarm = errorAlarmCheck.checkErrorAlarmCheck(ligedUseDays.get(), ligedAnnLeaUseService.get());
			if(!checkErrorAlarm)
				continue;
			ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
					employee.getWorkplaceId(),
					employee.getId(),
					"",
					TextResource.localize("KAL010_400"),
					TextResource.localize("KAL010_401"),
					TextResource.localize("KAL010_402",
							ligedAnnLeaUseService.get().v().toString(),
							ligedUseDays.get().v().toString()),	
					annualHolidayAlarmCondition.getAlarmCheckConAgr().getDisplayMessage().get().v()
					);
			listValueExtractAlarm.add(resultCheckRemain);
		}
		
		return listValueExtractAlarm;
	}
}
