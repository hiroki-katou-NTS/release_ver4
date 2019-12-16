package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.ConfirmLeavePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggregatePeriodWork;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveInfo;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.CalcNextAnnualLeaveGrantDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnLeaMaxDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.AttendanceRate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.YearDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerform;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerformRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantDays;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 処理：期間中の年休残数を取得
 * @author shuichi_ishida
 */
public class GetAnnLeaRemNumWithinPeriodProc {

	/** 社員 */
	private EmpEmployeeAdapter empEmployee;
	/** 年休社員基本情報 */
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;
	/** 年休付与テーブル設定 */
	private YearHolidayRepository yearHolidayRepo;
	/** 勤続年数テーブル */
	private LengthServiceRepository lengthServiceRepo;
	/** 年休設定 */
	private AnnualPaidLeaveSettingRepository annualPaidLeaveSet;
	/** 年休付与残数データ */
	private AnnLeaGrantRemDataRepository annLeaGrantRemDataRepo;
	/** 年休上限データ */
	private AnnLeaMaxDataRepository annLeaMaxDataRepo;
	/** 社員に対応する締め開始日を取得する */
	private GetClosureStartForEmployee getClosureStartForEmployee;
	/** 次回年休付与日を計算 */
	private CalcNextAnnualLeaveGrantDate calcNextAnnualLeaveGrantDate;
	/** 月次処理用の暫定残数管理データを作成する */
	@SuppressWarnings("unused")
	private InterimRemainOffMonthProcess interimRemOffMonth;
	/** 暫定年休管理データを作成する */
	private CreateInterimAnnualMngData createInterimAnnual;
	/** 暫定残数管理データ */
	private InterimRemainRepository interimRemainRepo;
	/** 暫定年休管理データ */
	private TmpAnnualHolidayMngRepository tmpAnnualLeaveMng;
	/** 月別実績の勤怠時間 */
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	/** 期間中の年休残数を取得 */
	private GetAnnLeaRemNumWithinPeriod getAnnLeaRemNumWithinPeriod;
	/** 締め状態管理 */
	private ClosureStatusManagementRepository closureSttMngRepo;
	/** 年休出勤率を計算する */
	private CalcAnnLeaAttendanceRate calcAnnLeaAttendanceRate;
	/** 年休付与テーブル */
	private GrantYearHolidayRepository grantYearHolidayRepo;
	/** 日別実績の運用開始設定 */
	private OperationStartSetDailyPerformRepository operationStartSetRepo;
	
	/** 会社ID */
	private String companyId;
	/** 社員ID */
	private String employeeId;
	/** 集計期間 */
	private DatePeriod aggrPeriod;
	/** モード */
	private InterimRemainMngMode mode;
	/** 翌月管理データ取得フラグ */
	private boolean isGetNextMonthData;
	/** 出勤率計算フラグ */
	private boolean isCalcAttendanceRate;
	/** 上書きフラグ */
	private Optional<Boolean> isOverWriteOpt;
	/** 上書き用の暫定年休管理データ */
	private Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt;
	/** 前回の年休の集計結果 */
	private Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt;
	/** 年休集計期間WORKリスト */
	private List<AggregatePeriodWork> aggregatePeriodWorks;
	/** 年休付与残数データリスト */
	private List<AnnualLeaveGrantRemaining> grantRemainingDatas;

