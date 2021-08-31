package nts.uk.cnv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindConversionTableResult {

	String conversionType;

	/** NONE **/
	String sourceColumn_none;

	/** CODE_TO_CODE **/
	String codeToCodeType;
	String sourceColumn_codeToCode;

	/** CODE_TO_ID **/
	String codeToIdType;
	String sourceColumn_codeToId;

	/** FIXID_VALUE **/
	String fixedValue;
	boolean fixedValueIsParam;

	/** FIXID_VALUE_WITH_CONDITION **/
	String sourceColumn_fixedCalueWithCond;
	String operator;
	String conditionValue;
	String fixedValueWithCond;
	boolean fixedValueWithCondIsParam;

	/** PARENT **/
	String parentTable;
	String sourceColumn_parent;
	String joinPKs;

	/** STRING_CONCAT **/
	String sourceColumn1;
	String sourceColumn2;
	String delimiter;

	/** TIME_WITH_DAY_ATTR **/
	String sourceColumn_timeWithDayAttr_dayAttr;
	String sourceColumn_timeWithDayAttr_time;

	/** DATETIME_MERGE **/
	String sourceColumn_yyyymmdd;
	String sourceColumn_yyyy;
	String sourceColumn_mm;
	String sourceColumn_yyyymm;
	String sourceColumn_mmdd;
	String sourceColumn_dd;
	String sourceColumn_hh;
	String sourceColumn_mi;
	String sourceColumn_hhmi;
	String sourceColumn_ss;
	String sourceColumn_minutes;
	String sourceColumn_yyyymmddhhmi;
	String sourceColumn_yyyymmddhhmiss;

	/** GUID **/

	/** PASSWORD **/
	String sourceColumn_password;

	/** FILE_ID **/
	String sourceColumn_fileId;

	/** SOURCE_JOIN **/
	String sourceTable;
	String sourceColumn_sourceJoin;
	String joinSourcePKs;
}
