package nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class PesionInforImport {
	private String pid;

	private String pname;

	private GeneralDate entryDate;

	private int gender;

	private GeneralDate birthDay;

	/** The employee id. */
	private String employeeId;

	/** The employee code. */
	private String employeeCode;

	private GeneralDate retiredDate;
	
	private String sMail;
}
