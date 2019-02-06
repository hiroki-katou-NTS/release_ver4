/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.export.monthlyschedule;

/**
 * The Class MonthlyReportConstant.
 */
public class MonthlyReportConstant {
	
	/** The Constant TEMPLATE_DATE. */
	public static final String TEMPLATE_DATE= "report/KWR006_Date.xlsx";

	/** The Constant TEMPLATE_EMPLOYEE. */
	public static final String TEMPLATE_EMPLOYEE = "report/KWR006_Employee.xlsx";
	
	/** The Constant CHUNK_SIZE. */
	public static final int CHUNK_SIZE = 16;

	/** The Constant DATA_COLUMN_INDEX. */
	public static final int[] DATA_COLUMN_INDEX = {3, 9, 11, 15, 17, 39};

	/** The Constant FONT_FAMILY. */
	public static final String FONT_FAMILY = "ＭＳ ゴシック";

	/** The Constant FONT_SIZE. */
	public static final double FONT_SIZE = 6.5;
	
	/** The Constant DATA_PREFIX. */
	public static final String DATA_PREFIX = "DATA_";
	
	/** The Constant DATA_PREFIX_NO_WORKPLACE. */
	public static final String DATA_PREFIX_NO_WORKPLACE = "NOWPK_";
	
	/** The Constant CHUNK_SIZE_ERROR. */
	public static final int CHUNK_SIZE_ERROR = 5;
	
	/** The Constant REMARK_CELL_WIDTH. */
	public static final int REMARK_CELL_WIDTH = 4;
}
