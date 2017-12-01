package nts.uk.ctx.bs.employee.app.command.employee.history;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfo;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfoRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregUpdateCommandHandler;

@Stateless
public class UpdateAffCompanyHistoryCommandHandler extends CommandHandler<UpdateAffCompanyHistoryCommand>
	implements PeregUpdateCommandHandler<UpdateAffCompanyHistoryCommand>{
	@Inject
	private AffCompanyHistRepository affCompanyHistRepository;
	
	@Inject
	private AffCompanyInfoRepository affCompanyInfoRepository;
	@Override
	public String targetCategoryCd() {
		return "CS00003";
	}

	@Override
	public Class<?> commandClass() {
		return UpdateAffCompanyHistoryCommand.class;
	}

	@Override
	protected void handle(CommandHandlerContext<UpdateAffCompanyHistoryCommand> context) {
		val command = context.getCommand();
		
		AffCompanyHist listHist = affCompanyHistRepository.getAffCompanyHistoryOfEmployee(command.getSId());
		if (listHist == null){
			throw new RuntimeException("Invalid AffCompanyHist");
		}
		 AffCompanyHistByEmployee listHistBySID = listHist.getAffCompanyHistByEmployee(command.getSId());
		
		Optional<AffCompanyHistItem> itemToBeUpdated = listHistBySID.getLstAffCompanyHistoryItem().stream()
				.filter(h->h.identifier().equals(command.getHistoryId())).findFirst();
		
		if (!itemToBeUpdated.isPresent()){
			throw new RuntimeException("Invalid AffCompanyHist");
		}
		listHistBySID.changeSpan(itemToBeUpdated.get(),new DatePeriod(command.getStartDate(),command.getEndDate()) );
		affCompanyHistRepository.update(listHistBySID, itemToBeUpdated.get());
		
		AffCompanyInfo histItem = AffCompanyInfo.createFromJavaType(command.getHistoryId(), command.getRecruitmentClassification(), command.getAdoptionDate(), command.getRetirementAllowanceCalcStartDate());
		affCompanyInfoRepository.update(histItem);
		
	}

}
