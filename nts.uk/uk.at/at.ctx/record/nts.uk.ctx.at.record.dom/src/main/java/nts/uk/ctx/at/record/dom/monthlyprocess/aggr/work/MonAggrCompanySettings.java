package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VacationAddSet;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.VerticalTotalMethodOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.workform.flex.MonthlyAggrSetOfFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthly;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.record.dom.optitem.calculation.disporder.FormulaDispOrder;
import nts.uk.ctx.at.record.dom.standardtime.AgreementOperationSetting;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLock;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComDeforLaborMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComFlexMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComRegulaMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.FlexShortageLimit;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.InsufficientFlexHolidayMnt;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.outsideot.UseClassification;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerform;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmptYearlyRetentionSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWork;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureClassification;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.flex.GetFlexPredWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別集計で必要な会社別設定
 * @author shuichi_ishida
 */
@Setter
@NoArgsConstructor
public class MonAggrCompanySettings {

	/** 会社ID */
	@Getter
	private String companyId;
	/** 勤務種類 */
	private ConcurrentMap<String, Object> workTypeMap;
	/** 就業時間帯：共通設定 */
	private ConcurrentMap<String, Object> workTimeCommonSetMap;
	/** 所定時間設定 */
	private ConcurrentMap<String, Object> predetermineTimeSetMap;
	/** 締め */
	@Getter
	private ConcurrentMap<Integer, Closure> closureMap;
	/** 締め当月の期間 */
	@Getter
	private ConcurrentMap<Integer, DatePeriod> currentMonthPeriodMap;
	/** 法定内振替順設定 */
	@Getter
	private LegalTransferOrderSetOfAggrMonthly legalTransferOrderSet;
	/** 残業枠の役割 */
	@Getter
	private CopyOnWriteArrayList<RoleOvertimeWork> roleOverTimeFrameList;
	/** 休出枠の役割 */
	@Getter
	private CopyOnWriteArrayList<RoleOfOpenPeriod> roleHolidayWorkFrameList;
	/** 休暇時間加算設定 */
	@Getter
	private ConcurrentMap<String, AggregateRoot> holidayAdditionMap;
	/** 労働時間と日数の設定の利用単位の設定 */
	@Getter
	private UsageUnitSetting usageUnitSet;
	/** 会社別通常勤務労働時間 */
	@Getter
	private WorkingTimeSetting comRegLaborTime;
	/** 会社別変形労働労働時間 */
	@Getter
	private WorkingTimeSetting comIrgLaborTime;
	/** 雇用別通常勤務労働時間 */
	private ConcurrentMap<String, Object> empRegLaborTimeMap;
	/** 雇用別変形労働労働時間 */
	private ConcurrentMap<String, Object> empIrgLaborTimeMap;
	/** 職場別通常勤務労働時間 */
	private ConcurrentMap<String, Object> wkpRegLaborTimeMap;
	/** 職場別変形労働労働時間 */
	private ConcurrentMap<String, Object> wkpIrgLaborTimeMap;
	/** 通常勤務会社別月別実績集計設定 */
	@Getter
	private Optional<ComRegulaMonthActCalSet> comRegSetOpt;
	/** 変形労働会社別月別実績集計設定 */
	@Getter
	private Optional<ComDeforLaborMonthActCalSet> comIrgSetOpt;
	/** フレックス会社別月別実績集計設定 */
	@Getter
	private Optional<ComFlexMonthActCalSet> comFlexSetOpt;
	/** フレックス勤務の月別集計設定 */
	@Getter
	private MonthlyAggrSetOfFlex aggrSetOfFlex;
	/** フレックス勤務所定労働時間 */
	@Getter
	private GetFlexPredWorkTime flexPredWorkTime;
	/** フレックス不足の年休補填管理 */
	@Getter
	private Optional<InsufficientFlexHolidayMnt> insufficientFlexOpt;
	/** フレックス不足の繰越上限管理 */
	@Getter
	private Optional<FlexShortageLimit> flexShortageLimitOpt;
	/** 休暇加算設定 */
	@Getter
	private VacationAddSet vacationAddSet;
	/** 時間外超過設定 */
	@Getter
	private OutsideOTSetting outsideOverTimeSet;
	/** 時間外超過設定：内訳項目一覧（積上番号順） */
	@Getter
	private CopyOnWriteArrayList<OutsideOTBRDItem> outsideOTBDItems;
	/** 時間外超過設定：超過時間一覧（超過時間順） */
	@Getter
	private CopyOnWriteArrayList<Overtime> outsideOTOverTimes;
	/** 丸め設定 */
	@Getter
	private RoundingSetOfMonthly roundingSet;
	/** 回数集計 */
	@Getter
	private CopyOnWriteArrayList<TotalTimes> totalTimesList;
	/** 月別実績の給与項目カウント */
	@Getter
	private PayItemCountOfMonthly payItemCount;
	/** 月別実績の縦計方法 */
	@Getter
	private VerticalTotalMethodOfMonthly verticalTotalMethod;
	/** 36協定運用設定 */
	@Getter
	private Optional<AgreementOperationSetting> agreementOperationSet;
	/** 任意項目 */
	@Getter
	private ConcurrentMap<Integer, OptionalItem> optionalItemMap;
	/** 適用する雇用条件 */
	@Getter
	private ConcurrentMap<Integer, EmpCondition> empConditionMap;
	/** 計算式 */
	@Getter
	private CopyOnWriteArrayList<Formula> formulaList;
	/** 計算式の並び順 */
	@Getter
	private CopyOnWriteArrayList<FormulaDispOrder> formulaOrderList;
	/** 年休設定 */
	@Getter
	private AnnualPaidLeaveSetting annualLeaveSet;
	/** 年休付与テーブル設定 */
	@Getter
	private ConcurrentMap<String, GrantHdTblSet> grantHdTblSetMap;
	/** 勤続年数テーブル */
	@Getter
	private ConcurrentMap<String, List<LengthServiceTbl>> lengthServiceTblListMap;
	/** 積立年休設定 */
	@Getter
	private Optional<RetentionYearlySetting> retentionYearlySet;
	/** 雇用積立年休設定 */
	@Getter
	private ConcurrentHashMap<String, EmptYearlyRetentionSetting> emptYearlyRetentionSetMap;
	/** 振休管理設定 */
	@Getter
	private Optional<ComSubstVacation> absSettingOpt;
	/** 代休管理設定 */
	@Getter
	private CompensatoryLeaveComSetting dayOffSetting;
	/** 実績ロック */
	private ConcurrentMap<Integer, ActualLock> actualLockMap;
	/** 日別実績の運用開始設定 */
	@Getter
	private Optional<OperationStartSetDailyPerform> operationStartSet;
	
