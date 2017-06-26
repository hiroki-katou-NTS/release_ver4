package nts.uk.ctx.pr.core.app.command.itemmaster;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateItemMasterNameCommandHandler extends CommandHandler<List<UpdateItemMasterCommand>> {

	@Inject
	private ItemMasterRepository itemMasterRepository;

	@Override
	protected void handle(CommandHandlerContext<List<UpdateItemMasterCommand>> context) {
		String companyCode = AppContexts.user().companyCode();

		// create loop for update Item Master Name
		for (UpdateItemMasterCommand updateCommand : context.getCommand()) {

			int categoryAtr = updateCommand.getCategoryAtr();

			String itemCode = updateCommand.getItemCode();

			// check Item Master Exists
			Optional<ItemMaster> itemmaster = this.itemMasterRepository.find(companyCode, categoryAtr, itemCode);
			if (!itemmaster.isPresent()) {

				// no , throw BusinessException
				throw new BusinessException(new RawErrorMessage("更新対象のデータが存在しません。"));// ER026
			}
			
			//if yes , create new itemMaster have data same item Master Just found Except the name (for just change ItemAbName and getItemAbNameE )
			ItemMaster newItemmaster = ItemMaster.createFromJavaType(companyCode, itemCode,
					itemmaster.get().getItemName().v(), itemmaster.get().getItemAbName().v(),
					updateCommand.getItemAbNameE(), updateCommand.getItemAbNameO(),
					itemmaster.get().getCategoryAtr().value, itemmaster.get().getFixAtr().value,
					itemmaster.get().getDisplaySet().value, itemmaster.get().getUniteCode().v(),
					itemmaster.get().getZeroDisplaySet().value, itemmaster.get().getItemDisplayAtr().value);
			
			//and update it
			this.itemMasterRepository.update(newItemmaster);
		}
	}
}
