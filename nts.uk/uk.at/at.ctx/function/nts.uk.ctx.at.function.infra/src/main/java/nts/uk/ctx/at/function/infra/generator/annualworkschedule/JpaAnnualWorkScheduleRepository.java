package nts.uk.ctx.at.function.infra.generator.annualworkschedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
//import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.adapter.RegularSortingTypeImport;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.function.dom.adapter.SortingConditionOrderImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationQueryDtoImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.PositionImport;
import nts.uk.ctx.at.function.dom.adapter.jobtitle.JobTitleAdapter;
import nts.uk.ctx.at.function.dom.adapter.jobtitle.JobTitleImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByPeriodAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceResultImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.GetAgreementPeriodFromYearAdapter;
import nts.uk.ctx.at.function.dom.annualworkschedule.Employee;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBook;
import nts.uk.ctx.at.function.dom.annualworkschedule.SetOutItemsWoSc;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.OutputAgreementTime;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.PageBreakIndicator;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.EmployeeData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.EmployeeInfo;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExcludeEmp;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportItem;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.HeaderData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.PrintFormat;
import nts.uk.ctx.at.function.dom.annualworkschedule.repository.SetOutItemsWoScRepository;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class JpaAnnualWorkScheduleRepository implements AnnualWorkScheduleRepository {

	@Inject
	private SetOutItemsWoScRepository setOutItemsWoScRepository;
	@Inject
	private MonthlyAttendanceItemAdapter monthlyAttendanceItemAdapter;
	@Inject
	private EmployeeInformationAdapter employeeInformationAdapter;
	@Inject
	private CompanyAdapter companyAdapter;
	@Inject
	private RegulationInfoEmployeeAdapter employeeAdapter;
	@Inject
	private GetExcessTimesYearAdapter getExcessTimesYearAdapter;
	@Inject
	private ClosureService closureService;
	@Inject
	private GetAgreementPeriodFromYearAdapter getAgreementPeriodFromYearPub;
	@Inject
	private AgreementTimeByPeriodAdapter agreementTimeByPeriodAdapter;
	@Inject
	private JobTitleAdapter jobTitleAdapter;

	public static final String YM_FORMATER = "uuuu/MM";

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ExportData outputProcess(String cid, String setItemsOutputCd, Year fiscalYear, YearMonth startYm,
			YearMonth endYm, List<Employee> employees, PrintFormat printFormat, int breakPage, ExcludeEmp excludeEmp,
			Integer monthLimit) {
		// ドメインモデル「年間勤務表（36チェックリスト）の出力項目設定」を取得する
		SetOutItemsWoSc setOutItemsWoSc = setOutItemsWoScRepository.getSetOutItemsWoScById(cid, setItemsOutputCd).get();

		// 帳表出力前チェックをする
		this.checkBeforOutput(startYm, endYm, employees, setOutItemsWoSc, printFormat);
		// ユーザ固有情報「年間勤務表（36チェックリスト）」を更新する -> client

		ExportData exportData = new ExportData();
		exportData.setPageBreak(EnumAdaptor.valueOf(breakPage, PageBreakIndicator.class));
		LocalDate endYmd = LocalDate.of(endYm.getYear(), endYm.getMonthValue(), 1).plus(1, ChronoUnit.MONTHS).minus(1,
				ChronoUnit.DAYS);
		List<String> employeeIds = employees.stream().map(m -> m.getEmployeeId()).collect(Collectors.toList());
		// init map employees data
		// <<Public>> 社員の情報を取得する
		exportData.setEmployees(this.getEmployeeInfo(employees, employeeIds, endYmd));
		HeaderData header = new HeaderData();
		header.setPrintFormat(printFormat);
		header.setOutputAgreementTime(setOutItemsWoSc.getDisplayFormat());
		header.setTitle(companyAdapter.getCurrentCompany().map(m -> m.getCompanyName()).orElse(""));
		// B1_1 + B1_2
		String periodStr = startYm.until(endYm, ChronoUnit.MONTHS) == 0
				? startYm.format(DateTimeFormatter.ofPattern(YM_FORMATER))
				: startYm.format(DateTimeFormatter.ofPattern(YM_FORMATER)) + "～"
						+ endYm.format(DateTimeFormatter.ofPattern(YM_FORMATER));
		header.setPeriod(TextResource.localize("KWR008_41") + " " + periodStr);
		List<ItemOutTblBook> listItemOut = setOutItemsWoSc.getListItemOutTblBook().stream()
				.filter(item -> item.isUseClassification()) // ドメインモデル「帳表に出力する項目．使用区分」をチェックする
				.sorted((i1, i2) -> Integer.compare(i1.getSortBy(), i2.getSortBy())).collect(Collectors.toList());
		if (PrintFormat.AGREEMENT_36.equals(printFormat)) {
			// A1_2
			header.setReportName(TextResource.localize("KWR008_58"));
			exportData.setOutNumExceedTime36Agr(setOutItemsWoSc.isOutNumExceedTime36Agr());
		} else {
			// A1_2
			header.setReportName(TextResource.localize("KWR008_57"));
			listItemOut = listItemOut.stream().filter(x -> !x.isItem36AgreementTime()).collect(Collectors.toList());
			exportData.setOutNumExceedTime36Agr(false);
		}
		exportData.setExportItems(listItemOut.stream().map(m -> new ExportItem(m.getCd().v(), m.getHeadingName().v()))
				.collect(Collectors.toList()));
		// 出力項目数による個人情報の出力制限について
		if (listItemOut.size() == 1) {
			header.setEmpInfoLabel(TextResource.localize("KWR008_44"));
		} else if (listItemOut.size() == 2) {
			header.setEmpInfoLabel(TextResource.localize("KWR008_43"));
		} else {
			header.setEmpInfoLabel(TextResource.localize("KWR008_42"));
		}
		YearMonth startYmClone = YearMonth.of(startYm.getYear(), startYm.getMonthValue());
		YearMonth endYmClone = YearMonth.of(endYm.getYear(), endYm.getMonthValue());
		// 期間
		YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(
				nts.arc.time.YearMonth.of(startYm.getYear(), startYm.getMonthValue()),
				nts.arc.time.YearMonth.of(endYm.getYear(), endYm.getMonthValue()));
		// set C2_3, C2_5
		header.setMonthPeriodLabels(
				this.createMonthPeriodLabels(startYmClone, endYm, setOutItemsWoSc.getDisplayFormat()));
		// set C1_2
		header.setMonths(this.createMonthLabels(startYmClone, endYmClone));
		exportData.setHeader(header);

		// 「年間勤務表（36チェックリスト）の出力項目設定」を取得する
		if (PrintFormat.AGREEMENT_36.equals(printFormat)) {
			// 36協定対象外者のチェック
			employeeIds = this.checkExcludeEmp36Agreement(excludeEmp, employeeIds, endYmd);
			// アルゴリズム「年間勤務表の作成」を実行する
			PeriodAtrOfAgreement periodAtr = null;
			if (OutputAgreementTime.TWO_MONTH.equals(setOutItemsWoSc.getDisplayFormat())) {
				periodAtr = PeriodAtrOfAgreement.TWO_MONTHS;
			} else if (OutputAgreementTime.THREE_MONTH.equals(setOutItemsWoSc.getDisplayFormat())){
				periodAtr = PeriodAtrOfAgreement.THREE_MONTHS;
			}
			this.createAnnualWorkSchedule36Agreement(cid, exportData, yearMonthPeriod, employeeIds, listItemOut,
					fiscalYear, startYm, setOutItemsWoSc.isOutNumExceedTime36Agr(), periodAtr, monthLimit);
		} else {
			// 年間勤務表(勤怠チェックリスト)を作成
			this.createAnnualWorkScheduleAttendance(exportData, yearMonthPeriod, employeeIds, listItemOut, startYm,
					setOutItemsWoSc.getDisplayFormat());
		}
		// 社員を並び替える
		this.sortEmployees(exportData, endYmd);
		// 出力するデータ件数をチェックする
		if (!exportData.hasDataItemOutput()) {
			throw new BusinessException("Msg_885");
		}
		return exportData;
	}

	/*
	 * 帳表出力前チェックをする
	 */
	private void checkBeforOutput(YearMonth startYm, YearMonth endYm, List<Employee> employees,
			SetOutItemsWoSc setOutItemsWoSc, PrintFormat printFormat) {
		// 対象期間をチェックする
		if (startYm.until(endYm, ChronoUnit.MONTHS) + 1 > 12)
			throw new BusinessException("Msg_883");
		// 出力対象の社員をチェックする
		if (employees == null || employees.isEmpty())
			throw new BusinessException("Msg_884");
		// 出力項目をチェックする
		List<ItemOutTblBook> listItemOutTblBook = setOutItemsWoSc.getListItemOutTblBook().stream()
				.filter(x -> x.isUseClassification()).collect(Collectors.toList());
		if (PrintFormat.ATTENDANCE.equals(printFormat)) {
			// 印刷形式="勤怠チェックリスト"の場合
			listItemOutTblBook = listItemOutTblBook.stream().filter(x -> !x.isItem36AgreementTime())
					.collect(Collectors.toList());
		}
		if (listItemOutTblBook.size() == 0) {
			throw new BusinessException("Msg_880");
		}
	}

	/**
	 * 社員を並び替える(任意)
	 * 
	 * @param employeeIds
	 *            List＜社員ID＞
	 * @param endYmd
	 *            対象期間の終了日
	 * @return
	 */
	private List<String> sortEmployees(List<String> employeeIds, LocalDate endYmd) {
		List<SortingConditionOrderImport> ordersImport = new ArrayList<>();
		ordersImport.add(new SortingConditionOrderImport(1, RegularSortingTypeImport.WORKPLACE));
		List<String> listEmp = employeeAdapter.sortEmployee(AppContexts.user().companyId(), employeeIds, ordersImport,
				GeneralDateTime.localDateTime(LocalDateTime.of(endYmd, LocalTime.of(0, 0))));
		return listEmp;
	}

	private void sortEmployees(ExportData exportData, LocalDate endYmd) {
		List<String> listEmpSorted = new ArrayList<>();
		List<String> listEmpIdOrgin = exportData.getEmployees().entrySet().stream().map(x -> x.getKey())
				.collect(Collectors.toList());
		List<String> listEmp = this.sortEmployees(listEmpIdOrgin, endYmd);
		listEmpSorted.addAll(listEmp);
		listEmpSorted.removeAll(exportData.getEmployeeIdsError());
		exportData.setEmployeeIds(listEmpSorted);
	}

	/**
	 * 社員の情報を取得する
	 * 
	 * @param employees
	 *            data Employees from client
	 * @param employeeIds
	 *            対象社員ID（List）
	 * @param endYmd
	 *            対象期間の終了日
	 * @return
	 */
	private Map<String, EmployeeData> getEmployeeInfo(List<Employee> employees, List<String> employeeIds,
			LocalDate endYmd) {
		// init Employee name map
		Map<String, String> empNameMap = employees.stream()
				.collect(Collectors.toMap(Employee::getEmployeeId, Employee::getName));
		Map<String, EmployeeData> employeesData = new HashMap<>();
		employeeInformationAdapter.getEmployeeInfo(new EmployeeInformationQueryDtoImport(employeeIds,
				GeneralDate.localDate(endYmd), true, false, true, true, false, false)).forEach(emp -> {
					employeesData.put(emp.getEmployeeId(),
							EmployeeData.builder()
									.employeeInfo(new EmployeeInfo(
											emp.getWorkplace() == null ? "" : emp.getWorkplace().getWorkplaceCode(),
											emp.getWorkplace() == null ? "マスタ未登録"
													: TextResource.localize("KWR008_50") + " "
															+ emp.getWorkplace().getWorkplaceName(),
											emp.getEmployeeCode(), empNameMap.get(emp.getEmployeeId()),
											emp.getEmployment() == null ? "" : emp.getEmployment().getEmploymentName(),
											emp.getPosition() == null ? "" : emp.getPosition().getPositionName()))
									.annualWorkSchedule(new HashMap<>()).build());
				});
		return employeesData;
	}

	/**
	 * 36協定対象外者のチェック
	 */
	private List<String> checkExcludeEmp36Agreement(ExcludeEmp excludeEmp, List<String> employeeIds, LocalDate endYmd) {
		List<String> empId = new ArrayList<>();
		GeneralDate endDate = GeneralDate.localDate(endYmd);
		// 年間勤務表（36チェックリスト）の出力条件.印字区分をチェック
		if (ExcludeEmp.NOT_PRINT.equals(excludeEmp)) {
			// <<Public>> 社員の情報を取得する
			List<EmployeeInformationImport> empInfoList = employeeInformationAdapter
					.getEmployeeInfo(new EmployeeInformationQueryDtoImport(employeeIds, endDate, false, false, true,
							false, false, false));
			for (EmployeeInformationImport emp : empInfoList) {
				// 取得した社員の「職位情報.管理職とする」をチェック
				if (!this.checkIsManager(emp, endDate)) {
					// パラメータ.対象社員ID（List）へ追加する
					empId.add(emp.getEmployeeId());
				}
			}
		} else {
			// 選択している社員すべて パラメータ.対象社員ID(List) へ追加する
			empId = employeeIds;
		}
		// パラメータ.対象社員ID（List）の件数をチェックする
		if (empId.isEmpty()) {
			// エラーメッセージ(#Msg_1397#)を表示
			throw new BusinessException("Msg_1397");
		}
		return empId;
	}

	private boolean checkIsManager(EmployeeInformationImport emp, GeneralDate endDate) {
		PositionImport postion = emp.getPosition();
		if (postion == null) {
			return false;
		}
		// 職位IDから職位を取得する
		Optional<JobTitleImport> jobTitleOtp = jobTitleAdapter.findByJobId(AppContexts.user().companyId(),
				emp.getPosition().getPositionId(), endDate);
		if (!jobTitleOtp.isPresent()) {
			return false;
		}
		return jobTitleOtp.get().isManager();
	}

	/**
	 * 年間勤務表の作成
	 */
	private void createAnnualWorkSchedule36Agreement(String cid, ExportData exportData, YearMonthPeriod yearMonthPeriod,
			List<String> employeeIds, List<ItemOutTblBook> listItemOut, Year fiscalYear, YearMonth startYm,
			boolean isOutNumExceed, PeriodAtrOfAgreement periodAtr, Integer monthLimit) {
		Optional<ItemOutTblBook> outputAgreementTime36 = listItemOut.stream().filter(m -> m.isItem36AgreementTime())
				.findFirst();
		employeeIds.forEach(empId -> {
			EmployeeData empData = exportData.getEmployees().get(empId);
			Map<String, AnnualWorkScheduleData> annualWorkScheduleData = new HashMap<>();
			// 36協定時間を出力するかのチェックをする
			if (outputAgreementTime36.isPresent()) {
				// アルゴリズム「36協定時間の作成」を実行する
				annualWorkScheduleData.putAll(this.create36AgreementTime(cid, yearMonthPeriod, empId,
						outputAgreementTime36.get(), fiscalYear, startYm, isOutNumExceed, periodAtr, monthLimit,
						exportData.getHeader() == null ? new ArrayList<>()
								: (exportData.getHeader().getMonthPeriodLabels()== null? new ArrayList<>(): exportData.getHeader().getMonthPeriodLabels())));
			}
			empData.setAnnualWorkSchedule(annualWorkScheduleData);
		});
		// アルゴリズム「任意項目の作成」を実行する
		this.createOptionalItems(exportData, yearMonthPeriod, employeeIds,
				listItemOut.stream().filter(item -> !item.isItem36AgreementTime()).collect(Collectors.toList()),
				startYm);
		// 対象の社員IDをエラーリストに格納する
		exportData.storeEmployeeError();
	}

	/**
	 * 36協定時間の作成
	 * 
	 * @param yearMonthPeriod
	 *            対象期間
	 * @param employeeId
	 *            社員ID
	 * @param outNumExceedTime36Agr
	 *            超過月数出力するか
	 * @param displayFormat
	 *            表示形式
	 */
	private Map<String, AnnualWorkScheduleData> create36AgreementTime(String cid, YearMonthPeriod yearMonthPeriod,
			String employeeId, ItemOutTblBook outputAgreementTime36, Year fiscalYear, YearMonth startYm, boolean isOutNumExceed,
			PeriodAtrOfAgreement periodAtr, Integer monthLimit, List<String> header) {
		GeneralDate criteria = GeneralDate.ymd(fiscalYear.v(), 12, 31);
		
		Month startMonth = new Month(startYm.getMonth().getValue());
		// RequestList453
		// 36協定時間を取得する
		// 明細用
		List<AgreementTimeByPeriodImport> listAgreementTimeByMonth = agreementTimeByPeriodAdapter.algorithm(cid, employeeId,
				criteria, startMonth, fiscalYear, PeriodAtrOfAgreement.ONE_MONTH);
		// 年間合計用
		List<AgreementTimeByPeriodImport> listAgreementTimeByYear = agreementTimeByPeriodAdapter.algorithm(cid, employeeId,
				criteria, startMonth, fiscalYear, PeriodAtrOfAgreement.ONE_YEAR);
		
		if (listAgreementTimeByMonth.isEmpty())
			return new HashMap<>();

		// パラメータ「超過月数を出力する」をチェックする
		Integer monthsExceeded = 0;
		if (isOutNumExceed) {
			// 年間超過回数の取得
			// RequestList458
			monthsExceeded = getExcessTimesYearAdapter.algorithm(employeeId, fiscalYear);
		}
		// パラメータ「表示形式」をチェックする
		List<AgreementTimeByPeriodImport> listExcesMonths = new ArrayList<>();
		if (PeriodAtrOfAgreement.TWO_MONTHS.equals(periodAtr) || PeriodAtrOfAgreement.THREE_MONTHS.equals(periodAtr)) {
			listExcesMonths = this.create36AgreementFewMonth(cid, employeeId, fiscalYear, startYm, periodAtr);
		}

		Map<String, AnnualWorkScheduleData> data = new HashMap<>();
		// アルゴリズム「月平均の算出」を実行する
		data.put(outputAgreementTime36.getCd().v(),
				AnnualWorkScheduleData.fromAgreementTimeList(outputAgreementTime36, listAgreementTimeByMonth,
						listAgreementTimeByYear, listExcesMonths, startYm, monthsExceeded, monthLimit, periodAtr, header)
						.calc(false));
		return data;
	}

	/**
	 * 任意項目の作成
	 * 
	 * @param yearMonthPeriod
	 *            対象期間
	 * @param employeeId
	 *            社員ID
	 * @param listItemOut
	 *            「36協定時間」以外の出力対象の項目設定（List）
	 */
	private void createOptionalItems(ExportData exportData, YearMonthPeriod yearMonthPeriod, List<String> employeeIds,
			List<ItemOutTblBook> listItemOut, YearMonth startYm) {
		List<Integer> allItemIds = new ArrayList<>();
		for(ItemOutTblBook itemOut: listItemOut){
			List<Integer> itemIds = itemOut.getListOperationSetting().stream().map(os -> os.getAttendanceItemId())
					.collect(Collectors.toList());
			allItemIds.addAll(itemIds);
		}
		allItemIds = allItemIds.stream().distinct().collect(Collectors.toList());
		// アルゴリズム「対象期間の月次データの取得」を実行する
		List<MonthlyAttendanceResultImport> allMonthlyAtt = monthlyAttendanceItemAdapter
				.getMonthlyValueOfParallel(employeeIds, yearMonthPeriod, allItemIds);

		listItemOut.forEach(itemOut -> {
			// アルゴリズム「出力項目の値の算出」を実行する
			Map<String, AnnualWorkScheduleData> empData = this.createOptionalItem(allMonthlyAtt, employeeIds, itemOut,
					startYm);
			employeeIds.forEach(empId -> {
				AnnualWorkScheduleData data = empData.get(empId);
				exportData.getEmployees().get(empId).getAnnualWorkSchedule().put(itemOut.getCd().v(), data);
			});
		});
	}

	/**
	 * 出力項目の値の算出
	 */
	private Map<String, AnnualWorkScheduleData> createOptionalItem(List<MonthlyAttendanceResultImport> allMonthlyAtt,
			List<String> employeeIds, ItemOutTblBook itemOut, YearMonth startYm) {
		// アルゴリズム「月平均の算出」を実行する
		Map<String, AnnualWorkScheduleData> empData = new HashMap<>();
		employeeIds.forEach(empId -> {
			List<MonthlyAttendanceResultImport> listMonthly = allMonthlyAtt.stream()
					.filter(x -> x.getEmployeeId().equals(empId)).collect(Collectors.toList());
			// アルゴリズム「月平均の算出」を実行する
			empData.put(empId,
					AnnualWorkScheduleData.fromMonthlyAttendanceList(itemOut, listMonthly, startYm).calc(true));
		});
		return empData;
	}

	/**
	 * 2・3ヶ月の36協定時間の作成
	 */
	private List<AgreementTimeByPeriodImport> create36AgreementFewMonth(String cid, String employeeId, Year fiscalYear,
			YearMonth startYm, PeriodAtrOfAgreement periodAtr) {
		// アルゴリズム「2・3ヶ月の36協定時間の作成」を実行する
		Closure closure = closureService.getClosureDataByEmployee(employeeId, GeneralDate.today());
		// 年度から集計期間を取得
		Optional<DatePeriod> datePeriod = getAgreementPeriodFromYearPub.algorithm(fiscalYear, closure);
		// ドメイン「３６協定運用設定」を取得する
		Month startMonth;
		startMonth = new Month(startYm.getMonthValue());
		// 基準日 = 「年度から集計期間を取得する」のOutputのenddate
		GeneralDate criteria = datePeriod.get().end();
		// 指定期間36協定時間の取得
		return agreementTimeByPeriodAdapter.algorithm(cid, employeeId, criteria, startMonth, fiscalYear, periodAtr);
	}

	/**
	 * 年間勤務表(勤怠チェックリスト)を作成
	 */
	private void createAnnualWorkScheduleAttendance(ExportData exportData, YearMonthPeriod yearMonthPeriod,
			List<String> employeeIds, List<ItemOutTblBook> listItemOut, YearMonth startYm,
			OutputAgreementTime displayFormat) {
		// アルゴリズム「任意項目の作成」を実行する
		this.createOptionalItems(exportData, yearMonthPeriod, employeeIds,
				listItemOut.stream().filter(item -> !item.isItem36AgreementTime()).collect(Collectors.toList()),
				startYm);
		// 対象の社員IDをエラーリストに出力する
		exportData.storeEmployeeError();
	}

	/**
	 * C1_2
	 * 
	 * @param startYm
	 * @param endYm
	 * @return
	 */
	private List<String> createMonthLabels(YearMonth startYm, YearMonth endYm) {
		List<String> months = Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "");
		int i = 0;
		while (!startYm.isAfter(endYm)) {
			months.set(i, String.valueOf(startYm.getMonthValue()));
			startYm = startYm.plusMonths(1);
			i++;
		}
		return months;
	}

	/**
	 * Create C2_3, C2_5
	 * 
	 * @param startYm
	 * @param endYm
	 * @param outputAgreementTime
	 * @return
	 */
	private List<String> createMonthPeriodLabels(YearMonth startYm, YearMonth endYm,
			OutputAgreementTime outputAgreementTime) {
		int distances = 0;
		if (OutputAgreementTime.TWO_MONTH.equals(outputAgreementTime))
			distances = 2;
		else if (OutputAgreementTime.THREE_MONTH.equals(outputAgreementTime))
			distances = 3;
		if (distances == 0)
			return null;
		List<String> monthPeriodLabels = new ArrayList<>();
		YearMonth periodStart = startYm;
		while (periodStart.isBefore(endYm)) {
			YearMonth periodEnd = periodStart.plusMonths(distances - 1);
			monthPeriodLabels.add(periodStart.getMonthValue() + "～" + periodEnd.getMonthValue());
			periodStart = periodStart.plusMonths(distances);
		}
		return monthPeriodLabels;
	}
}