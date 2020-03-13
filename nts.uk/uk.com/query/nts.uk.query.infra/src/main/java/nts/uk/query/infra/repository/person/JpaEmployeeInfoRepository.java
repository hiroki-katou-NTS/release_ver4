/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.infra.repository.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lombok.Data;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.query.model.person.BusinessTypeHistoryModel;
import nts.uk.query.model.person.ClassificationHistoryModel;
import nts.uk.query.model.person.EmployeeInfoQueryModel;
import nts.uk.query.model.person.EmployeeInfoRepository;
import nts.uk.query.model.person.EmployeeInfoResultModel;
import nts.uk.query.model.person.EmploymentHistoryModel;
import nts.uk.query.model.person.JobTitleHistoryModel;
import nts.uk.query.model.person.WorkPlaceHistoryModel;

/**
 * The Class JpaEmployeeInfoRepository.
 */
@Stateless
public class JpaEmployeeInfoRepository extends JpaRepository implements EmployeeInfoRepository {

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.query.model.person.EmployeeInfoRepository#find(nts.uk.query.model.
	 * person.EmployeeInfoQueryModel)
	 */
	
	private static final String SELECT_EMPLOYEE =  "SELECT"
			+ " SID, WKP_ID, WKP_STR_DATE, WKP_END_DATE, "
			+ " CLASSIFICATION_CODE, CLASS_STR_DATE, CLASS_END_DATE,"
			+ " EMP_CD, EMPLOYMENT_STR_DATE, EMPLOYMENT_END_DATE, "
			+ " JOB_TITLE_ID, JOB_STR_DATE, JOB_END_DATE, "
			+ " WORK_TYPE_CD, WORK_TYPE_STR_DATE, WORK_TYPE_END_DATE "
			+ " FROM EMPLOYEE_DATA_VIEW ";
	
	private static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static String SQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
		
	@Override
	public List<EmployeeInfoResultModel> find(EmployeeInfoQueryModel query) {
		// Check param.
		if (CollectionUtil.isEmpty(query.getEmployeeIds())) {
			return Collections.emptyList();
		}

		List<EmployeeDataDto> resultList = new ArrayList<>();
		
		StringBuilder selectBuilder = new StringBuilder();
		selectBuilder.append(SELECT_EMPLOYEE);
		
		GeneralDateTime startDateTime = GeneralDateTime.legacyDateTime(query.getPeroid().start().date());
		GeneralDateTime endDateTime = GeneralDateTime.legacyDateTime(query.getPeroid().end().date());
		
		StringBuilder whereBuilder = new StringBuilder()
				.append(" WHERE 1 = 1")
				.append(" OR ( 1 <> 1 ");

		// Find workplace.
		if (query.isFindWorkPlaceInfo()) {
			whereBuilder.append(" OR ( WKP_STR_DATE <= endDateTime AND WKP_END_DATE >= startDateTime ) ");
		}

		// Find classification.
		if (query.isFindClasssificationInfo()) {
			whereBuilder.append(" OR ( CLASS_STR_DATE <= endDateTime AND CLASS_END_DATE >= startDateTime ) ");
		}

		// Find JobTitle.
		if (query.isFindJobTilteInfo()) {
			whereBuilder.append(" OR ( JOB_STR_DATE <= endDateTime AND JOB_END_DATE >= startDateTime ) ");
		}

		// Find Employment
		if (query.isFindEmploymentInfo()) {
			whereBuilder.append(" OR ( EMPLOYMENT_STR_DATE <= endDateTime AND EMPLOYMENT_END_DATE >= startDateTime ) ");
			
		}

		// Find BusinessType.
		if (query.isFindBussinessTypeInfo()) {
			whereBuilder.append(" OR ( WORK_TYPE_STR_DATE <= endDateTime AND WORK_TYPE_END_DATE >= startDateTime ) ");
		}
		
		whereBuilder.append(" ) "); 
		// Add to condition list.
		// Where in employee ids.
		CollectionUtil.split(query.getEmployeeIds(), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			StringBuilder subWhereBuilder = new StringBuilder(whereBuilder);
			// remove element 2 in array.
			subWhereBuilder.append(" AND employee.SID in ('" + String.join("','", subList) + "')");
			String sql = selectBuilder.toString() + whereBuilder.toString();
			
			if(sql.contains("startDateTime")) {
				sql = sql.replaceAll("startDateTime", "'" + startDateTime.toString(DATE_TIME_FORMAT) + "'");
			}
			
			if(sql.contains("endDateTime")) {
				sql = sql.replaceAll("endDateTime", "'" + endDateTime.toString(DATE_TIME_FORMAT) + "'");
			}
			
			resultList.addAll(excuteQuery(sql));
	
		});

		// Group by employee id;
		Map<String, List<EmployeeDataDto>> resultMap = resultList.stream()
				.collect(Collectors.groupingBy(EmployeeDataDto::getSid));

		// Convert to result model.
		return resultMap.keySet()
				.stream()
				.map(key -> this.convertData(resultMap.get(key), query))
				.collect(Collectors.toList());

	}

	/**
	 * Convert data.
	 *
	 * @param datas the datas
	 * @param query the query
	 * @return the employee info result model
	 */
	private EmployeeInfoResultModel convertData(List<EmployeeDataDto> datas, EmployeeInfoQueryModel query) {
		EmployeeInfoResultModel info = new EmployeeInfoResultModel();
		info.setEmployeeId(datas.get(0).getSid());

		// Add workplace info.
		if (query.isFindWorkPlaceInfo()) {
			List<WorkPlaceHistoryModel> workPlaces = datas.stream()
					.filter(data -> data.getWorkplaceId() != null)
					.filter(this.distinctByKey(EmployeeDataDto::getWplStartDate))
					.map(data -> {
						return WorkPlaceHistoryModel.builder()
								.employeeId(data.getSid())
								.workplaceId(data.getWorkplaceId())
								.startDate(data.getWplStartDate())
								.endDate(data.getWplEndDate())
								.build();
					}).collect(Collectors.toList());
			info.setWorkPlaceHistorys(workPlaces);
		} else {
			info.setWorkPlaceHistorys(Collections.emptyList());
		}

		// Add classification info.
		if (query.isFindClasssificationInfo()) {
			List<ClassificationHistoryModel> classifications = datas.stream()
					.filter(data -> data.getClassificationCode() != null)
					.filter(this.distinctByKey(EmployeeDataDto::getClassStartDate))
					.map(data -> {
						return ClassificationHistoryModel.builder()
								.employeeId(data.getSid())
								.classificationCode(data.getClassificationCode())
								.startDate(data.getClassStartDate())
								.endDate(data.getClassEndDate())
								.build();
					}).collect(Collectors.toList());
			info.setClassificationHistorys(classifications);
		} else {
			info.setClassificationHistorys(Collections.emptyList());
		}

		// Add Employment info.
		if (query.isFindEmploymentInfo()) {
			List<EmploymentHistoryModel> employments = datas.stream()
					.filter(data -> data.getEmploymentCode() != null)
					.filter(this.distinctByKey(EmployeeDataDto::getEmpStartDate))
					.map(data -> {
						return EmploymentHistoryModel.builder()
								.employeeId(data.getSid())
								.employmentCode(data.getEmploymentCode())
								.startDate(data.getEmpStartDate())
								.endDate(data.getEmpEndDate())
								.build();
					}).collect(Collectors.toList());
			info.setEmploymentHistorys(employments);
		} else {
			info.setEmploymentHistorys(Collections.emptyList());
		}

		// Add Job title info.
		if (query.isFindJobTilteInfo()) {
			List<JobTitleHistoryModel> jobTitles = datas.stream()
					.filter(data -> data.getJobId() != null)
					.filter(this.distinctByKey(EmployeeDataDto::getJobStartDate))
					.map(data -> {
						return JobTitleHistoryModel.builder()
								.employeeId(data.getSid())
								.jobId(data.getJobId())
								.startDate(data.getJobStartDate())
								.endDate(data.getJobEndDate())
								.build();
					}).collect(Collectors.toList());
			info.setJobTitleHistorys(jobTitles);
		} else {
			info.setJobTitleHistorys(Collections.emptyList());
		}

		// Add Bussiness type info.
		if (query.isFindBussinessTypeInfo()) {
			List<BusinessTypeHistoryModel> businessTypes = datas.stream()
					.filter(data -> data.getBusinessTypeCode() != null)
					.filter(this.distinctByKey(EmployeeDataDto::getBusStartDate))
					.map(data -> {
						return BusinessTypeHistoryModel.builder()
								.employeeId(data.getSid())
								.businessTypeCode(data.getBusinessTypeCode())
								.startDate(data.getBusStartDate())
								.endDate(data.getBusEndDate())
								.build();
					}).collect(Collectors.toList());
			info.setBusinessTypeHistorys(businessTypes);
		} else {
			info.setBusinessTypeHistorys(Collections.emptyList());
		}
		return info;
	}
	
	/**
	 * Distinct by key.
	 *
	 * @param <T> the generic type
	 * @param keyExtractor the key extractor
	 * @return the java.util.function. predicate
	 */
	private <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	private List<EmployeeDataDto> excuteQuery(String sql) {
		
		Query query = this.getEntityManager()
				.createNativeQuery(sql);
		
		@SuppressWarnings("unchecked")
		List<Object[]> queryRs = query.getResultList();
		
		List<EmployeeDataDto> results = new ArrayList<>();
		
		queryRs.forEach(object -> {
			EmployeeDataDto result = new EmployeeDataDto();
			result.setSid(String.valueOf(object[0]));
			result.setWorkplaceId(String.valueOf(object[1]));
			result.setWplStartDate(GeneralDateTime.fromString(String.valueOf(object[3]), SQL_DATE_TIME_FORMAT));
			result.setWplEndDate(GeneralDateTime.fromString(String.valueOf(object[4]), SQL_DATE_TIME_FORMAT));
			result.setClassificationCode(String.valueOf(object[5]));
			result.setClassStartDate(GeneralDateTime.fromString(String.valueOf(object[6]), SQL_DATE_TIME_FORMAT));
			result.setClassEndDate(GeneralDateTime.fromString(String.valueOf(object[7]), SQL_DATE_TIME_FORMAT));
			result.setEmploymentCode(String.valueOf(object[8]));
			result.setEmpStartDate(GeneralDateTime.fromString(String.valueOf(object[9]), SQL_DATE_TIME_FORMAT));
			result.setEmpEndDate(GeneralDateTime.fromString(String.valueOf(object[10]), SQL_DATE_TIME_FORMAT));
			result.setJobId(String.valueOf(object[11]));
			result.setJobStartDate(GeneralDateTime.fromString(String.valueOf(object[12]), SQL_DATE_TIME_FORMAT));
			result.setJobEndDate(GeneralDateTime.fromString(String.valueOf(object[13]), SQL_DATE_TIME_FORMAT));
			result.setBusinessTypeCode(String.valueOf(object[14]));
			result.setBusStartDate(GeneralDate.fromString(String.valueOf(object[15]), SQL_DATE_TIME_FORMAT));
			result.setBusEndDate(GeneralDate.fromString(String.valueOf(object[16]), SQL_DATE_TIME_FORMAT));
		});
		
		return results;
	}
	
	@Data
	public class EmployeeDataDto {
		/** The employee id. */
		private String sid;
		
		/** The workplace id. */
		private String workplaceId;
		
		/** The start date. */
		private GeneralDateTime wplStartDate;
		
		/** The end date. */
		private GeneralDateTime wplEndDate;
		
		/** The classification code. */
		private String classificationCode;
		
		/** The start date. */
		private GeneralDateTime classStartDate;
		
		/** The end date. */
		private GeneralDateTime classEndDate;
		
		/** The employment code. */
		private String employmentCode;
		
		/** The start date. */
		private GeneralDateTime empStartDate;
		
		/** The end date. */
		private GeneralDateTime empEndDate;
		
		/** The job id. */
		private String jobId;
		
		/** The start date. */
		private GeneralDateTime jobStartDate;
		
		/** The end date. */
		private GeneralDateTime jobEndDate;
		
		/** The business type code. */
		private String businessTypeCode;
		
		/** The start date. */
		private GeneralDate busStartDate;
		
		/** The end date. */
		private GeneralDate busEndDate;
	}

}
