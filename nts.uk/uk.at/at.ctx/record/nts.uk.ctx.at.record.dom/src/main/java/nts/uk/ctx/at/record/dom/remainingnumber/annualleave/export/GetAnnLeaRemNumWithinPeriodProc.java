package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggregatePeriodWork;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.DividedDayEachProcess;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.ConfirmLeavePeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.CalcNextAnnualLeaveGrantDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveNumberInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveRemainingHistory;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxHistoryData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.CalcAnnLeaAttendanceRate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.CreateInterimAnnualMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.YearDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.scherec.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remain.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AttendanceRate;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerform;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureIdHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureIdHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee.RequireM1;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantDays;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantNum;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 処理：期間中の年休残数を取得
 * @author shuichi_ishida
 */
public class GetAnnLeaRemNumWithinPeriodProc {

//	/**
//	 * 期間中の年休残数を取得
//	 * @param require
//	 * @param cacheCarrier
//	 * @param companyId
//	 * @param employeeId 社員ID
//	 * @param aggrPeriod 集計期間
//	 * @param mode 実績のみ参照区分
//	 * @param criteriaDate 基準日
//	 * @param isGetNextMonthData 翌月管理データ取得フラグ
//	 * @param isCalcAttendanceRate 出勤率計算フラグ
//	 * @param isOverWriteOpt 上書きフラグ
//	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
//	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
//	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
//	 * @return 年休の集計結果
//	 */
//	public static Optional<AggrResultOfAnnualLeave> algorithm(RequireM3 require, CacheCarrier cacheCarrier, String companyId,
//			String employeeId, DatePeriod aggrPeriod, InterimRemainMngMode mode, GeneralDate criteriaDate,
//			boolean isGetNextMonthData, boolean isCalcAttendanceRate, Optional<Boolean> isOverWriteOpt,
//			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt, Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
//			Optional<Boolean> noCheckStartDate) {
//
//		return algorithm(require, cacheCarrier, companyId, employeeId, aggrPeriod, mode, criteriaDate,
//				isGetNextMonthData, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
//				prevAnnualLeaveOpt,
//				(noCheckStartDate.isPresent() ? noCheckStartDate.get() : false),
//				Optional.empty(), Optional.empty(), Optional.empty(),
//				Optional.empty(), Optional.empty(), Optional.empty());
//	}
//
//	/**
//	 * 期間中の年休残数を取得
//	 * @param companyId 会社ID
//	 * @param employeeId 社員ID
//	 * @param aggrPeriod 集計期間
//	 * @param mode モード
//	 * @param criteriaDate 基準日
//	 * @param isGetNextMonthData 翌月管理データ取得フラグ
//	 * @param isCalcAttendanceRate 出勤率計算フラグ
//	 * @param isOverWriteOpt 上書きフラグ
//	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
//	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
//	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
//	 * @param aggrPastMonthMode 過去月集計モード
//	 * @param yearMonth 年月
//	 * @param companySets 月別集計で必要な会社別設定
//	 * @param employeeSets 月別集計で必要な社員別設定
//	 * @param monthlyCalcDailys 月の計算中の日別実績データ
//	 * @return 年休の集計結果
//	 */
//	public static Optional<AggrResultOfAnnualLeave> algorithm(RequireM3 require, CacheCarrier cacheCarrier,
//			String companyId, String employeeId, DatePeriod aggrPeriod, InterimRemainMngMode mode,
//			GeneralDate criteriaDate, boolean isGetNextMonthData, boolean isCalcAttendanceRate,
//			Optional<Boolean> isOverWriteOpt, Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
//			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt, boolean noCheckStartDate,
//			Optional<MonAggrCompanySettings> companySets, Optional<MonAggrEmployeeSettings> employeeSets,
//			Optional<MonthlyCalculatingDailys> monthlyCalcDailys) {
//
//		return algorithm(require, cacheCarrier, companyId, employeeId, aggrPeriod, mode, criteriaDate,
//				isGetNextMonthData, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
//				prevAnnualLeaveOpt,
//				noCheckStartDate,
//				Optional.empty(), Optional.empty(), Optional.empty(),
//				companySets, employeeSets, monthlyCalcDailys);
//	}
//
//	/**
//	 * 期間中の年休残数を取得
//	 * @param companyId 会社ID
//	 * @param employeeId 社員ID
//	 * @param aggrPeriod 集計期間
//	 * @param mode モード
//	 * @param criteriaDate 基準日
//	 * @param isGetNextMonthData 翌月管理データ取得フラグ
//	 * @param isCalcAttendanceRate 出勤率計算フラグ
//	 * @param isOverWriteOpt 上書きフラグ
//	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
//	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
//	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
//	 * @param isOutShortRemainOpt 不足分付与残数データ出力区分
//	 * @param aggrPastMonthModeOpt 過去月集計モード
//	 * @param yearMonthOpt 年月
//	 * @return 年休の集計結果
//	 */
//	public static Optional<AggrResultOfAnnualLeave> algorithm(RequireM3 require, CacheCarrier cacheCarrier, String companyId,
//			String employeeId, DatePeriod aggrPeriod, InterimRemainMngMode mode, GeneralDate criteriaDate, boolean isGetNextMonthData,
//			boolean isCalcAttendanceRate, Optional<Boolean> isOverWriteOpt, Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
//			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt, Optional<Boolean> noCheckStartDate,
//			Optional<Boolean> isOutShortRemainOpt, Optional<Boolean> aggrPastMonthModeOpt, Optional<YearMonth> yearMonthOpt) {
//
//		return algorithm(require, cacheCarrier, companyId, employeeId, aggrPeriod, mode, criteriaDate,
//				isGetNextMonthData, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
//				prevAnnualLeaveOpt,
//				(noCheckStartDate.isPresent() ? noCheckStartDate.get() : false),
//				isOutShortRemainOpt, aggrPastMonthModeOpt, yearMonthOpt,
//				Optional.empty(), Optional.empty(), Optional.empty());
//	}

