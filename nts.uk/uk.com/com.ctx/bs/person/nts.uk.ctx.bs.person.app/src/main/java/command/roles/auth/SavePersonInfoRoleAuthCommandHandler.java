package command.roles.auth;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import command.roles.auth.category.SavePersonInfoCategoryAuthCommand;
import command.roles.auth.item.PersonInfoItemAuthCommand;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.bs.person.dom.person.role.auth.PersonInfoRoleAuth;
import nts.uk.ctx.bs.person.dom.person.role.auth.PersonInfoRoleAuthRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.category.PersonInfoCategoryAuth;
import nts.uk.ctx.bs.person.dom.person.role.auth.category.PersonInfoCategoryAuthRepository;
import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemAuth;
import nts.uk.ctx.bs.person.dom.person.role.auth.item.PersonInfoItemAuthRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class SavePersonInfoRoleAuthCommandHandler extends CommandHandler<SavePersonInfoRoleAuthCommand> {
	@Inject
	private PersonInfoRoleAuthRepository pRoleAuthRepo;
	/*
	 * @Inject private PersonInforRoleRepository pRoleRepo;
	 */
	/*
	 * @Inject private PerInfoCategoryRepositoty perInfoCtgRepositoty;
	 */
	@Inject
	private PersonInfoCategoryAuthRepository pCategoryAuthRepo;
	@Inject
	private PersonInfoItemAuthRepository pItemAuthRepo;

	@Override
	public void handle(CommandHandlerContext<SavePersonInfoRoleAuthCommand> context) {

		SavePersonInfoRoleAuthCommand roleCommand = context.getCommand();

		saveRoleAuth(roleCommand);

		saveCategoryAuth(roleCommand);

		saveItemAuth(roleCommand);

	}

	private void saveRoleAuth(SavePersonInfoRoleAuthCommand roleCommand) {

		String companyId = AppContexts.user().companyId();

		String roleId = roleCommand.getRoleId();

		/*
		 * Optional<PersonInforRole> optRole =
		 * this.pRoleRepo.getDetailPersonRole(roleId,
		 * AppContexts.user().companyId());
		 * 
		 * if (!optRole.isPresent()) { throw new BusinessException(new
		 * RawErrorMessage("")); }
		 */

		Optional<PersonInfoRoleAuth> optRoleAuth = this.pRoleAuthRepo.getDetailPersonRoleAuth(roleId,
				AppContexts.user().companyId());

		if (optRoleAuth.isPresent()) {

			PersonInfoRoleAuth oldRoleAuth = optRoleAuth.get();

			oldRoleAuth.updateFromJavaType(roleCommand.getAllowMapUpload(), roleCommand.getAllowMapBrowse(),
					roleCommand.getAllowDocUpload(), roleCommand.getAllowDocRef(), roleCommand.getAllowAvatarUpload(),
					roleCommand.getAllowAvatarRef());

			this.pRoleAuthRepo.update(oldRoleAuth);

		} else {
			PersonInfoRoleAuth pRoleAuthDomain = PersonInfoRoleAuth.createFromJavaType(roleId, companyId,
					roleCommand.getAllowMapUpload(), roleCommand.getAllowMapBrowse(), roleCommand.getAllowDocUpload(),
					roleCommand.getAllowDocRef(), roleCommand.getAllowAvatarUpload(), roleCommand.getAllowAvatarRef());

			this.pRoleAuthRepo.add(pRoleAuthDomain);

		}

	}

	private void saveCategoryAuth(SavePersonInfoRoleAuthCommand roleCommand) {

		SavePersonInfoCategoryAuthCommand pCategoryCommand = roleCommand.getCurrentCategory();

		String categoryId = pCategoryCommand.getCategoryId();

		/* String contractCode = AppContexts.user().contractCode(); */

		String roleId = roleCommand.getRoleId();

		/*
		 * Optional<PersonInfoCategory> optPCategory =
		 * this.perInfoCtgRepositoty.getPerInfoCategory(categoryId,
		 * contractCode);
		 * 
		 * if (!optPCategory.isPresent()) { throw new BusinessException(new
		 * RawErrorMessage("")); }
		 */

		Optional<PersonInfoCategoryAuth> optPCategoryAuth = this.pCategoryAuthRepo
				.getDetailPersonCategoryAuthByPId(roleId, categoryId);

		if (optPCategoryAuth.isPresent()) {
			PersonInfoCategoryAuth oldPCategoryAuth = optPCategoryAuth.get();
			oldPCategoryAuth.updateFromJavaType(pCategoryCommand.getAllowPersonRef(),
					pCategoryCommand.getAllowOtherRef(), pCategoryCommand.getAllowOtherCompanyRef(),
					pCategoryCommand.getSelfPastHisAuth(), pCategoryCommand.getSelfFutureHisAuth(),
					pCategoryCommand.getSelfAllowAddHis(), pCategoryCommand.getSelfAllowDelHis(),
					pCategoryCommand.getOtherPastHisAuth(), pCategoryCommand.getOtherFutureHisAuth(),
					pCategoryCommand.getOtherAllowAddHis(), pCategoryCommand.getOtherAllowDelHis(),
					pCategoryCommand.getSelfAllowAddMulti(), pCategoryCommand.getSelfAllowDelMulti(),
					pCategoryCommand.getOtherAllowAddMulti(), pCategoryCommand.getOtherAllowDelMulti());

			this.pCategoryAuthRepo.update(oldPCategoryAuth);

		} else {

			PersonInfoCategoryAuth pCategoryAuthDomain = PersonInfoCategoryAuth.createFromJavaType(roleId,
					pCategoryCommand.getCategoryId(), pCategoryCommand.getAllowPersonRef(),
					pCategoryCommand.getAllowOtherRef(), pCategoryCommand.getAllowOtherCompanyRef(),
					pCategoryCommand.getSelfPastHisAuth(), pCategoryCommand.getSelfFutureHisAuth(),
					pCategoryCommand.getSelfAllowAddHis(), pCategoryCommand.getSelfAllowDelHis(),
					pCategoryCommand.getOtherPastHisAuth(), pCategoryCommand.getOtherFutureHisAuth(),
					pCategoryCommand.getOtherAllowAddHis(), pCategoryCommand.getOtherAllowDelHis(),
					pCategoryCommand.getSelfAllowAddMulti(), pCategoryCommand.getSelfAllowDelMulti(),
					pCategoryCommand.getOtherAllowAddMulti(), pCategoryCommand.getOtherAllowDelMulti());

			this.pCategoryAuthRepo.add(pCategoryAuthDomain);

		}
	}

	private void saveItemAuth(SavePersonInfoRoleAuthCommand roleCommand) {

		SavePersonInfoCategoryAuthCommand pCategoryCommand = roleCommand.getCurrentCategory();

		String roleId = roleCommand.getRoleId();

		String categoryId = pCategoryCommand.getCategoryId();

		List<PersonInfoItemAuthCommand> listItems = pCategoryCommand.getRoleItemList();

		// List<PersonInfoItemDetail> itemDetailList =
		// this.pItemAuthRepo.getAllItemDetail(roleId, categoryId,
		// AppContexts.user().contractCode());

		for (PersonInfoItemAuthCommand pItemDetailCmd : listItems) {

			// PersonInfoItemDetail pitemDetail = itemDetailList.stream()
			// .filter(x ->
			// x.getPersonItemDefId().equals(pItemDetailCmd.getPersonItemDefId())).findFirst().get();
			//
			// if (pitemDetail == null) {
			// throw new BusinessException(new RawErrorMessage(""));
			// }

			Optional<PersonInfoItemAuth> optPItemAuth = this.pItemAuthRepo.getItemDetai(roleId, categoryId,
					pItemDetailCmd.getPersonItemDefId());

			if (optPItemAuth.isPresent()) {
				PersonInfoItemAuth oldItemAuthDomain = optPItemAuth.get();

				oldItemAuthDomain.updateFromJavaType(pItemDetailCmd.getSelfAuth(), pItemDetailCmd.getOtherAuth());
				this.pItemAuthRepo.update(oldItemAuthDomain);

			} else {

				PersonInfoItemAuth pItemAuthDomain = PersonInfoItemAuth.createFromJavaType(roleId, categoryId,
						pItemDetailCmd.getPersonItemDefId(), pItemDetailCmd.getSelfAuth(),
						pItemDetailCmd.getOtherAuth());

				this.pItemAuthRepo.add(pItemAuthDomain);

			}
		}
	}

}
