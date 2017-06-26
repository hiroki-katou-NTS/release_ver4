package nts.uk.ctx.pr.core.app.command.rule.employment.allot;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class DelAllotCompanyCmdHandler extends CommandHandler<DelAllotCompanyCmd>{
	
		
	@Inject
	private CompanyAllotSettingRepository companyRepo;
	
	
	@Override
	protected void handle(CommandHandlerContext<DelAllotCompanyCmd> context) {
		DelAllotCompanyCmd command = context.getCommand();
		
		//delete
		deleteObject(command);
		//update sau delete
		updateObject(command);

	}

	//function Delete
	public void deleteObject(DelAllotCompanyCmd command) {
		this.companyRepo.remove(command.getHistoryId());
	}
		
	//function Update
	public void updateObject(DelAllotCompanyCmd command) {
		Optional<CompanyAllotSetting> allotHistPrevious = companyRepo.getPreviousHistory(
				AppContexts.user().companyCode(),
				command.getStartDate() - 1);
		if(allotHistPrevious != null && allotHistPrevious.isPresent()){
			CompanyAllotSetting comAllot = allotHistPrevious.get();
			comAllot.setEndDate(new YearMonth(999912));
			companyRepo.update(comAllot);
		}
	}
}
