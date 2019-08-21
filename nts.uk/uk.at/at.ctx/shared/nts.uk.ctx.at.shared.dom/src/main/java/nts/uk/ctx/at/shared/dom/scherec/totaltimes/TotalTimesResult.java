package nts.uk.ctx.at.shared.dom.scherec.totaltimes;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 回数集計結果情報
 * @author shuichi_ishida
 */
@Getter
@Setter
public class TotalTimesResult {

	/** 回数 */
	private AttendanceDaysMonth count;
	/** 時間 */
	private AttendanceTimeMonth time;

	/** 大塚カスタマイズ：集計対象勤務種別 */
	public static String WORKTYPE_A = "0000000110";
	/** 大塚カスタマイズ：集計対象勤務種別 */
	public static String WORKTYPE_B = "0000000210";

	/**
	 * コンストラクタ
	 */
	public TotalTimesResult(){
		
		this.count = new AttendanceDaysMonth(0.0);
		this.time = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param count 回数
	 * @param time　時間
	 * @return 回数集計結果情報
	 */
	public static TotalTimesResult of(
			AttendanceDaysMonth count,
			AttendanceTimeMonth time){
		
		TotalTimesResult domain = new TotalTimesResult();
		domain.count = count;
		domain.time = time;
		return domain;
	}
	
	/**
	 * 回数を加算する
	 * @param count 回数
	 */
	public void addCount(double count){
		this.count = this.count.addDays(count);
	}
	
	/**
	 * 時間を加算する
	 * @param minutes 時間（分）
	 */
	public void addTime(int minutes){
		this.time = this.time.addMinutes(minutes);
	}
	
	/**
	 * 大塚カスタマイズの条件判断
	 * @param totalCountNo 回数集計No
	 * @param workTypeCode 勤務種別コード
	 * @return true:対象、false:対象外
	 */
	public boolean checkOotsukaCustomize(int totalCountNo, String workTypeCode){
		
		// 回数集計NOの判断
		if (totalCountNo != 25 && totalCountNo != 29) return true;
		
		// 勤務種別コードの判断
		if (workTypeCode.compareTo(WORKTYPE_A) == 0) return true;
		if (workTypeCode.compareTo(WORKTYPE_B) == 0) return true;
		
		return false;
	}
}
