/**
 * 6:00:49 PM Aug 28, 2017
 */
package nts.uk.screen.at.ws.dailyperformance.correction;

import java.util.List;

import nts.uk.screen.at.app.dailyperformance.correction.dto.CorrectionOfDailyPerformance;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ObjectShare;

/**
 * @author hungnm
 *
 */
public class DPParams {
	public DateRange dateRange;
	public List<DailyPerformanceEmployeeDto> lstEmployee;
	public Integer displayFormat;
	public Integer initScreen;
	public CorrectionOfDailyPerformance correctionOfDaily;
	// only use when click button A2_3
	public List<String> errorCodes;
	// only use when click button  A2_4
	public List<String> formatCodes;
    public ObjectShare objectShare;	
}
