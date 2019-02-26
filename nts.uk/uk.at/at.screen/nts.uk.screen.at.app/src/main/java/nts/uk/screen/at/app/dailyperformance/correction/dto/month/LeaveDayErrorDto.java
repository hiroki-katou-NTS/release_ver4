package nts.uk.screen.at.app.dailyperformance.correction.dto.month;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDayErrorDto {
	private boolean onlyErrorOldDb;

	private List<EmployeeMonthlyPerError> errorMonth;
	
	private Set<Pair<String, GeneralDate>> detailEmployeeError;
}
