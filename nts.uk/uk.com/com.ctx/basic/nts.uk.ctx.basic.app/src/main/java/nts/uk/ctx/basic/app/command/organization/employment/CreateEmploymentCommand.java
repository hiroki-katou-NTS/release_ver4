package nts.uk.ctx.basic.app.command.organization.employment;

import lombok.Getter;
import nts.uk.ctx.basic.dom.organization.employment.Employment;
import nts.uk.shr.com.context.AppContexts;

@Getter
public class CreateEmploymentCommand {	
	private String employmentCode;
	private String employmentName;
	private String memo;
	private int closeDateNo;
	private int processingNo;
	private int statutoryHolidayAtr;
	private String employementOutCd;
	private int displayFlg;
	
	public Employment toDomain(){
		return Employment.createFromJavaType(AppContexts.user().companyCode(),
				this.employmentCode,
				this.employmentName,
				this.closeDateNo, 
				this.memo,
				this.processingNo, 
				this.statutoryHolidayAtr,
				this.employementOutCd, 
				this.displayFlg);
	}
}
