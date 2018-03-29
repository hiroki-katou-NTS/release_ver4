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
import nts.uk.ctx.pereg.dom.person.info.category.PersonEmployeeType;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.person.personinfoctgdata.item.PerInfoItemDataRepository;
import nts.uk.ctx.pereg.dom.person.setting.init.item.PerInfoInitValueSetItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.ctx.pereg.dom.roles.auth.item.PersonInfoItemAuthRepository;
import nts.uk.shr.com.context.AppContexts;

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
		String itemName = itemCommand.getItemName();
		List<String> companyIdList = GetListCompanyOfContract.LIST_COMPANY_OF_CONTRACT;
		// String mess = "Msg_233";
		String contractCd = AppContexts.user().contractCode();
		
		// validate input
		validateCommand(itemCommand);
		
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
		oldItem.setItemName(itemName);
		PersonInfoItemDefinition newItem = MappingDtoToDomain.mappingFromDomaintoCommandForUpdate(itemCommand, oldItem);
		
		this.pernfoItemDefRep.updatePerInfoItemDefRoot(newItem, contractCd);
		return null;
	}

	private boolean isCheckData(String itemCode, List<String> ctgLst) {
		if (this.itemAuthRepo.hasItemData(itemCode, ctgLst)) {
			return true;
		}
		if (this.itemInitRepo.hasItemData(itemCode, ctgLst)) {
			return true;
		}
		
		if (this.empInfoRepo.hasItemData(itemCode, ctgLst)) {
			return true;
		}
		if (this.perItemRepo.hasItemData(ctgLst, itemCode)) {
			return true;
		}
		return false;
	}
	
	private void validateCommand(UpdateItemCommand itemCommand) {
		
		String itemName = itemCommand.getItemName();
		
		if (CheckNameSpace.checkName(itemName)){
			throw new BusinessException("Msg_928");
		}
		
		if (!this.pernfoItemDefRep.checkItemNameIsUnique(itemCommand.getPerInfoCtgId(), itemName,
				itemCommand.getPerInfoItemDefId())) {
			throw new BusinessException("Msg_358");
		}
		
		if (itemCommand.getSingleItem().getDataType() == 6) {
			String itemId = itemCommand.getSingleItem().getSelectionItemId();
			GeneralDate today = GeneralDate.today();
			List<Selection> selection = new ArrayList<>();
			String zeroCompanyId = AppContexts.user().zeroCompanyIdInContract();
			if (itemCommand.getPersonEmployeeType() == PersonEmployeeType.PERSON.value) {
				selection = this.selectionRepo.getAllSelectionByHistoryId(zeroCompanyId, itemId, today, 0);
			} else if (itemCommand.getPersonEmployeeType() == PersonEmployeeType.EMPLOYEE.value) {
				selection = this.selectionRepo.getAllSelectionByHistoryId(zeroCompanyId, itemId, today, 1);
			}
			if (selection == null || selection.size() == 0) {
				throw new BusinessException("Msg_587");
			}

		}
		
	}

}
