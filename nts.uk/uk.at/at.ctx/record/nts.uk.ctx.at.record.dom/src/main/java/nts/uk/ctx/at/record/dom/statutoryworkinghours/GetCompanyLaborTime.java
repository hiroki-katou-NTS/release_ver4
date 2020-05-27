package nts.uk.ctx.at.record.dom.statutoryworkinghours;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.statutoryworkinghours.GetCompanyLaborTimeImpl.Require;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

public interface GetCompanyLaborTime {

	Optional<WorkingTimeSetting> getComWorkingTimeSetting(String companyId,
			 WorkingSystem workingSystem);

	Optional<WorkingTimeSetting> getComWorkingTimeSettingRequire(Require require, String companyId,
			WorkingSystem workingSystem);
	
}
