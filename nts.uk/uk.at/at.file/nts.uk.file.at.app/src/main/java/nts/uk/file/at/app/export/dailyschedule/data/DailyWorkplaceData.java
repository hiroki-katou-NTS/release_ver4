package nts.uk.file.at.app.export.dailyschedule.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import nts.uk.file.at.app.export.dailyschedule.totalsum.WorkplaceTotal;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * Instantiates a new daily workplace data.
 * @author HoangNDH
 */
@Data
public class DailyWorkplaceData {
	
	/** The workplace code. */
	public String workplaceCode;
	
	/** The workplace name. */
	public String workplaceName;
	
	/** The level. */
	public int level;
	
	/** Parent workplace */
	public DailyWorkplaceData parent;
	
	/** The lst daily personal data. */
	public List<DailyPersonalPerformanceData> lstDailyPersonalData = new ArrayList<>();
	
	/** The lst child workplace data. */
	public Map<String, DailyWorkplaceData> lstChildWorkplaceData = new HashMap<String, DailyWorkplaceData>();
	
	/** The workplace total. */
	public WorkplaceTotal workplaceTotal;
	
	/** The workplace hierarchy total. */
	public WorkplaceTotal workplaceHierarchyTotal;
	
	// Period for optimizing performance
	public DatePeriod period;
	
	// Employee data & child workplaces' employee data presentation
	public boolean hasData = false;
}
