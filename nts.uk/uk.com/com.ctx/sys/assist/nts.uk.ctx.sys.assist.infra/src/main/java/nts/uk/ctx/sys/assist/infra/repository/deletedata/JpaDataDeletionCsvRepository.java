/**
 * 
 */
package nts.uk.ctx.sys.assist.infra.repository.deletedata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import com.google.common.base.Strings;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.HistoryDiviSion;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionCsvRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.TableDeletionDataCsv;
import nts.uk.ctx.sys.assist.dom.saveprotetion.SaveProtetion;
import nts.uk.ctx.sys.assist.dom.saveprotetion.SaveProtetionRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author hiep.th
 *
 */
@Stateless
public class JpaDataDeletionCsvRepository extends JpaRepository implements DataDeletionCsvRepository {
	private static final StringBuilder SELECT_TABLE_DEL_DATA_SQL = new StringBuilder(
			 "SELECT a.sspdtManualSetDeletionPK.delId, a.supplementExplanation, ")
			.append("a.startDateOfDaily, a.endDateOfDaily, a.startMonthOfMonthly, a.endMonthOfMonthly, ")
			.append("a.startYearOfMonthly, a.endYearOfMonthly, a.companyID, ")
			.append("b.delType, b.delCode, b.delName, c.categoryId, c.categoryName, c.timeStore, c.recoveryStorageRange, ")
			.append("c.otherCompanyCls, e.tableJapanName, e.tableEnglishName, ")
			.append("e.historyCls, e.defaultCondKeyQuery, e.fieldKeyQuery1, e.fieldKeyQuery2, ")
			.append("e.fieldKeyQuery3, e.fieldKeyQuery4, e.fieldKeyQuery5, e.fieldKeyQuery6, ")
			.append("e.fieldKeyQuery7, e.fieldKeyQuery8, e.fieldKeyQuery9, e.fieldKeyQuery10, e.clsKeyQuery1,")
			.append("e.clsKeyQuery2, e.clsKeyQuery3, e.clsKeyQuery4, e.clsKeyQuery5, e.clsKeyQuery6, ")
			.append("e.clsKeyQuery7, e.clsKeyQuery8, e.clsKeyQuery9, e.clsKeyQuery10, e.filedKeyUpdate1, ")
			.append("e.filedKeyUpdate2, e.filedKeyUpdate3, e.filedKeyUpdate4, e.filedKeyUpdate5, ")
			.append("e.filedKeyUpdate6, e.filedKeyUpdate7, e.filedKeyUpdate8, e.filedKeyUpdate9, ")
			.append("e.filedKeyUpdate10, e.filedKeyUpdate11, e.filedKeyUpdate12, e.filedKeyUpdate13, ")
			.append("e.filedKeyUpdate14, e.filedKeyUpdate15, e.filedKeyUpdate16, e.filedKeyUpdate17, ")
			.append("e.filedKeyUpdate18, e.filedKeyUpdate19, e.filedKeyUpdate20, e.fieldDate1, e.fieldDate2, ")
			.append("e.fieldDate3, e.fieldDate4, e.fieldDate5, e.fieldDate6, e.fieldDate7, e.fieldDate8, ")
			.append("e.fieldDate9, e.fieldDate10, e.fieldDate11, e.fieldDate12, e.fieldDate13, e.fieldDate14, ")
			.append("e.fieldDate15, e.fieldDate16, e.fieldDate17, e.fieldDate18, e.fieldDate19, e.fieldDate20, ")
			.append("e.hasParentTblFlg, e.parentTblName, e.parentTblJpName, e.fieldAcqDateTime, ")
			.append("e.fieldAcqEndDate, e.fieldAcqEmployeeId, e.fieldAcqStartDate, e.fieldAcqCid, e.fieldParent1, ")
			.append("e.fieldParent2, e.fieldParent3, e.fieldParent4, e.fieldParent5, e.fieldParent6, e.fieldParent7, ")
			.append("e.fieldParent8, e.fieldParent9, e.fieldParent10, e.fieldChild1, e.fieldChild2, e.fieldChild3, ")
			.append("e.fieldChild4, e.fieldChild5, e.fieldChild6, e.fieldChild7, e.fieldChild8, e.fieldChild9,e.fieldChild10,e.categoryFieldMtPk.tableNo ")
			.append("FROM SspdtManualSetDeletion a, SspdtResultDeletion b, SspmtCategoryForDelete c, SspdtCategoryDeletion d, SspmtCategoryFieldMtForDelete e ")
			.append("WHERE a.sspdtManualSetDeletionPK.delId = b.sspdtResultDeletionPK.delId AND a.sspdtManualSetDeletionPK.delId = d.sspdtCategoryDeletionPK.delId AND c.categoryId = e.categoryFieldMtPk.categoryId AND d.sspdtCategoryDeletionPK.categoryId = c.categoryId AND a.sspdtManualSetDeletionPK.delId = :delId ");
	
	private static final String SELECT_COLUMN_NAME_SQL = "select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS"
			+ " where TABLE_NAME = ?tableName";
	private static final String COMPANY_CD = "0";
	private static final String EMPLOYEE_CD = "5";
	private static final String YEAR = "6";
	private static final String YEAR_MONTH = "7";
	private static final String YEAR_MONTH_DAY = "8";
	
	@Inject
	private SaveProtetionRepository saveProtetionRepo;
	
	@Override
	public List<TableDeletionDataCsv> getTableDelDataCsvById(String delId) {
		List<Object[]> listTemp = this.queryProxy().query(SELECT_TABLE_DEL_DATA_SQL.toString(), Object[].class)
				.setParameter("delId", delId).getList();

		if (listTemp == null || listTemp.isEmpty()) {
			return Collections.emptyList();
		}
		return listTemp.stream().map(i -> createDomainFromEntity(i)).collect(Collectors.toList());
	}

