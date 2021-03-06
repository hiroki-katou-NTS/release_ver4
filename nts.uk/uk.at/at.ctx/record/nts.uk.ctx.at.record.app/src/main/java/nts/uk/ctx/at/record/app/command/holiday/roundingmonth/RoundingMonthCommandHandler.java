/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.holiday.roundingmonth;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roundingset.ItemRoundingSetOfMonthly;
//import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roundingset.RoundingMonth;
//import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roundingset.RoundingMonthRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roundingset.RoundingSetOfMonthlyRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class RoundingMonthCommandHandler.
 */
@Stateless
public class RoundingMonthCommandHandler extends CommandHandler<List<RoundingMonthCommand>>{

	/** The rounding month repository. */
//	@Inject
//	private RoundingMonthRepository roundingMonthRepository;

	@Inject
	private RoundingSetOfMonthlyRepository repo;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<List<RoundingMonthCommand>> context) {
		String companyId = AppContexts.user().companyId();
		repo.persistAndUpdateMonItemRound(
				context.getCommand().stream().map(i -> ItemRoundingSetOfMonthly.of(
						companyId,
						i.getTimeItemId(),
						new TimeRoundingSetting(i.getUnit(), i.getRounding())
				)).collect(Collectors.toList()),
				companyId
		);
//		roundingMonthRepository.addList(
//				context.getCommand().stream().map(dto -> {
//					return RoundingMonth.createFromJavaType(companyId, dto.getTimeItemId(), dto.getUnit(), dto.getRounding());
//				}).collect(Collectors.toList())
//		);
	}
}
