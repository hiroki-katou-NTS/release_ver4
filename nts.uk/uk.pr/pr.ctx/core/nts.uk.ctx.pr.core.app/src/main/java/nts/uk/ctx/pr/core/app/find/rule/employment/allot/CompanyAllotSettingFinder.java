package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
//import javax.enterprise.inject.Default;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutHeadDto;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
//import nts.uk.ctx.pr.core.dom.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSettingRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class CompanyAllotSettingFinder {
	@Inject
	private CompanyAllotSettingRepository companyAllotRepo;
	@Inject 
	private LayoutMasterRepository layoutMasterRepo;
	
	/**
	 * finder all allot by company code and layout code
	 * 
	 * @param companyCode
	 * @return
	 */
	public List<CompanyAllotSettingDto> getAllCompanyAllotSetting() {
		String companyCode = AppContexts.user().companyCode();
		return this.companyAllotRepo.findAll(companyCode).stream()
				.map(companyallot -> CompanyAllotSettingDto.fromDomain(companyallot))
				.collect(Collectors.toList());
	}
	/**
	 * 
	 * @param stmtCode
	 * @return
	 */
	
	public String getAllotLayoutName(String stmtCode) {
		String companyCode = AppContexts.user().companyCode();
		Optional<LayoutMaster> layoutName = this.layoutMasterRepo.getBy_SEL_7(companyCode, stmtCode);
		String result = "";
		if (layoutName.isPresent()) {
			result = layoutName.get().getStmtName().v();
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 * Get Item with Max END Date
	 */
	public Optional<CompanyAllotSettingDto> getMaxStartYM(){
		String companyCode = AppContexts.user().companyCode();
		return this.companyAllotRepo.maxStart(companyCode)
				.map(maxAllot -> CompanyAllotSettingDto.fromDomain(maxAllot));
	}
	
}