	/**
	 * @param i
	 * @return
	 */
	private TableDeletionDataCsv createDomainFromEntity(Object[] i) {
		String delId = String.valueOf(i[0]);
		String supplementExplanation = String.valueOf(i[1]);
		String startDateOfDaily = String.valueOf(i[2]);
		String endDateOfDaily = String.valueOf(i[3]);
		String startMonthOfMonthly = String.valueOf(i[4]);
		String endMonthOfMonthly = String.valueOf(i[5]);
		String startYearOfMonthly = String.valueOf(i[6]);
		String endYearOfMonthly = String.valueOf(i[7]);
		String companyId = String.valueOf(i[8]);
		
		int delType = Integer.parseInt(String.valueOf(i[9]));
		String delCode = String.valueOf(i[10]);
		String delName = String.valueOf(i[11]);
//		int saveForInvest = Integer.parseInt(String.valueOf(i[12]));
		String categoryId = String.valueOf(i[12]);
		String categoryName = String.valueOf(i[13]);
		int timeStore = Integer.parseInt(String.valueOf(i[14]));
		int recoveryStorageRange = Integer.parseInt(String.valueOf(i[15]));
		int otherCompanyCls = Integer.parseInt(String.valueOf(i[16]));
		String tableJapanName = String.valueOf(i[17]);
		String tableEnglishName = String.valueOf(i[18]);
		int historyCls = Integer.parseInt(String.valueOf(i[19]));
		String defaultCondKeyQuery = String.valueOf(i[20]  == null ? "" : i[20]);
		String fieldKeyQuery1 = String.valueOf(i[21] == null ? "" : i[21]);
		String fieldKeyQuery2 = String.valueOf(i[22] == null ? "" : i[22]);
		String fieldKeyQuery3 = String.valueOf(i[23] == null ? "" : i[23]);
		String fieldKeyQuery4 = String.valueOf(i[24] == null ? "" : i[24]);
		String fieldKeyQuery5 = String.valueOf(i[25] == null ? "" : i[25]);
		String fieldKeyQuery6 = String.valueOf(i[26] == null ? "" : i[26]);
		String fieldKeyQuery7 = String.valueOf(i[27] == null ? "" : i[27]);
		String fieldKeyQuery8 = String.valueOf(i[28] == null ? "" : i[28]);
		String fieldKeyQuery9 = String.valueOf(i[29] == null ? "" : i[29]);
		String fieldKeyQuery10 = String.valueOf(i[30] == null ? "" : i[30]);
		String clsKeyQuery1 = String.valueOf(i[31] == null ? "" : i[31]);
		String clsKeyQuery2 = String.valueOf(i[32] == null ? "" : i[32]);
		String clsKeyQuery3 = String.valueOf(i[33] == null ? "" : i[33]);
		String clsKeyQuery4 = String.valueOf(i[34] == null ? "" : i[34]);
		String clsKeyQuery5 = String.valueOf(i[35] == null ? "" : i[35]);
		String clsKeyQuery6 = String.valueOf(i[36] == null ? "" : i[36]);
		String clsKeyQuery7 = String.valueOf(i[37] == null ? "" : i[37]);
		String clsKeyQuery8 = String.valueOf(i[38] == null ? "" : i[38]);
		String clsKeyQuery9 = String.valueOf(i[39] == null ? "" : i[39]);
		String clsKeyQuery10 = String.valueOf(i[40] == null ? "" : i[40]);
		String filedKeyUpdate1 = String.valueOf(i[41] == null ? "" : i[41]);
		String filedKeyUpdate2 = String.valueOf(i[42] == null ? "" : i[42]);
		String filedKeyUpdate3 = String.valueOf(i[43] == null ? "" : i[43]);
		String filedKeyUpdate4 = String.valueOf(i[44] == null ? "" : i[44]);
		String filedKeyUpdate5 = String.valueOf(i[45] == null ? "" : i[45]);
		String filedKeyUpdate6 = String.valueOf(i[46] == null ? "" : i[46]);
		String filedKeyUpdate7 = String.valueOf(i[47] == null ? "" : i[47]);
		String filedKeyUpdate8 = String.valueOf(i[48] == null ? "" : i[48]);
		String filedKeyUpdate9 = String.valueOf(i[49] == null ? "" : i[49]);
		String filedKeyUpdate10 = String.valueOf(i[50] == null ? "" : i[50]);
		String filedKeyUpdate11 = String.valueOf(i[51] == null ? "" : i[51]);
		String filedKeyUpdate12 = String.valueOf(i[52] == null ? "" : i[52]);
		String filedKeyUpdate13 = String.valueOf(i[53] == null ? "" : i[53]);
		String filedKeyUpdate14 = String.valueOf(i[54] == null ? "" : i[54]);
		String filedKeyUpdate15 = String.valueOf(i[55] == null ? "" : i[55]);
		String filedKeyUpdate16 = String.valueOf(i[56] == null ? "" : i[56]);
		String filedKeyUpdate17 = String.valueOf(i[57] == null ? "" : i[57]);
		String filedKeyUpdate18 = String.valueOf(i[58] == null ? "" : i[58]);
		String filedKeyUpdate19 = String.valueOf(i[59] == null ? "" : i[59]);
		String filedKeyUpdate20 = String.valueOf(i[60] == null ? "" : i[60]);
		String fieldDate1 = String.valueOf(i[61] == null ? "" : i[61]);
		String fieldDate2 = String.valueOf(i[62] == null ? "" : i[62]);
		String fieldDate3 = String.valueOf(i[63] == null ? "" : i[63]);
		String fieldDate4 = String.valueOf(i[64] == null ? "" : i[64]);
		String fieldDate5 = String.valueOf(i[65] == null ? "" : i[65]);
		String fieldDate6 = String.valueOf(i[66] == null ? "" : i[66]);
		String fieldDate7 = String.valueOf(i[67] == null ? "" : i[67]);
		String fieldDate8 = String.valueOf(i[68] == null ? "" : i[68]);
		String fieldDate9 = String.valueOf(i[69] == null ? "" : i[69]);
		String fieldDate10 = String.valueOf(i[70] == null ? "" : i[70]);
		String fieldDate11 = String.valueOf(i[71] == null ? "" : i[71]);
		String fieldDate12 = String.valueOf(i[72] == null ? "" : i[72]);
		String fieldDate13 = String.valueOf(i[73] == null ? "" : i[73]);
		String fieldDate14 = String.valueOf(i[74] == null ? "" : i[74]);
		String fieldDate15 = String.valueOf(i[75] == null ? "" : i[75]);
		String fieldDate16 = String.valueOf(i[76] == null ? "" : i[76]);
		String fieldDate17 = String.valueOf(i[77] == null ? "" : i[77]);
		String fieldDate18 = String.valueOf(i[78] == null ? "" : i[78]);
		String fieldDate19 = String.valueOf(i[79] == null ? "" : i[79]);
		String fieldDate20 = String.valueOf(i[80] == null ? "" : i[80]);
		int hasParentTblFlg = Integer.parseInt(String.valueOf(i[81]));
		
		String parentTblName = String.valueOf(i[82]== null ? "" : i[82]);
		String parentTblJapanName = String.valueOf(i[83]== null ? "" : i[83]);
		
		String fieldAcqCid = String.valueOf(i[88] == null ? "" : i[88]);
		String fieldAcqEmployeeId = String.valueOf(i[86]== null ? "" : i[86]);
		String fieldAcqDateTime = String.valueOf(i[84]== null ? "" : i[84]);
		String fieldAcqStartDate = String.valueOf(i[87]== null ? "" : i[87]);
		String fieldAcqEndDate = String.valueOf(i[85]== null ? "" : i[85]);




		String fieldParent1 = String.valueOf(i[89] == null ? "" : i[89]);
		String fieldParent2 = String.valueOf(i[90] == null ? "" : i[90]);
		String fieldParent3 = String.valueOf(i[91] == null ? "" : i[91]);
		String fieldParent4 = String.valueOf(i[92] == null ? "" : i[92]);
		String fieldParent5 = String.valueOf(i[93] == null ? "" : i[93]);
		String fieldParent6 = String.valueOf(i[94] == null ? "" : i[94]);
		String fieldParent7 = String.valueOf(i[95] == null ? "" : i[95]);
		String fieldParent8 = String.valueOf(i[96] == null ? "" : i[96]);
		String fieldParent9 = String.valueOf(i[97] == null ? "" : i[97]);
		String fieldParent10 = String.valueOf(i[98] == null ? "" : i[98]);
		String fieldChild1 = String.valueOf(i[99] == null ? "" : i[99]);
		String fieldChild2 = String.valueOf(i[100] == null ? "" : i[100]);
		String fieldChild3 = String.valueOf(i[101] == null ? "" : i[101]);
		String fieldChild4 = String.valueOf(i[102] == null ? "" : i[102]);
		String fieldChild5 = String.valueOf(i[103] == null ? "" : i[103]);
		String fieldChild6 = String.valueOf(i[104] == null ? "" : i[104]);
		String fieldChild7 = String.valueOf(i[105] == null ? "" : i[105]);
		String fieldChild8 = String.valueOf(i[106] == null ? "" : i[106]);
		String fieldChild9 = String.valueOf(i[107] == null ? "" : i[107]);
		String fieldChild10 = String.valueOf(i[108] == null ? "" : i[108]);
		int tableNo = Integer.parseInt(String.valueOf(i[109]));
		String datetimenow = GeneralDateTime.now().toString("yyyyMMddHHmmss");
		String compressedFileName = companyId + delName + datetimenow;
		String internalFileName = companyId + categoryName  + tableJapanName;
		
		
		

		TableDeletionDataCsv dataDeletionCsv = new TableDeletionDataCsv(delId, delType, delCode, delName,
				supplementExplanation, categoryId, categoryName, timeStore, recoveryStorageRange, 
				otherCompanyCls, tableJapanName, tableEnglishName, historyCls, defaultCondKeyQuery, fieldKeyQuery1,
				fieldKeyQuery2, fieldKeyQuery3, fieldKeyQuery4, fieldKeyQuery5, fieldKeyQuery6, fieldKeyQuery7,
				fieldKeyQuery8, fieldKeyQuery9, fieldKeyQuery10, clsKeyQuery1, clsKeyQuery2, clsKeyQuery3, clsKeyQuery4,
				clsKeyQuery5, clsKeyQuery6, clsKeyQuery7, clsKeyQuery8, clsKeyQuery9, clsKeyQuery10, filedKeyUpdate1,
				filedKeyUpdate2, filedKeyUpdate3, filedKeyUpdate4, filedKeyUpdate5, filedKeyUpdate6, filedKeyUpdate7,
				filedKeyUpdate8, filedKeyUpdate9, filedKeyUpdate10, filedKeyUpdate11, filedKeyUpdate12,
				filedKeyUpdate13, filedKeyUpdate14, filedKeyUpdate15, filedKeyUpdate16, filedKeyUpdate17,
				filedKeyUpdate18, filedKeyUpdate19, filedKeyUpdate20, fieldDate1, fieldDate2, fieldDate3, fieldDate4,
				fieldDate5, fieldDate6, fieldDate7, fieldDate8, fieldDate9, fieldDate10, fieldDate11, fieldDate12,
				fieldDate13, fieldDate14, fieldDate15, fieldDate16, fieldDate17, fieldDate18, fieldDate19, fieldDate20,
				hasParentTblFlg, parentTblName, parentTblJapanName, fieldAcqDateTime, fieldAcqEndDate,
				fieldAcqEmployeeId, fieldAcqStartDate, fieldAcqCid, fieldParent1, fieldParent2, fieldParent3,
				fieldParent4, fieldParent5, fieldParent6, fieldParent7, fieldParent8, fieldParent9, fieldParent10,
				fieldChild1, fieldChild2, fieldChild3, fieldChild4, fieldChild5, fieldChild6, fieldChild7, fieldChild8,
				fieldChild9, fieldChild10, startDateOfDaily, endDateOfDaily, startMonthOfMonthly, endMonthOfMonthly,
				startYearOfMonthly, endYearOfMonthly, companyId , tableNo , compressedFileName , internalFileName);

		return dataDeletionCsv;
	}
	
	

