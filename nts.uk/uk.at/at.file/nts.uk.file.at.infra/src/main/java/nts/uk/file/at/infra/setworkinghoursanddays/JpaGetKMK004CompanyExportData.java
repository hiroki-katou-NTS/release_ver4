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
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.company.KshmtLegalTimeMCom;
import nts.uk.ctx.at.shared.infra.entity.workrule.week.KsrmtWeekRuleMng;
import nts.uk.file.at.app.export.setworkinghoursanddays.CompanyColumn;
import nts.uk.file.at.app.export.setworkinghoursanddays.GetKMK004CompanyExportRepository;
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
public class JpaGetKMK004CompanyExportData extends JpaRepository implements GetKMK004CompanyExportRepository {

	private static final String LEGAL_TIME_COM = "SELECT s FROM KshmtLegalTimeMCom s WHERE "
			+ " s.pk.cid = :cid AND s.pk.ym >= :start AND s.pk.ym <= :end" + " ORDER BY s.pk.ym";
	
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
					+" KRCST_COM_FLEX_M_CAL_SET.INCLUDE_HDWK, "
					+" KRCST_COM_FLEX_M_CAL_SET.LEGAL_AGGR_SET, "
					+" KRCST_COM_FLEX_M_CAL_SET.INSUFFIC_SET, "
					+" KRCST_COM_FLEX_M_CAL_SET.SETTLE_PERIOD_MON, "
					+" KRCST_COM_FLEX_M_CAL_SET.SETTLE_PERIOD, "
					+" KRCST_COM_FLEX_M_CAL_SET.START_MONTH AS FLEX_START_MONTH, "
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

