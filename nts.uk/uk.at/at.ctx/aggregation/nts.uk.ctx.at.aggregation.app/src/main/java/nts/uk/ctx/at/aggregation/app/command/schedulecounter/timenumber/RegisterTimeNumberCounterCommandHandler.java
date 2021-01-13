package nts.uk.ctx.at.aggregation.app.command.schedulecounter.timenumber;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timescounting.TimesNumberCounterSelection;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timescounting.TimesNumberCounterSelectionRepo;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timescounting.TimesNumberCounterType;
import nts.uk.shr.com.context.AppContexts;

/**
 * 回数集計情報を登録する
 */
@Stateless
public class RegisterTimeNumberCounterCommandHandler extends CommandHandler<RegisterTimeNumberCounterCommand> {
	@Inject
	private TimesNumberCounterSelectionRepo repository;

	@Override
	protected void handle(CommandHandlerContext<RegisterTimeNumberCounterCommand> context) {
		RegisterTimeNumberCounterCommand command = context.getCommand();

		//1: 取得する(ログイン会社ID、回数集計種類) : Optional<回数集計>
		Optional<TimesNumberCounterSelection> timesNumber =
			repository.get(AppContexts.user().companyId(), EnumAdaptor.valueOf(command.getType(),TimesNumberCounterType.class));
		TimesNumberCounterSelection newTimesNumber =
			TimesNumberCounterSelection.create(EnumAdaptor.valueOf(command.getType(),TimesNumberCounterType.class),command.getSelectedNoList());

		if (timesNumber.isPresent()){
			//2 : Optional<回数集計>.isPresent==true
			repository.update(AppContexts.user().companyId(), newTimesNumber);
		}else {
			//3 : Optional<回数集計>.isPresent==false
			repository.insert(AppContexts.user().companyId(), newTimesNumber);
		}
	}
}
