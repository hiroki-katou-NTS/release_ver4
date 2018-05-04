package nts.uk.ctx.at.record.dom.monthly.vacation.annualleave;

import lombok.Getter;
import lombok.Setter;

/**
 * 半日年休
 * @author shuichu_ishida
 */
@Getter
@Setter
public class HalfDayAnnualLeave {

	/** 残数 */
	private HalfDayAnnLeaRemainingNum remainingNum;
	/** 使用数 */
	private HalfDayAnnLeaUsedNum usedNum;
	
	/**
	 * コンストラクタ
	 */
	public HalfDayAnnualLeave(){
		
		this.remainingNum = new HalfDayAnnLeaRemainingNum();
		this.usedNum = new HalfDayAnnLeaUsedNum();
	}
	
	/**
	 * ファクトリー
	 * @param remainingNum 残数
	 * @param usedNum 使用数
	 * @return 半日年休
	 */
	public static HalfDayAnnualLeave of(
			HalfDayAnnLeaRemainingNum remainingNum,
			HalfDayAnnLeaUsedNum usedNum){
		
		HalfDayAnnualLeave domain = new HalfDayAnnualLeave();
		domain.remainingNum = remainingNum;
		domain.usedNum = usedNum;
		return domain;
	}
}
