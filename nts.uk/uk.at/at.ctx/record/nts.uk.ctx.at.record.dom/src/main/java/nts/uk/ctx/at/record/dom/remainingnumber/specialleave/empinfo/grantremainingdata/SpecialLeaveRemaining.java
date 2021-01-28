package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemaining;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.grantnumber.SpecialLeaveUndigestNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.DayNumberOfRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.TimeOfRemain;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeavaRemainTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeave;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveRemainingDetail;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveUseDays;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveUseNumber;

/**
 * 特休情報残数
 * @author shuichu_ishida
 */
@Getter
public class SpecialLeaveRemaining implements Cloneable {

	/** 特休（マイナスなし） */
	private SpecialLeave specialLeaveNoMinus;
	/** 特休（マイナスあり） */
	private SpecialLeave specialLeaveWithMinus;
	/** 特休未消化数 */
	@Setter
	private Optional<SpecialLeaveUndigestNumber> specialLeaveUndigestNumber;

	/**
	 * コンストラクタ
	 */
	public SpecialLeaveRemaining(){
		this.specialLeaveNoMinus = new SpecialLeave();
		this.specialLeaveWithMinus = new SpecialLeave();
		this.specialLeaveUndigestNumber = Optional.empty();
	}
	
	/**
	 * ファクトリー
	 * @param specialLeaveNoMinus 特休（マイナスなし）
	 * @param specialLeaveWithMinus 特休（マイナスあり）
	 * @param specialLeaveUndigestNumber 特休未消化数
	 * @return 特休情報残数
	 */
	public static SpecialLeaveRemaining of(
			SpecialLeave specialLeaveNoMinus,
			SpecialLeave specialLeaveWithMinus,
			Optional<SpecialLeaveUndigestNumber> specialLeaveUndigestNumber){
		
		SpecialLeaveRemaining domain = new SpecialLeaveRemaining();
		domain.specialLeaveNoMinus = specialLeaveNoMinus;
		domain.specialLeaveWithMinus = specialLeaveWithMinus;
		domain.specialLeaveUndigestNumber = specialLeaveUndigestNumber;
		return domain;
	}
	
