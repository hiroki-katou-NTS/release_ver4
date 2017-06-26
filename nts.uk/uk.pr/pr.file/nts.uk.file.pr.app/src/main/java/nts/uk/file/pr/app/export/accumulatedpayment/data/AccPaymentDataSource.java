/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class AccPaymentDataSource.
 */
@Builder
@Getter
@Setter
public class AccPaymentDataSource {
	
	/** The header data. */
	private AccPaymentHeaderData headerData;
	
	/** The accumulated payment data source. */
	private List<AccPaymentItemData> accPaymentItemData;
}
