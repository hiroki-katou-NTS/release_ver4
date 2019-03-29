package nts.uk.screen.at.app.monthlyperformance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckEmpEralOuput {
	private String employId;
	
	private TypeErrorAlarm typeAtr;

	public CheckEmpEralOuput(String employId, TypeErrorAlarm typeAtr) {
		super();
		this.employId = employId;
		this.typeAtr = typeAtr;
	}
	
}
