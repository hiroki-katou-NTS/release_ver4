package nts.uk.ctx.at.schedule.app.command.budget.premium;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculation;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculationDomainService;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculationRepository;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumName;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumRate;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumSetting;
import nts.uk.ctx.at.schedule.dom.budget.premium.UnitPrice;
import nts.uk.ctx.at.schedule.dom.budget.premium.UseAttribute;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@Stateless
@Transactional
public class UpdatePersonCostCalculationSettingCommandHandler extends CommandHandler<UpdatePersonCostCalculationSettingCommand>{

	@Inject
	private PersonCostCalculationDomainService personCostCalculationDomainService;
	
	@Override
	protected void handle(CommandHandlerContext<UpdatePersonCostCalculationSettingCommand> context) {
		String companyID = AppContexts.user().companyId();
		UpdatePersonCostCalculationSettingCommand command = context.getCommand();
		this.personCostCalculationDomainService.updatePersonCostCalculation(
				new PersonCostCalculation(
						companyID, 
						command.getHistoryID(), 
						GeneralDate.fromString(command.getStartDate(), "yyyy/MM/dd"), 
						GeneralDate.fromString(command.getEndDate(), "yyyy/MM/dd"),
						EnumAdaptor.valueOf(command.getUnitPrice(), UnitPrice.class),
						new Memo(command.getMemo()), 
						command.getPremiumSets().stream()
							.map(x -> new PremiumSetting(
									x.getCompanyID(), 
									x.getHistoryID(), 
									x.getPremiumID(), 
									new PremiumRate(x.getRate()), 
									x.getAttendanceID(),
									new PremiumName(x.getName()), 
									x.getDisplayNumber(), 
									EnumAdaptor.valueOf(x.getUseAtr(), UseAttribute.class), 
									x.getAttendanceItems()))
							.collect(Collectors.toList())
				)
		);
	}

}
