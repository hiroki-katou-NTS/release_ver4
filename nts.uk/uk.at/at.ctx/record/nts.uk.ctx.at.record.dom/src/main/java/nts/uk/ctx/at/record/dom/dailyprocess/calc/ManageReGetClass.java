package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 時間帯作成、時間計算で再取得が必要になっているクラスたちの管理クラス
 * @author keisuke_hoshina
 *
 */
@Getter
public class ManageReGetClass {
	
	//1日の範囲
	CalculationRangeOfOneDay calculationRangeOfOneDay;
	
	//日別実績(WORK)
	IntegrationOfDaily integrationOfDaily;
	
	//就業時間帯設定
	Optional<WorkTimeSetting> workTimeSetting;
	
	//勤務種類
	Optional<WorkType> workType;
	
	//就業時間帯別代休時間設定
	List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList;
	
	//労働制
	DailyCalculationPersonalInformation personalInfo;
	
	//計算処理に入ることができるかフラグ
	//(造語)
	Boolean calculatable;

	/**
	 * Constructor 
	 */
	private ManageReGetClass(CalculationRangeOfOneDay calculationRangeOfOneDay, IntegrationOfDaily integrationOfDaily,
			Optional<WorkTimeSetting> workTimeSetting, Optional<WorkType> workType,
			List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList,
			DailyCalculationPersonalInformation personalInfo, Boolean calculatable) {
		super();
		this.calculationRangeOfOneDay = calculationRangeOfOneDay;
		this.integrationOfDaily = integrationOfDaily;
		this.workTimeSetting = workTimeSetting;
		this.workType = workType;
		this.subHolTransferSetList = subHolTransferSetList;
		this.personalInfo = personalInfo;
		this.calculatable = calculatable;
	}
	
	/**
	 * 計算処理に入ることができないと判断できた時Factory Method
	 */
	public static ManageReGetClass cantCalc() {
		return new ManageReGetClass(null, 
									null, 
									null, 
									null, 
									null, 
									null, 
									false);
				
	}
	
	/**
	 * 計算処理に入ることができると判断できた時Factory Method
	 */
	public static ManageReGetClass canCalc(CalculationRangeOfOneDay calculationRangeOfOneDay, IntegrationOfDaily integrationOfDaily,
										  Optional<WorkTimeSetting> workTimeSetting, Optional<WorkType> workType,
										  List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList,
										  DailyCalculationPersonalInformation personalInfo) {
		return new ManageReGetClass(calculationRangeOfOneDay,
									integrationOfDaily,
									workTimeSetting,
									workType,
									subHolTransferSetList,
									personalInfo,
									true);
	
	}
}
