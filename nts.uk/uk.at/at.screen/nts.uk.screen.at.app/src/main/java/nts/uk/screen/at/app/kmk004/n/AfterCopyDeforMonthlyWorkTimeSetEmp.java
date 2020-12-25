package nts.uk.screen.at.app.kmk004.n;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentList;

/**
 * UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.N：雇用別法定労働時間の登録（変形労働）.メニュー別OCD.雇用別月単位労働時間（変形労働）を複写した時
 * 
 * @author tutt
 *
 */
public class AfterCopyDeforMonthlyWorkTimeSetEmp {

	@Inject
	private EmploymentList employmentList;
	
	public List<String> afterCopyDeforMonthlyWorkTimeSetEmp() {
		
		// 雇用リストを表示する
		return this.employmentList.get(LaborWorkTypeAttr.DEFOR_LABOR).stream().map(x -> x.employmentCode)
				.collect(Collectors.toList());
	}
	
}
