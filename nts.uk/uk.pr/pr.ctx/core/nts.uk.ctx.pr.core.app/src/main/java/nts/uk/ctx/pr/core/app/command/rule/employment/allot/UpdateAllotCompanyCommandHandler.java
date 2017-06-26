package nts.uk.ctx.pr.core.app.command.rule.employment.allot;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.UpdateAllotCompanyCommand;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSettingRepository;

@Stateless
@Transactional
public class UpdateAllotCompanyCommandHandler extends CommandHandler<UpdateAllotCompanyCommand>{
	@Inject
	private CompanyAllotSettingRepository companyRepo;
	
	@Override
	protected void handle(CommandHandlerContext<UpdateAllotCompanyCommand> context) {
		companyRepo.update(context.getCommand().toDomain()); 
	}
}