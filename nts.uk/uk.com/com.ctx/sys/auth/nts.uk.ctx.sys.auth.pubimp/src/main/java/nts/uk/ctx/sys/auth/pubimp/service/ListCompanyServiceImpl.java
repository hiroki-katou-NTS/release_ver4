/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.pubimp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.dom.employee.dto.EmployeeImport;
import nts.uk.ctx.sys.auth.dom.grant.RoleIndividualGrant;
import nts.uk.ctx.sys.auth.dom.grant.RoleIndividualGrantRepository;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleRepository;
import nts.uk.ctx.sys.auth.pub.service.ListCompanyService;

/**
 * The Class ListCompanyServiceImpl.
 */
@Stateless
public class ListCompanyServiceImpl implements ListCompanyService {

	/** The role individual grant repository. */
	@Inject
	private RoleIndividualGrantRepository roleIndividualGrantRepository;
	
	/** The role repository. */
	@Inject 
	private RoleRepository roleRepository; 
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.auth.pub.service.ListCompanyService#getListCompanyId(java.lang.String)
	 */
	@Override
	public List<String> getListCompanyId(String userId,String associatedPersonId) {
		List<String> lstCompanyId = new ArrayList<String>();
		// get roleIndividualGrant
		RoleIndividualGrant individualGrant = roleIndividualGrantRepository.findByUser(userId, GeneralDate.today()).get();
		// get roles by roleId
		List<Role> lstRole = roleRepository.findById(individualGrant.getRoleId());
		// TODO get list employee imported by User associated Id #No.124
		List<EmployeeImport> lstEm = Arrays.asList();

		// merge duplicate companyId from lstRole and lstEm
		for (Role item : lstRole) {
			if (item.getCompanyId() != null) {
				lstCompanyId.add(item.getCompanyId());
			}
		}

		for (EmployeeImport em : lstEm) {
			boolean haveComId = lstCompanyId.stream().anyMatch(item -> {
				return em.getCompanyId().equals(item);
			});
			if (!haveComId) {
				lstCompanyId.add(em.getCompanyId());
			}
		}
		return lstCompanyId;
	}

}
