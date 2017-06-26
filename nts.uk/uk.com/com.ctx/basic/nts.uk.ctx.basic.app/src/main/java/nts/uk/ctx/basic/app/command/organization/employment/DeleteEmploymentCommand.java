package nts.uk.ctx.basic.app.command.organization.employment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteEmploymentCommand {
	private String employmentCode;
	private int displayAtr;
}
