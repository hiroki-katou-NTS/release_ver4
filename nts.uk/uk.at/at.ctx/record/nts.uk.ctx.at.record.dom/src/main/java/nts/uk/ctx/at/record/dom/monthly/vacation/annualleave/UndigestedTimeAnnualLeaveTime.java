package nts.uk.ctx.at.record.dom.monthly.vacation.annualleave;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.maxdata.UsedMinutes;

/**
 * 時間年休未消化時間
 * @author shuichu_ishida
 */
@Getter
public class UndigestedTimeAnnualLeaveTime {

	/** 未消化時間 */
	private UsedMinutes undigestedTime;
	
	/**
	 * コンストラクタ
	 */
	public UndigestedTimeAnnualLeaveTime(){
		
		this.undigestedTime = new UsedMinutes(0);
	}
	
	/**
	 * ファクトリー
	 * @param undigestedTime 未消化時間
	 * @return 時間年休未消化時間
	 */
	public static UndigestedTimeAnnualLeaveTime of(UsedMinutes undigestedTime){
		
		UndigestedTimeAnnualLeaveTime domain = new UndigestedTimeAnnualLeaveTime();
		domain.undigestedTime = undigestedTime;
		return domain;
	}
	
	/**
	 * 分を加算する
	 * @param minutes 分
	 */
	public void addMinutes(int minutes){
		this.undigestedTime = this.undigestedTime.addMinutes(minutes);
	}
}
