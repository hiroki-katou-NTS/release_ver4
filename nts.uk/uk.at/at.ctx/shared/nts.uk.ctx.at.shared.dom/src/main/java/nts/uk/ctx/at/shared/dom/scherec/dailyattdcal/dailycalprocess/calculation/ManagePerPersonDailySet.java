package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.AddSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.WorkFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.premiumitem.PriceUnit;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;

/**
 * 毎日変更の可能性のあるマスタ管理クラス
 * （社員設定管理）
 * 
 * @author keisuke_hoshina
 *
 */
@Getter
public class ManagePerPersonDailySet {
	/** 労働条件 */
	private WorkingConditionItem personInfo;
	
	/** 法定労働 */
	private DailyUnit dailyUnit;
	
	/** 加算設定 */
	private AddSetting addSetting;
	
	/** 加給設定 */
	private Optional<BonusPaySetting> bonusPaySetting;
	
	/** インセンティブ単価 <応援勤務枠No, 単価> */
	private Map<WorkFrameNo, PriceUnit> incentiveUnitPrice = new HashMap<>();
	
	/** 平日時の所定時間設定
	 *年休、欠勤の場合に実績に就業時間帯が埋まっていない時に使用する。
	 * 例1： 欠勤の場合は、フレックスを-8：00のような計算をする。　平日時の所定時間を使って計算する。
	 * 例2： 年休の場合は、就業時間に加算する場合は平日時の所定時間分を就業時間に加算する（時給者の場合は就業時間に加算するケースが多い）
	 */
	private PredetermineTimeSetForCalc predetermineTimeSetByPersonWeekDay;
	
	/**
	 * Constructor
	 * @param personInfo 労働条件
	 * @param dailyUnit　法定労働時間
	 */
	public ManagePerPersonDailySet(
			WorkingConditionItem personInfo,
			DailyUnit dailyUnit,
			AddSetting addSetting,
			Optional<BonusPaySetting> bonusPaySetting,
			PredetermineTimeSetForCalc predetermineTimeSetByPersonWeekDay,
			Map<WorkFrameNo, PriceUnit> incentiveUnitPrice) {
		super();
		this.personInfo = personInfo;
		this.dailyUnit = dailyUnit;
		this.addSetting = addSetting;
		this.bonusPaySetting = bonusPaySetting;
		this.predetermineTimeSetByPersonWeekDay = predetermineTimeSetByPersonWeekDay;
		this.incentiveUnitPrice = incentiveUnitPrice;
	}
	
	/**
	 * インセンティブ単価を取得する
	 * @param no
	 * @return インセンティブ単価
	 */
	public PriceUnit getIncentiveUnitPrice(WorkFrameNo no) {
		return Optional.ofNullable(this.incentiveUnitPrice.get(no)).orElse(PriceUnit.ZERO);
	}
}
