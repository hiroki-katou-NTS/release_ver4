package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleSidDto;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.GetVacationAddSet;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VacationAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 処理：暫定年休管理データを作成する
 * @author shuichu_ishida
 */
public class CreateTempAnnLeaMngProc {

	/** 日別実績の勤務情報 */
	private WorkInformationRepository workInformationRepo;
	/** 勤務予定基本情報 */
	private BasicScheduleAdapter basicScheduleAdapter;
	/** 暫定年休管理データ */
	private InterimRemainRepository tempAnnualLeaveMngRepo;
	/** 勤務情報の取得 */
	private WorkTypeRepository workTypeRepo;
	/** 休暇加算設定の取得 */
	private GetVacationAddSet getVacationAddSet;
	/** 月別実績の勤怠時間 */
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	/** 会社ID */
	private String companyId;
	/** 社員ID */
	private String employeeId;
	/** 期間 */
	private DatePeriod period;
	/** モード */
	private InterimRemainMngMode mode;
	
	/** 月別集計で必要な会社別設定 */
	private MonAggrCompanySettings companySets;
	/** 月の計算中の日別実績データ */
	private MonthlyCalculatingDailys monthlyCalculatingDailys;
	
	/** 日別実績の勤務情報リスト */
	private Map<GeneralDate, WorkInfoOfDailyPerformance> workInfoOfDailys;
	/** 勤務予定基本情報リスト **/
	private Map<GeneralDate, BasicScheduleSidDto> basicSchedules;
	/** 暫定年休管理データリスト */
	private List<InterimRemain> tempAnnualLeaveMngs;
	
	private List<TmpAnnualHolidayMng> holidayMngs;
	/** 勤務種類リスト */
	private Map<String, WorkType> workTypeMap;
	/** 休暇加算設定 */
	private VacationAddSet vacationAddSet;
	
	public CreateTempAnnLeaMngProc(
			WorkInformationRepository workInformationRepo,
			BasicScheduleAdapter basicScheduleAdapter,
			InterimRemainRepository tempAnnualLeaveMngRepo,
			WorkTypeRepository workTypeRepo,
			GetVacationAddSet getVacationAddSet,
			AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo) {
		
		this.workInformationRepo = workInformationRepo;
		this.basicScheduleAdapter = basicScheduleAdapter;
		this.tempAnnualLeaveMngRepo = tempAnnualLeaveMngRepo;
		this.workTypeRepo = workTypeRepo;
		this.getVacationAddSet = getVacationAddSet;
		this.attendanceTimeOfMonthlyRepo = attendanceTimeOfMonthlyRepo;
		
		this.workInfoOfDailys = new HashMap<>();
		this.basicSchedules = new HashMap<>();
		this.tempAnnualLeaveMngs = new ArrayList<>();
		this.workTypeMap = new HashMap<>();
	}
	
	/**
	 * 勤務種類の取得
	 * @param workTypeCode 勤務種類コード
	 * @return 勤務種類
	 */
	private WorkType getWorkType(String workTypeCode){
		
		if (this.workTypeMap.containsKey(workTypeCode)) return this.workTypeMap.get(workTypeCode);
		
		val workTypeOpt = this.workTypeRepo.findByPK(this.companyId, workTypeCode);
		if (workTypeOpt.isPresent()){
			this.workTypeMap.put(workTypeCode, workTypeOpt.get());
		}
		else {
			this.workTypeMap.put(workTypeCode, null);
		}
		return this.workTypeMap.get(workTypeCode);
	}

	/**
	 * 暫定年休管理データを作成する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param mode モード
	 * @return 暫定年休管理データリスト
	 */
	public List<InterimRemain> algorithm(
			String companyId,
			String employeeId,
			DatePeriod period,
			InterimRemainMngMode mode) {

		return this.algorithm(companyId, employeeId, period, mode, Optional.empty(), Optional.empty());
	}
	
