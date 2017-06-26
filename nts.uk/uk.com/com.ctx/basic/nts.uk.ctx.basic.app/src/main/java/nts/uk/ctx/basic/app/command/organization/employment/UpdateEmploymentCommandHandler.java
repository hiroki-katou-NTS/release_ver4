package nts.uk.ctx.basic.app.command.organization.employment;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.employment.CloseDateNo;
import nts.uk.ctx.basic.dom.organization.employment.Employment;
import nts.uk.ctx.basic.dom.organization.employment.EmploymentCode;
import nts.uk.ctx.basic.dom.organization.employment.EmploymentName;
import nts.uk.ctx.basic.dom.organization.employment.EmploymentRepository;
import nts.uk.ctx.basic.dom.organization.employment.ManageOrNot;
import nts.uk.ctx.basic.dom.organization.employment.ProcessingNo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

@Stateless
@Transactional
public class UpdateEmploymentCommandHandler extends CommandHandler<UpdateEmploymentCommand>{
	@Inject
	private EmploymentRepository repository;
	@Override
	protected void handle(CommandHandlerContext<UpdateEmploymentCommand> context) {
		try{
			UpdateEmploymentCommand command = context.getCommand();
			String companyCode = AppContexts.user().companyCode();	
			Optional<Employment> employmentInf = repository.findEmployment(companyCode, command.getEmploymentCode());
			if(!employmentInf.isPresent()){
				throw new BusinessException("ER026");
			}
			//A_SEL_001にチェックが付いている場合
			if(command.getDisplayFlg() == 1){
				Optional<Employment> employmentByDisplayFlg = repository.findEmploymnetByDisplayFlg(companyCode);
				if(employmentByDisplayFlg.isPresent()){
					//[雇用マスタ.UPD-2]を実施する
					Employment employmentDisplay = employmentByDisplayFlg.get();
					employmentDisplay.setDisplayFlg(ManageOrNot.NOT_MANAGE);
					repository.update(employmentDisplay);
				}
			}
			Employment employment = employmentInf.get();
			employment.setEmploymentName(new EmploymentName(command.getEmploymentName()));
			employment.setCloseDateNo(new CloseDateNo(command.getCloseDateNo()));
			employment.setMemo(new Memo(command.getMemo()));
			employment.setProcessingNo(new ProcessingNo(command.getProcessingNo()));
			employment.setStatutoryHolidayAtr(EnumAdaptor.valueOf( command.getStatutoryHolidayAtr(), ManageOrNot.class));
			employment.setEmployementOutCd(new EmploymentCode(command.getEmployementOutCd()));
			employment.setDisplayFlg(EnumAdaptor.valueOf( command.getDisplayFlg(), ManageOrNot.class));
			this.repository.update(employment);
			
		}
		catch(Exception ex){
			throw ex;
		}
	}

}
