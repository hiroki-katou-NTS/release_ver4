package nts.uk.file.at.infra.schedule.daily;

/**
 * The Class WorkScheOutputConstants.
 * @author HoangNDH
 */
public class WorkScheOutputConstants {
	
	// Remark text
	public static final String ERAL = "ER/AL";
	
	/** The Constant DATE. */
	public static final String DATE = "日付";
	
	/** The Constant DAYMONTH. */
	public static final String DAYMONTH = "月日";
	
	/** The Constant YEARMONTH. */
	public static final String YEARMONTH = "年月";
	
	/** The Constant DAY. */
	public static final String DAY = "曜";
	
	/** The Constant REMARK. */
	public static final String REMARK = "備考";
	
	/** The Constant PERSONAL_NAME. */
	public static final String PERSONAL_NAME = "個人名";
	
	/** The Constant PERIOD. */
	public static final String PERIOD = "[期間]";
	
	// File/Sheet name
	public static final String SHEET_FILE_NAME = "日別勤務表";
	public static final String SHEET_FILE_NAME_MONTHLY = "月別勤務集計表";
	
	// Calculation text
	public static final String TOTAL_EMPLOYEE = "個人計";
	
	/** The Constant TOTAL_DAY. */
	public static final String TOTAL_DAY = "日数計";
	
	// Record part
	public static final String WORKPLACE = "【職場】";
	
	/** The Constant EMPLOYEE. */
	public static final String EMPLOYEE = "【個人】";
	
	/** The Constant EMPLOYMENT. */
	public static final String EMPLOYMENT = "【雇用】";
	
	/** The Constant POSITION. */
	public static final String POSITION = "【職位】";
	
	/** The Constant DATE_BRACKET. */
	public static final String DATE_BRACKET = "[日付]";
	
	// ER / AL
	public static final String ER = "ER";
	public static final String AL = "AL";
	public static final String ER_AL = "ER/AL";
	
	
	// Personal total
	public static final String PERSONAL_TOTAL = "個人計";
	
	/** The Constant WORKPLACE_TOTAL. */
	public static final String WORKPLACE_TOTAL = "職場計";
	
	/** The Constant GROSS_TOTAL. */
	public static final String GROSS_TOTAL = "総合計";
	
	/** The Constant WORKPLACE_HIERARCHY_TOTAL. */
	public static final String WORKPLACE_HIERARCHY_TOTAL = "職場累計";
	
	// DAY_COUNT
	public static final String DAY_COUNT = "日数計";
	
	/** The Constant PREDETERMINED. */
	public static final String PREDETERMINED = "所定日数";
	
	/** The Constant HOLIDAY. */
	public static final String HOLIDAY = "休日日数";
	
	/** The Constant OFF. */
	public static final String OFF = "休出日数";
	
	/** The Constant YEAR_OFF. */
	public static final String YEAR_OFF = "年休使用数";
	
	/** The Constant HEAVY. */
	public static final String HEAVY = "所定日数";
	
	/** The Constant SPECIAL. */
	public static final String SPECIAL = "特休日数";
	
	/** The Constant ABSENCE. */
	public static final String ABSENCE = "欠勤日数";
	
	/** The Constant LATE_COME. */
	public static final String LATE_COME = "遅刻回数";
	
	/** The Constant EARLY_LEAVE. */
	public static final String EARLY_LEAVE = "早退回数";
	
	/** The Constant DAY_COUNT_TITLES. */
	public static final String[] DAY_COUNT_TITLES =
		{
			PREDETERMINED, HOLIDAY, OFF, YEAR_OFF,
			HEAVY, SPECIAL, ABSENCE, LATE_COME, EARLY_LEAVE
		};
	
	// Range name, used to copy range from template to real sheet
	public static final String RANGE_EMPLOYEE_ROW = "EmployeeRow";
	
	/** The Constant RANGE_WORKPLACE_ROW. */
	public static final String RANGE_WORKPLACE_ROW = "WorkplaceRow";
	
	/** The Constant RANGE_WHITE_ROW. */
	public static final String RANGE_WHITE_ROW = "White"; // Append number of row after this constants, eg. "White3" for 3 rows
	
	/** The Constant RANGE_LIGHTBLUE_ROW. */
	public static final String RANGE_LIGHTBLUE_ROW = "LBlue"; // Append number of row after this constants, eg. "LBlue3" for 3 rows
	
	/** The Constant RANGE_DAYCOUNT_ROW. */
	public static final String RANGE_DAYCOUNT_ROW = "DayCount";
	
	/** The Constant RANGE_TOTAL_ROW. */
	public static final String RANGE_TOTAL_ROW = "Total"; // Append number of row after this constants, eg. "Total3" for 3 rows
	
	/** The Constant RANGE_GROSS_ROW. */
	public static final String RANGE_GROSS_ROW = "Gross"; // Append number of row after this constants, eg. "Gross3" for 3 rows
	
	/** The Constant RANGE_DATE_ROW. */
	public static final String RANGE_DATE_ROW = "DateRow";
	
	/** The Constant RANGE_DAILY_WORKPLACE_ROW. */
	public static final String RANGE_DAILY_WORKPLACE_ROW = "DailyWorkplaceRow";
}