	/**
	 * 暫定年休管理データを作成する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param mode モード
	 * @param companySets 月別集計で必要な会社別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @return 暫定年休管理データリスト
	 */
	public List<InterimRemain> algorithm(
			String companyId,
			String employeeId,
			DatePeriod period,
			InterimRemainMngMode mode,
			Optional<MonAggrCompanySettings> companySets,
			Optional<MonthlyCalculatingDailys> monthlyCalcDailys) {
		
		this.companyId = companyId;
		this.employeeId = employeeId;
		this.period = period;
		this.mode = mode;
		
		this.companySets = null;
		this.monthlyCalculatingDailys = null;
		if (companySets.isPresent()) this.companySets = companySets.get();
		if (monthlyCalcDailys.isPresent()) this.monthlyCalculatingDailys = monthlyCalcDailys.get();
		
		if (this.companySets != null){
			
			// 休暇加算設定　取得
			this.vacationAddSet = this.companySets.getVacationAddSet();

			// 勤務種類　取得
			this.workTypeMap = this.companySets.getAllWorkTypeMap();
		}
		else {
			
			// 休暇加算設定　取得
			this.vacationAddSet = this.getVacationAddSet.get(companyId);

			// 勤務種類　取得
			this.workTypeMap = new HashMap<>();
		}
		
		// 暫定データ作成用の勤務予定・勤務実績・申請を取得
		this.getSourceDataForCreate();
		
		// 暫定管理データを作成する
		this.createTempManagementData();
		
		//　「モード」をチェック
		if (mode == InterimRemainMngMode.OTHER){

			// 「暫定年休管理データ」をDELETEする
			this.tempAnnualLeaveMngRepo.removeByPeriod(employeeId, period);
			
			// 「暫定年休管理データ」をINSERTする
			for (val tempAnnualLeaveMng : this.tempAnnualLeaveMngs){
				this.tempAnnualLeaveMngRepo.persistAndUpdateInterimRemain(tempAnnualLeaveMng);
			}
			
			// 「個人残数更新フラグ管理．更新状況」を「更新済み」にUpdateする
			//*****（未）　設計待ち。
		}
		
		// 年休フレックス補填分を暫定年休管理データに反映する
		this.reflecttempManagementDataFromCompFlex();
		
		// 作成した暫定年休管理データを返す
		return this.tempAnnualLeaveMngs;
	}
	
	/**
	 * 暫定データ作成用の勤務予定・勤務実績・申請を取得
	 */
	private void getSourceDataForCreate(){
	
		// 「日別実績の勤務情報」を取得する　→　取得した「日別実績の勤務情報」を返す
		if (this.monthlyCalculatingDailys != null) {
			this.workInfoOfDailys = this.monthlyCalculatingDailys.getWorkInfoOfDailyMap();
		}
		else {
			val acquiredWorkInfos = this.workInformationRepo.findByPeriodOrderByYmd(this.employeeId, this.period);
			for (val acquiredWorkInfo : acquiredWorkInfos){
				val ymd = acquiredWorkInfo.getYmd();
				this.workInfoOfDailys.putIfAbsent(ymd, acquiredWorkInfo);
			}
		}
		
		// 「モード」をチェックする
		if (this.mode == InterimRemainMngMode.OTHER){

			// 社員．期間に未反映の申請を取得する　→　取得した「申請」を返す
			//*****（未）　RequestList依頼待ち。requestコンテキストの処理を呼んで、データを貰う。
		
			// 「勤務予定基本情報」を取得する　→　取得した「勤務予定基本情報」を返す
			//*****（未）　RequestList#141の完成待ち。仮に、今ある他の機能で代替。
			GeneralDate procDate = this.period.start();
			while (procDate.beforeOrEquals(this.period.end())){
				val basicScheduleSidOpt = this.basicScheduleAdapter.findAllBasicSchedule(
						this.employeeId, procDate);
				if (basicScheduleSidOpt.isPresent()){
					val basicScheduleSid = basicScheduleSidOpt.get();
					val ymd = basicScheduleSid.getDate();
					this.basicSchedules.putIfAbsent(ymd, basicScheduleSid);
				}
				procDate = procDate.addDays(1);
			}
		}
	}
	
	/**
	 * 暫定管理データを作成する
	 */
	private void createTempManagementData(){
		
		// 期間開始日～終了日まで1日づつループする
		for (GeneralDate procDate = this.period.start();
				procDate.beforeOrEquals(this.period.end()); procDate = procDate.addDays(1)){

			// 「日別実績の勤務情報」を取得する
			if (this.workInfoOfDailys.containsKey(procDate)){
				
				// 日別実績から暫定年休管理データを作成する
				this.createTempManagementDataFromDailyRecord(this.workInfoOfDailys.get(procDate));
				continue;
			}
			
			// 「勤務予定基本情報」を取得する
			if (this.basicSchedules.containsKey(procDate)){
				
				// 勤務予定から暫定年休管理データを作成する
				this.createTempManagementDataFromWorkSchedule(this.basicSchedules.get(procDate));
				continue;
			}
		}
	}
	
