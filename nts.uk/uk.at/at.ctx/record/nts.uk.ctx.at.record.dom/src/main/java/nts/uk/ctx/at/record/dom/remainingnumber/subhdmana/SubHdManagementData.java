/**
 * 
 */
package nts.uk.ctx.at.record.dom.remainingnumber.subhdmana;

import lombok.Getter;
import nts.arc.time.GeneralDate;

/**
 * @author nam.lh
 *
 */
@Getter
public class SubHdManagementData {
	private String employeeId;

	/**
	 * 休出
	 */
	private Boolean checkedHoliday;

	/**
	 * 年月日
	 */
	private GeneralDate dateHoliday;

	/**
	 * 休出日数
	 */
	private Double selectedCodeHoliday;
	/**
	 * 期限
	 */
	private GeneralDate duedateHoliday;

	// Domain CompensatoryDayOffManaData
	/**
	 * 代休
	 */
	private Boolean checkedSubHoliday;
	/**
	 * 年月日
	 */
	private GeneralDate dateSubHoliday;

	/**
	 * 代休日数
	 */
	private Double selectedCodeSubHoliday;

	// Option checked
	/**
	 * 分割消化
	 */
	private Boolean checkedSplit;
	/**
	 * 年月日
	 */
	private GeneralDate dateOptionSubHoliday;
	/**
	 * 代休日数
	 */
	private Double selectedCodeOptionSubHoliday;

	// Domain LeaveComDayOffManagement
	private String dayRemaining;

	/**
	 * @param employeeId
	 * @param checkedHoliday
	 * @param dateHoliday
	 * @param selectedCodeHoliday
	 * @param duedateHoliday
	 * @param checkedSubHoliday
	 * @param dateSubHoliday
	 * @param selectedCodeSubHoliday
	 * @param checkedSplit
	 * @param dateOptionSubHoliday
	 * @param selectedCodeOptionSubHoliday
	 * @param dayRemaining
	 */
	public SubHdManagementData(String employeeId, Boolean checkedHoliday, GeneralDate dateHoliday,
			Double selectedCodeHoliday, GeneralDate duedateHoliday, Boolean checkedSubHoliday,
			GeneralDate dateSubHoliday, Double selectedCodeSubHoliday, Boolean checkedSplit,
			GeneralDate dateOptionSubHoliday, Double selectedCodeOptionSubHoliday, String dayRemaining) {
		super();
		this.employeeId = employeeId;
		this.checkedHoliday = checkedHoliday;
		this.dateHoliday = dateHoliday;
		this.selectedCodeHoliday = selectedCodeHoliday;
		this.duedateHoliday = duedateHoliday;
		this.checkedSubHoliday = checkedSubHoliday;
		this.dateSubHoliday = dateSubHoliday;
		this.selectedCodeSubHoliday = selectedCodeSubHoliday;
		this.checkedSplit = checkedSplit;
		this.dateOptionSubHoliday = dateOptionSubHoliday;
		this.selectedCodeOptionSubHoliday = selectedCodeOptionSubHoliday;
		this.dayRemaining = dayRemaining;
	}
}
