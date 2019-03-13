package nts.uk.ctx.at.shared.dom.adapter.dailyperformance;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Import基準社員の承認対象者
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppEmpStatusImport {

	/**
	 * 基準社員
	 */
	private String employeeID;
	
	/**
	 * 対象者承認状況リスト
	 */
	private List<RouteSituationImport> routeSituationLst;
}
