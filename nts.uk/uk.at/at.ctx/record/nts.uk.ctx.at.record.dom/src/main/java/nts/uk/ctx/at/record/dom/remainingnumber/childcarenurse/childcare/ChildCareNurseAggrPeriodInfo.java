package nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.UsedTimes;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.ChildCareNurseUsedNumber;

/**
 * 集計期間の子の看護介護休暇情報
  * @author yuri_tamakoshi
 */
@Getter
@Setter
public class ChildCareNurseAggrPeriodInfo {
	/** 時間休暇使用回数 */
	private UsedTimes usedCount;
	/** 時間休暇使用日数 */
	private UsedTimes usedDays;
	/** 集計期間の使用数 */
	private  ChildCareNurseUsedNumber aggrPeriodUsedNumber;

	/**
	 * コンストラクタ
	 */
	public ChildCareNurseAggrPeriodInfo(){
		this.usedCount =new UsedTimes(0);
		this.usedDays = new UsedTimes(0);
		this.aggrPeriodUsedNumber = new ChildCareNurseUsedNumber();
	}

	/**
	 * ファクトリー
	 * @param usedCount 時間休暇使用回数
	 * @param usedDays 時間休暇使用日数
	 * @param aggrPeriodUsedNumber 集計期間の子の使用数
	 * @return 集計期間の子の看護介護休暇情報
	 */
	public static ChildCareNurseAggrPeriodInfo of(
			UsedTimes usedCount,
			UsedTimes usedDays,
			ChildCareNurseUsedNumber aggrPeriodUsedNumber){

		ChildCareNurseAggrPeriodInfo domain = new ChildCareNurseAggrPeriodInfo();
		domain.usedCount = usedCount;
		domain.usedDays = usedDays;
		domain.aggrPeriodUsedNumber = aggrPeriodUsedNumber;
		return domain;
	}
}
