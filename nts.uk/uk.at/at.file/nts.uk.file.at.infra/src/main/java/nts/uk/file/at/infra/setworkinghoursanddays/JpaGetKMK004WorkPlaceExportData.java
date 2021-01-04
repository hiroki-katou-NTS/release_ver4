package nts.uk.file.at.infra.setworkinghoursanddays;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lombok.val;
import nts.arc.i18n.I18NText;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekRuleManagement;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.workingplace.KshmtLegalTimeMWkp;
import nts.uk.ctx.at.shared.infra.entity.workrule.week.KsrmtWeekRuleMng;
import nts.uk.file.at.app.export.setworkinghoursanddays.GetKMK004WorkPlaceExportRepository;
import nts.uk.file.at.app.export.setworkinghoursanddays.WorkPlaceColumn;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellStyle;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;

/**
 * 
 * @author sonnlb
 *
 */
@Stateless
public class JpaGetKMK004WorkPlaceExportData extends JpaRepository implements GetKMK004WorkPlaceExportRepository {
	
	private static final String GET_EXPORT_MONTH = "SELECT m.MONTH_STR FROM BCMMT_COMPANY m WHERE m.CID = ?cid";
	
	private static final String LEGAL_TIME_WKP = "SELECT s FROM KshmtLegalTimeMWkp s WHERE "
			+ " s.pk.cid = :cid AND s.pk.ym >= :start AND s.pk.ym <= :end"
			+ " ORDER BY s.pk.ym";
	
	
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
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.SETTLE_PERIOD_MON,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.SETTLE_PERIOD,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.START_MONTH AS FLEX_START_MONTH,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.INCLUDE_HDWK,   ");
	     exportSQL.append("             KRCST_WKP_FLEX_M_CAL_SET.LEGAL_AGGR_SET,   ");
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
	     exportSQL.append("             FROM BSYMT_WKP_INFO   ");
	     exportSQL.append("              LEFT JOIN KSHST_WKP_REG_LABOR_TIME ON BSYMT_WKP_INFO.CID = KSHST_WKP_REG_LABOR_TIME.CID   ");
	     exportSQL.append("               AND BSYMT_WKP_INFO.WKP_ID = BSYMT_WKP_INFO.WKP_ID    ");
	     exportSQL.append("              LEFT JOIN KRCST_WKP_DEFOR_M_CAL_SET ON BSYMT_WKP_INFO.CID = KRCST_WKP_DEFOR_M_CAL_SET.CID   ");
	     exportSQL.append("               AND BSYMT_WKP_INFO.WKP_ID = KRCST_WKP_DEFOR_M_CAL_SET.WKP_ID   ");
	     exportSQL.append("              LEFT JOIN KRCST_WKP_FLEX_M_CAL_SET ON BSYMT_WKP_INFO.CID = KRCST_WKP_FLEX_M_CAL_SET.CID   ");
	     exportSQL.append("               AND BSYMT_WKP_INFO.WKP_ID = KRCST_WKP_FLEX_M_CAL_SET.WKP_ID   ");
	     exportSQL.append("              LEFT JOIN KRCST_WKP_REG_M_CAL_SET ON BSYMT_WKP_INFO.CID = KRCST_WKP_REG_M_CAL_SET.CID   ");
	     exportSQL.append("               AND BSYMT_WKP_INFO.WKP_ID = KRCST_WKP_REG_M_CAL_SET.WKPID   ");
	     exportSQL.append("              LEFT JOIN (SELECT *, ROW_NUMBER() OVER ( PARTITION BY CID ORDER BY END_DATE DESC ) AS RN FROM BSYMT_WKP_CONFIG_2) AS BSYMT_WKP_CONFIG  ");
	     exportSQL.append("             ON BSYMT_WKP_INFO.CID = BSYMT_WKP_CONFIG.CID AND BSYMT_WKP_CONFIG.RN = 1  ");
	     exportSQL.append("              LEFT JOIN KSHST_FLX_GET_PRWK_TIME ON BSYMT_WKP_INFO.CID = KSHST_FLX_GET_PRWK_TIME.CID   ");
	     exportSQL.append("              LEFT JOIN KSHST_WKP_TRANS_LAB_TIME ON BSYMT_WKP_INFO.CID = KSHST_WKP_TRANS_LAB_TIME.CID   ");
	     exportSQL.append("              AND BSYMT_WKP_INFO.WKP_ID = KSHST_WKP_TRANS_LAB_TIME.WKP_ID   ");
	     exportSQL.append("             WHERE BSYMT_WKP_INFO.CID = ?  ");
	     exportSQL.append("              AND BSYMT_WKP_INFO.WKP_ID IN (SELECT WKP_ID FROM KSRMT_LEGAL_TIME_M_WKP WHERE CID = ? AND KSRMT_LEGAL_TIME_M_WKP.YM >= ? AND KSRMT_LEGAL_TIME_M_WKP.YM <= ? )   ");
	     exportSQL.append("            ) TBL  ");
	     exportSQL.append("             ORDER BY TBL.HIERARCHY_CD ASC");