	/** エラー情報 */
	@Getter
	private ConcurrentMap<String, ErrMessageContent> errorInfos;
	
	private static Object NullObject = new Object();
	
	private MonAggrCompanySettings(String companyId){
		this.companyId = companyId;
		this.workTypeMap = new ConcurrentHashMap<>();
		this.workTimeCommonSetMap = new ConcurrentHashMap<>();
		this.predetermineTimeSetMap = new ConcurrentHashMap<>();
		this.closureMap = new ConcurrentHashMap<>();
		this.currentMonthPeriodMap = new ConcurrentHashMap<>();
		this.roleOverTimeFrameList = new CopyOnWriteArrayList<>();
		this.roleHolidayWorkFrameList = new CopyOnWriteArrayList<>();
		this.holidayAdditionMap = new ConcurrentHashMap<>();
		this.empRegLaborTimeMap = new ConcurrentHashMap<>();
		this.empIrgLaborTimeMap = new ConcurrentHashMap<>();
		this.wkpRegLaborTimeMap = new ConcurrentHashMap<>();
		this.wkpIrgLaborTimeMap = new ConcurrentHashMap<>();
		this.comRegSetOpt = Optional.empty();
		this.comIrgSetOpt = Optional.empty();
		this.comFlexSetOpt = Optional.empty();
		this.insufficientFlexOpt = Optional.empty();
		this.flexShortageLimitOpt = Optional.empty();
		this.outsideOTBDItems = new CopyOnWriteArrayList<>();
		this.outsideOTOverTimes = new CopyOnWriteArrayList<>();
		this.totalTimesList = new CopyOnWriteArrayList<>();
		this.agreementOperationSet = Optional.empty();
		this.optionalItemMap = new ConcurrentHashMap<>();
		this.empConditionMap = new ConcurrentHashMap<>();
		this.formulaList = new CopyOnWriteArrayList<>();
		this.formulaOrderList = new CopyOnWriteArrayList<>();
		this.grantHdTblSetMap = new ConcurrentHashMap<>();
		this.lengthServiceTblListMap = new ConcurrentHashMap<>();
		this.retentionYearlySet = Optional.empty();
		this.emptYearlyRetentionSetMap = new ConcurrentHashMap<>();
		this.absSettingOpt = Optional.empty();
		this.dayOffSetting = null;
		this.actualLockMap = new ConcurrentHashMap<>();
		this.operationStartSet = Optional.empty();
		this.errorInfos = new ConcurrentHashMap<>();
	}
	
