package nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;

/**
 * 積立年休残明細
 * @author shuichu_ishida
 */
@Getter
@Setter
public class ReserveLeaveRemainingDetail {

	/** 付与日 */
	private GeneralDate grantDate;
	
	/** 日数 */
	private ReserveLeaveRemainingDayNumber days;
	
	/**
	 * コンストラクタ
	 * @param grantDate 付与日
	 */
	public ReserveLeaveRemainingDetail(GeneralDate grantDate){
		
		this.grantDate = grantDate;
		
		this.days = new ReserveLeaveRemainingDayNumber(0.0);
	}
	
	/**
	 * ファクトリー
	 * @param grantDate 付与日
	 * @param days 日数
	 * @return 積立年休残明細
	 */
	public static ReserveLeaveRemainingDetail of(
			GeneralDate grantDate,
			ReserveLeaveRemainingDayNumber days){
		
		ReserveLeaveRemainingDetail domain = new ReserveLeaveRemainingDetail(grantDate);
		domain.days = days;
		return domain;
	}
}