	/**
	 * 期間中の年休残数を取得
	 * @param require Require
	 * @param cacheCarrier CacheCarrier
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggrPeriod 集計期間
	 * @param mode 実績のみ参照区分
	 * @param criteriaDate 基準日
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
	 * @param aggrPastMonthModeOpt 過去月集計モード
	 * @param yearMonthOpt 年月※過去月集計モード  = true の場合は必須
	 * @return 年休の集計結果
	 */
	public static Optional<AggrResultOfAnnualLeave> algorithm(
			RequireM3 require,
			CacheCarrier cacheCarrier,
			String companyId,
			String employeeId,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			GeneralDate criteriaDate,
			boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			Optional<Boolean> aggrPastMonthModeOpt,
			Optional<YearMonth> yearMonthOpt
			) {

		// 年休の使用区分を取得する
		boolean isManageAnnualLeave = false;

		// ドメインモデル「年休設定」を取得する
		AnnualPaidLeaveSetting annualLeaveSet = require.annualPaidLeaveSetting(companyId);

		if (annualLeaveSet != null) isManageAnnualLeave = annualLeaveSet.isManaged();
		if (!isManageAnnualLeave) return Optional.empty();

		AggrResultOfAnnualLeave aggrResult = new AggrResultOfAnnualLeave();

		// 社員、年休社員基本情報　取得
		Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoOpt = Optional.empty();

		EmployeeImport employee = require.employee(cacheCarrier,employeeId);
		annualLeaveEmpBasicInfoOpt = require.employeeAnnualLeaveBasicInfo(employeeId);

		if (employee == null) return Optional.empty();
		if (!annualLeaveEmpBasicInfoOpt.isPresent()) return Optional.empty();
		val empBasicInfo = annualLeaveEmpBasicInfoOpt.get();
		val grantTableCode = empBasicInfo.getGrantRule().getGrantTableCode().v();

		// 「休暇の集計期間から入社前、退職後を除く」を実行する
		aggrPeriod = ConfirmLeavePeriod.sumPeriod(aggrPeriod, employee);
		if (aggrPeriod == null) return Optional.empty();

		// 年休付与テーブル設定、勤続年数テーブル　取得
		Optional<GrantHdTblSet> grantHdTblSetOpt = Optional.empty();
		Optional<List<LengthServiceTbl>> lengthServiceTblsOpt = Optional.empty();

		grantHdTblSetOpt = require.grantHdTblSet(companyId, grantTableCode);
		lengthServiceTblsOpt = Optional.ofNullable(require.lengthServiceTbl(companyId, grantTableCode));

		// 年休付与残数データ　取得
		List<AnnualLeaveGrantRemaining> grantRemainingDatas;
		grantRemainingDatas = require.annualLeaveGrantRemainingData(employeeId).stream()
							.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());

		// 日別実績の運用開始設定　取得
		Optional<OperationStartSetDailyPerform> operationStartSetOpt = Optional.empty();
		operationStartSetOpt = require.dailyOperationStartSet(new CompanyId(companyId));

		// 集計開始日時点の年休情報を作成
		AnnualLeaveInfo annualLeaveInfo
			= createInfoAsOfPeriodStart(require, cacheCarrier, companyId, employeeId,
				prevAnnualLeaveOpt, aggrPastMonthModeOpt, yearMonthOpt, aggrPeriod,
				mode, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt);

		// 次回年休付与日を計算
		List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();
		{
			// 次回年休付与を計算
			nextAnnualLeaveGrantList = CalcNextAnnualLeaveGrantDate.algorithm(
					require, cacheCarrier,
					companyId, employeeId, Optional.of(aggrPeriod),
					Optional.ofNullable(employee), annualLeaveEmpBasicInfoOpt,
					grantHdTblSetOpt, lengthServiceTblsOpt);

//			// 「出勤率計算フラグ」をチェック
//			if (isCalcAttendanceRate){

				// 勤務実績によって次回年休付与を更新
				for (val nextAnnualGrantList : nextAnnualLeaveGrantList){
					if (!grantHdTblSetOpt.isPresent()) continue;
					if (!lengthServiceTblsOpt.isPresent()) continue;

					// 次回年休付与の付与日数を条件によって更新する
					{
						// 年休出勤率を計算する
						val resultRateOpt = CalcAnnLeaAttendanceRate.algorithm(require, cacheCarrier,
								companyId, employeeId,
								nextAnnualGrantList.getGrantDate(),
								Optional.of(nextAnnualGrantList.getTimes().v()),
								Optional.of(annualLeaveSet), Optional.of(employee), annualLeaveEmpBasicInfoOpt,
								grantHdTblSetOpt, lengthServiceTblsOpt, operationStartSetOpt);
						if (resultRateOpt.isPresent()){
							val resultRate = resultRateOpt.get();
							nextAnnualGrantList.setAttendanceRate(Optional.of(
									new AttendanceRate(resultRate.getAttendanceRate())));
							nextAnnualGrantList.setPrescribedDays(Optional.of(
									new YearDayNumber(resultRate.getPrescribedDays())));
							nextAnnualGrantList.setWorkingDays(Optional.of(
									new YearDayNumber(resultRate.getWorkingDays())));
							nextAnnualGrantList.setDeductedDays(Optional.of(
									new YearDayNumber(resultRate.getDeductedDays())));

							// 日数と出勤率から年休付与テーブルを取得する
							val grantConditionOpt = grantHdTblSetOpt.get().getGrantCondition(
									resultRate.getAttendanceRate(),
									resultRate.getPrescribedDays(),
									resultRate.getWorkingDays(),
									resultRate.getDeductedDays());

							if (grantConditionOpt.isPresent()){
								val grantCondition = grantConditionOpt.get();
								val conditionNo = grantCondition.getConditionNo();

								// 付与日数を計算
								val grantHdTblOpt = require.grantHdTbl(companyId, conditionNo,
																	grantTableCode, nextAnnualGrantList.getTimes().v());

								if (grantHdTblOpt.isPresent()){
									val grantHdTbl = grantHdTblOpt.get();
									nextAnnualGrantList.setGrantDays(Finally.of(grantHdTbl.getGrantDays()));
									nextAnnualGrantList.setHalfDayAnnualLeaveMaxTimes(grantHdTbl.getLimitDayYear());
									nextAnnualGrantList.setTimeAnnualLeaveMaxDays(grantHdTbl.getLimitTimeHd());
								}
							}
							else {

								// 次回年休付与．付与日数　←　0
								nextAnnualGrantList.setGrantDays(Finally.of(new GrantDays(0.0)));
							}
						}
					}
				}
//			}
		}

