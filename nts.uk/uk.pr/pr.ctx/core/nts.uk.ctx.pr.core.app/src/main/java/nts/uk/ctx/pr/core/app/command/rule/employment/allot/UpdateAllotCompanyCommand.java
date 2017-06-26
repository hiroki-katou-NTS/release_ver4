package nts.uk.ctx.pr.core.app.command.rule.employment.allot;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.shr.com.context.AppContexts;

@Getter
public class UpdateAllotCompanyCommand {
	 
	 private String payStmtCode;
	 private String bonusStmtCode;
	 private int startDate;
	 private int endDate;
	 private String historyId;
	 
	 
	 /**
	  * Convert to domain object from command values
	  * @return
	  */
	 public CompanyAllotSetting toDomain(){
		  return CompanyAllotSetting.createFromJavaType(AppContexts.user().companyCode(), 
		      this.historyId,  
		      this.startDate,
		      this.endDate,
		      this.payStmtCode, 
		      this.bonusStmtCode);
		 }
}
