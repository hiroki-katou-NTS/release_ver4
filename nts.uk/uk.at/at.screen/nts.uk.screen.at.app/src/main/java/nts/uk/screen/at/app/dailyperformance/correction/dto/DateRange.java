/**
 * 5:18:35 PM Aug 23, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * @author hungnm
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRange {
	private GeneralDate startDate;
	private GeneralDate endDate;

	public List<GeneralDate> toListDate() {
		List<GeneralDate> lstDate = new ArrayList<>();
		GeneralDate element = this.startDate;
		while (element.beforeOrEquals(this.endDate)) {
			lstDate.add(element);
			element = element.addDays(1);
		}
		return lstDate;
	}
	
	public boolean inRange(GeneralDate dateCheck) {
		if(dateCheck.after(endDate) || dateCheck.before(startDate)) {
			return false;
		}
		return true;
	}
	
	public DateRange changeMonth(int addMonth) {
		GeneralDate startDateResult = startDate.addMonths(addMonth);
		GeneralDate endDateTemp = endDate.addMonths(addMonth);
		GeneralDate endDateResult = endDateTemp.lastDateInMonth() == endDateTemp.day()
				? GeneralDate.ymd(endDateTemp.year(), endDateTemp.month(), endDateTemp.lastDateInMonth())
				: endDateTemp;
		return new DateRange(startDateResult, endDateResult);
	}
}
