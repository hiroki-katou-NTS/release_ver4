package nts.uk.ctx.at.request.app.command.setting.company.vacationapplicationsetting;

/*import java.util.Optional;*/

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateHdAppSetCommandHandler extends CommandHandler<HdAppSetCommand>{
	@Inject
	private HdAppSetRepository hdRep;

	@Override
	protected void handle(CommandHandlerContext<HdAppSetCommand> context) {
		String companyId = AppContexts.user().companyId();
		HdAppSetCommand data = context.getCommand();
//		Optional<HdAppSet> hdApp = hdRep.getAll();
		HdAppSet hdAppSet = HdAppSet.createFromJavaType(companyId, 
				data.use60h, data.obstacleName, data.regisShortLostHd, data.hdName, data.regisLackPubHd, 
				data.changeWrkHour, data.ckuperLimit, data.actualDisp, data.wrkHours, data.pridigCheck, 
				data.yearHdName, data.regisNumYear, data.furikyuName, data.regisInsuff, data.useGener, 
				data.useYear, data.timeDigest, data.absenteeism, data.concheckOutLegal, data.specialVaca,
				data.concheckDateRelease, data.appDateContra, data.yearResig, data.regisShortReser, 0, data.displayUnselect, data.dayDispSet);
//		if(hdApp.isPresent()){
			hdRep.update(hdAppSet);
//			return;
//		}
//		hdRep.insert(hdAppSet);
	}
	
}
