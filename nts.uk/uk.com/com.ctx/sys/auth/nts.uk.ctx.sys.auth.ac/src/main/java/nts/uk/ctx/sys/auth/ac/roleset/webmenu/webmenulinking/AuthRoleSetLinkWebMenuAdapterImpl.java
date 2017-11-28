/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.ac.roleset.webmenu.webmenulinking;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.auth.dom.roleset.webmenu.webmenulinking.RoleSetLinkWebMenuAdapter;
import nts.uk.ctx.sys.auth.dom.roleset.webmenu.webmenulinking.RoleSetLinkWebMenuImport;
import nts.uk.ctx.sys.portal.pub.webmenu.webmenulinking.RoleSetLinkWebMenuExport;
import nts.uk.ctx.sys.portal.pub.webmenu.webmenulinking.RoleSetLinkWebMenuPub;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WebMenuAdapterImpl.
 * @author HieuNV
 */
@Stateless
public class AuthRoleSetLinkWebMenuAdapterImpl implements RoleSetLinkWebMenuAdapter {
	
	/** The web menu pub. */
	@Inject
	private RoleSetLinkWebMenuPub roleSetLinkWebMenuPub;

	@Override
	public List<RoleSetLinkWebMenuImport> findAllWebMenuByRoleSetCd(String roleSetCd) {
		return this.roleSetLinkWebMenuPub.findAllWebMenuByRoleSetCd(AppContexts.user().companyId(), roleSetCd).stream()
				.map(item -> new RoleSetLinkWebMenuImport(item.getCompanyId(), item.getWebMenuCd(), item.getRoleSetCd())
				).collect(Collectors.toList());
	}

	@Override
	public void addRoleSetLinkWebMenu(RoleSetLinkWebMenuImport roleSetLinkWebMenu) {
		this.roleSetLinkWebMenuPub.addRoleSetLinkWebMenu(
				roleSetLinkWebMenu.getRoleSetCd()
				, roleSetLinkWebMenu.getWebMenuCd()
				, roleSetLinkWebMenu.getCompanyId());
	}

	@Override
	public void updateRoleSetLinkWebMenu(RoleSetLinkWebMenuImport roleSetLinkWebMenu) {
		this.roleSetLinkWebMenuPub.updateRoleSetLinkWebMenu(
				roleSetLinkWebMenu.getRoleSetCd()
				, roleSetLinkWebMenu.getWebMenuCd()
				, roleSetLinkWebMenu.getCompanyId());
	}

	@Override
	public void deleteAllRoleSetLinkWebMenu(String roleSetCd) {
		this.roleSetLinkWebMenuPub.deleteRoleSetLinkWebMenuByRoleSetCd(roleSetCd);
	}

	@Override
	public void addAllRoleSetLinkWebMenu(List<RoleSetLinkWebMenuImport> listRoleSetLinkWebMenuImport) {
		List<RoleSetLinkWebMenuExport> listRoleSetLinkWebMenuExport = listRoleSetLinkWebMenuImport.stream()
		.map(item -> new RoleSetLinkWebMenuExport(item.getCompanyId()
				, item.getRoleSetCd()
				, item.getWebMenuCd())).collect(Collectors.toList());
		
		this.roleSetLinkWebMenuPub.addAllRoleSetLinkWebMenu(listRoleSetLinkWebMenuExport);
	}
	
}
