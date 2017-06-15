package nts.uk.ctx.sys.portal.app.find.toppagesetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.dom.toppage.TopPage;
import nts.uk.ctx.sys.portal.dom.toppage.TopPageRepository;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageSelfSetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class TopPageSelfSettingFinder {
	@Inject
	private TopPageRepository topPageRepository;
	@Inject
	private StandardMenuRepository standardMenuRepository;
	@Inject
	private TopPageSelfSetRepository repository;
	
	/**
	 * Find all top page and stand menu
	 * @return
	 */
	public List<SelectMyPageDto> findSelectMyPage() {
		List<SelectMyPageDto> result = new ArrayList<>();
		
		String companyId = AppContexts.user().companyId();
		
		// get top page
		List<TopPage> topPages = topPageRepository.findAll(companyId);
		if (!topPages.isEmpty()) {
			topPages.stream()
					.forEach(x -> {
						result.add(new SelectMyPageDto(x.getTopPageCode().v(), x.getTopPageName().v(), 0));
					});
		}
		
		// get standard menu
		List<StandardMenu> standardMenus = standardMenuRepository.findAllWithAfterLoginDisplayIndicatorIsTrue(companyId);
		if (!standardMenus.isEmpty()) {
			standardMenus.stream()
					.forEach(x -> {
						result.add(new SelectMyPageDto(x.getCode().v(), x.getDisplayName().v(), 1));
					});
		}
		
		return result;
	}
	/**
	 * Find top page self set
	 * @return
	 */
	public TopPageSelfSettingDto getTopPageSelfSet(){
		//lay employeeId
		String employeeId = AppContexts.user().employeeCode();
		Optional<TopPageSelfSettingDto> lst = this.repository.getTopPageSelfSet(employeeId)
				.map(c->TopPageSelfSettingDto.fromDomain(c));
		if(!lst.isPresent()){
			return null;
		}else{
			return lst.get();
		}
		
	}
}
