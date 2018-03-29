/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.workrecord.monthcal.workplace;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpDeforLaborMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpDeforLaborMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpFlexMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpFlexMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpRegulaMonthActCalSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.workplace.WkpRegulaMonthActCalSetRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DelWkpMonthCalSetCommandHandler.
 */
@Stateless
public class DelWkpMonthCalSetCommandHandler extends CommandHandler<DelWkpMonthCalSetCommand> {

	/** The defor labor month act cal set repo. */
	@Inject
	private WkpDeforLaborMonthActCalSetRepository deforLaborMonthActCalSetRepo;

	/** The flex month act cal set repo. */
	@Inject
	private WkpFlexMonthActCalSetRepository flexMonthActCalSetRepo;

	/** The regula month act cal set repo. */
	@Inject
	private WkpRegulaMonthActCalSetRepository regulaMonthActCalSetRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<DelWkpMonthCalSetCommand> context) {

		// get company id
		String cid = AppContexts.user().companyId();

		// get work place id
		DelWkpMonthCalSetCommand command = context.getCommand();
		String wkpId = command.getWkpId();

		Optional<WkpDeforLaborMonthActCalSet> optWkpDeforLaborMonthActCalSet = deforLaborMonthActCalSetRepo.find(cid,
				wkpId);

		if (optWkpDeforLaborMonthActCalSet.isPresent()) {
			deforLaborMonthActCalSetRepo.remove(cid, wkpId);
		}

		Optional<WkpFlexMonthActCalSet> optWkpFlexMonthActCalSet = flexMonthActCalSetRepo.find(cid, wkpId);

		if (optWkpFlexMonthActCalSet.isPresent()) {
			flexMonthActCalSetRepo.remove(cid, wkpId);
		}

		Optional<WkpRegulaMonthActCalSet> optWkpRegulaMonthActCalSet = regulaMonthActCalSetRepo.find(cid, wkpId);

		if (optWkpRegulaMonthActCalSet.isPresent()) {
			regulaMonthActCalSetRepo.remove(cid, wkpId);
		}

	}

}
