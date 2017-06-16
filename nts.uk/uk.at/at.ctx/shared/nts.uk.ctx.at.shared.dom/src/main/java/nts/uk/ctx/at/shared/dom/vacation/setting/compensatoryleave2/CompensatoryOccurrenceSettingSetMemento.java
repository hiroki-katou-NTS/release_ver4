/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave2;

/**
 * The Interface CompensatoryOccurrenceSettingSetMemento.
 */
public interface CompensatoryOccurrenceSettingSetMemento {
	
	/**
	 * Sets the occurrence type.
	 *
	 * @param occurrenceType the new occurrence type
	 */
	void setOccurrenceType(CompensatoryOccurrenceDivision occurrenceType);

	/**
	 * Sets the transfer setting.
	 *
	 * @param transferSetting the new transfer setting
	 */
	void setTransferSetting(TransferSetting transferSetting);
}
