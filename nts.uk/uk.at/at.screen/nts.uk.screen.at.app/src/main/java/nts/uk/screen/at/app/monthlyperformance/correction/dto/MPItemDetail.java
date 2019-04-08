package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MPItemDetail {

	private String rowId;
	
	private int itemId;
	
	private String value;
	
	private String valueType;
	
	private String layoutCode;
	
	private String employeeId;
	
	private GeneralDate date;
	
	private Integer typeGroup;
}
