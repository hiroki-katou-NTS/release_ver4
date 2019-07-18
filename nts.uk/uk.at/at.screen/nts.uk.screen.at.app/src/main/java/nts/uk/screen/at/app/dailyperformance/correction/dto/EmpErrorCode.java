package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@NoArgsConstructor
public class EmpErrorCode extends EmpAndDate {

	@Getter
	private String errorCode;
	
	@Getter
	private Integer itemId;
	
	@Getter
	private Boolean onlyErrorHoliday;
	
	public EmpErrorCode(String employeeId, GeneralDate date, String errorCode, Integer itemId) {
		super(employeeId, date);
		this.errorCode = errorCode;
		this.itemId = itemId;
	}
	
	public EmpErrorCode(String employeeId, GeneralDate date, String errorCode, Integer itemId, Boolean onlyErrorHoliday) {
		super(employeeId, date);
		this.errorCode = errorCode;
		this.itemId = itemId;
		this.onlyErrorHoliday = onlyErrorHoliday;
	}

}
