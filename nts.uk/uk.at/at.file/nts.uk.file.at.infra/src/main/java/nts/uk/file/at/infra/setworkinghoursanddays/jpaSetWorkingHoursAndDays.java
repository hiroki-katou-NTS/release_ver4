package nts.uk.file.at.infra.setworkinghoursanddays;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.i18n.I18NText;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
<<<<<<< HEAD
import nts.uk.ctx.at.shared.dom.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.WeekStart;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.company.KshmtLegalTimeMCom;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.employee.KshmtLegalTimeMSya;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.employment.KshmtLegalTimeMEmp;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.workingplace.KshmtLegalTimeMWkp;
=======
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekStart;
import nts.uk.ctx.at.shared.infra.entity.workrule.week.KsrmtWeekRuleMng;
>>>>>>> 5c46af2182b... 「週の管理」の対応
import nts.uk.file.at.app.export.setworkinghoursanddays.CompanyColumn;
import nts.uk.file.at.app.export.setworkinghoursanddays.EmployeeColumn;
import nts.uk.file.at.app.export.setworkinghoursanddays.EmploymentColumn;
import nts.uk.file.at.app.export.setworkinghoursanddays.SetWorkingHoursAndDaysExRepository;
import nts.uk.file.at.app.export.setworkinghoursanddays.WorkPlaceColumn;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellStyle;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;

@Stateless
public class jpaSetWorkingHoursAndDays extends JpaRepository implements SetWorkingHoursAndDaysExRepository {
	private static final String GET_EXPORT_MONTH = "SELECT m.MONTH_STR FROM BCMMT_COMPANY m WHERE m.CID = ?cid";
	
	private static final String GET_EXPORT_EXCEL = 
									" SELECT "
											+" KSHST_COM_REG_LABOR_TIME.DAILY_TIME, KSHST_COM_REG_LABOR_TIME.WEEKLY_TIME, "
											+" KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR, "
											+" IIF (KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_COM_REG_M_CAL_SET.INCLUDE_LEGAL_AGGR , NUll) AS INCLUDE_LEGAL_AGGR, "
											+" IIF (KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_COM_REG_M_CAL_SET.INCLUDE_HOLIDAY_AGGR , NUll) AS INCLUDE_HOLIDAY_AGGR, "
											+" KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_OT, "
											+" IIF (KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_COM_REG_M_CAL_SET.INCLUDE_LEGAL_OT , NUll) AS INCLUDE_LEGAL_OT, "
											+" IIF (KRCST_COM_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_COM_REG_M_CAL_SET.INCLUDE_HOLIDAY_OT , NUll) AS INCLUDE_HOLIDAY_OT, "
											+" KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME, "
											+" KRCST_COM_FLEX_M_CAL_SET.AGGR_METHOD, "
											+" IIF (KRCST_COM_FLEX_M_CAL_SET.AGGR_METHOD = 0, KRCST_COM_FLEX_M_CAL_SET.INCLUDE_OT, NULL) AS INCLUDE_OT, "
											+" KRCST_COM_FLEX_M_CAL_SET.INSUFFIC_SET, "
											+" KSHST_COM_TRANS_LAB_TIME.DAILY_TIME AS REG_DAILY_TIME, "
											+" KSHST_COM_TRANS_LAB_TIME.WEEKLY_TIME AS REG_WEEKLY_TIME, "
											+" KRCST_COM_DEFOR_M_CAL_SET.STR_MONTH, "
											+" KRCST_COM_DEFOR_M_CAL_SET.PERIOD, "
											+" KRCST_COM_DEFOR_M_CAL_SET.REPEAT_ATR, "
											+" KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR AS DEFOR_INCLUDE_EXTRA_AGGR, "
											+" IIF(KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1 ,KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULL) AS DEFOR_INCLUDE_LEGAL_AGGR, "
											+" IIF(KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1 ,KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULL) AS DEFOR_INCLUDE_HOLIDAY_AGGR, "
											+" KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT AS DEFOR_INCLUDE_EXTRA_OT, "
											+" IIF(KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1 ,KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_LEGAL_OT, NULL) AS DEFOR_INCLUDE_LEGAL_OT, "
											+" IIF(KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1 ,KRCST_COM_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULL) AS DEFOR_INCLUDE_HOLIDAY_OT "
											+" FROM KRCST_COM_DEFOR_M_CAL_SET "
											+" 		INNER JOIN KRCST_COM_FLEX_M_CAL_SET ON KRCST_COM_DEFOR_M_CAL_SET.CID = KRCST_COM_FLEX_M_CAL_SET.CID "
											+" 		INNER JOIN KRCST_COM_REG_M_CAL_SET ON KRCST_COM_DEFOR_M_CAL_SET.CID = KRCST_COM_REG_M_CAL_SET.CID "
											+" 		INNER JOIN KSHST_COM_TRANS_LAB_TIME ON KRCST_COM_DEFOR_M_CAL_SET.CID = KSHST_COM_TRANS_LAB_TIME.CID "
											+" 		INNER JOIN KSHST_COM_REG_LABOR_TIME ON KRCST_COM_DEFOR_M_CAL_SET.CID = KSHST_COM_REG_LABOR_TIME.CID "
											+" 		INNER JOIN KSHST_FLX_GET_PRWK_TIME ON KRCST_COM_DEFOR_M_CAL_SET.CID = KSHST_FLX_GET_PRWK_TIME.CID "
											+" WHERE KRCST_COM_DEFOR_M_CAL_SET.CID = ? ";
	
	private static final String GET_EMPLOYMENT = "SELECT "
											+" ROW_NUMBER() OVER(PARTITION BY BSYMT_EMPLOYMENT.CODE ORDER BY IIF(BSYMT_EMPLOYMENT.NAME IS NOT NULL,0,1), BSYMT_EMPLOYMENT.CODE ASC) AS ROW_NUMBER_CODE, "
											+" BSYMT_EMPLOYMENT.CODE AS CODE,  "
											+" IIF(BSYMT_EMPLOYMENT.NAME IS NOT NULL, BSYMT_EMPLOYMENT.NAME, 'マスタ未登録') AS NAME, "
											+" KSHST_EMP_REG_LABOR_TIME.DAILY_TIME, "
											+" KSHST_EMP_REG_LABOR_TIME.WEEKLY_TIME, "
											+" KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR AS INCLUDE_EXTRA_AGGR, "
											+" IIF (KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_EMP_REG_M_CAL_SET.INCLUDE_LEGAL_AGGR , NUll) AS INCLUDE_LEGAL_AGGR, "
											+" IIF (KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_EMP_REG_M_CAL_SET.INCLUDE_HOLIDAY_AGGR , NUll) AS INCLUDE_HOLIDAY_AGGR, "
											+" KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_OT, "
											+" IIF (KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_EMP_REG_M_CAL_SET.INCLUDE_LEGAL_OT , NUll) AS INCLUDE_LEGAL_OT, "
											+" IIF (KRCST_EMP_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_EMP_REG_M_CAL_SET.INCLUDE_HOLIDAY_OT , NUll) AS INCLUDE_HOLIDAY_OT, "
											+ "KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME, "
											+" KRCST_EMP_FLEX_M_CAL_SET.AGGR_METHOD, "
											+" IIF( KRCST_EMP_FLEX_M_CAL_SET.AGGR_METHOD = 0, KRCST_EMP_FLEX_M_CAL_SET.INCLUDE_OT , NULL ) AS INCLUDE_OT, "
											+" KRCST_EMP_FLEX_M_CAL_SET.INSUFFIC_SET, "
											+" KSHST_EMP_TRANS_LAB_TIME.DAILY_TIME AS LAR_DAILY_TIME, "
											+" KSHST_EMP_TRANS_LAB_TIME.WEEKLY_TIME AS LAR_WEEKLY_TIME, "
											+" KRCST_EMP_DEFOR_M_CAL_SET.STR_MONTH, "
											+" KRCST_EMP_DEFOR_M_CAL_SET.PERIOD, "
											+" KRCST_EMP_DEFOR_M_CAL_SET.REPEAT_ATR, "
											+" KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR AS DEFOR_INCLUDE_EXTRA_AGGR, "
											+" IIF(KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_LEGAL_AGGR , NUll ) AS DEFOR_INCLUDE_LEGAL_AGGR, "
											+" IIF(KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_AGGR , NUll ) AS DEFOR_INCLUDE_HOLIDAY_AGGR, "
											+" KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT AS DEFOR_INCLUDE_EXTRA_OT, "
											+" IIF(KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_LEGAL_OT , NUll ) AS DEFOR_INCLUDE_LEGAL_OT, "
											+" IIF(KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_EMP_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_OT , NUll ) AS DEFOR_INCLUDE_HOLIDAY_OT "
											+" 	FROM KSHST_EMP_TRANS_LAB_TIME "
											+" 		INNER JOIN KSHST_EMP_REG_LABOR_TIME ON KSHST_EMP_TRANS_LAB_TIME.CID = KSHST_EMP_REG_LABOR_TIME.CID "
											+" 			AND KSHST_EMP_TRANS_LAB_TIME.EMP_CD = KSHST_EMP_REG_LABOR_TIME.EMP_CD "
											+" 		INNER JOIN KRCST_EMP_DEFOR_M_CAL_SET ON KSHST_EMP_TRANS_LAB_TIME.CID = KRCST_EMP_DEFOR_M_CAL_SET.CID "
											+" 			AND KSHST_EMP_TRANS_LAB_TIME.EMP_CD = KRCST_EMP_DEFOR_M_CAL_SET.EMP_CD "
											+" 		INNER JOIN KRCST_EMP_FLEX_M_CAL_SET ON KSHST_EMP_TRANS_LAB_TIME.CID = KRCST_EMP_FLEX_M_CAL_SET.CID "
											+" 			AND KSHST_EMP_TRANS_LAB_TIME.EMP_CD = KRCST_EMP_FLEX_M_CAL_SET.EMP_CD "
											+" 		INNER JOIN KRCST_EMP_REG_M_CAL_SET ON KSHST_EMP_TRANS_LAB_TIME.CID = KRCST_EMP_REG_M_CAL_SET.CID "
											+" 			AND KSHST_EMP_TRANS_LAB_TIME.EMP_CD = KRCST_EMP_REG_M_CAL_SET.EMP_CD "
											+" 		LEFT JOIN BSYMT_EMPLOYMENT ON KSHST_EMP_TRANS_LAB_TIME.CID = BSYMT_EMPLOYMENT.CID  "
											+" 			AND KSHST_EMP_TRANS_LAB_TIME.EMP_CD = BSYMT_EMPLOYMENT.CODE "
											+" 		LEFT JOIN KSHST_FLX_GET_PRWK_TIME ON KSHST_EMP_TRANS_LAB_TIME.CID = KSHST_FLX_GET_PRWK_TIME.CID "
											+" WHERE KSHST_EMP_TRANS_LAB_TIME.CID = ?  "
											+" ORDER BY IIF(BSYMT_EMPLOYMENT.NAME IS NOT NULL,0,1), KSHST_EMP_TRANS_LAB_TIME.EMP_CD";
	
