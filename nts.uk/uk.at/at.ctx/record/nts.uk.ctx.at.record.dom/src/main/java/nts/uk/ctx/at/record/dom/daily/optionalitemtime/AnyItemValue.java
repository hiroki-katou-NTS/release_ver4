package nts.uk.ctx.at.record.dom.daily.optionalitemtime;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 任意項目値 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnyItemValue {

	/** 任意項目NO: 任意項目NO */
	private AnyItemNo itemNo;

	/** 回数: 日次任意回数 */
	private Optional<AnyItemTimes> times;

	/** 金額: 日次任意金額 */
	private Optional<AnyItemAmount> amount;

	/** 時間: 日次任意時間 */
	private Optional<AnyItemTime> time;

	public void markAsTimes() {
		this.time = Optional.of(new AnyItemTime(0));
		this.amount = Optional.of(new AnyItemAmount(0));
	}

	public void markAsTime() {
		this.times = Optional.of(new AnyItemTimes(BigDecimal.valueOf(0)));
		this.amount = Optional.of(new AnyItemAmount(0));
	}

	public void markAsAmount() {
		this.time = Optional.of(new AnyItemTime(0));
		this.times = Optional.of(new AnyItemTimes(BigDecimal.valueOf(0)));
	}
	
	public void updateTimes(AnyItemTimes times){
		this.times = Optional.ofNullable(times);
	}
	
	public void updateAmount(AnyItemAmount amount){
		this.amount = Optional.ofNullable(amount);
	}
	
	public void updateTime(AnyItemTime time){
		this.time = Optional.ofNullable(time);
	}
	
	public BigDecimal getRowTimes() {
		if(this.times.isPresent()) {
			return this.times.get().v();
		}
		return BigDecimal.ZERO;
	}

	public int getRowAmount() {
		if(this.amount.isPresent()) {
			return this.amount.get().v();
		}
		return 0;
	}
	
	public int getRowTime() {
		if(this.time.isPresent()) {
			return this.time.get().v();
		}
		return 0;
	}
}
