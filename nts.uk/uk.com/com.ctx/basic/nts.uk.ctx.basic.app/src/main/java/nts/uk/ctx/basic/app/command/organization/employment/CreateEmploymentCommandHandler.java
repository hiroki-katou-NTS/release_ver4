package nts.uk.ctx.basic.app.command.organization.employment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.employment.Employment;
import nts.uk.ctx.basic.dom.organization.employment.EmploymentRepository;
import nts.uk.ctx.basic.dom.organization.employment.ManageOrNot;
import nts.uk.shr.com.context.AppContexts;
@Stateless
@Transactional
public class CreateEmploymentCommandHandler extends CommandHandler<CreateEmploymentCommand>{
	@Inject
	private EmploymentRepository repository;
	@Override
	protected void handle(CommandHandlerContext<CreateEmploymentCommand> context) {
		CreateEmploymentCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		Employment employ = command.toDomain();
		this.repository.findEmployment(companyCode, command.getEmploymentCode()).ifPresent(x -> {throw new BusinessException("ER005");});
		
		Optional<Employment> employmentByDisplayFlg = repository.findEmploymnetByDisplayFlg(companyCode);
		
		//employ.validate();
		this.repository.add(employ);
		//A_SEL_001にチェックが付いている場合
		//[雇用マスタ.UPD-2]を実施する
		if(command.getDisplayFlg() == 1 && employmentByDisplayFlg.isPresent()){
			Employment employmentDisplay = employmentByDisplayFlg.get();
			employmentDisplay.setDisplayFlg(ManageOrNot.NOT_MANAGE);
			repository.update(employmentDisplay);
		}
	}
}
