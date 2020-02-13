package nts.uk.ctx.hr.shared.dom.notice.report.registration.person;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
@Getter
@AllArgsConstructor
public class ApprovalStateHrImport {
	/**インスタンスID*/
	private String rootStateID;
	/**対象日*/
	private GeneralDate appDate;
	/**対象者ID*/
	private String employeeID;
	/**承認フェーズ*/
	private List<PhaseStateHrImport> lstPhaseState;
}