	/**
	 * 設定読み込み
	 * @param companyId 会社ID
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月別集計で必要な会社別設定
	 */
	public static MonAggrCompanySettings loadSettings(
			String companyId,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		MonAggrCompanySettings domain = new MonAggrCompanySettings(companyId);
		
		// 月別実績の給与項目カウント　取得
		domain.payItemCount = new PayItemCountOfMonthly(companyId);
		val payItemCountOpt = repositories.getPayItemCountOfMonthly().find(companyId);
		if (payItemCountOpt.isPresent()) domain.payItemCount = payItemCountOpt.get();
		
		// 月別実績の縦計方法　取得
		//*****（未）　設計待ち。特定日設定部分が保留中。仮に空条件で。
		domain.verticalTotalMethod = new VerticalTotalMethodOfMonthly(companyId);
		
		// 任意項目
		val optionalItems = repositories.getOptionalItem().findAll(companyId);
		List<Integer> optionalItemNoList = new ArrayList<>();
		for (val optionalItem : optionalItems){
			domain.optionalItemMap.put(optionalItem.getOptionalItemNo().v(), optionalItem);
			optionalItemNoList.add(optionalItem.getOptionalItemNo().v());
		}
		
		// 適用する雇用条件
		val empConditions = repositories.getEmpCondition().findAll(companyId, optionalItemNoList);
		for (val empCondifion : empConditions){
			domain.empConditionMap.put(empCondifion.getOptItemNo().v(), empCondifion);
		}
		
		// 計算式
		domain.formulaList.addAll(repositories.getFormula().find(companyId));
		
		// 計算式の並び順
		domain.formulaOrderList.addAll(repositories.getFormulaOrder().findAll(companyId));
		
		// 年休設定
		domain.annualLeaveSet = repositories.getAnnualPaidLeaveSet().findByCompanyId(companyId);

		// 年休付与テーブル設定、勤続年数テーブル
		val yearHolidays = repositories.getYearHoliday().findAll(companyId);
		for (val yearHoliday : yearHolidays){
			val yearHolidayCode = yearHoliday.getYearHolidayCode().v();
			domain.grantHdTblSetMap.put(yearHolidayCode, yearHoliday);
			domain.lengthServiceTblListMap.put(yearHolidayCode,
					repositories.getLengthService().findByCode(companyId, yearHolidayCode));
		}
		
		// 積立年休設定
		domain.retentionYearlySet = repositories.getRetentionYearlySet().findByCompanyId(companyId);
		
		// 雇用積立年休設定
		val emptYearlyRetentionSets = repositories.getEmploymentSet().findAll(companyId);
		for (val emptYearlyRetentionSet : emptYearlyRetentionSets){
			val employmentCode = emptYearlyRetentionSet.getEmploymentCode();
			domain.emptYearlyRetentionSetMap.put(employmentCode, emptYearlyRetentionSet);
		}
		
		// 振休管理設定
		domain.absSettingOpt = repositories.getSubstVacationMng().findById(companyId);
		
		// 代休管理設定
		domain.dayOffSetting = repositories.getCompensLeaveMng().find(companyId);
		
		// 実績ロック
		val actualLocks = repositories.getActualLock().findAll(companyId);
		for (val actualLock : actualLocks){
			Integer closureId = actualLock.getClosureId().value;
			domain.actualLockMap.put(closureId, actualLock);
		}
		
		// 日別実績の運用開始設定
		domain.operationStartSet = repositories.getOperationStartSet().findByCid(new CompanyId(companyId));
		
		// 設定読み込み処理　（36協定時間用）
		domain.loadSettingsForAgreementProc(companyId, repositories);
		
		return domain;
	}
	
