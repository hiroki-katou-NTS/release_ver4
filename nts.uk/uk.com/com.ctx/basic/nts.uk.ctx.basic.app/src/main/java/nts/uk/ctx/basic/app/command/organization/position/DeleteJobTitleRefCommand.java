package nts.uk.ctx.basic.app.command.organization.position;

import lombok.Value;
import nts.uk.ctx.basic.dom.organization.position.JobTitleRef;

@Value
public class DeleteJobTitleRefCommand {
	String companyCode;

	String historyId;

	String jobCode;

	String authCode;

	int referenceSettings;

	public JobTitleRef toDomain() {
		return JobTitleRef.createFromJavaType(companyCode, historyId, jobCode, authCode, referenceSettings);

	}
}
