package nts.uk.ctx.hr.shared.dom.employee;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.hr.shared.dom.adapter.EmployeeInfoQueryImport;
import nts.uk.ctx.hr.shared.dom.adapter.EmployeeInformationImport;

public interface EmployeeInformationAdaptor {

	/**
	 * 社員情報リストを取得
	 * 
	 * @param 個人IDリスト＜Optional>
	 * @param 社員IDリスト
	 * @param 基準日
	 * @return 社員情報のリスト
	 */
	public List<EmployeeInformationImport> getEmployeeInfos(Optional<List<String>> pIds, List<String> sIds,
			GeneralDate baseDate, Optional<Boolean> getDepartment, Optional<Boolean> getPosition,
			Optional<Boolean> getEmployment);

	public List<EmployeeInformationImport> find(EmployeeInfoQueryImport param);
}