		// ；年休集計期間を作成
		List<AggregatePeriodWork> aggregateWork = createAggregatePeriod(
				nextAnnualLeaveGrantList, aggrPeriod, grantRemainingDatas);

		// 2暫定年休管理データを取得する
		val tempAnnualLeaveMngs = getTempAnnualLeaveMngs(
				require, employeeId, aggrPeriod, mode,
				isOverWriteOpt, forOverWriteListOpt);

		for (val aggregatePeriodWork : aggregateWork){

			// 年休の消滅・付与・消化
			aggrResult = annualLeaveInfo.lapsedGrantDigest(
					require, companyId, employeeId, aggregatePeriodWork,
					tempAnnualLeaveMngs, aggrResult, annualLeaveSet);
		}

		// 年休不足分を付与残数データとして作成する
		aggrResult = createShortRemainingDatas(
				employeeId, companyId, aggrResult);

		// 年休不足分として作成した年休付与データを削除する
		aggrResult = deleteDummyRemainingDatas(aggrResult);

		// 「年休の集計結果」を返す
		return Optional.of(aggrResult);
	}

	/**
	 * 集計開始日時点の年休情報を作成
	 * @param require
	 * @param cacheCarrier
	 * @param companyId　会社ID
	 * @param employeeId 社員ID
//	 * @param noCheckStartDate
	 * @param prevAnnualLeaveOpt　前回の年休の集計結果
	 * @param aggrPastMonthModeOpt 過去月集計モード
	 * @param yearMonthOpt 年月
	 * @param aggrPeriod 集計期間
//	 * @param grantRemainingDatas
	 * @param mode 実績のみ参照区分
//	 * @param isGetNextMonthData
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt　上書き用の暫定管理データ
	 * @return
	 */
	private static AnnualLeaveInfo createInfoAsOfPeriodStart(
			RequireM3 require,
			CacheCarrier cacheCarrier,
			String companyId,
			String employeeId,
//			boolean noCheckStartDate,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			Optional<Boolean> aggrPastMonthModeOpt,
			Optional<YearMonth> yearMonthOpt,
			DatePeriod aggrPeriod,
//			List<AnnualLeaveGrantRemaining> grantRemainingDatas,
			InterimRemainMngMode mode,
//			boolean isGetNextMonthData,
			boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt){

		GeneralDate closureStartDate = aggrPeriod.start();

		AnnualLeaveInfo emptyInfo = new AnnualLeaveInfo();
		emptyInfo.setYmd(aggrPeriod.start());

		// 集計開始日時点の前回の年休の集計結果が存在するかチェック
		// パラメータ「集計開始日」とパラメータ「前回の年休の集計結果．年休情報(期間終了日の翌日開始時点)．年月日」を比較
		AnnualLeaveInfo prevAnnualLeaveInfo
			= prevAnnualLeaveOpt.map(c -> c.getAsOfStartNextDayOfPeriodEnd()).orElse(null);

		// 「開始日」と「年休情報．年月日」を比較
		boolean isSameInfo = false;
		if (prevAnnualLeaveInfo != null){
			if (aggrPeriod.start().equals(prevAnnualLeaveInfo.getYmd())){
				isSameInfo = true;
			}
		}

		if (isSameInfo){ // 集計開始日時点の前回の年休の集計結果が存在するとき

			// パラメータ「前回の年休の集計結果．年休情報(期間終了日の翌日開始時点)」の年休付与残数データ・年休上限データを取得
			// 年休付与残数データ、年休上限データをもとに年休情報を作成
			return createInfoFromRemainingData(
					prevAnnualLeaveInfo.getGrantRemainingList(),
					Optional.of(prevAnnualLeaveInfo.getMaxData()), aggrPeriod);

		} else {

			// 過去月集計モードの判断
			boolean aggrPastMonthMode = false;
			if (aggrPastMonthModeOpt.isPresent() && yearMonthOpt.isPresent()) {
				aggrPastMonthMode = aggrPastMonthModeOpt.get() ;
			}

			if (aggrPastMonthMode) { //  過去月集計モードのとき
				List<AnnualLeaveGrantRemaining> remainingDatas = new ArrayList<>();

				// 過去月集計モードで休暇残数を計算する締め開始日を取得する
				Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, employeeId, aggrPeriod.end());
				// 過去月モードの場合、年月は必須なのでemptyだった場合はそのままexceptionを発生させる。
				List<DatePeriod> closurePeriods = closure.getPeriodByYearMonth(yearMonthOpt.get());
				closureStartDate = aggrPeriod.start();
				if(!closurePeriods.isEmpty())
					closureStartDate = closurePeriods.get(0).start();

			} else {  //  過去月集計モードでないとき

				// 休暇残数を計算する締め開始日を取得する
				Optional<GeneralDate> closureStartDateOpt
					= getClosureStartDate(require, cacheCarrier, employeeId);
				if ( closureStartDateOpt.isPresent() ) {
					closureStartDate = closureStartDateOpt.get();
				}

			}

			// 社員の年休情報を取得
			AnnualLeaveInfo annualLeaveInfo = createInfoAsOfPeriodStart(
					 require,
					 cacheCarrier,
					 companyId,
					 employeeId,
					 prevAnnualLeaveOpt,
					 aggrPastMonthMode,
					 yearMonthOpt,
					 aggrPeriod,
					 mode,
					 isCalcAttendanceRate,
					 isOverWriteOpt,
					 forOverWriteListOpt,
					 closureStartDate);

			// 年休付与残数データ、年休上限データをもとに年休情報を作成
			return createInfoFromRemainingData(
					annualLeaveInfo.getGrantRemainingList(),
					Optional.ofNullable(annualLeaveInfo.getMaxData()),
					aggrPeriod);

		}
	}

