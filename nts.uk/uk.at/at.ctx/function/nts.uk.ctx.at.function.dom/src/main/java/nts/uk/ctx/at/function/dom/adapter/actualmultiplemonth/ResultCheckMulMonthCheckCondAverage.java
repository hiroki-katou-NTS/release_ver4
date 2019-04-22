package nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultCheckMulMonthCheckCondAverage {

	private boolean check;
	
	private BigDecimal avgValue;

	public ResultCheckMulMonthCheckCondAverage(boolean check, BigDecimal avgValue) {
		super();
		this.check = check;
		this.avgValue = avgValue;
	}
	
	
}