	/**
	 * 設定読み込み　（36協定時間用）
	 * @param companyId 会社ID
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月別集計で必要な会社別設定
	 */
	public static MonAggrCompanySettings loadSettingsForAgreement(
			String companyId,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		MonAggrCompanySettings domain = new MonAggrCompanySettings(companyId);

		// 設定読み込み処理　（36協定時間用）
		domain.loadSettingsForAgreementProc(companyId, repositories);
		
		return domain;
	}
	
	/**
	 * 設定読み込み処理　（36協定時間用）
	 * @param companyId 会社ID
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月別集計で必要な会社別設定
	 */
	private void loadSettingsForAgreementProc(
			String companyId,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 締め
		val closures = repositories.getClosure().findAllUse(companyId);
		for (val closure : closures){
			val closureId = closure.getClosureId().value;
			this.closureMap.putIfAbsent(closureId, closure);
			
			// 締め当月の期間
			val currentMonth = closure.getClosureMonth();
			if (currentMonth == null) continue;
			if (currentMonth.getProcessingYm() == null) continue;
			val currentPeriods = closure.getPeriodByYearMonth(currentMonth.getProcessingYm());
			val nextPeriods = closure.getPeriodByYearMonth(currentMonth.getProcessingYm().addMonths(1));
			if (currentPeriods.size() <= 0) continue;
			// ※　当月期間が2つある時は、2つめが当月。翌月が2つある時は、その1つめが当月の後期になる。
			val currentPeriod = currentPeriods.get(currentPeriods.size() - 1);
			boolean isMultiPeriod = (nextPeriods.size() > 1);
			if (isMultiPeriod){
				if (currentMonth.getClosureClassification().isPresent()){
					if (currentMonth.getClosureClassification().get() ==
							ClosureClassification.ClassificationClosingBefore){
						// 締め日変更前期間の時、当月期間そのまま
						this.currentMonthPeriodMap.put(closureId, currentPeriod);
					}
					else {
						// 締め日変更後期間の時、翌月の1つめの期間が当月
						if (nextPeriods.size() > 0){
							this.currentMonthPeriodMap.put(closureId, nextPeriods.get(0));
						}
					}
				}
				else {
					// 締め変更区分がない時、当月期間そのまま
					this.currentMonthPeriodMap.put(closureId, currentPeriod);
				}
			}
			else {
				// 締め期間が複数に分かれていない時、当月期間そのまま
				this.currentMonthPeriodMap.put(closureId, currentPeriod);
			}
		}
		
		// 法定内振替順設定
		val legalTransferOrderSetOpt = repositories.getLegalTransferOrderSetOfAggrMonthly().find(companyId);
		if (!legalTransferOrderSetOpt.isPresent()){
			this.errorInfos.put("009", new ErrMessageContent(TextResource.localize("Msg_1232")));
		}
		else {
			this.legalTransferOrderSet = legalTransferOrderSetOpt.get();
		}

		// 残業枠の役割
		this.roleOverTimeFrameList.addAll(repositories.getRoleOverTimeFrame().findByCID(companyId));

		// 休出枠の役割
		this.roleHolidayWorkFrameList.addAll(repositories.getRoleHolidayWorkFrame().findByCID(companyId));
		
		// 休暇時間加算設定
		for (val holidayAddition : repositories.getHolidayAddition().findByCompanyId(companyId).entrySet()){
			if (holidayAddition.getValue() == null) continue;
			this.holidayAdditionMap.put(holidayAddition.getKey(), holidayAddition.getValue());
		}
		
		// 労働時間と日数の設定の利用単位の設定
		this.usageUnitSet = new UsageUnitSetting(new CompanyId(companyId), false, false, false);
		val usagaUnitSetOpt = repositories.getUsageUnitSetRepo().findByCompany(companyId);
		if (usagaUnitSetOpt.isPresent()) this.usageUnitSet = usagaUnitSetOpt.get();
		
		// 会社別通常勤務労働時間
		val comRegLaborTime = repositories.getComRegularLaborTime().find(companyId);
		if (comRegLaborTime.isPresent()) this.comRegLaborTime = comRegLaborTime.get().getWorkingTimeSet();
		
		// 会社別変形労働労働時間
		val comIrgLaborTime = repositories.getComTransLaborTime().find(companyId);
		if (comIrgLaborTime.isPresent()) this.comIrgLaborTime = comIrgLaborTime.get().getWorkingTimeSet();
		
		// 通常勤務会社別月別実績集計設定
		this.comRegSetOpt = repositories.getComRegSetRepo().find(companyId);
		
		// 変形労働会社別月別実績集計設定
		this.comIrgSetOpt = repositories.getComIrgSetRepo().find(companyId);
		
		// フレックス会社別月別実績集計設定
		this.comFlexSetOpt = repositories.getComFlexSetRepo().find(companyId);
		
		// フレックス勤務の月別集計設定
		val aggrSetOfFlexOpt = repositories.getMonthlyAggrSetOfFlex().find(companyId);
		if (!aggrSetOfFlexOpt.isPresent()){
			this.errorInfos.put("011", new ErrMessageContent(TextResource.localize("Msg_1238")));
		}
		else {
			this.aggrSetOfFlex = aggrSetOfFlexOpt.get();
		}

		// フレックス勤務所定労働時間
		val flexPredWorkTimeOpt = repositories.getFlexPredWorktime().find(companyId);
		if (!flexPredWorkTimeOpt.isPresent()){
			this.errorInfos.put("016", new ErrMessageContent(TextResource.localize("Msg_1243")));
		}
		else {
			this.flexPredWorkTime = flexPredWorkTimeOpt.get();
		}

		// フレックス不足の年休補填管理
		this.insufficientFlexOpt = repositories.getInsufficientFlex().findByCId(companyId);
		
		// フレックス不足の繰越上限管理
		this.flexShortageLimitOpt = repositories.getFlexShortageLimit().get(companyId);
		
		// 休暇加算設定
		this.vacationAddSet = repositories.getVacationAddSet().get(companyId);
		
		// 時間外超過設定
		val outsideOTSetOpt = repositories.getOutsideOTSet().findById(companyId);
		if (!outsideOTSetOpt.isPresent()){
			this.errorInfos.put("014", new ErrMessageContent(TextResource.localize("Msg_1236")));
		}
		else {
			this.outsideOverTimeSet = outsideOTSetOpt.get();
			
			// 時間外超過設定：内訳項目一覧（積上番号順）
			this.outsideOTBDItems.addAll(this.outsideOverTimeSet.getBreakdownItems());
			this.outsideOTBDItems.removeIf(a -> { return a.getUseClassification() != UseClassification.UseClass_Use; });
			this.outsideOTBDItems.sort((a, b) -> a.getProductNumber().value - b.getProductNumber().value);
			
			// 時間外超過設定：超過時間一覧（超過時間順）
			this.outsideOTOverTimes.addAll(this.outsideOverTimeSet.getOvertimes());
			this.outsideOTOverTimes.removeIf(a -> { return a.getUseClassification() != UseClassification.UseClass_Use; });
			this.outsideOTOverTimes.sort((a, b) -> a.getOvertime().v() - b.getOvertime().v());
		}
		
		// 丸め設定
		this.roundingSet = new RoundingSetOfMonthly(companyId);
		val roundingSetOpt = repositories.getRoundingSetOfMonthly().find(companyId);
		if (roundingSetOpt.isPresent()) {
			this.roundingSet = roundingSetOpt.get();
		}
		else {
			this.errorInfos.put("013", new ErrMessageContent(TextResource.localize("Msg_1239")));
		}
		
		// 回数集計
		this.totalTimesList.addAll(repositories.getTotalTimes().getAllTotalTimes(companyId));
		if (this.totalTimesList.size() <= 0){
			this.errorInfos.put("020", new ErrMessageContent(TextResource.localize("Msg_1416")));
		}
		
		// 36協定運用設定を取得
		this.agreementOperationSet = repositories.getAgreementOperationSet().find(companyId);
		if (!this.agreementOperationSet.isPresent()){
			this.errorInfos.put("017", new ErrMessageContent(TextResource.localize("Msg_1246")));
		}
	}
	
