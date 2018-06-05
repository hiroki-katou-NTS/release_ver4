package nts.uk.ctx.sys.assist.infra.repository.tablelist;

import java.awt.event.ItemEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import com.google.common.base.Strings;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.dom.tablelist.TableListRepository;
import nts.uk.ctx.sys.assist.infra.entity.tablelist.SspmtTableList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class JpaTableListRepository extends JpaRepository implements TableListRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT t FROM SspmtTableList t";
	private static final String COMPANY_CD = "0";
	private static final String EMPLOYEE_CD = "5";
	private static final String YEAR = "6";
	private static final String YEAR_MONTH = "7";
	private static final String YEAR_MONTH_DAY = "8";

	@Override
	public void add(TableList domain) {
		this.commandProxy().insert(SspmtTableList.toEntity(domain));
	}

	@Override
	public Class<?> getTypeForTableName(String tableName) {
		for (EntityType<?> entityType : this.getEntityManager().getMetamodel().getEntities()) {
			Table table = entityType.getJavaType().getAnnotation(Table.class);
			if (table != null && table.name().equals(tableName)) {
				return entityType.getJavaType();
			}
		}
		return null;
	}

	@Override
	public String getFieldForColumnName(Class<?> tableType, String columnName) {
		for (Field field : tableType.getDeclaredFields()) {
			if (field.isAnnotationPresent(EmbeddedId.class)) {
				Class<?> pk = field.getType();
				for (Field fieldPk : pk.getDeclaredFields()) {
					Column columnPk = fieldPk.getDeclaredAnnotation(Column.class);
					if (columnPk != null && columnPk.name().equals(columnName)) {
						return field.getName() + "." + fieldPk.getName();
					}
				}
			}
			Column column = field.getDeclaredAnnotation(Column.class);
			if (column != null && column.name().equals(columnName)) {
				return field.getName();
			}
		}
		return "";
	}
	
	@Override
	public List<TableList> getByOffsetAndNumber(int offset, int number) {
		List<SspmtTableList> listTable = this.getEntityManager()
				.createQuery(SELECT_ALL_QUERY_STRING, SspmtTableList.class)
				.setFirstResult(offset).setMaxResults(number).getResultList();
		
		return listTable.stream().map(item -> item.toDomain()).collect(Collectors.toList());
	}
	
	@Override
	public List<TableList> getAllTableList() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtTableList.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public List<?> getDataDynamic(TableList tableList) {
		Class<?> tableExport = this.getTypeForTableName(tableList.getTableEnglishName());

		StringBuffer query = new StringBuffer("");
		
		// Select
		query.append("SELECT t");

		// From
		query.append(" FROM ").append(tableExport.getSimpleName()).append(" t");
		Class<?> tableParent = null;
		if (tableList.getHasParentTblFlg() == NotUseAtr.USE) {
			tableParent = this.getTypeForTableName(tableList.getParentTblName());
			query.append(" INNER JOIN ").append(tableParent.getSimpleName()).append(" p ON ");

			String[] parentFields = { tableList.getFieldParent1(), tableList.getFieldParent2(),
					tableList.getFieldParent3(), tableList.getFieldParent4(), tableList.getFieldParent5(),
					tableList.getFieldParent6(), tableList.getFieldParent7(), tableList.getFieldParent8(),
					tableList.getFieldParent9(), tableList.getFieldParent10() };

			String[] childFields = { tableList.getFieldChild1(), tableList.getFieldChild2(), tableList.getFieldChild3(),
					tableList.getFieldChild4(), tableList.getFieldChild5(), tableList.getFieldChild6(),
					tableList.getFieldChild7(), tableList.getFieldChild8(), tableList.getFieldChild9(),
					tableList.getFieldChild10() };

			boolean isFirstOnStatement = true;
			for (int i = 0; i < parentFields.length; i++) {
				String onStatement = getOnStatement(tableParent, parentFields[i], tableExport, childFields[i]);
				if (Strings.isNullOrEmpty(onStatement)) {
					if (!isFirstOnStatement) {
						query.append(" AND ");
					}
					isFirstOnStatement = false;
					query.append(onStatement);
				}
			}
		}

		String[] fieldKeyQuerys = { tableList.getFieldKeyQuery1(), tableList.getFieldKeyQuery2(),
				tableList.getFieldKeyQuery3(), tableList.getFieldKeyQuery4(), tableList.getFieldKeyQuery5(),
				tableList.getFieldKeyQuery6(), tableList.getFieldKeyQuery7(), tableList.getFieldKeyQuery8(),
				tableList.getFieldKeyQuery9(), tableList.getFieldKeyQuery10() };
		String[] clsKeyQuerys = { tableList.getClsKeyQuery1(), tableList.getClsKeyQuery2(), tableList.getClsKeyQuery3(),
				tableList.getClsKeyQuery4(), tableList.getClsKeyQuery5(), tableList.getClsKeyQuery6(),
				tableList.getClsKeyQuery7(), tableList.getClsKeyQuery8(), tableList.getClsKeyQuery9(),
				tableList.getClsKeyQuery10() };

		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (clsKeyQuerys[i] == EMPLOYEE_CD) {
				if (tableList.getHasParentTblFlg() == NotUseAtr.USE) {
					query.append(" INNER JOIN SspmtTargetEmployees e ON e.targetEmployeesPk.Sid = p.");
					query.append(this.getFieldForColumnName(tableParent, fieldKeyQuerys[i]));
				} else {
					query.append(" INNER JOIN SspmtTargetEmployees e ON e.targetEmployeesPk.Sid = t.");
					query.append(this.getFieldForColumnName(tableExport, fieldKeyQuerys[i]));
				}
			}
		}

		// Where
		query.append(" WHERE 1 = 1 ");

		List<Integer> companyCdIndexs = new ArrayList<Integer>();
		List<Integer> yearIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthIndexs = new ArrayList<Integer>();
		List<Integer> yearMonthDayIndexs = new ArrayList<Integer>();
		for (int i = 0; i < clsKeyQuerys.length; i++) {
			if (clsKeyQuerys[i] == null)
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
				query.append(" t.");
				query.append(this.getFieldForColumnName(tableExport, fieldKeyQuerys[index]));
				query.append(" = :companyId ");
				params.put("companyId", AppContexts.user().companyId());
			}
			query.append(" ) ");
		}
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
			query.append(" AND ( ");
			boolean isFirstOrStatement = true;
			for (Integer index : yearIndexs) {
				if (!isFirstOrStatement) {
					query.append(" OR ");
				}
				isFirstOrStatement = false;
				query.append(" (t.");
				query.append(this.getFieldForColumnName(tableExport, fieldKeyQuerys[index]));
				query.append(" >= :startDate ");
				params.put("startDate", tableList.getSaveDateFrom());
				query.append(" AND ");
				query.append(" t.");
				query.append(this.getFieldForColumnName(tableExport, fieldKeyQuerys[index]));
				query.append(" <= :endDate) ");
				params.put("endDate", tableList.getSaveDateTo());
			}
			query.append(" ) ");
		}

		// 抽出条件キー固定
		String extractCondKeyFix = tableList.getDefaultCondKeyQuery();
		if (!Strings.isNullOrEmpty(extractCondKeyFix)) {
			String[] extractCondArray = extractCondKeyFix.split(" ");
			for (int i = 0; i < extractCondArray.length; i++) {
				String fieldName = this.getFieldForColumnName(tableExport, extractCondArray[i]);
				if (!Strings.isNullOrEmpty(fieldName)) {
					extractCondArray[i] = "t." + fieldName;
				}
			}

			query.append(String.join(" ", extractCondArray));
		}

		TypedQueryWrapper<?> queryWrapper = this.queryProxy().query(query.toString(), tableExport);
		for(Entry<String, Object> entry : params.entrySet()) {
			queryWrapper = queryWrapper.setParameter(entry.getKey(), entry.getValue());
		}
		return queryWrapper.getList();		
	}

	private String getOnStatement(Class<?> parentTable, String parentColumn, Class<?> childTable, String childColumn) {
		if (!Strings.isNullOrEmpty(parentColumn) && !Strings.isNullOrEmpty(childColumn)) {
			String parentColumnJpa = this.getFieldForColumnName(parentTable, parentColumn);
			String childColumnJpa = this.getFieldForColumnName(childTable, childColumn);
			return "p." + parentColumnJpa + "=" + "t." + childColumnJpa;
		}
		return "";
	}

}
