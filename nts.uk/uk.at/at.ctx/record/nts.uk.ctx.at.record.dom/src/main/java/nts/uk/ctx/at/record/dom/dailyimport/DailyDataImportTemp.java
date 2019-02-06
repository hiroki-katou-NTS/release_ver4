package nts.uk.ctx.at.record.dom.dailyimport;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
public class DailyDataImportTemp {
	
	private String companyCode;
	
	private String employeeCode;

    private GeneralDate ymd;
    
    private String workType;

	private Optional<String> workTime;

	private Optional<String> startTime;
	
	private Optional<String> endTime;
	
	private Optional<String> breakStart1;

	private Optional<String> breakEnd1;

	private Optional<String> breakStart2;
	
	private Optional<String> breakEnd2;
}
