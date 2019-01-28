package nts.uk.screen.at.app.dailyperformance.correction.loadupdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CellEdit;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPHeaderDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DPPramLoadRow {
	
	private List<DPAttendanceItem> lstAttendanceItem;
	
	private List<DailyPerformanceEmployeeDto> lstEmployee;
	
	private DateRange dateRange;
	
	private Integer mode;
	
	private Integer displayFormat;
	
	private List<DPDataDto> lstData;
	
	private List<DPHeaderDto> lstHeader;
	
	private Set<String> autBussCode;
	
	private GeneralDate dateMonth;
	
	private Boolean onlyLoadMonth;
	
	private List<DailyRecordDto> dailys;
	
	private DateRange dateExtract;
	
	private IdentityProcessUseSetDto identityProcess;
	
	private boolean showLock;
	
	private List<CellEdit> cellEdits;
	
	private List<Pair<String, GeneralDate>> lstSidDateDomainError = new ArrayList<>();
		
	private boolean errorAllSidDate = false;
	
}