	/**
	 * 勤務種類の取得
	 * @param workTypeCode 勤務種類コード
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 勤務種類　（なければ、null）
	 */
	public WorkType getWorkTypeMap(
			String workTypeCode,
			RepositoriesRequiredByMonthlyAggr repositories){
	
		if (this.workTypeMap.containsKey(workTypeCode)){
			val result = this.workTypeMap.get(workTypeCode);
			if (result == NullObject) return null;
			return (WorkType)result;
		}
		
		val workTypeOpt = repositories.getWorkType().findByPK(this.companyId, workTypeCode);
		if (!workTypeOpt.isPresent()){
			this.workTypeMap.put(workTypeCode, NullObject);
			return null;
		}
		this.workTypeMap.put(workTypeCode, workTypeOpt.get());
		return (WorkType)this.workTypeMap.get(workTypeCode);
	}

	/**
	 * 現在の全ての勤務種類の取得
	 * @return 勤務種類マップ
	 */
	public Map<String, WorkType> getAllWorkTypeMap(){
		Map<String, WorkType> results = new HashMap<>();
		for (val entry : this.workTypeMap.entrySet()){
			WorkType value = null;
			if (entry.getValue() != NullObject) value = (WorkType)entry.getValue();
			results.put(entry.getKey(), value);
		}
		return results;
	}
	
