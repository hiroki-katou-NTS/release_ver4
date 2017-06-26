package nts.uk.ctx.basic.app.command.system.bank.personaccount;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.basic.dom.system.bank.personaccount.PersonUseSetting;

@NoArgsConstructor
@Data
public class UpdatePersonBankAccountCommand {
	
    private String companyCode;
	
    private String personID;
	
    private String histId;
	
    private int startYearMonth;
	
    private int endYearMonth;
	
    private PersonUseSetting useSet1;
    
    private PersonUseSetting useSet2;
    
    private PersonUseSetting useSet3;
    
    private PersonUseSetting useSet4;
    
    private PersonUseSetting useSet5;
} 
