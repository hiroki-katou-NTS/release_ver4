package nts.uk.ctx.pr.shared.app.command.socialinsurance.employeesociainsur.emphealinsurbeneinfo;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.*;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

@Stateless
public class AddEmpHealInsQualifiInfoCommandHandler
        extends CommandHandlerWithResult<AddEmpHealInsQualifiInfoCommand, PeregAddCommandResult>
        implements PeregAddCommandHandler<AddEmpHealInsQualifiInfoCommand> {
    @Inject
    private EmplHealInsurQualifiInforRepository emplHealInsurQualifiInforRepository;

    @Inject
    private HealInsurNumberInforRepository healInsurNumberInforRepository;

    @Override
    public String targetCategoryCd() {
        return "CS00082";
    }

    @Override
    public Class<?> commandClass() {
        return AddEmpHealInsQualifiInfoCommand.class;
    }

    @Override
    protected PeregAddCommandResult handle(CommandHandlerContext<AddEmpHealInsQualifiInfoCommand> context) {
        val command = context.getCommand();
        String hisId = IdentifierUtil.randomUniqueId();
        EmplHealInsurQualifiInfor qualifiInfor = new EmplHealInsurQualifiInfor(command.getEmployeeId(), new ArrayList<>());
        emplHealInsurQualifiInforRepository.add(qualifiInfor);
        HealInsurNumberInfor numberInfor = HealInsurNumberInfor.createFromJavaType(hisId, command.getHealInsNumber(), command.getNurCaseInsNumber());
        healInsurNumberInforRepository.add(numberInfor);
        return new PeregAddCommandResult(hisId);
    }
}