	@Override
	public SpecialLeaveRemaining clone() {
		SpecialLeaveRemaining cloned = new SpecialLeaveRemaining();
		try {
			cloned.specialLeaveNoMinus = this.specialLeaveNoMinus.clone();
			cloned.specialLeaveWithMinus = this.specialLeaveWithMinus.clone();
//			if (this.halfDaySpecialLeaveNoMinus.isPresent()){
//				cloned.halfDaySpecialLeaveNoMinus = Optional.of(this.halfDaySpecialLeaveNoMinus.get().clone());
//			}
//			if (this.halfDaySpecialLeaveWithMinus.isPresent()){
//				cloned.halfDaySpecialLeaveWithMinus = Optional.of(this.halfDaySpecialLeaveWithMinus.get().clone());
//			}
//			if (this.timeSpecialLeaveNoMinus.isPresent()){
//				cloned.timeSpecialLeaveNoMinus = Optional.of(this.timeSpecialLeaveNoMinus.get().clone());
//			}
//			if (this.timeSpecialLeaveWithMinus.isPresent()){
//				cloned.timeSpecialLeaveWithMinus = Optional.of(this.timeSpecialLeaveWithMinus.get().clone());
//			}
			if (this.specialLeaveUndigestNumber.isPresent()){
				cloned.specialLeaveUndigestNumber = Optional.of(this.specialLeaveUndigestNumber.get().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("SpecialLeaveRemaining clone error.");
		}
		return cloned;
	}
	
	/**
	 * 特休付与情報を更新
	 * @param remainingDataList 特休付与残数データリスト
	 * @param afterGrantAtr 付与後フラグ
	 */
	public void updateRemainingNumber(
			List<SpecialLeaveGrantRemaining> remainingDataList, 
			boolean afterGrantAtr){
		
		// 特別休暇付与残数データから実特別休暇の特別休暇残数を作成
		this.specialLeaveWithMinus.createRemainingNumberFromGrantRemaining(
				remainingDataList, afterGrantAtr);
		
		// 特別休暇付与残数データから特別休暇の特別休暇残数を作成
		// 特休（マイナスなし）を特休（マイナスあり）で上書き　＆　特休からマイナスを削除
		this.specialLeaveNoMinus = updateRemainingNumberNoMinus(this.specialLeaveWithMinus);
	}
	
	/**
	 * 特別休暇付与残数データから特別休暇の特別休暇残数を作成
	 * @param specialLeaveWithMinus 特休（マイナスあり）
	 */
	private SpecialLeave updateRemainingNumberNoMinus(SpecialLeave specialLeaveWithMinus){
		
		// 特休（マイナスなし）を特休（マイナスあり）で上書き
		SpecialLeave specialLeaveNoMinus = this.specialLeaveWithMinus.clone();
		
		// 特休からマイナスを削除
		// 「特別休暇．残数」「特別休暇．残数付与前」「特別休暇．残数付与後」をそれぞれ処理
		
		// 残数
		updateRemainingNumberWithMinusToNoMinus(
				specialLeaveNoMinus.getRemainingNumberInfo().getRemainingNumber(),
				specialLeaveNoMinus.getUsedNumberInfo().getUsedNumber());
		
		// 残数付与前
		updateRemainingNumberWithMinusToNoMinus(
				specialLeaveNoMinus.getRemainingNumberInfo().getRemainingNumberBeforeGrant(),
				specialLeaveNoMinus.getUsedNumberInfo().getUsedNumberBeforeGrant());
		
		// 残数付与後
		if ( specialLeaveNoMinus.getRemainingNumberInfo().getRemainingNumberAfterGrantOpt().isPresent()
				&& specialLeaveNoMinus.getUsedNumberInfo().getUsedNumberAfterGrantOpt().isPresent() ){
			updateRemainingNumberWithMinusToNoMinus(
				specialLeaveNoMinus.getRemainingNumberInfo().getRemainingNumberAfterGrantOpt().get(),
				specialLeaveNoMinus.getUsedNumberInfo().getUsedNumberAfterGrantOpt().get());
		}
		
		return specialLeaveNoMinus;
	}
	
	/**
	 * 特休（マイナスあり）を特休（マイナスなし）に変換
	 * @param specialLeaveRemainingNumber　特別休暇残数
	 * @param SpecialLeaveUseNumber　特別休暇使用数
	 */
	private void updateRemainingNumberWithMinusToNoMinus(
			SpecialLeaveRemainingNumber specialLeaveRemainingNumber,
			SpecialLeaveUseNumber specialLeaveUseNumber){
			
		// パラメータ「特別休暇残数．合計残日数」と「特別休暇残数．合計残時間」をチェック
		
		// 合計残日数<0　or 合計残時間 < 0
		double remainDays = specialLeaveUseNumber.getUseDays().map(x -> x.v()).orElse(0.0);
		int remainTimes = 0;
		if ( specialLeaveRemainingNumber.getTimeOfRemain().isPresent() ){
			remainTimes = specialLeaveRemainingNumber.getTimeOfRemain().get().v();
		}
		
		if ( remainDays < 0 || remainTimes < 0 ){
			
			// 特別休暇．使用数からマイナス分を引く
			if ( remainDays < 0 ){
				double useDays = specialLeaveUseNumber.getUseDays().map(x -> x.v()).orElse(0.0) + remainDays;
				specialLeaveUseNumber.setUseDays(Optional.of(new SpecialLeaveUseDays(useDays)));
			}
			if ( remainTimes < 0 ){
				if ( specialLeaveUseNumber.getUseTimes().isPresent() ){
					int useTimes = specialLeaveUseNumber.getUseTimes().get().getUseTimes().v() + remainTimes;
					specialLeaveUseNumber.getUseTimes().get().setUseTimes(new SpecialLeavaRemainTime(useTimes));
				}
			}
			
			// 特別休暇残数．明細を取得
			List<SpecialLeaveRemainingDetail> detailList
				= specialLeaveRemainingNumber.getDetails();
			
			// 取得した特別休暇残明細でループ
			detailList.forEach(c->{
				// 特別休暇残明細．日数←0
				c.setDays(new DayNumberOfRemain(0.0));
				// 特別休暇残明細.時間 ← 0
				c.setTime(Optional.of(new TimeOfRemain(0)));
			});
			
			// 特別休暇．残数．合計残日数←0
			specialLeaveRemainingNumber.setDayNumberOfRemain(new DayNumberOfRemain(0.0));
			
			// 特別休暇．残数．合計残時間←0
			specialLeaveRemainingNumber.setTimeOfRemain(Optional.of(new TimeOfRemain(0)));
		}
	}
	
	/**
	 * クリア
	 */
	public void clear(){
		specialLeaveNoMinus.clear();
		specialLeaveWithMinus.clear();
		this.specialLeaveUndigestNumber = Optional.empty();
	}
}
