package nts.uk.ctx.pr.shared.app.command.socialinsurance.employeesociainsur.emphealinsurbeneinfo;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.EmplHealInsurQualifiInforRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class UpdateEmplHealInsurQualifiInforCommandHandler extends CommandHandler<EmplHealInsurQualifiInforCommand>
{
    
    @Inject
    private EmplHealInsurQualifiInforRepository repository;
    
    @Override
    protected void handle(CommandHandlerContext<EmplHealInsurQualifiInforCommand> context) {

    }
}
