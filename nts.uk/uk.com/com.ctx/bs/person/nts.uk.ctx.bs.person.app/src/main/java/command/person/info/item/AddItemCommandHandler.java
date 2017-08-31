package command.person.info.item;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import command.person.info.category.GetListCompanyOfContract;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.bs.person.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;

@Stateless
public class AddItemCommandHandler extends CommandHandlerWithResult<AddItemCommand, String> {

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRep;

	private final static String SPECIAL_ITEM_CODE = "IO";
	private final static int ITEM_CODE_DEFAUT_NUMBER = 0;

	@Override
	protected String handle(CommandHandlerContext<AddItemCommand> context) {
		String perInfoItemId = null;
		AddItemCommand addItemCommand = context.getCommand();
		String contractCd = PersonInfoItemDefinition.ROOT_CONTRACT_CODE;
		PersonInfoCategory perInfoCtg = this.perInfoCtgRep
				.getPerInfoCategory(addItemCommand.getPerInfoCtgId(), contractCd).orElse(null);
		if (perInfoCtg == null) {
			return null;
		}
		String categoryCd = perInfoCtg.getCategoryCode().v();
		String itemCodeLastes = this.pernfoItemDefRep.getPerInfoItemCodeLastest(contractCd, categoryCd);
		String newItemCode = createNewCode(itemCodeLastes, SPECIAL_ITEM_CODE);
		AddItemCommand newItemCommand = new AddItemCommand(context.getCommand().getPerInfoCtgId(), newItemCode, null,
				context.getCommand().getItemName(), context.getCommand().getSingleItem());
		PersonInfoItemDefinition perInfoItemDef = MappingDtoToDomain.mappingFromDomaintoCommand(newItemCommand);
		perInfoItemId = this.pernfoItemDefRep.addPerInfoItemDefRoot(perInfoItemDef, contractCd, categoryCd);
		// get List PerInfoCtgId.
		List<String> companyIdList = GetListCompanyOfContract.LIST_COMPANY_OF_CONTRACT;
		List<String> ctgIdList = this.perInfoCtgRep.getPerInfoCtgIdList(companyIdList, categoryCd);
		if (ctgIdList == null || ctgIdList.isEmpty()) {
			return null;
		}
		this.pernfoItemDefRep.addPerInfoItemDefByCtgIdList(perInfoItemDef, ctgIdList);
		return perInfoItemId;
	}

	private String createNewCode(String codeLastest, String strSpecial) {
		String numberCode = String.valueOf(ITEM_CODE_DEFAUT_NUMBER + 1);
		if (codeLastest != null) {
			numberCode = String.valueOf(Integer.parseInt(codeLastest.substring(2, 7)) + 1);
		}
		for (int i = 5; i > 0; i--) {
			if (i == numberCode.length()) {
				break;
			}
			strSpecial += "0";
		}
		return strSpecial + numberCode;
	}

}
