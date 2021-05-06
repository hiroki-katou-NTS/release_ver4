package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.agreementprocess;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckRecordAgreementAdapter;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedAgreementResult;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedOvertimeImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.persistenceextractresult.AlarmExtractionCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.*;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.persistenceextractresult.*;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Stateless
public class AgreementCheckServiceImpl implements AgreementCheckService{

	@Inject
	private CheckRecordAgreementAdapter checkAgreementAdapter;

	@Inject
	private IAgreeNameErrorRepository agreeNameRepo;

	@Resource
	private SessionContext scContext;
	
	private AgreementCheckService self;
	@Inject
	private AgreementOperationSettingAdapter agreementOperationSettingAdapter;
	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;

	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	@PostConstruct
	public void init() {
		// Get self.
		this.self = scContext.getBusinessObject(AgreementCheckService.class);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void check(List<AlarmCheckConditionByCategory> agreementErAl, List<PeriodByAlarmCategory> periodAlarms,
			Optional<AgreementOperationSettingImport> agreementSetObj, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop, List<ValueExtractAlarm> result,
			Map<String, Integer> empIdToClosureId, List<Closure> closureList,
			Map<String, EmployeeSearchDto> mapEmployee, List<String> employeeIds) {
		String companyId = AppContexts.user().companyId();
		for (AlarmCheckConditionByCategory alarmCheck : agreementErAl) {
			// 36協定のアラームチェック条件
			AlarmChkCondAgree36 alarmChkCon36 = alarmCheck.getAlarmChkCondAgree36();

			
			for (PeriodByAlarmCategory periodAlarm : periodAlarms) {

				synchronized (this) {

					if (shouldStop.get()) {
						return;
					}
				}
				//????
				Object objCheckAgreement = checkAgreementAdapter.getCommonSetting(companyId, employeeIds, 
						new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));

				// 抽出条件に対応する期間を取得する
				// List<36協定エラーアラームのチェック条件>
				alarmChkCon36.getListCondError().stream().filter(e -> e.getUseAtr() == UseClassification.Use)
						.forEach(agreeConditionError -> {

							Period periodCheck = getPeriod(agreeConditionError);

							if (periodAlarm.getPeriod36Agreement() == periodCheck.value) {
								// ドメインモデル「36協定エラーアラームチェック名称」を取得する
								Optional<AgreeNameError> optAgreeName = agreeNameRepo.findById(periodCheck.value,
										agreeConditionError.getErrorAlarm().value);

								// アルゴリズム「36協定実績をチェックする」を実行する
								/*List<CheckedAgreementResult> checkAgreementsResult = checkAgreementAdapter
										.checkArgreementResult(employeeIds,
												new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()),
												agreeConditionError, agreementSetObj, closureList, empIdToClosureId,objCheckAgreement);
								if (!CollectionUtil.isEmpty(checkAgreementsResult)) {
									result.addAll(generationValueExtractAlarm(mapEmployee, checkAgreementsResult,
											agreeConditionError, optAgreeName));
								}*/
							}
						});

				if (Period.Yearly.value == periodAlarm.getPeriod36Agreement()) {
					List<DatePeriod> periodsYear = new ArrayList<>();
					periodsYear.add(new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));
					// アルゴリズム「超過回数チェック」を実行する
					List<CheckedOvertimeImport> checkOvertimes = checkAgreementAdapter.checkNumberOvertime(employeeIds,
							periodsYear, alarmChkCon36.getListCondOt());
					for (CheckedOvertimeImport check : checkOvertimes) {

						String checkedValue = String.valueOf(check.getCountAgreementOneEmp());
						
						String hour = check.getOt36().hour() + "";
						if (hour.length() < 2)
							hour = "0" + hour;
						String minute = check.getOt36().minute() + "";
						if (minute.length() < 2)
							minute = "0" + minute;
						String ot36 = hour + ":" + minute;
						
						String datePeriod = TextResource.localize("KAL010_906",check.getDatePeriod().start().toString(ErAlConstant.YM_FORMAT),check.getDatePeriod().end().toString(ErAlConstant.YM_FORMAT));
						result.add(new ValueExtractAlarm(mapEmployee.get(check.getEmployeeId()).getWorkplaceId(),
								check.getEmployeeId(), datePeriod, TextResource.localize("KAL010_208"),
								//TextResource.localize("KAL010_201")
								TextResource.localize("KAL010_120",check.getNo()+"")
								, TextResource.localize("KAL010_202",
										check.getNo() + "", ot36, check.getExcessNum().v() + ""),
								check.getMessageDisp().v(),checkedValue));
					}
				}
			}
		}
		self.countFinishedEmp(counter, employeeIds);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void countFinishedEmp(Consumer<Integer> counter, List<String> employeeIds) {
		synchronized (this) {
			counter.accept(employeeIds.size());
		}
	}