	public GetAnnLeaRemNumWithinPeriodProc(
			EmpEmployeeAdapter empEmployee,
			AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo,
			YearHolidayRepository yearHolidayRepo,
			LengthServiceRepository lengthServiceRepo,
			AnnualPaidLeaveSettingRepository annualPaidLeaveSet,
			AnnLeaGrantRemDataRepository annLeaGrantRemDataRepo,
			AnnLeaMaxDataRepository annLeaMaxDataRepo,
			GetClosureStartForEmployee getClosureStartForEmployee,
			CalcNextAnnualLeaveGrantDate calcNextAnnualLeaveGrantDate,
			InterimRemainOffMonthProcess interimRemOffMonth,
			CreateInterimAnnualMngData createInterimAnnual,
			InterimRemainRepository interimRemainRepo,
			TmpAnnualHolidayMngRepository tmpAnnualLeaveMng,
			AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo,
			GetAnnLeaRemNumWithinPeriod getAnnLeaRemNumWithinPeriod,
			ClosureStatusManagementRepository closureSttMngRepo,
			CalcAnnLeaAttendanceRate calcAnnLeaAttendanceRate,
			GrantYearHolidayRepository grantYearHolidayRepo,
			OperationStartSetDailyPerformRepository operationStartSetRepo) {
		
		this.empEmployee = empEmployee;
		this.annLeaEmpBasicInfoRepo = annLeaEmpBasicInfoRepo;
		this.yearHolidayRepo = yearHolidayRepo;
		this.lengthServiceRepo = lengthServiceRepo;
		this.annualPaidLeaveSet = annualPaidLeaveSet;
		this.annLeaGrantRemDataRepo = annLeaGrantRemDataRepo;
		this.annLeaMaxDataRepo = annLeaMaxDataRepo;
		this.getClosureStartForEmployee = getClosureStartForEmployee;
		this.calcNextAnnualLeaveGrantDate = calcNextAnnualLeaveGrantDate;
		this.interimRemOffMonth = interimRemOffMonth;
		this.createInterimAnnual = createInterimAnnual;
		this.interimRemainRepo = interimRemainRepo;
		this.tmpAnnualLeaveMng = tmpAnnualLeaveMng;
		this.attendanceTimeOfMonthlyRepo = attendanceTimeOfMonthlyRepo;
		this.getAnnLeaRemNumWithinPeriod = getAnnLeaRemNumWithinPeriod;
		this.closureSttMngRepo = closureSttMngRepo;
		this.calcAnnLeaAttendanceRate = calcAnnLeaAttendanceRate;
		this.grantYearHolidayRepo = grantYearHolidayRepo;
		this.operationStartSetRepo = operationStartSetRepo;
	}

