package nts.uk.ctx.basic.pub.system.bank.personaccount;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonBankAccountDto {
	@Getter
    private String companyCode;
	
	@Getter
    private String personID;
	
	@Getter
    private String histId;
	
	@Getter
    private int startYearMonth;
	
	@Getter
    private int endYearMonth;
	
	@Getter
    private PersonUseSettingDto useSet1;
    
	@Getter
    private PersonUseSettingDto useSet2;
    
	@Getter
    private PersonUseSettingDto useSet3;
    
	@Getter
    private PersonUseSettingDto useSet4;
    
	@Getter
    private PersonUseSettingDto useSet5;

	public PersonBankAccountDto(String companyCode, String personID, String histId, int startYearMonth, int endYearMonth,
			PersonUseSettingDto useSet1, PersonUseSettingDto useSet2, PersonUseSettingDto useSet3, PersonUseSettingDto useSet4,
			PersonUseSettingDto useSet5) {
		super();
		this.companyCode = companyCode;
		this.personID = personID;
		this.histId = histId;
		this.startYearMonth = startYearMonth;
		this.endYearMonth = endYearMonth;
		this.useSet1 = useSet1;
		this.useSet2 = useSet2;
		this.useSet3 = useSet3;
		this.useSet4 = useSet4;
		this.useSet5 = useSet5;
	}
}
