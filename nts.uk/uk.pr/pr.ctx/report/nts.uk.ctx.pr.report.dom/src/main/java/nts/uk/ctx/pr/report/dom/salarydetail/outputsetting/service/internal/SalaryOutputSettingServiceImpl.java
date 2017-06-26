/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.service.internal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingRepository;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.service.SalaryOutputSettingService;

/**
 * The Class SalaryOutputSettingServiceImpl.
 */
@Stateless
public class SalaryOutputSettingServiceImpl implements SalaryOutputSettingService {

	/** The repository. */
	@Inject
	private SalaryOutputSettingRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.service.
	 * SalaryOutputSettingService#validateRequiredItem(nts.uk.ctx.pr.report.dom.
	 * salarydetail.outputsetting.SalaryOutputSetting)
	 */
	@Override
	public void validateRequiredItem(SalaryOutputSetting salaryOutputSetting) {
		if (salaryOutputSetting.getCode() == null || salaryOutputSetting.getName() == null
				|| StringUtil.isNullOrEmpty(salaryOutputSetting.getCode().v(), true)
				|| StringUtil.isNullOrEmpty(salaryOutputSetting.getName().v(), true)
				|| CollectionUtil.isEmpty(salaryOutputSetting.getCategorySettings())) {
			throw new BusinessException("ER027");
		}
	}

	@Override
	public void checkDuplicateCode(String companyCode, String salaryOutputSettingCode) {
		if (repository.isExist(companyCode, salaryOutputSettingCode)) {
			throw new BusinessException("ER011");
		};
	}

}
