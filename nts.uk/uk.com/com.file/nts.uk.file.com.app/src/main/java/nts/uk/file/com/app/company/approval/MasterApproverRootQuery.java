package nts.uk.file.com.app.company.approval;

import lombok.Value;
import nts.arc.time.GeneralDate;

@Value
public class MasterApproverRootQuery {

	private int sysAtr;
	private GeneralDate baseDate;
	private boolean chkCompany;
	private boolean chkWorkplace;
	private boolean chkPerson;

}
