package nts.uk.file.at.infra.worktype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.file.at.app.export.worktype.ApprovalFunctionConfigExportImpl;
import nts.uk.file.at.app.export.worktype.ApprovalFunctionConfigRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterCellStyle;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;

@Stateless
public class JpaApprovalFunctionConfigRepository extends JpaRepository implements ApprovalFunctionConfigRepository {

	private static final String SELECT_APPROVAL_FUNCTION_CONFIG = "SELECT  "
			+ "	CASE WHEN TEMP.ROW_NUMBER = 1 THEN TEMP.CODE ELSE NULL END, "
			+ "	CASE WHEN TEMP.ROW_NUMBER = 1 THEN TEMP.NAME ELSE NULL END, "
			+ "	CASE WHEN TEMP.APP_TYPE = 0 THEN ?appType0 "
			+ "		 WHEN TEMP.APP_TYPE = 1 THEN ?appType1 "
			+ "		 WHEN TEMP.APP_TYPE = 2 THEN ?appType2 "
			+ "		 WHEN TEMP.APP_TYPE = 3 THEN ?appType3 "
			+ "		 WHEN TEMP.APP_TYPE = 4 THEN ?appType4 "
			+ "		 WHEN TEMP.APP_TYPE = 6 THEN ?appType6 "
			+ "		 WHEN TEMP.APP_TYPE = 7 THEN ?appType7 "
			+ "		 WHEN TEMP.APP_TYPE = 8 THEN ?appType8 "
			+ "		 WHEN TEMP.APP_TYPE = 9 THEN ?appType9 "
			+ "		 WHEN TEMP.APP_TYPE = 10 THEN ?appType10 "
			+ "		 WHEN TEMP.APP_TYPE = 14 THEN ?appType14 "
			+ "		 ELSE NULL "
			+ "	END, "
			+ "	CASE WHEN TEMP.USE_ATR = 0 THEN ?useText "
			+ "		 WHEN TEMP.USE_ATR = 1 THEN ?notUseText "
			+ "		 ELSE NULL "
			+ "	END, "
			+ "	CASE WHEN (TEMP.APP_TYPE != 0 AND TEMP.APP_TYPE != 6) OR TEMP.USE_ATR != 1 THEN NULL "
			+ "		 WHEN TEMP.REQUIRED_INSTRUCTION_FLG = 0 THEN ?notRequiredInstruction "
			+ "		 WHEN TEMP.REQUIRED_INSTRUCTION_FLG = 1 THEN ?requiredInstruction "
			+ "		 ELSE NULL "
			+ "	END REQUIRED_INSTRUCTION_FLG, "
			+ "	CASE WHEN TEMP.APP_TYPE = 0 AND TEMP.OT_APP_SETTING_FLG = 0 THEN ?notPrerequisiteUseAtrText "
			+ "		 WHEN TEMP.APP_TYPE = 0 AND TEMP.OT_APP_SETTING_FLG = 1 THEN ?prerequisiteUseAtrText "
			+ "		 WHEN TEMP.APP_TYPE = 6 AND TEMP.PREREQUISITE_FORPAUSE_FLG = 0 THEN ?notPrerequisiteUseAtrText "
			+ "		 WHEN TEMP.APP_TYPE = 6 AND TEMP.PREREQUISITE_FORPAUSE_FLG = 1 THEN ?prerequisiteUseAtrText "
			+ "		 ELSE NULL "
			+ "	END OT_APP_SETTING_FLG, "
			+ "	CASE WHEN TEMP.APP_TYPE != 0 AND TEMP.APP_TYPE != 6 AND TEMP.APP_TYPE != 8 THEN NULL "
			+ "		 WHEN TEMP.TIME_CAL_USE_ATR = 0 THEN ?timeCalNotUseText "
			+ "		 WHEN TEMP.TIME_CAL_USE_ATR = 1 THEN ?timeCalUseText "
			+ "		 ELSE NULL "
			+ "	END TIME_CAL_USE_ATR, "
			+ "	CASE WHEN (TEMP.APP_TYPE != 0 AND TEMP.APP_TYPE != 6) OR TEMP.TIME_CAL_USE_ATR != 1 THEN NULL "
			+ "		 WHEN TEMP.BREAK_INPUTFIELD_DIS_FLG = 0 THEN ?breakInputfieldNotDisp "
			+ "		 WHEN TEMP.BREAK_INPUTFIELD_DIS_FLG = 1 THEN ?breakInputfieldDisp "
			+ "		 ELSE NULL "
			+ "	END BREAK_INPUTFIELD_DIS_FLG, "
			+ "	CASE WHEN (TEMP.APP_TYPE != 0 AND TEMP.APP_TYPE != 6) OR TEMP.TIME_CAL_USE_ATR != 1 THEN NULL "
			+ "		 WHEN TEMP.GOOUT_TIME_BEGIN_DIS_FLG = 0 THEN ?gooutTimeBeginNotDisp "
			+ "		 WHEN TEMP.GOOUT_TIME_BEGIN_DIS_FLG = 1 THEN ?gooutTimeBeginDisp "
			+ "		 ELSE NULL "
			+ "	END GOOUT_TIME_BEGIN_DIS_FLG, "
			+ "	CASE WHEN (TEMP.APP_TYPE != 0 AND TEMP.APP_TYPE != 6) OR TEMP.TIME_CAL_USE_ATR != 1 THEN NULL "
			+ "		 WHEN TEMP.ATWORK_TIME_BEGIN_DIS_FLG = 0 THEN ?atWorkAtr0 "
			+ "		 WHEN TEMP.ATWORK_TIME_BEGIN_DIS_FLG = 1 THEN ?atWorkAtr1 "
			+ "		 WHEN TEMP.ATWORK_TIME_BEGIN_DIS_FLG = 2 THEN ?atWorkAtr2 "
			+ "		 WHEN TEMP.ATWORK_TIME_BEGIN_DIS_FLG = 3 THEN ?atWorkAtr3 "
			+ "		 ELSE NULL "
			+ "	END ATWORK_TIME_BEGIN_DIS_FLG, "
			+ "	CASE WHEN TEMP.APP_TYPE = 0 AND TEMP.TIME_INPUT_USE_ATR = 0 THEN ?notOvertimeHoursText "
			+ "		 WHEN TEMP.APP_TYPE = 0 AND TEMP.TIME_INPUT_USE_ATR = 1 THEN ?overtimeHoursText "
			+ "		 ELSE NULL "
			+ "	END OVERTIME_HOURS, "
			+ "	CASE WHEN TEMP.APP_TYPE = 6 AND TEMP.TIME_INPUT_USE_ATR = 0 THEN ?notbreakTimeText "
			+ "		 WHEN TEMP.APP_TYPE = 6 AND TEMP.TIME_INPUT_USE_ATR = 1 THEN ?breakTimeText "
			+ "		 ELSE NULL "
			+ "	END REST_TIME, "
			+ "	CASE WHEN TEMP.APP_TYPE = 9 AND TEMP.LATE_OR_LEAVE_APP_CANCEL_FLG = 0 THEN ?notLateOrLeaveAppCancelText "
			+ "		 WHEN TEMP.APP_TYPE = 9 AND TEMP.LATE_OR_LEAVE_APP_CANCEL_FLG = 1 THEN ?lateOrLeaveAppCancelText "
			+ "		 ELSE NULL "
			+ "	END LATE_OR_LEAVE_APP_CANCEL_FLG, "
			+ "	CASE WHEN TEMP.APP_TYPE = 9 AND TEMP.LATE_OR_LEAVE_APP_SETTING_FLG = 0 THEN ?notLateOrLeaveAppSettingText "
			+ "		 WHEN TEMP.APP_TYPE = 9 AND TEMP.LATE_OR_LEAVE_APP_SETTING_FLG = 1 THEN ?lateOrLeaveAppSettingText "
			+ "		 ELSE NULL "
			+ "	END LATE_OR_LEAVE_APP_SETTING_FLG, "
			+ "	TEMP.MEMO "
			+ "FROM "
			+ "(SELECT  "
			+ "	NULL AS CODE, "
			+ "	?companyText AS NAME, "
			+ "	APP_TYPE, "
			+ "	USE_ATR, "
			+ "	REQUIRED_INSTRUCTION_FLG, "
			+ "	OT_APP_SETTING_FLG, "
			+ "	PREREQUISITE_FORPAUSE_FLG, "
			+ "	TIME_CAL_USE_ATR, "
			+ "	BREAK_INPUTFIELD_DIS_FLG, "
			+ "	GOOUT_TIME_BEGIN_DIS_FLG, "
			+ "	ATWORK_TIME_BEGIN_DIS_FLG, "
			+ "	TIME_INPUT_USE_ATR, "
			+ "	LATE_OR_LEAVE_APP_CANCEL_FLG, "
			+ "	LATE_OR_LEAVE_APP_SETTING_FLG, "
			+ "	MEMO,  "
			+ "	0 AS NUM_ORDER,  "
			+ "	ROW_NUMBER() OVER (PARTITION BY CID ORDER BY CID, APP_TYPE) AS ROW_NUMBER  "
			+ "FROM KRQST_COM_APP_CF_DETAIL "
			+ "WHERE CID = ?cid "
			+ "UNION ALL "
			+ "SELECT "
			+ "	ISNULL(WPI.WKPCD, ?masterUnregistered) AS CODE, "
			+ "	ISNULL(WPI.WKP_NAME, ?masterUnregistered) AS NAME, "
			+ "	WP.APP_TYPE, "
			+ "	WP.USE_ATR, "
			+ "	WP.REQUIRED_INSTRUCTION_FLG, "
			+ "	WP.OT_APP_SETTING_FLG, "
			+ "	WP.PREREQUISITE_FORPAUSE_FLG, "
			+ "	WP.TIME_CAL_USE_ATR, "
			+ "	WP.BREAK_INPUTFIELD_DIS_FLG, "
			+ "	WP.GOOUT_TIME_BEGIN_DIS_FLG, "
			+ "	WP.ATWORK_TIME_BEGIN_DIS_FLG, "
			+ "	WP.TIME_INPUT_USE_ATR, "
			+ "	WP.LATE_OR_LEAVE_APP_CANCEL_FLG, "
			+ "	WP.LATE_OR_LEAVE_APP_SETTING_FLG, "
			+ "	WP.MEMO, "
			+ "	WP.NUM_ORDER, "
			+ "	WP.ROW_NUMBER "
			+ "FROM "
			+ "	(SELECT *, ROW_NUMBER() OVER (PARTITION BY CID ORDER BY CID, WKP_ID, APP_TYPE) AS NUM_ORDER, ROW_NUMBER() OVER (PARTITION BY CID, WKP_ID ORDER BY CID, WKP_ID, APP_TYPE) AS ROW_NUMBER FROM KRQST_WP_APP_CF_DETAIL) WP  "
			+ "	LEFT JOIN BSYMT_WORKPLACE_HIST WPH ON WP.CID = WPH.CID AND WP.WKP_ID = WPH.WKPID AND WPH.END_DATE = '9999-Dec-31' "
			+ "	LEFT JOIN BSYMT_WORKPLACE_INFO WPI ON WP.CID = WPI.CID AND WP.WKP_ID = WPI.WKPID AND WPH.HIST_ID = WPI.HIST_ID "
			+ "WHERE "
			+ "	WP.CID = ?cid) TEMP "
			+ "	ORDER BY TEMP.NUM_ORDER, TEMP.ROW_NUMBER";

