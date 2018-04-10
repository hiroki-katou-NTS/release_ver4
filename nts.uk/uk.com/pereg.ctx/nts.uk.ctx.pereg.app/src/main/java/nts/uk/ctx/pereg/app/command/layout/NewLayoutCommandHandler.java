package nts.uk.ctx.pereg.app.command.layout;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.I18NErrorMessage;
import nts.arc.error.RawErrorMessage;
import nts.arc.i18n.I18NText;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pereg.app.find.person.category.PerCtgInfoDto;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCtgFinder;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefDto;
import nts.uk.ctx.pereg.app.find.person.info.item.PerInfoItemDefFinder;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.daterangeitem.DateRangeItem;
import nts.uk.ctx.pereg.dom.person.layout.INewLayoutReposotory;
import nts.uk.ctx.pereg.dom.person.layout.NewLayout;
import nts.uk.ctx.pereg.dom.person.layout.classification.ILayoutPersonInfoClsRepository;
import nts.uk.ctx.pereg.dom.person.layout.classification.LayoutPersonInfoClassification;
import nts.uk.ctx.pereg.dom.person.layout.classification.definition.ILayoutPersonInfoClsDefRepository;
import nts.uk.ctx.pereg.dom.person.layout.classification.definition.LayoutPersonInfoClsDefinition;

@Stateless
@Transactional
public class NewLayoutCommandHandler extends CommandHandler<NewLayoutCommand> {

	@Inject
	INewLayoutReposotory layoutRepo;

	@Inject
	ILayoutPersonInfoClsRepository classfRepo;

	@Inject
	ILayoutPersonInfoClsDefRepository clsDefRepo;

	@Inject
	PerInfoItemDefFinder itemDefFinder;

	@Inject
	PerInfoCtgFinder itemCtgFinder;

	@Inject
	private PerInfoCategoryRepositoty perInfoCtgRepositoty;

	@Override
	protected void handle(CommandHandlerContext<NewLayoutCommand> context) {

		// get new layout domain and command
		NewLayout update = layoutRepo.getLayout(true).get();
		NewLayoutCommand command = context.getCommand();

		// update layout
		layoutRepo.update(update);

		// validate all usecase [Registration] at here
		// throw exception if not valid
		List<String> requiredIds = itemDefFinder.getRequiredIds();
		List<String> allSaveItemIds = command.getItemsClassification().stream().map(m -> m.listItemClsDf)
				.flatMap(List::stream).map(m -> m.getPersonInfoItemDefinitionID()).sorted()
				.collect(Collectors.toList());

		// エラーメッセージ（#Msg_201,システム必須項目のうち配置されていない項目（カンマ区切りの文字列））を表示する
		if (!allSaveItemIds.containsAll(requiredIds)) {
			requiredIds = requiredIds.stream().filter(m -> allSaveItemIds.indexOf(m) == -1)
					.collect(Collectors.toList());
			List<PerInfoItemDefDto> dto = itemDefFinder.getPerInfoItemDefByListId(requiredIds);
			if (!dto.isEmpty()) {
				String alert = String.join(", ", dto.stream().map(m -> m.getItemName()).collect(Collectors.toList()));

				throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_201").addRaw(alert).build()));
			}

			throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_201").build()));
		}

		// エラーメッセージ（#Msg_289#,２つ以上配置されている項目名）を表示する
		for (int i = 0; i < allSaveItemIds.size() - 2; i++) {
			if (allSaveItemIds.get(i).equals(allSaveItemIds.get(i + 1))) {
				throw new BusinessException(new RawErrorMessage("Msg_289"));
			}
		}

		List<String> allCatIds = command.getItemsClassification().stream().map(m -> m.getPersonInfoCategoryID())
				.collect(Collectors.toList());

		// EA修正履歴1018
		// カテゴリ内の必須項目チェックを追加。
		for (String categoryId : allCatIds) {

			Optional<DateRangeItem> range = perInfoCtgRepositoty.getDateRangeItemByCategoryId(categoryId);

			if (range.isPresent()) {
				PerCtgInfoDto ctgDto = itemCtgFinder.getDetailCtgInfo(categoryId);

				PerInfoItemDefDto startDto = itemDefFinder.getPerInfoItemDefByItemDefId(range.getStartDateItemId());
				PerInfoItemDefDto endDto = itemDefFinder.getPerInfoItemDefByItemDefId(range.getEndDateItemId());

				if (startDto.getIsAbolition() == 0 && endDto.getIsAbolition() == 0) {
					if (!allSaveItemIds.contains(startDto.getId()) && startDto.getIsRequired() == 1
							&& allSaveItemIds.contains(endDto.getId())) {
						throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_1111")
								.addRaw(ctgDto.getCategoryName()).addRaw(startDto.getItemName()).build()));
					} else if (allSaveItemIds.contains(startDto.getId()) && !allSaveItemIds.contains(endDto.getId())
							&& endDto.getIsRequired() == 1) {
						throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_1111")
								.addRaw(ctgDto.getCategoryName()).addRaw(endDto.getItemName()).build()));
					} else if (!allSaveItemIds.contains(startDto.getId()) && !allSaveItemIds.contains(endDto.getId())) {
						if (startDto.getIsRequired() == 1 && endDto.getIsRequired() == 1) {
							throw new BusinessException(
									new I18NErrorMessage(I18NText.main("Msg_1111").addRaw(ctgDto.getCategoryName())
											.addRaw(startDto.getItemName() + "," + endDto.getItemName()).build()));
						} else if (startDto.getIsRequired() == 1) {
							throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_1111")
									.addRaw(ctgDto.getCategoryName()).addRaw(startDto.getItemName()).build()));
						} else if (endDto.getIsRequired() == 1) {
							throw new BusinessException(new I18NErrorMessage(I18NText.main("Msg_1111")
									.addRaw(ctgDto.getCategoryName()).addRaw(endDto.getItemName()).build()));
						}
					}
				}
			}
		}

		// remove all classification in this layout
		classfRepo.removeAllByLayoutId(update.getLayoutID());

		// remove all itemdefinition relation with classification in this layout
		clsDefRepo.removeAllByLayoutId(update.getLayoutID());

		// push all classification and item definition to db
		List<ClassificationCommand> classCommands = command.getItemsClassification();
		if (!classCommands.isEmpty()) {
			// add all classification on client to db
			classfRepo.addClassifications(classCommands.stream()
					.map(m -> toClassificationDomain(m, update.getLayoutID())).collect(Collectors.toList()));

			// add all item definition relation with classification to db
			for (ClassificationCommand classCommand : classCommands) {
				List<ClassificationItemDfCommand> clsIDfs = classCommand.getListItemClsDf();
				if (!clsIDfs.isEmpty()) {
					clsDefRepo.addClassificationItemDefines(clsIDfs.stream()
							.map(m -> toClassItemDefDomain(m, update.getLayoutID(), classCommand.getDispOrder()))
							.collect(Collectors.toList()));
				}
			}
		}
	}

	private LayoutPersonInfoClassification toClassificationDomain(ClassificationCommand command, String layoutId) {
		return LayoutPersonInfoClassification.createFromJaveType(layoutId, command.getDispOrder(),
				command.getPersonInfoCategoryID(), command.getLayoutItemType());
	}

	private LayoutPersonInfoClsDefinition toClassItemDefDomain(ClassificationItemDfCommand command, String layoutId,
			int classDispOrder) {
		return LayoutPersonInfoClsDefinition.createFromJavaType(layoutId, classDispOrder, command.getDispOrder(),
				command.getPersonInfoItemDefinitionID());
	}

}
