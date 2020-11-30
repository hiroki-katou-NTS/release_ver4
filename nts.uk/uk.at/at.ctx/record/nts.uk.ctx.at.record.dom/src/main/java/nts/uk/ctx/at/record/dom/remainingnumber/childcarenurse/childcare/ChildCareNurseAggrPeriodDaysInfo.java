package nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;


/**
 * 集計期間の休暇情報
  * @author yuri_tamakoshi
 */
@Getter
@Setter
public class ChildCareNurseAggrPeriodDaysInfo {
	/** 子の看護休暇情報（本年） */
	private ChildCareNurseAggrPeriodInfo thisYear;
	/** 子の看護休暇情報（翌年） */
	private Optional<ChildCareNurseAggrPeriodInfo> nextYear;

	/**
	 * コンストラクタ
	 */
	public ChildCareNurseAggrPeriodDaysInfo(){

		this.thisYear = new ChildCareNurseAggrPeriodInfo();
		this.nextYear =  Optional.empty();

	}

	/**
	 * ファクトリー
	 * @param thisYear 子の看護休暇情報（本年）
	 * @param nextYear 子の看護休暇情報（翌年）
	 * @return 集計期間の休暇情報
	 */
	public static ChildCareNurseAggrPeriodDaysInfo of(
		ChildCareNurseAggrPeriodInfo thisYear,
		Optional<ChildCareNurseAggrPeriodInfo> nextYear){

	ChildCareNurseAggrPeriodDaysInfo domain = new ChildCareNurseAggrPeriodDaysInfo();
	domain.thisYear = thisYear;
	domain.nextYear = nextYear;
	return domain;
	}

}