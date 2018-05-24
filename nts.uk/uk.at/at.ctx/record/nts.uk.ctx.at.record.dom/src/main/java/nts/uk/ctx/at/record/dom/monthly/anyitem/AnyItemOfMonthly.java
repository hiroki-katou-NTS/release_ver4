package nts.uk.ctx.at.record.dom.monthly.anyitem;

import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyAmountMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimeMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimesMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * 月別実績の任意項目
 * @author shuichu_ishida
 */
@Getter
public class AnyItemOfMonthly extends AggregateRoot {

	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;
	/** 任意項目ID */
	private final int anyItemId;
	
	/** 時間 */
	private Optional<AnyTimeMonth> time;
	/** 回数 */
	private Optional<AnyTimesMonth> times;
	/** 金額 */
	private Optional<AnyAmountMonth> amount;
	
	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param anyItemId 任意項目ID
	 */
	public AnyItemOfMonthly(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int anyItemId){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.anyItemId = anyItemId;
		this.time = Optional.empty();
		this.times = Optional.empty();
		this.amount = Optional.empty();
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param anyItemId 任意項目ID
	 * @param time 時間
	 * @param times 回数
	 * @param amount 金額
	 * @return 月別実績の任意項目
	 */
	public static AnyItemOfMonthly of(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			int anyItemId,
			Optional<AnyTimeMonth> time,
			Optional<AnyTimesMonth> times,
			Optional<AnyAmountMonth> amount){
		
		AnyItemOfMonthly domain = new AnyItemOfMonthly(employeeId, yearMonth, closureId, closureDate, anyItemId);
		domain.time = time;
		domain.times = times;
		domain.amount = amount;
		return domain;
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AnyItemOfMonthly target){
		
		if (target.time.isPresent()){
			if (this.time.isPresent()){
				this.time = Optional.of(new AnyTimeMonth(this.time.get().v() + target.time.get().v()));
			}
			else {
				this.time = Optional.of(new AnyTimeMonth(target.time.get().v()));
			}
		}
		if (target.times.isPresent()){
			if (this.times.isPresent()){
				this.times = Optional.of(new AnyTimesMonth(
						this.times.get().v().doubleValue() + target.times.get().v().doubleValue()));
			}
			else {
				this.times = Optional.of(new AnyTimesMonth(target.times.get().v().doubleValue()));
			}
		}
		if (target.amount.isPresent()){
			if (this.amount.isPresent()){
				this.amount = Optional.of(new AnyAmountMonth(this.amount.get().v() + target.amount.get().v()));
			}
			else {
				this.amount = Optional.of(new AnyAmountMonth(target.amount.get().v()));
			}
		}
	}
}
