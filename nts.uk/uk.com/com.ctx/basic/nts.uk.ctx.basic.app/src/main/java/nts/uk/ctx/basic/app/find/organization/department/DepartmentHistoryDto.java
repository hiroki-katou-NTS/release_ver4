package nts.uk.ctx.basic.app.find.organization.department;

import java.time.format.DateTimeFormatter;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class DepartmentHistoryDto {

	private String startDate;

	private String endDate;

	private String historyId;

	public DepartmentHistoryDto(String historyId, GeneralDate startDate, GeneralDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		this.startDate = startDate.localDate().format(formatter);
		this.endDate = endDate.localDate().format(formatter);
		this.historyId = historyId;
	}

}