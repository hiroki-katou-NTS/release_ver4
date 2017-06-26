package nts.uk.ctx.basic.app.command.organization.classification;

import lombok.Getter;

@Getter
public class AddClassificationCommand {
	
	private String classificationCode;
	
	private String classificationName;
	
	private String memo;

}
