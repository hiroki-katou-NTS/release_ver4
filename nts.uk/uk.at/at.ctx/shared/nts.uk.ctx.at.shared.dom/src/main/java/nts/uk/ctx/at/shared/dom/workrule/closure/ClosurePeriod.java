package nts.uk.ctx.at.shared.dom.workrule.closure;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 締め期間
 * @author shuichu_ishida
 */
@Getter
public class ClosurePeriod {

	/** 締めID */
	private ClosureId closureId;
	/** 日付 */
	private ClosureDate closureDate;
	/** 年月 */
	private YearMonth yearMonth;
	/** 期間 */
	private DatePeriod period;

	/**
	 * コンストラクタ
	 */
	public ClosurePeriod(){
		this.closureId = ClosureId.RegularEmployee;
		this.closureDate = new ClosureDate(1, true);
		this.yearMonth = YearMonth.of(GeneralDate.today().year(), GeneralDate.today().month());
		this.period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
	}

	/**
	 * ファクトリー
	 * @param closureId 締めID
	 * @param closureDate 日付　（締め日）
	 * @param yearMonth 年月
	 * @param period 期間
	 * @return 締め期間
	 */
	public static ClosurePeriod of(
			ClosureId closureId, ClosureDate closureDate, YearMonth yearMonth, DatePeriod period){

		ClosurePeriod domain = new ClosurePeriod();
		domain.closureId = closureId;
		domain.closureDate = closureDate;
		domain.yearMonth = yearMonth;
		domain.period = period;
		return domain;
	}

	public ClosureMonth closureMonth() {
		return new ClosureMonth(this.yearMonth, this.closureId.value, this.closureDate);
	}
}
