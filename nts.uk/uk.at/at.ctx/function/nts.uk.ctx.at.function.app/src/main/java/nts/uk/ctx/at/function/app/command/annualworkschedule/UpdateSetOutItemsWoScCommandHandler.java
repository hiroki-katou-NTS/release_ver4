package nts.uk.ctx.at.function.app.command.annualworkschedule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.function.dom.annualworkschedule.CalcFormulaItem;
import nts.uk.ctx.at.function.dom.annualworkschedule.ItemOutTblBook;
import nts.uk.ctx.at.function.dom.annualworkschedule.SetOutItemsWoSc;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.OutputAgreementTime;
import nts.uk.ctx.at.function.dom.annualworkschedule.primitivevalue.OutItemsWoScCode;
import nts.uk.ctx.at.function.dom.annualworkschedule.primitivevalue.OutItemsWoScName;
import nts.uk.ctx.at.function.dom.annualworkschedule.repository.SetOutItemsWoScRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateSetOutItemsWoScCommandHandler extends CommandHandler<SetOutItemsWoScCommand> {
	@Inject
	private SetOutItemsWoScRepository repository;

	@Override
	protected void handle(CommandHandlerContext<SetOutItemsWoScCommand> context) {
		String companyId = AppContexts.user().companyId();
		SetOutItemsWoScCommand updateCommand = context.getCommand();

		Optional<ItemOutTblBookCommand> lastItemOutTblBookCommand
			= updateCommand.getListItemOutput().stream().filter(m -> !StringUtil.isNullOrEmpty(m.getCd(), true))
			.max((m1, m2) -> Integer.compare(Integer.valueOf(m1.getCd()), Integer.valueOf(m2.getCd())));

		int[] itemOutCd = {0};
		if (lastItemOutTblBookCommand.isPresent()) {
			itemOutCd[0] = Integer.valueOf(lastItemOutTblBookCommand.get().getCd());
		}

		List<ItemOutTblBook> listItemOutTblBook = updateCommand.getListItemOutput().stream()
				.map(m -> {
						String itemOutCdStr = StringUtil.padLeft(StringUtil.isNullOrEmpty(m.getCd(), true)?
																String.valueOf(++itemOutCd[0]) : m.getCd(), 2, '0');
						return ItemOutTblBook.createFromJavaType(companyId,
						updateCommand.getCd(),
						itemOutCdStr,
						m.getSortBy(),
						m.getHeadingName(), m.isUseClass(), m.getValOutFormat(),
						//list CalcFormulaItem
						m.getListOperationSetting().stream()
						.map(os -> CalcFormulaItem.createFromJavaType(companyId,
								updateCommand.getCd(),
								itemOutCdStr,
								os.getAttendanceItemId(),
								os.getOperation())).collect(Collectors.toList()));
				}).collect(Collectors.toList());

		repository.update(new SetOutItemsWoSc(companyId, new OutItemsWoScCode(updateCommand.getCd()),
						new OutItemsWoScName(updateCommand.getName()),
						updateCommand.isOutNumExceedTime36Agr(),
						EnumAdaptor.valueOf(updateCommand.getDisplayFormat(), OutputAgreementTime.class),
						listItemOutTblBook));
	}
}
