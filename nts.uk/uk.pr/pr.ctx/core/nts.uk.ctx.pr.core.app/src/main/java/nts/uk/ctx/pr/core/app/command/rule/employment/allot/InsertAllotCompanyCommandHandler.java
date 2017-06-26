package nts.uk.ctx.pr.core.app.command.rule.employment.allot;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSettingRepository;
import nts.uk.shr.com.context.AppContexts;


@Stateless
@Transactional
public class InsertAllotCompanyCommandHandler extends CommandHandler<InsertAllotCompanyCommand>{
	@Inject
	private CompanyAllotSettingRepository companyRepo;
	
	@Override
	protected void handle(CommandHandlerContext<InsertAllotCompanyCommand> context) {
		InsertAllotCompanyCommand command = context.getCommand();
		//Find hist Item with max date
		Optional<CompanyAllotSetting> maxDate = companyRepo.findMax(AppContexts.user().companyCode(),999912);
		//Get enddate of the previous item
		YearMonth endYM = new YearMonth(command.getStartDate()).previousMonth();
		// if Exist set Endate to previous item 
		if (maxDate.isPresent()) {
			maxDate.get().setEndDate(endYM);
		}
		//update procee
		companyRepo.update(maxDate.get());
		//Comand insert new item hist
		CompanyAllotSetting objInsert = command.toDomain(IdentifierUtil.randomUniqueId());
		//Set endate : 999912
		objInsert.setEndDate(new YearMonth(999912));
		//Insert process
		companyRepo.insert(objInsert);
	}
}
