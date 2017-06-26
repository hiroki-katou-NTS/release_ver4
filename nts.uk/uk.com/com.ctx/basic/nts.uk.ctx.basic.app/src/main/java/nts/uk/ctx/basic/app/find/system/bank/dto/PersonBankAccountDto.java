package nts.uk.ctx.basic.app.find.system.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBankAccountDto {
    private String companyCode;
	
    private String personID;
	
    private String histId;
	
    private int startYearMonth;
	
    private int endYearMonth;
	
    private PersonUseSettingDto useSet1;
    
    private PersonUseSettingDto useSet2;
    
    private PersonUseSettingDto useSet3;
    
    private PersonUseSettingDto useSet4;
    
    private PersonUseSettingDto useSet5;
}
