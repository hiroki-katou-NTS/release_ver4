package nts.uk.ctx.at.record.app.command.standardtime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class UpdateAgreementUnitSettingCommand {
	
	private int classificationUseAtr;

	private int employmentUseAtr;

	private int workPlaceUseAtr;
}
