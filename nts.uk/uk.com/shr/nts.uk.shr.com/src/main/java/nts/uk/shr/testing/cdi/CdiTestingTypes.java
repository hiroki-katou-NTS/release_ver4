package nts.uk.shr.testing.cdi;

import javax.enterprise.context.ApplicationScoped;

import nts.arc.testing.cdi.TypeDefine;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.system.config.InitializeWhenDeploy;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@ApplicationScoped
public class CdiTestingTypes implements InitializeWhenDeploy {

	@Override
	public void initialize() {
		
		TypeDefine.CLASSES.put("dateperiod", DatePeriod.class);
		TypeDefine.CONVERTERS.put("dateperiod", v -> {
			String[] parts = v.toString().split("-");
			GeneralDate start = GeneralDate.fromString(parts[0], "yyyyMMdd");
			GeneralDate end = GeneralDate.fromString(parts[1], "yyyyMMdd");
			return new DatePeriod(start, end);
		});
		
	}

	
}
