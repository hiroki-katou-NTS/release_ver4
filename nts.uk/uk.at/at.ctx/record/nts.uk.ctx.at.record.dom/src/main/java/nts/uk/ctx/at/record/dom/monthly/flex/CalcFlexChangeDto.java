package nts.uk.ctx.at.record.dom.monthly.flex;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalcFlexChangeDto {

	 private String employeeId;
     private GeneralDate date;
     private List<WorkingConditionItem> wCItems;
     public static CalcFlexChangeDto createCalcFlexDto(String employeeId, GeneralDate date){
    	 return new CalcFlexChangeDto(employeeId, date);
     }

	private CalcFlexChangeDto(String employeeId, GeneralDate date) {
		this.employeeId = employeeId;
		this.date = date;
	}
	
	public CalcFlexChangeDto createWCItem(List<WorkingConditionItem> wCItems) {
		this.wCItems = wCItems;
		return this;
	}
}
