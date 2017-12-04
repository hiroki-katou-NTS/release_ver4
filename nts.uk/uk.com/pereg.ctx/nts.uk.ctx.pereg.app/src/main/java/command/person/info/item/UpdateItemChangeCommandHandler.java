package command.person.info.item;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.bs.person.dom.person.info.item.SystemRequired;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateItemChangeCommandHandler extends CommandHandler<UpdateItemChangeCommand> {

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;
	@Inject
	private SelectionRepository selectionRepo;

	@Override
	protected void handle(CommandHandlerContext<UpdateItemChangeCommand> context) {
		UpdateItemChangeCommand command = context.getCommand();
		PersonInfoItemDefinition itemDef = this.pernfoItemDefRep
				.getPerInfoItemDefById(command.getId(), AppContexts.user().contractCode()).get();
		List<Selection> selection = new ArrayList<>();
		if (command.getDataType() == 6) {
			if (command.getPersonEmployeeType() == 1) {
				
				selection = this.selectionRepo.getAllSelectionByHistoryId(command.getSelectionItemId(),
						GeneralDate.today(), 0);
				
			} else if (command.getPersonEmployeeType() == 2) {
				
				selection = this.selectionRepo.getAllSelectionByHistoryId(command.getSelectionItemId(),
						GeneralDate.today(), 1);

			}
			if (selection == null || selection.size() == 0) {

				throw new BusinessException(new RawErrorMessage("Msg_587"));

			}
		}
		PersonInfoItemDefinition itemDefDomain;
		if (itemDef.getSystemRequired().equals(SystemRequired.REQUIRED)) {
			itemDefDomain = PersonInfoItemDefinition.createFromEntity(itemDef.getPerInfoItemDefId(),
					itemDef.getPerInfoCategoryId(), itemDef.getItemCode().v(), itemDef.getItemParentCode().v(),
					command.getItemName(), itemDef.getIsAbolition().value, itemDef.getIsFixed().value,
					itemDef.getIsRequired().value, itemDef.getSystemRequired().value,
					itemDef.getRequireChangable().value);
		} else {
			itemDefDomain = PersonInfoItemDefinition.createFromEntity(itemDef.getPerInfoItemDefId(),
					itemDef.getPerInfoCategoryId(), itemDef.getItemCode().v(), itemDef.getItemParentCode().v(),
					command.getItemName(), command.getIsAbolition(), itemDef.getIsFixed().value,
					command.getIsRequired(), itemDef.getSystemRequired().value, itemDef.getRequireChangable().value);
		}

		this.pernfoItemDefRep.updatePerInfoItemDef(itemDefDomain);
	}

}
