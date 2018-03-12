package nts.uk.ctx.pereg.app.command.person.info.item;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.app.command.person.info.category.CheckNameSpace;
import nts.uk.ctx.pereg.app.command.person.info.category.GetListCompanyOfContract;
import nts.uk.ctx.pereg.dom.person.additemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.info.category.IsFixed;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.ctx.pereg.dom.roles.auth.item.PersonInfoItemAuthRepository;

@Stateless
public class UpdateItemCommandHandler extends CommandHandlerWithResult<UpdateItemCommand, String> {

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	@Inject
	private SelectionRepository selectionRepo;
	
	@Inject
	private EmpInfoItemDataRepository empInfoRepo;

	@Inject
	private PerInfoItemDataRepository perItemRepo;

	@Inject
	private PersonInfoItemAuthRepository itemAuthRepo;

	@Inject
	private PerInfoInitValueSetItemRepository itemInitRepo;
	
	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRep;

	@Override
	protected String handle(CommandHandlerContext<UpdateItemCommand> context) {
		
		UpdateItemCommand itemCommand = context.getCommand();
		List<String> companyIdList = GetListCompanyOfContract.LIST_COMPANY_OF_CONTRACT;
		// String mess = "Msg_233";
		String contractCd = PersonInfoItemDefinition.ROOT_CONTRACT_CODE;
		if (CheckNameSpace.checkName(itemCommand.getItemName())){
			throw new BusinessException("Msg_928");
		}
		if (itemCommand.getSingleItem().getDataType() == 6) {

			List<Selection> selection = new ArrayList<>();
			if (itemCommand.getPersonEmployeeType() == 1) {
				selection = this.selectionRepo.getAllSelectionByHistoryId(
						itemCommand.getSingleItem().getSelectionItemId(), GeneralDate.today(), 0);
			} else if (itemCommand.getPersonEmployeeType() == 2) {
				selection = this.selectionRepo.getAllSelectionByHistoryId(
						itemCommand.getSingleItem().getSelectionItemId(), GeneralDate.today(), 1);
			}
			if (selection == null || selection.size() == 0) {

				throw new BusinessException("Msg_587");

			}

		}
		
		if (!this.pernfoItemDefRep.checkItemNameIsUnique(itemCommand.getPerInfoCtgId(), itemCommand.getItemName(),
				itemCommand.getPerInfoItemDefId())) {
			throw new BusinessException("Msg_358");
		}
		PersonInfoItemDefinition oldItem = this.pernfoItemDefRep
				.getPerInfoItemDefById(itemCommand.getPerInfoItemDefId(), contractCd).orElse(null);
		PersonInfoCategory category = this.perInfoCtgRep.getPerInfoCategory(oldItem.getPerInfoCategoryId(), contractCd)
				.orElse(null);
		if (category == null) {
			return null;
		}
		List<String> perInfoCtgIds = this.perInfoCtgRep.getPerInfoCtgIdList(companyIdList,
				category.getCategoryCode().v());
		if (oldItem == null || oldItem.getIsFixed() == IsFixed.FIXED) {
			return null;
		}
		
		if(this.isCheckData(oldItem.getItemCode().toString(), perInfoCtgIds)) {
			throw new BusinessException("Msg_233");
		}
		oldItem.setItemName(itemCommand.getItemName());
		PersonInfoItemDefinition newItem = MappingDtoToDomain.mappingFromDomaintoCommandForUpdate(itemCommand, oldItem);
		
		this.pernfoItemDefRep.updatePerInfoItemDefRoot(newItem, contractCd);
		return null;
	}

	private boolean isCheckData(String itemCode, List<String> ctgLst) {
		boolean itemAuth = this.itemAuthRepo.hasItemData(itemCode, ctgLst),
				itemInit = this.itemInitRepo.hasItemData(itemCode, ctgLst),
				isEmpData = this.empInfoRepo.hasItemData(itemCode, ctgLst),
				isPerData = this.perItemRepo.hasItemData(ctgLst, itemCode);
		if (itemAuth || itemInit || isEmpData || isPerData) {
			return true;
		}
		return false;

	}

}