	     GET_WORKPLACE = exportSQL.toString();
	  }

	  private String getStartOfWeek(String cid) {
			
			Optional<WeekRuleManagement> startOfWeek = this.queryProxy().find(cid, KsrmtWeekRuleMng.class)
																		.map(w -> w.toDomain());
			
			return KMK004PrintCommon.getWeekStart(startOfWeek);
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
		public List<MasterData> getWorkPlaceExportData(int startDate, int endDate) {
			String cid = AppContexts.user().companyId();
			List<MasterData> datas = new ArrayList<>();
			
			String startOfWeek = getStartOfWeek(cid);
			
			int month = this.month();
			
			int startYM = startDate * 100 + month;
			int endYM = startDate * 100 + ((month + 11) / 12) * 100 + (month + 11) % 12;

			val legalTimes = this.queryProxy().query(LEGAL_TIME_WKP, KshmtLegalTimeMWkp.class)
				.setParameter("cid", cid)
				.setParameter("start", startYM)
				.setParameter("end", endYM)
				.getList();
			
			try (PreparedStatement stmt = this.connection().prepareStatement(GET_WORKPLACE.toString())) {
				stmt.setString(1, cid);
				stmt.setString(2, cid);
				stmt.setInt(3, startYM);
				stmt.setInt(4, endYM);
				NtsResultSet result = new NtsResultSet(stmt.executeQuery());
				result.forEach(i -> {
					datas.addAll(buildWorkPlaceRow(i, legalTimes, startDate, endDate, month, startOfWeek));
				});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return datas;
		}

		private List<MasterData> buildWorkPlaceRow(NtsResultRecord r, List<KshmtLegalTimeMWkp> legals, int startDate, int endDate, int month, String startOfWeek) {
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
						//R12_1
						r.getInt("rk2") == 1 ? r.getString("WKPCD") : null,
						//R12_2
						r.getInt("rk2") == 1 ? r.getString("WKP_NAME") : null,
						//R12_3
						String.valueOf(y),
						//R12_4
						((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"), 
						//R12_5
						KMK004PrintCommon.convertTime(normal.isPresent() ? normal.get().legalTime : null),
						//R12_6
						KMK004PrintCommon.convertTime(r.getInt(("DAILY_TIME"))),
						//R12_7
						KMK004PrintCommon.convertTime(r.getInt(("WEEKLY_TIME"))),
						//R12_8
						KMK004PrintCommon.getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
						//R12_9
						r.getInt("INCLUDE_EXTRA_AGGR")==null ?null: r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null,
						//R12_10
						r.getInt("INCLUDE_EXTRA_AGGR")==null ?null:r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null, 
						//R12_11
						KMK004PrintCommon.getExtraType(r.getInt("INCLUDE_EXTRA_OT")),
						//R12_12
						r.getInt("INCLUDE_EXTRA_OT") == null ?null:r.getInt("INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null, 
						//R12_13		
						r.getInt("INCLUDE_EXTRA_OT") == null ?null:r.getInt("INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
						//R12_14
						KMK004PrintCommon.getFlexType(refPreTime),
						//R12_15
						((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"),
						//R12_16 
						refPreTime == null? null :KMK004PrintCommon.convertTime(flex.isPresent() ? flex.get().withinTime : null),
						//R12_17
						KMK004PrintCommon.convertTime(flex.isPresent() ? flex.get().legalTime : null),
						//R10_18
						KMK004PrintCommon.convertTime(flex.isPresent() ? flex.get().weekAvgTime : null),
						//R12_19
						r.getInt("SETTLE_PERIOD_MON")== null?null: r.getInt("SETTLE_PERIOD_MON") == 2 ? "2ヶ月" : "3ヶ月",
						//R12_20
						KMK004PrintCommon.getSettle(r.getInt("SETTLE_PERIOD")),
						//R12_21
						r.getInt("FLEX_START_MONTH")== null ? null: r.getInt("FLEX_START_MONTH").toString() + "月",
						//R12_22
						KMK004PrintCommon.getShortageTime(r.getInt("INSUFFIC_SET")), 
						//R12_23
						KMK004PrintCommon.getAggType(r.getInt("AGGR_METHOD")),
						//R12_24
						r.getInt("AGGR_METHOD") == null ?null: r.getInt("AGGR_METHOD") == 0 ? KMK004PrintCommon.getInclude(r.getInt("INCLUDE_OT")) : null,
						//R12_25
						KMK004PrintCommon.getInclude(r.getInt("INCLUDE_HDWK")),
						//R12_26
						KMK004PrintCommon.getLegal(r.getInt("LEGAL_AGGR_SET")),
						//R12_27
						((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"),
						//R12_28
						KMK004PrintCommon.convertTime(defor.isPresent() ? defor.get().legalTime : null),
						//R12_29
						KMK004PrintCommon.convertTime(r.getInt(("LAB_DAILY_TIME"))), 
						//R12_30
						KMK004PrintCommon.convertTime(r.getInt(("LAB_WEEKLY_TIME"))),
						//R12_31
						r.getInt("STR_MONTH")== null ?null: r.getInt("STR_MONTH") + I18NText.getText("KMK004_402"),
						//R12_32
						r.getInt("PERIOD")== null ?null:r.getInt("PERIOD") + I18NText.getText("KMK004_403"),
						//R12_33
						r.getInt("REPEAT_ATR") ==null ?null:r.getInt("REPEAT_ATR") == 1 ? "○" : "-",
						//R12_34
						KMK004PrintCommon.getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
						//R12_35
						r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")==null ?null:r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
						//R12_36
						r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")==null ?null:r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
						//R12_37
						KMK004PrintCommon.getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
						//R12_38
						r.getInt("DEFOR_INCLUDE_EXTRA_OT")==null?null:r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
						//E12_39
						r.getInt("DEFOR_INCLUDE_EXTRA_OT")==null?null:r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")): null
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
				datas.add(buildWorkPlaceARow(//R14_1
						 null,
						//R14_2
						null,
						//R14_3
						null,
						//R14_4
						((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"), 
						//R14_5
						KMK004PrintCommon.convertTime(normalN.isPresent() ? normalN.get().legalTime : null),
						//R14_6
						null,
						//R14_7
						null,
						//R14_8
						null,
						//R14_9
						null, 
						//R14_10
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
						((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"),
						//R14_16 
						refPreTime == null? null :KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().withinTime : null),
						//R14_17
						KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().legalTime : null),
						//R10_18
						KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().weekAvgTime : null),
						//R14_19
						null,
						//R14_20
						null,
						//R14_21
						null,
						//R14_22
						null, 
						//R14_23
						null,
						//R14_24
						null,
						//R14_25
						null,
						//R14_26
						null,
						//R14_27
						((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"),
						//R14_28
						KMK004PrintCommon.convertTime(deforN.isPresent() ? deforN.get().legalTime : null),
						//R14_29
						null, 
						//R14_30
						null,
						//R14_31
						null,
						//R14_32
						null,
						//R14_33
						null,
						//R14_34
						null,
						//R14_35
						null,
						//R14_36
						null,
						//R14_37
						null,
						//R14_38
						null,
						//R14_39
						null
						));
				
				// buil month remain
				for (int i = 1; i < 11; i++) {
					int m = (month + i) % 12 + 1;
					int currentYm = y * 100 + ((month + i) / 12) * 100 + m;
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
							//R12_1
						 null,
						//R12_2
						null,
						//R12_3
						null,
						//R12_4
						(m) + I18NText.getText("KMK004_401"), 
						//R12_5
						KMK004PrintCommon.convertTime(normalC.isPresent() ? normalC.get().legalTime : null),
						//R12_6
						null,
						//R12_7
						null,
						//R12_8
						null,
						//R12_9
						null, 
						//R12_10
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
						(m) + I18NText.getText("KMK004_401"),
						//R12_16 
						refPreTime == null? null :KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().withinTime : null),
						//R12_17
						KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().legalTime : null),
						//R10_18
						KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().weekAvgTime : null),
						//R12_19
						null,
						//R12_20
						null,
						//R12_21
						null,
						//R12_22
						null, 
						//R12_23
						null,
						//R12_24
						null,
						//R12_25
						null,
						//R12_26
						null,
						//R12_27
						(m) + I18NText.getText("KMK004_401"),
						//R12_28
						KMK004PrintCommon.convertTime(deforC.isPresent() ? deforC.get().legalTime : null),
						//R12_29
						null, 
						//R12_30
						null,
						//R12_31
						null,
						//R12_32
						null,
						//R12_33
						null,
						//R12_34
						null,
						//R12_35
						null,
						//R12_36
						null,
						//R12_37
						null,
						//R12_38
						null,
						//R12_39
						null
							));
				}
			}
			return datas;
		}
		
		public static MasterData buildWorkPlaceARow(
				String R14_1, String R14_2, String R14_3, String R14_4, String R14_5,
				String R14_6, String R14_7, String R14_8, String R14_9, String R14_10,
				String R14_11, String R14_12, String R14_13, String R14_14, String R14_15,
				String R14_16, String R14_17, String R14_18, String R14_19, String R14_20,
				String R14_21, String R14_22, String R14_23, String R14_24, String R14_25,
				String R14_26, String R14_27, String R14_28, String R14_29, String R14_30,
				String R14_31, String R14_32, String R14_33, String R14_34, String R14_35, 
				String R14_36, String R14_37,String R14_38, String R14_39) {
			
			Map<String, MasterCellData> data = new HashMap<>();
			data.put(WorkPlaceColumn.KMK004_187, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_187)
	            .value(R14_1)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	       data.put(WorkPlaceColumn.KMK004_188, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_188)
	            .value(R14_2)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
			data.put(WorkPlaceColumn.KMK004_372, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_372)
	            .value(R14_3)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_373, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_373)
	            .value(R14_4)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_374, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_374)
	            .value(R14_5)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_375, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_375)
	            .value(R14_6)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_376, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_376)
	            .value(R14_7)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_377, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_377)
	            .value(R14_8)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_378, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_378)
	            .value(R14_9)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_379, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_379)
	            .value(R14_10)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_380, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_380)
	            .value(R14_11)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_381, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_381)
	            .value(R14_12)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_382, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_382)
	            .value(R14_13)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_383, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_383)
	            .value(R14_14)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_384, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_384)
	            .value(R14_15)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_385, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_385)
	            .value(R14_16)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_386, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_386)
	            .value(R14_17)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_387, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_387)
	            .value(R14_18)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_388, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_388)
	            .value(R14_19)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_389, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_389)
	            .value(R14_20)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_390, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_390)
	            .value(R14_21)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_391, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_391)
	            .value(R14_22)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_392, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_392)
	            .value(R14_23)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_393, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_393)
	            .value(R14_24)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_394, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_394)
	            .value(R14_25)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_395, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_395)
	            .value(R14_26)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_396, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_396)
	            .value(R14_27)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_397, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_397)
	            .value(R14_28)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_375_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_375_1)
	            .value(R14_29)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_376_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_376_1)
	            .value(R14_30)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_398, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_398)
	            .value(R14_31)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_399, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_399)
	            .value(R14_32)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_400, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_400)
	            .value(R14_33)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_377_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_377)
	            .value(R14_34)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_378_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_378_1)
	            .value(R14_35)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_379_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_379_1)
	            .value(R14_36)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_380_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_380_1)
	            .value(R14_37)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_381_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_381_1)
	            .value(R14_38)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
	        data.put(WorkPlaceColumn.KMK004_382_1, MasterCellData.builder()
	            .columnId(WorkPlaceColumn.KMK004_382_1)
	            .value(R14_39)
	            .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
	            .build());
			return MasterData.builder().rowData(data).build();
		}

}
