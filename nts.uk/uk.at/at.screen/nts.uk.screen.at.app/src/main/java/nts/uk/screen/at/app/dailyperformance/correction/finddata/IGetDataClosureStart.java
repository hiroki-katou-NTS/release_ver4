package nts.uk.screen.at.app.dailyperformance.correction.finddata;

import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface IGetDataClosureStart {
	
	Optional<GeneralDate> getDataClosureStart(String employeeId);
	
	void clearClosureStart();

}
