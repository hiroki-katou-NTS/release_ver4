package nts.uk.cnv.app.command;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.cnv.dom.conversiontable.ConversionCategoryTable;
import nts.uk.cnv.dom.conversiontable.ConversionCategoryTableRepository;

@Stateless
public class RegistConversionCategoryCommandHandler  extends CommandHandler<RegistConversionCategoryCommand>{

	@Inject
	ConversionCategoryTableRepository repository;

	@Override
	protected void handle(CommandHandlerContext<RegistConversionCategoryCommand> context) {
		RegistConversionCategoryCommand command = context.getCommand();

		repository.delete(command.category);

		int seq = 0;
		for(String table : command.tables) {
			repository.regist(new ConversionCategoryTable(command.category, table, seq));
			seq++;
		}
	}

}
