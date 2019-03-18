package nts.uk.ctx.at.shared.dom.adapter.dailyperformance;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
/**
 * Importルート状況
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteSituationImport {
	/**
	 * 対象日
	 */
	private GeneralDate date;
	
	/**
	 * 対象者
	 */
	private String employeeID;
	
	/**
	 * ルートの状況
	 */
	private Integer approverEmpState;
	
	/**
	 * 承認状況
	 */
	private Optional<ApprovalStatusImport> approvalStatus;
}
