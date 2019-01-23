package nts.uk.ctx.at.record.app.command.dailyimport;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
public class DailyDataImportExexuteCommand {

	private GeneralDate startDate;
	
	private GeneralDate endDate;
}
