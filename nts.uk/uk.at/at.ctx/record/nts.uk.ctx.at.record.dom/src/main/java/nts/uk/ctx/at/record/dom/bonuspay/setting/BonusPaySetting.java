/**
 * 9:18:12 AM Jun 6, 2017
 */
package nts.uk.ctx.at.record.dom.bonuspay.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.bonuspay.primitives.BonusPaySettingCode;
import nts.uk.ctx.at.record.dom.bonuspay.primitives.BonusPaySettingName;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;

/**
 * @author hungnm
 *　加給設定
 */
@Getter

public class BonusPaySetting extends AggregateRoot{

	private String companyId;

	private BonusPaySettingCode code;

	private BonusPaySettingName name;

	private List<BonusPayTimesheet> lstBonusPayTimesheet;

	private List<SpecBonusPayTimesheet> lstSpecBonusPayTimesheet;

	private BonusPaySetting(String companyId, BonusPaySettingCode code, BonusPaySettingName name,
			List<BonusPayTimesheet> lstBonusPayTimesheet, List<SpecBonusPayTimesheet> lstSpecBonusPayTimesheet) {
		super();
		this.companyId = companyId;
		this.code = code;
		this.name = name;
		this.lstBonusPayTimesheet = lstBonusPayTimesheet;
		this.lstSpecBonusPayTimesheet = lstSpecBonusPayTimesheet;
	}

	private BonusPaySetting() {
		super();
	}

	public static BonusPaySetting createFromJavaType(String companyId, String code, String name,
			List<BonusPayTimesheet> lstBonusPayTimesheet, List<SpecBonusPayTimesheet> lstSpecBonusPayTimesheet) {
		return new BonusPaySetting(companyId, new BonusPaySettingCode(code),
				new BonusPaySettingName(name), lstBonusPayTimesheet, lstSpecBonusPayTimesheet);
	}
	
	/**
	 * 計算範囲との重複期間をリストにする(加給時間帯)
	 * @param calcRange 計算範囲
	 * @return　開始と終了を更新した加給時間帯
	 */
	public List<BonusPayTimesheet> getDuplicateBonusPayTimeList(TimeSpanForCalc calcRange){
		List<BonusPayTimesheet> returnList = new ArrayList<BonusPayTimesheet>();
		for(BonusPayTimesheet timesheet : lstBonusPayTimesheet){
			//Optional<TimeSpanForCalc> newRange = calcRange.getDuplicatedWith(timesheet.getCalcrange());
			Optional<TimeSpanForCalc> newRange = Optional.empty();
			if(newRange.isPresent()) {
				returnList.add(timesheet.reCreateCalcRange(newRange.get()));
			}
		}
		return returnList;
	}
	

	
	/**
	 * 加給時間帯の作成
	 * @param timeSpan 計算範囲
	 * @return 計算範囲に重複している時間帯リスト
	 */
	public List<BonusPayTimesheet> createDuplicationBonusPayTimeSheet(TimeSpanForCalc calcSpan){
		List<BonusPayTimesheet> bonusPayList = new ArrayList<>();
		Optional<TimeSpanForCalc> duplicateRange;
		for(BonusPayTimesheet bonusPay : bonusPayList) {
			//duplicateRange = calcSpan.getDuplicatedWith(bonusPay.getCalcrange());
			duplicateRange = Optional.empty();
			if(duplicateRange.isPresent()){
				bonusPayList.add(bonusPay.reCreateCalcRange(duplicateRange.get()));
			}
		}
		return bonusPayList;
	}
	
	/**
	 * 特定日加給時間帯の作成
	 * @param timeSpan 計算範囲
	 * @return 計算範囲に重複している時間帯リスト
	 */
	public List<SpecBonusPayTimesheet> createDuplicationSpecifyBonusPay(TimeSpanForCalc calcSpan){
		List<SpecBonusPayTimesheet> bonusPayList = new ArrayList<>();
		Optional<TimeSpanForCalc> duplicateRange;
		for(SpecBonusPayTimesheet bonusPay : bonusPayList) {
			//duplicateRange = calcSpan.getDuplicatedWith(bonusPay.getCalcrange());
			duplicateRange = Optional.empty();
			if(duplicateRange.isPresent()){
				bonusPayList.add(bonusPay.reCreateCalcRange(duplicateRange.get()));
			}
		}
		return bonusPayList;
	}
	
//---------------------------------------「計算時間帯」の特定日加給時間帯と加給時間が分かれていなかった時の処理------------------------------------------------
//	/**
//	 * 加給時間帯と特定日加給時間帯を１つにまとめる
//	 * @param calcRange
//	 * @return 加給時間帯リスト
//	 */
//	public List<BonusPayTimesheet> createBonusPayTimeSheetList(TimeSpanForCalc calcRange){
//		List<BonusPayTimesheet> Bpay = new ArrayList<BonusPayTimesheet>();
//		Bpay.addAll(getDuplicateBonusPayTimeList(calcRange));
//		Bpay.addAll(getDuplicateSpecBonusPayTimeList(calcRange));
//		return Bpay;
//	}
//	/**
//	 * 計算範囲と重複期間をリストにする(特定日加給時間帯)
//	 * @param calcRange 計算範囲
//	 * @return
//	 */
//	public List<BonusPayTimesheet> getDuplicateSpecBonusPayTimeList(TimeSpanForCalc calcRange){
//		List<BonusPayTimesheet> returnList = new ArrayList<BonusPayTimesheet>();
//		for(SpecBonusPayTimesheet timesheet : lstSpecBonusPayTimesheet){
//			Optional<TimeSpanForCalc> newRange = calcRange.getDuplicatedWith(new TimeSpanForCalc(new TimeWithDayAttr(timesheet.getStartTime().valueAsMinutes()),new TimeWithDayAttr(timesheet.getEndTime().valueAsMinutes())));
//			if(newRange.isPresent()) {
//				returnList.add(timesheet.reCreateCalcRange(newRange.get()));
//			}
//		}
//		return returnList;
//	}
//------------------------------------------------------------------------------------------------------------------------------
}
