package nts.uk.screen.at.app.dailyperformance.correction.dto.calctime;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DCCalcTimeParam {
	
	 private List<DailyRecordDto> dailyEdits;
	
     private List<DPItemValue> itemEdits;
     
     private Boolean changeSpr31;
     
     private Boolean changeSpr34;
}
