package nts.uk.ctx.at.record.app.command.standardtime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class RemoveAgreementTimeOfClassificationCommand {
	
	private int laborSystemAtr;
	
	private String classificationCode;

}