	/**
	 * 日別実績から暫定年休管理データを作成する
	 * @param workInfo 日別実績の勤務情報
	 */
	private void createTempManagementDataFromDailyRecord(WorkInfoOfDailyPerformance workInfo){
	
		// 勤務種類から年休の日数を取得
		val workTypeCode = workInfo.getRecordInfo().getWorkTypeCode();
		if (workTypeCode == null) return;
		val workType = this.getWorkType(workTypeCode.v());
		if (workType == null) return;
		val workTypeDaysCountTable = new WorkTypeDaysCountTable(workType, this.vacationAddSet, Optional.empty());
		
		val ymd = workInfo.getYmd();
		
		String annualId = IdentifierUtil.randomUniqueId();
		InterimRemain tempAnnualLeaveMng2 = new InterimRemain(this.employeeId, ymd, annualId);
		TmpAnnualHolidayMng annualHolidayMng = new TmpAnnualHolidayMng(annualId);
		
		val annualLeaveDays = workTypeDaysCountTable.getAnnualLeaveDays();
		if (annualLeaveDays.greaterThan(0.0)){
			
			// 暫定年休管理データを作成
			annualHolidayMng.setUseDays(new UseDay(annualLeaveDays.v()));
			annualHolidayMng.setWorkTypeCode(workTypeCode.v());
			tempAnnualLeaveMng2.setCreatorAtr(CreateAtr.RECORD);

			// 「暫定年休管理データ」を返す
			this.tempAnnualLeaveMngs.add(tempAnnualLeaveMng2);
			this.holidayMngs.add(annualHolidayMng);
		}
	}
	
	/**
	 * 勤務予定から暫定年休管理データを作成する
	 * @param basicSchedule 勤務予定基本情報
	 */
	private void createTempManagementDataFromWorkSchedule(BasicScheduleSidDto basicSchedule){
		
		// 勤務種類から年休の日数を取得
		val workTypeCode = new WorkTypeCode(basicSchedule.getWorkTypeCode());
		val workType = this.getWorkType(workTypeCode.v());
		if (workType == null) return;
		val workTypeDaysCountTable = new WorkTypeDaysCountTable(workType, this.vacationAddSet, Optional.empty());
		
		val ymd = basicSchedule.getDate();
		String annualId = IdentifierUtil.randomUniqueId();
		InterimRemain tempAnnualLeaveMng2 = new InterimRemain(this.employeeId, ymd, annualId);
		TmpAnnualHolidayMng annualHolidayMng = new TmpAnnualHolidayMng(annualId);
		
		val annualLeaveDays = workTypeDaysCountTable.getAnnualLeaveDays();
		if (annualLeaveDays.greaterThan(0.0)){
			
			// 暫定年休管理データを作成
			annualHolidayMng.setUseDays(new UseDay(annualLeaveDays.v()));
			annualHolidayMng.setWorkTypeCode(workTypeCode.v());
			tempAnnualLeaveMng2.setCreatorAtr(CreateAtr.SCHEDULE);

			// 「暫定年休管理データ」を返す
			this.tempAnnualLeaveMngs.add(tempAnnualLeaveMng2);
			this.holidayMngs.add(annualHolidayMng);
		}
	}
	
	/**
	 * 年休フレックス補填分を暫定年休管理データに反映する
	 */
	private void reflecttempManagementDataFromCompFlex(){

		// 「月別実績の勤怠時間」を取得
		val attendanceTimeOfMonthlys =
				this.attendanceTimeOfMonthlyRepo.findByDate(this.employeeId, this.period.end());
		for (val attendanceTimeOfMonthly : attendanceTimeOfMonthlys){
			val monthlyCalc = attendanceTimeOfMonthly.getMonthlyCalculation();
			
			// 「暫定年休管理データ」を作成
			InterimRemain tempAnnualLeaveMng2 = new InterimRemain(this.employeeId, attendanceTimeOfMonthly.getDatePeriod().end());
			TmpAnnualHolidayMng annualHolidayMng = new TmpAnnualHolidayMng();
			annualHolidayMng.setUseDays(new UseDay(
					monthlyCalc.getFlexTime().getFlexShortDeductTime().getAnnualLeaveDeductDays().v()));
			annualHolidayMng.setWorkTypeCode(new WorkTypeCode("DMY").v());
			tempAnnualLeaveMng2.setCreatorAtr(CreateAtr.RECORD);
			
			// 「暫定年休管理データ」に追加
			this.tempAnnualLeaveMngs.add(tempAnnualLeaveMng2);
			this.holidayMngs.add(annualHolidayMng);
		}
	}
}
