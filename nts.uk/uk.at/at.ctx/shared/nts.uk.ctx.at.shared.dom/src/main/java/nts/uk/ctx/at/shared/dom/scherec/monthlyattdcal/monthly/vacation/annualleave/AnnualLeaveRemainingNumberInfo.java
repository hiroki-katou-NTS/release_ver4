package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.remain.AnnualLeaveGrantRemaining;

/**
 * 年休残数情報
 * @author masaaki_jinno
 *
 */
@Getter
@AllArgsConstructor
public class AnnualLeaveRemainingNumberInfo implements Cloneable {

	/** 合計 */
	private AnnualLeaveRemainingNumber remainingNumber;
	
	/** 付与前 */
	private AnnualLeaveRemainingNumber remainingNumberBeforeGrant;
	
	/** 付与後 */
	private Optional<AnnualLeaveRemainingNumber> remainingNumberAfterGrantOpt;
	
	/**
	 * ファクトリ
	 * @param remainingNumber 合計
	 * @param remainingNumberBeforeGrant 付与前
	 * @param remainingNumberAfterGrantOpt 付与後
	 * @return
	 */
	public static AnnualLeaveRemainingNumberInfo of(
			AnnualLeaveRemainingNumber remainingNumber,
			AnnualLeaveRemainingNumber remainingNumberBeforeGrant,
			Optional<AnnualLeaveRemainingNumber> remainingNumberAfterGrantOpt
			){
		
		AnnualLeaveRemainingNumberInfo domain = new AnnualLeaveRemainingNumberInfo();
		domain.remainingNumber = remainingNumber;
		domain.remainingNumberBeforeGrant = remainingNumberBeforeGrant;
		domain.remainingNumberAfterGrantOpt= remainingNumberAfterGrantOpt;
		return domain;
	}
	
	/** コンストラクタ  */
	public AnnualLeaveRemainingNumberInfo(){
		this.remainingNumber = new AnnualLeaveRemainingNumber();
		this.remainingNumberBeforeGrant = new AnnualLeaveRemainingNumber();
		this.remainingNumberAfterGrantOpt = Optional.empty();
	}
	
	/**
	 * 年休付与残数データから年休残数を作成
	 * @param remainingDataList 年休付与残数データリスト
	 * @param afterGrantAtr 付与後フラグ
	 */
	public void createRemainingNumberFromGrantRemaining(
			List<AnnualLeaveGrantRemaining> remainingDataList, boolean afterGrantAtr){
		
		// 年休付与残数データから残数を作成
		this.remainingNumber.createRemainingNumberFromGrantRemaining(remainingDataList);
		
		// 「付与後フラグ」をチェック
		if (afterGrantAtr){
			
			// 残数付与後　←　残数
			this.remainingNumberAfterGrantOpt = Optional.of(this.remainingNumber.clone());
		}
		else {
			
			// 残数付与前　←　残数
			this.remainingNumberBeforeGrant = this.remainingNumber.clone();
		}
	}
	
	/**
	 * クローン
	 */
	public AnnualLeaveRemainingNumberInfo clone() {
		AnnualLeaveRemainingNumberInfo cloned = new AnnualLeaveRemainingNumberInfo();
		try {
			if ( remainingNumberBeforeGrant != null ){
				cloned.remainingNumberBeforeGrant = this.remainingNumberBeforeGrant.clone();
			}
			if ( remainingNumber != null ){
				cloned.remainingNumber = this.remainingNumber.clone();
			}
			
			if (this.remainingNumberAfterGrantOpt.isPresent()){
				cloned.remainingNumberAfterGrantOpt = Optional.of(this.remainingNumberAfterGrantOpt.get().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("AnnualLeaveRemainingInfo clone error.");
		}
		return cloned;
	}

	/**
	 * 付与前退避処理
	 */
	public void saveStateBeforeGrant(){
		// 合計残数を付与前に退避する
		remainingNumberBeforeGrant = remainingNumber.clone();
	}
	
	/**
	 * 付与後退避処理
	 */
	public void saveStateAfterGrant(){
		// 合計残数を付与後に退避する
		remainingNumberAfterGrantOpt = Optional.of(remainingNumber.clone());
	}
	
}

