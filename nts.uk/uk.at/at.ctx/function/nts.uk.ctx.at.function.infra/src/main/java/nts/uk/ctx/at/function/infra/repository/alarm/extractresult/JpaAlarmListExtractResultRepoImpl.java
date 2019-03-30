package nts.uk.ctx.at.function.infra.repository.alarm.extractresult;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResult;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.AlarmListExtractResultRepo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractEmployeeErAlData;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractEmployeeInfo;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult.ExtractExecuteType;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexData;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexDataPK;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexEmpData;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexEmpDataPK;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexEmpEralData;
import nts.uk.ctx.at.function.infra.entity.alarm.extractresult.KfnmtAlexEmpEralDataPK;

@Stateless
public class JpaAlarmListExtractResultRepoImpl extends JpaRepository implements AlarmListExtractResultRepo {

	private static final String FIND_ALEX_DATA = "SELECT EXECUTE_ID FROM KFNMT_ALEX_DATA WHERE CID = ? AND EXECUTE_TYPE = ? AND EXECUTE_EMP_ID = ?";
	
	@Override
	public List<AlarmListExtractResult> findBy(String compId, String executeEmpId, ExtractExecuteType type) {
		
		List<String> executeIds = findExecuteIds(compId, executeEmpId, type); 
		
		List<ExtractEmployeeInfo> empInfo = getExtractedEmp(executeIds); 
		List<ExtractEmployeeErAlData> empErrors = getExtractedError(executeIds); 
		
		return executeIds.stream().map(c -> new AlarmListExtractResult(executeEmpId, compId, type, c, 
								empInfo.stream().filter(e -> e.getExecuteId().equals(c)).collect(Collectors.toList()), 
								empErrors.stream().filter(e -> e.getExecuteId().equals(c)).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<AlarmListExtractResult> findByLimited(String compId, String executeEmpId, ExtractExecuteType type, String executeId, int limitedWith) {
		
		List<String> executeIds = findExecuteIds(compId, executeEmpId, type).stream()
				.filter(c -> c.equals(executeId)).collect(Collectors.toList()); 
		if(executeIds.isEmpty()){
			return Optional.empty();
		}

		List<ExtractEmployeeErAlData> empErrors = getExtractedErrorWithLimit(executeIds, limitedWith); 
		List<ExtractEmployeeInfo> empInfo = getExtractedEmp(executeIds, 
				empErrors.stream().map(c -> c.getEmployeeId()).distinct().collect(Collectors.toList())); 
		
		return Optional.of(new AlarmListExtractResult(executeEmpId, compId, type, executeId, empInfo, empErrors));
	}
	
	@Override
	public Optional<AlarmListExtractResult> findBy(String compId, String executeEmpId, ExtractExecuteType type, String executeId) {
		
		List<String> executeIds = findExecuteIds(compId, executeEmpId, type).stream()
				.filter(c -> c.equals(executeId)).collect(Collectors.toList()); 
		if(executeIds.isEmpty()){
			return Optional.empty();
		}
		
		List<ExtractEmployeeInfo> empInfo = getExtractedEmp(executeIds); 
		List<ExtractEmployeeErAlData> empErrors = getExtractedError(executeIds); 
		
		return Optional.of(new AlarmListExtractResult(executeEmpId, compId, type, executeId, empInfo, empErrors));
	}
	
	@Override
	public Optional<AlarmListExtractResult> findResultEmpInfo(String compId, String executeEmpId, ExtractExecuteType type, String executeId) {
		
		List<String> executeIds = findExecuteIds(compId, executeEmpId, type).stream()
				.filter(c -> c.equals(executeId)).collect(Collectors.toList()); 
		if(executeIds.isEmpty()){
			return Optional.empty();
		}
		
		List<ExtractEmployeeInfo> empInfo = getExtractedEmp(executeIds); 
//		List<ExtractEmployeeErAlData> empErrors = getExtractedError(executeIds); 
		
		return Optional.of(new AlarmListExtractResult(executeEmpId, compId, type, executeId, empInfo, new ArrayList<>()));
	}
	
	@Override
	public void insert(List<AlarmListExtractResult> result) {
		if(result.isEmpty()){
			
		}
		List<String> executeIds = findExecuteIds(result.get(0).getCid(), result.get(0).getExecuteEmpId(), result.get(0).getExecuteType());
		
		removeBeforeData(executeIds);

		List<Object> entities = new ArrayList<>();
		result.stream().forEach(r -> {
			entities.add(new KfnmtAlexData(new KfnmtAlexDataPK(r.getExecuteEmpId(), r.getCid(), r.getExecuteType().value, r.getExecuteId())));
			entities.addAll(r.getEmpInfos().stream().map(ei -> new KfnmtAlexEmpData(new KfnmtAlexEmpDataPK(ei.getExecuteId(), ei.getEmployeeId(), ei.getWorkplaceId()), 
																					ei.getEmployeeCode(), ei.getEmployeeName(), ei.getWorkplaceName(), 
																					ei.getHierarchyCode(), ei.getWpWorkStartDate(), ei.getWpWorkEndDate()))
					.collect(Collectors.toList()));
			entities.addAll(r.getEmpEralData().stream().map(ei -> new KfnmtAlexEmpEralData(new KfnmtAlexEmpEralDataPK(ei.getExecuteId(), ei.getEmployeeId(), 
																							ei.getRecordId()), ei.getAlarmTime(), ei.getCategoryCode(), 
																							ei.getCategoryName(), ei.getAlarmItem(), ei.getAlarmMes(), ei.getComment()))
					.collect(Collectors.toList()));
		});
		
		this.commandProxy().insertAll(entities);
	}
	
	@SneakyThrows
	private void removeBeforeData(List<String> executeIds) {
		if(executeIds.isEmpty()){
			return;
		}
		String conditions = executeIds.stream().map(c -> "?").collect(Collectors.joining(","));
		try(PreparedStatement st = this.connection().prepareStatement("DELETE FROM KFNMT_ALEX_DATA WHERE EXECUTE_ID IN (" + conditions + ")")) {
			try(PreparedStatement st1 = this.connection().prepareStatement("DELETE FROM KFNMT_ALEX_EMP_DATA WHERE EXECUTE_ID IN (" + conditions + ")")) {
				try(PreparedStatement st2 = this.connection().prepareStatement("DELETE FROM KFNMT_ALEX_EMP_ERAL_DATA WHERE EXECUTE_ID IN (" + conditions + ")")) {
					for(int i = 1; i <= executeIds.size(); i++){
						st.setString(i, executeIds.get(i - 1));
						st1.setString(i, executeIds.get(i - 1));
						st2.setString(i, executeIds.get(i - 1));
					}
					st.executeUpdate();
					st1.executeUpdate();
					st2.executeUpdate();
				}
			}
		}
	}

	@SneakyThrows
	private List<String> findExecuteIds(String compId, String executeEmpId, ExtractExecuteType type) {
		try(PreparedStatement st = this.connection().prepareStatement(FIND_ALEX_DATA)) {
			st.setString(1, compId);
			st.setInt(2, type.value);
			st.setString(3, executeEmpId);
			
			return new NtsResultSet(st.executeQuery()).getList(r -> r.getString(1));
		}
	}

	@SneakyThrows
	private List<ExtractEmployeeInfo> getExtractedEmp(List<String> executeIds, List<String> employeeIds) {
		if(executeIds.isEmpty() || employeeIds.isEmpty()) {
			return new ArrayList<>();
		}
		List<ExtractEmployeeInfo> result = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {

			result.addAll(internalGetExtractedEmp(executeIds, empIds));
		});
		return result;
	}
	
	@SneakyThrows
	private List<ExtractEmployeeInfo> internalGetExtractedEmp(List<String> executeIds, List<String> employeeIds) {

		String findEmpData = "SELECT EXECUTE_ID, EMPLOYEE_ID, EMPLOYEE_CODE, EMPLOYEE_NAME, WORKPLACE_ID, WORKPLACE_NAME, "
				+ "HIERARCHY_CD, WORKPLACE_WORK_START_DATE, WORKPLACE_WORK_END_DATE FROM KFNMT_ALEX_EMP_DATA WHERE EXECUTE_ID IN ("
				+ executeIds.stream().map(c -> "?").collect(Collectors.joining(","))
				+ ") AND EMPLOYEE_ID IN ("
				+ employeeIds.stream().map(c -> "?").collect(Collectors.joining(","))
				+ ")";
		try(PreparedStatement st = this.connection().prepareStatement(findEmpData)) {
			for(int i = 1; i <= executeIds.size(); i++){
				st.setString(i, executeIds.get(i - 1));
			}
			for(int i = 1; i <= employeeIds.size(); i++){
				st.setString(i + executeIds.size(), employeeIds.get(i - 1));
			}
			return new NtsResultSet(st.executeQuery()).getList(r -> {
				return new ExtractEmployeeInfo(r.getString(1), r.getString(2), r.getString(5), r.getString(3), r.getString(4), 
						r.getString(6), r.getString(7), r.getGeneralDate(8), r.getGeneralDate(9));
			});
		}
		
	}
	
	@SneakyThrows
	private List<ExtractEmployeeInfo> getExtractedEmp(List<String> executeIds) {
		if(executeIds.isEmpty()) {
			return new ArrayList<>();
		}
		String findEmpData = "SELECT EXECUTE_ID, EMPLOYEE_ID, EMPLOYEE_CODE, EMPLOYEE_NAME, WORKPLACE_ID, WORKPLACE_NAME, "
				+ "HIERARCHY_CD, WORKPLACE_WORK_START_DATE, WORKPLACE_WORK_END_DATE FROM KFNMT_ALEX_EMP_DATA WHERE EXECUTE_ID IN ("
				+ executeIds.stream().map(c -> "?").collect(Collectors.joining(","))
				+ ") ORDER BY HIERARCHY_CD, EMPLOYEE_CODE";
		try(PreparedStatement st = this.connection().prepareStatement(findEmpData)) {
			for(int i = 1; i <= executeIds.size(); i++){
				st.setString(i, executeIds.get(i - 1));
			}
			
			return new NtsResultSet(st.executeQuery()).getList(r -> {
				return new ExtractEmployeeInfo(r.getString(1), r.getString(2), r.getString(5), r.getString(3), r.getString(4), 
						r.getString(6), r.getString(7), r.getGeneralDate(8), r.getGeneralDate(9));
			});
		}
	}
	
	@SneakyThrows
	private List<ExtractEmployeeErAlData> getExtractedErrorWithLimit(List<String> executeIds, int limit) {
		
		StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT er.EXECUTE_ID, er.ALARM_TARGET_TIME, er.CATEGORY_CODE, er.CATEGORY_NAME, er.ALARM_ITEM, er.ALARM_MESSAGE, ");
		queryBuilder.append(" er.COMMENT, er.EMPLOYEE_ID, er.RECORD_ID, em.HIERARCHY_CD, em.EMPLOYEE_CODE FROM KFNMT_ALEX_EMP_ERAL_DATA er" );
		queryBuilder.append(" JOIN KFNMT_ALEX_EMP_DATA em ON er.EMPLOYEE_ID = em.EMPLOYEE_ID" );
		queryBuilder.append(" WHERE er.EXECUTE_ID IN (");
		queryBuilder.append(executeIds.stream().map(c -> "?").collect(Collectors.joining(",")));
		queryBuilder.append(") ORDER BY em.HIERARCHY_CD, em.EMPLOYEE_CODE, er.ALARM_TARGET_TIME, er.CATEGORY_CODE, er.ALARM_ITEM " );
		queryBuilder.append(" OFFSET 0 ROWS FETCH FIRST " + limit + " ROWS ONLY " );
		
		return buildQueryAndGet(executeIds, queryBuilder.toString());
	}

	@SneakyThrows
	private List<ExtractEmployeeErAlData> getExtractedError(List<String> executeIds) {
		String query = "SELECT EXECUTE_ID, ALARM_TARGET_TIME, CATEGORY_CODE, CATEGORY_NAME, ALARM_ITEM, ALARM_MESSAGE, "
				+ "COMMENT, EMPLOYEE_ID, RECORD_ID FROM KFNMT_ALEX_EMP_ERAL_DATA WHERE EXECUTE_ID IN ("
				+ executeIds.stream().map(c -> "?").collect(Collectors.joining(","))
				+ ")";
		
		return buildQueryAndGet(executeIds, query);
	}

	private List<ExtractEmployeeErAlData> buildQueryAndGet(List<String> executeIds, String query)
			throws SQLException {
		if(executeIds.isEmpty()) {
			return new ArrayList<>();
		}
		
		try(PreparedStatement st = this.connection().prepareStatement(query)) {
			for(int i = 1; i <= executeIds.size(); i++){
				st.setString(i, executeIds.get(i - 1));
			}
			
			return new NtsResultSet(st.executeQuery()).getList(r -> {
				return new ExtractEmployeeErAlData(r.getString(1), r.getString(8), r.getString(9), r.getString(2), r.getInt(3),
						r.getString(4), r.getString(5), r.getString(6), r.getString(7));
			});
		}
	}


}
