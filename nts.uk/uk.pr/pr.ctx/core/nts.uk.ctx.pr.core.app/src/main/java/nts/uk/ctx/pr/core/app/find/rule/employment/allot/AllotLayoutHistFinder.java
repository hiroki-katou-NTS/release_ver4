package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutHistoryDto;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AllotLayoutHistFinder {
	@Inject
	private LayoutHistRepository LayoutHistRepo;
	@Inject
	private LayoutMasterRepository layoutMasterRepo;

	public List<LayoutHistoryDto> getSel1LayoutHistory(int baseYM) {
		String companyCode = AppContexts.user().companyCode();
		
		return this.LayoutHistRepo.getBy_SEL_1(companyCode, baseYM).stream()
				.map(histlayout -> LayoutHistoryDto.fromDomain(histlayout)).collect(Collectors.toList());
	}

	public String getAllotLayoutName(String stmtCode) {
		String companyCode = AppContexts.user().companyCode();
		Optional<LayoutMaster> layoutName = this.layoutMasterRepo.getBy_SEL_7(companyCode, stmtCode);
		String result = "";
		if (layoutName.isPresent()) {
			result = layoutName.get().getStmtName().v();
		}
		return result;
	}
}