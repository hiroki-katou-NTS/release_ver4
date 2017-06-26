/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingName;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingSetMemento;

/**
 * The Class SalaryOutputSettingDto.
 */
@Builder
public class SalaryOutputSettingDto implements SalaryOutputSettingSetMemento {

	/** The code. */
	public String code;

	/** The name. */
	public String name;

	/** The category settings. */
	public List<SalaryCategorySettingDto> categorySettings;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCompanyCode(nts.uk.ctx.pr.report.dom.
	 * company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCode(nts.uk.ctx.pr.report.dom.
	 * salarydetail.outputsetting.SalaryOutputSettingCode)
	 */
	@Override
	public void setCode(SalaryOutputSettingCode salaryOutputSettingCode) {
		this.code = salaryOutputSettingCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setName(nts.uk.ctx.pr.report.dom.
	 * salarydetail.outputsetting.SalaryOutputSettingName)
	 */
	@Override
	public void setName(SalaryOutputSettingName salaryOutputSettingName) {
		this.name = salaryOutputSettingName.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputSettingSetMemento#setCategorySettings(java.util.List)
	 */
	@Override
	public void setCategorySettings(List<SalaryCategorySetting> listSalaryCategorySetting) {
		if (CollectionUtil.isEmpty(listSalaryCategorySetting)) {
			return;
		}
		this.categorySettings = listSalaryCategorySetting.stream().map(setting -> {
			SalaryCategorySettingDto dto = SalaryCategorySettingDto.builder().build();
			setting.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

}
