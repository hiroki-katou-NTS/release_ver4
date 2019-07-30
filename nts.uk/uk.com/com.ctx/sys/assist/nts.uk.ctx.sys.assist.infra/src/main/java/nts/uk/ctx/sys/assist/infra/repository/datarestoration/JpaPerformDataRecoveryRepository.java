package nts.uk.ctx.sys.assist.infra.repository.datarestoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.assist.dom.category.StorageRangeSaved;
import nts.uk.ctx.sys.assist.dom.datarestoration.DelDataException;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecovery;
import nts.uk.ctx.sys.assist.dom.datarestoration.PerformDataRecoveryRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.RestorationTarget;
import nts.uk.ctx.sys.assist.dom.datarestoration.SettingException;
import nts.uk.ctx.sys.assist.dom.datarestoration.SqlException;
import nts.uk.ctx.sys.assist.dom.datarestoration.Target;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspmtPerformDataRecovery;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspmtRestorationTarget;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspmtTarget;
import nts.uk.ctx.sys.assist.infra.entity.tablelist.SspmtTableList;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class JpaPerformDataRecoveryRepository extends JpaRepository implements PerformDataRecoveryRepository {

	private static final String SELECT_TABLE_LIST_BY_DATA_RECOVERY_QUERY_STRING = "SELECT t FROM SspmtTableList t WHERE t.dataRecoveryProcessId =:dataRecoveryProcessId";

	private static final String SELECT_ALL_QUERY_STRING = "SELECT t FROM SspmtTableList t WHERE  t.tableListPk.categoryId =:categoryId AND t.dataRecoveryProcessId =:dataRecoveryProcessId AND t.storageRangeSaved =:storageRangeSaved ORDER BY t.tableListPk.tableNo DESC ";
	
	private static final String SELECT_ALL_TARGET = "SELECT t FROM SspmtTarget t WHERE  t.targetPk.dataRecoveryProcessId =:dataRecoveryProcessId ";

	private static final String SELECT_INTERNAL_FILE_NAME = "SELECT t FROM SspmtTableList t WHERE  t.dataRecoveryProcessId =:dataRecoveryProcessId AND t.internalFileName =:internalFileName ";

	private static final String SELECT_ALL_TABLE_LIST_QUERY_STRING = "SELECT f FROM SspmtTableList f";

	private static final String SELECT_BY_RECOVERY_PROCESSING_ID_QUERY_STRING = "SELECT DISTINCT t.tableListPk.categoryId, t.categoryName, t.saveSetCode, t.saveSetName, t.saveDateFrom, t.saveDateTo, t.storageRangeSaved, t.retentionPeriodCls, t.anotherComCls, t.compressedFileName, t.canNotBeOld, t.supplementaryExplanation, t.saveForm FROM SspmtTableList t WHERE  t.dataRecoveryProcessId =:dataRecoveryProcessId GROUP BY t.tableListPk.categoryId, t.categoryName, t.saveSetCode, t.saveSetName, t.saveDateFrom, t.saveDateTo, t.storageRangeSaved, t.retentionPeriodCls, t.anotherComCls, t.compressedFileName, t.canNotBeOld, t.supplementaryExplanation, t.saveForm";

	private static final String SELECT_TARGET_BY_DATA_RECOVERY_PROCESS_ID = "SELECT t FROM SspmtTarget t WHERE t.targetPk.dataRecoveryProcessId=:dataRecoveryProcessId";

	private static final String SELECT_RESTORATION_TARGET_BY_DATA_RECOVERY_PROCESS_ID = "SELECT st FROM SspmtRestorationTarget st WHERE st.restorationTargetPk.dataRecoveryProcessId=:dataRecoveryProcessId";

	private static final String DELETE_BY_LIST_ID_EMPLOYEE = "DELETE FROM SspmtTarget t WHERE t.targetPk.dataRecoveryProcessId =:dataRecoveryProcessId AND t.targetPk.sid NOT IN :employeeIdList";

	private static final String UPDATE_BY_LIST_CATEGORY_ID = "UPDATE SspmtTableList t SET t.selectionTargetForRes =:selectionTarget  WHERE t.dataRecoveryProcessId =:dataRecoveryProcessId AND t.tableListPk.categoryId not in :listCheckCate ";

	private static final String UPDATE_DATE_FROM_TO_BY_LIST_CATEGORY_ID = "UPDATE SspmtTableList t SET t.saveDateFrom =:startOfPeriod, t.saveDateTo =:endOfPeriod  WHERE t.dataRecoveryProcessId =:dataRecoveryProcessId AND t.tableListPk.categoryId =:checkCate ";
	
	private static final String DELETE_TABLE_LIST = "DELETE FROM SspmtTableList  t where t.dataRecoveryProcessId =:dataRecoveryProcessId";
	
	
	
	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public Optional<PerformDataRecovery> getPerformDatRecoverById(String dataRecoveryProcessId) {
		List<SspmtTarget> targetData = this.queryProxy()
				.query(SELECT_TARGET_BY_DATA_RECOVERY_PROCESS_ID, SspmtTarget.class)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getList();
		List<SspmtRestorationTarget> restorationTarget = this.queryProxy()
				.query(SELECT_RESTORATION_TARGET_BY_DATA_RECOVERY_PROCESS_ID, SspmtRestorationTarget.class)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getList();
		return Optional.ofNullable(this.getEntityManager().find(SspmtPerformDataRecovery.class, dataRecoveryProcessId)
				.toDomain(targetData, restorationTarget));
	}
	
	@Override
	public void add(PerformDataRecovery domain) {
		this.commandProxy().insert(SspmtPerformDataRecovery.toEntity(domain));
	}

	@Override
	public void update(PerformDataRecovery domain) {
		this.commandProxy().update(SspmtPerformDataRecovery.toEntity(domain));
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public void remove(String dataRecoveryProcessId) {
		this.commandProxy().remove(SspmtPerformDataRecovery.class, dataRecoveryProcessId);
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public List<TableList> getByStorageRangeSaved(String categoryId, String dataRecoveryProcessId, StorageRangeSaved storageRangeSaved) {
		List<SspmtTableList> listTable = this.getEntityManager()
				.createQuery(SELECT_ALL_QUERY_STRING, SspmtTableList.class).setParameter("categoryId", categoryId)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId)
				.setParameter("storageRangeSaved", storageRangeSaved.value).getResultList();

		return listTable.stream().map(SspmtTableList::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<Target> findByDataRecoveryId(String dataRecoveryProcessId) {
		List<SspmtTarget> listTarget = this.getEntityManager().createQuery(SELECT_ALL_TARGET, SspmtTarget.class)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getResultList();

		return listTarget.stream().map(SspmtTarget::toDomain).collect(Collectors.toList());
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public Optional<TableList> getByInternal(String internalFileName, String dataRecoveryProcessId) {
		return this.queryProxy().query(SELECT_INTERNAL_FILE_NAME, SspmtTableList.class)
				.setParameter("internalFileName", internalFileName)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getSingle(SspmtTableList::toDomain);
	}

	@Override
	public Integer countDataExitTableByVKeyUp(Map<String, String> filedWhere, String tableName, String namePhysicalCid,
			String cidCurrent, String dataRecoveryProcessId,String employeeCode) {
		
		if (tableName != null) {
			Integer x = 0;
			StringBuilder COUNT_BY_TABLE_SQL = new StringBuilder("SELECT count(*) from ");
			COUNT_BY_TABLE_SQL.append(tableName).append(" WHERE 1=1 ");
			COUNT_BY_TABLE_SQL.append(makeWhereClause(filedWhere, namePhysicalCid, cidCurrent));
			x = (Integer) (this.getEntityManager().createNativeQuery(COUNT_BY_TABLE_SQL.toString()).getSingleResult());
			if (x > 1) {
				String target = employeeCode;
				String errorContent = null;
				GeneralDate targetDate = GeneralDate.today();
				String contentSql = COUNT_BY_TABLE_SQL.toString();
				String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
				throw new SettingException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
			}
			return x;
		}
		return 0;
	}
	
	@Override
	public Integer countDataTransactionExitTableByVKeyUp(Map<String, String> filedWhere, String tableName,
			String namePhysicalCid, String cidCurrent, String dataRecoveryProcessId, String employeeCode) {
		if (tableName != null) {
			Integer x = 0;
			StringBuilder COUNT_BY_TABLE_SQL = new StringBuilder("SELECT count(*) from ");
			COUNT_BY_TABLE_SQL.append(tableName).append(" WHERE 1=1 ");
			COUNT_BY_TABLE_SQL.append(makeWhereClause(filedWhere, namePhysicalCid, cidCurrent));
			x = (Integer) (this.getEntityManager().createNativeQuery(COUNT_BY_TABLE_SQL.toString()).getSingleResult());
			if (x > 1) {
				String target = employeeCode;
				String errorContent = null;
				GeneralDate targetDate = GeneralDate.today();
				String contentSql = COUNT_BY_TABLE_SQL.toString();
				String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
				throw new SettingException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
			}
			return x;
		}
		return 0;
	}

	private StringBuilder makeWhereClause(Map<String, String> filedWhere, String namePhysicalCid, String cidCurrent) {
		StringBuilder whereClause = new StringBuilder();
		for (Map.Entry<String, String> filed : filedWhere.entrySet()) {
			if (!filed.getValue().isEmpty()) {

				if (!Objects.isNull(namePhysicalCid) && filed.getKey().equals(namePhysicalCid)) {
					whereClause.append(" AND ").append(filed.getKey()).append(" = '").append(cidCurrent).append("'");
				} else {
					whereClause.append(" AND ").append(filed.getKey()).append(" = '").append(filed.getValue())
							.append("'");
				}

			}
		}
		return whereClause;
	}

	@Override
	public void deleteDataExitTableByVkey(List<Map<String, String>> listFiledWhere2, String tableName,
			String namePhysicalCid, String cidCurrent, String employeeCode, String dataRecoveryProcessId, TableList tableList) {

		EntityManager em = this.getEntityManager();

		if (tableName != null) {
			StringBuilder DELETE_DATA_TABLE_SQL = new StringBuilder("");
			try {
				for (int i = 0; i < listFiledWhere2.size(); i++) {
					StringBuilder DELETE_BY_TABLE_SQL = new StringBuilder("DELETE FROM ");
					DELETE_BY_TABLE_SQL.append(tableName).append(" WHERE 1=1 ");
					DELETE_BY_TABLE_SQL.append(makeWhereClause(listFiledWhere2.get(i), namePhysicalCid, cidCurrent));
					
					DELETE_DATA_TABLE_SQL.append(DELETE_BY_TABLE_SQL.toString() +"; ");
				}
				Query query = em.createNativeQuery(DELETE_DATA_TABLE_SQL.toString());
				query.executeUpdate();
			} catch (Exception e) {
				String target = employeeCode;
				String errorContent = e.getMessage();
				GeneralDate targetDate = GeneralDate.today();
				String contentSql = DELETE_DATA_TABLE_SQL.toString();
				String processingContent = "データベース復旧処理  " + TextResource.localize("CMF004_465") + " " + tableList.getTableJapaneseName();
				throw new SqlException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent) ;
			}
		}
	}
	
	@Override
	public void deleteTransactionDataExitTableByVkey(List<Map<String, String>> listFiledWhere2, String tableName, String namePhysicalCid,
			String cidCurrent, String employeeCode, String dataRecoveryProcessId, TableList tableList) {

		EntityManager em = this.getEntityManager();

		if (tableName != null) {
			StringBuilder DELETE_DATA_TABLE_SQL = new StringBuilder("");
			try {
				for (int i = 0; i < listFiledWhere2.size(); i++) {
					StringBuilder DELETE_BY_TABLE_SQL = new StringBuilder("DELETE FROM ");
					DELETE_BY_TABLE_SQL.append(tableName).append(" WHERE 1=1 ");
					DELETE_BY_TABLE_SQL.append(makeWhereClause(listFiledWhere2.get(i), namePhysicalCid, cidCurrent));
					DELETE_DATA_TABLE_SQL.append(DELETE_BY_TABLE_SQL.toString() + "; ");
				}
				Query query = em.createNativeQuery(DELETE_DATA_TABLE_SQL.toString());
				query.executeUpdate();
			} catch (Exception e) {
				String target = employeeCode;
				String errorContent = e.getMessage();
				GeneralDate targetDate = GeneralDate.today();
				String contentSql = DELETE_DATA_TABLE_SQL.toString();
				String processingContent = "データベース復旧処理  " + TextResource.localize("CMF004_465") + " " + tableList.getTableJapaneseName();
				throw new SqlException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
			}
		}
	}
	
	@Override
	public void insertDataTable( StringBuilder insertToTable,String employeeCode,String dataRecoveryProcessId, TableList tableList) {
		String insertDb = "";
		try {
			EntityManager em = this.getEntityManager();
			insertDb = insertToTable.toString().replaceAll(", \\) VALUES \\(" , ") VALUES (").replaceAll("\\]", "\\)").replaceAll("\\[", "\\(");
			Query query = em.createNativeQuery(insertDb);
			query.executeUpdate();
		} catch (Exception e) {
			String target = employeeCode;
			String errorContent = e.getMessage();
			GeneralDate targetDate = GeneralDate.today();
			String contentSql = insertDb;
			String processingContent = "データベース復旧処理  " + TextResource.localize("CMF004_465" + " " + tableList.getTableJapaneseName());
			throw new SqlException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
		}
	}
	
	
	@Override
	public void insertTransactionDataTable(StringBuilder insertToTable,String employeeCode,String dataRecoveryProcessId,TableList tableList) {
		
		String insertDb = "";
		try {
			EntityManager em = this.getEntityManager();
			insertDb = insertToTable.toString().replaceAll(", \\) VALUES \\(" , ") VALUES (").replaceAll("\\]", "\\)").replaceAll("\\[", "\\(");
			Query query = em.createNativeQuery(insertDb);
			query.executeUpdate();
			
		} catch (Exception e) {
			String target = employeeCode;
			String errorContent = e.getMessage();
			GeneralDate targetDate = GeneralDate.today();
			String contentSql = insertDb;
			String processingContent = "データベース復旧処理  " + TextResource.localize("CMF004_465") + " " + tableList.getTableJapaneseName();
			throw new SqlException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
		}
	}
	

	@Override
	public List<TableList> getAllTableList() {
		return this.queryProxy().query(SELECT_ALL_TABLE_LIST_QUERY_STRING, SspmtTableList.class)
				.getList(SspmtTableList::toDomain);
	}

	@Override
	public List<TableList> getByRecoveryProcessingId(String dataRecoveryProcessId) {
		List<TableList> tableList = new ArrayList<>();
		List<Object[]> listItems = this.queryProxy()
				.query(SELECT_BY_RECOVERY_PROCESSING_ID_QUERY_STRING, Object[].class)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getList();
		listItems.stream().forEach(x -> {
			tableList.add(fromDomain(x));
		});
		
		// fix bug #102589 start
		List<TableList> lstTableListDuplicate = new ArrayList<>();
		Map<String, List<TableList>> map = tableList.stream().collect(Collectors.groupingBy(TableList::getCategoryId));
		map.forEach((k, v) -> {
			if (v.size() > 1) {
				lstTableListDuplicate.addAll(v.stream().filter(x -> x.getStorageRangeSaved() == StorageRangeSaved.ALL_EMP).collect(Collectors.toList()));
			}
		});
		
		if(!lstTableListDuplicate.isEmpty()){
			tableList.removeAll(lstTableListDuplicate);
		}// end
		
		return tableList;
	}

	@Override
	public void deleteEmployeeHis(TableList table, String whereCid, String whereSid, String cid, String employeeId,String employeeCode ,String dataRecoveryProcessId) {
		String sqlContent = "";
		try {
			EntityManager em = this.getEntityManager();
			if (table.getTableEnglishName() != null) {
				StringBuilder DELETE_BY_TABLE_SQL = new StringBuilder("DELETE t FROM  ");
				DELETE_BY_TABLE_SQL.append(table.getTableEnglishName()).append(" t");
				boolean hasParentTblFlg = false;

				// アルゴリズム「親テーブルをJOINする」を実行する
				if (table.getHasParentTblFlg() == NotUseAtr.USE && table.getParentTblName().isPresent()) {
					hasParentTblFlg = true;
					DELETE_BY_TABLE_SQL.append(" INNER JOIN ").append(table.getParentTblName().get()).append(" p ON ");

					String[] parentFields = { table.getFieldParent1().orElse(""), table.getFieldParent2().orElse(""),
							table.getFieldParent3().orElse(""), table.getFieldParent4().orElse(""),
							table.getFieldParent5().orElse(""), table.getFieldParent6().orElse(""),
							table.getFieldParent7().orElse(""), table.getFieldParent8().orElse(""),
							table.getFieldParent9().orElse(""), table.getFieldParent10().orElse("") };

					String[] childFields = { table.getFieldChild1().orElse(""), table.getFieldChild2().orElse(""),
							table.getFieldChild3().orElse(""), table.getFieldChild4().orElse(""),
							table.getFieldChild5().orElse(""), table.getFieldChild6().orElse(""),
							table.getFieldChild7().orElse(""), table.getFieldChild8().orElse(""),
							table.getFieldChild9().orElse(""), table.getFieldChild10().orElse("") };

					boolean isFirstOnStatement = true;
					for (int i = 0; i < parentFields.length; i++) {
						if (!Strings.isNullOrEmpty(parentFields[i]) && !Strings.isNullOrEmpty(childFields[i])) {
							if (!isFirstOnStatement) {
								DELETE_BY_TABLE_SQL.append(" AND ");
							}
							isFirstOnStatement = false;
							DELETE_BY_TABLE_SQL.append("p." + parentFields[i] + "=" + "t." + childFields[i]);
						}
					}
				}

				DELETE_BY_TABLE_SQL.append(" WHERE 1=1  ");

				// fix bug #108095 - add defaultCondKeyQuery
				Optional<String> defaultCondKeyQuery = table.getDefaultCondKeyQuery();
				if (defaultCondKeyQuery.isPresent()) {
					if (defaultCondKeyQuery.get() != null && !"null".equals(defaultCondKeyQuery.get())
							&& !defaultCondKeyQuery.get().isEmpty()) {
						DELETE_BY_TABLE_SQL.append(" AND " + table.getDefaultCondKeyQuery().get() + " ");
					}
				}

				if (hasParentTblFlg && !StringUtils.isBlank(whereSid) && !StringUtils.isBlank(employeeId)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" p.").append(whereSid).append(" = '").append(employeeId)
							.append("'");
				} else if (!hasParentTblFlg && !StringUtils.isBlank(whereSid) && !StringUtils.isBlank(employeeId)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" t.").append(whereSid).append(" = '").append(employeeId)
							.append("'");
				}

				if (hasParentTblFlg && !StringUtils.isBlank(whereCid) && !StringUtils.isBlank(cid)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" p.").append(whereCid).append(" = '").append(cid)
							.append("'");
				} else if (!hasParentTblFlg && !StringUtils.isBlank(whereCid) && !StringUtils.isBlank(cid)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" t.").append(whereCid).append(" = '").append(cid)
							.append("'");
				}
				System.out.println("QUERY:  " + DELETE_BY_TABLE_SQL.toString());
				sqlContent = DELETE_BY_TABLE_SQL.toString();
				Query query = em.createNativeQuery(DELETE_BY_TABLE_SQL.toString());
				query.executeUpdate();
			}
		} catch (Exception error) {
			String target = employeeCode;
			String errorContent = error.getMessage();
			GeneralDate targetDate = null;
			String contentSql = sqlContent;
			String processingContent = "履歴データ削除  " + TextResource.localize("CMF004_462") + " " + table.getTableJapaneseName();
			throw new DelDataException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
		}
	}

	public void addTargetEmployee(Target domain) {
		this.commandProxy().insert(SspmtTarget.toEntity(domain));
	}

	private static TableList fromDomain(Object[] objectSurfaceItem) {

		return new TableList(objectSurfaceItem[0].toString(), objectSurfaceItem[1].toString(),
				objectSurfaceItem[2].toString(), objectSurfaceItem[3].toString(), objectSurfaceItem[4].toString(),
				objectSurfaceItem[5].toString(), Integer.parseInt(objectSurfaceItem[6].toString()),
				Integer.parseInt(objectSurfaceItem[7].toString()), Integer.parseInt(objectSurfaceItem[8].toString()),
				objectSurfaceItem[9].toString(), Integer.parseInt(objectSurfaceItem[10].toString()),
				objectSurfaceItem[11].toString(), objectSurfaceItem[12].toString());
	}

	@Override
	public void deleteEmployeeDataRecovery(String dataRecoveryProcessId, List<String> employeeIdList) {
		CollectionUtil.split(employeeIdList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subEmployeeIdList -> {
			this.getEntityManager().createQuery(DELETE_BY_LIST_ID_EMPLOYEE, SspmtTarget.class)
			.setParameter("dataRecoveryProcessId", dataRecoveryProcessId)
			.setParameter("employeeIdList", subEmployeeIdList).executeUpdate();
		});

	}

	@Override
	public List<TableList> getByDataRecoveryId(String dataRecoveryProcessId) {
		List<SspmtTableList> listTable = this.getEntityManager()
				.createQuery(SELECT_TABLE_LIST_BY_DATA_RECOVERY_QUERY_STRING, SspmtTableList.class)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).getResultList();
		return listTable.stream().map(item -> item.toDomain()).collect(Collectors.toList());
	}

	@Override
	public void updateCategorySelect(int selectionTarget, String dataRecoveryProcessId, List<String> listCheckCate) {
		CollectionUtil.split(listCheckCate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			this.getEntityManager().createQuery(UPDATE_BY_LIST_CATEGORY_ID, SspmtTarget.class)
				.setParameter("selectionTarget", selectionTarget)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId)
				.setParameter("listCheckCate", subList).executeUpdate();
		});
	}

	@Override
	public void updateCategorySelectByDateFromTo(String startOfPeriod, String endOfPeriod, String dataRecoveryProcessId,
			String checkCate) {
		this.getEntityManager().createQuery(UPDATE_DATE_FROM_TO_BY_LIST_CATEGORY_ID, SspmtTarget.class)
				.setParameter("startOfPeriod", startOfPeriod).setParameter("endOfPeriod", endOfPeriod)
				.setParameter("dataRecoveryProcessId", dataRecoveryProcessId).setParameter("checkCate", checkCate)
				.executeUpdate();
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public void deleteTableListByDataStorageProcessingId(String dataRecoveryProcessId) {

		if (dataRecoveryProcessId != null) {
			this.getEntityManager().createQuery(DELETE_TABLE_LIST, SspmtTableList.class)
			.setParameter("dataRecoveryProcessId", dataRecoveryProcessId)
			.executeUpdate();

		}
		
	}

	@Override
	public void addRestorationTarget(RestorationTarget domain) {
		this.commandProxy().insert(SspmtRestorationTarget.toEntity(domain));

	}

	@Override
	public void deleteTransactionEmployeeHis(TableList table, String whereCid, String whereSid, String cid,
			String employeeId , String employeeCode , String dataRecoveryProcessId) {
		String sqlContent = "";
		try {
			EntityManager em = this.getEntityManager();

			if (table.getTableEnglishName() != null) {
				StringBuilder DELETE_BY_TABLE_SQL = new StringBuilder("DELETE t FROM ");
				DELETE_BY_TABLE_SQL.append(table.getTableEnglishName()).append(" t ");
				boolean hasParentTblFlg = false;

				// アルゴリズム「親テーブルをJOINする」を実行する
				if (table.getHasParentTblFlg() == NotUseAtr.USE && table.getParentTblName().isPresent()) {
					hasParentTblFlg = true;
					DELETE_BY_TABLE_SQL.append(" INNER JOIN ").append(table.getParentTblName().get()).append(" p ON ");

					String[] parentFields = { table.getFieldParent1().orElse(""), table.getFieldParent2().orElse(""),
							table.getFieldParent3().orElse(""), table.getFieldParent4().orElse(""),
							table.getFieldParent5().orElse(""), table.getFieldParent6().orElse(""),
							table.getFieldParent7().orElse(""), table.getFieldParent8().orElse(""),
							table.getFieldParent9().orElse(""), table.getFieldParent10().orElse("") };

					String[] childFields = { table.getFieldChild1().orElse(""), table.getFieldChild2().orElse(""),
							table.getFieldChild3().orElse(""), table.getFieldChild4().orElse(""),
							table.getFieldChild5().orElse(""), table.getFieldChild6().orElse(""),
							table.getFieldChild7().orElse(""), table.getFieldChild8().orElse(""),
							table.getFieldChild9().orElse(""), table.getFieldChild10().orElse("") };

					boolean isFirstOnStatement = true;
					for (int i = 0; i < parentFields.length; i++) {
						if (!Strings.isNullOrEmpty(parentFields[i]) && !Strings.isNullOrEmpty(childFields[i])) {
							if (!isFirstOnStatement) {
								DELETE_BY_TABLE_SQL.append(" AND ");
							}
							isFirstOnStatement = false;
							DELETE_BY_TABLE_SQL.append("p." + parentFields[i] + "=" + "t." + childFields[i]);
						}
					}
				}

				DELETE_BY_TABLE_SQL.append(" WHERE 1=1  ");

				// fix bug #108095 - add defaultCondKeyQuery
				Optional<String> defaultCondKeyQuery = table.getDefaultCondKeyQuery();
				if (defaultCondKeyQuery.isPresent()) {
					if (defaultCondKeyQuery.get() != null && !"null".equals(defaultCondKeyQuery.get())
							&& !defaultCondKeyQuery.get().isEmpty()) {
						DELETE_BY_TABLE_SQL.append(" AND " + table.getDefaultCondKeyQuery().get() + " ");
					}
				}

				if (hasParentTblFlg && !StringUtils.isBlank(whereSid) && !StringUtils.isBlank(employeeId)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" p.").append(whereSid).append(" = '").append(employeeId)
							.append("'");
				} else if (!hasParentTblFlg && !StringUtils.isBlank(whereSid) && !StringUtils.isBlank(employeeId)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" t.").append(whereSid).append(" = '").append(employeeId)
							.append("'");
				}

				if (hasParentTblFlg && !StringUtils.isBlank(whereCid) && !StringUtils.isBlank(cid)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" p.").append(whereCid).append(" = '").append(cid)
							.append("'");
				} else if (!hasParentTblFlg && !StringUtils.isBlank(whereCid) && !StringUtils.isBlank(cid)) {
					DELETE_BY_TABLE_SQL.append(" AND ").append(" t.").append(whereCid).append(" = '").append(cid)
							.append("'");
				}

				System.out.println("QUERY:  " + DELETE_BY_TABLE_SQL.toString());
				sqlContent = DELETE_BY_TABLE_SQL.toString();
				Query query = em.createNativeQuery(DELETE_BY_TABLE_SQL.toString());
				query.executeUpdate();
			}
		} catch (Exception error) {
			String target = employeeCode;
			String errorContent = error.getMessage();
			GeneralDate targetDate = null;
			String contentSql = sqlContent;
			String processingContent = "履歴データ削除  " + TextResource.localize("CMF004_462") + " " +  table.getTableJapaneseName();
			throw new DelDataException(dataRecoveryProcessId, target, errorContent, targetDate, contentSql, processingContent);
		}
	}
	
	@Override
	public void addAllTargetEmployee(List<Target> listTarget) {
		this.commandProxy().insertAll(listTarget.stream().map(x->{
			return SspmtTarget.toEntity(x);
		}).collect(Collectors.toList()));
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public List<String> getTypeColumnNotNull(String tableName) {
		List<String> data = new ArrayList<>();
		if (tableName != null) {
			StringBuilder SELECT_BY_TABLE_SQL = new StringBuilder("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ");
			SELECT_BY_TABLE_SQL.append("'"+tableName+"'").append(" AND IS_NULLABLE = 'NO'");
			data = this.getEntityManager().createNativeQuery(SELECT_BY_TABLE_SQL.toString()).getResultList();
		}
		return data;
	}
}
