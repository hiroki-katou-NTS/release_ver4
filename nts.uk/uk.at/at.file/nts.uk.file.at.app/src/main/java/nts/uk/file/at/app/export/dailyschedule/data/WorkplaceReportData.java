package nts.uk.file.at.app.export.dailyschedule.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalValue;
import nts.uk.file.at.app.export.dailyschedule.totalsum.WorkplaceTotal;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkplaceReportData.
 * @author HoangNDH
 */
@Data
public class WorkplaceReportData {
	
	/** The workplace id. */
	public String workplaceCode; // 職場コード
	
	/** The workplace name. */
	public String workplaceName; // 職場総称
	
	/** The level. */
	public int level;
	
	/**  Parent workplace. */
	public WorkplaceReportData parent;
	
	/** The lst employee report data. */
	public List<EmployeeReportData> lstEmployeeReportData = new ArrayList<>();
	
	/** The lst child workplace report data. */
	public Map<String, WorkplaceReportData> lstChildWorkplaceReportData = new TreeMap<>();
	
	/** The workplace total. */
	public WorkplaceTotal workplaceTotal;
	
	/** The gross total. */
	private List<TotalValue> grossTotal;
	
	// Period for optimizing performance
	public DatePeriod period;

	// Employee data & child workplaces' employee data presentation
	public boolean hasData = false;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorkplaceInfo) {
			WorkplaceInfo data = (WorkplaceInfo) obj;
			return StringUtils.equals(this.workplaceCode, data.getWorkplaceId());
		}
		else {
			WorkplaceReportData data = (WorkplaceReportData) obj;
			return StringUtils.equals(this.workplaceCode, data.workplaceCode);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Instantiates a new workplace report data.
	 *
	 * @param workplaceCode the workplace code
	 */
	public WorkplaceReportData(String workplaceCode) {
		super();
		this.workplaceCode = workplaceCode;
	}

	/**
	 * Instantiates a new workplace report data.
	 */
	public WorkplaceReportData() {
		super();
	}
	
	
}
