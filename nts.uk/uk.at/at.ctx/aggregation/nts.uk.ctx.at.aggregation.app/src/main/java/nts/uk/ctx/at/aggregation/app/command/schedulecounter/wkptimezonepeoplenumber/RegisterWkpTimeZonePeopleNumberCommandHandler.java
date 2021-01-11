package nts.uk.ctx.at.aggregation.app.command.schedulecounter.wkptimezonepeoplenumber;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterStartTime;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterTimeZonePeopleNumber;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.timezonepeople.WorkplaceCounterTimeZonePeopleNumberRepo;
import nts.uk.shr.com.context.AppContexts;

/**
 * 時間帯人数情報を登録する
 */
@Stateless
public class RegisterWkpTimeZonePeopleNumberCommandHandler extends CommandHandler<RegisterWkpTimeZonePeopleNumberCommand> {

	@Inject
	private WorkplaceCounterTimeZonePeopleNumberRepo repository;

	@Override
	protected void handle(CommandHandlerContext<RegisterWkpTimeZonePeopleNumberCommand> context) {
		RegisterWkpTimeZonePeopleNumberCommand command = context.getCommand();

		//1 : 取得する(ログイン会社ID) : Optional<時間帯人数>
		Optional<WorkplaceCounterTimeZonePeopleNumber> timeZonePeopleNumber = repository.get(AppContexts.user().companyId());
		WorkplaceCounterTimeZonePeopleNumber newTimeZonePeopleNumber =
			WorkplaceCounterTimeZonePeopleNumber.create(command.getTimeZone().stream().map(WorkplaceCounterStartTime::new).collect(Collectors.toList()));
		if (timeZonePeopleNumber.isPresent()){
			//2 : Optional<時間帯人数>.isPresent==true
			repository.update(AppContexts.user().companyId(), newTimeZonePeopleNumber);
		}else {
			//3 : Optional<時間帯人数>.isPresent==false
			repository.insert(AppContexts.user().companyId(), newTimeZonePeopleNumber);
		}
	}
}
