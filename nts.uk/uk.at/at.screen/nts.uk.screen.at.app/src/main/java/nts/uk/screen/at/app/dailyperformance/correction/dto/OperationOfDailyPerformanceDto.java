package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.SettingUnit;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationOfDailyPerformanceDto {
	
	private String companyId;

	private SettingUnit settingUnit;
}
