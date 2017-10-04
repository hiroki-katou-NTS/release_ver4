package nts.uk.ctx.bs.person.dom.person.info.setting.regHistory;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

@Getter
public class EmpRegHistory extends AggregateRoot {

	String registeredEmployeeID;
	String companyId;
	GeneralDate registeredDate;
	String lastRegEmployeeID;

	public EmpRegHistory(String registeredEmployeeID, String companyId, GeneralDate registeredDate,
			String lastRegEmployeeID) {
		this.registeredEmployeeID = registeredEmployeeID;
		this.companyId = companyId;
		this.registeredDate = registeredDate;
		this.lastRegEmployeeID = lastRegEmployeeID;
	}

	public static EmpRegHistory createFromJavaType(String registeredEmployeeID, String companyId,
			GeneralDate registeredDate, String lastRegEmployeeID) {
		return new EmpRegHistory(registeredEmployeeID, companyId, registeredDate, lastRegEmployeeID);
	}

}
