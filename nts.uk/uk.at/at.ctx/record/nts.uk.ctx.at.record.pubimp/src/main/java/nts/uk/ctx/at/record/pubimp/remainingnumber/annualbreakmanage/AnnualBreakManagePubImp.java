package nts.uk.ctx.at.record.pubimp.remainingnumber.annualbreakmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnLeaRemNumWithinPeriodProc;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveInfo;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.pub.remainingnumber.annualbreakmanage.AnnualBreakManageExport;
import nts.uk.ctx.at.record.pub.remainingnumber.annualbreakmanage.AnnualBreakManagePub;
import nts.uk.ctx.at.record.pub.remainingnumber.annualbreakmanage.YearlyHolidaysTimeRemainingExport;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainingHistory;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.GetNextAnnualLeaveGrant;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.GetNextAnnualLeaveGrantProcKdm002;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AnnualBreakManagePubImp implements AnnualBreakManagePub {
	@Inject
	AnnualLeaveTimeRemainHistRepository annualLeaveTimeRemainHistRepository;

	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepository;

	@Inject
	private EmployeeRecordAdapter pmployeeRecordAdapter;

	/** 年休付与テーブル設定 */
	@Inject
	private YearHolidayRepository yearHolidayRepo;

	@Inject
	private AnnLeaGrantRemDataRepository annLeaGrantRemDataRepo;

	@Inject
	private AnnLeaGrantRemDataRepository grantDataRep;
	@Inject
	private RecordDomRequireService requireService;

	@Override
	public List<AnnualBreakManageExport> getEmployeeId(List<String> employeeId, GeneralDate startDate,
			GeneralDate endDate) {
		List<AnnualBreakManageExport> annualBreakManageExport = new ArrayList<>();
		Map<String, List<NextAnnualLeaveGrant>> nextAnnualLeaveGrantMap = calculateNextHolidayGrantImpr(employeeId, new DatePeriod(startDate.addDays(-1), endDate.addDays(-1)));
		List<String> noNextAnnualLeaveGrantEmp = new ArrayList<>();
		for (String emp : employeeId) {
			List<NextAnnualLeaveGrant> nextAnnualLeaveGrant = nextAnnualLeaveGrantMap.get(emp);
			// 「年休付与がある社員IDList」に処理中の社員IDを追加
			if (!nextAnnualLeaveGrant.isEmpty() ) {
				annualBreakManageExport.add(new AnnualBreakManageExport(emp));
			}else{
//				List<AnnualLeaveGrantRemainingData> listAnnLeaRemData = grantDataRep.findInDate(emp, startDate, endDate);
//				if(!listAnnLeaRemData.isEmpty()){
//					annualBreakManageExport.add(new AnnualBreakManageExport(emp));
//				}
				noNextAnnualLeaveGrantEmp.add(emp);
			}
		}
		if (!noNextAnnualLeaveGrantEmp.isEmpty()) {
			Map<String, List<AnnualLeaveGrantRemainingData>> annLeaRemDataMap = grantDataRep.findInDate(noNextAnnualLeaveGrantEmp, startDate, endDate);
			for (String sid : noNextAnnualLeaveGrantEmp) {
				List<AnnualLeaveGrantRemainingData> listAnnLeaRemData = annLeaRemDataMap.get(sid);
				if (listAnnLeaRemData != null && !listAnnLeaRemData.isEmpty()) {
					annualBreakManageExport.add(new AnnualBreakManageExport(sid));
				}
			}
		}
		return annualBreakManageExport;
	}

	@Override
	public List<YearlyHolidaysTimeRemainingExport> getYearHolidayTimeAnnualRemaining(String employeeId,
			GeneralDate confirmDay, GeneralDate holidayGrantStart, GeneralDate holidayGrantEnd) {
		val require = requireService.createRequire();
		val cacheCarrier = new CacheCarrier();

		List<YearlyHolidaysTimeRemainingExport> yearlyHolidaysTimeRemainingExport = new ArrayList<>();
		// 全締めの当月と期間を取得する
		Optional<GeneralDate> startDate = GetClosureStartForEmployee.algorithm(require, cacheCarrier, employeeId);
		if (!startDate.isPresent()) {
			return yearlyHolidaysTimeRemainingExport;
		}

		/*// 指定日年の1/1を計算
		GeneralDate designatedStartDate = GeneralDate.ymd(confirmDay.year(), 1, 1);

		// 計算期間．終了日←パラメータ「指定日」
		GeneralDate designatedEndDate = confirmDay;
		*/
		//計算期間を作成
		DatePeriod designatedPeriod = new DatePeriod(holidayGrantStart, holidayGrantEnd);

		// ログインしている会社ID　取得
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = loginUserContext.companyId();

		// 期間中の年休残数を取得
		Optional<AggrResultOfAnnualLeave> aggrResultOfAnnualLeave =
				GetAnnLeaRemNumWithinPeriodProc.
				algorithm(require, cacheCarrier, companyId,
						employeeId,
						new DatePeriod(startDate.get(), confirmDay),
						InterimRemainMngMode.OTHER,
						designatedPeriod.end(),
						false,
						false,
						Optional.of(false),
						Optional.empty(),
						Optional.empty(),
						Optional.empty());
		if (aggrResultOfAnnualLeave.isPresent()){
			//締め開始日以前に付与された年休残数を取得する
			yearlyHolidaysTimeRemainingExport = getNumberOfAnnualHolidayGrantedBeforeCloseDate(employeeId, companyId, startDate.get(), designatedPeriod.start(), designatedPeriod.end());
			// 取得した年休の集計結果．年休情報(付与時点)でループ
			Optional<List<AnnualLeaveInfo>> optListAnnus = aggrResultOfAnnualLeave.get().getAsOfGrant();
			if (optListAnnus.isPresent() && !optListAnnus.get().isEmpty()) {
				for (AnnualLeaveInfo annualLeaveInfoe : optListAnnus.get()) {
					// 「年休の集計結果」で付与された年月日をチェック
				    // 計算期間．開始日<=年休情報．年月日<=計算期間．終了日
					if (designatedPeriod.start().beforeOrEquals(annualLeaveInfoe.getYmd()) && designatedPeriod.end().afterOrEquals(annualLeaveInfoe.getYmd())) {
						YearlyHolidaysTimeRemainingExport yhtre =
								new YearlyHolidaysTimeRemainingExport(annualLeaveInfoe.getYmd(),
										null,
										annualLeaveInfoe.getRemainingNumber().getAnnualLeaveWithMinus()
												.getRemainingNumberInfo().getRemainingNumber().getTotalRemainingDays().v());
						yearlyHolidaysTimeRemainingExport.add(yhtre );
					}
				}
			}

			// List<指定日時点の年休残数>の年休残数を全て更新
			for (YearlyHolidaysTimeRemainingExport yyearlyHolidaysTimeRemainingExport : yearlyHolidaysTimeRemainingExport) {
				yyearlyHolidaysTimeRemainingExport.setAnnualRemaining(aggrResultOfAnnualLeave.get().getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus()
							.getRemainingNumberInfo().getRemainingNumber().getTotalRemainingDays().v());
			}

		}

		return yearlyHolidaysTimeRemainingExport;
	}

	@Override
	public List<NextAnnualLeaveGrant> calculateNextHolidayGrant(String employeeId, DatePeriod time) {
		val require = requireService.createRequire();
		val cacheCarrier = new CacheCarrier();

		List<NextAnnualLeaveGrant> nextAnnualLeaveGrant = new ArrayList<>();
		// ○Imported(就業)「社員」を取得する
		EmployeeRecordImport employeeRecordImport = pmployeeRecordAdapter.getPersonInfor(employeeId);
		// ○パラメータ「期間」をチェック
			Optional<GeneralDate> start_date = Optional.empty();
			if (time == null) {
				start_date = GetClosureStartForEmployee.algorithm(require, cacheCarrier, employeeId);
			}

			// ドメインモデル「年休付与テーブル設定」を取得する (Lấy domain 「年休付与テーブル設定」)
			// ログインしている会社ID　取得
			LoginUserContext loginUserContext = AppContexts.user();
			String companyId = loginUserContext.companyId();
			Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfo = annLeaEmpBasicInfoRepository.get(employeeId);
			if (!annualLeaveEmpBasicInfo.isPresent()) {
				return nextAnnualLeaveGrant;
			}
			val grantHdTblSetOpt = this.yearHolidayRepo.findByCode(
					companyId, annualLeaveEmpBasicInfo.get().getGrantRule().getGrantTableCode().toString() );
			if (!grantHdTblSetOpt.isPresent()){
				return nextAnnualLeaveGrant;
			}

			GeneralDate date = null;
			System.out.println("employeeRecordImport1: " + employeeRecordImport.getEntryDate());
			System.out.println("employeeRecordImport2: " + employeeRecordImport.getEmployeeId());

			System.out.println("employeeRecordImport3: " + date);

			//○次回年休付与を取得する
			nextAnnualLeaveGrant = GetNextAnnualLeaveGrant
					.algorithm(require, cacheCarrier, companyId,
							annualLeaveEmpBasicInfo.get().getGrantRule().getGrantTableCode().toString(),
							employeeRecordImport.getEntryDate(),
							annualLeaveEmpBasicInfo.get().getGrantRule().getGrantStandardDate(),
							time == null ? new DatePeriod(start_date.get().addDays(1), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")) : time,
							time == null ? true: false);
		return nextAnnualLeaveGrant;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Map<String, List<NextAnnualLeaveGrant>> calculateNextHolidayGrantImpr(List<String> employeeIds,
			DatePeriod time) {
		val require = requireService.createRequire();

		String companyId = AppContexts.user().companyId();
		Map<String, GeneralDate> employeeRecordImportMap = pmployeeRecordAdapter.getPersonInfor(employeeIds).stream()
				.collect(Collectors.toMap(EmployeeRecordImport::getEmployeeId, EmployeeRecordImport::getEntryDate));
		Map<String, AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoMap = annLeaEmpBasicInfoRepository
				.getList(employeeIds).stream()
				.collect(Collectors.toMap(AnnualLeaveEmpBasicInfo::getEmployeeId, Function.identity()));
		Map<String, List<NextAnnualLeaveGrant>> nextAnnualLeaveGrantMap = GetNextAnnualLeaveGrantProcKdm002
				.algorithm(require, companyId, employeeIds,
							annualLeaveEmpBasicInfoMap, employeeRecordImportMap, time, false);
		return nextAnnualLeaveGrantMap;
	}

	@Override
	public List<YearlyHolidaysTimeRemainingExport> getNumberOfAnnualHolidayGrantedBeforeCloseDate(String employeeId, String companyId,
			GeneralDate startDate, GeneralDate calcStartDate, GeneralDate calcEndDate) {
		List<YearlyHolidaysTimeRemainingExport> yearlyHolidaysTimeRemainingExport = new ArrayList<>();
		//締め開始日 ＞＝ 計算開始日
		if (startDate.afterOrEquals(calcStartDate)) {
			//過去データ取得終了日
			GeneralDate pastDataEndDate = startDate;
			if(startDate.afterOrEquals(calcEndDate)) {
				pastDataEndDate = calcEndDate;
			}

			// ドメインモデル「年休付与時点残数履歴データ」を取得
			List<AnnualLeaveTimeRemainingHistory> annualLeaveTimeRemainingHistory = annualLeaveTimeRemainHistRepository.findByCalcDateClosureDate(employeeId, startDate, pastDataEndDate);
			if(annualLeaveTimeRemainingHistory.isEmpty()) {
				List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData = annLeaGrantRemDataRepo.findByPeriod(employeeId, calcStartDate, pastDataEndDate);
				for (AnnualLeaveGrantRemainingData items : listAnnualLeaveGrantRemainingData) {
					yearlyHolidaysTimeRemainingExport.add(new YearlyHolidaysTimeRemainingExport(items.getGrantDate(), 0.0, items.getDetails().getGrantNumber().getDays().v()));
				}
				for (YearlyHolidaysTimeRemainingExport timeRemainingExport : yearlyHolidaysTimeRemainingExport) {
					List<AnnualLeaveGrantRemainingData> listAnnualLeaveGrantRemainingData1 = annLeaGrantRemDataRepo.findByGrantDateAndDeadline(employeeId, timeRemainingExport.getAnnualHolidayGrantDay(), timeRemainingExport.getAnnualHolidayGrantDay());
					Double annualRemainingGrantTime = timeRemainingExport.getAnnualRemainingGrantTime();
					for (AnnualLeaveGrantRemainingData annualLeaveGrantRemainingData : listAnnualLeaveGrantRemainingData1) {
						annualRemainingGrantTime += annualLeaveGrantRemainingData.getDetails().getRemainingNumber().getDays().v();
					}
					timeRemainingExport.setAnnualRemainingGrantTime(annualRemainingGrantTime);
				}
			} else {
				for (AnnualLeaveTimeRemainingHistory altrh : annualLeaveTimeRemainingHistory) {
					// 年休付与時点残数履歴データを集計する
					YearlyHolidaysTimeRemainingExport yhtre = new YearlyHolidaysTimeRemainingExport(
							altrh.getGrantProcessDate(), null, altrh.getDetails().getRemainingNumber().getDays().v());
					yearlyHolidaysTimeRemainingExport.add(yhtre);
				}
			}
		}
		return yearlyHolidaysTimeRemainingExport;
	}

}