	@Override
	public List<MasterData> getAllApprovalFunctionConfig(String cid) {
		Query queryString = getEntityManager().createNativeQuery(SELECT_APPROVAL_FUNCTION_CONFIG)
				.setParameter("companyText", TextResource.localize("KAF022_649"))
				.setParameter("cid", cid)
				.setParameter("masterUnregistered", TextResource.localize("Enum_MasterUnregistered"))
				.setParameter("appType0", TextResource.localize("KAF022_3"))
				.setParameter("appType1", TextResource.localize("KAF022_4"))
				.setParameter("appType2", TextResource.localize("KAF022_5"))
				.setParameter("appType3", TextResource.localize("KAF022_6"))
				.setParameter("appType4", TextResource.localize("KAF022_7"))
				.setParameter("appType6", TextResource.localize("KAF022_8"))
				.setParameter("appType7", TextResource.localize("KAF022_11"))
				.setParameter("appType8", TextResource.localize("KAF022_9"))
				.setParameter("appType9", TextResource.localize("KAF022_286"))
				.setParameter("appType10", TextResource.localize("KAF022_12"))
				.setParameter("appType14", TextResource.localize("KAF022_13"))
				.setParameter("useText", TextResource.localize("KAF022_100"))
				.setParameter("notUseText", TextResource.localize("KAF022_101"))
				.setParameter("requiredInstruction", "○")
				.setParameter("notRequiredInstruction", "-")
				.setParameter("notPrerequisiteUseAtrText", TextResource.localize("KAF022_291"))
				.setParameter("prerequisiteUseAtrText", TextResource.localize("KAF022_292"))
				.setParameter("timeCalUseText", TextResource.localize("KAF022_295"))
				.setParameter("timeCalNotUseText", TextResource.localize("KAF022_296"))
				.setParameter("breakInputfieldDisp", "○")
				.setParameter("breakInputfieldNotDisp", "-")
				.setParameter("gooutTimeBeginDisp", "○")
				.setParameter("gooutTimeBeginNotDisp", "-")
				.setParameter("atWorkAtr0", TextResource.localize("KAF022_37"))
				.setParameter("atWorkAtr1", TextResource.localize("KAF022_301"))
				.setParameter("atWorkAtr2", TextResource.localize("KAF022_302"))
				.setParameter("atWorkAtr3", TextResource.localize("KAF022_303"))
				.setParameter("overtimeHoursText", TextResource.localize("KAF022_305"))
				.setParameter("notOvertimeHoursText", TextResource.localize("KAF022_306"))
				.setParameter("breakTimeText", TextResource.localize("KAF022_308"))
				.setParameter("notbreakTimeText", TextResource.localize("KAF022_309"))
				.setParameter("lateOrLeaveAppCancelText", TextResource.localize("KAF022_311"))
				.setParameter("notLateOrLeaveAppCancelText", TextResource.localize("KAF022_312"))
				.setParameter("lateOrLeaveAppSettingText", TextResource.localize("KAF022_313"))
				.setParameter("notLateOrLeaveAppSettingText", TextResource.localize("KAF022_314"));
		List<Object[]> resultQuery = queryString.getResultList();
		List<MasterData> result = new ArrayList<MasterData>();
		for (Object[] obj : resultQuery) {
			result.add(toData(obj));
		}
		return result;
	}

	private MasterData toData(Object[] r) {
		Map<String, MasterCellData> data = new HashMap<>();
		data.put(ApprovalFunctionConfigExportImpl.KAF022_635,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_635).value(r[0])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_636,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_636).value(r[1])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_637,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_637).value(r[2])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_638,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_638).value(r[3])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_639,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_639).value(r[4])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_640,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_640).value(r[5])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_641,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_641).value(r[6])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_642,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_642).value(r[7])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_643,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_643).value(r[8])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_644,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_644).value(r[9])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_645,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_645).value(r[10])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_646,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_646).value(r[11])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_647,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_647).value(r[12])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_648,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_648).value(r[13])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		data.put(ApprovalFunctionConfigExportImpl.KAF022_649,
				MasterCellData.builder().columnId(ApprovalFunctionConfigExportImpl.KAF022_649).value(r[14])
						.style(MasterCellStyle.build().horizontalAlign(ColumnTextAlign.LEFT)).build());
		return MasterData.builder().rowData(data).build();
	}

}
