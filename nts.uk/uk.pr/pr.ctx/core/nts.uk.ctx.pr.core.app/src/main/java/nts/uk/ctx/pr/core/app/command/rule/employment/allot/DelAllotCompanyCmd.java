package nts.uk.ctx.pr.core.app.command.rule.employment.allot;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.shr.com.context.AppContexts;

@Getter
@Setter
public class DelAllotCompanyCmd {
	
	private String companyCode;
	
	private String historyId;
	
	private int startDate;
	 
	private int endDate;	 
	 
	private String payStmtCode;
	 
	private String bonusStmtCode;
	
	public CompanyAllotSetting toDomain(String historyId, int endDate){
		return CompanyAllotSetting.createFromJavaType(
				AppContexts.user().companyCode(),
				historyId, 
				this.startDate, 
				endDate, 
				this.payStmtCode, 
				this.bonusStmtCode);
	}

}
