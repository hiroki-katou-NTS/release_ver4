package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;
@Data
@AllArgsConstructor
public class DPItemValue {
	
	private String rowId;
	
	private int itemId;
	
	private String value;
	
	private String valueType;
	
	private String layoutCode;
	
	private String employeeId;
	
	private GeneralDate date;
	
	private Integer typeGroup;
	
	public DPItemValue(String rowId, String employeeId, GeneralDate date, int itemId){
		this.rowId = rowId;
		this.employeeId = employeeId;
		this.date = date;
		this.itemId = itemId;
	}
}
