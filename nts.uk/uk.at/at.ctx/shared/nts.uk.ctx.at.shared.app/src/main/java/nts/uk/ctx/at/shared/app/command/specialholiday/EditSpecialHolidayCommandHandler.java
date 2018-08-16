package nts.uk.ctx.at.shared.app.command.specialholiday;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.event.SpecialHolidayChangedInfo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author tanlv
 *
 */
@Transactional
@Stateless
public class EditSpecialHolidayCommandHandler extends CommandHandlerWithResult<SpecialHolidayCommand, List<String>> {
	@Inject
	private SpecialHolidayRepository sphdRepo;
	
	@Override
	protected List<String> handle(CommandHandlerContext<SpecialHolidayCommand> context) {
		SpecialHolidayCommand command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		List<String> errList = new ArrayList<String>();
		
		SpecialHoliday domain = command.toDomain(companyId);
		errList.addAll(domain.getGrantPeriodic().validateInput());
		errList.addAll(domain.getSpecialLeaveRestriction().validateInput());
		
		if (errList.isEmpty()) {
			// call event
			Optional<SpecialHoliday> oldDomain =  sphdRepo.findByCode(companyId, domain.getSpecialHolidayCode().v());
			if(oldDomain.isPresent()){
				boolean isNewChanged = isNameChanged(oldDomain.get(),domain);
				
				boolean isTimeChanged = isTimeChanged(oldDomain.get(),domain); 
				
						if(isNewChanged||isTimeChanged ){
							SpecialHolidayChangedInfo sHC = SpecialHolidayChangedInfo.createFromDomain(true,domain);
							sHC.toBePublished();			
						}
				
			}
			sphdRepo.update(domain);
			
			domain.publishEvent(true);
		}
		
		return errList;
	}

	private boolean isTimeChanged(SpecialHoliday oldDomain, SpecialHoliday newDomain) {
		Integer oldTypeTime = oldDomain.getGrantRegular().getTypeTime().value;
		Integer newTypeTime = newDomain.getGrantRegular().getTypeTime().value;
		return oldTypeTime!=newTypeTime;
	}

	private boolean isNameChanged(SpecialHoliday oldDomain, SpecialHoliday newDomain) {
		String oldName =  oldDomain.getSpecialHolidayName().v();
		String newName =  newDomain.getSpecialHolidayName().v();
		return  !oldName.equals(newName);
	}
}
