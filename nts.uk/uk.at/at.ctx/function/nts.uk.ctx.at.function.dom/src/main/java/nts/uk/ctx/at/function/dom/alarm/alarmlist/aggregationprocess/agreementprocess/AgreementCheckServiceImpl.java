package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.agreementprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.log4j.Logger;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckRecordAgreementAdapter;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedAgreementResult;
import nts.uk.ctx.at.function.dom.adapter.agreement.CheckedOvertimeImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.ErAlConstant;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AgreeConditionError;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AgreeNameError;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.AlarmChkCondAgree36;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.IAgreeNameErrorRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.Period;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.agree36.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AgreementCheckServiceImpl implements AgreementCheckService{

	@Inject
	private CheckRecordAgreementAdapter checkAgreementAdapter;

	@Inject
	private IAgreeNameErrorRepository agreeNameRepo;

	@Resource
	private SessionContext scContext;
	
	private AgreementCheckService self;
	
	@PostConstruct
	public void init() {
		// Get self.
		this.self = scContext.getBusinessObject(AgreementCheckService.class);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void check(List<AlarmCheckConditionByCategory> agreementErAl, List<PeriodByAlarmCategory> periodAlarms,
			Optional<AgreementOperationSettingImport> agreementSetObj, Consumer<Integer> counter,
			Supplier<Boolean> shouldStop, List<ValueExtractAlarm> result, List<String> empIds,
			Map<String, Integer> empIdToClosureId, List<Closure> closureList,
			Map<String, EmployeeSearchDto> mapEmployee, List<String> employeeIds) {
		for (AlarmCheckConditionByCategory alarmCheck : agreementErAl) {
			// 36協定のアラームチェック条件
			AlarmChkCondAgree36 alarmChkCon36 = alarmCheck.getAlarmChkCondAgree36();

			// アルゴリズム「超過回数チェック」を実行する
			for (PeriodByAlarmCategory periodAlarm : periodAlarms) {

				synchronized (this) {

					if (shouldStop.get()) {
						return;
					}
				}

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
								List<CheckedAgreementResult> checkAgreementsResult = checkAgreementAdapter
										.checkArgreementResult(employeeIds,
												new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()),
												agreeConditionError, agreementSetObj, closureList, empIdToClosureId);
								if (!CollectionUtil.isEmpty(checkAgreementsResult)) {
									result.addAll(generationValueExtractAlarm(mapEmployee, checkAgreementsResult,
											agreeConditionError, optAgreeName));
								}
							}
						});

				if (Period.Yearly.value == periodAlarm.getPeriod36Agreement()) {
					List<DatePeriod> periodsYear = new ArrayList<>();
					periodsYear.add(new DatePeriod(periodAlarm.getStartDate(), periodAlarm.getEndDate()));
					List<CheckedOvertimeImport> checkOvertimes = checkAgreementAdapter.checkNumberOvertime(empIds,
							periodsYear, alarmChkCon36.getListCondOt());
					for (CheckedOvertimeImport check : checkOvertimes) {

						String hour = check.getOt36().hour() + "";
						if (hour.length() < 2)
							hour = "0" + hour;
						String minute = check.getOt36().minute() + "";
						if (minute.length() < 2)
							minute = "0" + minute;
						String ot36 = hour + ":" + minute;

						String datePeriod = check.getDatePeriod().start().toString(ErAlConstant.DATE_FORMAT) + ErAlConstant.PERIOD_SEPERATOR
								+ check.getDatePeriod().end().toString(ErAlConstant.DATE_FORMAT);

						result.add(new ValueExtractAlarm(mapEmployee.get(check.getEmployeeId()).getWorkplaceId(),
								check.getEmployeeId(), datePeriod, TextResource.localize("KAL010_208"),
								TextResource.localize("KAL010_201"), TextResource.localize("KAL010_202",
										check.getNo() + "", ot36, check.getExcessNum().v() + ""),
								check.getMessageDisp().v()));
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
				// workplaceID
				String workPlaceId = mapEmployee.get(checkedAgreementResult.getEmpId()).getWorkplaceId();

				// 年月日
				String alarmValueDate = yearmonthToString(
						checkedAgreementResult.getAgreementTimeByPeriod().getStartMonth()) + ErAlConstant.PERIOD_SEPERATOR
						+ yearmonthToString(checkedAgreementResult.getAgreementTimeByPeriod().getEndMonth());
				// alarm name
				String alarmItem = optAgreeName.isPresent() ? optAgreeName.get().getName().v() : "";
				// カテゴリ
				String alarmContent = TextResource.localize("KAL010_203", alarmItem,
						formatHourData(checkedAgreementResult.getUpperLimit()), formatHourData(
								checkedAgreementResult.getAgreementTimeByPeriod().getAgreementTime().toString()));
				lstReturn.add(new ValueExtractAlarm(workPlaceId, checkedAgreementResult.getEmpId(), alarmValueDate,
						TextResource.localize("KAL010_208"), alarmItem, alarmContent,
						agreeConditionError.getMessageDisp().v()));
			}
		}
		return lstReturn;
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
}
