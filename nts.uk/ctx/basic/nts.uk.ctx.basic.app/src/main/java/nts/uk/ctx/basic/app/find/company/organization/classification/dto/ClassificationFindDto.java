/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.app.find.company.organization.classification.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.basic.dom.company.organization.CompanyId;
import nts.uk.ctx.basic.dom.company.organization.classification.ClassificationCode;
import nts.uk.ctx.basic.dom.company.organization.classification.ClassificationName;
import nts.uk.ctx.basic.dom.company.organization.classification.ClassificationSetMemento;

/**
 * The Class ClassificationFindDto.
 */
@Getter
@Setter
public class ClassificationFindDto implements ClassificationSetMemento{
	
	/** The code. */
	private String code;
	
	/** The name. */
	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.category.
	 * ManagementCategorySetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		// No thing code
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.category.
	 * ManagementCategorySetMemento#setManagementCategoryCode(nts.uk.ctx.basic.
	 * dom.company.organization.category.ManagementCategoryCode)
	 */
	@Override
	public void setManagementCategoryCode(ClassificationCode managementCategoryCode) {
		this.code = managementCategoryCode.v();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.category.
	 * ManagementCategorySetMemento#setManagementCategoryName(nts.uk.ctx.basic.
	 * dom.company.organization.category.ManagementCategoryName)
	 */
	@Override
	public void setManagementCategoryName(ClassificationName managementCategoryName) {
		this.name = managementCategoryName.v();
		
	}

}
