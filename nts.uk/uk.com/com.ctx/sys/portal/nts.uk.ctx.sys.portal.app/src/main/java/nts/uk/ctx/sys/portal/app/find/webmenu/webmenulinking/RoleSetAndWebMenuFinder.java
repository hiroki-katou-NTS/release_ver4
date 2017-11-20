package nts.uk.ctx.sys.portal.app.find.webmenu.webmenulinking;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.sys.portal.app.find.standardmenu.StandardMenuDto;
import nts.uk.ctx.sys.portal.dom.enums.MenuAtr;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.WebMenuSetting;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.MenuBar;
import nts.uk.ctx.sys.portal.dom.webmenu.SelectedAtr;
import nts.uk.ctx.sys.portal.dom.webmenu.TitleBar;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTying;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTyingRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetAndWebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetAndWebMenuRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Stateless
public class RoleSetAndWebMenuFinder {

	@Inject
	private RoleSetAndWebMenuRepository roleSetAndWebMenuRepository;

	/**
	 * Find a RoleSetAndWebMenu by webMenuCd and roleSetCd
	 * @param webMenuCode
	 * @return
	 */
	public RoleSetAndWebMenuDto find(String webMenuCd, String roleSetCd) {
		String companyId = AppContexts.user().companyId();
		Optional<RoleSetAndWebMenu> webMenuOpt = roleSetAndWebMenuRepository.findByKey(companyId, webMenuCd, roleSetCd);
		if (!webMenuOpt.isPresent()) {
			return null;
		}
		
		return RoleSetAndWebMenuDto.build(webMenuOpt.get());
	}
	
	/**
	 * Find all RoleSetAndWebMenu by company id and role set cd
	 * @param roleSetCd
	 * @return
	 */
	public List<RoleSetAndWebMenuDto> findAllByRoleSet(String roleSetCd) {

		String companyId = AppContexts.user().companyId();
		return roleSetAndWebMenuRepository.findByRoleSetCd(companyId, roleSetCd).stream().map(item -> {
			return RoleSetAndWebMenuDto.build(item);
		}).collect(Collectors.toList());
	}

	/**
	 * Find all RoleSetAndWebMenu by company id
	 * @return
	 */
	public List<RoleSetAndWebMenuDto> findAll() {

		String companyId = AppContexts.user().companyId();
		return roleSetAndWebMenuRepository.findByCompanyId(companyId).stream().map(item -> {
			return RoleSetAndWebMenuDto.build(item);
		}).collect(Collectors.toList());
	}

}