	private static final String GET_WORKPLACE;
	  static {
	      StringBuilder exportSQL = new StringBuilder();
	     exportSQL.append("  SELECT * FROM ( ");
	     exportSQL.append("             SELECT IIF(BSYMT_WKP_INFO.HIERARCHY_CD IS NOT NULL, BSYMT_WKP_INFO.HIERARCHY_CD, '999999999999999999999999999999') AS HIERARCHY_CD,  ");
	     exportSQL.append("             ROW_NUMBER() OVER(PARTITION BY BSYMT_WKP_INFO.WKP_CD ORDER BY IIF(BSYMT_WKP_INFO.HIERARCHY_CD IS NOT NULL,0,1),BSYMT_WKP_INFO.HIERARCHY_CD ASC) AS rk2,  ");
	     exportSQL.append("             KSHST_WKP_TRANS_LAB_TIME.WKP_ID, BSYMT_WKP_INFO.WKP_CD AS WKPCD,   ");
	     exportSQL.append("             BSYMT_WKP_INFO.WKP_NAME AS WKP_NAME,   ");
	     exportSQL.append("             KSHST_WKP_REG_LABOR_TIME .DAILY_TIME,   ");
	     exportSQL.append("             KSHST_WKP_REG_LABOR_TIME.WEEKLY_TIME,   ");
	     exportSQL.append("             KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR,   ");
	     exportSQL.append("             IIF (KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR != 0, KRCST_WKP_REG_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULL) AS INCLUDE_LEGAL_AGGR,   ");
	     exportSQL.append("             IIF (KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR != 0, KRCST_WKP_REG_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULL) AS INCLUDE_HOLIDAY_AGGR,   ");
	     exportSQL.append("             KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_OT,   ");
	     exportSQL.append("             IIF (KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_OT != 0, KRCST_WKP_REG_M_CAL_SET.INCLUDE_LEGAL_OT, NULL) AS INCLUDE_LEGAL_OT,   ");
	     exportSQL.append("             IIF (KRCST_WKP_REG_M_CAL_SET.INCLUDE_EXTRA_OT != 0, KRCST_WKP_REG_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULL) AS INCLUDE_HOLIDAY_OT,   ");
	     exportSQL.append("             KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.AGGR_METHOD,   ");
	     exportSQL.append("             IIF(KRCST_WKP_FLEX_M_CAL_SET.AGGR_METHOD = 0, KRCST_WKP_FLEX_M_CAL_SET.INCLUDE_OT, NULL) AS INCLUDE_OT,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.INSUFFIC_SET,   ");
	     exportSQL.append("             KSHST_WKP_TRANS_LAB_TIME.DAILY_TIME AS LAB_DAILY_TIME,   ");
	     exportSQL.append("             KSHST_WKP_TRANS_LAB_TIME.WEEKLY_TIME AS LAB_WEEKLY_TIME,   ");
	     exportSQL.append("             KRCST_WKP_DEFOR_M_CAL_SET.STR_MONTH,   ");
	     exportSQL.append("             KRCST_WKP_DEFOR_M_CAL_SET.PERIOD,   ");
	     exportSQL.append("             KRCST_WKP_DEFOR_M_CAL_SET.REPEAT_ATR,   ");
	     exportSQL.append("             KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR AS DEFOR_INCLUDE_EXTRA_AGGR,   ");
	     exportSQL.append("             IIF(KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR != 0, KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULL) AS DEFOR_INCLUDE_LEGAL_AGGR,   ");
	     exportSQL.append("             IIF(KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR != 0, KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULL) AS DEFOR_INCLUDE_HOLIDAY_AGGR,   ");
	     exportSQL.append("             KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT AS DEFOR_INCLUDE_EXTRA_OT,   ");
	     exportSQL.append("             IIF(KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT != 0, KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_LEGAL_OT, NULL) AS DEFOR_INCLUDE_LEGAL_OT,   ");
	     exportSQL.append("             IIF(KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT != 0, KRCST_WKP_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULL) AS DEFOR_INCLUDE_HOLIDAY_OT   ");
	     exportSQL.append("             FROM KSHST_WKP_TRANS_LAB_TIME    ");
	     exportSQL.append("              INNER JOIN KSHST_WKP_REG_LABOR_TIME ON KSHST_WKP_TRANS_LAB_TIME.CID = KSHST_WKP_REG_LABOR_TIME.CID   ");
	     exportSQL.append("               AND KSHST_WKP_TRANS_LAB_TIME.WKP_ID = KSHST_WKP_REG_LABOR_TIME.WKP_ID    ");
	     exportSQL.append("              INNER JOIN KRCST_WKP_DEFOR_M_CAL_SET ON KSHST_WKP_TRANS_LAB_TIME.CID = KRCST_WKP_DEFOR_M_CAL_SET.CID   ");
	     exportSQL.append("               AND KSHST_WKP_TRANS_LAB_TIME.WKP_ID = KRCST_WKP_DEFOR_M_CAL_SET.WKP_ID   ");
	     exportSQL.append("              INNER JOIN KRCST_WKP_FLEX_M_CAL_SET ON KSHST_WKP_TRANS_LAB_TIME.CID = KRCST_WKP_FLEX_M_CAL_SET.CID   ");
	     exportSQL.append("               AND KSHST_WKP_TRANS_LAB_TIME.WKP_ID = KRCST_WKP_FLEX_M_CAL_SET.WKP_ID   ");
	     exportSQL.append("              INNER JOIN KRCST_WKP_REG_M_CAL_SET ON KSHST_WKP_TRANS_LAB_TIME.CID = KRCST_WKP_REG_M_CAL_SET.CID   ");
	     exportSQL.append("               AND KSHST_WKP_TRANS_LAB_TIME.WKP_ID = KRCST_WKP_REG_M_CAL_SET.WKPID   ");
	     exportSQL.append("              INNER JOIN (SELECT *, ROW_NUMBER() OVER ( PARTITION BY CID ORDER BY END_DATE DESC ) AS RN FROM BSYMT_WKP_CONFIG_2) AS BSYMT_WKP_CONFIG  ");
	     exportSQL.append("             ON KSHST_WKP_TRANS_LAB_TIME.CID = BSYMT_WKP_CONFIG.CID AND BSYMT_WKP_CONFIG.RN = 1  ");
	     exportSQL.append("              LEFT JOIN BSYMT_WKP_INFO ON KSHST_WKP_TRANS_LAB_TIME.CID = BSYMT_WKP_INFO.CID   ");
	     exportSQL.append("               AND KSHST_WKP_TRANS_LAB_TIME.WKP_ID = BSYMT_WKP_INFO.WKP_ID   ");
	     exportSQL.append("               AND BSYMT_WKP_INFO.WKP_HIST_ID = BSYMT_WKP_CONFIG.WKP_HIST_ID  ");
	     exportSQL.append("              LEFT JOIN KSHST_FLX_GET_PRWK_TIME ON KSHST_WKP_TRANS_LAB_TIME.CID = KSHST_FLX_GET_PRWK_TIME.CID   ");
	     exportSQL.append("             WHERE KSHST_WKP_TRANS_LAB_TIME.CID = ?  ");
	     exportSQL.append("            ) TBL  ");
	     exportSQL.append("             ORDER BY TBL.HIERARCHY_CD ASC");

	     GET_WORKPLACE = exportSQL.toString();
	  }
	
<<<<<<< HEAD
	private static final String GET_EMPLOYEE = " SELECT "  
			+ " ROW_NUMBER() OVER( "
			+ " 	PARTITION BY BSYMT_EMP_DTA_MNG_INFO.SCD "
			+ " 	ORDER BY BSYMT_EMP_DTA_MNG_INFO.SCD ASC) AS ROW_NUMBER, " 
			+ " BSYMT_EMP_DTA_MNG_INFO.SID, "
			+ " BSYMT_EMP_DTA_MNG_INFO.SCD, "
			+ " BPSMT_PERSON.BUSINESS_NAME, "
			+ " KSHST_SHA_REG_LABOR_TIME.DAILY_TIME, "
			+ " KSHST_SHA_REG_LABOR_TIME.WEEKLY_TIME, " 
			+ " KSHST_SHA_REG_LABOR_TIME.WEEK_STR, "
			+ " KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR, "
			+ " IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULL) AS INCLUDE_LEGAL_AGGR, "
			+ " IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULL) AS INCLUDE_HOLIDAY_AGGR, " 
			+ " KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT, "
			+ " IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_LEGAL_OT, NULL) AS INCLUDE_LEGAL_OT, "
			+ " IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULL) AS INCLUDE_HOLIDAY_OT, " 
			+ " KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME, "
			+ " KRCST_SHA_FLEX_M_CAL_SET.AGGR_METHOD, "
			+ " IIF(KRCST_SHA_FLEX_M_CAL_SET.AGGR_METHOD = 0, KRCST_SHA_FLEX_M_CAL_SET.INCLUDE_OT, NULL) AS INCLUDE_OT, "
			+ " KRCST_SHA_FLEX_M_CAL_SET.INSUFFIC_SET, "
			+ " KSHST_SHA_TRANS_LAB_TIME.DAILY_TIME AS TRANS_DAILY_TIME, "
			+ " KSHST_SHA_TRANS_LAB_TIME.WEEKLY_TIME AS TRANS_WEEKLY_TIME, "
			+ " KSHST_SHA_TRANS_LAB_TIME.WEEK_STR AS TRANS_WEEK_STR, "
			+ " KRCST_SHA_DEFOR_M_CAL_SET.STR_MONTH, "
			+ " KRCST_SHA_DEFOR_M_CAL_SET.PERIOD, "
			+ " KRCST_SHA_DEFOR_M_CAL_SET.REPEAT_ATR, "
			+ " KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR AS DEFOR_INCLUDE_EXTRA_AGGR, "
			+ " IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULl) AS DEFOR_INCLUDE_LEGAL_AGGR, "
			+ " IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULl) AS DEFOR_INCLUDE_HOLIDAY_AGGR, " 
			+ " KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT AS DEFOR_INCLUDE_EXTRA_OT, "
			+ " IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_LEGAL_OT, NULl) AS DEFOR_INCLUDE_LEGAL_OT, "
			+ " IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULl) AS DEFOR_INCLUDE_HOLIDAY_OT " 
			+ " FROM KSHST_SHA_TRANS_LAB_TIME "
			+ " INNER JOIN KSHST_SHA_REG_LABOR_TIME ON  "
			+ " 	KSHST_SHA_TRANS_LAB_TIME.CID = KSHST_SHA_REG_LABOR_TIME.CID "
			+ " 	AND KSHST_SHA_TRANS_LAB_TIME.SID = KSHST_SHA_REG_LABOR_TIME.SID "
			+ " INNER JOIN KRCST_SHA_DEFOR_M_CAL_SET ON "
			+ " 	KSHST_SHA_TRANS_LAB_TIME.CID = KRCST_SHA_DEFOR_M_CAL_SET.CID "
			+ " 	AND KSHST_SHA_TRANS_LAB_TIME.SID = KRCST_SHA_DEFOR_M_CAL_SET.SID "
			+ " INNER JOIN KRCST_SHA_FLEX_M_CAL_SET ON  "
			+ " 	KSHST_SHA_TRANS_LAB_TIME.CID = KRCST_SHA_FLEX_M_CAL_SET.CID "
			+ " 	AND KSHST_SHA_TRANS_LAB_TIME.SID = KRCST_SHA_FLEX_M_CAL_SET.SID "
			+ " INNER JOIN KRCST_SHA_REG_M_CAL_SET ON  "
			+ " 	KSHST_SHA_TRANS_LAB_TIME.CID = KRCST_SHA_REG_M_CAL_SET.CID "
			+ " 	AND KSHST_SHA_TRANS_LAB_TIME.SID = KRCST_SHA_REG_M_CAL_SET.SID "
			+ " INNER JOIN BSYMT_EMP_DTA_MNG_INFO ON "
			+ " 	KSHST_SHA_TRANS_LAB_TIME.CID = BSYMT_EMP_DTA_MNG_INFO.CID "
			+ " 	AND KSHST_SHA_TRANS_LAB_TIME.SID = BSYMT_EMP_DTA_MNG_INFO.SID "
			+ " INNER JOIN BPSMT_PERSON ON "
			+ " 	BSYMT_EMP_DTA_MNG_INFO.PID = BPSMT_PERSON.PID "
			+ " LEFT JOIN KSHST_FLX_GET_PRWK_TIME ON  "
			+ "	KSHST_SHA_TRANS_LAB_TIME.CID = KSHST_FLX_GET_PRWK_TIME.CID "
			+ " WHERE  KSHST_SHA_TRANS_LAB_TIME.CID = ? "
			+ " ORDER BY BSYMT_EMP_DTA_MNG_INFO.SCD ASC " ;
=======
	private static final String GET_EMPLOYEE = " SELECT  " 
			 + "ROW_NUMBER() OVER(PARTITION BY BSYMT_EMP_DTA_MNG_INFO.SCD ORDER BY BSYMT_EMP_DTA_MNG_INFO.SCD ASC, KSHST_SHA_NORMAL_SET.[YEAR]) AS ROW_NUMBER, "
			 + "BSYMT_EMP_DTA_MNG_INFO.SCD, " 
			 + "BPSMT_PERSON.BUSINESS_NAME, "
			 + "KSHST_SHA_NORMAL_SET.[YEAR], " 
			 + "KSHST_SHA_NORMAL_SET.JAN_TIME AS S1, " 
			 + "KSHST_SHA_NORMAL_SET.FEB_TIME AS S2, " 
			 + "KSHST_SHA_NORMAL_SET.MAR_TIME AS S3, " 
			 + "KSHST_SHA_NORMAL_SET.APR_TIME AS S4, " 
			 + "KSHST_SHA_NORMAL_SET.MAY_TIME AS S5, " 
			 + "KSHST_SHA_NORMAL_SET.JUN_TIME AS S6, " 
			 + "KSHST_SHA_NORMAL_SET.JUL_TIME AS S7, " 
			 + "KSHST_SHA_NORMAL_SET.AUG_TIME AS S8, " 
			 + "KSHST_SHA_NORMAL_SET.SEP_TIME AS S9, " 
			 + "KSHST_SHA_NORMAL_SET.OCT_TIME AS S10, " 
			 + "KSHST_SHA_NORMAL_SET.NOV_TIME AS S11, " 
			 + "KSHST_SHA_NORMAL_SET.DEC_TIME AS S12, " 
			 + "KSHST_SHA_REG_LABOR_TIME.DAILY_TIME, " 
			 + "KSHST_SHA_REG_LABOR_TIME.WEEKLY_TIME, " 
			 + "KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR, " 
			 + "IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULL) AS INCLUDE_LEGAL_AGGR, " 
			 + "IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULL) AS INCLUDE_HOLIDAY_AGGR, " 
			 + "KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT, " 
			 + "IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_LEGAL_OT, NULL) AS INCLUDE_LEGAL_OT, " 
			 + "IIF(KRCST_SHA_REG_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_REG_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULL) AS INCLUDE_HOLIDAY_OT, " 
			 + "KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME, " 
			 + "KSHST_SHA_FLEX_SET.STAT_JAN_TIME AS F1, " 
			 + "KSHST_SHA_FLEX_SET.STAT_FEB_TIME AS F2, " 
			 + "KSHST_SHA_FLEX_SET.STAT_MAR_TIME AS F3, " 
			 + "KSHST_SHA_FLEX_SET.STAT_APR_TIME AS F4, " 
			 + "KSHST_SHA_FLEX_SET.STAT_MAY_TIME AS F5, " 
			 + "KSHST_SHA_FLEX_SET.STAT_JUN_TIME AS F6, " 
			 + "KSHST_SHA_FLEX_SET.STAT_JUL_TIME AS F7, " 
			 + "KSHST_SHA_FLEX_SET.STAT_AUG_TIME AS F8, " 
			 + "KSHST_SHA_FLEX_SET.STAT_SEP_TIME AS F9, " 
			 + "KSHST_SHA_FLEX_SET.STAT_OCT_TIME AS F10, " 
			 + "KSHST_SHA_FLEX_SET.STAT_NOV_TIME AS F11, " 
			 + "KSHST_SHA_FLEX_SET.STAT_DEC_TIME AS F12, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_JAN_TIME, NULL) AS T1, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_FEB_TIME, NULL) AS T2, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_MAR_TIME, NULL) AS T3, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_APR_TIME, NULL) AS T4, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_MAY_TIME, NULL) AS T5, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_JUN_TIME, NULL) AS T6, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_JUL_TIME, NULL) AS T7, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_AUG_TIME, NULL) AS T8, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_SEP_TIME, NULL) AS T9, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_OCT_TIME, NULL) AS T10, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_NOV_TIME, NULL) AS T11, " 
			 + "IIF(KSHST_FLX_GET_PRWK_TIME.REFERENCE_PRED_TIME = 0, KSHST_SHA_FLEX_SET.SPEC_DEC_TIME, NULL) AS T12, " 
			 + "KRCST_SHA_FLEX_M_CAL_SET.AGGR_METHOD, " 
			 + "IIF(KRCST_SHA_FLEX_M_CAL_SET.AGGR_METHOD = 0, KRCST_SHA_FLEX_M_CAL_SET.INCLUDE_OT, NULL) AS INCLUDE_OT, " 
			 + "KRCST_SHA_FLEX_M_CAL_SET.INSUFFIC_SET, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.JAN_TIME AS M1, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.FEB_TIME AS M2, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.MAR_TIME AS M3, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.APR_TIME AS M4, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.MAY_TIME AS M5, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.JUN_TIME AS M6, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.JUL_TIME AS M7, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.AUG_TIME AS M8, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.SEP_TIME AS M9, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.OCT_TIME AS M10, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.NOV_TIME AS M11, " 
			 + "KSHST_SHA_DEFOR_LAR_SET.DEC_TIME AS M12, " 
			 + "KSHST_SHA_TRANS_LAB_TIME.DAILY_TIME AS TRANS_DAILY_TIME, " 
			 + "KSHST_SHA_TRANS_LAB_TIME.WEEKLY_TIME AS TRANS_WEEKLY_TIME, " 
			 + "KRCST_SHA_DEFOR_M_CAL_SET.STR_MONTH, " 
			 + "KRCST_SHA_DEFOR_M_CAL_SET.PERIOD, " 
			 + "KRCST_SHA_DEFOR_M_CAL_SET.REPEAT_ATR, " 
			 + "KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR AS DEFOR_INCLUDE_EXTRA_AGGR, " 
			 + "IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_LEGAL_AGGR, NULl) AS DEFOR_INCLUDE_LEGAL_AGGR, " 
			 + "IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_AGGR = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_AGGR, NULl) AS DEFOR_INCLUDE_HOLIDAY_AGGR, " 
			 + "KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT AS DEFOR_INCLUDE_EXTRA_OT, " 
			 + "IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_LEGAL_OT, NULl) AS DEFOR_INCLUDE_LEGAL_OT, " 
			 + "IIF(KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_EXTRA_OT = 1, KRCST_SHA_DEFOR_M_CAL_SET.INCLUDE_HOLIDAY_OT, NULl) AS DEFOR_INCLUDE_HOLIDAY_OT " 
			 + "FROM KSHST_SHA_NORMAL_SET  " 
			 + "INNER JOIN KSHST_SHA_FLEX_SET ON  " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KSHST_SHA_FLEX_SET.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.[YEAR] >= ? " 
			 + "	AND KSHST_SHA_NORMAL_SET.[YEAR] <= ? " 
			 + "	AND KSHST_SHA_NORMAL_SET.[YEAR] = KSHST_SHA_FLEX_SET.[YEAR] " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KSHST_SHA_FLEX_SET.SID " 
			 + "INNER JOIN KSHST_SHA_DEFOR_LAR_SET ON " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KSHST_SHA_DEFOR_LAR_SET.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.[YEAR] = KSHST_SHA_DEFOR_LAR_SET.[YEAR] " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KSHST_SHA_DEFOR_LAR_SET.SID " 
			 + "INNER JOIN KSHST_SHA_TRANS_LAB_TIME ON " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KSHST_SHA_TRANS_LAB_TIME.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KSHST_SHA_TRANS_LAB_TIME.SID " 
			 + "INNER JOIN KSHST_SHA_REG_LABOR_TIME ON  " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KSHST_SHA_REG_LABOR_TIME.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KSHST_SHA_REG_LABOR_TIME.SID " 
			 + "INNER JOIN KRCST_SHA_DEFOR_M_CAL_SET ON " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KRCST_SHA_DEFOR_M_CAL_SET.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KRCST_SHA_DEFOR_M_CAL_SET.SID " 
			 + "INNER JOIN KRCST_SHA_FLEX_M_CAL_SET ON  " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KRCST_SHA_FLEX_M_CAL_SET.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KRCST_SHA_FLEX_M_CAL_SET.SID " 
			 + "INNER JOIN KRCST_SHA_REG_M_CAL_SET ON  " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KRCST_SHA_REG_M_CAL_SET.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = KRCST_SHA_REG_M_CAL_SET.SID " 
			 + "INNER JOIN BSYMT_EMP_DTA_MNG_INFO ON " 
			 + "	KSHST_SHA_NORMAL_SET.CID = BSYMT_EMP_DTA_MNG_INFO.CID " 
			 + "	AND KSHST_SHA_NORMAL_SET.SID = BSYMT_EMP_DTA_MNG_INFO.SID " 
			 + "INNER JOIN BPSMT_PERSON ON " 
			 + "	BSYMT_EMP_DTA_MNG_INFO.PID = BPSMT_PERSON.PID " 
			 + "LEFT JOIN KSHST_FLX_GET_PRWK_TIME ON  " 
			 + "	KSHST_SHA_NORMAL_SET.CID = KSHST_FLX_GET_PRWK_TIME.CID " 
			 + "WHERE  KSHST_SHA_NORMAL_SET.CID = ? "
			 + "ORDER BY BSYMT_EMP_DTA_MNG_INFO.SCD, KSHST_SHA_NORMAL_SET.[YEAR] ASC  " ;
>>>>>>> 5c46af2182b... 「週の管理」の対応
	
