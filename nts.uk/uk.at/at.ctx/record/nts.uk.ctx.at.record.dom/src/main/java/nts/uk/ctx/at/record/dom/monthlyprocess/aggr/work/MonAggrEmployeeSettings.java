package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkPlaceSidImport;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaDeforLaborMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaFlexMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaRegulaMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別集計で必要な社員別設定
 * @author shuichi_ishida
 */
@Getter
@Setter
@NoArgsConstructor
public class MonAggrEmployeeSettings {

	/** 社員ID */
	private String employeeId;
	/** 社員 */
	private EmployeeImport employee;
	/** 所属雇用履歴 */
	private List<BsEmploymentHistoryImport> employments;
	/** 所属職場履歴 */
	private List<AffWorkPlaceSidImport> workplaces;
	/** 上位職場履歴 */
	private Map<String, List<String>> workPlacesToRoot;
	/** 社員別通常勤務労働時間 */
	private Optional<WorkingTimeSetting> regLaborTime;
	/** 社員別変形労働労働時間 */
	private Optional<WorkingTimeSetting> irgLaborTime;
	/** 通常勤務社員別月別実績集計設定 */
	private Optional<ShaRegulaMonthActCalSet> shaRegSetOpt;
	/** 変形労働社員別月別実績集計設定 */
	private Optional<ShaDeforLaborMonthActCalSet> shaIrgSetOpt;
	/** フレックス社員別月別実績集計設定 */
	private Optional<ShaFlexMonthActCalSet> shaFlexSetOpt;
	/** 年休社員基本情報 */
	private Optional<AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoOpt;
	/** 締め状態管理 */
	private List<ClosureStatusManagement> closureStatusMngs;
	/** 労働条件項目 */
	private List<WorkingConditionItem> workingConditionItems;
	/** 労働条件 */
	private Map<String, DatePeriod> workingConditions;
	
	/** 集計開始日を締め開始日とする */
	@Setter
	private boolean noCheckStartDate;
	/** エラー情報 */
	private Map<String, ErrMessageContent> errorInfos;
	
	private MonAggrEmployeeSettings(String employeeId){
		this.employeeId = employeeId;
		this.employee = null;
		this.employments = new ArrayList<>();
		this.workplaces = new ArrayList<>();
		this.workPlacesToRoot = new HashMap<>();
		this.regLaborTime = Optional.empty();
		this.irgLaborTime = Optional.empty();
		this.shaRegSetOpt = Optional.empty();
		this.shaIrgSetOpt = Optional.empty();
		this.shaFlexSetOpt = Optional.empty();
		this.annualLeaveEmpBasicInfoOpt = Optional.empty();
		this.closureStatusMngs = new ArrayList<>();
		this.workingConditionItems = new ArrayList<>();
		this.workingConditions = new HashMap<>();
		
		this.noCheckStartDate = true;
		this.errorInfos = new HashMap<>();
	}
	