	/**
	 * 期間中の年休残数を取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggrPeriod 集計期間
	 * @param mode モード
	 * @param criteriaDate 基準日
	 * @param isGetNextMonthData 翌月管理データ取得フラグ
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
	 * @return 年休の集計結果
	 */
	public Optional<AggrResultOfAnnualLeave> algorithm(
			String companyId,
			String employeeId,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			GeneralDate criteriaDate,
			boolean isGetNextMonthData,
			boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			Optional<Boolean> noCheckStartDate) {
	
		return this.algorithm(companyId, employeeId, aggrPeriod, mode, criteriaDate,
				isGetNextMonthData, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
				prevAnnualLeaveOpt,
				(noCheckStartDate.isPresent() ? noCheckStartDate.get() : false),
				Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	/**
	 * 期間中の年休残数を取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggrPeriod 集計期間
	 * @param mode モード
	 * @param criteriaDate 基準日
	 * @param isGetNextMonthData 翌月管理データ取得フラグ
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt 上書き用の暫定年休管理データ
	 * @param prevAnnualLeaveOpt 前回の年休の集計結果
	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @return 年休の集計結果
	 */
	public Optional<AggrResultOfAnnualLeave> algorithm(
			String companyId,
			String employeeId,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			GeneralDate criteriaDate,
			boolean isGetNextMonthData,
			boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			boolean noCheckStartDate,
			Optional<MonAggrCompanySettings> companySets,
			Optional<MonAggrEmployeeSettings> employeeSets,
			Optional<MonthlyCalculatingDailys> monthlyCalcDailys) {
		
		this.companyId = companyId;
		this.employeeId = employeeId;
		this.aggrPeriod = aggrPeriod;
		this.mode = mode;
		this.isGetNextMonthData = isGetNextMonthData;
		this.isCalcAttendanceRate = isCalcAttendanceRate;
		this.isOverWriteOpt = isOverWriteOpt;
		this.forOverWriteListOpt = forOverWriteListOpt;
		this.prevAnnualLeaveOpt = prevAnnualLeaveOpt;
		
		// 年休の使用区分を取得する
		boolean isManageAnnualLeave = false;
		AnnualPaidLeaveSetting annualLeaveSet = null;
		if (companySets.isPresent()){
			annualLeaveSet = companySets.get().getAnnualLeaveSet();
		}
		else {
			annualLeaveSet = this.annualPaidLeaveSet.findByCompanyId(companyId);
		}
		if (annualLeaveSet != null) isManageAnnualLeave = annualLeaveSet.isManaged();
		if (!isManageAnnualLeave) return Optional.empty();

		AggrResultOfAnnualLeave aggrResult = new AggrResultOfAnnualLeave();
		
		// 社員、年休社員基本情報　取得
		EmployeeImport employee = null;
		Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoOpt = Optional.empty();
		if (employeeSets.isPresent()){
			employee = employeeSets.get().getEmployee();
			annualLeaveEmpBasicInfoOpt = employeeSets.get().getAnnualLeaveEmpBasicInfoOpt();
		}
		else {
			employee = this.empEmployee.findByEmpId(employeeId);
			annualLeaveEmpBasicInfoOpt = this.annLeaEmpBasicInfoRepo.get(employeeId);
		}
		if (employee == null) return Optional.empty();
		if (!annualLeaveEmpBasicInfoOpt.isPresent()) return Optional.empty();
		val empBasicInfo = annualLeaveEmpBasicInfoOpt.get();
		val grantTableCode = empBasicInfo.getGrantRule().getGrantTableCode().v();
		
		// 「休暇の集計期間から入社前、退職後を除く」を実行する
		this.aggrPeriod = ConfirmLeavePeriod.sumPeriod(this.aggrPeriod, employee);
		if (this.aggrPeriod == null) return Optional.empty();
		
		// 年休付与テーブル設定、勤続年数テーブル　取得
		Optional<GrantHdTblSet> grantHdTblSetOpt = Optional.empty();
		Optional<List<LengthServiceTbl>> lengthServiceTblsOpt = Optional.empty();
		if (companySets.isPresent()){
			grantHdTblSetOpt = Optional.ofNullable(
					companySets.get().getGrantHdTblSetMap().get(grantTableCode));
			lengthServiceTblsOpt = Optional.ofNullable(
					companySets.get().getLengthServiceTblListMap().get(grantTableCode));
		}
		else {
			grantHdTblSetOpt = this.yearHolidayRepo.findByCode(companyId, grantTableCode);
			lengthServiceTblsOpt = Optional.ofNullable(this.lengthServiceRepo.findByCode(companyId, grantTableCode));
		}
		
		// 年休付与残数データ　取得
		if (monthlyCalcDailys.isPresent()){
			this.grantRemainingDatas = monthlyCalcDailys.get().getGrantRemainingDatas();
		}
		else {
			this.grantRemainingDatas =
					this.annLeaGrantRemDataRepo.findNotExp(employeeId).stream()
							.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());
		}
		
		// 日別実績の運用開始設定　取得
		Optional<OperationStartSetDailyPerform> operationStartSetOpt = Optional.empty();
		if (companySets.isPresent()){
			operationStartSetOpt = companySets.get().getOperationStartSet();
		}
		else {
			operationStartSetOpt = this.operationStartSetRepo.findByCid(new CompanyId(companyId));
		}
		
		// 集計開始日時点の年休情報を作成
		AnnualLeaveInfo annualLeaveInfo = this.createInfoAsOfPeriodStart(noCheckStartDate);
		
		// 次回年休付与日を計算
		List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();
		{
			// 次回年休付与を計算
			nextAnnualLeaveGrantList = this.calcNextAnnualLeaveGrantDate.algorithm(
					companyId, employeeId, Optional.of(this.aggrPeriod),
					Optional.ofNullable(employee), annualLeaveEmpBasicInfoOpt,
					grantHdTblSetOpt, lengthServiceTblsOpt);
			
			// 「出勤率計算フラグ」をチェック
			if (isCalcAttendanceRate){
				
				// 勤務実績によって次回年休付与を更新
				for (val nextAnnualGrantList : nextAnnualLeaveGrantList){
					if (!grantHdTblSetOpt.isPresent()) continue;
					if (!lengthServiceTblsOpt.isPresent()) continue;
					
					// 次回年休付与の付与日数を条件によって更新する
					{
						// 年休出勤率を計算する
						val resultRateOpt = this.calcAnnLeaAttendanceRate.algorithm(companyId, employeeId,
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
								val grantHdTblOpt = this.grantYearHolidayRepo.find(
										companyId, conditionNo, grantTableCode, nextAnnualGrantList.getTimes().v());
								if (grantHdTblOpt.isPresent()){
									val grantHdTbl = grantHdTblOpt.get();
									nextAnnualGrantList.setGrantDays(
											grantHdTbl.getGrantDays());
									nextAnnualGrantList.setHalfDayAnnualLeaveMaxTimes(
											grantHdTbl.getLimitDayYear());
									nextAnnualGrantList.setTimeAnnualLeaveMaxDays(
											grantHdTbl.getLimitTimeHd());
								}
							}
							else {
								
								// 次回年休付与．付与日数　←　0
								nextAnnualGrantList.setGrantDays(new GrantDays(0.0));
							}
						}
					}
				}
			}
		}
		
		// 年休集計期間を作成
		this.createAggregatePeriod(nextAnnualLeaveGrantList);
		
		// 暫定年休管理データを取得する
		val tempAnnualLeaveMngs = this.getTempAnnualLeaveMngs();
		
		for (val aggregatePeriodWork : this.aggregatePeriodWorks){

			// 年休の消滅・付与・消化
			aggrResult = annualLeaveInfo.lapsedGrantDigest(companyId, employeeId, aggregatePeriodWork,
					tempAnnualLeaveMngs, isGetNextMonthData, isCalcAttendanceRate, aggrResult, annualLeaveSet);
		}
		
		// 年休不足分として作成した年休付与データを削除する
		aggrResult = this.deleteDummyRemainingDatas(aggrResult);
		
		// 「年休の集計結果」を返す
		return Optional.of(aggrResult);
	}
	
