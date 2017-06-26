package nts.uk.ctx.pr.core.app.command.paymentdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPosCatalogLines;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPositionCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.SumScopeAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LineDispAtr;

class Helper {


	static List<DetailItem> createDetailsOfPayment(Map<CategoryAtr, List<DetailItem>> payDetail,
			List<LayoutMasterDetail> layoutDetailMasterList) {
		
		return payDetail.get(CategoryAtr.PAYMENT).stream()
			.filter(c -> layoutDetailMasterList.stream().anyMatch(x -> x.getItemCode().equals(c.getItemCode())))
			.collect(Collectors.toList());
	}

	static List<DetailItem> createDetailsOfDeduction(Map<CategoryAtr, List<DetailItem>> payDetail,
			List<LayoutMasterDetail> layoutDetailMasterList) {
		
		return payDetail.get(CategoryAtr.DEDUCTION).stream()
			.filter(c -> layoutDetailMasterList.stream().anyMatch(x -> x.getItemCode().equals(c.getItemCode())))
			.collect(Collectors.toList());
	}
	
	static List<PrintPositionCategory> createPositionCategory(List<LayoutMasterCategory> categoryList,
			List<LayoutMasterLine> lineList) {
		List<PrintPositionCategory> result = new ArrayList<>();
		
		categoryList.forEach(category -> {
			// find line
			List<LayoutMasterLine> lines = lineList.stream()
					.filter(x -> x.getCategoryAtr() == category.getCtAtr() && LineDispAtr.ENABLE == x.getLineDisplayAttribute())
					.collect(Collectors.toList());
			
			PrintPositionCategory printPositionCategory = new PrintPositionCategory(category.getCtAtr(), new PrintPosCatalogLines(lines.size()));
			if (!lines.isEmpty()) {
				result.add(printPositionCategory);
			}
		});
		
		return result;
	}

	/** get payment detail to calculate total payment **/
	static List<DetailItem> detailItemByTotalPayment(Map<CategoryAtr, List<DetailItem>> payDetail, List<LayoutMasterDetail> layoutMasterDetailList) {
		return payDetail.get(CategoryAtr.PAYMENT).stream()
				.filter(c -> layoutMasterDetailList.stream().anyMatch(x -> x.getItemCode().equals(c.getItemCode()) && SumScopeAtr.INCLUDED == x.getSumScopeAtr()))
				.collect(Collectors.toList());
	}
	
	/** get payment detail to calculate deduction total payment **/
	static List<DetailItem> layoutMasterByDeductionTotalPayment(Map<CategoryAtr, List<DetailItem>> payDetail, List<LayoutMasterDetail> layoutMasterDetailList) {
		return payDetail.get(CategoryAtr.DEDUCTION).stream()
				.filter(c -> layoutMasterDetailList.stream().anyMatch(x -> x.getItemCode().equals(c.getItemCode()) && SumScopeAtr.INCLUDED == x.getSumScopeAtr()))
				.collect(Collectors.toList());
	}
}
