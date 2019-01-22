/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.export.dailyschedule.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalCountDay;
import nts.uk.file.at.app.export.dailyschedule.totalsum.TotalValue;
import nts.uk.file.at.app.export.monthlyschedule.DetailedMonthlyPerformanceReportData;

/**
 * Instantiates a new employee report data.
 * @author HoangNDH
 */
@Data
public class EmployeeReportData {
	
	/** The employee id. */
	public String employeeId;
	
	/** The employee code. */
	public String employeeCode;
	
	/** The employee name. */
	public String employeeName;
	
	/** The employment name. */
	public String employmentName;
	
	/** The position. */
	public String position;
	
	/** The lst detailed performance. */
	public List<DetailedDailyPerformanceReportData> lstDetailedPerformance;
	
	/** The lst detailed monthly performance. */
	public List<DetailedMonthlyPerformanceReportData> lstDetailedMonthlyPerformance;
	
	/** The total count day. */
	public TotalCountDay totalCountDay = new TotalCountDay();
	
	/** The personal total. */
	public Map<Integer, TotalValue> mapPersonalTotal = new LinkedHashMap<>();
	
	/** The count. */
	public int count = 0;
	
	// Copy data
	public EmployeeReportData copyData() {
		EmployeeReportData newEmployee = new EmployeeReportData();
		newEmployee.employeeId = employeeId;
		newEmployee.employeeCode = employeeCode;
		newEmployee.employeeName = employeeName;
		newEmployee.employmentName = employmentName;
		newEmployee.position = position;
		newEmployee.lstDetailedPerformance = new ArrayList<>();
		return newEmployee;
	}

	/**
	 * Count item.
	 *
	 * @return the int
	 */
	public int countItem() {
		this.lstDetailedMonthlyPerformance.stream().forEach(item -> {
			this.count = this.count + item.getActualValue().size();
		});
		return this.count;
	}
}
