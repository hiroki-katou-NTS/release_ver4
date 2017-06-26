package nts.uk.ctx.pr.core.app.find.paymentdata;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.paymentdata.CommuNotaxLimit;

@Value
public class CommuteNotaxLimitDto {
	
	private String commuNotaxLimitCode;
	
	
	private String commuNotaxLimitName;
	
	
	private BigDecimal commuNotaxLimitValue;
	
	public static CommuteNotaxLimitDto fromDomain(CommuNotaxLimit domain){
		return new CommuteNotaxLimitDto(domain.getCommuNotaxLimitCode().v(), 
										domain.getCommuNotaxLimitName().v(), 
										domain.getCommuNotaxLimitValue().v());
	}
}