	/**
	 * 
	 */
	@Override
	public List<List<String>> getDataForEachCaegory(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions) {
		
//		Map<String, Object> parrams = new HashMap<>();
//		String sqlStr = buildGetDataForEachCatSql(tableDelData, employeeDeletions, parrams);
//		Query query = this.getEntityManager().createNativeQuery(sqlStr);
//		for(Entry<String, Object> entry : parrams.entrySet()) {
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//		
//		@SuppressWarnings("unchecked")
//		List<Object[]> listTemp = (List<Object[]>)query.getResultList();
//		return listTemp.stream().map(objects -> {
//			List<String> record = new ArrayList<String>();
//			for (Object field : objects) {
//				record.add(String.valueOf(field == null ? "" : field));
//			}
//			return record;
//        }).collect(Collectors.toList());
		List targetEmployeesSid = employeeDeletions.stream().map(emp -> emp.getEmployeeId()).collect(Collectors.toList());
		List<List<String>> result =  this.getDataDynamic(tableDelData, targetEmployeesSid);
		return result;
	}
	
	public List<List<String>> getDataDynamic(TableDeletionDataCsv tableList, List<String> targetEmployeesSid) {
		StringBuffer query = new StringBuffer("");
		// All Column
		List<String> columns = getAllColumnName(tableList.getTableEnglishName());
		// Select
		query.append("SELECT ");
		
		query.append(getFieldAcq(columns, tableList.getFieldAcqCid(), "H_CID"));
		query.append(getFieldAcq(columns, tableList.getFieldAcqEmployeeId(), "H_SID"));
		query.append(getFieldAcq(columns, tableList.getFieldAcqDateTime(), "H_DATE"));
		query.append(getFieldAcq(columns, tableList.getFieldAcqStartDate(), "H_DATE_START"));
		query.append(getFieldAcq(columns, tableList.getFieldAcqEndDate(), "H_DATE_END"));

		// All Column
		for (int i = 0; i < columns.size(); i++) {
			query.append(" t.").append(columns.get(i));
			if (i < columns.size() - 1) {
				query.append(",");
			}
		}
		
		// From
		query.append(" FROM ").append(tableList.getTableEnglishName()).append(" t");
		if (tableList.getHasParentTblFlg() == NotUseAtr.USE.value && tableList.getParentTblName().isPresent()) {
			// アルゴリズム「親テーブルをJOINする」を実行する
			query.append(" INNER JOIN ").append(tableList.getParentTblName().get()).append(" p ON ");

			String[] parentFields = { tableList.getFieldParent1().orElse(""), tableList.getFieldParent2().orElse(""),
					tableList.getFieldParent3().orElse(""), tableList.getFieldParent4().orElse(""),
					tableList.getFieldParent5().orElse(""), tableList.getFieldParent6().orElse(""),
					tableList.getFieldParent7().orElse(""), tableList.getFieldParent8().orElse(""),
					tableList.getFieldParent9().orElse(""), tableList.getFieldParent10().orElse("") };

			String[] childFields = { tableList.getFieldChild1().orElse(""), tableList.getFieldChild2().orElse(""),
					tableList.getFieldChild3().orElse(""), tableList.getFieldChild4().orElse(""),
					tableList.getFieldChild5().orElse(""), tableList.getFieldChild6().orElse(""),
					tableList.getFieldChild7().orElse(""), tableList.getFieldChild8().orElse(""),
					tableList.getFieldChild9().orElse(""), tableList.getFieldChild10().orElse("") };

			boolean isFirstOnStatement = true;
			for (int i = 0; i < parentFields.length; i++) {
				if (!Strings.isNullOrEmpty(parentFields[i]) && !Strings.isNullOrEmpty(childFields[i])) {
					if (!isFirstOnStatement) {
						query.append(" AND ");
					}
					isFirstOnStatement = false;
					query.append("p." + parentFields[i] + "=" + "t." + childFields[i]);
				}
			}
		}

		String[] fieldKeyQuerys = { tableList.getFieldKeyQuery1().orElse(""), tableList.getFieldKeyQuery2().orElse(""),
				tableList.getFieldKeyQuery3().orElse(""), tableList.getFieldKeyQuery4().orElse(""),
				tableList.getFieldKeyQuery5().orElse(""), tableList.getFieldKeyQuery6().orElse(""),
				tableList.getFieldKeyQuery7().orElse(""), tableList.getFieldKeyQuery8().orElse(""),
				tableList.getFieldKeyQuery9().orElse(""), tableList.getFieldKeyQuery10().orElse("") };
		String[] clsKeyQuerys = { tableList.getClsKeyQuery1().orElse(""), tableList.getClsKeyQuery2().orElse(""),
				tableList.getClsKeyQuery3().orElse(""), tableList.getClsKeyQuery4().orElse(""),
				tableList.getClsKeyQuery5().orElse(""), tableList.getClsKeyQuery6().orElse(""),
				tableList.getClsKeyQuery7().orElse(""), tableList.getClsKeyQuery8().orElse(""),
				tableList.getClsKeyQuery9().orElse(""), tableList.getClsKeyQuery10().orElse("") };
		
		// Where
		query.append(" WHERE 1 = 1 ");

		List<Integer> companyCdIndexs = new ArrayList<Integer>();
		List<Integer> yearIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthDayIndexs = new ArrayList<Integer>();
		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (clsKeyQuerys[i] == "")
				continue;
			switch (clsKeyQuerys[i]) {
			case COMPANY_CD:
				companyCdIndexs.add(i);
				break;

			case YEAR:
				yearIndexs.add(i);
				break;

			case YEAR_MONTH:
				yearMonthIndexs.add(i);
				break;

			case YEAR_MONTH_DAY:
				yearMonthDayIndexs.add(i);
				break;

			default:
				break;
			}
		}

