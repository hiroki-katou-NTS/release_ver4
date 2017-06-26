package nts.uk.ctx.basic.dom.system.bank.personaccount;


import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

public class PersonBankAccount extends AggregateRoot {
	
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
    private PersonUseSetting useSet1;
    
	@Getter
    private PersonUseSetting useSet2;
    
	@Getter
    private PersonUseSetting useSet3;
    
	@Getter
    private PersonUseSetting useSet4;
    
	@Getter
    private PersonUseSetting useSet5;

	public PersonBankAccount(String companyCode, String personID, String histId, int startYearMonth, int endYearMonth,
			PersonUseSetting useSet1, PersonUseSetting useSet2, PersonUseSetting useSet3, PersonUseSetting useSet4,
			PersonUseSetting useSet5) {
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
	
//	public static PersonBankAccount createFromJavaType(String)
	
}