	/**
	 * 集計開始日時点の年休情報を作成
	 * @param noCheckStartDate 集計開始日を締め開始日とする　（締め開始日を確認しない）
	 * @return 年休情報
	 */
	private AnnualLeaveInfo createInfoAsOfPeriodStart(
			boolean noCheckStartDate){
	
		AnnualLeaveInfo emptyInfo = new AnnualLeaveInfo();
		emptyInfo.setYmd(this.aggrPeriod.start());
		
		// 「前回の年休情報」を確認　（前回の年休の集計結果．年休情報（期間終了日の翌日開始時点））
		AnnualLeaveInfo prevAnnualLeaveInfo = null;
		if (this.prevAnnualLeaveOpt.isPresent()){
			prevAnnualLeaveInfo = this.prevAnnualLeaveOpt.get().getAsOfStartNextDayOfPeriodEnd();
		}
		
		// 「開始日」と「年休情報．年月日」を比較
		boolean isSameInfo = false;
		if (prevAnnualLeaveInfo != null){
			if (this.aggrPeriod.start() == prevAnnualLeaveInfo.getYmd()){
				isSameInfo = true;
			}
		}
		if (isSameInfo){
			
			// 「前回の年休情報」を取得　→　取得内容をもとに年休情報を作成
			return this.createInfoFromRemainingData(
					prevAnnualLeaveInfo.getGrantRemainingList(), prevAnnualLeaveInfo.getMaxData());
		}
		
		// 「集計開始日を締め開始日とする」をチェック　（締め開始日としない時、締め開始日を確認する）
		boolean isAfterClosureStart = false;
		Optional<GeneralDate> closureStartOpt = Optional.empty();
		if (!noCheckStartDate){
			
			// 休暇残数を計算する締め開始日を取得する
			GeneralDate closureStart = null;	// 締め開始日
			{
				// 最新の締め終了日翌日を取得する
				Optional<ClosureStatusManagement> sttMng = this.closureSttMngRepo.getLatestByEmpId(this.employeeId);
				if (sttMng.isPresent()){
					closureStart = sttMng.get().getPeriod().end().addDays(1);
					closureStartOpt = Optional.of(closureStart);
				}
				else {
					
					//　社員に対応する締め開始日を取得する
					closureStartOpt = this.getClosureStartForEmployee.algorithm(this.employeeId);
					if (closureStartOpt.isPresent()) closureStart = closureStartOpt.get();
				}
			}
			
			// 取得した締め開始日と「集計開始日」を比較
			if (closureStart != null){
				
				// 締め開始日＜集計開始日　か確認する
				if (closureStart.before(this.aggrPeriod.start())) isAfterClosureStart = true;
			}
		}
		if (!closureStartOpt.isPresent()) closureStartOpt = Optional.of(this.aggrPeriod.start());
		
		if (isAfterClosureStart){
			// 締め開始日<集計開始日　の時
			
			// 開始日までの年休残数を計算　（締め開始日～集計開始日前日）
			val aggrResultOpt = this.getAnnLeaRemNumWithinPeriod.algorithm(
					this.companyId, this.employeeId,
					new DatePeriod(closureStartOpt.get(), this.aggrPeriod.start().addDays(-1)),
					this.mode,
					this.aggrPeriod.start().addDays(-1),
					this.isGetNextMonthData,
					this.isCalcAttendanceRate,
					this.isOverWriteOpt,
					this.forOverWriteListOpt,
					Optional.empty(),
					Optional.of(true));
			if (!aggrResultOpt.isPresent()) return emptyInfo;
			val aggrResult = aggrResultOpt.get();
			
			// 年休情報（期間終了日の翌日開始時点）を取得
			val asOfPeriodEnd = aggrResult.getAsOfPeriodEnd();
			
			// 取得内容をもとに年休情報を作成
			return this.createInfoFromRemainingData(asOfPeriodEnd.getGrantRemainingList(),
					asOfPeriodEnd.getMaxData());
		}

		// 締め開始日>=集計開始日　or 締め開始日がnull　の時
		
		// 「年休付与残数データ」を取得
		List<AnnualLeaveGrantRemaining> remainingDatas = new ArrayList<>();
		for (val grantRemainingData : this.grantRemainingDatas){
			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
			if (grantRemainingData.getGrantDate().after(closureStartOpt.get())) continue;
			if (grantRemainingData.getDeadline().before(closureStartOpt.get())) continue;
			remainingDatas.add(grantRemainingData);
		}
		
		// 「年休上限データ」を取得
		val annLeaMaxDataOpt = this.annLeaMaxDataRepo.get(this.employeeId);
		if (!annLeaMaxDataOpt.isPresent()) return emptyInfo;
		val annLeaMaxData = annLeaMaxDataOpt.get();

		// 取得内容をもとに年休情報を作成
		return this.createInfoFromRemainingData(remainingDatas, annLeaMaxData);
	}
	
