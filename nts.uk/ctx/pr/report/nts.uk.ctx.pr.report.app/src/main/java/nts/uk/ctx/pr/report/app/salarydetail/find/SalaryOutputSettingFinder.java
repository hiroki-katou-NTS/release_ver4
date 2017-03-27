/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.find;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.salarydetail.find.dto.SalaryOutputSettingDto;
import nts.uk.ctx.pr.report.app.salarydetail.find.dto.SalaryOutputSettingHeaderDto;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryOutputSettingFinder.
 */
@Stateless
public class SalaryOutputSettingFinder {

	/** The repository. */
	@Inject
	private SalaryOutputSettingRepository repository;

	/** The header repository. */
	@Inject
	private SalaryOutputSettingHeaderRepository headerRepository;

	/**
	 * Find.
	 *
	 * @param code the code
	 * @return the salary output setting dto
	 */
	public SalaryOutputSettingDto find(String code) {
		SalaryOutputSetting outputSetting = this.repository.findByCode(AppContexts.user().companyCode(), code);
		SalaryOutputSettingDto dto = SalaryOutputSettingDto.builder().build();
		outputSetting.saveToMemento(dto);
		return dto;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<SalaryOutputSettingHeaderDto> findAll() {
		return headerRepository.findAll(AppContexts.user().companyCode());
	}
}
