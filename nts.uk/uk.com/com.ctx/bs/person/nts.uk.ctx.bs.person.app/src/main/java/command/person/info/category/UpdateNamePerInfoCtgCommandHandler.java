package command.person.info.category;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PerInfoCtgByCompanyRepositoty;
import nts.uk.ctx.bs.person.dom.person.info.category.PersonInfoCategory;
import nts.uk.shr.com.context.AppContexts;
/**
 * The class UpdateNamePerInfoCtgCommandHandler 
 * @author lanlt
 *
 */

@Stateless
public class UpdateNamePerInfoCtgCommandHandler extends CommandHandler<UpdateNamePerInfoCtgCommand> {
	@Inject
	private PerInfoCategoryRepositoty perInfoCategoryRepositoty;

	@Inject
	private PerInfoCtgByCompanyRepositoty perInfoCtgRepositoty;

	@Override
	protected void handle(CommandHandlerContext<UpdateNamePerInfoCtgCommand> context) {
		UpdateNamePerInfoCtgCommand update = context.getCommand();
		String companyId = AppContexts.user().companyId();
		String contractCd = companyId.substring(0, 12);
		boolean isExistName = this.perInfoCategoryRepositoty.checkCtgNameIsUnique(companyId, update.getCategoryName());
		if (isExistName) {
			PersonInfoCategory categoryInfo = this.perInfoCtgRepositoty
					.getDetailCategoryInfo(companyId, update.getCategoryId(), contractCd).orElse(null);
			if (categoryInfo != null) {
				List<String> ctgIdList = this.perInfoCtgRepositoty.getItemInfoId(update.getCategoryId(),
						contractCd);
				PersonInfoCategory categoryUpdate = null;
				if (ctgIdList.size() > 0) {
					categoryUpdate = PersonInfoCategory.createFromEntity(update.getCategoryId(), companyId,
							categoryInfo.getCategoryCode().v(), categoryInfo.getCategoryParentCode().v(),
							update.getCategoryName(), categoryInfo.getPersonEmployeeType().value,
							categoryInfo.getIsAbolition().value, categoryInfo.getCategoryType().value,
							categoryInfo.getIsFixed().value);

				} else {
					categoryUpdate = PersonInfoCategory.createFromEntity(update.getCategoryId(), companyId,
							categoryInfo.getCategoryCode().v(), categoryInfo.getCategoryParentCode().v(),
							update.getCategoryName(), categoryInfo.getPersonEmployeeType().value,
							update.isAbolition() == true ? 1 : 0, categoryInfo.getCategoryType().value,
							categoryInfo.getIsFixed().value);

				}
				this.perInfoCtgRepositoty.update(categoryUpdate);
			}
		} else {
			throw new BusinessException("Msg215");

		}

	}

}
