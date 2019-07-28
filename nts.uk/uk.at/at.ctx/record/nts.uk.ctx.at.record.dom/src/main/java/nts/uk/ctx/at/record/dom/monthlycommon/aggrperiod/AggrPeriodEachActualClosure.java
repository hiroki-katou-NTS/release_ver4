package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実締め毎集計期間
 * @author shuichi_ishida
 */
@Getter
public class AggrPeriodEachActualClosure {

	/** 締めID */
	private ClosureId closureId;
	/** 締め日 */
	private ClosureDate closureDate;
	/** 年月 */
	private YearMonth yearMonth;
	/** 期間 */
	@Setter
	private DatePeriod period;
	/** 本来の締め期間 */
	private DatePeriod originalClosurePeriod;
	
	/** 排他エラー */
	@Setter
	private Boolean happendOptimistLockError;
	
	/**
	 * コンストラクタ
	 */
	public AggrPeriodEachActualClosure(){
		
		this.closureId = ClosureId.RegularEmployee;
		this.closureDate = new ClosureDate(1, true);
		this.yearMonth = YearMonth.of(GeneralDate.today().year(), GeneralDate.today().month());
		this.period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.originalClosurePeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		
		this.happendOptimistLockError = false;
	}
	
	/**
	 * ファクトリー
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param yearMonth 年月
	 * @param period 期間
	 * @param originalClosurePeriod 本来の締め期間
	 * @return 実締め毎集計期間
	 */
	public static AggrPeriodEachActualClosure of(ClosureId closureId, ClosureDate closureDate,
			YearMonth yearMonth, DatePeriod period, DatePeriod originalClosurePeriod){
		
		AggrPeriodEachActualClosure domain = new AggrPeriodEachActualClosure();
		domain.closureId = closureId;
		domain.closureDate = closureDate;
		domain.yearMonth = yearMonth;
		domain.period = period;
		domain.originalClosurePeriod = originalClosurePeriod;
		return domain;
	}
}