		Map<String, Object> params = new HashMap<>();
		if (companyCdIndexs.size() > 0) {
			query.append(" AND ( ");
			boolean isFirstOrStatement = true;
			for (Integer index : companyCdIndexs) {
				if (!isFirstOrStatement) {
					query.append(" OR ");
				}
				isFirstOrStatement = false;
				if (columns.contains(fieldKeyQuerys[index])) {
					query.append(" t.");
					query.append(fieldKeyQuerys[index]);
				} else {
					query.append(" p.");
					query.append(fieldKeyQuerys[index]);
				}
				query.append(" = ?companyId ");
				params.put("companyId", AppContexts.user().companyId());
			}
			query.append(" ) ");
		}

		// 履歴区分を判別する。履歴なしの場合
		if (tableList.getHistoryCls() == HistoryDiviSion.NO_HISTORY.value) {
			List<Integer> indexs = new ArrayList<Integer>();
			switch (tableList.getTimeStore()) {
			case 3: // ANNUAL
				indexs = yearIndexs;
				break;
			case 2: // MONTHLY
				indexs = yearMonthIndexs;
				break;
			case 1: // DAILY
				indexs = yearMonthDayIndexs;
				break;

			default:
				break;
			}

			if (indexs.size() > 0) {
				indexs.add(99);
				query.append(" AND ( ");
				boolean isFirstOrStatement = true;
				for (int i = 0; i <= indexs.size(); i++) {
					if (!isFirstOrStatement) {
						query.append(" OR ");
					}
					isFirstOrStatement = false;
					if ((i != (indexs.size() - 1)) && (i < indexs.size())) {
						// Start Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i)])) {
							query.append(" (t.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						} else {
							query.append(" (p.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						}

						query.append(" >= ?startDate ");
						query.append(" AND ");

						// End Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i)])) {
							query.append(" t.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						} else {
							query.append(" p.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						}

						query.append(" <= ?endDate) ");
					} else if (i == (indexs.size() - 1)) {
						// fix bug #103051
						// Start Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i - 2)])) {
							query.append(" (t.");
							query.append(fieldKeyQuerys[indexs.get(i - 2)]);
						} else {
							query.append(" (p.");
							query.append(fieldKeyQuerys[indexs.get(i - 2)]);
						}

						query.append(" <= ?startDate ");
						query.append(" AND ");

						// End Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i - 1)])) {
							query.append(" t.");
							query.append(fieldKeyQuerys[indexs.get(i - 1)]);
						} else {
							query.append(" p.");
							query.append(fieldKeyQuerys[indexs.get(i - 1)]);
						}

						isFirstOrStatement = true;

						query.append(" >= ?endDate) ");
					}

					switch (tableList.getTimeStore()) {
					case 1 : //DAILY
						params.put("startDate", tableList.getStartDateOfDaily());
						params.put("endDate", tableList.getEndDateOfDaily());
						break;
					case 2 : //MONTHLY
						params.put("startDate",
								Integer.valueOf(tableList.getStartDateOfDaily().replaceAll("\\/", "")));
						params.put("endDate", Integer.valueOf(tableList.getEndDateOfDaily().replaceAll("\\/", "")));
						break;
					case 3 : // ANNUAL
						params.put("startDate", Integer.valueOf(tableList.getStartDateOfDaily()));
						params.put("endDate", Integer.valueOf(tableList.getEndDateOfDaily()));
						break;

					default:
						break;
					}
				}
				query.append(" ) ");
			}
		}

		// 抽出条件キー固定
		query.append(tableList.getDefaultCondKeyQuery().orElse(""));
		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (EMPLOYEE_CD.equals(clsKeyQuerys[i]) && !targetEmployeesSid.isEmpty()) {
				if (tableList.getHasParentTblFlg() == NotUseAtr.USE.value) {
					query.append(" AND p." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				} else {
					query.append(" AND t." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				}
			}
		}
		
		// Order By
		query.append(" ORDER BY H_CID, H_SID, H_DATE, H_DATE_START");
		String querySql = query.toString();
		List<Object[]> listTemp = new ArrayList<>();
		if(!targetEmployeesSid.isEmpty()) {
			List<String> lSid = new ArrayList<>();
			CollectionUtil.split(targetEmployeesSid, 1000, subIdList -> {
				lSid.add(subIdList.toString().replaceAll("\\[", "\\'").replaceAll("\\]", "\\'").replaceAll(", ","\\', '"));
			});
			for (String sid : lSid) {
				Query queryString = getEntityManager().createNativeQuery(querySql.replaceAll("\\?listTargetSid", sid));
				for (Entry<String, Object> entry : params.entrySet()) {
					queryString.setParameter(entry.getKey(), entry.getValue());
				}
				listTemp.addAll((List<Object[]>) queryString.getResultList());
			}
		}else {
			Query queryString = getEntityManager().createNativeQuery(querySql);
			for (Entry<String, Object> entry : params.entrySet()) {
				queryString.setParameter(entry.getKey(), entry.getValue());
			}
			listTemp.addAll((List<Object[]>) queryString.getResultList());
		}
		return listTemp.stream().map(objects -> {
			List<String> record = new ArrayList<String>();
			for (Object field : objects) {
				record.add(field != null ? String.valueOf(field) : "");
			}
			return record;
		}).collect(Collectors.toList());
	}
	
	public void delelteDataDynamic(TableDeletionDataCsv tableList, List<String> targetEmployeesSid) {
		StringBuffer query = new StringBuffer("");
		// All Column
		List<String> columns = getAllColumnName(tableList.getTableEnglishName());
		// Select
		query.append(" DELETE "  + tableList.getTableEnglishName());
		
		// From
		query.append(" FROM ").append(tableList.getTableEnglishName()).append(" t");
		if (tableList.getHasParentTblFlg() == NotUseAtr.USE.value && tableList.getParentTblName().isPresent()) {
			// アルゴリズム「親テーブルをJOINする」を実行する
			query.append(" INNER JOIN ").append(tableList.getParentTblName().get()).append(" p ON ");

			String[] parentFields = { tableList.getFieldParent1().orElse(""), tableList.getFieldParent2().orElse(""),
					tableList.getFieldParent3().orElse(""), tableList.getFieldParent4().orElse(""),
					tableList.getFieldParent5().orElse(""), tableList.getFieldParent6().orElse(""),
					tableList.getFieldParent7().orElse(""), tableList.getFieldParent8().orElse(""),
					tableList.getFieldParent9().orElse(""), tableList.getFieldParent10().orElse("") };

			String[] childFields = { tableList.getFieldChild1().orElse(""), tableList.getFieldChild2().orElse(""),
					tableList.getFieldChild3().orElse(""), tableList.getFieldChild4().orElse(""),
					tableList.getFieldChild5().orElse(""), tableList.getFieldChild6().orElse(""),
					tableList.getFieldChild7().orElse(""), tableList.getFieldChild8().orElse(""),
					tableList.getFieldChild9().orElse(""), tableList.getFieldChild10().orElse("") };

			boolean isFirstOnStatement = true;
			for (int i = 0; i < parentFields.length; i++) {
				if (!Strings.isNullOrEmpty(parentFields[i]) && !Strings.isNullOrEmpty(childFields[i])) {
					if (!isFirstOnStatement) {
						query.append(" AND ");
					}
					isFirstOnStatement = false;
					query.append("p." + parentFields[i] + "=" + "t." + childFields[i]);
				}
			}
		}

		String[] fieldKeyQuerys = { tableList.getFieldKeyQuery1().orElse(""), tableList.getFieldKeyQuery2().orElse(""),
				tableList.getFieldKeyQuery3().orElse(""), tableList.getFieldKeyQuery4().orElse(""),
				tableList.getFieldKeyQuery5().orElse(""), tableList.getFieldKeyQuery6().orElse(""),
				tableList.getFieldKeyQuery7().orElse(""), tableList.getFieldKeyQuery8().orElse(""),
				tableList.getFieldKeyQuery9().orElse(""), tableList.getFieldKeyQuery10().orElse("") };
		String[] clsKeyQuerys = { tableList.getClsKeyQuery1().orElse(""), tableList.getClsKeyQuery2().orElse(""),
				tableList.getClsKeyQuery3().orElse(""), tableList.getClsKeyQuery4().orElse(""),
				tableList.getClsKeyQuery5().orElse(""), tableList.getClsKeyQuery6().orElse(""),
				tableList.getClsKeyQuery7().orElse(""), tableList.getClsKeyQuery8().orElse(""),
				tableList.getClsKeyQuery9().orElse(""), tableList.getClsKeyQuery10().orElse("") };
		
		// Where
		query.append(" WHERE 1 = 1 ");

		List<Integer> companyCdIndexs = new ArrayList<Integer>();
		List<Integer> yearIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthDayIndexs = new ArrayList<Integer>();
		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (clsKeyQuerys[i] == "")
				continue;
			switch (clsKeyQuerys[i]) {
			case COMPANY_CD:
				companyCdIndexs.add(i);
				break;

			case YEAR:
				yearIndexs.add(i);
				break;

			case YEAR_MONTH:
				yearMonthIndexs.add(i);
				break;

			case YEAR_MONTH_DAY:
				yearMonthDayIndexs.add(i);
				break;

			default:
				break;
			}
		}

		Map<String, Object> params = new HashMap<>();
		if (companyCdIndexs.size() > 0) {
			query.append(" AND ( ");
			boolean isFirstOrStatement = true;
			for (Integer index : companyCdIndexs) {
				if (!isFirstOrStatement) {
					query.append(" OR ");
				}
				isFirstOrStatement = false;
				if (columns.contains(fieldKeyQuerys[index])) {
					query.append(" t.");
					query.append(fieldKeyQuerys[index]);
				} else {
					query.append(" p.");
					query.append(fieldKeyQuerys[index]);
				}
				query.append(" = ?companyId ");
				params.put("companyId", AppContexts.user().companyId());
			}
			query.append(" ) ");
		}

		// 履歴区分を判別する。履歴なしの場合
		if (tableList.getHistoryCls() == HistoryDiviSion.NO_HISTORY.value) {
			List<Integer> indexs = new ArrayList<Integer>();
			switch (tableList.getTimeStore()) {
			case 3: // ANNUAL
				indexs = yearIndexs;
				break;
			case 2: // MONTHLY
				indexs = yearMonthIndexs;
				break;
			case 1: // DAILY
				indexs = yearMonthDayIndexs;
				break;

			default:
				break;
			}

			if (indexs.size() > 0) {
				indexs.add(99);
				query.append(" AND ( ");
				boolean isFirstOrStatement = true;
				for (int i = 0; i <= indexs.size(); i++) {
					if (!isFirstOrStatement) {
						query.append(" OR ");
					}
					isFirstOrStatement = false;
					if ((i != (indexs.size() - 1)) && (i < indexs.size())) {
						// Start Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i)])) {
							query.append(" (t.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						} else {
							query.append(" (p.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						}

						query.append(" >= ?startDate ");
						query.append(" AND ");

						// End Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i)])) {
							query.append(" t.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						} else {
							query.append(" p.");
							query.append(fieldKeyQuerys[indexs.get(i)]);
						}

						query.append(" <= ?endDate) ");
					} else if (i == (indexs.size() - 1)) {
						// fix bug #103051
						// Start Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i - 2)])) {
							query.append(" (t.");
							query.append(fieldKeyQuerys[indexs.get(i - 2)]);
						} else {
							query.append(" (p.");
							query.append(fieldKeyQuerys[indexs.get(i - 2)]);
						}

						query.append(" <= ?startDate ");
						query.append(" AND ");

						// End Date
						if (columns.contains(fieldKeyQuerys[indexs.get(i - 1)])) {
							query.append(" t.");
							query.append(fieldKeyQuerys[indexs.get(i - 1)]);
						} else {
							query.append(" p.");
							query.append(fieldKeyQuerys[indexs.get(i - 1)]);
						}

						isFirstOrStatement = true;

						query.append(" >= ?endDate) ");
					}

					switch (tableList.getTimeStore()) {
					case 1 : //DAILY
						params.put("startDate", tableList.getStartDateOfDaily());
						params.put("endDate", tableList.getEndDateOfDaily());
						break;
					case 2 : //MONTHLY
						params.put("startDate",
								Integer.valueOf(tableList.getStartDateOfDaily().replaceAll("\\/", "")));
						params.put("endDate", Integer.valueOf(tableList.getEndDateOfDaily().replaceAll("\\/", "")));
						break;
					case 3 : // ANNUAL
						params.put("startDate", Integer.valueOf(tableList.getStartDateOfDaily()));
						params.put("endDate", Integer.valueOf(tableList.getEndDateOfDaily()));
						break;

					default:
						break;
					}
				}
				query.append(" ) ");
			}
		}

		// 抽出条件キー固定
		query.append(tableList.getDefaultCondKeyQuery().orElse(""));
		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (EMPLOYEE_CD.equals(clsKeyQuerys[i]) && !targetEmployeesSid.isEmpty()) {
				if (tableList.getHasParentTblFlg() == NotUseAtr.USE.value) {
					query.append(" AND p." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				} else {
					query.append(" AND t." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				}
			}
		}
		
		
		String querySql = query.toString();
		if(!targetEmployeesSid.isEmpty()) {
			List<String> lSid = new ArrayList<>();
			CollectionUtil.split(targetEmployeesSid, 1000, subIdList -> {
				lSid.add(subIdList.toString().replaceAll("\\[", "\\'").replaceAll("\\]", "\\'").replaceAll(", ","\\', '"));
			});
			for (String sid : lSid) {
				Query queryString = getEntityManager().createNativeQuery(querySql.replaceAll("\\?listTargetSid", sid));
				for (Entry<String, Object> entry : params.entrySet()) {
					queryString.setParameter(entry.getKey(), entry.getValue());
				}
				queryString.executeUpdate();
			}
		}else {
			Query queryString = getEntityManager().createNativeQuery(querySql);
			for (Entry<String, Object> entry : params.entrySet()) {
				queryString.setParameter(entry.getKey(), entry.getValue());
			}
			queryString.executeUpdate();
		}
	}



	/**
	 * 
	 * @param tableDelData
	 * @param employeeDeletions
	 * @param parrams
	 * @return
	 */
	private String buildGetDataForEachCatSql(TableDeletionDataCsv tableDelData, 
			List<EmployeeDeletion> employeeDeletions, Map<String, Object> parrams) {
		boolean hasParentTbl = tableDelData.hasParentTblFlg();
		StringBuffer query = new StringBuffer();
		// build select part
		buildSelectPart(query, tableDelData, hasParentTbl);
		//build form part
		buildFromPart(query, tableDelData.getTableEnglishName());
		//build inner joint
		if (hasParentTbl && tableDelData.getParentTblName().isPresent()) {
			buildInnerJoint(query, tableDelData);
		}
		//build where part
		buildWherePart(query, tableDelData, employeeDeletions, parrams);
		return query.toString();
	}
	
	private String getFieldAcq(List<String> allColumns, Optional<String> fieldName, String fieldAcqName) {
		String fieldAcq = fieldName.orElse("");
		if (!Strings.isNullOrEmpty(fieldAcq)) {
			if (allColumns.contains(fieldAcq)) {
				return " t." + fieldAcq + " AS " + fieldAcqName + ", ";
			} else {
				return " p." + fieldAcq + " AS " + fieldAcqName + ", ";
			}
		} else {
			return " '' AS " + fieldAcqName + ", ";
		}
	}
	
	
	
	

	public List<String> getAllColumnName(String tableName) {
		List<?> columns = this.getEntityManager().createNativeQuery(SELECT_COLUMN_NAME_SQL)
				.setParameter("tableName", tableName).getResultList();
		if (columns == null || columns.isEmpty()) {
			return Collections.emptyList();
		}
		return columns.stream().map(item -> {
			return String.valueOf(item);
		}).collect(Collectors.toList());

	}

	/**
	 * build the select part
	 * @param buffer
	 * @param tblName
	 * @param parentTblName
	 * @param acqCidField
	 * @param acqEmployeeField
	 * @param acqDateField
	 * @param acqStartDateField
	 * @param acqEndDateField
	 * @param hasParentTbl
	 */
	private void buildSelectPart(StringBuffer buffer, TableDeletionDataCsv tableDelData,
			boolean hasParentTbl) {
		String acqCidField = tableDelData.getFieldAcqCid().get();
		String acqEmployeeField = tableDelData.getFieldAcqEmployeeId().get();
		String acqDateField = tableDelData.getFieldAcqDateTime().get();
		String acqStartDateField = tableDelData.getFieldAcqStartDate().get();
		String acqEndDateField = tableDelData.getFieldAcqEndDate().get();
		String tblName = tableDelData.getTableEnglishName();
		String parentTblName = tableDelData.getParentTblName().get();
		String tblAcq = tblName;
		if (hasParentTbl) {
			tblAcq = parentTblName;
		}

		buffer.append("SELECT ");
		// acqCidField
		if (acqCidField != null && !"null".equals(acqCidField) && !acqCidField.isEmpty()) {
			buffer.append(tblAcq + "." + acqCidField + " AS H_CID, ");
		} else {
			buffer.append(" NULL AS H_CID, ");
		}
		
		// acqEmployeeField
		if (acqEmployeeField != null && !"null".equals(acqEmployeeField) && !acqEmployeeField.isEmpty()) {
			buffer.append(tblAcq + "." + acqEmployeeField + " AS H_SID, ");
		} else {
			buffer.append(" NULL AS H_SID, ");
		}
		// acqDateField
		if (acqDateField != null && !"null".equals(acqDateField) && !acqDateField.isEmpty()) {
			buffer.append(tblAcq + "." + acqDateField + " AS H_DATE, ");
		} else {
			buffer.append(" NULL AS H_DATE, ");
		}
		// acqStartDateField
		if (acqStartDateField != null && !"null".equals(acqStartDateField) && !acqStartDateField.isEmpty()) {
			buffer.append(tblAcq + "." + acqStartDateField + " AS H_DATE_START, ");
		} else {
			buffer.append(" NULL AS H_DATE_START, ");
		}
		// acqEndDateField
		if (acqEndDateField != null && !"null".equals(acqEndDateField) && !acqEndDateField.isEmpty()) {
			buffer.append(tblAcq + "." + acqEndDateField + " AS H_DATE_END, ");
		} else {
			buffer.append(" NULL AS H_DATE_END, ");
		}
		
		buffer.append(tblName + ".* ");
	}
	
	/**
	 * 
	 * @param buffer
	 * @param tblName
	 */
	private void buildFromPart(StringBuffer buffer, String tblName) {
		buffer.append(" FROM " + tblName + " AS " + tblName);
	}
	
	/**
	 * build inner joint in case: the having parrent table
	 * @param buffer
	 * @param tableDelData
	 */
	private void buildInnerJoint(StringBuffer buffer, TableDeletionDataCsv tableDelData) {
		String tblName = tableDelData.getTableEnglishName();
		String parentTblName = tableDelData.getParentTblName().get();
		buffer.append(" INNER　JOIN " + parentTblName + " ON ");
		
		String prefix = "";
		String fieldChild1 = tableDelData.getFieldChild1().get();
		String fieldParent1 = tableDelData.getFieldParent1().get();
		if (fieldChild1 != null && !"null".equals(fieldChild1) && !fieldChild1.isEmpty() 
				&& fieldParent1 != null && !"null".equals(fieldParent1) && !fieldParent1.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild1 
				+ " = " + parentTblName + "." + fieldParent1);
		}
		
		String fieldChild2 = tableDelData.getFieldChild2().get();
		String fieldParent2 = tableDelData.getFieldParent2().get();
		if (fieldChild2 != null && !"null".equals(fieldChild2) && !fieldChild2.isEmpty() 
				&& fieldParent2 != null && !"null".equals(fieldParent2) && !fieldParent2.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild2 
				+ " = " + parentTblName + "." + fieldParent2);
		}
		
		String fieldChild3 = tableDelData.getFieldChild3().get();
		String fieldParent3 = tableDelData.getFieldParent3().get();
		if (fieldChild3 != null && !"null".equals(fieldChild3) && !fieldChild3.isEmpty() 
				&& fieldParent3 != null && !"null".equals(fieldParent3) && !fieldParent3.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild3 
				+ " = " + parentTblName + "." + fieldParent3);
		}
		
		String fieldChild4 = tableDelData.getFieldChild4().get();
		String fieldParent4 = tableDelData.getFieldParent4().get();
		if (fieldChild4 != null && !"null".equals(fieldChild4) && !fieldChild4.isEmpty() 
				&& fieldParent4 != null && !"null".equals(fieldParent4) && !fieldParent4.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild4 
				+ " = " + parentTblName + "." + fieldParent4);
		}
		
