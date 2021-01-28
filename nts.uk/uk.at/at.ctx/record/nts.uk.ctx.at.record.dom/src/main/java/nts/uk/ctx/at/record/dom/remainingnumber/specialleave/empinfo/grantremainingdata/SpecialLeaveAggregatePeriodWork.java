package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * 特別休暇集計期間WORK
 * @author masaaki_jinno
 *SpecialLeaveLapsedWork
 */
@Getter
@Setter
public class SpecialLeaveAggregatePeriodWork {

	/** 期間 */
	private DatePeriod period;
	/** 終了日 */
	private NextDayAfterPeriodEndWork 	endDay;
	/** 消滅（消滅情報WORK） */
	private SpecialLeaveLapsedWork lapsedWork;
	/** 付与（付与情報WORK） */
	private SpecialLeaveGrantWork grantWork;
	/** 付与前か付与後か */
	private GrantPeriodAtr grantPeriodAtr;

	/**
	 * コンストラクタ
	 */
	public SpecialLeaveAggregatePeriodWork(){
		this.period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.endDay = new NextDayAfterPeriodEndWork();
		this.lapsedWork = new SpecialLeaveLapsedWork();
		this.grantWork = new SpecialLeaveGrantWork();
		this.grantPeriodAtr = GrantPeriodAtr.BEFORE_GRANT;
	}

	public SpecialLeaveAggregatePeriodWork(DatePeriod period) {
		this.period = period;
		this.endDay = new NextDayAfterPeriodEndWork();
		this.lapsedWork = new SpecialLeaveLapsedWork();
		this.grantWork = new SpecialLeaveGrantWork();
		this.grantPeriodAtr = GrantPeriodAtr.BEFORE_GRANT;
	}

	/**
	 * @param period 期間
	 * @param endDay 終了日
	 * @param lapsedAtr 消滅
	 * @param grantWork 付与
	 * @param grantPeriodAtr 付与前か付与後か
	 * @return 特休集計期間WORK
	 */
	public static SpecialLeaveAggregatePeriodWork of(
			DatePeriod period,
			NextDayAfterPeriodEndWork endDay,
			SpecialLeaveLapsedWork lapsedAtr,
			SpecialLeaveGrantWork grantWork,
			GrantPeriodAtr grantPeriodAtr){

		SpecialLeaveAggregatePeriodWork domain = new SpecialLeaveAggregatePeriodWork();
		domain.period = period;
		domain.endDay = endDay;
		domain.lapsedWork = lapsedAtr;
		domain.grantWork = grantWork;
		domain.grantPeriodAtr = grantPeriodAtr;
		return domain;
	}
}