	/**
	 * 設定読み込み
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月別集計で必要な社員別設定
	 */
	public static MonAggrEmployeeSettings loadSettings(
			String companyId,
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
	
		MonAggrEmployeeSettings domain = new MonAggrEmployeeSettings(employeeId);
		List<String> employeeIds = new ArrayList<>(Arrays.asList(employeeId));
		
		// 社員
		domain.employee = repositories.getEmpEmployee().findByEmpId(employeeId);
		if (domain.employee == null){
			domain.errorInfos.put("002", new ErrMessageContent(TextResource.localize("Msg_1156")));
			return domain;
		}
		
		// 取得用期間の確認　（過去1ヶ月、未来1ヶ月を配慮）
		GeneralDate findStart = GeneralDate.min();
		if (period.start().after(GeneralDate.min().addMonths(1))) findStart = period.start().addMonths(-1);
		GeneralDate findEnd = GeneralDate.max();
		if (period.end().before(GeneralDate.max().addMonths(-1))) findEnd = period.end().addMonths(1);
		DatePeriod findPeriod = new DatePeriod(findStart, findEnd);
		
		// 所属雇用履歴
		GeneralDate empCriteria = findPeriod.start();
		while (empCriteria.beforeOrEquals(findPeriod.end())){
			val employmentOpt = repositories.getEmployment().findEmploymentHistory(companyId, employeeId, empCriteria);
			if (employmentOpt.isPresent()){
				domain.employments.add(employmentOpt.get());
				empCriteria = employmentOpt.get().getPeriod().end();
				if (empCriteria.afterOrEquals(GeneralDate.max())) break;
				empCriteria = empCriteria.addDays(1);
			}
			else break;
		}
		
		// 所属職場履歴
		GeneralDate wkpCriteria = findPeriod.start();
		while (wkpCriteria.beforeOrEquals(findPeriod.end())){
			val workplaceOpt = repositories.getAffWorkplace().findBySidAndDate(employeeId, wkpCriteria);
			if (workplaceOpt.isPresent()){
				domain.workplaces.add(workplaceOpt.get());
				wkpCriteria = workplaceOpt.get().getDateRange().end();
				
				// 終了日時点の上位職場履歴
				val workplacesToRoot = repositories.getAffWorkplace().findAffiliatedWorkPlaceIdsToRoot(
						companyId, employeeId, wkpCriteria);
				domain.workPlacesToRoot.put(workplaceOpt.get().getWorkplaceId(), workplacesToRoot);
				
				// 次の履歴へ
				if (wkpCriteria.afterOrEquals(GeneralDate.max())) break;
				wkpCriteria = wkpCriteria.addDays(1);
			}
			else break;
		}
		
		// 社員別通常勤務労働時間
		val regWorkTime = repositories.getShainRegularWorkTime().find(companyId, employeeId);
		if (regWorkTime.isPresent()) domain.regLaborTime = Optional.of(regWorkTime.get().getWorkingTimeSet());
		
		// 社員別変形労働労働時間
		val irgWorkTime = repositories.getShainTransLaborTime().find(companyId, employeeId);
		if (irgWorkTime.isPresent()) domain.irgLaborTime = Optional.of(irgWorkTime.get().getWorkingTimeSet());
		
		// 通常勤務社員別月別実績集計設定
		domain.shaRegSetOpt = repositories.getShaRegSetRepo().find(companyId, employeeId);
		
		// 変形労働社員別月別実績集計設定
		domain.shaIrgSetOpt = repositories.getShaIrgSetRepo().find(companyId, employeeId);
		
		// フレックス社員別月別実績集計設定
		domain.shaFlexSetOpt = repositories.getShaFlexSetRepo().find(companyId, employeeId);
		
		// 年休社員基本情報
		domain.annualLeaveEmpBasicInfoOpt = repositories.getAnnLeaEmpBasicInfo().get(employeeId);
		
		// 締め処理状態
		domain.closureStatusMngs = repositories.getClosureStatusMng().getByIdListAndDatePeriod(employeeIds, period);
		
		// 「労働条件項目」を取得
		List<WorkingConditionItem> workingConditionItems = repositories.getWorkingConditionItem()
				.getBySidAndPeriodOrderByStrD(employeeId, period);
		if (!workingConditionItems.isEmpty()){
			// 同じ労働制の履歴を統合　と　「労働条件」の確認
			domain.IntegrateHistoryOfSameWorkSys(workingConditionItems, repositories);
		}
		
		return domain;
	}
	
