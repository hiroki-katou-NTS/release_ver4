/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger;

import java.util.List;

import nts.arc.time.GeneralDate;

/**
 * The Class WageLedgerReportQuery.
 */
public class WageLedgerReportQuery {
	
	/** The target year. */
	public int targetYear;
	
	/** The is aggreate preliminary month. */
	public Boolean isAggreatePreliminaryMonth;
	
	/** The layout type. */
	public LayoutType layoutType;
	
	/** The is page break indicator. */
	public Boolean isPageBreakIndicator;
	
	/** The output type. */
	public OutputType outputType;
	
	/** The output setting code. */
	public String outputSettingCode;
	
	/** The employee ids. */
	public List<String> employeeIds;
	
	/** The base date. */
	public GeneralDate baseDate;
	
	/**
	 * The Enum LayoutType.
	 */
	public enum LayoutType {
		
		/** The One page. */
		OnePage,
		
		/** The New layout. */
		NewLayout
	}
	
	/**
	 * The Enum OutputType.
	 */
	public enum OutputType {
		
		/** The Detail item. */
		MasterItems,
		
		/** The Output setting item. */
		OutputSettingItems
	}
}
