package nts.uk.ctx.basic.app.command.organization.position;

import lombok.Value;
import nts.uk.ctx.basic.dom.organization.position.JobTitleRef;

@Value
public class UpdateTittleRefCommand {
	String companyCode;

	String historyId;

	String jobCode;

	String authorizationCode;

	int referenceSettings;

	public JobTitleRef toDomain() {
		return JobTitleRef.createFromJavaType(companyCode, historyId, jobCode, authorizationCode, referenceSettings);

	}
}
