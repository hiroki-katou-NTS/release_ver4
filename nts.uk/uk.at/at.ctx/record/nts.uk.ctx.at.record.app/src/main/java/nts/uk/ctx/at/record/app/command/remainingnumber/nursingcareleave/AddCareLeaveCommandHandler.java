package nts.uk.ctx.at.record.app.command.remainingnumber.nursingcareleave;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.data.NursCareLevRemainDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.data.NursingCareLeaveRemainingData;
import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.info.NursCareLevRemainInfoRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.nursingcareleavemanagement.info.NursingCareLeaveRemainingInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
@Transactional
public class AddCareLeaveCommandHandler extends CommandHandlerWithResult<AddCareLeaveCommand, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddCareLeaveCommand>{
	
	@Inject
	private NursCareLevRemainDataRepository dataRepo;
	
	@Inject
	private NursCareLevRemainInfoRepository infoRepo;

	@Override
	public String targetCategoryCd() {
		return "CS00036";
	}

	@Override
	public Class<?> commandClass() {
		return AddCareLeaveCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddCareLeaveCommand> context) {
		String cId = AppContexts.user().companyId();
		AddCareLeaveCommand data = context.getCommand();
		NursingCareLeaveRemainingData childCareData = NursingCareLeaveRemainingData.getChildCareHDRemaining(data.getSId(), data.getChildCareUsedDays());
		NursingCareLeaveRemainingData careData = NursingCareLeaveRemainingData.getCareHDRemaining(data.getSId(), data.getCareUsedDays());
		dataRepo.add(childCareData, cId);
		dataRepo.add(careData, cId);
		
		NursingCareLeaveRemainingInfo childCareInfo = NursingCareLeaveRemainingInfo.createChildCareLeaveInfo(data.getSId(), data.isChildCareUseArt()? 1: 0, 
				data.getChildCareUpLimSet(), 
				data.getChildCareThisFiscal() == null ? Optional.empty() : Optional.of(data.getChildCareThisFiscal()), 
				data.getChildCareNextFiscal() == null ? Optional.empty() : Optional.of(data.getChildCareNextFiscal()));
		NursingCareLeaveRemainingInfo careInfo= NursingCareLeaveRemainingInfo.createCareLeaveInfo(data.getSId(), data.isCareUseArt()? 1: 0, 
				data.getCareUpLimSet(), 
				data.getCareThisFiscal() == null ? Optional.empty() : Optional.of(data.getCareThisFiscal()), 
				data.getCareNextFiscal() == null ? Optional.empty() : Optional.of(data.getCareNextFiscal()));
		infoRepo.add(childCareInfo, cId);
		infoRepo.add(careInfo, cId);
		return new PeregAddCommandResult(data.getSId());
	}

}
