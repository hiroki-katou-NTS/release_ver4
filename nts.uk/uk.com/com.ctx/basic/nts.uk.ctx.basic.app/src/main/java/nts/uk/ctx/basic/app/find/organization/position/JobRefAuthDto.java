package nts.uk.ctx.basic.app.find.organization.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.basic.dom.organization.position.JobRefAuth;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRefAuthDto {

	
	private String authCode;
	
	private String authName;
	
	private int referenceSettings;
	

	public static JobRefAuthDto fromDomain(JobRefAuth domain) {
		return new JobRefAuthDto(
				domain.getAuthCode().v(),
				domain.getAuthName().v(),
				domain.getReferenceSettings().value);
	}
}
