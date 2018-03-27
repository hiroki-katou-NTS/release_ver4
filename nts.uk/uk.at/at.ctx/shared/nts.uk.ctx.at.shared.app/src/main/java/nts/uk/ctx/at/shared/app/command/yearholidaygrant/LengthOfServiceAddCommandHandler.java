package nts.uk.ctx.at.shared.app.command.yearholidaygrant;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author TanLV
 *
 */
@Transactional
@Stateless
public class LengthOfServiceAddCommandHandler extends CommandHandler<List<GrantHolidayCommand>> {
	@Inject
	private LengthServiceRepository lengthServiceRepository;
	
	@Inject
	private GrantYearHolidayRepository grantYearHolidayRepo;

	@Override
	protected void handle(CommandHandlerContext<List<GrantHolidayCommand>> context) {
		List<GrantHolidayCommand> command = context.getCommand();
		String companyId = AppContexts.user().companyId();
		
		// check exists code
		List<LengthServiceTbl> lengthServiceTbl = lengthServiceRepository.findByCode(companyId, command.get(0).getYearHolidayCode());
		if (lengthServiceTbl.size() > 0) {
			lengthServiceRepository.remove(companyId, command.get(0).getYearHolidayCode());
			grantYearHolidayRepo.remove(companyId, command.get(0).getYearHolidayCode());
		}
		
		List<LengthServiceTbl> lengthServiceData = new ArrayList<>();
		for (GrantHolidayCommand data : command) {
			LengthServiceTbl lengthService = LengthServiceTbl.createFromJavaType(companyId, data.getYearHolidayCode(), data.getGrantNum(), 
					data.getAllowStatus(), data.getStandGrantDay(), data.getYear(), data.getMonth());
			
			if(lengthService.getMonth() != null || lengthService.getYear() != null) {
				lengthServiceData.add(lengthService);
			}
		}
		
		LengthServiceTbl.validateInput(lengthServiceData);
		
		// Add to Length Service
		for (LengthServiceTbl data : lengthServiceData) {
			lengthServiceRepository.add(data);
		}
		
		List<GrantHdTbl> grantDomain = new ArrayList<>();
		for (GrantHolidayCommand data : command) {
			GrantHdTbl newData = GrantHdTbl.createFromJavaType(companyId, data.getConditionNo(), data.getYearHolidayCode(), 
					data.getGrantNum(), data.getGrantDays(), data.getLimitTimeHd() != null ? data.getLimitTimeHd() : 0, 
					data.getLimitDayYear() != null ? data.getLimitDayYear() : 0);
			
			if(newData.getGrantDays().v() != null && newData.getLimitTimeHd().isPresent() && newData.getLimitDayYear().isPresent()) {
				grantDomain.add(newData);
			}
		}
		
		// Add to GrantHd
		for (GrantHdTbl data : grantDomain) {
			grantYearHolidayRepo.add(data);
		}
	}
}
