package nts.uk.ctx.basic.app.command.organization.position;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.basic.dom.organization.position.JobTitleRef;

@Getter
@Setter
public class AddJobTitleRefCommand {

	String authorizationCode;

	int referenceSettings;
//
//	public JobTitleRef toDomain() {
//		return JobTitleRef.createFromJavaType(companyCode, historyId, jobCode, authorizationCode, referenceSettings);
//
//	}
}
