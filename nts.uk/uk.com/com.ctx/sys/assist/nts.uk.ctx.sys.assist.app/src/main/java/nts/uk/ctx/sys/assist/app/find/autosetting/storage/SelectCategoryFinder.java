package nts.uk.ctx.sys.assist.app.find.autosetting.storage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.app.command.autosetting.storage.SelectCategoryCommand;
import nts.uk.ctx.sys.assist.dom.category.Category;
import nts.uk.ctx.sys.assist.dom.category.CategoryRepository;
import nts.uk.ctx.sys.assist.dom.storage.DataStoragePatternSettingRepository;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageSelectionCategory;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageSelectionCategoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * アルゴリズム「画面表示処理」
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SelectCategoryFinder {

	@Inject
	private CategoryRepository categoryRepository;

	@Inject
	private DataStorageSelectionCategoryRepository dataStorageSelectionCategoryRepository;

	@Inject
	private DataStoragePatternSettingRepository dataStoragePatternSettingRepository;

	public DataStoragePatternSettingDto<SaveSelectionCategoryNameDto> findSelectCategoryInfo(
			SelectCategoryCommand command) {

		List<SaveSelectionCategoryNameDto> categoryNames = findSelectionCategoryName(command);

		// 設定初期表示処理
		return dataStoragePatternSettingRepository
				.findByContractCdAndPatternCdAndPatternAtr(AppContexts.user().contractCode(), command.getPatternCode(),
						command.getPatternClassification())
				.map(pattern -> {
					DataStoragePatternSettingDto<SaveSelectionCategoryNameDto> dto = DataStoragePatternSettingDto
							.createFromDomain(pattern);
					dto.setSelectCategories(categoryNames);
					return dto;
				}).orElse(null);
	}

	private List<SaveSelectionCategoryNameDto> findSelectionCategoryName(SelectCategoryCommand command) {

		// 選択カテゴリを取得する
		List<DataStorageSelectionCategory> selectCategories = dataStorageSelectionCategoryRepository
				.findByPatternCdAndPatternAtrAndSystemTypes(command.getPatternCode(),
						command.getPatternClassification(), command.getSystemType(), AppContexts.user().contractCode());

		// ドメインモデル「カテゴリ」を取得する
		List<Category> categories = categoryRepository.getCategoryByListId(
				selectCategories.stream().map(data -> data.getCategoryId().v()).collect(Collectors.toList()));

		// 1. オブジェクト「選択カテゴリ名称」を作成する
		// 2. 作成したList<選択カテゴリ名称>を返す。
		return selectCategories.stream()
				.map(sc -> categories.stream().filter(c -> c.getCategoryId().v().equals(sc.getCategoryId().v()))
						.findAny()
						.map(master -> SaveSelectionCategoryNameDto.builder().categoryId(sc.getCategoryId().v())
								.categoryName(master.getCategoryName().v()).systemType(sc.getSystemType().value)
								.periodDivision(master.getTimeStore().value)
								.separateCompClassification(master.getOtherCompanyCls().value)
								.specifiedMethod(master.getStoredProcedureSpecified().value)
								.storeRange(master.getStorageRangeSaved().value)
								.holder(new TextResourceHolderDto("CMF003_634", sc.getSystemType().nameId)).build())
						.orElse(null))
				.filter(Objects::nonNull).sorted(Comparator.comparing(SaveSelectionCategoryNameDto::getCategoryId))
				.collect(Collectors.toList());
	}
}
