package nts.uk.screen.at.ws.cmm040.WorkLocationWS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkLocationCodeParam {
	
	private String workLocationCode;
	
	public WorkLocationCodeParam(String workLocationCode) {
		super();
		this.workLocationCode = workLocationCode;
	}


}
