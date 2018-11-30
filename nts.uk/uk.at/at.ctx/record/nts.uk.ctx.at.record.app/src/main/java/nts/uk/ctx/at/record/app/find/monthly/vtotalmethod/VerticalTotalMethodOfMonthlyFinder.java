package nts.uk.ctx.at.record.app.find.monthly.vtotalmethod;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

//import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.SpecDayMonthCountCon;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.VerticalTotalMethodOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.VerticalTotalMethodOfMonthlyRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class VerticalTotalMethodOfMonthlyFinder.
 *
 * @author HoangNDH
 */
@Stateless
public class VerticalTotalMethodOfMonthlyFinder {
	
	/** The repository. */
	@Inject
	VerticalTotalMethodOfMonthlyRepository repository;
	
	/**
	 * Find setting.
	 *
	 * @return the vertical total method of monthly
	 */
	public VerticalTotalMethodOfMonthlyDto findSetting () {
		String companyId = AppContexts.user().companyId();
		
		Optional<VerticalTotalMethodOfMonthly> optSetting = repository.findByCid(companyId);
		
		if (optSetting.isPresent()) {
			VerticalTotalMethodOfMonthly setting = optSetting.get();
			VerticalTotalMethodOfMonthlyDto dto = new VerticalTotalMethodOfMonthlyDto();
			dto.setAttendanceItemCountingMethod(setting.getTransferAttendanceDays().getTADaysCountCondition().value);
			return dto;
		}
		else {
			return null;
		}
	}
}
