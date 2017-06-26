package nts.uk.ctx.pr.core.app.find.rule.employment.layout.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail.LayoutMasterDetailDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.line.LayoutMasterLineDto;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class LayoutMasterCategoryFinder {
	@Inject
	private LayoutMasterCategoryRepository cateRepo;
	@Inject
	private LayoutMasterLineRepository lineRepo;
	@Inject
	private LayoutMasterDetailRepository detailRepo;

	public List<LayoutMasterCategoryDto> getCategories(String companyCd, String layoutCd, int startYm) {
		return this.cateRepo.getCategories(companyCd, layoutCd, startYm).stream()
				.map(c -> LayoutMasterCategoryDto.fromDomain(c)).collect(Collectors.toList());
	}

	/**
	 * get LayoutMasterCategory with lines and details data by LayoutCode
	 * 
	 * @param layoutCd
	 * @param startYm
	 * @return
	 */
	public List<LayoutMasterCategoryDto> getCategoriesFullData(String layoutCd, String historyId, int startYm) {
		String companyCd = AppContexts.user().companyCode();
		List<LayoutMasterCategoryDto> categories = getCategories(historyId);
		List<LayoutMasterLineDto> lines = getLines(historyId);
		List<LayoutMasterDetailDto> details = getDetails(historyId);
		List<LayoutMasterDetailDto> detail9Items = new ArrayList<>();

		categories.sort((c1, c2) -> c1.getCtgPos() - c2.getCtgPos());
		for (LayoutMasterCategoryDto category : categories) {
			List<LayoutMasterLineDto> linesOfCategory = lines.stream()
					.filter(l -> l.getCategoryAtr() == category.getCategoryAtr()).collect(Collectors.toList());
			linesOfCategory.sort((c1, c2) -> c1.getLinePosition() - c2.getLinePosition());
			for (LayoutMasterLineDto line : linesOfCategory) {
				detail9Items = new ArrayList<>();
				for (int i = 1; i <= 9; i++) {
					int posColumn = i;
					LayoutMasterDetailDto item = details.stream()
							.filter(d -> d.getAutoLineId().equals(line.getAutoLineId())
									&& d.getCategoryAtr() == line.getCategoryAtr() && d.getItemPosColumn() == posColumn)
							.findFirst().orElse(
									 new LayoutMasterDetailDto(layoutCd,
									 category.getCategoryAtr(), "itemTemp-" +String.valueOf(i),
									 line.getAutoLineId(), i , 0, "+",
									 0, 0, 0, 0, "", "", "", BigDecimal.ZERO,
									 0, BigDecimal.ZERO, 0, BigDecimal.ZERO,
									 0, BigDecimal.ZERO, 0, BigDecimal.ZERO));
					detail9Items.add(item);
				}

				line.getDetails().addAll(detail9Items);
			}
			category.getLines().addAll(linesOfCategory);
		}

		return categories;
	}

	private List<LayoutMasterDetailDto> getDetails(String historyId) {
		return this.detailRepo.getDetails(historyId).stream()
				.map(c -> LayoutMasterDetailDto.fromDomain(c)).collect(Collectors.toList());
	}

	private List<LayoutMasterLineDto> getLines(String historyId) {
		return this.lineRepo.getLines(historyId).stream().map(c -> LayoutMasterLineDto.fromDomain(c))
				.collect(Collectors.toList());
	}

	private List<LayoutMasterCategoryDto> getCategories(String historyId) {
		return this.cateRepo.getCategories(historyId).stream()
				.map(c -> LayoutMasterCategoryDto.fromDomain(c)).collect(Collectors.toList());
	}
}
