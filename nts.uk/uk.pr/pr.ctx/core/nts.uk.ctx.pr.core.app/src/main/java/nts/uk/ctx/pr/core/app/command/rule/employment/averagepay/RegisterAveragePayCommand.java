package nts.uk.ctx.pr.core.app.command.rule.employment.averagepay;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@NoArgsConstructor
public class RegisterAveragePayCommand {
	private int roundTimingSet;
	private int attendDayGettingSet;
	private int roundDigitSet;
	private BigDecimal exceptionPayRate;
	private List<String> selectedSalaryItems;
	private List<String> selectedAttendItems;
}
