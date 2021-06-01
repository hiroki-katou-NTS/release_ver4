package nts.uk.ctx.exio.app.command.exi.codeconvert;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.exio.dom.input.revise.type.codeconvert.CodeConvertDetails;
import nts.uk.ctx.exio.dom.input.revise.type.codeconvert.ExternalImportCodeConvert;
import nts.uk.ctx.exio.dom.input.revise.type.codeconvert.ExternalImportCodeConvertRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateAcceptCdConvertCommandHandler extends CommandHandler<AcceptCdConvertCommand> {

	@Inject
	private ExternalImportCodeConvertRepository repository;

	@Override
	protected void handle(CommandHandlerContext<AcceptCdConvertCommand> context) {
		AcceptCdConvertCommand updateCommand = context.getCommand();
		String companyId = AppContexts.user().companyId();
		repository.update(new ExternalImportCodeConvert(companyId, updateCommand.getConvertCd(), updateCommand.getConvertName(),
				updateCommand.getAcceptWithoutSetting(),
				updateCommand.getCdConvertDetails().stream().map(itemDetail -> {
					return new CodeConvertDetails(companyId, itemDetail.getConvertCd(), itemDetail.getLineNumber(),
							itemDetail.getOutputItem(), itemDetail.getSystemCd());
				}).collect(Collectors.toList())));

	}
}