	/**
	 * 年休付与残数データから年休情報を作成
	 * @param grantRemainingDataList 付与残数データリスト
	 * @param maxData 上限データ
	 * @return 年休情報
	 */
	private AnnualLeaveInfo createInfoFromRemainingData(
			List<AnnualLeaveGrantRemaining> grantRemainingDataList,
			AnnualLeaveMaxData maxData){
		
		AnnualLeaveInfo returnInfo = new AnnualLeaveInfo();
		returnInfo.setYmd(this.aggrPeriod.start());

		// 年休情報．年休付与情報　←　パラメータ「付与残数データ」
		List<AnnualLeaveGrantRemaining> targetDatas = new ArrayList<>();
		for (val grantRemainingData : grantRemainingDataList){
			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
			targetDatas.add(grantRemainingData);
		}
		targetDatas.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
		returnInfo.setGrantRemainingList(targetDatas);
		
		// 年休情報残数を更新
		returnInfo.updateRemainingNumber();
		
		// 年休情報を返す
		return returnInfo;
	}
	
	/**
	 * 年休集計期間を作成
	 * @param nextAnnualLeaveGrantList 次回年休付与リスト
	 * @return 年休集計期間WORKリスト
	 */
	private void createAggregatePeriod(List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList){
		
		this.aggregatePeriodWorks = new ArrayList<>();
		
		// 処理単位分割日リスト
		Map<GeneralDate, DividedDayEachProcess> dividedDayMap = new HashMap<>();
		
		// 期間終了日翌日
		GeneralDate nextDayOfPeriodEnd = this.aggrPeriod.end();
		if (nextDayOfPeriodEnd.before(GeneralDate.max())) nextDayOfPeriodEnd = nextDayOfPeriodEnd.addDays(1);
		
		// 「年休付与残数データ」を取得　（期限日　昇順、付与日　昇順）
		List<AnnualLeaveGrantRemainingData> remainingDatas = new ArrayList<>();
		remainingDatas.addAll(this.grantRemainingDatas);
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
			if (!this.aggrPeriod.contains(deadline)) continue;
			
			val nextDayOfDeadline = deadline.addDays(1);
			dividedDayMap.putIfAbsent(nextDayOfDeadline, new DividedDayEachProcess(nextDayOfDeadline));
			dividedDayMap.get(nextDayOfDeadline).setLapsedAtr(true);
		}
		
