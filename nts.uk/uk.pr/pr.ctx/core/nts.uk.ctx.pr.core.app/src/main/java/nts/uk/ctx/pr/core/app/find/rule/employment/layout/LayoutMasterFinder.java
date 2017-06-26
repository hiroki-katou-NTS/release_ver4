package nts.uk.ctx.pr.core.app.find.rule.employment.layout;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;

@Stateless
public class LayoutMasterFinder {
	@Inject
	private LayoutMasterRepository repository;
	
	@Inject
	private LayoutHistRepository layoutHistRepo;

	/**
	 * finder all layout by company code and layout code
	 * 
	 * @param companyCode
	 * @return
	 */
	public List<LayoutHeadDto> getAllLayoutHead(String companyCode) {
		return this.repository.getLayoutHeads(companyCode).stream().map(layout -> LayoutHeadDto.fromDomain(layout))
				.collect(Collectors.toList());
	}
	
	public List<LayoutHistoryDto> getAllLayoutHist(String companyCode) {
		return layoutHistRepo.getAllLayoutHist(companyCode).stream()
				.map(layout -> LayoutHistoryDto.fromDomain(layout)).collect(Collectors.toList());
	}

	/**
	 * finder layout by company code, layout code, start year month
	 * 
	 * @param companyCode
	 * @param stmtCode
	 * @param strYm
	 * @return
	 */
	public Optional<LayoutHeadDto> getLayout(String companyCode, String stmtCode, String historyId) {
		return this.repository.getLayout(companyCode, historyId, stmtCode).map(layout -> LayoutHeadDto.fromDomain(layout));
	}
	/**
	 * find all layouts with max startYM
	 * @param companyCode
	 * @return
	 */
	public List<LayoutHeadDto> getLayoutsWithMaxStartYm(String companyCode)
	{
		return this.repository.getLayoutsWithMaxStartYm(companyCode).stream()
				.map(layout -> LayoutHeadDto.fromDomain(layout))
				.collect(Collectors.toList());
	}
	// 17.03.2017 Lanlt add function getHistoryWithMaxStartYm(String companyCode)
	/**
	 * find all history with max startYm
	 * @param companyCode
	 * @return
	 */
	public List<LayoutHistoryDto> getHistoryWithMaxStartYm(String companyCode, int  baseYM)
	{
		return this.layoutHistRepo.getBy_SEL_3(companyCode,baseYM).stream()
				.map(layout -> LayoutHistoryDto.fromDomain(layout))
				.collect(Collectors.toList());
	}

}
