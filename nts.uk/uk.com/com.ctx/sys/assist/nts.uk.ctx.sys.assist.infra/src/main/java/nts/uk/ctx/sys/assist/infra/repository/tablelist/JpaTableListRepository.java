package nts.uk.ctx.sys.assist.infra.repository.tablelist;

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
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.HistoryDiviSion;
import nts.uk.ctx.sys.assist.dom.saveprotetion.SaveProtetion;
import nts.uk.ctx.sys.assist.dom.saveprotetion.SaveProtetionRepository;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.dom.tablelist.TableListRepository;
import nts.uk.ctx.sys.assist.infra.entity.tablelist.SspmtTableList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class JpaTableListRepository extends JpaRepository implements TableListRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT t FROM SspmtTableList t WHERE t.tableListPk.dataStorageProcessingId =:storeProcessingId";
	private static final String SELECT_COLUMN_NAME_MSSQL = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?tableName";

	private static final String COMPANY_CD = "0";
	private static final String EMPLOYEE_CD = "5";
	private static final String YEAR = "6";
	private static final String YEAR_MONTH = "7";
	private static final String YEAR_MONTH_DAY = "8";
	
	@Inject
	private SaveProtetionRepository saveProtetionRepo;

	@Override
	public void add(TableList domain) {
		this.commandProxy().insert(SspmtTableList.toEntity(domain));
	}
	
	@Override
	public void update(TableList domain) {
		this.commandProxy().update(SspmtTableList.toEntity(domain));
	}

	@Override
	public List<TableList> getByOffsetAndNumber(String storeProcessingId, int offset, int number) {
		List<SspmtTableList> listTable = this.getEntityManager()
				.createQuery(SELECT_ALL_QUERY_STRING, SspmtTableList.class)
				.setParameter("storeProcessingId", storeProcessingId).setFirstResult(offset).setMaxResults(number)
				.getResultList();

		return listTable.stream().map(item -> item.toDomain()).collect(Collectors.toList());
	}

	@Override
	public List<TableList> getByProcessingId(String storeProcessingId) {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtTableList.class)
				.setParameter("storeProcessingId", storeProcessingId).getList(c -> c.toDomain());
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
	
	@Override
	public List<List<String>> getDataDynamic(TableList tableList, List<String> targetEmployeesSid) {
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

		//アルゴリズム「個人情報の保護」を実行する 
		List<SaveProtetion> listSaveProtetion = saveProtetionRepo.getSaveProtection(Integer.valueOf(tableList.getCategoryId()), tableList.getTableNo());
		if(tableList.getSurveyPreservation() == NotUseAtr.USE && !listSaveProtetion.isEmpty()) {
			for (SaveProtetion saveProtetion : listSaveProtetion) {
				String rePlaceCol = saveProtetion.getReplaceColumn().trim();
				String newValue = "";
				// Vì domain không tạo Enum nên phải fix code ngu
				if(saveProtetion.getCorrectClasscification() == 0) {
					newValue = "'' AS " + rePlaceCol;
				}else if(saveProtetion.getCorrectClasscification() == 1){
					if(columns.contains("EMPLOYEE_CODE")) {
						newValue = "t.EMPLOYEE_CODE AS " + rePlaceCol;
					}else {
						newValue = "'' AS " + rePlaceCol;
					}
				}else if(saveProtetion.getCorrectClasscification() == 2) {
					newValue = "0 AS " + rePlaceCol;
				}else if(saveProtetion.getCorrectClasscification() == 3) {
					newValue = "'0' AS " + rePlaceCol;
				}
				query = new StringBuffer(query.toString().replaceAll("t." + rePlaceCol, newValue));
			}
		}
		
		// From
		query.append(" FROM ").append(tableList.getTableEnglishName()).append(" t");
		if (tableList.getHasParentTblFlg() == NotUseAtr.USE && tableList.getParentTblName().isPresent()) {
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
		if (tableList.getHistoryCls() == HistoryDiviSion.NO_HISTORY) {
			List<Integer> indexs = new ArrayList<Integer>();
			switch (tableList.getRetentionPeriodCls()) {
			case ANNUAL:
				indexs = yearIndexs;
				break;
			case MONTHLY:
				indexs = yearMonthIndexs;
				break;
			case DAILY:
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

					switch (tableList.getRetentionPeriodCls()) {
					case DAILY:
						params.put("startDate", tableList.getSaveDateFrom().get());
						params.put("endDate", tableList.getSaveDateTo().get());
						break;
					case MONTHLY:
						params.put("startDate",
								Integer.valueOf(tableList.getSaveDateFrom().get().replaceAll("\\/", "")));
						params.put("endDate", Integer.valueOf(tableList.getSaveDateTo().get().replaceAll("\\/", "")));
						break;
					case ANNUAL:
						params.put("startDate", Integer.valueOf(tableList.getSaveDateFrom().get()));
						params.put("endDate", Integer.valueOf(tableList.getSaveDateTo().get()));
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
				if (tableList.getHasParentTblFlg() == NotUseAtr.USE) {
					query.append(" AND p." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				} else {
					query.append(" AND t." + fieldKeyQuerys[i] + " IN (?listTargetSid) ");
				}
			}
		}
		
		// Order By
		query.append(" ORDER BY H_CID, H_SID, H_DATE, H_DATE_START");
		String querySql = query.toString();
		if(tableList.getTableEnglishName().equals("BPSMT_PERSON")) {
			query.toString();
		}
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

	@Override
	public List<String> getAllColumnName(String tableName) {
		List<?> columns = this.getEntityManager().createNativeQuery(SELECT_COLUMN_NAME_MSSQL)
				.setParameter("tableName", tableName).getResultList();
		if (columns == null || columns.isEmpty()) {
			return Collections.emptyList();
		}
		return columns.stream().map(item -> {
			return String.valueOf(item);
		}).collect(Collectors.toList());

	}
}