	/**
	 * 就業時間帯：共通設定の取得
	 * @param workTimeCode 就業時間帯コード
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 就業時間帯：共通設定　（なければ、null）
	 */
	public WorkTimezoneCommonSet getWorkTimeCommonSetMap(
			String workTimeCode,
			RepositoriesRequiredByMonthlyAggr repositories){
	
		if (this.workTimeCommonSetMap.containsKey(workTimeCode)){
			val result = this.workTimeCommonSetMap.get(workTimeCode);
			if (result == NullObject) return null;
			return (WorkTimezoneCommonSet)result;
		}
		
		val workTimeCommonSetOpt = repositories.getCommonSet().get(this.companyId, workTimeCode);
		if (!workTimeCommonSetOpt.isPresent()){
			this.workTimeCommonSetMap.put(workTimeCode, NullObject);
			return null;
		}
		this.workTimeCommonSetMap.put(workTimeCode, workTimeCommonSetOpt.get());
		return (WorkTimezoneCommonSet)this.workTimeCommonSetMap.get(workTimeCode);
	}

	/**
	 * 所定時間設定の取得
	 * @param workTimeCode 就業時間帯コード
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 所定時間設定　（なければ、null）
	 */
	public PredetemineTimeSetting getPredetemineTimeSetMap(
			String workTimeCode,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		if (this.predetermineTimeSetMap.containsKey(workTimeCode)){
			val result = this.predetermineTimeSetMap.get(workTimeCode);
			if (result == NullObject) return null;
			return (PredetemineTimeSetting)result;
		}
		
		val predetermineTimeSetOpt = repositories.getPredetermineTimeSet().findByWorkTimeCode(this.companyId, workTimeCode);
		if (!predetermineTimeSetOpt.isPresent()){
			this.predetermineTimeSetMap.put(workTimeCode, NullObject);
			return null;
		}
		this.predetermineTimeSetMap.put(workTimeCode, predetermineTimeSetOpt.get());
		return (PredetemineTimeSetting)this.predetermineTimeSetMap.get(workTimeCode);
	}
	
	/**
	 * 実績ロックされているか判定する　（月別用）
	 * @param baseDate 基準日
	 * @param closureId 締めID
	 * @return 実績のロック状態　（ロックorアンロック）
	 */
	public LockStatus getDetermineActualLocked(GeneralDate baseDate, Integer closureId){
		
		LockStatus currentLockStatus = LockStatus.UNLOCK;
		
		// 「実績ロック」を取得する
		if (!this.actualLockMap.containsKey(closureId)) return LockStatus.UNLOCK;
		ActualLock actualLock = this.actualLockMap.get(closureId);
		if (actualLock == null) return LockStatus.UNLOCK;
		
		// 月のロック状態を判定する
		currentLockStatus = actualLock.getMonthlyLockState();
		
		// ロック状態をチェックする
		if (currentLockStatus == LockStatus.UNLOCK) return LockStatus.UNLOCK;
		
		// 基準日が当月に含まれているかチェックする
		if (!this.currentMonthPeriodMap.containsKey(closureId)) return LockStatus.UNLOCK;
		DatePeriod currentPeriod = this.currentMonthPeriodMap.get(closureId);
		if (currentPeriod == null) return LockStatus.UNLOCK;
		if (currentPeriod.contains(baseDate)) {
			// 基準日が締め期間に含まれている
			return LockStatus.LOCK;
		}
		//基準日が締め期間に含まれていない
		return LockStatus.UNLOCK;
	}
	
