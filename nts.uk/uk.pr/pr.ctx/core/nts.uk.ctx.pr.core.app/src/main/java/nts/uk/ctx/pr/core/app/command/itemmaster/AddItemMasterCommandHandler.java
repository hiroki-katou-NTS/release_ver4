package nts.uk.ctx.pr.core.app.command.itemmaster;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttendRespository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeductRespository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class AddItemMasterCommandHandler extends CommandHandler<AddItemMasterCommand> {

	@Inject
	private ItemMasterRepository itemMasterRepository;
	// Inject for add sub item after add Item Master
	// item salary
	@Inject
	private ItemSalaryRespository itemSalaryRespository;

	// item deduct

	@Inject
	private ItemDeductRespository itemDeductRespository;

	// item attend

	@Inject
	private ItemAttendRespository itemAttendRespository;

	protected void handle(CommandHandlerContext<AddItemMasterCommand> context) {

		String companyCode = AppContexts.user().companyCode();

		int categoryAtr = context.getCommand().getCategoryAtr();

		ItemMaster itemMaster = context.getCommand().toDomain();

		// validate
		itemMaster.validate();
		// because add new from client item Code is not alphalnumberic
		itemMaster.validateAddNew();

		String itemCode = itemMaster.getItemCode().v();

		if (this.itemMasterRepository.find(companyCode, categoryAtr, itemCode).isPresent()) {
			throw new BusinessException(new RawErrorMessage("入力したコードは既に存在しています。\r\nを確認してください。")); // ER005
		}

		itemMasterRepository.add(itemMaster);

		// add sub item after add ItemMaster
		switch (categoryAtr) {

		case 0:

			// if item Category 支給
			addItemSalary(companyCode, context);
			break;

		case 1:

			// 控除
			addItemDeduct(companyCode, context);
			break;

		case 2:

			// 勤怠
			addItemAttend(companyCode, context);
			break;

		}

	}

	// add new Item Attend of this Item Master
	private void addItemAttend(String companyCode, CommandHandlerContext<AddItemMasterCommand> context) {

		String itemCode = context.getCommand().toDomain().getItemCode().v();

		context.getCommand().getItemAttend().setItemCode(itemCode);

		// use interface for add sub item
		if (this.itemAttendRespository.find(companyCode, itemCode).isPresent()) {
			throw new BusinessException(new RawErrorMessage("入力したコードは既に存在しています。\r\nを確認してください。")); // ER005
		}

		this.itemAttendRespository.add(companyCode, context.getCommand().getItemAttend().toDomain());

	}

	// add new Item Deduct of this Item Master
	private void addItemDeduct(String companyCode, CommandHandlerContext<AddItemMasterCommand> context) {

		String itemCode = context.getCommand().toDomain().getItemCode().v();

		context.getCommand().getItemDeduct().setItemCode(itemCode);
		// use interface for add sub item

		if (this.itemDeductRespository.find(companyCode, itemCode).isPresent()) {
			throw new BusinessException(new RawErrorMessage("入力したコードは既に存在しています。\r\nを確認してください。")); // ER005
		}

		this.itemDeductRespository.add(companyCode, context.getCommand().getItemDeduct().toDomain());

	}

	// add new Item Salary of this Item Master
	private void addItemSalary(String companyCode, CommandHandlerContext<AddItemMasterCommand> context) {

		String itemCode = context.getCommand().toDomain().getItemCode().v();

		context.getCommand().getItemSalary().setItemCode(itemCode);

		// use interface for add sub item
		if (this.itemSalaryRespository.find(companyCode, itemCode).isPresent()) {
			throw new BusinessException(new RawErrorMessage("入力したコードは既に存在しています。\r\nを確認してください。")); // ER005
		}

		this.itemSalaryRespository.add(companyCode, context.getCommand().getItemSalary().toDomain());

	}

}