	private Period getPeriod(AgreeConditionError agreeConditionError) {
		if (agreeConditionError.getPeriod() == Period.One_Week || agreeConditionError.getPeriod() == Period.Two_Week
				|| agreeConditionError.getPeriod() == Period.Four_Week) {
			return Period.One_Week;
		}
		return agreeConditionError.getPeriod();
	}

	private List<ValueExtractAlarm> generationValueExtractAlarm(Map<String, EmployeeSearchDto> mapEmployee,
			List<CheckedAgreementResult> checkAgreementsResult, AgreeConditionError agreeConditionError,
			Optional<AgreeNameError> optAgreeName) {
		List<ValueExtractAlarm> lstReturn = new ArrayList<>();
		for (CheckedAgreementResult checkedAgreementResult : checkAgreementsResult) {
			if (checkedAgreementResult.isCheckResult()) {
				String checkedValue = formatHourData(checkedAgreementResult.getAgreementTimeByPeriod().getAgreementTime().toString());
				// workplaceID
				String workPlaceId = mapEmployee.get(checkedAgreementResult.getEmpId()).getWorkplaceId();

				// 年月日
				String alarmValueDate =TextResource.localize("KAL010_906", 
						yearmonthToString(checkedAgreementResult.getAgreementTimeByPeriod().getStartMonth())+"" 
						,
						yearmonthToString(checkedAgreementResult.getAgreementTimeByPeriod().getEndMonth())+"");
				// alarm name
				String alarmItem = optAgreeName.isPresent() ? optAgreeName.get().getName().v() : "";
				//
				String nameByMonthDistance = alarmItemByMonth(checkedAgreementResult.getAgreementTimeByPeriod().getStartMonth(),checkedAgreementResult.getAgreementTimeByPeriod().getEndMonth());
				// カテゴリ
				String alarmContent = TextResource.localize("KAL010_203", nameByMonthDistance,
						formatHourData(checkedAgreementResult.getUpperLimit()), formatHourData(
								checkedAgreementResult.getAgreementTimeByPeriod().getAgreementTime().toString()));
				lstReturn.add(new ValueExtractAlarm(workPlaceId, checkedAgreementResult.getEmpId(), alarmValueDate,
						TextResource.localize("KAL010_208"), alarmItem, alarmContent,
						agreeConditionError.getMessageDisp().v(),checkedValue));
			}
		}
		return lstReturn;
	}
	
	private String alarmItemByMonth(YearMonth yearMonthStart,YearMonth yearMonthEnd) {
		String alarmItem = "";
		int yearStart = yearMonthStart.year();
		int yearEnd = yearMonthEnd.year();
		int monthDistance = (yearEnd - yearStart)*12 +yearMonthEnd.month() -  yearMonthStart.month();
		if(monthDistance == 1) {
			alarmItem = TextResource.localize("KAL010_212");
		}else if(monthDistance == 2) {
			alarmItem = TextResource.localize("KAL010_213");
		}else if(monthDistance == 3) {
			alarmItem = TextResource.localize("KAL010_214");
		}else if(monthDistance == 4) {
			alarmItem = TextResource.localize("KAL010_215");
		}else if(monthDistance == 5) {
			alarmItem = TextResource.localize("KAL010_216");
		}
		return alarmItem ;
		
	}

	private String yearmonthToString(YearMonth yearMonth) {
		return GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1).toString(ErAlConstant.YM_FORMAT);
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

