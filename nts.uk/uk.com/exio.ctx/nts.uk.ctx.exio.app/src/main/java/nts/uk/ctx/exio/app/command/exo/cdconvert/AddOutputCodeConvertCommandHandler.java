package nts.uk.ctx.exio.app.command.exo.cdconvert;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.exio.dom.exo.cdconvert.CdConvertDetail;
import nts.uk.ctx.exio.dom.exo.cdconvert.OutputCodeConvert;
import nts.uk.ctx.exio.dom.exo.cdconvert.OutputCodeConvertRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class AddOutputCodeConvertCommandHandler extends CommandHandler<OutputCodeConvertCommand> {

	@Inject
	private OutputCodeConvertRepository repository;

	@Override
	protected void handle(CommandHandlerContext<OutputCodeConvertCommand> context) {
		OutputCodeConvertCommand addCommand = context.getCommand();
		String companyId = AppContexts.user().companyId();
		repository.add(new OutputCodeConvert(addCommand.getConvertCode(), addCommand.getConvertName(), companyId,
				addCommand.getAcceptWithoutSetting(),
				addCommand.getListCdConvertDetailCommand().stream().map(itemDetail -> {
					return new CdConvertDetail(companyId, itemDetail.getConvertCd(), itemDetail.getOutputItem(),
							itemDetail.getSystemCd(), itemDetail.getLineNumber());
				}).collect(Collectors.toList())));
	}
}