//		// 取得した締め開始日と「集計開始日」を比較
//		if (closureStart != null){
//
//			// 締め開始日＜集計開始日　か確認する
//			if (closureStart.before(aggrPeriod.start())){
//				isAfterClosureStart = true;
//			}
//		}
//
//		if (!closureStartOpt.isPresent()){
//			closureStartOpt = Optional.of(aggrPeriod.start());
//		}
//
//		if (isAfterClosureStart){
//			// 締め開始日<集計開始日　の時
//
//			// 開始日までの年休残数を計算　（締め開始日～集計開始日前日）
//			val aggrResultOpt = algorithm(
//					require, cacheCarrier, companyId, employeeId,
//					new DatePeriod(closureStartOpt.get(),
//					aggrPeriod.start().addDays(-1)), mode, aggrPeriod.start().addDays(-1),
//					isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
//					Optional.empty(), aggrPastMonthModeOpt, yearMonthOpt);
//
//			if (!aggrResultOpt.isPresent()) return emptyInfo;
//			val aggrResult = aggrResultOpt.get();
//
//			// 年休情報（期間終了日の翌日開始時点）を取得
//			val asOfPeriodEnd = aggrResult.getAsOfStartNextDayOfPeriodEnd();
//
//			// 取得内容をもとに年休情報を作成
//			return createInfoFromRemainingData(
//					asOfPeriodEnd.getGrantRemainingList(),
//					Optional.of(asOfPeriodEnd.getMaxData()), aggrPeriod);
//		}
//
//		// 締め開始日>=集計開始日　or 締め開始日がnull　の時
//
//
//		// 「年休付与残数データ」を取得  要修正　のちに対応
//		List<AnnualLeaveGrantRemaining> remainingDatas = new ArrayList<>();
//		List<AnnualLeaveGrantRemainingData> remainings = require.annualLeaveGrantRemainingData(employeeId);
//
//		GeneralDate closureStartDate = closureStart;
//		remainings.stream()
//			.filter(c->c.getDeadline().afterOrEquals(closureStartDate)
//					&& c.getGrantDate().beforeOrEquals(closureStartDate)
//					&& c.getExpirationStatus().IsAVAILABLE())
//			.forEach(c->remainingDatas.add(new AnnualLeaveGrantRemaining(c)));
//
////		for (val grantRemainingData : grantRemainingDatas){
////			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
////			if (grantRemainingData.getGrantDate().after(closureStartOpt.get())) continue;
////			if (grantRemainingData.getDeadline().before(closureStartOpt.get())) continue;
////			remainingDatas.add(grantRemainingData);
////		}
//
//		// 「年休上限データ」を取得
//		val annLeaMaxDataOpt = require.annualLeaveMaxData(employeeId);
//
//		// 取得内容をもとに年休情報を作成
//		return createInfoFromRemainingData(remainingDatas, annLeaMaxDataOpt, aggrPeriod);
//	}

	/**
	 * 休暇残数を計算する締め開始日を取得する
	 * @param employeeId 社員ID
	 * @return 締め開始日
	 */
	static public Optional<GeneralDate> getClosureStartDate(
			RequireM3 require, CacheCarrier cacheCarrier, String employeeId) {

		GeneralDate closureStart = null;	// 締め開始日
		Optional<GeneralDate> closureStartOpt = Optional.empty();

		// 最新の締め終了日翌日を取得する
		Optional<ClosureStatusManagement> sttMng = require.latestClosureStatusManagement(employeeId);
		if (sttMng.isPresent()){
			// 受け取った「年月日」を返す
			closureStart = sttMng.get().getPeriod().end();
			if (closureStart.before(GeneralDate.max())){
				closureStart = closureStart.addDays(1);
			}
			closureStartOpt = Optional.of(closureStart);
		}
		else {
			// 社員に対応する締め開始日を取得する
			// 受け取った「締め開始日」を返す
			closureStartOpt = GetClosureStartForEmployee.algorithm(require, cacheCarrier, employeeId);
		}

		return closureStartOpt;
	}

	/**
	 * 社員の年休情報を取得
	 * @param require
	 * @param cacheCarrier
	 * @param companyId　会社ID
	 * @param employeeId 社員ID
	 * @param prevAnnualLeaveOpt　前回の年休の集計結果
	 * @param aggrPastMonthMode 過去月集計モード
	 * @param yearMonthOpt 年月
	 * @param aggrPeriod 集計期間
	 * @param mode 実績のみ参照区分
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt　上書き用の暫定管理データ
	 * @return
	 */
	private static AnnualLeaveInfo createInfoAsOfPeriodStart(
			RequireM3 require,
			CacheCarrier cacheCarrier,
			String companyId,
			String employeeId,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			Boolean aggrPastMonthMode,
			Optional<YearMonth> yearMonthOpt,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			GeneralDate closureStart
			){

			// 年休付与残数データリスト
			List<AnnualLeaveGrantRemaining> grantRemainingDatas
				= new ArrayList<AnnualLeaveGrantRemaining>();

			// 年休上限データを取得
			Optional<AnnualLeaveMaxData>  annLeaMaxDataOpt = Optional.empty();


			// 取得した締め開始日とパラメータ「集計開始日」を比較

			 // 締め開始日=パラメータ「集計開始日」
			if ( closureStart.equals(aggrPeriod.start())
					 // 締め開始日>パラメータ「集計開始日」 && パラメータ．過去月集計モードがfalse
					|| ( closureStart.after(aggrPeriod.start()) && !aggrPastMonthMode )
				){

				// ドメインモデル「年休付与残数データ」を取得
	//			【条件】
	//			社員ID=パラメータ「社員ID」
	//					付与日<=締め開始日
	//					期限日>=締め開始日
	//					期限切れ状態=使用可能
				grantRemainingDatas = require.annualLeaveGrantRemainingData(employeeId).stream()
						.filter(c->c.getGrantDate().beforeOrEquals(closureStart)
								&& c.getDeadline().afterOrEquals(closureStart)
								&& c.getExpirationStatus().IsAVAILABLE())
						.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());

				// 年休上限データを取得
				annLeaMaxDataOpt = require.annualLeaveMaxData(employeeId);

			}
			// 締め開始日>パラメータ「集計開始日」 && パラメータ．過去月集計モードがtrue
			else if (closureStart.after(aggrPeriod.start())
					&& aggrPastMonthMode ){

					// ドメインモデル「年休付与残数履歴データ」を取得
					List<AnnualLeaveRemainingHistory> remainHistList
						= require.annualLeaveRemainingHistory(
							employeeId, yearMonthOpt.get());
					if (remainHistList.size() > 0) {
						// 付与日 ASC、期限切れ状態＝「使用可能」　を採用
						remainHistList.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
						for (AnnualLeaveRemainingHistory remainHist : remainHistList) {
							if (remainHist.getExpirationStatus() != LeaveExpirationStatus.AVAILABLE) continue;

							// 取得したドメインを年休付与残数データに変換
							AnnualLeaveGrantRemaining remainData = new AnnualLeaveGrantRemaining(
									AnnualLeaveGrantRemainingData.createFromHistory(remainHist));
							grantRemainingDatas.add(remainData);
						}
					}

					// 社員の締めを取得
					Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, employeeId, aggrPeriod.end());

					// 年月指定して年休上限データを取得
					Optional<AnnualLeaveMaxHistoryData> annualLeaveMaxHistoryData
						= require.AnnualLeaveMaxHistoryData(
								employeeId, yearMonthOpt.get(), closure.getClosureId(), closure.getClosureDateOfCurrentMonth().get());

					// 「AnnualLeaveMaxData」クラスへキャスト
					if ( annualLeaveMaxHistoryData.isPresent() ) {
						annLeaMaxDataOpt = Optional.of((AnnualLeaveMaxData)annualLeaveMaxHistoryData.get());
					}

			} else if (closureStart.before(aggrPeriod.start())){ // 締め開始日<パラメータ「集計開始日」

					// 開始日までの年休残数を計算　（締め開始日～集計開始日前日）
					val aggrResultOpt = algorithm(
							require, cacheCarrier, companyId, employeeId,
							new DatePeriod(closureStart, aggrPeriod.start().addDays(-1)),
							mode, aggrPeriod.start().addDays(-1),
							isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
							Optional.empty(), Optional.of(aggrPastMonthMode), yearMonthOpt);

					if (aggrResultOpt.isPresent()) {
						val aggrResult = aggrResultOpt.get();

						// 年休情報（期間終了日の翌日開始時点）を取得
						val asOfPeriodEnd = aggrResult.getAsOfStartNextDayOfPeriodEnd();

						// 集計結果から、年休付与残数データを作成する
						// 集計結果から、上限データを作成する
						// 年休付与残数データ、年休上限データを返す

						return createInfoFromRemainingData(
								asOfPeriodEnd.getGrantRemainingList(),
								Optional.of(asOfPeriodEnd.getMaxData()), aggrPeriod);
					}
			}

			AnnualLeaveInfo annualLeaveInfo = new AnnualLeaveInfo();
			annualLeaveInfo.setGrantRemainingList(grantRemainingDatas);
			if ( annLeaMaxDataOpt.isPresent() ) {
				annualLeaveInfo.setMaxData(annLeaMaxDataOpt.get());
			}
			return annualLeaveInfo;
	}

	/**
	 * 年休付与残数データから年休情報を作成
	 * @param grantRemainingDataList 付与残数データリスト
	 * @param maxDataOpt 上限データ
	 * @return 年休情報
	 */
	private static AnnualLeaveInfo createInfoFromRemainingData(List<AnnualLeaveGrantRemaining> grantRemainingDataList,
			Optional<AnnualLeaveMaxData> maxDataOpt, DatePeriod aggrPeriod){

		AnnualLeaveInfo returnInfo = new AnnualLeaveInfo();
		returnInfo.setYmd(aggrPeriod.start());

		String employeeId = "";

		// 年休情報．年休付与情報　←　パラメータ「付与残数データ」
		List<AnnualLeaveGrantRemaining> targetDatas = new ArrayList<>();
		for (val grantRemainingData : grantRemainingDataList){
			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
			targetDatas.add(grantRemainingData);
			employeeId = grantRemainingData.getEmployeeId();
		}
		targetDatas.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
		returnInfo.setGrantRemainingList(targetDatas);

		// 年休情報．上限データ　←　パラメータ「上限データ」
		if (!maxDataOpt.isPresent()) {
			Integer intNull = null;
			AnnualLeaveMaxData emptyMaxData = AnnualLeaveMaxData.createFromJavaType(
					employeeId, intNull, intNull, intNull, intNull);
			returnInfo.setMaxData(emptyMaxData);
		}
		else {
			returnInfo.setMaxData(maxDataOpt.get());
		}

		// 年休情報残数を更新
		returnInfo.updateRemainingNumber(false);

		// 年休情報を返す
		return returnInfo;
	}

	/**
	 * 年休集計期間を作成
	 * @param nextAnnualLeaveGrantList 次回年休付与リスト
	 * @return 年休集計期間WORKリスト
	 */
	private static List<AggregatePeriodWork> createAggregatePeriod(List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList,
			DatePeriod aggrPeriod, List<AnnualLeaveGrantRemaining> grantRemainingDatas){

		List<AggregatePeriodWork> aggregatePeriodWorks = new ArrayList<>();

		// 処理単位分割日リスト
		Map<GeneralDate, DividedDayEachProcess> dividedDayMap = new HashMap<>();

		// 期間終了日翌日
		GeneralDate nextDayOfPeriodEnd = aggrPeriod.end();
		if (nextDayOfPeriodEnd.before(GeneralDate.max())) nextDayOfPeriodEnd = nextDayOfPeriodEnd.addDays(1);

		// 「年休付与残数データ」を取得　（期限日　昇順、付与日　昇順）
		List<AnnualLeaveGrantRemainingData> remainingDatas = new ArrayList<>();
		remainingDatas.addAll(grantRemainingDatas);
		Collections.sort(remainingDatas, new Comparator<AnnualLeaveGrantRemainingData>() {
			@Override
			public int compare(AnnualLeaveGrantRemainingData o1, AnnualLeaveGrantRemainingData o2) {
				int compDeadline = o1.getDeadline().compareTo(o2.getDeadline());
				if (compDeadline != 0) return compDeadline;
				return o1.getGrantDate().compareTo(o2.getGrantDate());
			}
		});

		// 取得した「年休付与残数データ」をすべて「処理単位分割日リスト」に追加
		for (val remainingData : remainingDatas){
			val deadline = remainingData.getDeadline();
			if (!aggrPeriod.contains(deadline)) continue;

			val nextDayOfDeadline = deadline.addDays(1);
			dividedDayMap.putIfAbsent(nextDayOfDeadline, new DividedDayEachProcess(nextDayOfDeadline));
			dividedDayMap.get(nextDayOfDeadline).getLapsedWork().setLapsedAtr(true);
		}

		// 「次回年休付与リスト」をすべて「処理単位分割日リスト」に追加
		for (val nextAnnualLeaveGrant : nextAnnualLeaveGrantList){
			val grantDate = nextAnnualLeaveGrant.getGrantDate();
			if (grantDate.beforeOrEquals(aggrPeriod.start().addDays(1))) continue;
			if (grantDate.after(nextDayOfPeriodEnd)) continue;

			dividedDayMap.putIfAbsent(grantDate, new DividedDayEachProcess(grantDate));
			dividedDayMap.get(grantDate).getGrantWork().setGrantAtr(true);
			//dividedDayMap.get(grantDate).setNextAnnualLeaveGrant(Optional.of(nextAnnualLeaveGrant));
		}

		// 期間終了日翌日の「処理単位分割日」を取得・追加　→　フラグ設定
		dividedDayMap.putIfAbsent(nextDayOfPeriodEnd, new DividedDayEachProcess(nextDayOfPeriodEnd));
		dividedDayMap.get(nextDayOfPeriodEnd).getEndDay().setNextPeriodEndAtr(true);

		// 「処理単位分割日」をソート
		List<DividedDayEachProcess> dividedDayList = new ArrayList<>();
		dividedDayList.addAll(dividedDayMap.values());
		dividedDayList.sort((a, b) -> a.getYmd().compareTo(b.getYmd()));

		// 「年休集計期間WORK」を作成
		AggregatePeriodWork startWork = new AggregatePeriodWork();
		val startWorkEnd = dividedDayList.get(0).getYmd().addDays(-1);
		startWork.setPeriod(new DatePeriod(aggrPeriod.start(), startWorkEnd));
		aggregatePeriodWorks.add(startWork);

		// 付与後フラグ
		boolean isAfterGrant = false;

		for (int index = 0; index < dividedDayList.size(); index++){
			val nowDividedDay = dividedDayList.get(index);
			DividedDayEachProcess nextDividedDay = null;
			if (index + 1 < dividedDayList.size()) nextDividedDay = dividedDayList.get(index + 1);

			// 付与フラグをチェック
			if (nowDividedDay.getGrantWork().isGrantAtr()) isAfterGrant = true;

			// 年休集計期間WORKを作成し、Listに追加
			GeneralDate workPeriodEnd = nextDayOfPeriodEnd;
			if (nextDividedDay != null) workPeriodEnd = nextDividedDay.getYmd().addDays(-1);

//			AggregatePeriodWork nowWork = AggregatePeriodWork.of(
//					new DatePeriod(nowDividedDay.getYmd(), workPeriodEnd),
//					false,
//					nowDividedDay.isNextDayAfterPeriodEnd(),
//					nowDividedDay.isGrantAtr(),
//					isAfterGrant,
//					nowDividedDay.isLapsedAtr(),
//					nowDividedDay.getNextAnnualLeaveGrant());
			AggregatePeriodWork nowWork = new AggregatePeriodWork(new DatePeriod(nowDividedDay.getYmd(), workPeriodEnd),
					nowDividedDay.getLapsedWork(),
					nowDividedDay.getGrantWork(),
					nowDividedDay.getEndDay(),
					nowDividedDay.getGrantPeriodAtr());
			aggregatePeriodWorks.add(nowWork);
		}

		// 処理期間内で何回目の付与なのかを保持。（一回目の付与を判断したい）
		AtomicInteger grantNumber = new AtomicInteger(1);
		for( AggregatePeriodWork nowWork : aggregatePeriodWorks){
			if ( nowWork.getGrantWork().isGrantAtr() ) // 付与のとき
			{
				nowWork.getGrantWork().getSpecialLeaveGrant().ifPresent(x -> {
					x.setTimes(new GrantNum(grantNumber.get()));
				});
				grantNumber.incrementAndGet();
			}
		}

		for(AggregatePeriodWork work : aggregatePeriodWorks) {
			if(work.getPeriod().contains(aggrPeriod.end()))
				work.getEndWork().setPeriodEndAtr(true);
			if(work.getPeriod().contains(aggrPeriod.end().addDays(1)))
				work.getEndWork().setNextPeriodEndAtr(true);
		}

		return aggregatePeriodWorks;
	}

	/**
	 * 暫定年休管理データを取得する
	 * @return 暫定年休管理データWORKリスト
	 */
	private static List<TmpAnnualLeaveMngWork> getTempAnnualLeaveMngs(
			RequireM2 require,
			String employeeId,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt){

		List<TmpAnnualLeaveMngWork> results = new ArrayList<>();

		// 「モード」をチェック
		if (mode == InterimRemainMngMode.MONTHLY){
			// 月次モード
		}
		if (mode == InterimRemainMngMode.OTHER){
			// その他モード

			// 「暫定年休管理データ」を取得する
			val interimRemains = require.tmpAnnualHolidayMng(employeeId, aggrPeriod);
			for (val master : interimRemains){
				results.add(TmpAnnualLeaveMngWork.of(master));
			}

			// 年休フレックス補填分を暫定年休データに反映する
			{
				// 「月別実績の勤怠時間」を取得
				val attendanceTimes = require.attendanceTimeOfMonthly(employeeId, aggrPeriod);
				for (val attendanceTime : attendanceTimes){

					// 月別実績の勤怠時間からフレックス補填の暫定年休管理データを作成する
					val compensFlexWorkOpt = CreateInterimAnnualMngData.ofCompensFlexToWork(
							attendanceTime, attendanceTime.getDatePeriod().end());

					// 「暫定年休管理データ」を返す
					if (compensFlexWorkOpt.isPresent()) results.add(compensFlexWorkOpt.get());
				}
			}
		}

		// 「上書きフラグ」をチェック
		if (isOverWriteOpt.isPresent()){
			if (isOverWriteOpt.get()){

				// 上書き用データがある時、使用する
				if (forOverWriteListOpt.isPresent()){
					val overWrites = forOverWriteListOpt.get();
					for (val overWrite : overWrites){
						// 重複データを削除
						ListIterator<TmpAnnualLeaveMngWork> itrResult = results.listIterator();
						while (itrResult.hasNext()){
							TmpAnnualLeaveMngWork target = itrResult.next();
							if (target.equals(overWrite)) itrResult.remove();
						}
					}
					for (val overWrite : overWrites){
						// 上書き用データを追加
						results.add(overWrite);
					}
				}
			}
		}

		results.sort((a, b) -> a.getYmd().compareTo(b.getYmd()));
		return results;
	}

	/**
	 * 年休不足分を付与残数データとして作成する
	 * @param result 年休の集計結果
	 * @param isOutShortRemainOpt 不足分付与残数データ出力区分
	 * @return 年休の集計結果
	 */
	private static AggrResultOfAnnualLeave createShortRemainingDatas(
			String employeeId, String companyId,
			AggrResultOfAnnualLeave result){

//		// 「不足分付与残数データ出力区分」をチェック
//		if (!isOutShortRemainOpt.isPresent()) return result;
//		if (isOutShortRemainOpt.get() == false) return result;

		// 「年休情報」を取得
		val annualLeaveInfo = result.getAsOfPeriodEnd();

		// 合計使用数・残数
		Double useDays = 0.0;
		Integer useTime = null;
		Double remainDays = 0.0;
		Integer remainTime = null;
		Optional<GeneralDate> dummyDateOpt = Optional.empty();

		// ダミーとして作成した「年休付与残数(List)」を取得
		val itrRemainDatas = annualLeaveInfo.getGrantRemainingList().listIterator();
		while (itrRemainDatas.hasNext()){
			val remainData = itrRemainDatas.next();
			if (remainData.isDummyAtr() == false) continue;

			// 取得した年休付与残数の「年休使用数」、「年休残数」をそれぞれ合計
			AnnualLeaveNumberInfo detail = (AnnualLeaveNumberInfo) remainData.getDetails();
			useDays += detail.getUsedNumber().getDays().v();
			if (detail.getUsedNumber().getMinutes().isPresent()) {
				if (useTime == null) useTime = 0;
				useTime += detail.getUsedNumber().getMinutes().get().v();
			}
			remainDays += detail.getRemainingNumber().getDays().v();
			if (detail.getRemainingNumber().getMinutes().isPresent()) {
				if (remainTime == null) remainTime = 0;
				remainTime += detail.getRemainingNumber().getMinutes().get().v();
			}

			// 不足分合計データの付与日・期限日には、不足分の最終日を入れる
			dummyDateOpt = Optional.of(remainData.getGrantDate());
		}

		if (dummyDateOpt.isPresent()) {

			// 合計した「年休使用数」「年休残数」から年休付与残数を作成
			val newRemainData = new AnnualLeaveGrantRemaining(AnnualLeaveGrantRemainingData.createFromJavaType(
					"",employeeId, dummyDateOpt.get(), dummyDateOpt.get(),
					LeaveExpirationStatus.AVAILABLE.value, GrantRemainRegisterType.MONTH_CLOSE.value,
					0.0, null,
					useDays, useTime, null,
					remainDays, remainTime,
					0.0,
					null, null, null));
			newRemainData.setDummyAtr(false);

			// 年休情報．付与残数データに作成した年休付与残数を追加
			annualLeaveInfo.getGrantRemainingList().add(newRemainData);

//			// 年休不足分として作成した年休付与データを削除する
//			val itrRemainDatasTmp
//				= annualLeaveInfo.getGrantRemainingList().stream().filter(
//						c->c.isDummyAtr() == false
//						).collect(Collectors.toList());
//			annualLeaveInfo.setGrantRemainingList(itrRemainDatasTmp);
		}

		// 年休の集計結果を返す
		return result;
	}

	/**
	 * 年休不足分として作成した年休付与データを削除する
	 * @param result 年休の集計結果
	 * @return 年休の集計結果
	 */
	private static AggrResultOfAnnualLeave deleteDummyRemainingDatas(AggrResultOfAnnualLeave result){

		// 期間終了日時点の不足分付与残数データを削除する
		val itrAsOfPeriodEndData = result.getAsOfPeriodEnd().getGrantRemainingList().listIterator();
		while (itrAsOfPeriodEndData.hasNext()){
			val remainData = itrAsOfPeriodEndData.next();
			if (remainData.isDummyAtr()) itrAsOfPeriodEndData.remove();
		}

		// 期間終了日の翌日開始時点の不足分付与残数データを削除する
		val itrAsOfEndNextData = result.getAsOfStartNextDayOfPeriodEnd().getGrantRemainingList().listIterator();
		while (itrAsOfEndNextData.hasNext()){
			val remainData = itrAsOfEndNextData.next();
			if (remainData.isDummyAtr()) itrAsOfEndNextData.remove();
		}

		// 付与時点の不足分付与残数データを削除する
		if (result.getAsOfGrant().isPresent()){
			for (val asOfGrant : result.getAsOfGrant().get()){
				val itrAsOfGrant = asOfGrant.getGrantRemainingList().listIterator();
				while (itrAsOfGrant.hasNext()){
					val remainData = itrAsOfGrant.next();
					if (remainData.isDummyAtr()) itrAsOfGrant.remove();
				}
			}
		}

		// 消滅時点の不足分付与残数データを削除する
		if (result.getLapsed().isPresent()){
			for (val lapsed : result.getLapsed().get()){
				val itrLapsed = lapsed.getGrantRemainingList().listIterator();
				while (itrLapsed.hasNext()){
					val remainData = itrLapsed.next();
					if (remainData.isDummyAtr()) itrLapsed.remove();
				}
			}
		}

		// 年休の集計結果を返す
		return result;
	}

	public static interface RequireM2 {

//		List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType);

		List<TmpAnnualHolidayMng> tmpAnnualHolidayMng(String sid, DatePeriod dateData);

		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthly(String employeeId, DatePeriod period);
	}

	public static interface RequireM3 extends RequireM2,
												GetClosureStartForEmployee.RequireM1,
												CalcNextAnnualLeaveGrantDate.RequireM2,
												CalcAnnLeaAttendanceRate.RequireM1,
												LeaveRemainingNumber.RequireM3,
//												GetClosureIdHistory.RequireM2{
												GetClosureIdHistory.RequireM2,
												nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService.RequireM3{

		List<AnnualLeaveRemainingHistory> annualLeaveRemainingHistory(String sid, YearMonth ym);

		/** 締め状態管理 */
		Optional<ClosureStatusManagement> latestClosureStatusManagement(String employeeId);

		/** 年休上限履歴データを取得する */
		Optional<AnnualLeaveMaxHistoryData> AnnualLeaveMaxHistoryData(
				String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);

		Optional<AnnualLeaveMaxData> annualLeaveMaxData(String employeeId);

		List<AnnualLeaveGrantRemainingData> annualLeaveGrantRemainingData(String employeeId);

		Optional<OperationStartSetDailyPerform> dailyOperationStartSet(CompanyId companyId);
	}

}