package nts.uk.ctx.at.record.app.command.standardtime.yearsetting;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class UpdateAgreementYearSettingCommand {

	private String employeeId;

	private int yearValue;
	
	private Long errorOneYear;
	
	private Long alarmOneYear;
}
