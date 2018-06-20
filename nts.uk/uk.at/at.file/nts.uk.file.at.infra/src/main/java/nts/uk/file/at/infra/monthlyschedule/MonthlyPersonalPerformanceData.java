package nts.uk.file.at.infra.monthlyschedule;

import java.util.List;

import lombok.Data;
import nts.uk.file.at.app.export.dailyschedule.ActualValue;

/**
 * Instantiates a new daily personal performance data.
 * @author HoangNDH
 */
@Data
public class MonthlyPersonalPerformanceData {
	
	/** The error alarm code. */
	public String errorAlarmCode;
	
	/** The employee name. */
	public String employeeName;
	
	/** The detailed error data. */
	public String detailedErrorData;
	
	/** The actual value. */
	public List<ActualValue> actualValue;
}