		// 「次回年休付与リスト」をすべて「処理単位分割日リスト」に追加
		for (val nextAnnualLeaveGrant : nextAnnualLeaveGrantList){
			val grantDate = nextAnnualLeaveGrant.getGrantDate();
			if (grantDate.before(this.aggrPeriod.start().addDays(1))) continue;
			if (grantDate.after(nextDayOfPeriodEnd)) continue;
			
			dividedDayMap.putIfAbsent(grantDate, new DividedDayEachProcess(grantDate));
			dividedDayMap.get(grantDate).setGrantAtr(true);
			dividedDayMap.get(grantDate).setNextAnnualLeaveGrant(Optional.of(nextAnnualLeaveGrant));
		}
		
		// 期間終了日翌日の「処理単位分割日」を取得・追加　→　フラグ設定
		dividedDayMap.putIfAbsent(nextDayOfPeriodEnd, new DividedDayEachProcess(nextDayOfPeriodEnd));
		dividedDayMap.get(nextDayOfPeriodEnd).setNextDayAfterPeriodEnd(true);
		
		// 「処理単位分割日」をソート
		List<DividedDayEachProcess> dividedDayList = new ArrayList<>();
		dividedDayList.addAll(dividedDayMap.values());
		dividedDayList.sort((a, b) -> a.getYmd().compareTo(b.getYmd()));
		
		// 「年休集計期間WORK」を作成
		AggregatePeriodWork startWork = new AggregatePeriodWork();
		val startWorkEnd = dividedDayList.get(0).getYmd().addDays(-1);
		startWork.setPeriod(new DatePeriod(this.aggrPeriod.start(), startWorkEnd));
		this.aggregatePeriodWorks.add(startWork);
		
		// 付与後フラグ
		boolean isAfterGrant = false;
		
