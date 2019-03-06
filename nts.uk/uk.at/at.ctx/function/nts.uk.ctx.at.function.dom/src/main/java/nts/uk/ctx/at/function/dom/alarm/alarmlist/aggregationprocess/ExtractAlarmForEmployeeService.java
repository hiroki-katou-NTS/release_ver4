package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.approvalmanagement.ApprovalProcessAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.approvalmanagement.ApprovalProcessImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.agreementprocess.AgreementProcessService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess.DailyAggregationProcessService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.monthly.MonthlyAggregateProcessService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.multiplemonth.MultipleMonthAggregateProcessService;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.w4d4alarm.W4D4AlarmService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ExtractAlarmForEmployeeService {
	
	private static final List<Integer> CHECK_CATEGORY = Arrays.asList(AlarmCategory.DAILY.value, AlarmCategory.SCHEDULE_4WEEK.value, AlarmCategory.AGREEMENT.value, AlarmCategory.MONTHLY.value, AlarmCategory.MULTIPLE_MONTH.value);

	@Inject 
	private DailyAggregationProcessService dailyAggregationProcessService;
	
	@Inject
	private W4D4AlarmService w4D4AlarmService;
		
	@Inject
	private WorkplaceAdapter workplaceAdapter;

	@Inject
	private AgreementProcessService agreementProcessService;

	@Inject
	private MonthlyAggregateProcessService monthlyAggregateProcessService;

	@Inject
	private MultipleMonthAggregateProcessService multipleMonthAggregateProcessService;

	@Inject
	private ApprovalProcessAdapter approvalProcessAdapter;

	@Inject
	private IdentityConfirmProcessAdapter identityConfirmProcessAdapter;
	
	@Inject
	private AgreementOperationSettingAdapter agreementOperationSettingAdapter;
	
	@Inject
	private AlarmCheckConditionByCategoryRepository erAlByCateRepo;
	
	@Inject
	private ManagedParallelWithContext parallelManager;
	
	public List<ValueExtractAlarm> process(String comId, List<CheckCondition> checkConList, List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees){
		
		List<ValueExtractAlarm> result = new ArrayList<>();
		List<Integer> listCategory = listPeriodByCategory.stream().map( x->x.getCategory()).collect(Collectors.toList());
		checkConList.removeIf( e->!listCategory.contains(e.getAlarmCategory().value));
		List<String> employeeIds = employees.stream().map(c -> c.getId()).collect(Collectors.toList());
		List<WorkplaceImport>  optWorkplaceImports = workplaceAdapter.getWorlkplaceHistoryByIDs(employeeIds);
		//#101960 「本人確認処理の利用設定」取得処理を追加（アルゴリズム移動） and 「承認処理の利用設定」取得処理を追加
		ApprovalProcessImport appovalProcess = null;
		IdentityConfirmProcessImport identConfrimProcess = null;
		// #101971, #101141, #101142, #101372 36協定チェック時の事前取得処理追加
		Optional<AgreementOperationSettingImport> agreementSetObj = null;
		for (CheckCondition checkCondition : checkConList) {
			if(checkCondition.isAgrrement()){
				agreementSetObj = agreementOperationSettingAdapter.find(comId);
			}
			else if (checkCondition.isDaily() || checkCondition.isMonthly()) {
				appovalProcess = approvalProcessAdapter.getApprovalProcess(comId);
				identConfrimProcess = identityConfirmProcessAdapter.getIdentityConfirmProcess(comId);
			}
		}
		// 次のチェック条件コードで集計する(loop list by category)
		for (CheckCondition checkCondition : checkConList) {
			// get Period by category
			List<PeriodByAlarmCategory> periodAlarms = listPeriodByCategory.stream().filter(c -> c.getCategory() == checkCondition.getAlarmCategory().value).collect(Collectors.toList());			
			List<DatePeriod> datePeriods = periodAlarms.stream().map(e -> 
				new DatePeriod(e.getStartDate(), e.getEndDate())).collect(Collectors.toList());
				List<WorkplaceImport>  optWorkplaceImport = optWorkplaceImports.stream().filter(e -> employeeIds.contains(e.getEmployeeId())
						 && (e.getDateRange().start().beforeOrEquals(datePeriods.get(0).end()) && 
								 e.getDateRange().end().afterOrEquals(datePeriods.get(0).end()))).collect(Collectors.toList());
					
				
				optWorkplaceImport.stream().forEach(wp -> {
					employees.stream().filter(e -> e.getId().equals(wp.getEmployeeId())).findFirst().ifPresent(e -> {
						e.setWorkplaceId(wp.getWorkplaceId());
						e.setWorkplaceCode(wp.getWorkplaceCode());
						e.setWorkplaceName(wp.getWorkplaceName());
					});
				});
				
				// カテゴリ：日次のチェック条件(daily)
				if (checkCondition.isDaily()) {
					for (String checkConditionCode : checkCondition.getCheckConditionList()) {						
						// アルゴリズム「日次の集計処理」を実行する
						result.addAll(dailyAggregationProcessService.dailyAggregationProcess(comId, checkConditionCode, 
								periodAlarms.get(0), employees, datePeriods.get(0)));
					}
				}
				// カテゴリ：4週4休のチェック条件(4 week 4 day)
				else if (checkCondition.is4W4D()) {
					// アルゴリズム「4週4休の集計処理」を実行する
//					for (String checkConditionCode : checkCondition.getCheckConditionList()) {
						List<ValueExtractAlarm> w4d4AlarmList = w4D4AlarmService.calculateTotal4W4D(employees, datePeriods.get(0), checkCondition.getCheckConditionList());
						result.addAll(w4d4AlarmList);
//					}
				}
				// カテゴリ：36協定
				else if(checkCondition.isAgrrement()) {
					List<ValueExtractAlarm> agreementAlarmList = agreementProcessService.agreementProcess(checkCondition.getCheckConditionList(), periodAlarms, employees, agreementSetObj);
					result.addAll(agreementAlarmList);
				}
				// カテゴリ：月次のチェック条件 (monthly)
				else if (checkCondition.isMonthly()) {
					
					for (String checkConditionCode : checkCondition.getCheckConditionList()) {						
						// アルゴリズム「日次の集計処理」を実行する
						List<ValueExtractAlarm> monthlyAlarmList = monthlyAggregateProcessService.monthlyAggregateProcess(comId,checkConditionCode, datePeriods.get(0), employees, appovalProcess, identConfrimProcess);
						result.addAll(monthlyAlarmList);
					}
				}
				//Hoidd
				else if (checkCondition.isMultipleMonth()) {
					
					for (String checkConditionCode : checkCondition.getCheckConditionList()) {						
						// 複数月の集計処理
						List<ValueExtractAlarm> mutilpleMonthlAlarmList = multipleMonthAggregateProcessService.multimonthAggregateProcess(comId,checkConditionCode, datePeriods.get(0), employees);
						result.addAll(mutilpleMonthlAlarmList);
					}
				}

		}
		return result;
	}
	
	private List<AlarmCheckConditionByCategory> getAlarmCheckConditionCate(List<AlarmCheckConditionByCategory> allEralCate, AlarmCategory cate, List<CheckCondition> target){
		return allEralCate.stream().filter(c -> {
			if(c.getCategory() != cate ){
				return false;
			}
			Optional<CheckCondition> cco = target.stream().filter(cc -> cc.getAlarmCategory() == cate).findFirst();
			if(cco.isPresent()){
				return cco.get().getCheckConditionList().contains(c.getCode().v());
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	public List<ValueExtractAlarm> processV2(String comId, List<CheckCondition> checkConList, List<PeriodByAlarmCategory> listPeriodByCategory, 
			List<EmployeeSearchDto> employees, List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<ValueExtractAlarm> result = (new ArrayList<>());

		List<String> employeeIds = employees.stream().map(c -> c.getId()).collect(Collectors.toList());
		List<WorkplaceImport> optWorkplaceImports = workplaceAdapter.getWorlkplaceHistoryByIDs(employeeIds);
		
//		List<Runnable> checks = new ArrayList<>();
//		checks.add(() -> {
			result.addAll(runDailyCheckErAl(comId, checkConList, listPeriodByCategory, employees,
					employeeIds, optWorkplaceImports, eralCate, counter, shouldStop));
//		});
//		checks.add(() -> {
			result.addAll(runW4d4CheckErAl(comId, checkConList, listPeriodByCategory, employees, employeeIds,
					optWorkplaceImports, eralCate, counter, shouldStop));
//		});
//		checks.add(() -> {
			result.addAll(runAgreementCheckErAl(comId, checkConList, listPeriodByCategory, employees, employeeIds,
					optWorkplaceImports, eralCate, counter, shouldStop));
//		});
//		checks.add(() -> {
			result.addAll(runMonthlyCheckErAl(comId, checkConList, listPeriodByCategory, employees,
					employeeIds, optWorkplaceImports, eralCate, counter, shouldStop));
//		});
//		checks.add(() -> {
			result.addAll(runMultiMonthCheckErAl(comId, checkConList, listPeriodByCategory, employees,
					employeeIds, optWorkplaceImports, eralCate, counter, shouldStop));
//		});

//		parallelManager.forEach(checks, check -> check.run());

		return result;
	}

	private List<ValueExtractAlarm> runMultiMonthCheckErAl(String comId, List<CheckCondition> checkConList,
			List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees,
			List<String> employeeIds, List<WorkplaceImport> optWorkplaceImports,
			List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<AlarmCheckConditionByCategory> multiMonthErAl = getAlarmCheckConditionCate(eralCate, AlarmCategory.MULTIPLE_MONTH, checkConList);
		if(!multiMonthErAl.isEmpty()){
			PeriodByAlarmCategory multiMonthCate = listPeriodByCategory.stream().filter(c -> c.getCategory() == AlarmCategory.MULTIPLE_MONTH.value).findFirst().get();
			
			fillWorkPlaceForCategory(employees, employeeIds, optWorkplaceImports, multiMonthCate);
			
			/** need internal response */
			return (multipleMonthAggregateProcessService.multimonthAggregateProcess(comId, multiMonthErAl, new DatePeriod(multiMonthCate.getStartDate(), 
					multiMonthCate.getEndDate()), employees, counter, shouldStop));
		}
		
		return new ArrayList<>();
	}

	private List<ValueExtractAlarm> runMonthlyCheckErAl(String comId, List<CheckCondition> checkConList,
			List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees,
			List<String> employeeIds, List<WorkplaceImport> optWorkplaceImports,
			List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<AlarmCheckConditionByCategory> monthlyErAl = getAlarmCheckConditionCate(eralCate, AlarmCategory.MONTHLY, checkConList);
		if(!monthlyErAl.isEmpty()){
			PeriodByAlarmCategory monthlyCate = listPeriodByCategory.stream().filter(c -> c.getCategory() == AlarmCategory.MONTHLY.value).findFirst().get();
			
			fillWorkPlaceForCategory(employees, employeeIds, optWorkplaceImports, monthlyCate);
			
			/** need internal response */
			// #101960 「本人確認処理の利用設定」取得処理を追加（アルゴリズム移動） and 「承認処理の利用設定」取得処理を追加
			return (monthlyAggregateProcessService.monthlyAggregateProcess(comId, monthlyErAl,
					new DatePeriod(monthlyCate.getStartDate(), monthlyCate.getEndDate()), employees, counter, shouldStop));
		}
		
		return new ArrayList<>();
	}

	private List<ValueExtractAlarm> runAgreementCheckErAl(String comId, List<CheckCondition> checkConList,
			List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees,
			List<String> employeeIds, List<WorkplaceImport> optWorkplaceImports,
			List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<AlarmCheckConditionByCategory> agreementErAl = getAlarmCheckConditionCate(eralCate, AlarmCategory.AGREEMENT, checkConList);
		if(!agreementErAl.isEmpty()){
			PeriodByAlarmCategory agreementCate = listPeriodByCategory.stream().filter(c -> c.getCategory() == AlarmCategory.AGREEMENT.value).findFirst().get();
			
			fillWorkPlaceForCategory(employees, employeeIds, optWorkplaceImports, agreementCate);
			
			/** need internal response */
			// #101971, #101141, #101142, #101372 36協定チェック時の事前取得処理追加
			return agreementProcessService.agreementProcess(comId, agreementErAl, listPeriodByCategory, employees, agreementOperationSettingAdapter.find(comId), counter, shouldStop);
		}
		
		return new ArrayList<>();
	}

	private List<ValueExtractAlarm> runW4d4CheckErAl(String comId, List<CheckCondition> checkConList,
			List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees,
			List<String> employeeIds, List<WorkplaceImport> optWorkplaceImports,
			List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<AlarmCheckConditionByCategory> w4d4ErAl = getAlarmCheckConditionCate(eralCate, AlarmCategory.SCHEDULE_4WEEK, checkConList);
		if(!w4d4ErAl.isEmpty()){
			PeriodByAlarmCategory w4d4Cate = listPeriodByCategory.stream().filter(c -> c.getCategory() == AlarmCategory.SCHEDULE_4WEEK.value).findFirst().get();
			
			fillWorkPlaceForCategory(employees, employeeIds, optWorkplaceImports, w4d4Cate);
			
			/** need internal response */
			return (w4D4AlarmService.calculateTotal4W4D(comId, employees, new DatePeriod(w4d4Cate.getStartDate(), w4d4Cate.getEndDate()), w4d4ErAl, counter, shouldStop));
		}
		return new ArrayList<>();
	}

	private List<ValueExtractAlarm> runDailyCheckErAl(String comId, List<CheckCondition> checkConList,
			List<PeriodByAlarmCategory> listPeriodByCategory, List<EmployeeSearchDto> employees,
			List<String> employeeIds, List<WorkplaceImport> optWorkplaceImports,
			List<AlarmCheckConditionByCategory> eralCate, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		List<AlarmCheckConditionByCategory> dailyErAl = getAlarmCheckConditionCate(eralCate, AlarmCategory.DAILY, checkConList);
		if(!dailyErAl.isEmpty()){
			PeriodByAlarmCategory dailyCate = listPeriodByCategory.stream().filter(c -> c.getCategory() == AlarmCategory.DAILY.value).findFirst().get();
			
			fillWorkPlaceForCategory(employees, employeeIds, optWorkplaceImports, dailyCate);
			/** need internal response */
			return (dailyAggregationProcessService.dailyAggregationProcess(comId, dailyCate, employees, dailyErAl, counter, shouldStop));
		}
		
		return new ArrayList<>();
	}

	private void fillWorkPlaceForCategory(List<EmployeeSearchDto> employees, List<String> employeeIds,
			List<WorkplaceImport> optWorkplaceImports, PeriodByAlarmCategory dailyCate) {
		optWorkplaceImports.stream()
							.filter(e -> employeeIds.contains(e.getEmployeeId())
									&& (e.getDateRange().start().beforeOrEquals(dailyCate.getEndDate())
											&& e.getDateRange().end().afterOrEquals(dailyCate.getEndDate())))
							.forEach(wp -> {
								employees.stream().filter(e -> e.getId().equals(wp.getEmployeeId())).findFirst().ifPresent(e -> {
									e.setWorkplaceId(wp.getWorkplaceId());
									e.setWorkplaceCode(wp.getWorkplaceCode());
									e.setWorkplaceName(wp.getWorkplaceName());
								});
							});
	}
}
