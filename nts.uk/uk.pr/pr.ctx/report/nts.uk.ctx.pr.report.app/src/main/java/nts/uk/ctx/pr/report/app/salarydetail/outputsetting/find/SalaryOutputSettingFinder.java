/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto.SalaryOutputSettingDto;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto.SalaryOutputSettingHeaderDto;
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
		return this.repository.findAll(AppContexts.user().companyCode()).stream().map(setting -> {
			return SalaryOutputSettingHeaderDto.builder()
					.code(setting.getCode().v())
					.name(setting.getName().v())
					.build();
		}).collect(Collectors.toList());
	}
}
