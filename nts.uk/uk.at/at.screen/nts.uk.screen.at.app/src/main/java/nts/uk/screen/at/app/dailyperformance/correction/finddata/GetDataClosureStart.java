package nts.uk.screen.at.app.dailyperformance.correction.finddata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;

@Stateless
public class GetDataClosureStart implements IGetDataClosureStart {

	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;

	private static Map<String, Optional<GeneralDate>> MAP_SID_DATE_CLOSURE_START = new HashMap<>();

	//社員に対応する締め開始日を取得する 
	// performance
	@Override
	public Optional<GeneralDate> getDataClosureStart(String employeeId) {
		if (MAP_SID_DATE_CLOSURE_START.containsKey(employeeId)) {
			return MAP_SID_DATE_CLOSURE_START.get(employeeId);
		} else {
			Optional<GeneralDate> result = getClosureStartForEmployee.algorithm(employeeId);
			MAP_SID_DATE_CLOSURE_START.put(employeeId, result);
			return result;
		}
	}

	@Override
	public void clearClosureStart() {
		MAP_SID_DATE_CLOSURE_START.clear();
	}

}