		for (int index = 0; index < dividedDayList.size(); index++){
			val nowDividedDay = dividedDayList.get(index);
			DividedDayEachProcess nextDividedDay = null;
			if (index + 1 < dividedDayList.size()) nextDividedDay = dividedDayList.get(index + 1);
			
			// 付与フラグをチェック
			if (nowDividedDay.isGrantAtr()) isAfterGrant = true;
			
			// 年休集計期間WORKを作成し、Listに追加
			GeneralDate workPeriodEnd = nextDayOfPeriodEnd;
			if (nextDividedDay != null) workPeriodEnd = nextDividedDay.getYmd().addDays(-1);
			AggregatePeriodWork nowWork = AggregatePeriodWork.of(
					new DatePeriod(nowDividedDay.getYmd(), workPeriodEnd),
					nowDividedDay.isNextDayAfterPeriodEnd(),
					nowDividedDay.isGrantAtr(),
					isAfterGrant,
					nowDividedDay.isLapsedAtr(),
					nowDividedDay.getNextAnnualLeaveGrant());
			this.aggregatePeriodWorks.add(nowWork);
		}
	}
	
	/**
	 * 暫定年休管理データを取得する
	 * @return 暫定年休管理データWORKリスト
	 */
	private List<TmpAnnualLeaveMngWork> getTempAnnualLeaveMngs(){
		
		List<TmpAnnualLeaveMngWork> results = new ArrayList<>();
		
		// 「モード」をチェック
		if (this.mode == InterimRemainMngMode.MONTHLY){
			// 月次モード
			
			// 月別実績用の暫定残数管理データを作成する
//			val dailyInterimRemainMngDataMap = this.interimRemOffMonth.monthInterimRemainData(
//					this.companyId, this.employeeId, this.aggrPeriod);
			
			// 受け取った「日別暫定管理データ」を年休のみに絞り込む
//			for (val dailyInterimRemainMngData : dailyInterimRemainMngDataMap.values()){
//				if (!dailyInterimRemainMngData.getAnnualHolidayData().isPresent()) continue;
//				if (dailyInterimRemainMngData.getRecAbsData().size() <= 0) continue;
//				val master = dailyInterimRemainMngData.getRecAbsData().get(0);
//				val data = dailyInterimRemainMngData.getAnnualHolidayData().get();
//				results.add(TmpAnnualLeaveMngWork.of(master, data));
//			}
		}
		if (this.mode == InterimRemainMngMode.OTHER){
			// その他モード
			
			// 「暫定年休管理データ」を取得する
			val interimRemains = this.interimRemainRepo.getRemainBySidPriod(
					this.employeeId, this.aggrPeriod, RemainType.ANNUAL);
			for (val master : interimRemains){
				val tmpAnnualLeaveMngOpt = this.tmpAnnualLeaveMng.getById(master.getRemainManaID());
				if (!tmpAnnualLeaveMngOpt.isPresent()) continue;
				val data = tmpAnnualLeaveMngOpt.get();
				results.add(TmpAnnualLeaveMngWork.of(master, data));
			}
			
			// 年休フレックス補填分を暫定年休データに反映する
			{
				// 「月別実績の勤怠時間」を取得
				val attendanceTimes = this.attendanceTimeOfMonthlyRepo.findByPeriodIntoEndYmd(
						this.employeeId, this.aggrPeriod);
				for (val attendanceTime : attendanceTimes){
					
					// 月別実績の勤怠時間からフレックス補填の暫定年休管理データを作成する
					val compensFlexWorkOpt = this.createInterimAnnual.ofCompensFlexToWork(
							attendanceTime, attendanceTime.getDatePeriod().end());
					
					// 「暫定年休管理データ」を返す
					if (compensFlexWorkOpt.isPresent()) results.add(compensFlexWorkOpt.get());
				}
			}
		}
		
		// 「上書きフラグ」をチェック
		if (this.isOverWriteOpt.isPresent()){
			if (this.isOverWriteOpt.get()){
				
				// 上書き用データがある時、使用する
				if (this.forOverWriteListOpt.isPresent()){
					val overWrites = this.forOverWriteListOpt.get();
					for (val overWrite : overWrites){
						// 重複データを削除
						ListIterator<TmpAnnualLeaveMngWork> itrResult = results.listIterator();
						while (itrResult.hasNext()){
							TmpAnnualLeaveMngWork target = itrResult.next();
							if (target.equals(overWrite)) itrResult.remove();
						}
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
	 * 年休不足分として作成した年休付与データを削除する
	 * @param result 年休の集計結果
	 */
	private AggrResultOfAnnualLeave deleteDummyRemainingDatas(AggrResultOfAnnualLeave result){
		
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
}
