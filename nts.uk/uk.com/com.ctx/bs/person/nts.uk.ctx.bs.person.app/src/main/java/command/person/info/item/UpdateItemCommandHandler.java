package command.person.info.item;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.bs.person.dom.person.info.category.IsFixed;
import nts.uk.ctx.bs.person.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.item.PersonInfoItemDefinition;

@Stateless
public class UpdateItemCommandHandler extends CommandHandlerWithResult<UpdateItemCommand, String> {

	@Inject
	private PerInfoItemDefRepositoty pernfoItemDefRep;

	@Override
	protected String handle(CommandHandlerContext<UpdateItemCommand> context) {
		UpdateItemCommand itemCommand = context.getCommand();
		String mess = "Msg_233";
		String contractCd = PersonInfoItemDefinition.ROOT_CONTRACT_CODE;
		PersonInfoItemDefinition oldItem = this.pernfoItemDefRep
				.getPerInfoItemDefById(itemCommand.getPerInfoItemDefId(), contractCd).orElse(null);
		oldItem.validate();
		if (oldItem == null || oldItem.getIsFixed() == IsFixed.FIXED) {
			return null;
		}
		oldItem.setItemName(itemCommand.getItemName());
		PersonInfoItemDefinition newItem = oldItem;
		if (!checkQuantityItemData()) {
			newItem = MappingDtoToDomain.mappingFromDomaintoCommandForUpdate(itemCommand, oldItem);
			mess = null;
		}
		this.pernfoItemDefRep.updatePerInfoItemDefRoot(newItem, contractCd);
		return mess;
	}

	private boolean checkQuantityItemData() {
		// TODO-TuongVC: sau nay khi lam den domain [PersonInfoItemData] can
		// hoan thien not
		/*
		 * activity lien quan: [PersonInfoItemData] ở đây lấy như thế nào nhỉ
		 * Đứclần giải thích tiếp theo sẽ có giải thích về bảng này anh cứ viết
		 * method check để return true là mặc định sau khi có bảng rồi thì viết
		 * logic sau cũng được
		 */
		// Hiện tại trả về true ~ số lượng > 1
		return true;
	}

}
