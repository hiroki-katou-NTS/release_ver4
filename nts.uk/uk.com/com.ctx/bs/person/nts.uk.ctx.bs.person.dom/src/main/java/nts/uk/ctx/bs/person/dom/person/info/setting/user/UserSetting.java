package nts.uk.ctx.bs.person.dom.person.info.setting.user;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.enums.EnumAdaptor;

@Getter
public class UserSetting extends AggregateRoot{
	private String employeeId;
	private EmpCodeValType empCodeValType;
	private CardNoValType cardNoValType;
	private RecentRegType recentRegType;
	private String empCodeLetter;
	private String cardNoLetter;
	
	private UserSetting(String employeeId, int empCodeValType, int cardNoValType, 
			int recentRegType, String empCodeLetter, String cardNoLetter){
		this.employeeId = employeeId;
		this.empCodeValType = EnumAdaptor.valueOf(empCodeValType,EmpCodeValType.class);
		this.cardNoValType = EnumAdaptor.valueOf(cardNoValType, CardNoValType.class);
		this.recentRegType = EnumAdaptor.valueOf(recentRegType, RecentRegType.class);
		this.empCodeLetter = empCodeLetter;
		this.cardNoLetter = cardNoLetter;
	}
	
	public static UserSetting generateFullObject(String employeeId, int empCodeValType, int cardNoValType, 
			int recentRegType, String empCodeLetter, String cardNoLetter){
		return new UserSetting( employeeId,  empCodeValType,  cardNoValType, 
				 recentRegType,  empCodeLetter,  cardNoLetter);
	}
}