	/**
	 * 労働時間の取得
	 * @param employmentCd 雇用コード
	 * @param workplaceIds 上位職場含む職場コードリスト
	 * @param workingSystem 労働制
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 労働時間
	 */
	public Optional<WorkingTimeSetting> getWorkingTimeSetting(
			String employmentCd,
			List<String> workplaceIds,
			WorkingSystem workingSystem,
			MonAggrEmployeeSettings employeeSets,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 通常勤務
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			if (this.usageUnitSet.isEmployee()){
				if (employeeSets.getRegLaborTime().isPresent()){
					return Optional.of(employeeSets.getRegLaborTime().get());
				}
			}
			if (this.usageUnitSet.isWorkPlace()){
				for (val workplaceId : workplaceIds){
					if (this.wkpRegLaborTimeMap.containsKey(workplaceId)){
						if (this.wkpRegLaborTimeMap.get(workplaceId) != NullObject){
							return Optional.of((WorkingTimeSetting)this.wkpRegLaborTimeMap.get(workplaceId));
						}
					}
					else {
						val wkpLaborTimeOpt = repositories.getWkpRegularLaborTime().find(this.companyId, workplaceId);
						if (wkpLaborTimeOpt.isPresent()){
							this.wkpRegLaborTimeMap.put(workplaceId, wkpLaborTimeOpt.get().getWorkingTimeSet());
							return Optional.of((WorkingTimeSetting)this.wkpRegLaborTimeMap.get(workplaceId));
						}
						else {
							this.wkpRegLaborTimeMap.put(workplaceId, NullObject);
						}
					}
				}
			}
			if (this.usageUnitSet.isEmployment()){
				if (this.empRegLaborTimeMap.containsKey(employmentCd)){
					if (this.empRegLaborTimeMap.get(employmentCd) != NullObject){
						return Optional.of((WorkingTimeSetting)this.empRegLaborTimeMap.get(employmentCd));
					}
				}
				else {
					val empLaborTimeOpt = repositories.getEmpRegularWorkTime().findById(this.companyId, employmentCd);
					if (empLaborTimeOpt.isPresent()){
						this.empRegLaborTimeMap.put(employmentCd, empLaborTimeOpt.get().getWorkingTimeSet());
						return Optional.of((WorkingTimeSetting)this.empRegLaborTimeMap.get(employmentCd));
					}
					else {
						this.empRegLaborTimeMap.put(employmentCd, NullObject);
					}
				}
			}
			return Optional.ofNullable(this.comRegLaborTime);
		}
		
		// 変形労働
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			if (this.usageUnitSet.isEmployee()){
				if (employeeSets.getIrgLaborTime().isPresent()){
					return Optional.of(employeeSets.getIrgLaborTime().get());
				}
			}
			if (this.usageUnitSet.isWorkPlace()){
				for (val workplaceId : workplaceIds){
					if (this.wkpIrgLaborTimeMap.containsKey(workplaceId)){
						if (this.wkpIrgLaborTimeMap.get(workplaceId) != NullObject){
							return Optional.of((WorkingTimeSetting)this.wkpIrgLaborTimeMap.get(workplaceId));
						}
					}
					else {
						val wkpLaborTimeOpt = repositories.getWkpTransLaborTime().find(this.companyId, workplaceId);
						if (wkpLaborTimeOpt.isPresent()){
							this.wkpIrgLaborTimeMap.put(workplaceId, wkpLaborTimeOpt.get().getWorkingTimeSet());
							return Optional.of((WorkingTimeSetting)this.wkpIrgLaborTimeMap.get(workplaceId));
						}
						else {
							this.wkpIrgLaborTimeMap.put(workplaceId, NullObject);
						}
					}
				}
			}
			if (this.usageUnitSet.isEmployment()){
				if (this.empIrgLaborTimeMap.containsKey(employmentCd)){
					if (this.empIrgLaborTimeMap.get(employmentCd) != NullObject){
						return Optional.of((WorkingTimeSetting)this.empIrgLaborTimeMap.get(employmentCd));
					}
				}
				else {
					val empLaborTimeOpt = repositories.getEmpTransWorkTime().find(this.companyId, employmentCd);
					if (empLaborTimeOpt.isPresent()){
						this.empIrgLaborTimeMap.put(employmentCd, empLaborTimeOpt.get().getWorkingTimeSet());
						return Optional.of((WorkingTimeSetting)this.empIrgLaborTimeMap.get(employmentCd));
					}
					else {
						this.empIrgLaborTimeMap.put(employmentCd, NullObject);
					}
				}
			}
			return Optional.ofNullable(this.comIrgLaborTime);
		}
		
		return Optional.empty();
	}
}
