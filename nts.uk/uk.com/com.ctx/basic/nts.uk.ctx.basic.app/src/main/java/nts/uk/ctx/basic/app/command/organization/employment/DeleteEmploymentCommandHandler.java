package nts.uk.ctx.basic.app.command.organization.employment;

import java.util.List;
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
public class DeleteEmploymentCommandHandler extends CommandHandler<DeleteEmploymentCommand>{
	@Inject
	private EmploymentRepository repository;
	
	@Override
	protected void handle(CommandHandlerContext<DeleteEmploymentCommand> context) {
		DeleteEmploymentCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		Optional<Employment> emDelete = repository.findEmployment(companyCode, command.getEmploymentCode());
		if(!emDelete.isPresent()){
			throw new BusinessException("ER010");
		}
		
		//初期表示するのはコードの昇順で先頭になる項目です。
		if (emDelete.get().getDisplayFlg() == ManageOrNot.MANAGE) {
			List<Employment> lstEmployment = repository.findAllEmployment(companyCode);
			if(lstEmployment != null){
				for (Employment employment : lstEmployment) {
					//can check trong truong hop xoa employment dau tien va dong thoi cung duoc chon hien thi
					if(employment.getEmploymentCode().v().equals(command.getEmploymentCode())) {
						continue;
					} else {
						Employment employmentInf = employment;
						employmentInf.setDisplayFlg(ManageOrNot.MANAGE);
						this.repository.update(employmentInf);
						break;
					}
				}
				
			}
		}
		
		this.repository.remove(companyCode, command.getEmploymentCode());			
	}

}
