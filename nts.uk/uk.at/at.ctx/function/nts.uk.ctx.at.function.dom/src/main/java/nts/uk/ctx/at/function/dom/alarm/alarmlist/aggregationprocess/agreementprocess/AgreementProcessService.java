package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.agreementprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.SneakyThrows;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckRecordAgreementAdapter;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedAgreementResult;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedOvertimeImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AgreeConditionError;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AgreeNameError;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AlarmChkCondAgree36;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.IAgreeNameErrorRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.Period;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.UseClassification;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AgreementProcessService {
	
	@Inject
	private AlarmCheckConditionByCategoryRepository alarmCheckRepo;
	
	@Inject
	private CheckRecordAgreementAdapter checkAgreementAdapter;
	
	@Inject
	private IAgreeNameErrorRepository agreeNameRepo;
	
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	
	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private ManagedParallelWithContext parallelManager;
	
	@Resource
	private SessionContext scContext;
	
	public List<ValueExtractAlarm> agreementProcess(List<String> checkConditionCodes, List<PeriodByAlarmCategory> periodAlarms, List<EmployeeSearchDto> employees, Optional<AgreementOperationSettingImport> agreementSetObj){
		
		List<ValueExtractAlarm> result = new ArrayList<ValueExtractAlarm>();
		List<String> employeeIds = employees.stream().map( e ->e.getId()).collect(Collectors.toList());
		String comId = AppContexts.user().companyId();
		List<Closure> closureList = new ArrayList<>();
		Map<String,Integer> mapEmpIdClosureID = new HashMap<>();
		Map<String,String> mapEmpCodeEmpId = new HashMap<>();
		/**社員ID（List）と指定期間から社員の雇用履歴を取得 */
		List<SharedSidPeriodDateEmploymentImport> employmentHistList = this.shareEmploymentAdapter
				.getEmpHistBySidAndPeriod(employeeIds, new DatePeriod(GeneralDate.today(), GeneralDate.today()));
		if (!CollectionUtil.isEmpty(employmentHistList)) {
			List<String> empCds = new ArrayList<>();
			for (SharedSidPeriodDateEmploymentImport objecCheck : employmentHistList) {
				for (AffPeriodEmpCodeImport affPeriodEmpCodeImport : objecCheck.getAffPeriodEmpCodeExports()) {
					if (!mapEmpCodeEmpId.containsKey(affPeriodEmpCodeImport.getEmploymentCode())) {
						empCds.add(affPeriodEmpCodeImport.getEmploymentCode());
						mapEmpCodeEmpId.put(affPeriodEmpCodeImport.getEmploymentCode(), objecCheck.getEmployeeId());
					}
				}
			}
			
			/** ドメインモデル「雇用に紐づく就業締め」を取得する */
			List<ClosureEmployment> closureEmploymentList = closureEmploymentRepo.findListEmployment(comId, empCds);

			if (!CollectionUtil.isEmpty(closureEmploymentList)) {
				List<Integer> closureIds = new ArrayList<>();
				for (ClosureEmployment closureEmployment : closureEmploymentList) {
					String empCode = closureEmployment.getEmploymentCD();
					if(mapEmpCodeEmpId.containsKey(empCode)){
						mapEmpIdClosureID.put(mapEmpCodeEmpId.get(empCode), closureEmployment.getClosureId());
						closureIds.add(closureEmployment.getClosureId());
					}
				}
				/** ドメインモデル「締め」を取得する*/
				closureList = closureRepository.findByListId(comId, closureIds);
			}

		}
		
		
		
		// get List<カテゴリ別アラームチェック条件>
		List<AlarmCheckConditionByCategory> listAlarmCheck= alarmCheckRepo.findByCategoryAndCode(comId, AlarmCategory.AGREEMENT.value	, checkConditionCodes);
		
		Map<String, EmployeeSearchDto> mapEmployee = employees.stream().collect(Collectors.toMap(EmployeeSearchDto::getId, x ->x));
		
		for(AlarmCheckConditionByCategory alarmCheck : listAlarmCheck) {
			// 36協定のアラームチェック条件
			AlarmChkCondAgree36 alarmChkCon36 = alarmCheck.getAlarmChkCondAgree36();
			
			// List<36協定エラーアラームのチェック条件>
			List<AgreeConditionError> listCheck  = alarmChkCon36.getListCondError().stream().filter( e ->e.getUseAtr()==UseClassification.Use).collect(Collectors.toList());
				
			//抽出条件に対応する期間を取得する 
			for (AgreeConditionError agreeConditionError : listCheck) {
				//ドメインモデル「36協定エラーアラームチェック名称」を取得する
				Optional<AgreeNameError> optAgreeName = agreeNameRepo.findById(agreeConditionError.getPeriod().value,
						agreeConditionError.getErrorAlarm().value);
				Period periodCheck = getPeriod(agreeConditionError);
				for (PeriodByAlarmCategory periodAlarm : periodAlarms) {
					if(periodAlarm.getPeriod36Agreement() == periodCheck.value){
						DatePeriod period = new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate());
						// アルゴリズム「36協定実績をチェックする」を実行する
						List<CheckedAgreementResult> checkAgreementsResult = checkAgreementAdapter.checkArgreementResult(employeeIds,
								period, agreeConditionError, agreementSetObj,closureList,mapEmpIdClosureID);
						if(!CollectionUtil.isEmpty(checkAgreementsResult)){
							result.addAll(generationValueExtractAlarm(mapEmployee,checkAgreementsResult,agreeConditionError,optAgreeName));	
						}
					}
				}
			}
			
			// アルゴリズム「超過回数チェック」を実行する
			for (PeriodByAlarmCategory periodAlarm : periodAlarms) {
				if(Period.Yearly.value == periodAlarm.getPeriod36Agreement()){
					List<DatePeriod> periodsYear = new ArrayList<>();
					periodsYear.add(new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));
					List<CheckedOvertimeImport> checkOvertimes = checkAgreementAdapter.checkNumberOvertime(employeeIds, periodsYear,
							alarmChkCon36.getListCondOt());
					for (CheckedOvertimeImport check : checkOvertimes) {

						String hour = check.getOt36().hour() + "";
						if (hour.length() < 2)
							hour = "0" + hour;
						String minute = check.getOt36().minute() + "";
						if (minute.length() < 2)
							minute = "0" + minute;
						String ot36 = hour + ":" + minute;

						String datePeriod = check.getDatePeriod().start().toString() + "~"
								+ check.getDatePeriod().end().toString();

						result.add(new ValueExtractAlarm(mapEmployee.get(check.getEmployeeId()).getWorkplaceId(),
								check.getEmployeeId(), datePeriod, TextResource.localize("KAL010_208"),
								TextResource.localize("KAL010_201"), TextResource.localize("KAL010_202",
										check.getNo() + "", ot36, check.getExcessNum().v() + ""),
								check.getMessageDisp().v()));
					}
				}
			}
		}
		return result;
	}

	@Inject
	private AgreementCheckService checkService;
	
	@SneakyThrows
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> agreementProcess(String comId, List<AlarmCheckConditionByCategory> agreementErAl, List<PeriodByAlarmCategory> periodAlarms, 
			List<EmployeeSearchDto> employees, Optional<AgreementOperationSettingImport> agreementSetObj, Consumer<Integer> counter, Supplier<Boolean> shouldStop){

//		Logger.getLogger(getClass()).info("Transaction Status 1: " + tsr.getTransactionStatus());
		List<ValueExtractAlarm> result = Collections.synchronizedList(new ArrayList<>());
		List<String> empIds = employees.stream().map( e ->e.getId()).collect(Collectors.toList());
		Map<String,Integer> empIdToClosureId = new HashMap<>();
		Map<String,String> employmentCodeToEmpIds = new HashMap<>();
		List<Closure> closureList = getClosure(comId, empIds, empIdToClosureId, employmentCodeToEmpIds);
		
		Map<String, EmployeeSearchDto> mapEmployee = employees.stream().collect(Collectors.toMap(EmployeeSearchDto::getId, x ->x));
//		Logger.getLogger(getClass()).info("Transaction Status 2: " + tsr.getTransactionStatus());
		/** TODO: parallel from here */
		parallelManager.forEach(CollectionUtil.partitionBySize(empIds, 50), employeeIds -> {
//		CollectionUtil.split(empIds, 25, employeeIds -> {
			checkService.check(agreementErAl, periodAlarms, agreementSetObj, counter, shouldStop, result, empIds, empIdToClosureId,
					closureList, mapEmployee, employeeIds);
		});
//		Logger.getLogger(getClass()).info("Transaction Status 10: " + tsr.getTransactionStatus());
		return result;
	}

	

	private Period getPeriod(AgreeConditionError agreeConditionError) {
		if (agreeConditionError.getPeriod() == Period.One_Week 
				|| agreeConditionError.getPeriod() == Period.Two_Week 
				|| agreeConditionError.getPeriod() == Period.Four_Week) {
			return Period.One_Week;
		}
		return agreeConditionError.getPeriod();
	}
	
	private List<Closure> getClosure(String comId, List<String> empIds, Map<String,Integer> empIdToClosureId, Map<String,String> employmentCodeToEmpIds){

		/**社員ID（List）と指定期間から社員の雇用履歴を取得 */
		List<SharedSidPeriodDateEmploymentImport> employmentHistList = this.shareEmploymentAdapter.getEmpHistBySidAndPeriod(empIds, new DatePeriod(GeneralDate.today(), GeneralDate.today()));
		if (!CollectionUtil.isEmpty(employmentHistList)) {
			for (SharedSidPeriodDateEmploymentImport objecCheck : employmentHistList) {
				for (AffPeriodEmpCodeImport affPeriodEmpCodeImport : objecCheck.getAffPeriodEmpCodeExports()) {
					if (!employmentCodeToEmpIds.containsKey(affPeriodEmpCodeImport.getEmploymentCode())) {
						employmentCodeToEmpIds.put(affPeriodEmpCodeImport.getEmploymentCode(), objecCheck.getEmployeeId());
					}
				}
					
			}
				
			
			/** ドメインモデル「雇用に紐づく就業締め」を取得する */
			List<ClosureEmployment> closureEmploymentList = closureEmploymentRepo.findListEmployment(comId, new ArrayList<>(employmentCodeToEmpIds.keySet()));

			if (!CollectionUtil.isEmpty(closureEmploymentList)) {
				empIds.stream().forEach(empId -> {
					closureEmploymentList.stream().filter(c -> employmentCodeToEmpIds.containsKey(c.getEmploymentCD())).findFirst().ifPresent(clo -> {
						empIdToClosureId.put(empId, clo.getClosureId());
					});
				});
				
				/** ドメインモデル「締め」を取得する*/
				return closureRepository.findByListId(comId, empIdToClosureId.values().stream().distinct().collect(Collectors.toList()));
			}

		}
		
		return new ArrayList<>();
	}
	
	private List<ValueExtractAlarm> generationValueExtractAlarm(Map<String, EmployeeSearchDto> mapEmployee, List<CheckedAgreementResult> checkAgreementsResult,AgreeConditionError agreeConditionError,Optional<AgreeNameError> optAgreeName){
		List<ValueExtractAlarm> lstReturn = new ArrayList<>();
		for (CheckedAgreementResult checkedAgreementResult : checkAgreementsResult) {
			if(checkedAgreementResult.isCheckResult()){
				// workplaceID
				String workPlaceId = mapEmployee.get(checkedAgreementResult.getEmpId()).getWorkplaceId();
				
				//年月日
				String alarmValueDate = yearmonthToString(checkedAgreementResult.getAgreementTimeByPeriod().getStartMonth()) +" ～ " + yearmonthToString(checkedAgreementResult.getAgreementTimeByPeriod().getEndMonth());
				//alarm name
				String alarmItem = optAgreeName.isPresent() ? optAgreeName.get().getName().v() : "" ;
				//カテゴリ
				String alarmContent = TextResource.localize("KAL010_203",alarmItem,formatHourData(checkedAgreementResult.getUpperLimit()),formatHourData(checkedAgreementResult.getAgreementTimeByPeriod().getAgreementTime().toString()));
				lstReturn.add(new ValueExtractAlarm(workPlaceId,checkedAgreementResult.getEmpId(),alarmValueDate,
						TextResource.localize("KAL010_208"),alarmItem,alarmContent,agreeConditionError.getMessageDisp().v()));
			}
		}
		return lstReturn;
	}
	
	private String yearmonthToString(YearMonth yearMonth){
		if(yearMonth.month()<10){
			return  String.valueOf(yearMonth.year())+"/0"+  String.valueOf(yearMonth.month());
		}
		return String.valueOf(yearMonth.year())+"/"+  String.valueOf(yearMonth.month());
	}
	
	private String formatHourData(String minutes) {
		String h = "", m = "";
		if (minutes != null && !minutes.equals("")) {
			Integer hour = Integer.parseInt(minutes);
			h = hour.intValue() / 60 + "";
			m = hour.intValue() % 60 + "";
			if (h.length() < 2)
				h = "0" + h;
			if (m.length() < 2)
				m = "0" + m;

			return h + ":" + m;
		} else {
			return "0:00";
		}
	}
}
