package nts.uk.ctx.basic.app.find.organization.workplace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class WorkPlaceHistoryDto {

	private String startDate;

	private String endDate;

	private String historyId;

	public WorkPlaceHistoryDto(String historyId, GeneralDate startDate, GeneralDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		this.startDate = startDate.localDate().format(formatter);
		this.endDate = endDate.localDate().format(formatter);
		this.historyId = historyId;
	}

}
