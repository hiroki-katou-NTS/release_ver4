package nts.uk.ctx.basic.app.command.organization.payclassification;

import lombok.Data;

@Data
public class UpdatePayClassificationCommand {
	
	private String payClassificationCode;
	
	private String payClassificationName;
	
	private String memo;

	
	
}
