/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.dto.SalaryCategorySettingDto;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingName;

/**
 * The Class SalaryOutputSettingSaveCommand.
 */
@Setter
@Getter
public class SalaryOutputSettingSaveCommand {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The category settings. */
	private List<SalaryCategorySettingDto> categorySettings;

	/** The is create mode. */
	private boolean isCreateMode;

	/**
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the salary output setting
	 */
	public SalaryOutputSetting toDomain(String companyCode) {
		return new SalaryOutputSetting(new SalaryOutputSettingGetMementoImpl(companyCode, this));
	}

	/**
	 * The Class SalaryOutputSettingGetMementoImpl.
	 */
	public class SalaryOutputSettingGetMementoImpl implements SalaryOutputSettingGetMemento {

		/** The company code. */
		private String companyCode;

		/** The command. */
		private SalaryOutputSettingSaveCommand command;

		/**
		 * Instantiates a new salary output setting get memento impl.
		 *
		 * @param companyCode the company code
		 * @param command the command
		 */
		public SalaryOutputSettingGetMementoImpl(String companyCode, SalaryOutputSettingSaveCommand command) {
			super();
			this.companyCode = companyCode;
			this.command = command;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputSettingGetMemento#getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return this.companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputSettingGetMemento#getCode()
		 */
		@Override
		public SalaryOutputSettingCode getCode() {
			return new SalaryOutputSettingCode(this.command.code);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputSettingGetMemento#getName()
		 */
		@Override
		public SalaryOutputSettingName getName() {
			return new SalaryOutputSettingName(this.command.name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
		 * SalaryOutputSettingGetMemento#getCategorySettings()
		 */
		@Override
		public List<SalaryCategorySetting> getCategorySettings() {
			return this.command.categorySettings.stream().map(setting -> setting.toDomain())
					.collect(Collectors.toList());
		}

	}
}