	@Override
	public void get36AlarmCheck(String cid, AlarmChkCondAgree36 alarmChkCon36,
			List<PeriodByAlarmCategory> periodAlarms,
			Consumer<Integer> counter, Supplier<Boolean> shouldStop,
			List<WorkPlaceHistImport> getWplByListSidAndPeriod, List<String> employeeIds,
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckInfor,
			List<AlarmEmployeeList> alarmEmployeeList, List<AlarmExtractionCondition> alarmExtractConditions,
			String alarmCheckConditionCode) {
		//３６協定運用設定
		Optional<AgreementOperationSettingImport> aggreementSetting = agreementOperationSettingAdapter.findForAlarm(cid);
		if(!aggreementSetting.isPresent()) {
			throw new BusinessException("Msg_"); //TODO	
		}
		
		AgreementOperationSettingImport aggSetting = aggreementSetting.get();
		val cacheCarrier = new CacheCarrier();
		// 社員(list)に対応する処理締めを取得する
		Map<String, Closure> mapEmpClosures = ClosureService
				.getClosureByEmployees(
						ClosureService.createRequireM7(closureRepository, closureEmploymentRepo,
								shareEmploymentAdapter),
						cacheCarrier, employeeIds, GeneralDate.today());	
		// 36協定のアラームチェック条件			
		for (PeriodByAlarmCategory periodAlarm : periodAlarms) {

			synchronized (this) {

				if (shouldStop.get()) {
					return;
				}
			}
			//指定期間36協定時間の取得
			Object objCheckAgreement = checkAgreementAdapter.getCommonSetting(cid, employeeIds, 
					new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));

			// 抽出条件に対応する期間を取得する
			// List<36協定エラーアラームのチェック条件>
			alarmChkCon36.getListCondError().stream().filter(e -> e.getUseAtr() == UseClassification.Use)
					.forEach(agreeConditionError -> {
						Period periodCheck = getPeriod(agreeConditionError);
						lstCheckInfor.add(new AlarmListCheckInfor(String.valueOf(periodCheck.value), AlarmListCheckType.FixCheck));
						if (periodAlarm.getPeriod36Agreement() == periodCheck.value) {
							// ドメインモデル「36協定エラーアラームチェック名称」を取得する
							Optional<AgreeNameError> optAgreeName = agreeNameRepo.findById(periodCheck.value,
									agreeConditionError.getErrorAlarm().value);

							// アルゴリズム「36協定実績をチェックする」を実行する
							List<CheckedAgreementResult> checkAgreementsResult = checkAgreementAdapter
									.checkArgreementResult(employeeIds,
											new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()),
											agreeConditionError, aggreementSetting, mapEmpClosures,objCheckAgreement);
							if (!CollectionUtil.isEmpty(checkAgreementsResult)) {
								/*result.addAll(generationValueExtractAlarm(mapEmployee, checkAgreementsResult,
										agreeConditionError, optAgreeName));*/
								//TODO
							}
						}
					});

			if (Period.Yearly.value == periodAlarm.getPeriod36Agreement()) {
				List<DatePeriod> periodsYear = new ArrayList<>();
				periodsYear.add(new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));
				// アルゴリズム「超過回数チェック」を実行する
				List<CheckedOvertimeImport> checkOvertimes = checkAgreementAdapter.checkNumberOvertime(employeeIds,
						periodsYear, alarmChkCon36.getListCondOt());
				for (CheckedOvertimeImport check : checkOvertimes) {

					String checkedValue = String.valueOf(check.getCountAgreementOneEmp());
					
					String hour = check.getOt36().hour() + "";
					if (hour.length() < 2)
						hour = "0" + hour;
					String minute = check.getOt36().minute() + "";
					if (minute.length() < 2)
						minute = "0" + minute;
					String ot36 = hour + ":" + minute;
					
					String datePeriod = TextResource.localize("KAL010_906",check.getDatePeriod().start().toString(ErAlConstant.YM_FORMAT),check.getDatePeriod().end().toString(ErAlConstant.YM_FORMAT));
					/*result.add(new ValueExtractAlarm(mapEmployee.get(check.getEmployeeId()).getWorkplaceId(),
							check.getEmployeeId(), datePeriod, TextResource.localize("KAL010_208"),
							//TextResource.localize("KAL010_201")
							TextResource.localize("KAL010_120",check.getNo()+"")
							, TextResource.localize("KAL010_202",
									check.getNo() + "", ot36, check.getExcessNum().v() + ""),
							check.getMessageDisp().v(),checkedValue));*/
					//TODO
				}
			}
		}
		self.countFinishedEmp(counter, employeeIds);
		
	}
	
	private Integer calculateTagetYear(YearMonth tagetYM, int startingMonth) {

		Integer tagetYear = tagetYM.year();
		// 対象年月.月＞起算月
		if (tagetYM.month() < startingMonth) {
			tagetYear = tagetYM.year() - 1;
		}
		return tagetYear;
	}

}
