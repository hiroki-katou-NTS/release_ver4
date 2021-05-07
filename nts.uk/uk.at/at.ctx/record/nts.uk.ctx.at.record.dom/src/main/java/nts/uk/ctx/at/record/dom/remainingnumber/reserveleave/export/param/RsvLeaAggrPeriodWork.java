package nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.MaxDaysRetention;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * 積立年休集計期間WORK
 * @author shuichu_ishida
 */
@Getter
@Setter
public class RsvLeaAggrPeriodWork {

	/** 期間 */
	private DatePeriod period;
	/** 終了日 */
	private RsvLeaNextDayAfterPeriodEndWork endWork;
	/** 期間の開始日に付与があるか */
	private boolean grantAtr;
	/** 付与回数 */
	private int grantNumber = 0;
	/** 付与後 */
	private boolean afterGrant;
	/** 期間の開始日に消滅するかどうか */
	private boolean lapsedAtr;
	/** 上限日数 */
	private MaxDaysRetention maxDays;
	/** 積立年休付与 */
	private Optional<NextReserveLeaveGrant> reserveLeaveGrant;

	/**
	 * コンストラクタ
	 */
	public RsvLeaAggrPeriodWork(){

		this.period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.endWork = new RsvLeaNextDayAfterPeriodEndWork();
		this.grantAtr = false;
		this.afterGrant = false;
		this.lapsedAtr = false;
		this.maxDays = new MaxDaysRetention(0);
		this.reserveLeaveGrant = Optional.empty();
	}

	/**
	 * ファクトリー
	 * @param period 期間
	 * @param nextDayAfterPeriodEnd 期間終了後翌日
	 * @param grantAtr 付与フラグ
	 * @param afterGrant 付与後
	 * @param lapsedAtr 消滅フラグ
	 * @param maxDays 上限日数
	 * @param reserveLeaveGrant 積立年休付与
	 * @return 積立年休集計期間WORK
	 */
	public static RsvLeaAggrPeriodWork of(
			DatePeriod period,
			RsvLeaNextDayAfterPeriodEndWork endWork,
			boolean grantAtr,
			boolean afterGrant,
			boolean lapsedAtr,
			MaxDaysRetention maxDays,
			Optional<NextReserveLeaveGrant> reserveLeaveGrant){

		RsvLeaAggrPeriodWork domain = new RsvLeaAggrPeriodWork();
		domain.period = period;
		domain.endWork = endWork;
		domain.grantAtr = grantAtr;
		domain.afterGrant = afterGrant;
		domain.lapsedAtr = lapsedAtr;
		domain.maxDays = maxDays;
		domain.reserveLeaveGrant = reserveLeaveGrant;
		return domain;
	}
}
