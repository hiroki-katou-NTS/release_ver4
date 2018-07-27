package nts.uk.ctx.at.function.dom.holidaysremaining.report;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.holidaysremaining.HolidaysRemainingManagement;

@Getter
@Setter
public class HolidayRemainingDataSource {
	private GeneralDate startMonth;
	private GeneralDate endMonth;
	private String outputItemSettingCode;
	private int pageBreak;
	private GeneralDate baseDate;
	private HolidaysRemainingManagement holidaysRemainingManagement;
	private boolean isSameCurrentMonth;
	private List<String> empIds;
	private Map<String, HolidaysRemainingEmployee> mapEmployees;

	public HolidayRemainingDataSource(String startMonth, String endMonth, String outputItemSettingCode, int pageBreak,
			String baseDate, HolidaysRemainingManagement holidaysRemainingManagement, boolean isSameCurrentMonth,
			List<String> listEmployeeIds, Map<String, HolidaysRemainingEmployee> mapEmployees) {
		super();
		this.startMonth = GeneralDate.fromString(startMonth, "yyyy/MM/dd");
		this.endMonth = GeneralDate.fromString(endMonth, "yyyy/MM/dd");
		this.outputItemSettingCode = outputItemSettingCode;
		this.pageBreak = pageBreak;
		this.baseDate = GeneralDate.fromString(baseDate, "yyyy/MM/dd");
		this.holidaysRemainingManagement = holidaysRemainingManagement;
		this.isSameCurrentMonth = isSameCurrentMonth;
		this.empIds = listEmployeeIds;
		this.mapEmployees = mapEmployees;
	}
}
