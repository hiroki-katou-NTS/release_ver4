package command.person.info.category;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import command.person.info.item.AddItemCommand;
import command.person.info.item.SingleItemCommand;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.info.item.PernfoItemDefRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AddPerInfoCtgCommandHandler extends CommandHandler<AddPerInfoCtgCommand> {

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRep;

	@Inject
	private PernfoItemDefRepositoty pernfoItemDefRep;

	private final static String SPECIAL_CTG_CODE = "CO";
	private final static String SPECIAL_ITEM_CODE = "IO";

	@Override
	protected void handle(CommandHandlerContext<AddPerInfoCtgCommand> context) {
		AddPerInfoCtgCommand perInfoCtgCommand = context.getCommand();
		String contractCd = AppContexts.user().contractCode();
		String newCtgCode = createNewCode(perInfoCtgRep.getPerInfoCtgCodeLastest(contractCd), SPECIAL_CTG_CODE);

		List<String> companyIdList = GetListCompanyOfContract.LIST_COMPANY_OF_CONTRACT;
		PersonInfoCategory perInfoCtg = PersonInfoCategory.createFromJavaType(PersonInfoCategory.ROOT_COMPANY_ID,
				newCtgCode, perInfoCtgCommand.getCategoryName().v(), perInfoCtgCommand.getCategoryType().value);

		this.perInfoCtgRep.addPerInfoCtgRoot(perInfoCtg, contractCd);
		this.perInfoCtgRep.addPerInfoCtgWithListCompany(perInfoCtg, contractCd, companyIdList);

		List<String> ctgIdList = perInfoCtgRep.getPerInfoCtgIdList(PersonInfoCategory.ROOT_COMPANY_ID, newCtgCode);
		String newItemCode = createNewCode(
				pernfoItemDefRep.getPerInfoItemCodeLastest(PersonInfoItemDefinition.ROOT_CONTRACT_CODE, newCtgCode),
				SPECIAL_ITEM_CODE);
		SingleItemCommand singleItemCommand = null;
		AddItemCommand addItemCommand = new AddItemCommand(perInfoCtg.getPersonInfoCategoryId(), newItemCode,
				newItemCode, null, 0, singleItemCommand);
	}

	private String createNewCode(String codeLastest, String strSpecial) {
		String numberCode = String.valueOf(Integer.parseInt(codeLastest.substring(2, 7)) + 1);
		for (int i = 5; i > 0; i++) {
			if (i == numberCode.length()) {
				break;
			}
			strSpecial += "0";
		}
		return strSpecial + numberCode;

	}

}
