package nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceIdAndPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.companyRecord.StatusOfEmployeeAdapter;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.erroralarmcheck.ErrorAlarmCheck;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.whethertocheck.WhetherToCheck;
import nts.uk.ctx.at.shared.dom.alarmList.persistenceextractresult.*;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.AlarmCheckSubConAgr;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.AnnualHolidayAlarmCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.YearlyUsageObDay;
import nts.uk.ctx.at.shared.dom.alarmList.AlarmCategory;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionAlarmPeriodDate;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.ReferenceAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.vacation.obligannleause.AnnLeaUsedDaysOutput;
import nts.uk.ctx.at.shared.dom.vacation.obligannleause.ObligedAnnLeaUseService;
import nts.uk.ctx.at.shared.dom.vacation.obligannleause.ObligedAnnualLeaveUse;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * ?????????????????????
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
		
		//??????????????????????????????????????????????????????????????????????????????
		Optional<AlarmCheckConditionByCategory> alCheckConByCategory =   alCheckConByCategoryRepo.find(companyID, AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY.value, checkConditionCode);
		if(!alCheckConByCategory.isPresent()) {
			return Collections.emptyList();
		}
		AnnualHolidayAlarmCondition annualHolidayAlarmCondition = (AnnualHolidayAlarmCondition) alCheckConByCategory.get().getExtractionCondition();
		//????????????????????????????????????.????????????????????????
		YearlyUsageObDay yearlyUsageObDay = annualHolidayAlarmCondition.getAlarmCheckConAgr().getUsageObliDay();
			
		for(EmployeeSearchDto employee : employees) {
			//???????????????????????????
			boolean check  = whetherToCheck.whetherToCheck(companyId, employee.getId(), annualHolidayAlarmCondition);
			if(!check)
				continue;
			
			//???????????????????????????????????????????????????????????????
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData =  annLeaGrantRemDataRepository.findByCheckState(employee.getId(),LeaveExpirationStatus.AVAILABLE.value);
			//sort
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingDataSort = listAnnualLeaveGrantRemainingData.stream().sorted((x,y) -> x.getGrantDate().compareTo(y.getGrantDate()))
					.collect(Collectors.toList());
			// create obligedAnnualLeaveUse
			ObligedAnnualLeaveUse obligedAnnualLeaveUse = ObligedAnnualLeaveUse.create(
					employee.getId(), 
					new AnnualLeaveUsedDayNumber(Double.valueOf(yearlyUsageObDay.v())), 
					listAnnualLeaveGrantRemainingDataSort); 
			//???????????????????????????(JAPAN)
			Optional<AnnualLeaveUsedDayNumber> ligedUseDays = obligedAnnLeaUseService.getObligedUseDays(
					companyId, 
					annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
					GeneralDate.today(),
					obligedAnnualLeaveUse); 
			if(!ligedUseDays.isPresent())
				continue;
			//??????????????????????????????????????????????????????(JAPAN)
			AnnLeaUsedDaysOutput ligedUseOutput = obligedAnnLeaUseService.getAnnualLeaveUsedDays(
					companyId, 
					employee.getId(),
					GeneralDate.today(),
					ReferenceAtr.APP_AND_SCHE,
					annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
					obligedAnnualLeaveUse); 
			if(!ligedUseOutput.getDays().isPresent())
				continue;
			if(!ligedUseOutput.getPeriod().isPresent())
				continue;
			
			//?????????????????????????????????
			boolean checkErrorAlarm = errorAlarmCheck.checkErrorAlarmCheck(ligedUseDays.get(), ligedUseOutput.getDays().get());
			if(!checkErrorAlarm)
				continue;
			ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
					employee.getWorkplaceId(),
					employee.getId(),
					TextResource.localize("KAL010_908", 
							dateToString(ligedUseOutput.getPeriod().get().start()),
							dateToString(ligedUseOutput.getPeriod().get().end())),
					TextResource.localize("KAL010_400"),
					TextResource.localize("KAL010_401"),
					TextResource.localize("KAL010_402",
							ligedUseOutput.getDays().get().v().toString(),
							ligedUseDays.get().v().toString()),	
					annualHolidayAlarmCondition.getAlarmCheckConAgr().getDisplayMessage().get().v(),
					null
					);
			listValueExtractAlarm.add(resultCheckRemain);
		}
		
		return listValueExtractAlarm;
	}
	/**
	 * ?????????????????????
	 * @param companyID
	 * @param alCheckConByCategories
	 * @param employees
	 * @param counter
	 * @param shouldStop
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> totalProcessAnnualHolidayV2(String companyID, List<AlarmCheckConditionByCategory> alCheckConByCategories ,List<EmployeeSearchDto> employees, 
		Consumer<Integer> counter, Supplier<Boolean> shouldStop){
		List<ValueExtractAlarm> listValueExtractAlarm = Collections.synchronizedList(new ArrayList<>()); 	
		
		//??????????????????????????????????????????????????????????????????????????????
		if(alCheckConByCategories.isEmpty()) {
			return Collections.emptyList();
		}
		for(AlarmCheckConditionByCategory alCheckConByCategory : alCheckConByCategories) {
			//????????????????????????
			AnnualHolidayAlarmCondition annualHolidayAlarmCondition = (AnnualHolidayAlarmCondition) alCheckConByCategory.getExtractionCondition();
			//????????????????????????????????????.????????????????????????
			YearlyUsageObDay yearlyUsageObDay = annualHolidayAlarmCondition.getAlarmCheckConAgr().getUsageObliDay();
				
			for(EmployeeSearchDto employee : employees) {
				if(shouldStop.get()) {
					return new ArrayList<>();
				}
				//???????????????????????????
				boolean check  = whetherToCheck.whetherToCheck(companyID, employee.getId(), annualHolidayAlarmCondition);
				if(!check)
					continue;
				
				//???????????????????????????????????????????????????????????????
				List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData =  annLeaGrantRemDataRepository.findByCheckState(employee.getId(),
						LeaveExpirationStatus.AVAILABLE.value);
				//sort
				List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingDataSort = listAnnualLeaveGrantRemainingData.stream()
						.sorted((x,y) -> x.getGrantDate().compareTo(y.getGrantDate()))
						.collect(Collectors.toList());
				// create obligedAnnualLeaveUse
				ObligedAnnualLeaveUse obligedAnnualLeaveUse = ObligedAnnualLeaveUse.create(
						employee.getId(), 
						new AnnualLeaveUsedDayNumber(Double.valueOf(yearlyUsageObDay.v())), 
						listAnnualLeaveGrantRemainingDataSort); 
				//???????????????????????????(JAPAN)
				Optional<AnnualLeaveUsedDayNumber> ligedUseDays = obligedAnnLeaUseService.getObligedUseDays(
						companyID, 
						annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
						GeneralDate.today(),
						obligedAnnualLeaveUse); 
				if(!ligedUseDays.isPresent())
					continue;
				//??????????????????????????????????????????????????????(JAPAN)
				AnnLeaUsedDaysOutput ligedUseOutput = obligedAnnLeaUseService.getAnnualLeaveUsedDays(
						companyID, 
						employee.getId(),
						GeneralDate.today(),
						ReferenceAtr.APP_AND_SCHE,
						annualHolidayAlarmCondition.getAlarmCheckConAgr().isDistByPeriod(), 
						obligedAnnualLeaveUse); 
				if(!ligedUseOutput.getDays().isPresent())
					continue;
				if(!ligedUseOutput.getPeriod().isPresent())
					continue;
				
				//?????????????????????????????????
				boolean checkErrorAlarm = errorAlarmCheck.checkErrorAlarmCheck(ligedUseDays.get(), ligedUseOutput.getDays().get());
				if(!checkErrorAlarm)
					continue;
				ValueExtractAlarm resultCheckRemain = new ValueExtractAlarm(
						employee.getWorkplaceId(),
						employee.getId(),
						TextResource.localize("KAL010_908", 
								dateToString(ligedUseOutput.getPeriod().get().start()),
								dateToString(ligedUseOutput.getPeriod().get().end())),
						TextResource.localize("KAL010_400"),
						TextResource.localize("KAL010_401"),
						TextResource.localize("KAL010_402",
								ligedUseOutput.getDays().get().v().toString(),
								ligedUseDays.get().v().toString()),	
						annualHolidayAlarmCondition.getAlarmCheckConAgr().getDisplayMessage().get().v(),
						ligedUseOutput.getDays().get().v().toString()
						);
				listValueExtractAlarm.add(resultCheckRemain);
			}
			counter.accept(employees.size());
		}
		
		return listValueExtractAlarm;
	}
	
	/**
	 * ?????????????????????
	 * @param companyID
	 * @param alCheckConByCategories
	 * @param employees
	 * @param counter
	 * @param shouldStop
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void checkAnnualHolidayAlarm(String companyID, AnnualHolidayAlarmCondition annualHolidayCond ,List<String> employees, 
		Consumer<Integer> counter, Supplier<Boolean> shouldStop, 
		List<WorkPlaceHistImport> getWplByListSidAndPeriod,List<StatusOfEmployeeAdapter> lstStatusEmp,
		List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckInfor,
		List<AlarmEmployeeList> alarmEmployeeList, List<AlarmExtractionCondition> alarmExtractConditions,
		String alarmCheckConditionCode){
		lstCheckInfor.add(new AlarmListCheckInfor("1", AlarmListCheckType.FixCheck));	
		//????????????????????????????????????.????????????????????????
		YearlyUsageObDay yearlyUsageObDay = annualHolidayCond.getAlarmCheckConAgr().getUsageObliDay();
		String checkCondNo = "";
		AlarmCheckSubConAgr alarmCheckSubConAgr = annualHolidayCond.getAlarmCheckSubConAgr();
		if (Objects.nonNull(alarmCheckSubConAgr)) {
			// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????True =>????????????????????????
			if (alarmCheckSubConAgr.isNarrowUntilNext()) {
				checkCondNo = "1";
			}
			//???????????????????????????????????????????????????????????????????????????????????????????????????????????????True =>????????????????????????
			if (alarmCheckSubConAgr.isNarrowLastDay()) {
				checkCondNo = "2";
			}

			String finalCheckCondNo = checkCondNo;
			List<AlarmExtractionCondition> extractionConditions = alarmExtractConditions.stream()
					.filter(x -> x.getAlarmListCheckType() == AlarmListCheckType.FixCheck && x.getAlarmCheckConditionNo().equals(finalCheckCondNo))
					.collect(Collectors.toList());
			if (extractionConditions.isEmpty()) {
				alarmExtractConditions.add(new AlarmExtractionCondition(
						String.valueOf(checkCondNo),
						new AlarmCheckConditionCode(alarmCheckConditionCode),
						AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY,
						AlarmListCheckType.FixCheck
				));
			}
		}

//		?????????????????????????????????????????????????????????
//	????????????????????????????????????????????????????????????
//	???????????????????????????????????????????????????????????????????????????????????????  //TODO: QA #116112

		for(String sid : employees) {
			if(shouldStop.get()) {
				return;
			}
			//???????????????????????????
			boolean check  = whetherToCheck.whetherToCheck(companyID, sid, annualHolidayCond);
			if(!check)
				continue;
			
			//???????????????????????????????????????????????????????????????
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData =  annLeaGrantRemDataRepository.findByCheckState(sid,
					LeaveExpirationStatus.AVAILABLE.value);
			//sort
			List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingDataSort = listAnnualLeaveGrantRemainingData.stream()
					.sorted((x,y) -> x.getGrantDate().compareTo(y.getGrantDate()))
					.collect(Collectors.toList());
			// create obligedAnnualLeaveUse
			ObligedAnnualLeaveUse obligedAnnualLeaveUse = ObligedAnnualLeaveUse.create(
					sid, 
					new AnnualLeaveUsedDayNumber(Double.valueOf(yearlyUsageObDay.v())), 
					listAnnualLeaveGrantRemainingDataSort); 
			//???????????????????????????(JAPAN)
			Optional<AnnualLeaveUsedDayNumber> ligedUseDays = obligedAnnLeaUseService.getObligedUseDays(
					companyID, 
					annualHolidayCond.getAlarmCheckConAgr().isDistByPeriod(), 
					GeneralDate.today(),
					obligedAnnualLeaveUse); 
			if(!ligedUseDays.isPresent())
				continue;
			//??????????????????????????????????????????????????????(JAPAN)
			AnnLeaUsedDaysOutput ligedUseOutput = obligedAnnLeaUseService.getAnnualLeaveUsedDays(
					companyID, 
					sid,
					GeneralDate.today(),
					ReferenceAtr.APP_AND_SCHE,
					annualHolidayCond.getAlarmCheckConAgr().isDistByPeriod(), 
					obligedAnnualLeaveUse); 
			if(!ligedUseOutput.getDays().isPresent())
				continue;
			if(!ligedUseOutput.getPeriod().isPresent())
				continue;
			
			//?????????????????????????????????
			boolean checkErrorAlarm = errorAlarmCheck.checkErrorAlarmCheck(ligedUseDays.get(), ligedUseOutput.getDays().get());
			if(!checkErrorAlarm)
				continue;
			String workplaceId = "";
			List<WorkPlaceHistImport> getWpl = getWplByListSidAndPeriod.stream().filter(x -> x.getEmployeeId().equals(sid)).collect(Collectors.toList());
			if(!getWpl.isEmpty()) {
				 WorkPlaceIdAndPeriodImport wpPeriod = getWpl.get(0).getLstWkpIdAndPeriod().get(0);
				 workplaceId = wpPeriod.getWorkplaceId();
			}
			ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(ligedUseOutput.getPeriod().get().start()),
					Optional.ofNullable(ligedUseOutput.getPeriod().get().end()));
			ExtractResultDetail detail = new ExtractResultDetail(
					pDate,
					TextResource.localize("KAL010_401"),
					TextResource.localize("KAL010_402",
							ligedUseOutput.getDays().get().v().toString(),
							ligedUseDays.get().v().toString()),
					GeneralDateTime.now(),
					Optional.ofNullable(workplaceId),
					Optional.ofNullable(annualHolidayCond.getAlarmCheckConAgr().getDisplayMessage().get().v()),
					Optional.ofNullable(ligedUseOutput.getDays().get().v().toString()));


			if (alarmEmployeeList.stream().anyMatch(i -> i.getEmployeeID().equals(sid))) {
				for (AlarmEmployeeList i : alarmEmployeeList) {
					if (i.getEmployeeID().equals(sid)) {
						List<ExtractResultDetail> details = new ArrayList<>(Arrays.asList(detail));
						List<AlarmExtractInfoResult> alarmExtractInfoResults = new ArrayList<>(i.getAlarmExtractInfoResults());
						alarmExtractInfoResults.add(
								new AlarmExtractInfoResult(
										String.valueOf(checkCondNo),
										new AlarmCheckConditionCode(alarmCheckConditionCode),
										AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY,
										AlarmListCheckType.FixCheck,
										details
								)
						);
						i.setAlarmExtractInfoResults(alarmExtractInfoResults);
						break;
					}
				}
			} else {
				List<ExtractResultDetail> details = new ArrayList<>(Arrays.asList(detail));
				List<AlarmExtractInfoResult> alarmExtractInfoResults = new ArrayList<>(Arrays.asList(
						new AlarmExtractInfoResult(
								String.valueOf(checkCondNo),
								new AlarmCheckConditionCode(alarmCheckConditionCode),
								AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY,
								AlarmListCheckType.FixCheck,
								details
						)
				));
				alarmEmployeeList.add(new AlarmEmployeeList(alarmExtractInfoResults, sid));
			}
		}
		counter.accept(employees.size());
	}
	
	
	private String dateToString(GeneralDate date) {
		return date.toString(ErAlConstant.DATE_FORMAT);
	}
}
