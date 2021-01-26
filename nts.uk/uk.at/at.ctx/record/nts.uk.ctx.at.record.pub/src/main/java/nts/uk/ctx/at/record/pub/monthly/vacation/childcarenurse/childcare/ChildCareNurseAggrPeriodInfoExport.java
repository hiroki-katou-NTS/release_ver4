package nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.ChildCareNurseUsedNumber;

/**
 * 集計期間の子の看護介護休暇情報
  * @author yuri_tamakoshi
 */
@Getter
public class ChildCareNurseAggrPeriodInfoExport {
	/** 子の看護介護休暇の時間休暇使用回数 */
	private Integer usedCount;
	/** 子の看護介護休暇の時間休暇使用日数 */
	private Integer usedDays;
	/** 集計期間の子の看護介護休暇使用数 */
	private  ChildCareNurseUsedNumber aggrPeriodUsedNumber;


	/**
	 * コンストラクタ
	 */
	public ChildCareNurseAggrPeriodInfoExport(){
		this.usedCount = new Integer(0);
		this.usedDays = new Integer(0);
		this.aggrPeriodUsedNumber = new ChildCareNurseUsedNumber();
	}

	/**
	 * ファクトリー
	 * @param usedCount 子の看護介護休暇の時間休暇使用回数
	 * @param usedDays 子の看護介護休暇の時間休暇使用日数
	 * @param aggrPeriodUsedNumber集計期間の子の看護介護休暇使用数
	 * @return 集計期間の子の看護介護休暇情報
	 */
	public static ChildCareNurseAggrPeriodInfoExport of(
			Integer usedCount,
			Integer usedDays,
			ChildCareNurseUsedNumber aggrPeriodUsedNumber){

		ChildCareNurseAggrPeriodInfoExport domain = new ChildCareNurseAggrPeriodInfoExport();
		domain.usedCount = usedCount;
		domain.usedDays = usedDays;
		domain.aggrPeriodUsedNumber = aggrPeriodUsedNumber;
		return domain;
	}
}
