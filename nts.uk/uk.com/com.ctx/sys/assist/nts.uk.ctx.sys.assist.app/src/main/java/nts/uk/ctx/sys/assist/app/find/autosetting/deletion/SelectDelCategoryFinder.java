package nts.uk.ctx.sys.assist.app.find.autosetting.deletion;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.app.command.autosetting.deletion.SelectDelCategoryCommand;
import nts.uk.ctx.sys.assist.app.find.autosetting.TextResourceHolderDto;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDelete;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDeleteRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionPatternSettingRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionSelectionCategory;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionSelectionCategoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * アルゴリズム「画面表示処理」
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SelectDelCategoryFinder {

	@Inject
	private CategoryForDeleteRepository categoryRepository;

	@Inject
	private DataDeletionSelectionCategoryRepository dataDeletionSelectionCategoryRepository;

	@Inject
	private DataDeletionPatternSettingRepository dataDeletionPatternSettingRepository;

	public DataDeletionPatternSettingDto findSelectCategoryInfo(SelectDelCategoryCommand command) {

		List<DelSelectionCategoryNameDto> categoryNames = findSelectionCategoryName(command);

		// 設定初期表示処理
		return dataDeletionPatternSettingRepository
				.findByPk(AppContexts.user().contractCode(), command.getPatternCode(), command.getPatternClassification())
				.map(pattern -> {
					DataDeletionPatternSettingDto dto = new DataDeletionPatternSettingDto();
					pattern.setMemento(dto);
					dto.setSelectCategories(categoryNames);
					return dto;
				}).orElse(null);
	}

	private List<DelSelectionCategoryNameDto> findSelectionCategoryName(SelectDelCategoryCommand command) {

		// 選択カテゴリを取得する
		List<DataDeletionSelectionCategory> selectCategories = dataDeletionSelectionCategoryRepository
				.findByPatternCdAndPatternAtrAndSystemTypes(command.getPatternCode(),
						command.getPatternClassification(), command.getSystemType());

		// ドメインモデル「カテゴリ」を取得する
		List<CategoryForDelete> categories = categoryRepository.getCategoryByListId(
				selectCategories.stream().map(data -> data.getCategoryId().v()).collect(Collectors.toList()));

		// 1. オブジェクト「選択カテゴリ名称」を作成する
		// 2. 作成したList<選択カテゴリ名称>を返す。
		return selectCategories.stream().map(sc -> {
			CategoryForDelete master = categories.stream().filter(c -> c.getCategoryId().v().equals(sc.getCategoryId().v()))
					.findAny().get();
			DelSelectionCategoryNameDto dto = new DelSelectionCategoryNameDto();
			dto.setCategoryId(sc.getCategoryId().v());
			dto.setCategoryName(master.getCategoryName().v());
			dto.setSystemType(sc.getSystemType().value);
			dto.setPeriodDivision(master.getTimeStore().value);
			dto.setSeparateCompClassification(master.getOtherCompanyCls().value);
			dto.setSpecifiedMethod(master.getStoredProcedureSpecified().value);
			dto.setStoreRange(master.getStorageRangeSaved().value);
			dto.setHolder(new TextResourceHolderDto("CMF003_634", sc.getSystemType().nameId));
			return dto;
		}).sorted(Comparator.comparing(DelSelectionCategoryNameDto::getCategoryId)).collect(Collectors.toList());
	}
}
