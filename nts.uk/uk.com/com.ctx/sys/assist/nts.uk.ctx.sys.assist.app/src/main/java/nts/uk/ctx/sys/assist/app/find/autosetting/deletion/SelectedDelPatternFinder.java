package nts.uk.ctx.sys.assist.app.find.autosetting.deletion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.app.command.autosetting.deletion.FindDelSelectedPatternCommand;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDelete;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDeleteRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionPatternSetting;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionPatternSettingRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionSelectionCategory;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionSelectionCategoryRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SelectedDelPatternFinder {

	@Inject
	private DataDeletionPatternSettingRepository dataDeletionPatternSettingRepository;

	@Inject
	private DataDeletionSelectionCategoryRepository dataDeletionSelectionCategoryRepository;

	@Inject
	private CategoryForDeleteRepository categoryForDeleteRepository;

	public SelectedDelPatternParameterDto findSelectedPattern(FindDelSelectedPatternCommand command) {
		SelectedDelPatternParameterDto dto = new SelectedDelPatternParameterDto();
		// オブジェクト「選択カテゴリ」を取得する
		List<DataDeletionSelectionCategory> selectCategories = dataDeletionSelectionCategoryRepository
				.findByPatternCdAndPatternAtrAndSystemTypes(command.getPatternCode(),
						command.getPatternClassification(), command.getCategories().stream()
								.map(DeleteCategoryDto::getSystemType).collect(Collectors.toList()),
						AppContexts.user().contractCode());
		// ドメインモデル「カテゴリ」を取得する
		List<CategoryForDelete> categories = categoryForDeleteRepository.getCategoryByListId(
				selectCategories.stream().map(c -> c.getCategoryId().v()).collect(Collectors.toList()));
		Optional<DataDeletionPatternSetting> op = dataDeletionPatternSettingRepository.findByPk(
				AppContexts.user().contractCode(), command.getPatternCode(), command.getPatternClassification());

		// List<選択カテゴリ名称＞を作成
		if (op.isPresent()) {
			DataDeletionPatternSetting pattern = op.get();
			dto.setPattern(DataDeletionPatternSettingDto.createFromDomain(pattern));
			dto.getPattern().setSelectCategories(selectCategories.stream().map(sc -> 
				categories.stream().filter(u -> u.getCategoryId().equals(sc.getCategoryId()))
						.findFirst()
						.map(category -> SelectionDelCategoryNameDto.builder()
							.categoryId(sc.getCategoryId().v())
							.categoryName(category.getCategoryName().v())
							.retentionPeriod(category.getTimeStore().nameId)
							.systemType(sc.getSystemType().value)
							.build())
						.orElse(null)
			).filter(Objects::nonNull).collect(Collectors.toList()));
		}

		// List<選択可能カテゴリ＞を作成
		dto.setSelectableCategories(getSelectable(dto.getPattern().getSelectCategories(), command.getCategories()));
		// オブジェクト「選択パターンパラメータ」を返す。
		return dto;
	}

	private List<DeleteCategoryDto> getSelectable(List<SelectionDelCategoryNameDto> selected,
			List<DeleteCategoryDto> categories) {
		return categories.stream()
				.filter(c -> !selected.stream().anyMatch(
						s -> s.getCategoryId().equals(c.getCategoryId()) && s.getSystemType() == c.getSystemType()))
				.collect(Collectors.toList());
	}
}
