package nts.uk.screen.at.app.dailyperformance.correction.lock.button;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPHeaderDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DPDisplayLockParam {

	private List<DPAttendanceItem> lstAttendanceItem;

	private List<DailyPerformanceEmployeeDto> lstEmployee;

	private DateRange dateRange;

	private Integer mode;

	private Integer displayFormat;

	private List<DPDataDto> lstData;

	private List<DPHeaderDto> lstHeader;

	private boolean showLock;
	
	private Integer closureId;

}