	@Override
	public List<MasterData> getCompanyExportData(int startDate, int endDate) {
		String cid = AppContexts.user().companyId();
		List<MasterData> datas = new ArrayList<>();

		String startOfWeek = getStartOfWeek(cid);

		val legalTimes = this.queryProxy().query(LEGAL_TIME_COM, KshmtLegalTimeMCom.class).setParameter("cid", cid)
				.setParameter("start", startDate * 100 + 1).setParameter("end", endDate * 100 + 12).getList();

		try (PreparedStatement stmt = this.connection().prepareStatement(GET_EXPORT_EXCEL.toString())) {
//			stmt.setInt(1, startDate);
//			stmt.setInt(2, endDate);
			stmt.setString(1, cid);
			NtsResultSet result = new NtsResultSet(stmt.executeQuery());
			int month = this.month();
			result.forEach(i -> {
				datas.addAll(buildCompanyRow(i, legalTimes, startDate, endDate, month, startOfWeek));
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datas;
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
		List<?> data = monthQuery.getResultList();
		if (data.size() == 0) {
			month = 1;
		} else {
			month = Integer.valueOf(data.get(0).toString());
		}
		return month;
	}
	
	private List<MasterData> buildCompanyRow(NtsResultRecord r, List<KshmtLegalTimeMCom> legals, int startDate, int endDate, int month, String startOfWeek) {
		List<MasterData> datas = new ArrayList<>();

		Integer refPreTime = r.getInt("REFERENCE_PRED_TIME");
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
					//R8_3
					String.valueOf(y), 
					//R8_4
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"),
					//R8_5
					KMK004PrintCommon.convertTime(normal.isPresent() ? normal.get().legalTime : 0),
					//R8_6
					KMK004PrintCommon.convertTime(r.getInt(("DAILY_TIME"))),
					//R8_7
					KMK004PrintCommon.convertTime(r.getInt(("WEEKLY_TIME"))),
					//R8_8
					KMK004PrintCommon.getExtraType(r.getInt("INCLUDE_EXTRA_AGGR")),
					//R8_9
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_LEGAL_AGGR")) : null,
					//R8_10
					r.getInt("INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_HOLIDAY_AGGR")) : null,
					//R8_11
					KMK004PrintCommon.getExtraType(r.getInt("INCLUDE_EXTRA_OT")),		
					//R8_12
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_LEGAL_OT")) : null,
					//R8_13
					r.getInt("INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("INCLUDE_HOLIDAY_OT")) : null,
					//R8_14
					KMK004PrintCommon.getFlexType(refPreTime),
					//R8_15
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"),
					//R8_16
					KMK004PrintCommon.convertTime(flex.isPresent() ? flex.get().withinTime : 0),
					//R8_17
					KMK004PrintCommon.convertTime(flex.isPresent() ? flex.get().legalTime : 0),
					//R8_18
					KMK004PrintCommon.convertTime(flex.isPresent() ?flex.get().weekAvgTime : 0),
					//R8_19
					r.getInt("SETTLE_PERIOD_MON") == 2 ? "2ヶ月" : "3ヶ月",
					//R8_20
					KMK004PrintCommon.getSettle(r.getInt("SETTLE_PERIOD")),
					//R8_21
					r.getInt("FLEX_START_MONTH").toString() + "月",
					//R8_22
					KMK004PrintCommon.getShortageTime(r.getInt("INSUFFIC_SET")),
					//R8_23
					KMK004PrintCommon.getAggType(r.getInt("AGGR_METHOD")),
					//R8_24
					r.getInt("AGGR_METHOD") == 0 ? KMK004PrintCommon.getInclude(r.getInt("INCLUDE_OT")) : null,
					//R8_25
					KMK004PrintCommon.getInclude(r.getInt("INCLUDE_HDWK")),
					//R8_26
					KMK004PrintCommon.getLegal(r.getInt("LEGAL_AGGR_SET")),
					//R8_27		
					((month - 1) % 12 + 1) + I18NText.getText("KMK004_401"),
					//R8_28
					KMK004PrintCommon.convertTime(defor.isPresent() ? defor.get().legalTime : 0),
					//R8_29
					KMK004PrintCommon.convertTime(r.getInt("REG_DAILY_TIME")),
					//R8_30
					KMK004PrintCommon.convertTime(r.getInt("REG_WEEKLY_TIME")), 
					//R8_31
					r.getInt("STR_MONTH") + I18NText.getText("KMK004_402"),
					//R8_32
					r.getInt("PERIOD") + I18NText.getText("KMK004_403"),
					//R8_33
					r.getInt("REPEAT_ATR") == 1 ? "○" : "-",
					//R8_34
					KMK004PrintCommon.getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_AGGR")),
					//R8_35
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_AGGR")) : null,
					//R8_36
					r.getInt("DEFOR_INCLUDE_EXTRA_AGGR") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_AGGR")) : null,
					// R8_37
					KMK004PrintCommon.getWeeklySurcharge(r.getInt("DEFOR_INCLUDE_EXTRA_OT")),
					// R8_38
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_LEGAL_OT")) : null,
					// R8_39
					r.getInt("DEFOR_INCLUDE_EXTRA_OT") != 0 ? KMK004PrintCommon.getLegalType(r.getInt("DEFOR_INCLUDE_HOLIDAY_OT")): null
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
			datas.add(buildARow(
					//R8_3
					null, 
					//R8_4
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"),
					//R8_5
					KMK004PrintCommon.convertTime(normalN.isPresent() ? normalN.get().legalTime : 0),
					//R8_6
					null,
					//R8_7
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
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"),
					//R8_16
					KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().withinTime : 0),
					//R8_17
					KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().legalTime : 0),
					//R8_18
					KMK004PrintCommon.convertTime(flexN.isPresent() ? flexN.get().weekAvgTime : 0),
					//R8_19
					null,
					//R8_20
					null,
					//R8_21
					null,
					//R8_22
					null,
					//R8_23
					null,
					//R8_24
					null,
					//R8_25
					null,
					//R8_26
					null,
					//R8_27		
					((month - 1) % 12 + 2) + I18NText.getText("KMK004_401"),
					//R8_28
					KMK004PrintCommon.convertTime(deforN.isPresent() ? deforN.get().legalTime : 0),
					//R8_29
					null,
					//R8_30
					null, 
					//R8_31
					null,
					//R8_32
					null,
					//R8_33
					null,
					//R8_34
					null,
					//R8_35
					null,
					//R8_36
					null,
					// R8_37
					null,
					// R8_38
					null,
					// R8_39
					null));
			
			// buil month remain
			for (int i = 1; i < 11; i++) {
				int m = (month + i) % 12 + 1;
				int currentYm = y *100 + m;
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
						//R8_3
						null, 
						//R8_4
						(m) + I18NText.getText("KMK004_401"),
						//R8_5
						KMK004PrintCommon.convertTime(normalC.isPresent() ? normalC.get().legalTime : 0),
						//R8_6
						null,
						//R8_7
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
						(m) + I18NText.getText("KMK004_401"),
						//R8_16
						KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().withinTime : 0),
						//R8_17
						KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().legalTime : 0),
						//R8_18
						KMK004PrintCommon.convertTime(flexC.isPresent() ? flexC.get().weekAvgTime : 0),
						//R8_19
						null,
						//R8_20
						null,
						//R8_21
						null,
						//R8_22
						null,
						//R8_23
						null,
						//R8_24
						null,
						//R8_25
						null,
						//R8_26
						null,
						//R8_27		
						(m) + I18NText.getText("KMK004_401"),
						//R8_28
						KMK004PrintCommon.convertTime(deforC.isPresent() ? deforC.get().legalTime : 0),
						//R8_29
						null,
						//R8_30
						null, 
						//R8_31
						null,
						//R8_32
						null,
						//R8_33
						null,
						//R8_34
						null,
						//R8_35
						null,
						//R8_36
						null,
						// R8_37
						null,
						// R8_38
						null,
						// R8_39
						null
						));
			}
		}
		return datas;
	}
	
	private MasterData buildARow(
			String r8_3, String r8_4, String r8_5,
			String r8_6, String r8_7, String r8_8, String r8_9, String r8_10,
			String r8_11, String r8_12, String r8_13, String r8_14, String r8_15,
			String r8_16, String r8_17, String r8_18, String r8_19, String r8_20,
			String r8_21, String r8_22, String r8_23, String r8_24, String r8_25,
			String r8_26, String r8_27, String r8_28, String r8_29, String r8_30,
			String r8_31, String r8_32, String r8_33, String r8_34, String r8_35, 
			String r8_36, String r8_37,String r8_38, String r8_39) {
		
		Map<String, MasterCellData> data = new HashMap<>();
		 data.put(CompanyColumn.KMK004_372, MasterCellData.builder()
                 .columnId(CompanyColumn.KMK004_372)
                 .value(r8_3)
                 .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                 .build());
            data.put(CompanyColumn.KMK004_373, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_373)
                .value(r8_4)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_374, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_374)
                .value(r8_5)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_375, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_375)
                .value(r8_6)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_376, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_376)
                .value(r8_7)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_377, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_377)
                .value(r8_8)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_378, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_378)
                .value(r8_9)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_379, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_379)
                .value(r8_10)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_380, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_380)
                .value(r8_11)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_381, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_381)
                .value(r8_12)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_382, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_382)
                .value(r8_13)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_383, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_383)
                .value(r8_14)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_384, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_384)
                .value(r8_15)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_385, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_385)
                .value(r8_16)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_386, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_386)
                .value(r8_17)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_387, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_387)
                .value(r8_18)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_388, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_388)
                .value(r8_19)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_389, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_389)
                .value(r8_20)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_390, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_390)
                .value(r8_21)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_391, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_391)
                .value(r8_22)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_392, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_392)
                .value(r8_23)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_393, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_393)
                .value(r8_24)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_394, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_394)
                .value(r8_25)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_395, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_395)
                .value(r8_26)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_396, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_396)
                .value(r8_27)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_397, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_397)
                .value(r8_28)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_375_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_375_1)
                .value(r8_29)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_376_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_376_1)
                .value(r8_30)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.RIGHT))
                .build());
            data.put(CompanyColumn.KMK004_398, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_398)
                .value(r8_31)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_399, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_399)
                .value(r8_32)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_400, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_400)
                .value(r8_33)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_377_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_377)
                .value(r8_34)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_378_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_378_1)
                .value(r8_35)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_379_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_379_1)
                .value(r8_36)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_380_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_380_1)
                .value(r8_37)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_381_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_381_1)
                .value(r8_38)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
            data.put(CompanyColumn.KMK004_382_1, MasterCellData.builder()
                .columnId(CompanyColumn.KMK004_382_1)
                .value(r8_39)
                .style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT))
                .build());
		return MasterData.builder().rowData(data).build();
	}
}
