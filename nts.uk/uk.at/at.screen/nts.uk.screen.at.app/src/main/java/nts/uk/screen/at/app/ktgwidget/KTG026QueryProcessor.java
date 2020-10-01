package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.Year;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.sys.auth.dom.wkpmanager.dom.EmpBasicInfoImport;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.function.dom.adapter.standardtime.GetAgreementPeriodFromYearAdapter;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.closure.ClosureHistPeriod;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.closure.GetSpecifyPeriod;

import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTime;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTimeOfMngPeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetExcessTimesYear;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.ScheRecAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.sys.auth.dom.adapter.person.EmployeeBasicInforAuthImport;
import nts.uk.ctx.sys.auth.dom.wkpmanager.EmpInfoAdapter;
import nts.uk.screen.at.app.ktgwidget.find.dto.AgreementExcessInfoDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.AgreementTimeOfManagePeriodDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.EmployeesOvertimeDisplayDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.PresentClosingPeriodDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.YearAndEmpOtHoursDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.YearMonthOvertime;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class KTG026QueryProcessor {

	// The record dom require service
	@Inject
	private RecordDomRequireService requireService;

	// The agreement operation setting repository
	@Inject
	AgreementOperationSettingRepository agreementOperationSettingRepository;

	// The get specify period
	@Inject
	private GetSpecifyPeriod getSpecifyPeriod;

	// Get excess times year
	@Inject
	private GetExcessTimesYear getExcessTimesYear;

	// 36協定運用設定の取得
	@Inject
	private AgreementOperationSettingRepository agreementOpeSetRepo;

	// 管理期間の36協定時間を取得
	@Inject
	private GetAgreementTimeOfMngPeriod getAgreementTimeOfMngPeriod;

	// The employee info adapter
	@Inject
	private EmpInfoAdapter empInfoAdapter;

	@Inject
	private GetAgreementPeriodFromYearAdapter aggremnentPeriodFromYear;

	@Inject
	private GetExcessTimesYearAdapter getExcessTimesYearAdapter;

	/**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG026_時間外労働時間の表示(従業員用).アルゴリズム.起動する.起動する
	 * 
	 * @param employeeId
	 */
	public EmployeesOvertimeDisplayDto startScreenKtg026(String employeeId, Integer targetDate, Integer targetYear,
			int currentOrNextMonth) {
		// 従業員用の時間外時間表示
		EmployeesOvertimeDisplayDto result = EmployeesOvertimeDisplayDto.builder().build();

		val require = requireService.createRequire();
		CacheCarrier cacheCarrier = new CacheCarrier();
		// システム日付
		GeneralDate systemDate = GeneralDate.today();

		// 社員に対応する処理締めを取得する
		Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, employeeId, systemDate);

		// 従業員用の時間外時間表示．ログイン者の締めID＝取得したドメインモデル「締め」．締めID
		result.setClosureID(closure.getClosureId().value);

		YearMonth yearMonth = closure.getClosureMonth().getProcessingYm();

		// 指定した年月の締め期間を取得する
		List<ClosureHistPeriod> lstClosureHist = getSpecifyPeriod.getSpecifyPeriod(yearMonth);

		// 従業員用の時間外時間表示．当月の締め情報．処理年月＝取得したドメインモデル「締め」．当月
		// 従業員用の時間外時間表示．当月の締め情報．締め開始日＝取得した締め期間．開始日
		// 従業員用の時間外時間表示．当月の締め情報．締め終了日＝取得した締め期間．終了日
		if (!lstClosureHist.isEmpty()) {
			PresentClosingPeriodDto closingPeriod = PresentClosingPeriodDto.builder()
					.processingYm(closure.getClosureMonth().getProcessingYm().v())
					.closureStartDate(lstClosureHist.get(0).getPeriod().start())
					.closureEndDate(lstClosureHist.get(0).getPeriod().start()).build();
			result.setClosingPeriod(closingPeriod);
		}

		// アルゴリズム「年月を指定して、36協定期間の年度を取得する」を実行する
		Year year = this.getYearAgreementPeriod(closure.getClosureMonth().getProcessingYm());

		// 従業員用の時間外時間表示．当月含む年＝取得した年度
		result.setYearIncludeThisMonth(year.v());
		// 従業員用の時間外時間表示．表示する年＝取得した年度
		result.setDisplayYear(year.v());

		// INPUT．対象年をチェックする
		if (targetYear != null) { // INPUT．対象年 != NULL
			// 従業員用の時間外時間表示．表示する年＝INPUT．対象年
			result.setDisplayYear(targetYear);

		} else if (targetDate != null) { // INPUT．対象年 = NULL && INPUT．対象年月 != NULL
			// アルゴリズム「年月を指定して、36協定期間の年度を取得する」を実行する
			// 従業員用の時間外時間表示．表示する年＝取得した年度
			result.setDisplayYear(this.getYearAgreementPeriod(YearMonth.of(targetDate)).v());

		} else if (currentOrNextMonth == 2) { // INPUT．表示年月＝翌月表示
			Year yearIncludeNextMonth = this
					.getYearAgreementPeriod(YearMonth.of(result.getClosingPeriod().getProcessingYm()).addMonths(1));
			result.setYearIncludeNextMonth(yearIncludeNextMonth.v());
			result.setDisplayYear(yearIncludeNextMonth.v());
		}

		// 指定する年と指定する社員の時間外時間と超過回数の取得
		YearAndEmpOtHoursDto otHours = this.getYearAndEmployeeOTHours(employeeId, closure.getClosureId().value,
				new Year(result.getDisplayYear()), YearMonth.of(result.getClosingPeriod().getProcessingYm()));
		// 上長用の時間外時間表示．対象社員の各月の時間外時間＝取得した「年月ごとの時間外時間」
		result.setYmOvertimes(otHours.getOvertimeHours());

		// 上長用の時間外時間表示．対象社員の年間超過回数＝取得した「36協定超過情報」
		AgreementExcessInfoDto agreeInfo = AgreementExcessInfoDto.builder()
				.excessTimes(otHours.getAgreeInfo())
				.yearMonths(
						otHours.getOvertimeHours().stream().map(x -> x.getYearMonth()).collect(Collectors.toList()))
				.build();
		result.setAgreeInfo(agreeInfo);

		// 社員ID(List)から個人社員基本情報を取得
		List<EmpBasicInfoImport> empInfoLst = this.empInfoAdapter.getListPersonInfo(Arrays.asList(employeeId));

		// 上長用の時間外時間表示．対象社員の個人情報＝取得した「個人社員基本情報」
		if (!empInfoLst.isEmpty()) {
			EmpBasicInfoImport empInfo = empInfoLst.get(0);
			result.setEmpInfo(new EmployeeBasicInforAuthImport(empInfo.getPId(), empInfo.getNamePerson(),
					empInfo.getEntryDate(), empInfo.getGender(), empInfo.getBirthDay(), empInfo.getEmployeeId(),
					empInfo.getEmployeeCode(), empInfo.getRetiredDate()));
		}

		return result;
	}

	/**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG026_時間外労働時間の表示(従業員用).ユースケース.対象年の時間外労働時間を抽出する.対象年の時間外労働時間を抽出する
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG026_時間外労働時間の表示(従業員用).ユースケース.対象年を前年に切り替える.対象年を前年に切り替える
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG026_時間外労働時間の表示(従業員用).ユースケース.対象年を翌年に切り替える.対象年を翌年に切り替える
	 * 
	 * @param employeeId
	 * @param closingId
	 * @param targetYear
	 * @param processingDate
	 * @return 従業員用の時間外時間表示
	 */
	public EmployeesOvertimeDisplayDto extractOvertime(String employeeId, int closingId, Integer targetYear,
			int processingDate) {
		// 指定する年と指定する社員の時間外時間と超過回数の取得
		YearAndEmpOtHoursDto otHours = this.getYearAndEmployeeOTHours(employeeId, closingId, new Year(targetYear),
				YearMonth.of(processingDate));
		AgreementExcessInfoDto agreeInfo = AgreementExcessInfoDto.builder()
				.excessTimes(otHours.getAgreeInfo())
				.yearMonths(
						otHours.getOvertimeHours().stream().map(x -> x.getYearMonth()).collect(Collectors.toList()))
				.build();
		// 「従業員用の時間外時間表示」を更新する
		return EmployeesOvertimeDisplayDto.builder().ymOvertimes(otHours.getOvertimeHours()).agreeInfo(agreeInfo)
				.build();
	}

	/**
	 * 年月を指定して、36協定期間の年度を取得する
	 * 
	 * @param yearMonth 年月
	 * @return year 年度
	 */
	public Year getYearAgreementPeriod(YearMonth yearMonth) {
		// 会社ID
		String companyID = AppContexts.user().companyId();
		// 起算月を取得 Get the starting month
		AgreementOperationSetting agreeOpSet = agreementOperationSettingRepository.find(companyID).get();
		Year year = new Year(yearMonth.year());

		// パラメータ「年月」の月と起算月を比較 The month of the parameter "year-month" and the starting
		// month are compared
		if (yearMonth.month() < (agreeOpSet.getStartingMonth().value + 1)) { // パラメータ「年月」の月 ＜ 起算月 の場合
			// パラメータ「年月」の年を-1年する Make the year of the parameter "year-month" -1 year
			year = new Year(yearMonth.year() - 1);
		}

		return year;
	}

	/**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG026_時間外労働時間の表示(従業員用).アルゴリズム.指定する年と指定する社員の時間外時間と超過回数の取得.指定する年と指定する社員の時間外時間と超過回数の取得
	 * @param employeeId 社員ID
	 * @param closingId 締めID
	 * @param targetYear 対象年
	 * @param processingDate 当月の年月
	 * @return year and employee overtime hours
	 */
	public YearAndEmpOtHoursDto getYearAndEmployeeOTHours(String employeeId, int closingId, Year targetYear, YearMonth processingDate) {
		// 会社ID
		String companyId = AppContexts.user().companyId();
		// 年月ごとの時間外時間
		List<YearMonthOvertime> ymOvertimes = new ArrayList<YearMonthOvertime>();
		
		val require = requireService.createRequire();
		CacheCarrier cacheCarrier = new CacheCarrier();
		GeneralDate systemDate = GeneralDate.today();
		// [No.458]年間超過回数の取得
		// OUTPUT．「36協定超過情報」＝取得した「36協定超過情報」
		int agreeInfo = getExcessTimesYearAdapter.algorithm(employeeId, targetYear);
		
		// 年度から36集計期間を計算
		// ○社員に対応する処理締めを取得する (Lấy closure xử lý ứng với employee)
		Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, employeeId, systemDate);
		
		// ○３６協定運用設定を取得 (Lấy 36AgreementOperationSetting)
		Optional<AgreementOperationSetting> agreeOperation = agreementOpeSetRepo.find(companyId);
		
		// ○年度から集計期間を取得 (Lấy thời gian tính toán từ niên độ )
		Optional<DatePeriod> datePeriod = aggremnentPeriodFromYear.algorithm(targetYear, closure);
		
		if (!datePeriod.isPresent()) {
			return YearAndEmpOtHoursDto.builder().build();
		}
		
		YearMonth loopYM = datePeriod.get().start().yearMonth();
		int month = loopYM.month();
		// 取得した期間の年月をループする
		for (int i = month; i <= 12; i++) {
			int ym = loopYM.v();
			// ループする年月とINPUT．当月の年月の大小比較
			if (processingDate.addMonths(2).lessThanOrEqualTo(loopYM)) { // [INPUT．当月の年月．AddMonth(2)<=ループする年月]がtrue
				break;
				
			} else if (processingDate.lessThanOrEqualTo(loopYM)) { // [INPUT．当月の年月<=ループする年月]がtrue
				// o【NO.333】36協定時間の取得
				AgreementTimeOfManagePeriod agreementTimeDetail = GetAgreementTime.get(require, employeeId, loopYM, new ArrayList<>(), loopYM.lastGeneralDate(), ScheRecAtr.SCHEDULE);
				ymOvertimes.add(YearMonthOvertime.builder().yearMonth(ym).agreeTime(AgreementTimeOfManagePeriodDto.from(agreementTimeDetail)).build());
			} else { // [INPUT．当月の年月<=ループする年月]がfalse
				// [NO.612]年月期間を指定して管理期間の36協定時間を取得する
				List<AgreementTimeOfManagePeriod> agreementTimeToppageLst = 
						GetAgreementTimeOfMngPeriod.get(require, Arrays.asList(employeeId), new YearMonthPeriod(datePeriod.get().start().yearMonth(), datePeriod.get().end().yearMonth()));
				
				agreementTimeToppageLst.stream().forEach(item -> 
					ymOvertimes.add(YearMonthOvertime.builder().yearMonth(ym).agreeTime(AgreementTimeOfManagePeriodDto.from(item)).build())
				);
			}
			
			loopYM = loopYM.nextMonth();
		}
		// OUTPUT．年月ごとの時間外時間をソートする
		ymOvertimes.stream().sorted((a, b) -> b.getYearMonth() - a.getYearMonth());
		
		return YearAndEmpOtHoursDto.builder().agreeInfo(agreeInfo).overtimeHours(ymOvertimes).build();
		
	}
}
