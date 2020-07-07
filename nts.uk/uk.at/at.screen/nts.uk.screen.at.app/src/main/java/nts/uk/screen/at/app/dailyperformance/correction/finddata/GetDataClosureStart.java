package nts.uk.screen.at.app.dailyperformance.correction.finddata;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;

@Stateless
public class GetDataClosureStart implements IGetDataClosureStart {

	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;

	// 社員に対応する締め開始日を取得する
	// performance
	@Override
	public Optional<GeneralDate> getDataClosureStart(String employeeId) {
		return getClosureStartForEmployee.algorithm(employeeId);
	}

}
