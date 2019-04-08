package nts.uk.screen.at.app.dailyperformance.correction.lock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ClosureDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkapproval.ApproveRootStatusForEmpDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@AllArgsConstructor
public class DPLockDto {

	//日別実績のロック				
	//職場の就業確定				
	private Map<String, DatePeriod> lockDayAndWpl;
	
	//月別実績の承認				
	private Map<String, ApproveRootStatusForEmpDto> lockCheckMonth;
	
	//月別実績の確認
	private Pair<List<ClosureSidDto>, List<ConfirmationMonthDto>> lockConfirmMonth;
	
	//日別実績の承認
	private Map<String, ApproveRootStatusForEmpDto> lockCheckApprovalDay;
	
	//日別実績の確認
	private Map<String, Boolean> signDayMap;
	
	//過去実績
	private Pair<List<ClosureDto>, Map<Integer, DatePeriod>> lockHist;
	
	public DPLockDto(){
		this.lockCheckMonth = new HashMap<>();
		this.lockCheckApprovalDay = new HashMap<>();
		this.signDayMap = new HashMap<>();
		this.lockDayAndWpl = new HashMap<>();
	}
}
