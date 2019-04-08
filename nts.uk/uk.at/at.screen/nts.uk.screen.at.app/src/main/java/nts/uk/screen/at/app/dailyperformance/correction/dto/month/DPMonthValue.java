package nts.uk.screen.at.app.dailyperformance.correction.dto.month;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;

@NoArgsConstructor
@Getter
public class DPMonthValue extends DPMonthParent {
	private List<DPItemValue> items;
	private String redConditionMessage;
	private Boolean hasFlex;
	private Boolean needCallCalc;

	public DPMonthValue(String employeeId, int yearMonth, int closureId, ClosureDateDto closureDate,
			List<DPItemValue> items) {
		super(employeeId, yearMonth, closureId, closureDate);
		this.items = items;
	}

}
