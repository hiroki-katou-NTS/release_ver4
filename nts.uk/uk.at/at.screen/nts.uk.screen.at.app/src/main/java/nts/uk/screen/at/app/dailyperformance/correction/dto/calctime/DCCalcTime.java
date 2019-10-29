package nts.uk.screen.at.app.dailyperformance.correction.dto.calctime;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellStateDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DCCalcTime {
   
	private List<DCCellEdit> cellEdits;
	
	private List<DailyRecordDto> dailyEdits;
	
	private List<DPCellStateDto> clearStates;
	
	private boolean errorFindMaster28;
	
	private boolean errorFindMaster29;
}