	private static final String GET_USAGE = "SELECT s.IS_EMP, s.IS_WKP, s.IS_EMPT FROM KUWST_USAGE_UNIT_WT_SET s WHERE s.CID = ?cid ";
	
	@Override
	public Object[] getUsage() {
		String cid = AppContexts.user().companyId();
		Query usage = this.getEntityManager().createNativeQuery(GET_USAGE.toString()).setParameter("cid", cid);
		List<Object[]> data = usage.getResultList();
		if (data.size() == 0) {
			return null;
		}
		return data.get(0);
	}
	
	private int month(){
		String cid = AppContexts.user().companyId();
		int month = 1;
		Query monthQuery = this.getEntityManager().createNativeQuery(GET_EXPORT_MONTH.toString()).setParameter("cid", cid);
		List data = monthQuery.getResultList();
		if (data.size() == 0) {
			month = 1;
		} else {
			month = Integer.valueOf(data.get(0).toString());
		}
		return month;
	}
	
	@Override
	public List<MasterData> getCompanyExportData(int startDate, int endDate) {
		String cid = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
		
<<<<<<< HEAD
		val legalTimes = this.queryProxy().query(LEGAL_TIME_COM, KshmtLegalTimeMCom.class)
				.setParameter("cid", cid)
				.setParameter("start", startDate * 100 + 1)
				.setParameter("end", endDate * 100 + 12)
				.getList();
=======
		String startOfWeek = getStartOfWeek(cid);
>>>>>>> 5c46af2182b... 「週の管理」の対応
		
		try (PreparedStatement stmt = this.connection().prepareStatement(GET_EXPORT_EXCEL.toString())) {
			stmt.setInt(1, startDate);
			stmt.setInt(2, endDate);
			stmt.setString(3, cid);
			NtsResultSet result = new NtsResultSet(stmt.executeQuery());
			int month = this.month();
			result.forEach(i -> {
<<<<<<< HEAD
				datas.addAll(buildCompanyRow(i, legalTimes, startDate, endDate, month));
=======
				datas.addAll(buildCompanyRow(i, month, startOfWeek));
>>>>>>> 5c46af2182b... 「週の管理」の対応
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}
<<<<<<< HEAD
	private List<MasterData> buildCompanyRow(NtsResultRecord r, List<KshmtLegalTimeMCom> legals, int startDate, int endDate, int month) {
=======
	
	private String getStartOfWeek(String cid) {
		
		int startOfWeek = this.queryProxy().find(cid, KsrmtWeekRuleMng.class)
											.map(w -> w.startOfWeek).orElse(0);
		
		return getWeekStart(startOfWeek);
	}
	
	private List<MasterData> buildCompanyRow(NtsResultRecord r, int month, String startOfWeek){
>>>>>>> 5c46af2182b... 「週の管理」の対応
		List<MasterData> datas = new ArrayList<>();

		Integer refPreTime = r.getInt("REFERENCE_PRED_TIME");
		
<<<<<<< HEAD
		for (int y = startDate; y <= endDate; y++) {
			int ym = y *100 + month;
			
			val normal = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val defor = legals.stream()
					.filter(l -> {
						return l.pk.ym == ym && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value;
					})
					.findFirst();
			val flex = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			
			datas.add(buildARow(
					String.valueOf(y),
					//R8_2 R8_3
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"), 
					//R8_4
					convertTime(normal.isPresent() ? normal.get().legalTime : 0),
					//R8_5
					I18NText.getText("KMK004_177"),
					//R8_7
					convertTime(r.getInt(("DAILY_TIME"))), 
					//R8_9
					getWeekStart(r.getInt("WEEK_STR")),
					//R8_10
					getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
					//R8_11
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null, 
					//R8_12
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null, 
					//R8_13
					getExtraType(r.getInt("INCLUDE_EXTRA_OT")), 
					//R8_14
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null,
					//R8_15
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
					//R8_16
					getFlexType(r.getInt("REFERENCE_PRED_TIME")), 
					//R8_17 R8_18
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R8_19
					refPreTime != null && refPreTime == 0 ? (flex.isPresent() ? String.valueOf(flex.get().withinTime) : null) : null,
					// R10_22
					convertTime(flex.isPresent() ? flex.get().legalTime : 0),
					//R8_21		
					getAggType(r.getInt("AGGR_METHOD")),
					//R8_22
					r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
					//R8_23
					getShortageTime(r.getInt("INSUFFIC_SET")), 
					//R8_24 8_25
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R8_26
					convertTime(defor.isPresent() ? defor.get().legalTime : 0),
					//R8_27		
					I18NText.getText("KMK004_177"), 
					//R8_29
					convertTime(r.getInt("REG_DAILY_TIME")), 
					//R8_31
					getWeekStart(r.getInt("REG_WEEK_STR")),
					//R8_32 R8_33
					r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"), 
					//R8_34 R8_35
					r.getInt("PERIOD") + I18NText.getText("KMK004_180"), 
					//R8_36
					r.getInt("REPEAT_ATR") == 1 ? "○" : "-", 
					//R8_37
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
					//R8_38
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
					//R8_39
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
					//R8_40
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
					//R8_41
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
					//R8_42
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null
					));

			int nextYm = y *100 + month + 1;
			val normalN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val deforN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
					.findFirst();
			val flexN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			//Arow month + 1
=======
		//Arow month
		datas.add(buildARow(
				//R8_1
				r.getString(("YEAR")),
				//R8_2 R8_3
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"), 
				//R8_4
				convertTime(r.getInt(("N"+ ((month - 1) % 12 + 1)))),
				//R8_5
				I18NText.getText("KMK004_177"),
				//R8_7
				convertTime(r.getInt(("DAILY_TIME"))), 
				//R8_9
				startOfWeek,
				//R8_10
				getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
				//R8_11
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null, 
				//R8_12
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null, 
				//R8_13
				getExtraType(r.getInt("INCLUDE_EXTRA_OT")), 
				//R8_14
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null,
				//R8_15
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
				//R8_16
				getFlexType(r.getInt("REFERENCE_PRED_TIME")), 
				//R8_17 R8_18
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				//R8_19
				r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("SPEC_T"+ ((month - 1) % 12 + 1)))) : null,
				//R8_20
				convertTime(r.getInt(("F"+ ((month - 1) % 12 + 1)))),
				//R8_21		
				getAggType(r.getInt("AGGR_METHOD")),
				//R8_22
				r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
				//R8_23
				getShortageTime(r.getInt("INSUFFIC_SET")), 
				//R8_24 8_25
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				//R8_26
				convertTime(r.getInt(("T"+ ((month - 1) % 12 + 1)))), 
				//R8_27		
				I18NText.getText("KMK004_177"), 
				//R8_29
				convertTime(r.getInt("REG_DAILY_TIME")), 
				//R8_31
				startOfWeek,
				//R8_32 R8_33
				r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"), 
				//R8_34 R8_35
				r.getInt("PERIOD") + I18NText.getText("KMK004_180"), 
				//R8_36
				r.getInt("REPEAT_ATR") == 1 ? "○" : "-", 
				//R8_37
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
				//R8_38
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
				//R8_39
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
				//R8_40
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
				//R8_41
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
				//R8_42
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null
				));
		
		//Arow month + 1
		datas.add(buildARow(
				//R8_1
				null,
				//R8_2 R8_3
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"), 
				//R8_4
				convertTime(r.getInt(("N"+ ((month - 1) % 12 + 2)))),
				//R8_6
				I18NText.getText("KMK004_178"),
				//R8_8
				convertTime(r.getInt(("WEEKLY_TIME"))), 
				//R8_9
				null,
				//R8_10
				null,
				//R8_11
				null, 
				//R8_12
				null, 
				//R8_13
				null, 
				//R8_14
				null,
				//R8_15
				null,
				//R8_16
				null, 
				//R8_17 R8_18
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				//R8_19
				r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("SPEC_T"+ ((month - 1) % 12 + 2)))) : null,
				//R8_20
				convertTime(r.getInt(("F"+ ((month - 1) % 12 + 2)))),
				//R8_21		
				null,
				//R8_22
				null,
				//R8_23
				null, 
				//R8_24 8_25
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				//R8_26
				convertTime(r.getInt(("T"+ ((month - 1) % 12 + 2)))), 
				//R8_28	
				I18NText.getText("KMK004_178"), 
				//R8_30
				convertTime(r.getInt("REG_WEEKLY_TIME")), 
				//R8_31
				null,
				//R8_32 R8_33
				null, 
				//R8_34 R8_35
				null, 
				//R8_36
				null, 
				//R8_37
				null,
				//R8_38
				null,
				//R8_39
				null,
				//R8_40
				null,
				//R8_41
				null,
				//R8_42
				null
				));
		//Arow month remain
		for(int i = 1 ; i < 11; i++){
>>>>>>> 5c46af2182b... 「週の管理」の対応
			datas.add(buildARow(
					//R8_1
					null,
					//R8_2 R8_3
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"), 
					//R8_4
					convertTime(normalN.isPresent() ? normalN.get().legalTime : 0),
					//R8_6
					I18NText.getText("KMK004_178"),
					//R8_8
					convertTime(r.getInt(("WEEKLY_TIME"))), 
					//R8_9
					null,
					//R8_10
					null,
					//R8_11
					null, 
					//R8_12
					null, 
					//R8_13
					null, 
					//R8_14
					null,
					//R8_15
					null,
					//R8_16
					null, 
					//R8_17 R8_18
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R8_19
					refPreTime != null && refPreTime == 0 ? (flexN.isPresent() ? String.valueOf(flexN.get().withinTime) : null) : null,
					// R10_22
					convertTime(flexN.isPresent() ? flexN.get().legalTime : 0),
					//R8_21		
					null,
					//R8_22
					null,
					//R8_23
					null, 
					//R8_24 8_25
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R8_26
					convertTime(deforN.isPresent() ? deforN.get().legalTime : 0),
					//R8_28	
					I18NText.getText("KMK004_178"), 
					//R8_30
					convertTime(r.getInt("REG_WEEKLY_TIME")), 
					//R8_31
					null,
					//R8_32 R8_33
					null, 
					//R8_34 R8_35
					null, 
					//R8_36
					null, 
					//R8_37
					null,
					//R8_38
					null,
					//R8_39
					null,
					//R8_40
					null,
					//R8_41
					null,
					//R8_42
					null
					));
			
			// buil month remain
			for (int i = month + 1; i <= month + 10; i++) {
				int currentYm = y *100 + month + 1;
				val normalC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
						.findFirst();
				val deforC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
						.findFirst();
				val flexC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.type == LaborWorkTypeAttr.FLEX.value)
						.findFirst();
				datas.add(buildARow(
						//R8_1
						null,
						//R8_2 R8_3
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"), 
						//R8_4
						convertTime(normalC.isPresent() ? normalC.get().legalTime : 0),
						//R8_6
						null,
						//R8_8
						null, 
						//R8_9
						null,
						//R8_10
						null,
						//R8_11
						null, 
						//R8_12
						null, 
						//R8_13
						null, 
						//R8_14
						null,
						//R8_15
						null,
						//R8_16
						null, 
						//R8_17 R8_18
						(month + i) % 12 + 1 + I18NText.getText("KMK004_176"),
						//R8_19
						refPreTime != null && refPreTime == 0 ? (flexC.isPresent() ? String.valueOf(flexC.get().withinTime) : null) : null,
						// R10_22
						convertTime(flexC.isPresent() ? flexC.get().legalTime : 0),
						//R8_21		
						null,
						//R8_22
						null,
						//R8_23
						null, 
						//R8_24 8_25
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						//R8_26
						convertTime(deforC.isPresent() ? deforC.get().legalTime : 0),
						//R8_28	
						null, 
						//R8_30
						null, 
						//R8_31
						null,
						//R8_32 R8_33
						null, 
						//R8_34 R8_35
						null, 
						//R8_36
						null, 
						//R8_37
						null,
						//R8_38
						null,
						//R8_39
						null,
						//R8_40
						null,
						//R8_41
						null,
						//R8_42
						null
						));
			}
		}
		return datas;
	}
	
