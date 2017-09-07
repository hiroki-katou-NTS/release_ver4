/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.login;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.company.pub.company.CompanyExport;
import nts.uk.ctx.bs.company.pub.company.ICompanyPub;
import nts.uk.ctx.sys.gateway.dom.login.adapter.CompanyInformationAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.CompanyInformationImport;

/**
 * The Class CompanyInformationAdapterImpl.
 */
@Stateless
public class CompanyInformationAdapterImpl implements CompanyInformationAdapter {

	@Inject
	ICompanyPub iCompanyPub;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.CompanyInformationAdapter#
	 * findByContractCode(java.lang.String)
	 */
	@Override
	public List<CompanyInformationImport> findAll() {
		List<CompanyExport> lstReciveCompany = iCompanyPub.getAllCompany();

		List<CompanyInformationImport> lstCompany = new ArrayList<>();
		lstReciveCompany.stream().map(c -> {
			return lstCompany.add(new CompanyInformationImport(c.getCompanyId(), c.getCompanyCode(), c.getCompanyName()));
		}).collect(Collectors.toList());
		return lstCompany;
	}

}
