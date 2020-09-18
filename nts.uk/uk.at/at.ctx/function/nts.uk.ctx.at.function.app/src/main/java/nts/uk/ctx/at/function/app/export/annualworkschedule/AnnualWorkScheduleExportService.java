package nts.uk.ctx.at.function.app.export.annualworkschedule;

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

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.Year;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.function.dom.adapter.RegularSortingTypeImport;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.function.dom.adapter.SortingConditionOrderImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationQueryDtoImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.PositionImport;
import nts.uk.ctx.at.function.dom.adapter.jobtitle.JobTitleAdapter;
import nts.uk.ctx.at.function.dom.adapter.jobtitle.JobTitleImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByEmpImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByPeriodAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.AgreementTimeByPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceResultImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.AgreementOperationSettingImport;
import nts.uk.ctx.at.function.dom.adapter.standardtime.GetAgreementPeriodFromYearAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.TimeOverLimitTypeImport;
import nts.uk.ctx.at.function.dom.annualworkschedule.Employee;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBook;
import nts.uk.ctx.at.function.dom.annualworkschedule.SetOutItemsWoSc;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.AnnualWorkSheetPrintingForm;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.MonthsInTotalDisplay;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.PageBreakIndicator;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.TotalAverageDisplay;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleGenerator;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.EmployeeData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.EmployeeInfo;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExcludeEmp;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportItem;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.HeaderData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.PrintFormat;
import nts.uk.ctx.at.function.dom.annualworkschedule.repository.SetOutItemsWoScRepository;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.export.WorkRecordExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.AffiliationStatus;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.EmpAffInfoExport;
import nts.uk.ctx.at.record.dom.workrecord.export.dto.PeriodInformation;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeYear;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreMaxAverageTime;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreMaxTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.company.CompanyAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AnnualWorkScheduleExportService extends ExportService<AnnualWorkScheduleExportQuery> {
	@Inject
	private AnnualWorkScheduleGenerator generator;
	@Inject
	private AgreementOperationSettingAdapter agreementOperationSettingAdapter;

	// move JpaAnnualWorkScheduleRepository class to here
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
	private GetAgreementPeriodFromYearAdapter getAgreementPeriodFromYearPub;
	@Inject
	private AgreementTimeByPeriodAdapter agreementTimeByPeriodAdapter;
	@Inject
	private JobTitleAdapter jobTitleAdapter;
	@Inject
	private AgreementOperationSettingRepository agreementOperationSettingRepository;
	@Inject
	private WorkRecordExport workRecordExport;
	@Inject
	private GetExcessTimesYearAdapter getExcessTimesYearAdapter;
	@Inject
	private RecordDomRequireService requireService;

	public static final String YM_FORMATER = "uuuu/MM";
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected void handle(ExportServiceContext<AnnualWorkScheduleExportQuery> context) {
		String companyId = AppContexts.user().companyId();
		AnnualWorkScheduleExportQuery query = context.getQuery();
		PrintFormat printFormat = EnumAdaptor.valueOf(query.getPrintFormat(), PrintFormat.class);
		//・基準月（画面AのA11_2）
		Integer baseMonth = query.getCurentMonth();
		ExcludeEmp excludeEmp = EnumAdaptor.valueOf(query.getExcludeEmp(), ExcludeEmp.class);
		List<Employee> employees = query.getEmployees().stream()
                .distinct()
				.map(m -> new Employee(m.getEmployeeId(), m.getCode(), m.getName(), m.getWorkplaceName()))
				.collect(Collectors.toList());
		Year fiscalYear = null;
		YearMonth startYm = null;
		YearMonth endYm = null;
		Integer monthLimit;
		GeneralDate baseDate = GeneralDate.fromString(query.getBaseDate(), "yyyy/MM/dd");
		// ドメインモデル「３６協定運用設定」を取得する
		Optional<AgreementOperationSettingImport> agreementSetObj = agreementOperationSettingAdapter.find(companyId);
		if (PrintFormat.AGREEMENT_36.equals(printFormat)) {
			fiscalYear = new Year(Integer.parseInt(query.getFiscalYear()));
			startYm = this.getStartYearMonth(agreementSetObj, fiscalYear);
			endYm = startYm.plusMonths(11);
		} else {
			startYm = YearMonth.parse(query.getStartYearMonth(), DateTimeFormatter.ofPattern("uuuu/MM"));
			endYm = YearMonth.parse(query.getEndYearMonth(), DateTimeFormatter.ofPattern("uuuu/MM"));
		}
		// get ３６協定超過上限回数
		if (agreementSetObj.isPresent()) {
			monthLimit = agreementSetObj.get().getNumberTimesOverLimitType().value;
		} else {
			monthLimit = TimeOverLimitTypeImport.ZERO_TIMES.value;
		}
		ExportData data = this.outputProcess(companyId, query.getSetItemsOutputCd(), fiscalYear, startYm, endYm, employees, printFormat, query.getBreakPage(), excludeEmp, monthLimit, baseMonth, baseDate);
		val dataSetter = context.getDataSetter();
		List<String> employeeError = data.getEmployeeError();
		if (!employeeError.isEmpty()) {
			dataSetter.setData("messageId", "Msg_1344");
			dataSetter.setData("totalEmpErr", employeeError.size());
			for (int i = 0; i < employeeError.size(); i++) {
				dataSetter.setData("empErr" + i, employeeError.get(i));
			}
		}
		// invoke generator
		this.generator.generate(context.getGeneratorContext(), data);
	}

	/**
	 * Get startYm, endYm
	 * @param startYm output
	 * @param endYm output
	 */
	private YearMonth getStartYearMonth(Optional<AgreementOperationSettingImport> agreementSetObj, Year fiscalYear) {
		String month = "01";
		// 「36協定運用設定」．起算月から年度の期間を求める
		if (agreementSetObj.isPresent()) {
			switch (agreementSetObj.get().getStartingMonth()) {
			case JANUARY:
				month = "01";
				break;
			case FEBRUARY:
				month = "02";
				break;
			case MARCH:
				month = "03";
				break;
			case APRIL:
				month = "04";
				break;
			case MAY:
				month = "05";
				break;
			case JUNE:
				month = "06";
				break;
			case JULY:
				month = "07";
				break;
			case AUGUST:
				month = "08";
				break;
			case SEPTEMBER:
				month = "09";
				break;
			case OCTOBER:
				month = "10";
				break;
			case NOVEMBER:
				month = "11";
				break;
			case DECEMBER:
				month = "12";
				break;
			}
		}
		StringBuilder ym = new StringBuilder();
		ym.append(fiscalYear.toString());
		ym.append('/');
		ym.append(month);
		return YearMonth.parse(ym.toString(), DateTimeFormatter.ofPattern("uuuu/MM"));
	}
	
	/**
	 * Create data export*/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ExportData outputProcess(String cid, String setItemsOutputCd, Year fiscalYear, YearMonth startYm, YearMonth endYm, List<Employee> employees, 
			PrintFormat printFormat, int breakPage, ExcludeEmp excludeEmp, Integer monthLimit, Integer baseMonth, GeneralDate baseDate) {
		ExportData exportData = new ExportData();
		YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(nts.arc.time.YearMonth.of(startYm.getYear(), startYm.getMonthValue()), nts.arc.time.YearMonth.of(endYm.getYear(), endYm.getMonthValue()));
		// ドメインモデル「年間勤務表（36チェックリスト）の出力項目設定」を取得する
		SetOutItemsWoSc setOutItemsWoSc = setOutItemsWoScRepository.getSetOutItemsWoScById(cid, setItemsOutputCd).get();
		// 帳表出力前チェックをする
		this.checkBeforOutput(startYm, endYm, employees, setOutItemsWoSc, printFormat);
		// ユーザ固有情報「年間勤務表（36チェックリスト）」を更新する -> client
		exportData.setPageBreak(EnumAdaptor.valueOf(breakPage, PageBreakIndicator.class));
		LocalDate endYmd = LocalDate.of(endYm.getYear(), endYm.getMonthValue(), 1).plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);
		List<String> employeeIds = employees.stream().map(m -> m.getEmployeeId()).collect(Collectors.toList());
		
		// init map employees data
		// <<Public>> 社員の情報を取得する
		exportData.setEmployees(this.getEmployeeInfo(employees, employeeIds, endYmd));
		HeaderData header = new HeaderData();
		header.setPrintFormat(printFormat);
		header.setMonthsInTotalDisplay(setOutItemsWoSc.getMonthsInTotalDisplay());
		header.setTitle(companyAdapter.getCurrentCompany().map(m -> m.getCompanyName()).orElse(""));
		// B1_1 + B1_2
		String periodStr = startYm.until(endYm, ChronoUnit.MONTHS) == 0? startYm.format(DateTimeFormatter.ofPattern(YM_FORMATER)) : startYm.format(DateTimeFormatter.ofPattern(YM_FORMATER)) + "～" + endYm.format(DateTimeFormatter.ofPattern(YM_FORMATER));
		header.setPeriod(TextResource.localize("KWR008_41") + " " + periodStr);
		List<ItemOutTblBook> listItemOut = setOutItemsWoSc.getListItemOutTblBook().stream() .filter(item -> item.isUseClassification()) // ドメインモデル「帳表に出力する項目．使用区分」をチェックする
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
		exportData.setExportItems(listItemOut.stream().map(m -> new ExportItem(m.getCd().v(), m.getHeadingName().v())).collect(Collectors.toList()));
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
		if(setOutItemsWoSc.getMonthsInTotalDisplay().isPresent()) {
			// set C2_3, C2_5
			header.setMonthPeriodLabels(this.createMonthPeriodLabels(startYmClone, endYm, setOutItemsWoSc.getMonthsInTotalDisplay().get()));
		}
		// set C1_2
		header.setMonths(this.createMonthLabels(startYmClone, endYmClone));
		exportData.setHeader(header);

		// 「年間勤務表（36チェックリスト）の出力項目設定」を取得する
		if (PrintFormat.AGREEMENT_36.equals(printFormat)) {
			// 36協定対象外者のチェック
			employeeIds = this.checkExcludeEmp36Agreement(excludeEmp, employeeIds, endYmd);
			// アルゴリズム「年間勤務表の作成」を実行する
			PeriodAtrOfAgreement periodAtr = null;
			if (setOutItemsWoSc.getMonthsInTotalDisplay().isPresent() && setOutItemsWoSc.isMultiMonthDisplay() && setOutItemsWoSc.getTotalAverageDisplay() == TotalAverageDisplay.TOTAL) {
				if (MonthsInTotalDisplay.TWO_MONTH.equals(setOutItemsWoSc.getMonthsInTotalDisplay().get())) {
					periodAtr = PeriodAtrOfAgreement.TWO_MONTHS;
				} else if (MonthsInTotalDisplay.THREE_MONTH.equals(setOutItemsWoSc.getMonthsInTotalDisplay().get())) {
					periodAtr = PeriodAtrOfAgreement.THREE_MONTHS;
				}
			}

			this.createAnnualWorkSchedule36Agreement(cid, exportData, yearMonthPeriod, employeeIds, listItemOut, fiscalYear, startYm, setOutItemsWoSc.isOutNumExceedTime36Agr(), periodAtr, monthLimit, baseMonth, setOutItemsWoSc, baseDate);
		} else {
			// 年間勤務表(勤怠チェックリスト)を作成
			this.createAnnualWorkScheduleAttendance(exportData, yearMonthPeriod, employeeIds, listItemOut, startYm, baseDate);
		}
		// 社員を並び替える
		this.sortEmployees(exportData, endYmd);
		// 出力するデータ件数をチェックする
		if (!exportData.hasDataItemOutput()) {
			throw new BusinessException("Msg_885");
		}
		return exportData;
	}
	
	/**
	 * 年間勤務表の作成
	 */
	private void createAnnualWorkSchedule36Agreement(String cid, ExportData exportData, YearMonthPeriod yearMonthPeriod, List<String> employeeIds, List<ItemOutTblBook> listItemOut, Year fiscalYear, YearMonth startYm, boolean isOutNumExceed, PeriodAtrOfAgreement periodAtr, Integer monthLimit, Integer baseMonth, SetOutItemsWoSc setOutItemsWoSc, GeneralDate baseDate) {
		List<ItemOutTblBook> outputAgreementTime36 = listItemOut.stream().filter(m -> m.isItem36AgreementTime()).collect(Collectors.toList());
		String employeeIdLogin = AppContexts.user().employeeId();
		//ドメインモデル「36協定運用設定」を取得する
		Optional<AgreementOperationSetting> agreementOperationSetting = agreementOperationSettingRepository.find(cid);
		//年度を指定して36協定期間を取得 - get RequestList554
		/** TODO: 36協定時間対応により、コメントアウトされた */
		Optional<DatePeriod> period = Optional.empty();//GetAgreementPeriod.byYear(requireService.createRequire(),
//				new CacheCarrier(), cid, employeeIdLogin, GeneralDate.ymd(yearMonthPeriod.end().year(), yearMonthPeriod.end().month(), yearMonthPeriod.end().lastDateInMonth()), fiscalYear);
		if(!period.isPresent()) {
			throw new BusinessException("Msg_1513");
		}
		DatePeriod datePeriod = period.get(); 
		YearMonthPeriod yearMonthPeriodRQL554 = new YearMonthPeriod(datePeriod.start().yearMonth(),datePeriod.end().yearMonth());
		boolean average = false;
		//並び順 = 2  & 使用区分 = 使用する(Thứ tự =2 and phần sử dụng = sử dụng)
		if(setOutItemsWoSc.getPrintForm() == AnnualWorkSheetPrintingForm.AGREEMENT_CHECK_36 && setOutItemsWoSc.isMultiMonthDisplay() && setOutItemsWoSc.getTotalAverageDisplay() == TotalAverageDisplay.AVERAGE) {
			List<AgreMaxAverageTime> listAgreMaxAverageTimeImport =  this.createMonthlyAverage(cid, employeeIds.get(0), datePeriod.end(), nts.arc.time.YearMonth.of(fiscalYear.v(), baseMonth));
			List<String> periodLable = new ArrayList<>();
			//sort by start date
			for (int i=0; i< listAgreMaxAverageTimeImport.size();i++) {
				for(int j = i+1; j<listAgreMaxAverageTimeImport.size();j++) {
					if(listAgreMaxAverageTimeImport.get(j).getPeriod().start().greaterThan(listAgreMaxAverageTimeImport.get(i).getPeriod().start())) {
						val tg = listAgreMaxAverageTimeImport.get(i);
						listAgreMaxAverageTimeImport.set(i, listAgreMaxAverageTimeImport.get(j));
						listAgreMaxAverageTimeImport.set(j, tg);
					}
				}
			}
			for (AgreMaxAverageTime agreMaxAverage: listAgreMaxAverageTimeImport) {
				periodLable.add(agreMaxAverage.getPeriod().start().month() + "～" +  agreMaxAverage.getPeriod().end().month());
			}
			HeaderData headerData = exportData.getHeader();
			headerData.setMonthPeriodLabels(periodLable);
			headerData.setMaximumAgreementTime(true);
			exportData.setHeader(headerData);
			average = true;
		}
		
		GeneralDate criteria = GeneralDate.ymd(fiscalYear.v(), 12, 31);
        Month startMonth = new Month(startYm.getMonth().getValue());
        List<PeriodAtrOfAgreement> periodAtrs = new ArrayList<>();
        periodAtrs.add(PeriodAtrOfAgreement.ONE_MONTH);
        periodAtrs.add(PeriodAtrOfAgreement.ONE_YEAR);
        if (PeriodAtrOfAgreement.TWO_MONTHS.equals(periodAtr) || PeriodAtrOfAgreement.THREE_MONTHS.equals(periodAtr)) {
            periodAtrs.add(periodAtr);
        }

        // パラメータ「超過月数を出力する」をチェックする
        Map<String, Integer> monthsExceededAll = new HashMap<>();
        if (isOutNumExceed) {
            // 年間超過回数の取得
            // RequestList458
            monthsExceededAll = getExcessTimesYearAdapter.algorithm(employeeIds, fiscalYear);
        }
        
        EmpAffInfoExport EmpAffInfoExport = workRecordExport.getAffiliationPeriod(employeeIds, yearMonthPeriod, GeneralDate.today());
		Map<String, YearMonthPeriod> employees = new HashMap<>();
		for (AffiliationStatus emp : EmpAffInfoExport.getAffiliationStatus()) {
			nts.arc.time.YearMonth start = emp.getPeriodInformation().get(0).getYearMonthPeriod().start();
			nts.arc.time.YearMonth end = emp.getPeriodInformation().get(0).getYearMonthPeriod().end();
			for (PeriodInformation infor : emp.getPeriodInformation()) {
				if(infor.getYearMonthPeriod().start().lessThan(start)) {
					start = infor.getYearMonthPeriod().start();
				}
				if(infor.getYearMonthPeriod().end().lessThan(end)) {
					end = infor.getYearMonthPeriod().end();
				}
			}
			employees.put(emp.getEmployeeID(), new YearMonthPeriod(start, end));
		}
		
		// RequestList453
        // 36協定時間を取得する
		/** TODO: 36協定時間対応により、コメントアウトされた */
        Map<String, List<AgreementTimeByEmpImport>> agreementTimeAll = new HashMap<>();
//                agreementTimeByPeriodAdapter.algorithmImprove(cid, employeeIds, criteria, startMonth, fiscalYear, periodAtrs, employees)
//                        .stream().collect(Collectors.groupingBy(AgreementTimeByEmpImport::getEmployeeId));
		
		for (String empId : employeeIds) {
			EmployeeData empData = exportData.getEmployees().get(empId);
			Map<String, AnnualWorkScheduleData> annualWorkScheduleData = new HashMap<>();
			//sort by order -- 並び順をチェック
			if(outputAgreementTime36.size()>1) {
				if(outputAgreementTime36.get(0).getSortBy() > outputAgreementTime36.get(1).getSortBy()) {
					ItemOutTblBook tg = outputAgreementTime36.get(0);
					outputAgreementTime36.set(0, outputAgreementTime36.get(1));
					outputAgreementTime36.set(1, tg);
				}
			}
			Integer monthsExceeded = 0;
            if (monthsExceededAll.containsKey(empId)) {
                monthsExceeded = monthsExceededAll.get(empId);
            }
			for (ItemOutTblBook itemOutTblBook : outputAgreementTime36) {
				if(itemOutTblBook.getSortBy() == 1 && itemOutTblBook.isUseClassification()) {
					//並び順 = 1  & 使用区分 = 使用する(thứ tự = 1 and phần sử dụng =  使用する)
					annualWorkScheduleData.putAll(this.create36AgreementTime(agreementTimeAll.get(empId), monthsExceeded,
							itemOutTblBook, startYm, periodAtr, monthLimit,
	                        exportData.getHeader() == null ? new ArrayList<>()
	                                : (exportData.getHeader().getMonthPeriodLabels() == null ? new ArrayList<>() : exportData.getHeader().getMonthPeriodLabels())));
					empData.setAnnualWorkSchedule(annualWorkScheduleData);
				}else if(itemOutTblBook.getSortBy() == 2 && itemOutTblBook.isUseClassification()) {
					annualWorkScheduleData.putAll(this.create36MaximumAgreementTimeForOneMonth(cid, employees.get(empId), empId, itemOutTblBook, fiscalYear, startYm, average, periodAtr, monthLimit, exportData.getHeader() == null ? new ArrayList<>(): (exportData.getHeader().getMonthPeriodLabels()== null? new ArrayList<>(): exportData.getHeader().getMonthPeriodLabels()), datePeriod.end(), baseMonth));
					empData.setAnnualWorkSchedule(annualWorkScheduleData);
				}
			}
		}
		// アルゴリズム「任意項目の作成」を実行する
		this.createOptionalItems(exportData, yearMonthPeriod, employeeIds, listItemOut.stream().filter(item -> !item.isItem36AgreementTime()).collect(Collectors.toList()), startYm, baseDate);
		// 対象の社員IDをエラーリストに格納する
		exportData.storeEmployeeError();
	}
	
	/**
	 * 36協定明細項目の作成 
	 * @param yearMonthPeriod 対象期間
	 * @param employeeId 社員ID
	 * @param outNumExceedTime36Agr 超過月数出力するか
	 *            
	 */
	private Map<String, AnnualWorkScheduleData> create36AgreementTime(
			List<AgreementTimeByEmpImport> agreementTimes, Integer monthsExceeded,
			ItemOutTblBook outputAgreementTime36, YearMonth startYm,
			PeriodAtrOfAgreement periodAtr, Integer monthLimit, List<String> header) {
		// RequestList453
		// 36協定時間を取得する
		// 明細用
		List<AgreementTimeByPeriodImport> listAgreementTimeByMonth = agreementTimes.stream()
				.filter(x -> PeriodAtrOfAgreement.ONE_MONTH.equals(x.getPeriodAtr()))
				.map(AgreementTimeByEmpImport::getAgreementTime).collect(Collectors.toList());
		// 年間合計用
		List<AgreementTimeByPeriodImport> listAgreementTimeByYear = agreementTimes.stream()
				.filter(x -> PeriodAtrOfAgreement.ONE_YEAR.equals(x.getPeriodAtr()))
				.map(AgreementTimeByEmpImport::getAgreementTime).collect(Collectors.toList());
		
		if (listAgreementTimeByMonth.isEmpty())
			return new HashMap<>();

		// パラメータ「表示形式」をチェックする
		List<AgreementTimeByPeriodImport> listExcesMonths = new ArrayList<>();
		if (PeriodAtrOfAgreement.TWO_MONTHS.equals(periodAtr) || PeriodAtrOfAgreement.THREE_MONTHS.equals(periodAtr)) {
			listExcesMonths = agreementTimes.stream()
					.filter(x -> periodAtr.equals(x.getPeriodAtr()))
					.map(AgreementTimeByEmpImport::getAgreementTime).collect(Collectors.toList());
		}

		Map<String, AnnualWorkScheduleData> data = new HashMap<>();
		// アルゴリズム「月平均の算出」を実行する
		data.put(outputAgreementTime36.getCd().v(),
				AnnualWorkScheduleData.fromAgreementTimeList(outputAgreementTime36, listAgreementTimeByMonth,
						listAgreementTimeByYear, listExcesMonths, startYm, monthsExceeded, monthLimit, periodAtr, header, false)
						.calc(false));
		return data;
	}
	
	/**
	 * 1ヶ月の36協定上限時間を取得する
	 * @param yearMonthPeriod 対象期間
	 * @param employeeId 社員ID
	 * @param outNumExceedTime36Agr 超過月数出力するか
	 *            
	 */
	private Map<String, AnnualWorkScheduleData> create36MaximumAgreementTimeForOneMonth(String cid, YearMonthPeriod yearMonthPeriodRQL554,
			String employeeId, ItemOutTblBook outputAgreementTime36, Year fiscalYear, YearMonth startYm, boolean average,
			PeriodAtrOfAgreement periodAtr, Integer monthLimit, List<String> header, GeneralDate endDate, Integer baseMonth) {
		
		List<AgreementTimeByPeriodImport> listAgreementTimeByMonth = new ArrayList<>();
		//requestList 548 
		/** TODO: 36協定時間対応により、コメントアウトされた */
//		List<AgreMaxTimeMonthOut> agreMaxTimeMonthOut = GetAgreTimeByPeriod.maxTime(requireService.createRequire(),
//				cid, employeeId, yearMonthPeriodRQL554);
		int sum = 0;
//		for (AgreMaxTimeMonthOut agreMax : agreMaxTimeMonthOut) {
//			AgreementTimeByPeriodImport oneMonth = new AgreementTimeByPeriodImport(agreMax.getYearMonth(), null, 
//					new AttendanceTimeYear(agreMax.getMaxTime().getAgreementTime().v()),
//					"", "", "", "", 
//					agreMax.getMaxTime().getStatus()==AgreMaxTimeStatusOfMonthly.NORMAL?AgreementTimeStatusOfMonthly.NORMAL:AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR);
//			listAgreementTimeByMonth.add(oneMonth);
//			sum += agreMax.getMaxTime().getAgreementTime().v(); 
//		}
		List<AgreementTimeByPeriodImport> listAgreementTimeByYear = new ArrayList<>();
		AgreementTimeByPeriodImport byYear = null;
		if(sum > 0) {
			byYear = new AgreementTimeByPeriodImport(null, null, new AttendanceTimeYear(sum), "", "", "", "", AgreementTimeStatusOfMonthly.NORMAL);
		}else {
			byYear = new AgreementTimeByPeriodImport(null, null, new AttendanceTimeYear(0), "", "", "", "", AgreementTimeStatusOfMonthly.NORMAL);
		}
		listAgreementTimeByYear.add(byYear);
		if (listAgreementTimeByMonth.isEmpty())
			return new HashMap<>();
		// 複数月表示をチェック
		Integer monthsExceeded = 0;
		
		List<AgreementTimeByPeriodImport> listAgreMaxAverageTime = new ArrayList<>();
		if(average) {
			//暦上の年月変換処理
			nts.arc.time.YearMonth specifiedYearMonth = this.converYearMonth(yearMonthPeriodRQL554, baseMonth);
			//36協定上限時間の複数月平均を取得する
			List<AgreMaxAverageTime> listAgreMaxAverageTimeImport =  this.createMonthlyAverage(cid, employeeId, endDate, specifiedYearMonth);
			for (AgreMaxAverageTime agreMaxAverage: listAgreMaxAverageTimeImport) {
				AgreementTimeByPeriodImport item = new AgreementTimeByPeriodImport(agreMaxAverage.getPeriod().start(), null, new AttendanceTimeYear(agreMaxAverage.getAverageTime().v()), null, null, null, null, 
						agreMaxAverage.getStatus()==AgreMaxTimeStatusOfMonthly.NORMAL?AgreementTimeStatusOfMonthly.NORMAL:AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR);
				listAgreMaxAverageTime.add(item);
			}
		}
		Map<String, AnnualWorkScheduleData> data = new HashMap<>();
		// アルゴリズム「月平均の算出」を実行する
		data.put(outputAgreementTime36.getCd().v(), 
				AnnualWorkScheduleData.fromAgreementTimeList(outputAgreementTime36, listAgreementTimeByMonth, listAgreementTimeByYear, listAgreMaxAverageTime, startYm, monthsExceeded, monthLimit, periodAtr, header, true)
						.calc(false));
		return data;
	}
	
	/**
	 * 36協定上限時間の複数月平均を取得する
	 * */
	private List<AgreMaxAverageTime> createMonthlyAverage(String companyId, String employeeId, GeneralDate criteria, nts.arc.time.YearMonth yearMonth) {
		//get requestList547
		/** TODO: 36協定時間対応により、コメントアウトされた */
//		Optional<AgreMaxAverageTimeMulti> agreMaxAverageTimeMulti = GetAgreTimeByPeriod.maxAverageTimeMulti(
//				requireService.createRequire(), companyId, employeeId, criteria, yearMonth);
//		if(agreMaxAverageTimeMulti.isPresent()) {
//			return agreMaxAverageTimeMulti.get().getAverageTimeList();
//		}
		return new ArrayList<>();
	}
	
	/**
	 * 任意項目の作成
	 * @param yearMonthPeriod 対象期間
	 * @param employeeId 社員ID
	 * @param listItemOut 「36協定時間」以外の出力対象の項目設定（List）
	 */
	private void createOptionalItems(ExportData exportData, YearMonthPeriod yearMonthPeriod, List<String> employeeIds,
			List<ItemOutTblBook> listItemOut, YearMonth startYm, GeneralDate baseDate) {
		List<Integer> allItemIds = new ArrayList<>();
		for(ItemOutTblBook itemOut: listItemOut){
			List<Integer> itemIds = itemOut.getListOperationSetting().stream().map(os -> os.getAttendanceItemId()).collect(Collectors.toList());
			allItemIds.addAll(itemIds);
		}
		allItemIds = allItemIds.stream().distinct().collect(Collectors.toList());
		
		EmpAffInfoExport EmpAffInfoExport = workRecordExport.getAffiliationPeriod(employeeIds, yearMonthPeriod, baseDate);
		Map<String, YearMonthPeriod> employees = new HashMap<>();
		for (AffiliationStatus emp : EmpAffInfoExport.getAffiliationStatus()) {
			nts.arc.time.YearMonth start = emp.getPeriodInformation().get(0).getYearMonthPeriod().start();
			nts.arc.time.YearMonth end = emp.getPeriodInformation().get(0).getYearMonthPeriod().end();
			for (PeriodInformation infor : emp.getPeriodInformation()) {
				if(infor.getYearMonthPeriod().start().lessThan(start)) {
					start = infor.getYearMonthPeriod().start();
				}
				if(infor.getYearMonthPeriod().end().lessThan(end)) {
					end = infor.getYearMonthPeriod().end();
				}
			}
			employees.put(emp.getEmployeeID(), new YearMonthPeriod(start, end));
		}
		// アルゴリズム「対象期間の月次データの取得」を実行する
		List<MonthlyAttendanceResultImport> allMonthlyAtt = monthlyAttendanceItemAdapter.getMonthlyValueOfParallel(employees, allItemIds);
		listItemOut.forEach(itemOut -> {
			// アルゴリズム「出力項目の値の算出」を実行する
			Map<String, AnnualWorkScheduleData> empData = this.createOptionalItem(allMonthlyAtt, employeeIds, itemOut, startYm);
			employeeIds.forEach(empId -> {
				AnnualWorkScheduleData data = empData.get(empId);
				exportData.getEmployees().get(empId).getAnnualWorkSchedule().put(itemOut.getCd().v(), data);
			});
		});
	}

	/**
	 * 帳表出力前チェックをする
	 */
	private void checkBeforOutput(YearMonth startYm, YearMonth endYm, List<Employee> employees, SetOutItemsWoSc setOutItemsWoSc, PrintFormat printFormat) {
		// 対象期間をチェックする
		if (startYm.until(endYm, ChronoUnit.MONTHS) + 1 > 12)
			throw new BusinessException("Msg_883");
		// 出力対象の社員をチェックする
		if (employees == null || employees.isEmpty())
			throw new BusinessException("Msg_884");
		// 出力項目をチェックする
		List<ItemOutTblBook> listItemOutTblBook = setOutItemsWoSc.getListItemOutTblBook().stream() .filter(x -> x.isUseClassification()).collect(Collectors.toList());
		if (PrintFormat.ATTENDANCE.equals(printFormat)) {
			// 印刷形式="勤怠チェックリスト"の場合
			listItemOutTblBook = listItemOutTblBook.stream().filter(x -> !x.isItem36AgreementTime()) .collect(Collectors.toList());
		}
		if (listItemOutTblBook.size() == 0) {
			throw new BusinessException("Msg_880");
		}
	}

	/**
	 * 社員の情報を取得する 
	 * @param employees data Employees from client
	 * @param employeeIds 対象社員ID（List）
	 * @param endYmd 対象期間の終了日
	 * @return
	 */
	private Map<String, EmployeeData> getEmployeeInfo(List<Employee> employees, List<String> employeeIds, LocalDate endYmd) {
		
		Map<String, String> empNameMap = new HashMap<>();
		for (Employee employee : employees) {
			empNameMap.put(employee.getEmployeeId(), employee.getName());
		}
		//employees.stream().collect(Collectors.toMap(Employee::getEmployeeId, Employee::getName));
		Map<String, EmployeeData> employeesData = new HashMap<>();
		employeeInformationAdapter.getEmployeeInfo(new EmployeeInformationQueryDtoImport(employeeIds,
				GeneralDate.localDate(endYmd), true, false, true, true, false, false)).forEach(emp -> {
					employeesData.put(emp.getEmployeeId(), EmployeeData.builder().employeeInfo(new EmployeeInfo(
							emp.getWorkplace() == null ? "" : emp.getWorkplace().getWorkplaceCode(),
							emp.getWorkplace() == null ? "マスタ未登録"
									: TextResource.localize("KWR008_50") + " " + emp.getWorkplace().getWorkplaceName(),
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
			List<EmployeeInformationImport> empInfoList = employeeInformationAdapter.getEmployeeInfo(new EmployeeInformationQueryDtoImport(employeeIds, endDate, false, false, true,false, false, false));
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
		Optional<JobTitleImport> jobTitleOtp = jobTitleAdapter.findByJobId(AppContexts.user().companyId(), emp.getPosition().getPositionId(), endDate);
		if (!jobTitleOtp.isPresent()) {
			return false;
		}
		return jobTitleOtp.get().isManager();
	}

	/**
	 * 出力項目の値の算出
	 */
	private Map<String, AnnualWorkScheduleData> createOptionalItem(List<MonthlyAttendanceResultImport> allMonthlyAtt, List<String> employeeIds, ItemOutTblBook itemOut, YearMonth startYm) {
		// アルゴリズム「月平均の算出」を実行する
		Map<String, AnnualWorkScheduleData> empData = new HashMap<>();
		employeeIds.forEach(empId -> {
			List<MonthlyAttendanceResultImport> listMonthly = allMonthlyAtt.stream().filter(x -> x.getEmployeeId().equals(empId)).collect(Collectors.toList());
			// アルゴリズム「月平均の算出」を実行する
			empData.put(empId, AnnualWorkScheduleData.fromMonthlyAttendanceList(itemOut, listMonthly, startYm).calc(true));
		});
		return empData;
	}

	/**
	 * 2・3ヶ月の36協定時間の作成
	 */
	private List<AgreementTimeByPeriodImport> create36AgreementFewMonth(String cid, String employeeId, Year fiscalYear, YearMonth startYm, PeriodAtrOfAgreement periodAtr) {
		// アルゴリズム「2・3ヶ月の36協定時間の作成」を実行する
		Closure closure = ClosureService.getClosureDataByEmployee(requireService.createRequire(), new CacheCarrier(), employeeId, GeneralDate.today());
		// 年度から集計期間を取得
		Optional<DatePeriod> datePeriod = getAgreementPeriodFromYearPub.algorithm(fiscalYear, closure);
		// ドメイン「３６協定運用設定」を取得する
		Month startMonth;
		startMonth = new Month(startYm.getMonthValue());
		// 基準日 = 「年度から集計期間を取得する」のOutputのenddate
		GeneralDate criteria = datePeriod.get().end();
		// 指定期間36協定時間の取得
		/** TODO: 36協定時間対応により、コメントアウトされた */
		return new ArrayList<>();
//		return agreementTimeByPeriodAdapter.algorithm(cid, employeeId, criteria, startMonth, fiscalYear, periodAtr);
	}

	/**
	 * 年間勤務表(勤怠チェックリスト)を作成
	 */
	private void createAnnualWorkScheduleAttendance(ExportData exportData, YearMonthPeriod yearMonthPeriod,
			List<String> employeeIds, List<ItemOutTblBook> listItemOut, YearMonth startYm, GeneralDate baseDate) {
		// アルゴリズム「任意項目の作成」を実行する
		this.createOptionalItems(exportData, yearMonthPeriod, employeeIds, listItemOut.stream().filter(item -> !item.isItem36AgreementTime()).collect(Collectors.toList()), startYm, baseDate);
		// 対象の社員IDをエラーリストに出力する
		exportData.storeEmployeeError();
	}

	/**
	 * C1_2
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
	 * Create C2_3, C2_5, C2_7
	 * @param startYm
	 * @param endYm
	 * @param outputAgreementTime
	 * @return
	 */
	private List<String> createMonthPeriodLabels(YearMonth startYm, YearMonth endYm,
			MonthsInTotalDisplay monthsInTotalDisplay) {
		int distances = 0;
		if (MonthsInTotalDisplay.TWO_MONTH.equals(monthsInTotalDisplay))
			distances = 2;
		else if (MonthsInTotalDisplay.THREE_MONTH.equals(monthsInTotalDisplay))
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
	
	/**
	 * 社員を並び替える(任意)
	 * @param employeeIds  List＜社員ID＞
	 * @param endYmd  対象期間の終了日
	 * @return
	 */
	private List<String> sortEmployees(List<String> employeeIds, LocalDate endYmd) {
		List<SortingConditionOrderImport> ordersImport = new ArrayList<>();
		ordersImport.add(new SortingConditionOrderImport(1, RegularSortingTypeImport.WORKPLACE));
		List<String> listEmp = employeeAdapter.sortEmployee(AppContexts.user().companyId(), employeeIds, ordersImport, GeneralDateTime.localDateTime(LocalDateTime.of(endYmd, LocalTime.of(0, 0))));
		return listEmp;
	}

	private void sortEmployees(ExportData exportData, LocalDate endYmd) {
		List<String> listEmpSorted = new ArrayList<>();
		List<String> listEmpIdOrgin = exportData.getEmployees().entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList());
		List<String> listEmp = this.sortEmployees(listEmpIdOrgin, endYmd);
		listEmpSorted.addAll(listEmp);
		listEmpSorted.removeAll(exportData.getEmployeeIdsError());
		exportData.setEmployeeIds(listEmpSorted);
	}
	
	/**
	 * アルゴリズム「暦上の年月変換処理」を実行
	 * */
	private nts.arc.time.YearMonth converYearMonth(YearMonthPeriod yearMonthPeriodRQL554, int baseMonth){
		if(baseMonth > yearMonthPeriodRQL554.end().month()) {
			return nts.arc.time.YearMonth.of(yearMonthPeriodRQL554.start().year(), baseMonth);
		}else {
			return nts.arc.time.YearMonth.of(yearMonthPeriodRQL554.end().year(), baseMonth);
		}
	}
}