		String fieldChild5 = tableDelData.getFieldChild5().get();
		String fieldParent5 = tableDelData.getFieldParent5().get();
		if (fieldChild5 != null && !"null".equals(fieldChild5) && !fieldChild5.isEmpty() 
				&& fieldParent5 != null && !"null".equals(fieldParent5) && !fieldParent5.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild5 
				+ " = " + parentTblName + "." + fieldParent5);
		}
		
		String fieldChild6 = tableDelData.getFieldChild6().get();
		String fieldParent6 = tableDelData.getFieldParent6().get();
		if (fieldChild6 != null && !"null".equals(fieldChild6) && !fieldChild6.isEmpty() 
				&& fieldParent6 != null && !"null".equals(fieldParent6) && !fieldParent6.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild6 
				+ " = " + parentTblName + "." + fieldParent6);
		}
		
		String fieldChild7 = tableDelData.getFieldChild7().get();
		String fieldParent7 = tableDelData.getFieldParent7().get();
		if (fieldChild7 != null && !"null".equals(fieldChild7) && !fieldChild7.isEmpty() 
				&& fieldParent7 != null && !"null".equals(fieldParent7) && !fieldParent7.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild7 
				+ " = " + parentTblName + "." + fieldParent7);
		}
		
		String fieldChild8 = tableDelData.getFieldChild8().get();
		String fieldParent8 = tableDelData.getFieldParent8().get();
		if (fieldChild8 != null && !"null".equals(fieldChild8) && !fieldChild8.isEmpty() 
				&& fieldParent8 != null && !"null".equals(fieldParent8) && !fieldParent8.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild8 
				+ " = " + parentTblName + "." + fieldParent8);
		}
		
		String fieldChild9 = tableDelData.getFieldChild9().get();
		String fieldParent9 = tableDelData.getFieldParent9().get();
		if (fieldChild9 != null && !"null".equals(fieldChild9) && !fieldChild9.isEmpty() 
				&& fieldParent9 != null && !"null".equals(fieldParent9) && !fieldParent9.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild9 
				+ " = " + parentTblName + "." + fieldParent9);
		}
		
		String fieldChild10 = tableDelData.getFieldChild10().get();
		String fieldParent10 = tableDelData.getFieldParent10().get();
		if (fieldChild10 != null && !"null".equals(fieldChild10) && !fieldChild10.isEmpty() 
				&& fieldParent10 != null && !"null".equals(fieldParent10) && !fieldParent10.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild10 
				+ " = " + parentTblName + "." + fieldParent10);
		}
	}
	
	/**
	 * 
	 * @param buffer
	 * @param tableDelData
	 * @param employeeDeletions
	 * @param parrams
	 */
	private void buildWherePart(StringBuffer buffer, TableDeletionDataCsv tableDelData, 
			List<EmployeeDeletion> employeeDeletions, Map<String, Object> parrams) {
//		int timeStore = tableDelData.getTimeStore();
		String tblAcq = tableDelData.getTableEnglishName();
		if (tableDelData.hasParentTblFlg()) {
			tblAcq = tableDelData.getParentTblName().get();
		}
		
		List lstEmployeeIds = employeeDeletions.stream().map(emp -> emp.getEmployeeId()).collect(Collectors.toList());
		
		buffer.append(" WHERE 1 = 1 ");
		
		//company id
		String acqCidField = tableDelData.getFieldAcqCid().get();
		if (acqCidField != null && !"null".equals(acqCidField) && !acqCidField.isEmpty()) {
			buffer.append(" AND " + tblAcq + "." + acqCidField + " = ?cid ");
			parrams.put("cid", tableDelData.getCompanyId());
		}
		
		//employee id
		String acqEmployeeField = tableDelData.getFieldAcqEmployeeId().get();
		if (acqEmployeeField != null && !"null".equals(acqEmployeeField) && !acqEmployeeField.isEmpty()) {
			if (lstEmployeeIds != null && !lstEmployeeIds.isEmpty()) {
				buffer.append(" AND " + tblAcq + "." + acqEmployeeField + " IN (");
				
				for (int i = 0; i < lstEmployeeIds.size(); i++) {
					if (i <= lstEmployeeIds.size() - 2) {
						buffer.append("?employeeId" + i + ",");
					}
					else {
						buffer.append("?employeeId" + i + ")");
					}
					parrams.put("employeeId" + i, lstEmployeeIds.get(i));
				}
			}
		}
		
		//date
		String acqDateTimeField = tableDelData.getFieldAcqDateTime().get();
		if (acqDateTimeField != null && !"null".equals(acqDateTimeField) && !acqDateTimeField.isEmpty()) {
//			if (!isDateFieldInOracle(acqDateTimeField, tableDelData)) {
				buffer.append(" AND " + tblAcq + "." + acqDateTimeField + " >= ?startDate ");
				buffer.append(" AND " + tblAcq + "." + acqDateTimeField + " <= ?endDate ");
//			} else {
//				buffer.append(" AND " + tblAcq + "." + acqDateTimeField + " >= " + toDateOracle(timeStore, " ?startDate "));
//				buffer.append(" AND " + tblAcq + "." + acqDateTimeField + " <= " + toDateOracle(timeStore, " ?endDate "));
//			}
			setDateParrams(tableDelData, parrams);
		}
		
		//period date
		String acqStartDateField = tableDelData.getFieldAcqStartDate().get();
		String acqEndDateField = tableDelData.getFieldAcqEndDate().get();
		
		if (acqStartDateField != null && !"null".equals(acqStartDateField) && !acqStartDateField.isEmpty()
				&& acqEndDateField != null && !"null".equals(acqEndDateField) && !acqEndDateField.isEmpty()) {
//			if (!isDateFieldInOracle(acqStartDateField, tableDelData)) {
				buffer.append(" AND " + tblAcq + "." + acqStartDateField + " >= ?startDate ");
				buffer.append(" AND " + tblAcq + "." + acqEndDateField + " <= ?endDate ");
//			} else {
//				buffer.append(" AND " + tblAcq + "." + acqStartDateField + " >= " + toDateOracle(timeStore, " ?startDate "));
//				buffer.append(" AND " + tblAcq + "." + acqEndDateField + " <= " + toDateOracle(timeStore, " ?endDate "));
//			}
			setDateParrams(tableDelData, parrams);
		}
		
		//condition default
		String defaultCondKeyQuery = tableDelData.getDefaultCondKeyQuery().get();
		if (defaultCondKeyQuery != null && !"null".equals(defaultCondKeyQuery) && !defaultCondKeyQuery.isEmpty()) {
			buffer.append(" " + defaultCondKeyQuery);
		}
	}

	/**
	 * 
	 * @param tableDelData
	 * @param parrams
	 */
	private void setDateParrams(TableDeletionDataCsv tableDelData, Map<String, Object> parrams) {
		TimeStore timeStore = TimeStore.valueOf(tableDelData.getTimeStore());
		if (timeStore == TimeStore.DAILY) {
			parrams.put("startDate", tableDelData.getStartDateOfDaily());
			parrams.put("endDate", tableDelData.getEndDateOfDaily());
		} else if (timeStore == TimeStore.MONTHLY) {
			Optional<Integer> startMonthly = Optional.of(Integer.parseInt(tableDelData.getStartMonthOfMonthly()));
			Optional<Integer> endMonthly = Optional.of(Integer.parseInt(tableDelData.getEndMonthOfMonthly()));
			
			parrams.put("startDate", startMonthly.get());
			parrams.put("endDate", endMonthly.get());
			
		} else if (timeStore == TimeStore.ANNUAL) {
			parrams.put("startDate", tableDelData.getStartYearOfMonthly());
			parrams.put("endDate", tableDelData.getEndYearOfMonthly());
		}
	}

	/**
	 * 
	 */
	@Override
	public List<String> getColumnName(String nameTable) {
		@SuppressWarnings("unchecked")
		List<String> listTemp = this.getEntityManager().createNativeQuery(SELECT_COLUMN_NAME_SQL)
				.setParameter("tableName", nameTable).getResultList();

		if (listTemp == null || listTemp.isEmpty()) {
			return Collections.emptyList();
		}
		return listTemp.stream().map(temp -> {
			return String.valueOf(temp);
		}).collect(Collectors.toList());
	}

	/**
	 * 
	 */
	@Override
	public void deleteData(TableDeletionDataCsv tableDelData, List<EmployeeDeletion> employeeDeletions) {
		
		List targetEmployeesSid = employeeDeletions.stream().map(emp -> emp.getEmployeeId()).collect(Collectors.toList());
		delelteDataDynamic(tableDelData, targetEmployeesSid);
	}
	
	/**
	 * 
	 * @param tableDelData
	 * @param employeeDeletions
	 * @param parrams
	 * @return
	 */
	private String buildDelDataForEachCatSql(TableDeletionDataCsv tableDelData, 
			List<EmployeeDeletion> employeeDeletions, Map<String, Object> parrams) {
		StringBuffer buffer = new StringBuffer();
		//build form part
		buildFromDelPart(buffer, tableDelData.getTableEnglishName());
		//build where part
		buildWhereDelPart(buffer, tableDelData, employeeDeletions, parrams);
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param buffer
	 * @param tblName
	 */
	private void buildFromDelPart(StringBuffer buffer, String tblName) {
		buffer.append(" DELETE "  + tblName + " FROM " + tblName );
	}
	
	/**
	 * 
	 * @param buffer
	 * @param tableDelData
	 * @param employeeDeletions
	 * @param parrams
	 */
	private void buildWhereDelPart(StringBuffer buffer, TableDeletionDataCsv tableDelData, 
			List<EmployeeDeletion> employeeDeletions, Map<String, Object> parrams) {
		if (!tableDelData.hasParentTblFlg()) {
			buildWherePart(buffer, tableDelData, employeeDeletions, parrams);
		}
		else {
			buildWhereExist(buffer, tableDelData, employeeDeletions, parrams);
		}
	}
	
	/**
	 * 
	 * @param buffer
	 * @param tableDelData
	 * @param employeeDeletions
	 * @param parrams
	 */
	private void buildWhereExist(StringBuffer buffer, TableDeletionDataCsv tableDelData, 
			List<EmployeeDeletion> employeeDeletions, Map<String, Object> parrams) {
		
		String tblName = tableDelData.getTableEnglishName();
		String parentTblName = tableDelData.getParentTblName().get();
		
		String fieldChild1 = tableDelData.getFieldChild1().get();
		String fieldParent1 = tableDelData.getFieldParent1().get();
		String fieldChild2 = tableDelData.getFieldChild2().get();
		String fieldParent2 = tableDelData.getFieldParent2().get();
		String fieldChild3 = tableDelData.getFieldChild3().get();
		String fieldParent3 = tableDelData.getFieldParent3().get();
		String fieldChild4 = tableDelData.getFieldChild4().get();
		String fieldParent4 = tableDelData.getFieldParent4().get();
		String fieldChild5 = tableDelData.getFieldChild5().get();
		String fieldParent5 = tableDelData.getFieldParent5().get();
		String fieldChild6 = tableDelData.getFieldChild6().get();
		String fieldParent6 = tableDelData.getFieldParent6().get();
		String fieldChild7 = tableDelData.getFieldChild7().get();
		String fieldParent7 = tableDelData.getFieldParent7().get();
		String fieldChild8 = tableDelData.getFieldChild8().get();
		String fieldParent8 = tableDelData.getFieldParent8().get();
		String fieldChild9 = tableDelData.getFieldChild9().get();
		String fieldParent9 = tableDelData.getFieldParent9().get();
		String fieldChild10 = tableDelData.getFieldChild10().get();
		String fieldParent10 = tableDelData.getFieldParent10().get();
		
		//build inner joint
		String prefix = "";
		buffer.append(" INNER JOIN " + parentTblName + " ON ");
		prefix = "";
		if (fieldChild1 != null && !"null".equals(fieldChild1) && !fieldChild1.isEmpty() && fieldParent1 != null
				&& !"null".equals(fieldParent1) && !fieldParent1.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild1 + " = " + parentTblName + "." + fieldParent1);
		}

		if (fieldChild2 != null && !"null".equals(fieldChild2) && !fieldChild2.isEmpty() && fieldParent2 != null
				&& !"null".equals(fieldParent2) && !fieldParent2.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild2 + " = " + parentTblName + "." + fieldParent2);
		}

		if (fieldChild3 != null && !"null".equals(fieldChild3) && !fieldChild3.isEmpty() && fieldParent3 != null
				&& !"null".equals(fieldParent3) && !fieldParent3.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild3 + " = " + parentTblName + "." + fieldParent3);
		}

		if (fieldChild4 != null && !"null".equals(fieldChild4) && !fieldChild4.isEmpty() && fieldParent4 != null
				&& !"null".equals(fieldParent4) && !fieldParent4.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild4 + " = " + parentTblName + "." + fieldParent4);
		}

		if (fieldChild5 != null && !"null".equals(fieldChild5) && !fieldChild5.isEmpty() && fieldParent5 != null
				&& !"null".equals(fieldParent5) && !fieldParent5.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild5 + " = " + parentTblName + "." + fieldParent5);
		}

		if (fieldChild6 != null && !"null".equals(fieldChild6) && !fieldChild6.isEmpty() && fieldParent6 != null
				&& !"null".equals(fieldParent6) && !fieldParent6.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild6 + " = " + parentTblName + "." + fieldParent6);
		}

		if (fieldChild7 != null && !"null".equals(fieldChild7) && !fieldChild7.isEmpty() && fieldParent7 != null
				&& !"null".equals(fieldParent7) && !fieldParent7.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild7 + " = " + parentTblName + "." + fieldParent7);
		}

		if (fieldChild8 != null && !"null".equals(fieldChild8) && !fieldChild8.isEmpty() && fieldParent8 != null
				&& !"null".equals(fieldParent8) && !fieldParent8.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild8 + " = " + parentTblName + "." + fieldParent8);
		}

		if (fieldChild9 != null && !"null".equals(fieldChild9) && !fieldChild9.isEmpty() && fieldParent9 != null
				&& !"null".equals(fieldParent9) && !fieldParent9.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild9 + " = " + parentTblName + "." + fieldParent9);
		}

		if (fieldChild10 != null && !"null".equals(fieldChild10) && !fieldChild10.isEmpty() && fieldParent10 != null
				&& !"null".equals(fieldParent10) && !fieldParent10.isEmpty()) {
			buffer.append(prefix);
			prefix = " AND ";
			buffer.append(tblName + "." + fieldChild10 + " = " + parentTblName + "." + fieldParent10);
		}
		
		//where for select
		buildWherePart(buffer, tableDelData, employeeDeletions, parrams);
		
		
//		buffer.append(" )");
	}
}
