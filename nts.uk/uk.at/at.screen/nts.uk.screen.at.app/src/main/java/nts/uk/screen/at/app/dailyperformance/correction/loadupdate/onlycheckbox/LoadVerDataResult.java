package nts.uk.screen.at.app.dailyperformance.correction.loadupdate.onlycheckbox;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadVerDataResult {
	private List<DailyRecordDto> lstDomainOld;
}
