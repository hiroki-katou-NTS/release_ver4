/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.insurance;

public class ReportConstant {
    
    /** The Constant REPORT_FILE_NAME. */
    public static final String REPORT_FILE_NAME = "給与社会保険料チェックリスト.pdf";
    
    /** The Constant NUMBER_ZERO. */
    public static final int NUMBER_ZERO = 0;

    /** The Constant NUMBER_ONE. */
    public static final int NUMBER_ONE = 1;

    /** The Constant NUMBER_SECOND. */
    public static final int NUMBER_SECOND = 2;

    /** The Constant INDEX_ROW_CONTENT_AREA. */
    public static final int INDEX_ROW_CONTENT_AREA = 16;

    /** The Constant RADIX. */
    public static final int RADIX = 16;
    
    /** The Constant COLOR_EMPLOYEE_HEX. */
    public static final String COLOR_EMPLOYEE_HEX = "C8F295";

    /** The Constant HEADER. */
    public static final String HEADER = "HEADER";
    
    /** The Constant SHEET_NAME. */
    public static final String SHEET_NAME = "Sheet 1";
    
    /** The Constant RANGE_OFFICE. */
    public static final String RANGE_OFFICE = "RangeOffice";

    /** The Constant RANGE_EMPLOYEE. */
    public static final String RANGE_EMPLOYEE = "RangeEmployee";

    /** The Constant RANGE_FOOTER_EACH_OFFICE. */
    public static final String RANGE_FOOTER_EACH_OFFICE = "RangeFooterEachOffice";
    
    /** The Constant RANGE_DELIVERY_NOTICE_AMOUNT. */
    public static final String RANGE_DELIVERY_NOTICE_AMOUNT = "RangeDeliveryNoticeAmount";

    /** The Constant RANGE_CHILD_RAISING. */
    public static final String RANGE_CHILD_RAISING = "RangeChildRaising";
    
    /** The Constant INDEX_COLUMN_DELIVERY. */
    public static final int INDEX_COLUMN_DELIVERY = 2;

    /** The Constant INDEX_COLUMN_INSURED. */
    public static final int INDEX_COLUMN_INSURED = 7;

    /** The Constant INDEX_COLUMN_CHILD_RAISING. */
    public static final int INDEX_COLUMN_CHILD_RAISING = 12;

    /** The Constant ALPHABET_A. */
    public static final String ALPHABET_A = "A";
    
    /** The Constant ALPHABET_O. */
    public static final String ALPHABET_O = "O";
    
    /** The Constant ALPHABET_Q. */
    public static final String ALPHABET_Q = "Q";
    
    /** The Constant OPERATOR_SUB. */
    public static final String OPERATOR_SUB = "-";

    /** The Constant OFFICE_JP. */
    public static final String OFFICE_JP = "事業所 : ";

    /** The Constant NUMBER_LINE_OF_PAGE. */
    public static final int NUMBER_LINE_OF_PAGE = 61;

    /** The Constant COLUMN_WIDTH. */
    public static final double COLUMN_WIDTH = 11;
    
    /** The Constant COLUMN_WIDTH_OFFICE_CODE. */
    public static final double COLUMN_WIDTH_OFFICE_CODE = 19;
    
    /**
     * Instantiates a new report constant.
     */
    private ReportConstant() {
        throw new IllegalAccessError("Utility class");
    }
}
