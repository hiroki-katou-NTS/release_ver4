/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySetting;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItem;

/**
 * The Class SalaryCategorySettingDto.
 */
@Setter
public class SalaryCategorySettingDto {

	/** The category. */
	private SalaryCategory category;

	/** The output items. */
	private List<SalaryOutputItemDto> outputItems;

	/**
	 * To domain.
	 *
	 * @return the salary category setting
	 */
	public SalaryCategorySetting toDomain() {
		return new SalaryCategorySetting(new SalaryCategorySettingGetMementoImpl(this));
	}

	/**
	 * The Class SalaryCategorySettingGetMementoImpl.
	 */
	public class SalaryCategorySettingGetMementoImpl implements SalaryCategorySettingGetMemento {

		/** The dto. */
		private SalaryCategorySettingDto dto;

		/**
		 * Instantiates a new salary category setting get memento impl.
		 *
		 * @param dto the dto
		 */
		public SalaryCategorySettingGetMementoImpl(SalaryCategorySettingDto dto) {
			super();
			this.dto = dto;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingGetMemento#getSalaryCategory()
		 */
		@Override
		public SalaryCategory getSalaryCategory() {
			return dto.category;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryCategorySettingGetMemento#getSalaryOutputItems()
		 */
		@Override
		public List<SalaryOutputItem> getSalaryOutputItems() {
			return dto.outputItems.stream().map(SalaryOutputItemDto::toDomain).collect(Collectors.toList());
		}

	}
}
