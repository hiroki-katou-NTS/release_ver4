package nts.uk.ctx.pr.proto.app.find.layout.detail;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.uk.ctx.pr.proto.dom.layout.detail.LayoutMasterDetailRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
public class LayoutMasterDetailFinder {
	@Inject
	private LayoutMasterDetailRepository repository;
	/**
	 * find all layout master details
	 * @param companyCd
	 * @param stmtCd
	 * @param startYm
	 * @return
	 */
	public List<LayoutMasterDetailDto> getDetails(String companyCd,
			String stmtCd,
			int startYm){
		return this.repository.getDetails(companyCd,
				stmtCd,
				startYm).stream()
				.map(item ->LayoutMasterDetailDto.fromDomain(item))
				.collect(Collectors.toList());
	}
	/**
	 * find layout master detail
	 * @param companyCd
	 * @param stmtCd
	 * @param startYm
	 * @param categoryAtr
	 * @param itemCd
	 * @return
	 */
	public Optional<LayoutMasterDetailDto> getDetail(
			  String stmtCd
			, int startYm
			, int categoryAtr
			, String itemCd){
		
		return this.repository.getDetail(AppContexts.user().companyCode(), stmtCd, startYm, categoryAtr, itemCd)
				.map(item -> LayoutMasterDetailDto.fromDomain(item));
	}
}