	/**
	 * 同じ労働制の履歴を統合
	 * @param target 労働条件項目リスト　（統合前）
	 * @param repositories 月別集計が必要とするリポジトリ
	 */
	private void IntegrateHistoryOfSameWorkSys(
			List<WorkingConditionItem> target,
			RepositoriesRequiredByMonthlyAggr repositories){

		this.workingConditionItems = new ArrayList<>();
		this.workingConditions = new HashMap<>();
		
		val itrTarget = target.listIterator();
		while (itrTarget.hasNext()){
			
			// 要素[n]を取得
			WorkingConditionItem startItem = itrTarget.next();
			val startHistoryId = startItem.getHistoryId();
			val startConditionOpt = repositories.getWorkingCondition().getByHistoryId(startHistoryId);
			if (!startConditionOpt.isPresent()) continue;
			val startCondition = startConditionOpt.get();
			if (startCondition.getDateHistoryItem().isEmpty()) continue;
			DatePeriod startPeriod = startCondition.getDateHistoryItem().get(0).span();
			
			// 要素[n]と要素[n+1]以降を順次比較
			WorkingConditionItem endItem = null;
			while (itrTarget.hasNext()){
				WorkingConditionItem nextItem = target.get(itrTarget.nextIndex());
				if (startItem.getLaborSystem() != nextItem.getLaborSystem() ||
					startItem.getHourlyPaymentAtr() != nextItem.getHourlyPaymentAtr()){
					
					// 労働制または時給者区分が異なる履歴が見つかった時点で、労働条件の統合をやめる
					break;
				}
			
				// 労働制と時給者区分が同じ履歴の要素を順次取得
				endItem = itrTarget.next();
			}
			
			// 次の要素がなくなった、または、異なる履歴が見つかれば、集計要素を確定する
			if (endItem == null){
				this.workingConditionItems.add(startItem);
				this.workingConditions.putIfAbsent(startHistoryId, startPeriod);
				continue;
			}
			val endHistoryId = endItem.getHistoryId();
			val endConditionOpt = repositories.getWorkingCondition().getByHistoryId(endHistoryId);
			if (!endConditionOpt.isPresent()) continue;;
			val endCondition = endConditionOpt.get();
			if (endCondition.getDateHistoryItem().isEmpty()) continue;
			this.workingConditionItems.add(endItem);
			this.workingConditions.putIfAbsent(endHistoryId,
					new DatePeriod(startPeriod.start(), endCondition.getDateHistoryItem().get(0).end()));
		}
	}
	
	/**
	 * 所属雇用履歴の取得
	 * @param ymd 年月日
	 * @return 所属雇用履歴
	 */
	public Optional<BsEmploymentHistoryImport> getEmployment(GeneralDate ymd){
		return this.employments.stream().filter(c -> c.getPeriod().contains(ymd)).findFirst();
	}
	
	/**
	 * 所属職場履歴の取得
	 * @param ymd 年月日
	 * @return 所属職場履歴
	 */
	public Optional<AffWorkPlaceSidImport> getWorkplace(GeneralDate ymd){
		return this.workplaces.stream().filter(c -> c.getDateRange().contains(ymd)).findFirst();
	}
	
	/**
	 * 所属職場履歴の取得
	 * @param ymd 年月日
	 * @return 所属職場履歴
	 */
	public List<String> getWorkplacesToRoot(GeneralDate ymd){
		val workplaceOpt = this.workplaces.stream().filter(c -> c.getDateRange().contains(ymd)).findFirst();
		if (!workplaceOpt.isPresent()) return new ArrayList<>();
		val workplaceId = workplaceOpt.get().getWorkplaceId();
		if (!this.workPlacesToRoot.containsKey(workplaceId)) return new ArrayList<>();
		return this.workPlacesToRoot.get(workplaceId);
	}
	
	/**
	 * 処理する期間が締められているかチェックする
	 * @param criteria 基準日
	 * @return true：締められている、false：締められていない
	 */
	public boolean checkClosedMonth(GeneralDate criteria){
		for (val closureStatusMng : this.closureStatusMngs){
			if (!closureStatusMng.getPeriod().contains(criteria)) continue;
			return true;
		}
		return false;
	}
	
	/**
	 * 労働条件項目の取得
	 * @param ymd 年月日
	 * @return 労働条件項目
	 */
	public Optional<WorkingConditionItem> getWorkingConditionItem(GeneralDate ymd){
		for (val workingConditionItem : this.workingConditionItems) {
			val historyId = workingConditionItem.getHistoryId();
			if (!this.workingConditions.containsKey(historyId)) continue;
			if (this.workingConditions.get(historyId).contains(ymd)) return Optional.of(workingConditionItem);
		}
		return Optional.empty();
	}
}
