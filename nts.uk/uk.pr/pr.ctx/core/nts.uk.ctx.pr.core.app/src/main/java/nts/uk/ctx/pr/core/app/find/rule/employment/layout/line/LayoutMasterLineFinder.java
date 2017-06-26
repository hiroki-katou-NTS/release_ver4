package nts.uk.ctx.pr.core.app.find.rule.employment.layout.line;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;

@Stateless
public class LayoutMasterLineFinder {
	@Inject
	private LayoutMasterLineRepository repository;
	
	public List<LayoutMasterLineDto> getLines(
			String companyCd,
			String stmtCd,
			int startYm){
		return this.repository.getLines(companyCd, stmtCd, startYm).stream()
				.map(item -> LayoutMasterLineDto.fromDomain(item))
				.collect(Collectors.toList());
	}
	public List<LayoutMasterLineDto> getLines(
			String companyCd,
			String stmtCd,
			String historyId){
		return this.repository.getLines(companyCd, stmtCd, historyId).stream()
				.map(item -> LayoutMasterLineDto.fromDomain(item))
				.collect(Collectors.toList());
	}
}