	private MasterData buildARow(
			String value1, String value2, String value3, String value4, String value5,
			String value6, String value7, String value8, String value9, String value10,
			String value11, String value12, String value13, String value14, String value15,
			String value16, String value17, String value18, String value19, String value20,
			String value21, String value22, String value23, String value24, String value25,
			String value26, String value27, String value28, String value29, String value30,
			String value31, String value32, String value33) {
		
		Map<String, MasterCellData> data = new HashMap<>();
            data.put(CompanyColumn.KMK004_155, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_155)
                .value(value2)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_156, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_156)
                .value(value3)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_154, MasterCellData.builder()
                    .columnId(CompanyColumn.KMK004_154)
                    .value(value1)
                    .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                    .build());
            data.put(CompanyColumn.KMK004_157, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_157)
                .value(value4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_156_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_156_1)
                .value(value5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_158, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_158)
                .value(value6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_159, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_159)
                .value(value7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_160, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_160)
                .value(value8)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_161, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_161)
                .value(value9)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_162, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_162)
                .value(value10)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_163, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_163)
                .value(value11)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_164, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_164)
                .value(value12)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_165, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_165)
                .value(value13)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_166, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_166)
                .value(value14)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_167, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_167)
                .value(value15)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_156_2, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_156_2)
                .value(value16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_168, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_168)
                .value(value17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_169, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_169)
                .value(value18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_170, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_170)
                .value(value19)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_171, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_171)
                .value(value20)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_156_3, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_156_3)
                .value(value21)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_172, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_172)
                .value(value22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_156_4, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_156_4)
                .value(value23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_158_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_158_1)
                .value(value24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_173, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_173)
                .value(value25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_174, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_174)
                .value(value26)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_175, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_175)
                .value(value27)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_159_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_159_1)
                .value(value28)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_160_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_160_1)
                .value(value29)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_161_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_161_1)
                .value(value30)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_162_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_162_1)
                .value(value31)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_163_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_163_1)
                .value(value32)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_164_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_164_1)
                .value(value33)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		return MasterData.builder().rowData(data).build();
	}
	
	@Override
	public List<MasterData> getEmploymentExportData(int startDate, int endDate) {
		String cid = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
		
<<<<<<< HEAD
		val legalTimes = this.queryProxy().query(LEGAL_TIME_EMP, KshmtLegalTimeMEmp.class)
				.setParameter("cid", cid)
				.setParameter("start", startDate * 100 + 1)
				.setParameter("end", endDate * 100 + 12)
				.getList();
			
=======
		String startOfWeek = getStartOfWeek(cid);
		
>>>>>>> 5c46af2182b... 「週の管理」の対応
		try (PreparedStatement stmt = this.connection().prepareStatement(GET_EMPLOYMENT.toString())) {
			stmt.setInt(1, startDate);
			stmt.setInt(2, endDate);
			stmt.setString(3, cid);
			NtsResultSet result = new NtsResultSet(stmt.executeQuery());
			int month = this.month();
			result.forEach(i -> {
<<<<<<< HEAD
				datas.addAll(buildEmploymentRow(i, legalTimes, startDate, endDate, month));
=======
				datas.addAll(buildEmploymentRow(i, month, startOfWeek));
>>>>>>> 5c46af2182b... 「週の管理」の対応
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
<<<<<<< HEAD
	private List<MasterData> buildEmploymentRow(NtsResultRecord r, List<KshmtLegalTimeMEmp> legals, int startDate, int endDate, int month) {
=======
	private List<MasterData> buildEmploymentRow(NtsResultRecord r, int month, String startOfWeek){
>>>>>>> 5c46af2182b... 「週の管理」の対応
		List<MasterData> datas = new ArrayList<>();

		Integer refPreTime = r.getInt("REFERENCE_PRED_TIME");
		
<<<<<<< HEAD
		for (int y = startDate; y <= endDate; y++) {
			String employmentCode = r.getString("CODE");
			int ym = y *100 + month;
			
			val normal = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val defor = legals.stream()
					.filter(l -> {
						return l.pk.ym == ym && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value;
					})
					.findFirst();
			val flex = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			
			datas.add(buildWorkPlaceARow(
					//R12_3
					String.valueOf(y),
					//R12_1
					r.getInt("ROW_NUMBER_CODE") == 1 ? r.getString("CODE") : null,
					//R12_2
					r.getInt("ROW_NUMBER_CODE") == 1 ? r.getString("NAME") : null,
					//R12_4 R12_5
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"), 
					//R12_6
					convertTime(normal.isPresent() ? normal.get().legalTime : 0),
					//R12_7
					I18NText.getText("KMK004_177"),
					//R12_9
					convertTime(r.getInt("DAILY_TIME")),
					//R12_11
					getWeekStart(r.getInt("WEEK_STR")),
					//R12_12
					getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
					//R12_13
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null, 
					//R12_14		
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null, 
					//R12_15		
					getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
					//R12_16
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null, 
					//R12_17
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null, 
					//R12_18
					refPreTime == null? null : getFlexType(r.getInt("REFERENCE_PRED_TIME")),
					//R12_19 R12_20
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R12_21
					refPreTime != null && refPreTime == 0 ? (flex.isPresent() ? String.valueOf(flex.get().withinTime) : null) : null,
					// R10_22
					convertTime(flex.isPresent() ? flex.get().legalTime : 0),
					//R12_23
					getAggType(r.getInt("AGGR_METHOD")),
					//R12_24
					r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
					//R12_25
					getShortageTime(r.getInt("INSUFFIC_SET")), 
					//R12_26 R12_27
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R12_28
					convertTime(defor.isPresent() ? defor.get().legalTime : 0),
					//R12_29
					I18NText.getText("KMK004_177"), 
					//R12_31
					convertTime(r.getInt("LAR_DAILY_TIME")),
					//R12_33
					getWeekStart(r.getInt("LAR_WEEK_STR")),
					//R12_34
					r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"), 
					//R12_35
					r.getInt("PERIOD") + I18NText.getText("KMK004_180"), 
					//R12_38
					r.getInt("REPEAT_ATR") == 1 ? "○" : "-", 
					//R8_39
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
					//R8_40
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
					//R8_41
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
					//R8_42
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
					//R8_43
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
					//R8_44
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null
					));

			int nextYm = y *100 + month + 1;
			val normalN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val deforN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
					.findFirst();
			val flexN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			// buil arow = month + 1
=======
		String refPreTime = r.getString("REFERENCE_PRED_TIME");
		
		datas.add(buildEmploymentARow(
				//R12_3
			     r.getString("YEAR"),
				//R12_1
				r.getInt("ROW_NUMBER_CODE") == 1 ? r.getString("CODE") : null,
				//R12_2
				r.getInt("ROW_NUMBER_CODE") == 1 ? r.getString("NAME") : null,
				//R12_4 R12_5
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"), 
				//R12_6
				convertTime(r.getInt(("M"+ ((month - 1) % 12 + 1)))),
				//R12_7
				I18NText.getText("KMK004_177"),
				//R12_9
				convertTime(r.getInt("DAILY_TIME")),
				//R12_11
				startOfWeek,
				//R12_12
				getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
				//R12_13
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null, 
				//R12_14		
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null, 
				//R12_15		
				getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
				//R12_16
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null, 
				//R12_17
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null, 
				//R12_18
				refPreTime == null? null : getFlexType(r.getInt("REFERENCE_PRED_TIME")),
				//R12_19 R12_20
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				//R12_21
				refPreTime != null && r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("F"+ ((month - 1) % 12 + 1)))) : null,
				//R12_22
				convertTime(r.getInt(("S"+ ((month - 1) % 12 + 1)))),
				//R12_23
				getAggType(r.getInt("AGGR_METHOD")),
				//R12_24
				r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
				//R12_25
				getShortageTime(r.getInt("INSUFFIC_SET")), 
				//R12_26 R12_27
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				//R12_28
				convertTime(r.getInt(("D"+ ((month - 1) % 12 + 1)))), 
				//R12_29
				I18NText.getText("KMK004_177"), 
				//R12_31
				convertTime(r.getInt("LAR_DAILY_TIME")),
				//R12_33
				startOfWeek,
				//R12_34
				r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"), 
				//R12_35
				r.getInt("PERIOD") + I18NText.getText("KMK004_180"), 
				//R12_38
				r.getInt("REPEAT_ATR") == 1 ? "○" : "-", 
				//R8_39
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
				//R8_40
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
				//R8_41
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
				//R8_42
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
				//R8_43
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
				//R8_44
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null
				));
		//Arow month + 1
		datas.add(buildEmploymentARow(
				//R12_1
				null,
				//R12_2
				null,
				//R12_3
				null,
				//R12_4 R12_5
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"), 
				//R12_6
				convertTime(r.getInt(("M"+ ((month - 1) % 12 + 2)))),
				//R12_8
				I18NText.getText("KMK004_178"),
				//R12_9
				convertTime(r.getInt("WEEKLY_TIME")),
				//R12_11
				null,
				//R12_12
				null,
				//R12_13
				null, 
				//R12_14		
				null, 
				//R12_15		
				null,
				//R12_16
				null, 
				//R12_17
				null, 
				//R12_18
				null,
				//R12_19 R12_20
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				//R12_21
				refPreTime != null && r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("F"+ ((month - 1) % 12 + 2)))) : null,
				//R12_22
				convertTime(r.getInt(("S"+ ((month - 1) % 12 + 2)))),
				//R12_23
				null,
				//R12_24
				null,
				//R12_25
				null, 
				//R12_26 R12_27
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				//R12_28
				convertTime(r.getInt(("D"+ ((month - 1) % 12 + 2)))), 
				//R12_30
				I18NText.getText("KMK004_178"), 
				//R12_32
				convertTime(r.getInt("LAR_WEEKLY_TIME")),
				//R12_33
				null,
				//R12_34
				null, 
				//R12_35
				null, 
				//R12_38
				null, 
				//R8_39
				null,
				//R8_40
				null,
				//R8_41
				null,
				//R8_42
				null,
				//R8_43
				null,
				//R8_44
				null
				));
		for(int i = 1 ; i < 11; i++){
>>>>>>> 5c46af2182b... 「週の管理」の対応
			datas.add(buildEmploymentARow(
					//R12_1
					null,
					//R12_2
					null,
					//R12_3
					null,
					//R12_4 R12_5
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"), 
					//R12_6
					convertTime(normalN.isPresent() ? normalN.get().legalTime : 0),
					//R12_8
					I18NText.getText("KMK004_178"),
					//R12_9
					convertTime(r.getInt("WEEKLY_TIME")),
					//R12_11
					null,
					//R12_12
					null,
					//R12_13
					null, 
					//R12_14		
					null, 
					//R12_15		
					null,
					//R12_16
					null, 
					//R12_17
					null, 
					//R12_18
					null,
					//R12_19 R12_20
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R12_21
					refPreTime != null && refPreTime == 0 ? (flexN.isPresent() ? String.valueOf(flexN.get().withinTime) : null) : null,
					// R10_22
					convertTime(flexN.isPresent() ? flexN.get().legalTime : 0),
					//R12_23
					null,
					//R12_24
					null,
					//R12_25
					null, 
					//R12_26 R12_27
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R12_28
					convertTime(deforN.isPresent() ? deforN.get().legalTime : 0),
					//R12_30
					I18NText.getText("KMK004_178"), 
					//R12_32
					convertTime(r.getInt("LAR_WEEKLY_TIME")),
					//R12_33
					null,
					//R12_34
					null, 
					//R12_35
					null, 
					//R12_38
					null, 
					//R8_39
					null,
					//R8_40
					null,
					//R8_41
					null,
					//R8_42
					null,
					//R8_43
					null,
					//R8_44
					null
					));
			
			// buil month remain
			for (int i = month + 1; i <= month + 10; i++) {
				int currentYm = y *100 + month + 1;
				val normalC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
						.findFirst();
				val deforC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
						.findFirst();
				val flexC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.empCD.equals(employmentCode) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
						.findFirst();
				datas.add(buildEmploymentARow(
						//R12_1
						null,
						//R12_2
						null,
						//R12_3
						null,
						//R12_4 R12_5
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"), 
						//R12_6
						convertTime(normalC.isPresent() ? normalC.get().legalTime : 0),
						//R12_8
						null,
						//R12_9
						null,
						//R12_11
						null,
						//R12_12
						null,
						//R12_13
						null, 
						//R12_14		
						null, 
						//R12_15		
						null,
						//R12_16
						null, 
						//R12_17
						null, 
						//R12_18
						null,
						//R12_19 R12_20
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						//R12_21
						refPreTime != null && refPreTime == 0 ? (flexC.isPresent() ? String.valueOf(flexC.get().withinTime) : null) : null,
						// R10_22
						convertTime(flexC.isPresent() ? flexC.get().legalTime : 0),
						//R12_23
						null,
						//R12_24
						null,
						//R12_25
						null, 
						//R12_26 R12_27
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						//R12_28
						convertTime(deforC.isPresent() ? deforC.get().legalTime : 0),
						//R12_30
						null, 
						//R12_32
						null,
						//R12_33
						null,
						//R12_34
						null, 
						//R12_35
						null, 
						//R12_38
						null, 
						//R8_39
						null,
						//R8_40
						null,
						//R8_41
						null,
						//R8_42
						null,
						//R8_43
						null,
						//R8_44
						null
						));
			}
		}
		return datas;
	}
	
	private MasterData buildEmploymentARow(
			String value1, String value2, String value3, String value4, String value5,
			String value6, String value7, String value8, String value9, String value10,
			String value11, String value12, String value13, String value14, String value15,
			String value16, String value17, String value18, String value19, String value20,
			String value21, String value22, String value23, String value24, String value25,
			String value26, String value27, String value28, String value29, String value30,
			String value31, String value32, String value33, String value34, String value35) {
		
		Map<String, MasterCellData> data = new HashMap<>();
			data.put(EmploymentColumn.KMK004_154, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_154)
                .value(value1)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_185, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_185)
                .value(value2)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_186, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_186)
                .value(value3)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_155, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_155)
                .value(value4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_156, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_156)
                .value(value5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_157, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_157)
                .value(value6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_156_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_156_1)
                .value(value7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_158, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_158)
                .value(value8)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_159, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_159)
                .value(value9)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_160, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_160)
                .value(value10)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_161, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_161)
                .value(value11)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_162, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_162)
                .value(value12)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_163, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_163)
                .value(value13)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_164, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_164)
                .value(value14)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_165, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_165)
                .value(value15)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_166, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_166)
                .value(value16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_167, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_167)
                .value(value17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_156_2, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_156_2)
                .value(value18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_168, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_168)
                .value(value19)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_169, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_169)
                .value(value20)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_170, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_170)
                .value(value21)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_171, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_171)
                .value(value22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_156_3, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_156_3)
                .value(value23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_172, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_172)
                .value(value24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_156_4, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_156_4)
                .value(value25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_158_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_158_1)
                .value(value26)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_173, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_173)
                .value(value27)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_174, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_174)
                .value(value28)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(EmploymentColumn.KMK004_175, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_175)
                .value(value29)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_159_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_159_1)
                .value(value30)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_160_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_160_1)
                .value(value31)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_161_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_161_1)
                .value(value32)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_162_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_162_1)
                .value(value33)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_163_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_163_1)
                .value(value34)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(EmploymentColumn.KMK004_164_1, MasterCellData.builder()
                .columnId(EmploymentColumn.KMK004_164_1)
                .value(value35)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
			
		return MasterData.builder().rowData(data).build();
	}
	
	@Override
	public List<MasterData> getWorkPlaceExportData(int startDate, int endDate) {
		String cid = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
<<<<<<< HEAD

		val legalTimes = this.queryProxy().query(LEGAL_TIME_WKP, KshmtLegalTimeMWkp.class)
			.setParameter("cid", cid)
			.setParameter("start", startDate * 100 + 1)
			.setParameter("end", endDate * 100 + 12)
			.getList();
=======
		
		String startOfWeek = getStartOfWeek(cid);
>>>>>>> 5c46af2182b... 「週の管理」の対応
		
		try (PreparedStatement stmt = this.connection().prepareStatement(GET_WORKPLACE.toString())) {
			stmt.setInt(1, startDate);
			stmt.setInt(2, endDate);
			stmt.setString(3, cid);
			NtsResultSet result = new NtsResultSet(stmt.executeQuery());
			int month = this.month();
			result.forEach(i -> {
<<<<<<< HEAD
				datas.addAll(buildWorkPlaceRow(i, legalTimes, startDate, endDate, month));
=======
				datas.addAll(buildWorkPlaceRow(i, month, startOfWeek));
>>>>>>> 5c46af2182b... 「週の管理」の対応
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
<<<<<<< HEAD

	private List<MasterData> buildWorkPlaceRow(NtsResultRecord r, List<KshmtLegalTimeMWkp> legals, int startDate, int endDate, int month) {
=======
	
	private List<MasterData> buildWorkPlaceRow(NtsResultRecord r, int month, String startOfWeek){
>>>>>>> 5c46af2182b... 「週の管理」の対応
		List<MasterData> datas = new ArrayList<>();

		Integer refPreTime = r.getInt("REFERENCE_PRED_TIME");
		
		for (int y = startDate; y <= endDate; y++) {
			String wid = r.getString("WKP_ID");
			int ym = y *100 + month;
			
			val normal = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val defor = legals.stream()
					.filter(l -> {
						return l.pk.ym == ym && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value;
					})
					.findFirst();
			val flex = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			
			datas.add(buildWorkPlaceARow(
					String.valueOf(y),
					//R14_1
					r.getInt("rk2") == 1 ? r.getString("WKPCD") : null,
					//R14_2
					r.getInt("rk2") == 1 ? r.getString("WKP_NAME") : null,
					//R14_3
					//R14_4 R14_5
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"), 
					//R14_6
					convertTime(normal.isPresent() ? normal.get().legalTime : 0),
					//R14_7
					I18NText.getText("KMK004_177"),
					//R14_9
					convertTime(r.getInt("DAILY_TIME")),
					//R14_11
					startOfWeek,
					//R14_12
					getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
					//R14_13
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null, 
					//R14_14
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null,
					//R14_15	
					getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
					//R14_16
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null, 
					//R14_17
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null, 
					//R14_18
					refPreTime == null? null : getFlexType(r.getInt("REFERENCE_PRED_TIME")),
					//R14_19 R14_20
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R14_21
					refPreTime != null && refPreTime == 0 ? (flex.isPresent() ? String.valueOf(flex.get().withinTime) : null) : null,
					// R10_22
					convertTime(flex.isPresent() ? flex.get().legalTime : 0),
					//R14_23
					getAggType(r.getInt("AGGR_METHOD")),
					//R14_24
					r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
					//14_25
					getShortageTime(r.getInt("INSUFFIC_SET")),
					//R14_26 R14_27
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					//R14_28
					convertTime(defor.isPresent() ? defor.get().legalTime : 0),
					//R14_29
					I18NText.getText("KMK004_177"), 
					//R14_31
					convertTime(r.getInt("LAB_DAILY_TIME")),
					//R14_33
					startOfWeek,
					//R14_34 R14_35
					r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"), 
					//R14_36 R14_37
					r.getInt("PERIOD") + I18NText.getText("KMK004_180"), 
					//R14_38
					r.getInt("REPEAT_ATR") == 1 ? "○" : "-",
					//R14_39
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
					//R14_40
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
					//R14_41
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
					//R14_42
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
					//R14_43
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
					//R14_44
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null
					));

			int nextYm = y *100 + month + 1;
			val normalN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val deforN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
					.findFirst();
			val flexN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			// buil arow = month + 1
			datas.add(buildWorkPlaceARow(
					//R14_1
					null,
					//R14_2
					null,
					//R14_3
					null,
					//R14_4 R14_5
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"), 
					//R14_6
					convertTime(normalN.isPresent() ? normalN.get().legalTime : 0),
					//R14_7
					I18NText.getText("KMK004_178"),
					//R14_9
					convertTime(r.getInt("WEEKLY_TIME")),
					//R14_11
					null,
					//R14_12
					null,
					//R14_13
					null, 
					//R14_14
					null,
					//R14_15	
					null,
					//R14_16
					null, 
					//R14_17
					null, 
					//R14_18
					null,
					//R14_19 R14_20
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R14_21
					refPreTime != null && refPreTime == 0 ? (flexN.isPresent() ? String.valueOf(flexN.get().withinTime) : null) : null,
					// R10_22
					convertTime(flexN.isPresent() ? flexN.get().legalTime : 0),
					//R14_23
					null,
					//R14_24
					null,
					//14_25
					null,
					//R14_26 R14_27
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
					//R14_28
					convertTime(deforN.isPresent() ? deforN.get().legalTime : 0),
					//R14_230
					I18NText.getText("KMK004_178"), 
					//R14_32
					convertTime(r.getInt("LAB_WEEKLY_TIME")),
					//R14_33
					null,
					//R14_34 R14_35
					null, 
					//R14_36 R14_37
					null, 
					//R14_38
					null,
					//R14_39
					null,
					//R14_40
					null,
					//R14_41
					null,
					//R14_42
					null,
					//R14_43
					null,
					//R14_44
					null
					));
			
			// buil month remain
			for (int i = month + 1; i <= month + 10; i++) {
				int currentYm = y *100 + month + 1;
				val normalC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
						.findFirst();
				val deforC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
						.findFirst();
				val flexC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.wkpId.equals(wid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
						.findFirst();
				datas.add(buildWorkPlaceARow(
						//R14_1
						null,
						//R14_2
						null,
						//R14_3
						null,
						//R14_4 R14_5
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"), 
						//R14_6
						convertTime(normalC.isPresent() ? normalC.get().legalTime : 0),
						//R14_7
						null,
						//R14_9
						null,
						//R14_11
						null,
						//R14_12
						null,
						//R14_13
						null, 
						//R14_14
						null,
						//R14_15	
						null,
						//R14_16
						null, 
						//R14_17
						null, 
						//R14_18
						null,
						//R14_19 R14_20
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						//R14_21
						refPreTime != null && refPreTime == 0 ? (flexC.isPresent() ? String.valueOf(flexC.get().withinTime) : null) : null,
						// R10_22
						convertTime(flexC.isPresent() ? flexC.get().legalTime : 0),
						//R14_23
						null,
						//R14_24
						null,
						//14_25
						null,
						//R14_26 R14_27
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						//R14_28
						convertTime(deforC.isPresent() ? deforC.get().legalTime : 0),
						//R14_29
						null, 
						//R14_31
						null,
						//R14_33
						null,
						//R14_34 R14_35
						null, 
						//R14_36 R14_37
						null, 
						//R14_38
						null,
						//R14_39
						null,
						//R14_40
						null,
						//R14_41
						null,
						//R14_42
						null,
						//R14_43
						null,
						//R14_44
						null
						));
			}
		}
		return datas;
	}
	
	private MasterData buildWorkPlaceARow(
			String value1, String value2, String value3, String value4, String value5,
			String value6, String value7, String value8, String value9, String value10,
			String value11, String value12, String value13, String value14, String value15,
			String value16, String value17, String value18, String value19, String value20,
			String value21, String value22, String value23, String value24, String value25,
			String value26, String value27, String value28, String value29, String value30,
			String value31, String value32, String value33, String value34, String value35) {
		
		Map<String, MasterCellData> data = new HashMap<>();
		//R14_1
		data.put(WorkPlaceColumn.KMK004_154, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_154)
                .value(value1)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_2
		data.put(WorkPlaceColumn.KMK004_187, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_187)
                .value(value2)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_3
		data.put(WorkPlaceColumn.KMK004_188, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_188)
                .value(value3)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_4
		data.put(WorkPlaceColumn.KMK004_155, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_155)
                .value(value4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_6
		data.put(WorkPlaceColumn.KMK004_156, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_156)
                .value(value5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_9
		data.put(WorkPlaceColumn.KMK004_157, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_157)
                .value(value6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_10
		data.put(WorkPlaceColumn.KMK004_156_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_156_1)
                .value(value7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_11
		data.put(WorkPlaceColumn.KMK004_158, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_158)
                .value(value8)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_12
		data.put(WorkPlaceColumn.KMK004_159, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_159)
                .value(value9)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_13
		data.put(WorkPlaceColumn.KMK004_160, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_160)
                .value(value10)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_14
		data.put(WorkPlaceColumn.KMK004_161, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_161)
                .value(value11)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_15
		data.put(WorkPlaceColumn.KMK004_162, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_162)
                .value(value12)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_16
		data.put(WorkPlaceColumn.KMK004_163, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_163)
                .value(value13)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_17
		data.put(WorkPlaceColumn.KMK004_164, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_164)
                .value(value14)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_18
		data.put(WorkPlaceColumn.KMK004_165, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_165)
                .value(value15)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_19
		data.put(WorkPlaceColumn.KMK004_166, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_166)
                .value(value16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_21
		data.put(WorkPlaceColumn.KMK004_167, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_167)
                .value(value17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_22
		data.put(WorkPlaceColumn.KMK004_156_2, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_156_2)
                .value(value18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_23
		data.put(WorkPlaceColumn.KMK004_168, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_168)
                .value(value19)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_24
		data.put(WorkPlaceColumn.KMK004_169, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_169)
                .value(value20)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_25
		data.put(WorkPlaceColumn.KMK004_170, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_170)
                .value(value21)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_26
		data.put(WorkPlaceColumn.KMK004_171, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_171)
                .value(value22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_28
		data.put(WorkPlaceColumn.KMK004_156_3, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_156_3)
                .value(value23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_31
		data.put(WorkPlaceColumn.KMK004_172, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_172)
                .value(value24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_32
		data.put(WorkPlaceColumn.KMK004_156_4, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_156_4)
                .value(value25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_33
		data.put(WorkPlaceColumn.KMK004_158_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_158_1)
                .value(value26)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_34
		data.put(WorkPlaceColumn.KMK004_173, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_173)
                .value(value27)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_36
		data.put(WorkPlaceColumn.KMK004_174, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_174)
                .value(value28)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		//R14_38
		data.put(WorkPlaceColumn.KMK004_175, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_175)
                .value(value29)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_39
		data.put(WorkPlaceColumn.KMK004_159_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_159_1)
                .value(value30)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_40
		data.put(WorkPlaceColumn.KMK004_160_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_160_1)
                .value(value31)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_41
		data.put(WorkPlaceColumn.KMK004_161_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_161_1)
                .value(value32)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_42
		data.put(WorkPlaceColumn.KMK004_162_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_162_1)
                .value(value33)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_43
		data.put(WorkPlaceColumn.KMK004_163_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_163_1)
                .value(value34)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		//R14_44
		data.put(WorkPlaceColumn.KMK004_164_1, MasterCellData.builder()
                .columnId(WorkPlaceColumn.KMK004_164_1)
                .value(value35)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		return MasterData.builder().rowData(data).build();
	}

	@Override
	public List<MasterData> getEmployeeData(int startDate, int endDate) {
		String cid = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();
		
<<<<<<< HEAD
		val legalTimes = this.queryProxy().query(LEGAL_TIME_SYA, KshmtLegalTimeMSya.class)
			.setParameter("cid", cid)
			.setParameter("start", startDate * 100 + 1)
			.setParameter("end", endDate * 100 + 12)
			.getList();
=======
		String startOfWeek = getStartOfWeek(cid);
>>>>>>> 5c46af2182b... 「週の管理」の対応
		
		try (PreparedStatement stmt = this.connection().prepareStatement(GET_EMPLOYEE.toString())) {
			stmt.setInt(1, startDate);
			stmt.setInt(2, endDate);
			stmt.setString(3, cid);
			NtsResultSet result = new NtsResultSet(stmt.executeQuery());
			int month = this.month();
			result.forEach(i -> {
<<<<<<< HEAD
				datas.addAll(buildEmployeeRow(i, legalTimes, startDate, endDate, month));
=======
				datas.addAll(buildEmployeeRow(i, month, startOfWeek));
>>>>>>> 5c46af2182b... 「週の管理」の対応
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
<<<<<<< HEAD
	private List<MasterData> buildEmployeeRow(NtsResultRecord r, List<KshmtLegalTimeMSya> legals, int startDate, int endDate, int month) {
		List<MasterData> datas = new ArrayList<>();

		Integer refPreTime = r.getInt("REFERENCE_PRED_TIME");
		
		for (int y = startDate; y <= endDate; y++) {
			String sid = r.getString("SID");
			int ym = y *100 + month;
			
			val normal = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val defor = legals.stream()
					.filter(l -> {
						return l.pk.ym == ym && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value;
					})
					.findFirst();
			val flex = legals.stream()
					.filter(l -> l.pk.ym == ym && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			
=======
	private List<MasterData> buildEmployeeRow(NtsResultRecord r, int month, String startOfWeek) {
		List<MasterData> datas = new ArrayList<>();
		String refPreTime = r.getString("REFERENCE_PRED_TIME");
			datas.add(buildEmployeeARow(
				r.getString("YEAR"),
				// R10_1
				r.getInt("ROW_NUMBER") == 1 ? r.getString("SCD") : null,
				// R10_2
				r.getInt("ROW_NUMBER") == 1 ? r.getString("BUSINESS_NAME") : null,
				// R10_3
				// R10_4 R10_5
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				// R10_6
				convertTime(r.getInt(("S" + ((month - 1) % 12 + 1)))),
				// R10_7
				I18NText.getText("KMK004_177"),
				// R10_9
				convertTime(r.getInt("DAILY_TIME")),
				// R10_11
				startOfWeek,
				// R10_12
				getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
				// R10_13
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null,
				// R10_14
				r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null,
				// R10_15
				getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
				// R10_16
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null,
				// R10_17
				r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
				// R10_18
				refPreTime == null? null :getFlexType(r.getInt("REFERENCE_PRED_TIME")),
				// R10_19 R10_20
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				// R10_21
				refPreTime != null && r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("T" + ((month - 1) % 12 + 1)))) : null,
				// R10_22
				convertTime(r.getInt(("F" + ((month - 1) % 12 + 1)))),
				// R10_23
				getAggType(r.getInt("AGGR_METHOD")),
				// R10_24
				r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
				// R10_25
				getShortageTime(r.getInt("INSUFFIC_SET")),
				// R10_26 R10_27
				((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
				// R10_28
				convertTime(r.getInt(("M" + ((month - 1) % 12 + 1)))),
				// R10_29
				I18NText.getText("KMK004_177"),
				// R10_31
				convertTime(r.getInt("TRANS_DAILY_TIME")),
				// R10_33
				startOfWeek,
				// R10_34 35
				r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"),
				// R10_36 37
				r.getInt("PERIOD") + I18NText.getText("KMK004_180"),
				// R10_38
				r.getInt("REPEAT_ATR") == 1 ? "○" : "-",
				// R14_39
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
				// R14_40
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
				// R14_41
				r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
				// R14_42
				getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
				// R14_43
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
				// R14_44
				r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null));
		// buil arow = month + 1
			datas.add(buildEmployeeARow(
				// R10_1
				null,
				// R10_2
				null,
				// R10_3
				null,
				// R10_4 R10_5
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_6
				convertTime(r.getInt(("S" + ((month - 1) % 12 + 2)))),
				// R10_8
				I18NText.getText("KMK004_178"),
				// R10_9
				convertTime(r.getInt("WEEKLY_TIME")),
				// R10_11
				null,
				// R10_12
				null,
				// R10_13
				null,
				// R10_14
				null,
				// R10_15
				null,
				// R10_16
				null,
				// R10_17
				null,
				// R10_18
				null,
				// R10_19 R10_20
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_21
				refPreTime != null && r.getInt("REFERENCE_PRED_TIME") == 0 ? convertTime(r.getInt(("T" + ((month - 1) % 12 + 2)))) : null,
				// R10_22
				convertTime(r.getInt(("F" + ((month - 1) % 12 + 2)))),
				// R10_23
				null,
				// R10_24
				null,
				// R10_25
				null,
				// R10_26 R10_27
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_28
				convertTime(r.getInt(("M" + ((month - 1) % 12 + 2)))),
				// R10_30
				I18NText.getText("KMK004_178"),
				// R10_32
				convertTime(r.getInt("TRANS_WEEKLY_TIME")),
				// R10_33
				null,
				// R10_34 35
				null,
				// R10_36 37
				null,
				// R10_38
				null,
				// R14_39
				null,
				// R14_40
				null,
				// R14_41
				null,
				// R14_42
				null,
				// R14_43
				null,
				// R14_44
				null));
		// buil month remain
		for (int i = 1; i < 11; i++) {
>>>>>>> 5c46af2182b... 「週の管理」の対応
			datas.add(buildEmployeeARow(
					String.valueOf(y),
					// R10_1
					r.getInt("ROW_NUMBER") == 1 ? r.getString("SCD") : null,
					// R10_2
					r.getInt("ROW_NUMBER") == 1 ? r.getString("BUSINESS_NAME") : null,
					// R10_3
					// R10_4 R10_5
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					// R10_6
					convertTime(normal.isPresent() ? normal.get().legalTime : 0),
					// R10_7
					I18NText.getText("KMK004_177"),
					// R10_9
					convertTime(r.getInt("DAILY_TIME")),
					// R10_11
					getWeekStart(r.getInt("WEEK_STR")),
					// R10_12
					getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
					// R10_13
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null,
					// R10_14
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null,
					// R10_15
					getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
					// R10_16
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null,
					// R10_17
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
					// R10_18
					refPreTime == null? null :getFlexType(refPreTime),
					// R10_19 R10_20
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					// R10_21
					refPreTime != null && refPreTime == 0 ? (flex.isPresent() ? String.valueOf(flex.get().withinTime) : null) : null,
					// R10_22
					convertTime(flex.isPresent() ? flex.get().legalTime : 0),
					// R10_23
					getAggType(r.getInt("AGGR_METHOD")),
					// R10_24
					r.getInt("AGGR_METHOD") == 0 ? getInclude(r.getInt("INCLUDE_OT")) : null,
					// R10_25
					getShortageTime(r.getInt("INSUFFIC_SET")),
					// R10_26 R10_27
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_176"),
					// R10_28
					convertTime(defor.isPresent() ? defor.get().legalTime : 0),
					// R10_29
					I18NText.getText("KMK004_177"),
					// R10_31
					convertTime(r.getInt("TRANS_DAILY_TIME")),
					// R10_33
					getWeekStart(r.getInt("TRANS_WEEK_STR")),
					// R10_34 35
					r.getInt("STR_MONTH") + I18NText.getText("KMK004_179"),
					// R10_36 37
					r.getInt("PERIOD") + I18NText.getText("KMK004_180"),
					// R10_38
					r.getInt("REPEAT_ATR") == 1 ? "○" : "-",
					// R14_39
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
					// R14_40
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
					// R14_41
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
					// R14_42
					getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
					// R14_43
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
					// R14_44
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")) : null));

			int nextYm = y *100 + month + 1;
			val normalN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
					.findFirst();
			val deforN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
					.findFirst();
			val flexN = legals.stream()
					.filter(l -> l.pk.ym == nextYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
					.findFirst();
			// buil arow = month + 1
			datas.add(buildEmployeeARow(
				// R10_4 R10_5
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_6
				convertTime(normalN.isPresent() ? normalN.get().legalTime : 0),
				// R10_8
				I18NText.getText("KMK004_178"),
				// R10_9
				convertTime(r.getInt("WEEKLY_TIME")),
				// R10_19 R10_20
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_21
				refPreTime != null && refPreTime == 0 ? (flexN.isPresent() ? String.valueOf(flexN.get().withinTime) : null) : null,
				// R10_22
				convertTime(flexN.isPresent() ? flexN.get().legalTime : 0),
				// R10_26 R10_27
				((month - 1) % 12 + 2) + I18NText.getText("KMK004_176"),
				// R10_28
				convertTime(deforN.isPresent() ? deforN.get().legalTime : 0),
				// R10_30
				I18NText.getText("KMK004_178"),
				// R10_32
				convertTime(r.getInt("TRANS_WEEKLY_TIME"))));
			
			// buil month remain
			for (int i = month + 1; i <= month + 10; i++) {
				int currentYm = y *100 + month + 1;
				val normalC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.REGULAR_LABOR.value)
						.findFirst();
				val deforC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.DEFOR_LABOR.value)
						.findFirst();
				val flexC = legals.stream()
						.filter(l -> l.pk.ym == currentYm && l.pk.sid.equals(sid) && l.pk.type == LaborWorkTypeAttr.FLEX.value)
						.findFirst();
				datas.add(buildEmployeeARow(
						// R10_1
						null,
						// R10_2
						null,
						// R10_3
						null,
						// R10_4 R10_5
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						// R10_6
						convertTime(normalC.isPresent() ? normalC.get().legalTime : 0),
						// R10_8
						null,
						// R10_9
						null,
						// R10_11
						null,
						// R10_12
						null,
						// R10_13
						null,
						// R10_14
						null,
						// R10_15
						null,
						// R10_16
						null,
						// R10_17
						null,
						// R10_18
						null,
						// R10_19 R10_20
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						// R10_21
						refPreTime != null && refPreTime == 0 ? (flexC.isPresent() ? String.valueOf(flexC.get().withinTime) : null) : null,
								// R10_22
						convertTime(flexC.isPresent() ? flexC.get().legalTime : 0),
						// R10_23
						null,
						// R10_24
						null,
						// R10_25
						null,
						// R10_26 R10_27
						((month + i) % 12 + 1) + I18NText.getText("KMK004_176"),
						// R10_28
						convertTime(deforC.isPresent() ? deforC.get().legalTime : 0),
						// R10_30
						null,
						// R10_32
						null,
						// R10_33
						null,
						// R10_34 35
						null,
						// R10_36 37
						null,
						// R10_38
						null,
						// R14_39
						null,
						// R14_40
						null,
						// R14_41
						null,
						// R14_42
						null,
						// R14_43
						null,
						// R14_44
						null));
			}
		}
		return datas;
	}
	
	private MasterData buildEmployeeARow(
			String value1, String value2, String value3, String value4, String value5,
			String value6, String value7, String value8, String value9, String value10,
			String value11, String value12, String value13, String value14, String value15,
			String value16, String value17, String value18, String value19, String value20,
			String value21, String value22, String value23, String value24, String value25,
			String value26, String value27, String value28, String value29, String value30,
			String value31, String value32, String value33, String value34, String value35) {
		
		Map<String, MasterCellData> data = new HashMap<>();
		
		data.put(EmployeeColumn.KMK004_154, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_154)
                .value(value1)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_183, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_183)
                .value(value2)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_184, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_184)
                .value(value3)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_155, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_155)
                .value(value4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156)
                .value(value5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_157, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_157)
                .value(value6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_1)
                .value(value7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_158, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_158)
                .value(value8)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_159, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_159)
                .value(value9)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_160, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_160)
                .value(value10)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_161, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_161)
                .value(value11)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_162, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_162)
                .value(value12)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_163, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_163)
                .value(value13)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_164, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_164)
                .value(value14)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_165, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_165)
                .value(value15)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_166, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_166)
                .value(value16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_167, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_167)
                .value(value17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_2, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_2)
                .value(value18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_168, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_168)
                .value(value19)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_169, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_169)
                .value(value20)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_170, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_170)
                .value(value21)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_171, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_171)
                .value(value22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_3, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_3)
                .value(value23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_172, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_172)
                .value(value24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_4, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_4)
                .value(value25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_158_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_158_1)
                .value(value26)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_173, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_173)
                .value(value27)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_174, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_174)
                .value(value28)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_175, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_175)
                .value(value29)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_159_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_159_1)
                .value(value30)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_160_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_160_1)
                .value(value31)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_161_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_161_1)
                .value(value32)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_162_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_162_1)
                .value(value33)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_163_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_163_1)
                .value(value34)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_164_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_164_1)
                .value(value35)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		
		return MasterData.builder().rowData(data).build();
	}
	
	private MasterData buildEmployeeARow(String value4, String value5,
			String value6, String value7, String value16, String value17, 
			String value18, String value22, String value23, String value24, String value25) {
		
		Map<String, MasterCellData> data = new HashMap<>();
		
		data.put(EmployeeColumn.KMK004_154, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_154)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_183, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_183)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_184, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_184)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_155, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_155)
                .value(value4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156)
                .value(value5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_157, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_157)
                .value(value6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_1)
                .value(value7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_158, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_158)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_159, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_159)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_160, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_160)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_161, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_161)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_162, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_162)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_163, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_163)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_164, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_164)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_165, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_165)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_166, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_166)
                .value(value16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_167, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_167)
                .value(value17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_2, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_2)
                .value(value18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_168, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_168)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_169, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_169)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_170, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_170)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_171, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_171)
                .value(value22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_3, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_3)
                .value(value23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_172, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_172)
                .value(value24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_156_4, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_156_4)
                .value(value25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_158_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_158_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_173, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_173)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_174, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_174)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
		data.put(EmployeeColumn.KMK004_175, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_175)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_159_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_159_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_160_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_160_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_161_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_161_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_162_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_162_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_163_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_163_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		data.put(EmployeeColumn.KMK004_164_1, MasterCellData.builder()
                .columnId(EmployeeColumn.KMK004_164_1)
                .value(null)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		
		return MasterData.builder().rowData(data).build();
	}

	private String convertTime(int pTime) {
		String time =  String.format("%d:%02d", pTime / 60, pTime % 60);
		return time;
	}
	
	private String getWeekStart(int weekStart) {
		String weekStartType = null;
		WeekStart type = EnumAdaptor.valueOf(weekStart, WeekStart.class);
		switch (type) {
			case Monday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Monday");
				break;
			case Tuesday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Tuesday");
				break;
			case Wednesday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Wednesday");
				break;
			case Thursday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Thursday");
				break;
			case Friday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Friday");
				break;
			case Saturday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Saturday");
				break;
			case Sunday:
				weekStartType = I18NText.getText("Enum_DayOfWeek_Sunday");
				break;
			case TighteningStartDate:
				weekStartType = "締め開始日";
				break;
			default:
				break;
			}
		return weekStartType;
	}
	
	public static String getExtraType(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_59");
    	case 1:
    		return TextResource.localize("KMK004_58");
    	default: 
    		return null;
    	}
    }
	
	public static String getLegalType(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_64");
    	case 1:
    		return TextResource.localize("KMK004_63");
    	default: 
    		return null;
    	}
    }
	
	public static String getFlexType(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_147");
    	case 1:
    		return TextResource.localize("KMK004_148");
    	default: 
    		return null;
    	}
    }
	
	public static String getAggType(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_181");
    	case 1:
    		return TextResource.localize("KMK004_182");
    	default: 
    		return null;
    	}
    }
	
	public static String getInclude(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_73");
    	case 1:
    		return TextResource.localize("KMK004_72");
    	default: 
    		return null;
    	}
    }
	
	public static String getShortageTime(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_76");
    	case 1:
    		return TextResource.localize("KMK004_77");
    	default: 
    		return null;
    	}
    }
	
	public static String getWeeklySurcharge(int value){
    	switch (value){
    	case 0:
    		return TextResource.localize("KMK004_59");
    	case 1:
    		return TextResource.localize("KMK004_58");
    	default: 
    		return null;
    	}
    }
	
	
	
}
