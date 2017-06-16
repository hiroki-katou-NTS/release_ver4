/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave2;

/**
 * The Interface CompensatoryOccurrenceSettingGetMemento.
 */
public interface CompensatoryOccurrenceSettingGetMemento {
	 
 	/**
 	 * Gets the occurrence type.
 	 *
 	 * @return the occurrence type
 	 */
 	CompensatoryOccurrenceDivision getOccurrenceType(); 
     
     /**
      * Gets the transfer setting.
      *
      * @return the transfer setting
      */
     TransferSetting getTransferSetting();
}
