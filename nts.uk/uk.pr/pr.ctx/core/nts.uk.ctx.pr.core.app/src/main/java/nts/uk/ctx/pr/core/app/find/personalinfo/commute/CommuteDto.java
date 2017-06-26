package nts.uk.ctx.pr.core.app.find.personalinfo.commute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteFee;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommuteDto {	
	private String commuNotaxLimitCode;
	private String commuNotaxLimitName;
	private double commuNotaxLimitValue;
}
