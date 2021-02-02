package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemaining;

/**
 * 特別休暇
 * @author do_dt
 *
 */
@AllArgsConstructor
@Setter
@Getter
public class SpecialLeave extends DomainObject implements Cloneable, Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 使用数（特別休暇使用情報）
	 */
	private SpecialLeaveUsedInfo usedNumberInfo;

	/**
	 * 残数（特別休暇残数情報）
	 */
	private SpecialLeaveRemainingNumberInfo remainingNumberInfo;

	/**
	 * コンストラクタ
	 */
	public SpecialLeave(){

		this.usedNumberInfo = new SpecialLeaveUsedInfo();
		this.remainingNumberInfo = new SpecialLeaveRemainingNumberInfo();
	}

	/**
	 * ファクトリー
	 * @param usedNumberInfo 特別休暇使用情報
	 * @param remainingNumberInfo 残数
	 * @return 実特休
	 */
	public static SpecialLeave of(
			SpecialLeaveUsedInfo usedNumberInfo,
			SpecialLeaveRemainingNumberInfo remainingNumberInfo){

		SpecialLeave domain = new SpecialLeave();
		domain.usedNumberInfo = usedNumberInfo;
		domain.remainingNumberInfo = remainingNumberInfo;
		return domain;
	}

	@Override
	public SpecialLeave clone() {
		SpecialLeave cloned = new SpecialLeave();
			cloned.usedNumberInfo = this.usedNumberInfo.clone();
			cloned.remainingNumberInfo = this.remainingNumberInfo.clone();
		return cloned;
	}

	/**
	 * データをクリア
	 */
	public void clear(){
		usedNumberInfo.clear();
		remainingNumberInfo.clear();
	}

	/**
	 * 特別休暇付与残数データから実特別休暇の特別休暇残数を作成
	 * @param remainingDataList 特休付与残数データリスト
	 * @param afterGrantAtr 付与後フラグ
	 */
	public void createRemainingNumberFromGrantRemaining(
			List<SpecialLeaveGrantRemaining> remainingDataList, boolean afterGrantAtr){

		// 特別休暇付与残数データから特別休暇残数を作成
		this.remainingNumberInfo.createRemainingNumberFromGrantRemaining(remainingDataList, afterGrantAtr);
	}

	/**
	 * 使用数を加算する
	 * @param days 日数
	 * @param afterGrantAtr 付与後フラグ
	 */
	public void addUsedNumber(SpecialLeaveUseNumber usedNumber, boolean afterGrantAtr){

		this.usedNumberInfo.addUsedNumber(usedNumber, afterGrantAtr);

	}

	/**
	 * 付与前退避処理
	 */
	public void saveStateBeforeGrant(){
		// 合計残数を付与前に退避する
		this.usedNumberInfo.saveStateBeforeGrant();
		this.remainingNumberInfo.saveStateBeforeGrant();
	}

	/**
	 * 付与後退避処理
	 */
	public void saveStateAfterGrant(){
		// 合計残数を付与後に退避する
		this.usedNumberInfo.saveStateAfterGrant();
		this.remainingNumberInfo.saveStateAfterGrant();
	}

}