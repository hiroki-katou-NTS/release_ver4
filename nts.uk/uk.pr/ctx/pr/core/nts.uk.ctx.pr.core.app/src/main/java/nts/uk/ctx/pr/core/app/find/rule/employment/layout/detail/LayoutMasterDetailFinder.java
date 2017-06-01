package nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class LayoutMasterDetailFinder {
	@Inject
	private LayoutMasterDetailRepository repository;

	/**
	 * find all layout master details
	 * 
	 * @param companyCd
	 * @param stmtCd
	 * @param startYm
	 * @return
	 */
	public List<LayoutMasterDetailDto> getDetails(String companyCd, String stmtCd, int startYm) {
		return this.repository.getDetails(companyCd, stmtCd, startYm).stream()
				.map(item -> LayoutMasterDetailDto.fromDomain(item)).collect(Collectors.toList());
	}

	/**
	 * find layout master detail
	 * 
	 * @param companyCd
	 * @param stmtCd
	 * @param historyId
	 * @param categoryAtr
	 * @param itemCd
	 * @return
	 */
	public LayoutMasterDetailDto getDetail(String stmtCd, String historyId, int categoryAtr, String itemCd) {
		Optional<LayoutMasterDetailDto> itemOp = this.repository
				.getDetail(AppContexts.user().companyCode(), stmtCd, historyId, categoryAtr, itemCd)
				.map(item -> LayoutMasterDetailDto.fromDomain(item));
		return !itemOp.isPresent() ? null : itemOp.get();
	}
}
