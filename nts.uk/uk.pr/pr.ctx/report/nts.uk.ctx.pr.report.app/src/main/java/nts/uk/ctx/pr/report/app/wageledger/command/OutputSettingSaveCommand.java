/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.app.wageledger.command.dto.CategorySettingDto;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingName;

/**
 * The Class OutputSettingSaveCommand.
 */

/**
 * Sets the creates the mode.
 *
 * @param isCreateMode the new creates the mode
 */
@Setter

/**
 * Checks if is creates the mode.
 *
 * @return true, if is creates the mode
 */
@Getter
public class OutputSettingSaveCommand {
	
	/** The code. */
	private String code;
	
	/** The name. */
	private String name;
	
	/** The once sheet per person. */
	private boolean onceSheetPerPerson;
	
	/** The category settings. */
	private List<CategorySettingDto> categorySettings;
	
	/** The is create mode. */
	private boolean isCreateMode;
	
	/**
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the WL output setting
	 */
	public WLOutputSetting toDomain(String companyCode) {
		return new WLOutputSetting(new OutputSettingGetMementoImpl(companyCode, this));
	}
	
	/**
	 * The Class OutputSettingGetMementoImpl.
	 */
	public class OutputSettingGetMementoImpl implements WLOutputSettingGetMemento {

		/** The company code. */
		private String companyCode;
		
		/** The command. */
		private OutputSettingSaveCommand command;
		
		/**
		 * Instantiates a new output setting get memento impl.
		 *
		 * @param companyCode the company code
		 * @param command the command
		 */
		public OutputSettingGetMementoImpl(String companyCode, OutputSettingSaveCommand command) {
			super();
			this.companyCode = companyCode;
			this.command = command;
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
		 * #getOnceSheetPerPerson()
		 */
		@Override
		public Boolean getOnceSheetPerPerson() {
			return this.command.isOnceSheetPerPerson();
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
		 * #getName()
		 */
		@Override
		public WLOutputSettingName getName() {
			return new WLOutputSettingName(this.command.getName());
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
		 * #getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return this.companyCode;
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
		 * #getCode()
		 */
		@Override
		public WLOutputSettingCode getCode() {
			return new WLOutputSettingCode(this.command.getCode());
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingGetMemento
		 * #getCategorySettings()
		 */
		@Override
		public List<WLCategorySetting> getCategorySettings() {
			return this.command.getCategorySettings().stream()
					.map(setting -> setting.toDomain()).collect(Collectors.toList());
		}
		
	}

}
