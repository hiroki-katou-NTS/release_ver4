package nts.uk.ctx.at.schedule.app.find.shift.schedulehorizontal;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
/**
 * 
 * @author yennth
 *
 */
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.shift.schedulehorizontal.repository.HoriTotalCategoryRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class HoriCalDaySetFinder {
	@Inject
	private HoriTotalCategoryRepository horiRep;
	
	public List<HoriCalDaySetDto> finder(){
		String companyId = AppContexts.user().companyId();
		return this.horiRep.findAllCal(companyId)
							.stream()
							.map(x -> {
								return new HoriCalDaySetDto(companyId,
															x.getCategoryCode().v(),
															x.getHalfDay().value,
															x.getYearHd().value,
															x.getSpecialHoliday().value,
															x.getHeavyHd().value);
							}).collect(Collectors.toList());
	}
}
