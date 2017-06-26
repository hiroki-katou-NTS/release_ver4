/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.find.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingName;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento;

/**
 * The Class OutputSettingDto.
 */
@Builder
public class OutputSettingDto implements WLOutputSettingSetMemento{

	/** The code. */
	public String code;

	/** The name. */
	public String name;

	/** The is once sheet per person. */
	public Boolean isOnceSheetPerPerson;

	/** The category settings. */
	public List<CategorySettingDto> categorySettings;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento
	 * #setCode(nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode)
	 */
	@Override
	public void setCode(WLOutputSettingCode code) {
		this.code = code.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento
	 * #setName(nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingName)
	 */
	@Override
	public void setName(WLOutputSettingName name) {
		this.name = name.v();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento
	 * #setCategorySettings(java.util.List)
	 */
	@Override
	public void setCategorySettings(List<WLCategorySetting> categorySettings) {
		if (CollectionUtil.isEmpty(categorySettings)) {
			return;
		}
		this.categorySettings = categorySettings.stream().map(setting -> {
			CategorySettingDto dto = CategorySettingDto.builder().build();
			setting.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento
	 * #setOnceSheetPerPerson(java.lang.Boolean)
	 */
	@Override
	public void setOnceSheetPerPerson(Boolean onceSheetPerPerson) {
		this.isOnceSheetPerPerson = onceSheetPerPerson;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingSetMemento
	 * #setCompanyCode(nts.uk.ctx.pr.report.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}
}
