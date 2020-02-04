/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.infra.repository.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.infra.entity.employee.order.BsymtEmpOrderCond;
import nts.uk.ctx.bs.employee.infra.entity.employee.order.BsymtEmployeeOrder;
import nts.uk.ctx.bs.employee.infra.entity.employee.order.BsymtEmployeeOrderPK;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.query.infra.entity.employee.EmployeeDataView;
import nts.uk.query.infra.entity.employee.EmployeeDataView_;
import nts.uk.query.model.employee.CCG001SystemType;
import nts.uk.query.model.employee.EmployeeSearchQuery;
import nts.uk.query.model.employee.RegularSortingType;
import nts.uk.query.model.employee.RegulationInfoEmployee;
import nts.uk.query.model.employee.RegulationInfoEmployeeRepository;
import nts.uk.query.model.employee.SortingConditionOrder;

/**
 * The Class JpaRegulationInfoEmployeeRepository.
 */
@Stateless
public class JpaRegulationInfoEmployeeRepository extends JpaRepository implements RegulationInfoEmployeeRepository {

	/** The Constant LEAVE_ABSENCE_QUOTA_NO. */
	public static final int LEAVE_ABSENCE_QUOTA_NO = 1;

	/** The Constant NOT_DELETED. */
	private static final int NOT_DELETED = 0;

	/** The Constant MAX_WHERE_IN. */
	private static final int MAX_WHERE_IN = 1000;
	
	/** The Constant MAX_PARAMETERS. */
	private static final int MAX_PARAMETERS = 800;

	/** The Constant NAME_TYPE. */
	// 現在は、氏名の種類を選択する機能がないので、「ビジネスネーム日本語」固定で
	// => 「氏名カナ」 ＝ 「ビジネスネームカナ」
	private static final int NAME_TYPE = 1;

	private static final String FIND_EMPLOYEE = "SELECT e, p"
			+ " FROM BsymtEmployeeDataMngInfo e"
			+ " LEFT JOIN BpsmtPerson p ON p.bpsmtPersonPk.pId = e.bsymtEmployeeDataMngInfoPk.pId"
			+ " WHERE e.bsymtEmployeeDataMngInfoPk.sId IN :listSid";

	private static final String FIND_WORKPLACE = "SELECT awh.sid, wi.wkpcd, wi.bsymtWorkplaceInfoPK.wkpid, wi.wkpName"
			+ " FROM BsymtAffiWorkplaceHist awh"
			+ " LEFT JOIN BsymtAffiWorkplaceHistItem awhi ON awhi.hisId = awh.hisId"
			+ " LEFT JOIN BsymtWorkplaceHist wh ON wh.bsymtWorkplaceHistPK.wkpid = awhi.workPlaceId"
			+ " LEFT JOIN BsymtWorkplaceInfo wi ON wi.bsymtWorkplaceInfoPK.historyId = wh.bsymtWorkplaceHistPK.historyId"
			+ " AND wi.bsymtWorkplaceInfoPK.wkpid = wh.bsymtWorkplaceHistPK.wkpid"
			+ " WHERE awh.sid IN :listSid"
			+ " AND awh.strDate <= :refDate"
			+ " AND awh.endDate >= :refDate"
			+ " AND wh.strD <= :refDate"
			+ " AND wh.endD >= :refDate";

	private static final String EMPTY_LIST = "EMPTY_LIST";
	private static final Integer ELEMENT_300 = 300;
	private static final Integer ELEMENT_800 = 800;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.model.EmployeeQueryModelRepository#find(nts.uk.query.model.
	 * EmployeeSearchQuery)
	 */
	@Override
	public List<RegulationInfoEmployee> find(String comId, EmployeeSearchQuery paramQuery) {
		// max paramenter count = 800
		int countParameter = 0;
		
		// Return empty list if all status of employee = 対象外
		if (!paramQuery.getIncludeIncumbents() && !paramQuery.getIncludeOccupancy() && !paramQuery.getIncludeRetirees()
				&& !paramQuery.getIncludeWorkersOnLeave()) {
			return Collections.emptyList();
		}
		EntityManager em = this.getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EmployeeDataView> cq = cb.createQuery(EmployeeDataView.class);
		Root<EmployeeDataView> root = cq.from(EmployeeDataView.class);

		// Constructing condition.
		List<Predicate> conditions = new ArrayList<>();
		List<String> employmentCodes = new ArrayList<>(Optional.ofNullable(paramQuery.getEmploymentCodes()).orElse(Collections.emptyList()));
		List<String> workplaceCodes = new ArrayList<>(Optional.ofNullable(paramQuery.getWorkplaceCodes()).orElse(Collections.emptyList()));
		List<String> classificationCodes = new ArrayList<>(Optional.ofNullable(paramQuery.getClassificationCodes()).orElse(Collections.emptyList()));
		List<String> jobTitleCodes = new ArrayList<>(Optional.ofNullable(paramQuery.getJobTitleCodes()).orElse(Collections.emptyList()));
		List<String> worktypeCodes = new ArrayList<>(Optional.ofNullable(paramQuery.getWorktypeCodes()).orElse(Collections.emptyList()));
		List<Integer> closureIds = new ArrayList<>(Optional.ofNullable(paramQuery.getClosureIds()).orElse(Collections.emptyList()));
		GeneralDateTime baseDate = paramQuery.getBaseDate();
		
		// Add company condition 
		conditions.add(cb.equal(root.get(EmployeeDataView_.cid), comId));

		// Add NOT_DELETED condition
		conditions.add(cb.equal(root.get(EmployeeDataView_.delStatusAtr), NOT_DELETED));
		countParameter += 2;

		// employment condition
		Predicate empCondition = cb.and(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.employmentStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.employmentEndDate), baseDate));
		if (paramQuery.getFilterByEmployment()) {
			// return empty list if condition code list is empty
			if (employmentCodes.isEmpty()) {
				return Collections.emptyList();
			}

			// update query conditions
			conditions.add(empCondition);
			countParameter += 2;				
		} else {
			conditions.add(cb.or(cb.isNull(root.get(EmployeeDataView_.employmentStrDate)), empCondition));
			countParameter += 3;
		}

		// workplace condition
		Predicate wplCondition = cb.and(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.wplStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.wplEndDate), baseDate),
				cb.lessThanOrEqualTo(root.get(EmployeeDataView_.wplInfoStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.wplInfoEndDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.wkpConfEndDate), baseDate),
				cb.lessThanOrEqualTo(root.get(EmployeeDataView_.wkpConfStrDate), baseDate));
		if (paramQuery.getFilterByWorkplace()) {
			// return empty list if condition code list is empty
			if (workplaceCodes.isEmpty()) {
				return Collections.emptyList();
			}

			// update query conditions
			conditions.add(wplCondition);
			countParameter += 6;
		} else {
			conditions.add(cb.or(cb.isNull(root.get(EmployeeDataView_.wplStrDate)), wplCondition));
			countParameter += 7;
		}

		// classification condition
		Predicate clsCondition = cb.and(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.classStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.classEndDate), baseDate));
		if (paramQuery.getFilterByClassification()) {
			// return empty list if condition code list is empty
			if (classificationCodes.isEmpty()) {
				return Collections.emptyList();
			}

			// update query conditions
			conditions.add(clsCondition);
			countParameter += 2;
		} else {
			conditions.add(cb.or(cb.isNull(root.get(EmployeeDataView_.classStrDate)), clsCondition));
			countParameter += 3;
		}

		// jobtitle condition
		Predicate jobCondition = cb.and(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.jobStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.jobEndDate), baseDate),
				cb.lessThanOrEqualTo(root.get(EmployeeDataView_.jobInfoStrDate), baseDate),
				cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.jobInfoEndDate), baseDate));
		if (paramQuery.getFilterByJobTitle()) {
			// return empty list if condition code list is empty
			if (jobTitleCodes.isEmpty()) {
				return Collections.emptyList();
			}

			// update query conditions
			conditions.add(jobCondition);
			countParameter += 4;
		} else {
			conditions.add(cb.or(cb.isNull(root.get(EmployeeDataView_.jobStrDate)), jobCondition));
			countParameter += 5;
		}
		if (paramQuery.getSystemType() == CCG001SystemType.EMPLOYMENT.value) {
			if (paramQuery.getFilterByWorktype()) {
				// return empty list if condition code list is empty
				if (worktypeCodes.isEmpty()) {
					return Collections.emptyList();
				}

				// update query conditions
				conditions.add(root.get(EmployeeDataView_.workTypeCd).in(worktypeCodes));
				conditions.add(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.workTypeStrDate),
						GeneralDate.localDate(baseDate.toLocalDate())));
				conditions.add(cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.workTypeEndDate),
						GeneralDate.localDate(baseDate.toLocalDate())));
				countParameter += 2 + worktypeCodes.size();
			}
			if (paramQuery.getFilterByClosure()) {
				// return empty list if condition code list is empty
				if (closureIds.isEmpty()) {
					return Collections.emptyList();
				}

				// update query conditions
				conditions.add(root.get(EmployeeDataView_.closureId).in(closureIds));
				countParameter += closureIds.size();

				// check exist before add employment conditions
				if (!paramQuery.getFilterByEmployment()) {
					conditions.add(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.employmentStrDate),
							baseDate));
					conditions.add(cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.employmentEndDate),
							baseDate));
					countParameter += 2;
				}
			}
		}

		// Filter result list by status of employee
		GeneralDateTime retireStart = paramQuery.getRetireStart() == null ? paramQuery.getPeriodStart()
				: paramQuery.getRetireStart();
		GeneralDateTime retireEnd = paramQuery.getRetireEnd() == null ? paramQuery.getPeriodEnd()
				: paramQuery.getRetireEnd();
		GeneralDateTime start = paramQuery.getPeriodStart();
		GeneralDateTime end = paramQuery.getPeriodEnd();

		Predicate isWorking = cb.or(
				cb.and(cb.isNull(root.get(EmployeeDataView_.tempAbsFrameNo)),
						cb.isNull(root.get(EmployeeDataView_.absStrDate))),
				cb.greaterThan(root.get(EmployeeDataView_.absStrDate), end),
				cb.lessThan(root.get(EmployeeDataView_.absEndDate), start));

		// is in company
		Predicate isInCompany = (cb.not(cb.or(cb.greaterThan(root.get(EmployeeDataView_.comStrDate), end),
				cb.lessThan(root.get(EmployeeDataView_.comEndDate), start))));

		Predicate incumbentCondition = cb.disjunction();
		Predicate workerOnLeaveCondition = cb.disjunction();
		Predicate occupancyCondition = cb.disjunction();
		Predicate retireCondition = cb.disjunction();

		// includeIncumbents
		if (paramQuery.getIncludeIncumbents()) {
			incumbentCondition = cb.and(isInCompany, isWorking);
		}

		// workerOnLeave
		if (paramQuery.getIncludeWorkersOnLeave()) {
			workerOnLeaveCondition = cb.and(isInCompany, cb.not(isWorking),
					cb.equal(root.get(EmployeeDataView_.tempAbsFrameNo), LEAVE_ABSENCE_QUOTA_NO));
		}

		// Occupancy
		if (paramQuery.getIncludeOccupancy()) {
			occupancyCondition = cb.and(isInCompany, cb.not(isWorking),
					cb.notEqual(root.get(EmployeeDataView_.tempAbsFrameNo), LEAVE_ABSENCE_QUOTA_NO));
		}

		// retire
		if (paramQuery.getIncludeRetirees()) {
			retireCondition = cb.and(cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.comEndDate), retireStart),
					cb.lessThanOrEqualTo(root.get(EmployeeDataView_.comEndDate), retireEnd),
					cb.notEqual(root.get(EmployeeDataView_.comEndDate), GeneralDateTime.ymdhms(9999, 12, 31, 0, 0, 0)));
		}

		conditions.add(cb.or(incumbentCondition, workerOnLeaveCondition, occupancyCondition, retireCondition));
		countParameter += 10;

		// sort
		if (paramQuery.getSystemType() != CCG001SystemType.ADMINISTRATOR.value) {
			List<Order> orders = this.getOrders(paramQuery.getSystemType(), NAME_TYPE,
					this.getSortConditions(comId, paramQuery.getSystemType(), paramQuery.getSortOrderNo()));
			cq.orderBy(orders);
		}
		List<EmployeeDataView> resultList = new ArrayList<>();
		int countParameterFinal = countParameter; 
		// Fix bug #100057
		if (CollectionUtil.isEmpty(employmentCodes)) {
			employmentCodes.add(EMPTY_LIST);
		}
		if (CollectionUtil.isEmpty(jobTitleCodes)) {
			jobTitleCodes.add(EMPTY_LIST);
		}
		if (CollectionUtil.isEmpty(classificationCodes)) {
			classificationCodes.add(EMPTY_LIST);
		}
		if (CollectionUtil.isEmpty(workplaceCodes)) {
			workplaceCodes.add(EMPTY_LIST);
		}
		// employment condition
		CollectionUtil.split(employmentCodes, ELEMENT_300, splitEmploymentCodes -> {
			// workplace condition
			CollectionUtil.split(jobTitleCodes, ELEMENT_300, splitJobTitleCodes -> {
				// classification condition
				CollectionUtil.split(classificationCodes, ELEMENT_300, splitClassificationCodes -> {
					// jobtitle condition
					CollectionUtil.split(workplaceCodes, ELEMENT_800 - (splitEmploymentCodes.size() + splitJobTitleCodes.size() + splitClassificationCodes.size() - countParameterFinal), splitWorkplaceCodes -> {
						resultList.addAll(executeQuery(paramQuery.getFilterByEmployment(), splitEmploymentCodes, 
								paramQuery.getFilterByWorkplace(), splitWorkplaceCodes, 
								paramQuery.getFilterByClassification(), splitClassificationCodes, 
								paramQuery.getFilterByJobTitle(), splitJobTitleCodes, conditions, cb, cq, comId, paramQuery, em, root));
					});
				});
			});
		});

		// Distinct employee in result list.
		List<EmployeeDataView> resultListDistinct = resultList.stream().filter(this.distinctByKey(EmployeeDataView::getSid))
				.collect(Collectors.toList());

		return resultListDistinct.stream().map(entity -> RegulationInfoEmployee.builder()
				.classificationCode(Optional.ofNullable(entity.getClassificationCode())).employeeCode(entity.getScd())
				.employeeID(entity.getSid()).employmentCode(Optional.ofNullable(entity.getEmpCd()))
				.hireDate(Optional.ofNullable(entity.getComStrDate()))
				.jobTitleCode(Optional.ofNullable(entity.getJobCd()))
				.name(Optional.ofNullable(entity.getBusinessName()))
				.workplaceId(Optional.ofNullable(entity.getWorkplaceId()))
				.workplaceHierarchyCode(Optional.ofNullable(entity.getWplHierarchyCode()))
				.workplaceCode(Optional.ofNullable(entity.getWplCd()))
				.workplaceName(Optional.ofNullable(entity.getWplName()))
				.build())
				.sorted(Comparator.comparing(RegulationInfoEmployee::getEmployeeCode))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the sort conditions.
	 *
	 * @param comId the com id
	 * @param systemType the system type
	 * @param sortOrderNo the sort order no
	 * @return the sort conditions
	 */
	private List<SortingConditionOrder> getSortConditions(String comId, Integer systemType, Integer sortOrderNo) {
		if (sortOrderNo == null) {
			return Collections.emptyList();
		}
		BsymtEmployeeOrderPK pk = new BsymtEmployeeOrderPK(comId, sortOrderNo, systemType);
		Optional<BsymtEmployeeOrder> empOrder = this.queryProxy().find(pk, BsymtEmployeeOrder.class);
		List<BsymtEmpOrderCond> conditions = empOrder.isPresent() ? empOrder.get().getLstBsymtEmpOrderCond()
				: Collections.emptyList();

		return conditions.stream().map(cond -> {
			SortingConditionOrder mapped = new SortingConditionOrder();
			mapped.setOrder(cond.getConditionOrder());
			mapped.setType(RegularSortingType.valueOf(cond.getId().getOrderType()));
			return mapped;
		}).sorted(((a, b) -> a.getOrder() - b.getOrder())).collect(Collectors.toList());
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

	private List<EmployeeDataSort> getCriteriaQueryOfSortingEmployees(String comId, List<String> sIds,
			GeneralDateTime referenceDate) {
		List<EmployeeDataSort> results = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT employee.SID, empHisItem.EMP_CD, wkpconfinf.HIERARCHY_CD, classHisItem.CLASSIFICATION_CODE, jobseq.DISPORDER, jobInfo.JOB_CD, comHis.START_DATE, person.BUSINESS_NAME_KANA, employee.SCD ")
		.append(" FROM BSYMT_EMP_DTA_MNG_INFO employee")
		.append(" LEFT JOIN BPSMT_PERSON person ON employee.PID = person.PID")
		// Employment info
		.append(" LEFT JOIN BSYMT_EMPLOYMENT_HIST empHis ON employee.SID = empHis.SID AND employee.CID = empHis.CID")
		.append(" LEFT JOIN BSYMT_EMPLOYMENT_HIS_ITEM empHisItem ON empHisItem.HIST_ID = empHis.HIST_ID")
		// Workplace info
		.append(" LEFT JOIN BSYMT_AFF_WORKPLACE_HIST wplHis ON employee.SID = wplHis.SID AND employee.CID = wplHis.CID")
		.append(" LEFT JOIN BSYMT_AFF_WPL_HIST_ITEM wplHisItem ON wplHis.HIST_ID = wplHisItem.HIST_ID")
		.append(" LEFT JOIN BSYMT_WORKPLACE_INFO wplInfo ON wplHisItem.WORKPLACE_ID = wplInfo.WKPID")
		.append(" LEFT JOIN BSYMT_WORKPLACE_HIST wplInfoHis ON wplInfo.HIST_ID = wplInfoHis.HIST_ID AND wplInfo.CID = wplInfoHis.CID")
		.append(" LEFT JOIN BSYMT_WKP_CONFIG_INFO wkpconfinf ON wkpconfinf.WKPID = wplHisItem.WORKPLACE_ID")
		.append(" LEFT JOIN BSYMT_WKP_CONFIG wkpconf ON wkpconf.HIST_ID = wkpconfinf.HIST_ID AND wkpconf.CID = wkpconfinf.CID")
		// Classification info
		.append(" LEFT JOIN BSYMT_AFF_CLASS_HISTORY classHis ON employee.SID = classHis.SID AND employee.CID = classHis.CID")
		.append(" LEFT JOIN BSYMT_AFF_CLASS_HIS_ITEM classHisItem ON classHis.HIST_ID = classHisItem.HIST_ID")
		// JobTitle Info
		.append(" LEFT JOIN BSYMT_AFF_JOB_HIST jobHis ON employee.SID = jobHis.SID AND employee.CID = jobHis.CID")
		.append(" LEFT JOIN BSYMT_AFF_JOB_HIST_ITEM jobHisItem ON jobHis.HIST_ID = jobHisItem.HIST_ID")
		.append(" LEFT JOIN BSYMT_JOB_INFO jobInfo ON jobHisItem.JOB_TITLE_ID = jobInfo.JOB_ID")
		.append(" LEFT JOIN BSYMT_JOB_HIST jobInfoHis ON jobInfo.HIST_ID = jobInfoHis.HIST_ID AND jobInfo.CID = jobInfoHis.CID")
		.append(" LEFT JOIN BSYMT_JOB_SEQ_MASTER jobseq ON jobseq.SEQ_CD = jobInfo.SEQUENCE_CD AND jobseq.CID = jobInfo.CID")
		// Com hist
		.append(" LEFT JOIN BSYMT_AFF_COM_HIST comHis ON employee.SID = comHis.SID AND comHis.CID = employee.CID")
		.append(" WHERE 1=1")
		// company id
		.append(" AND employee.CID = '" + comId + "'")
		// employee id
		.append(" AND employee.SID in ('" + String.join("','", sIds) + "')")
		// employment
		.append(" AND rfDate BETWEEN empHis.START_DATE AND empHis.END_DATE")
		// workplace
		.append(" AND rfDate BETWEEN wplHis.START_DATE AND wplHis.END_DATE")
		.append(" AND rfDate BETWEEN wkpconf.START_DATE AND wkpconf.END_DATE")
		// classification
		.append(" AND rfDate BETWEEN classHis.START_DATE AND classHis.END_DATE")
		// job title
		.append(" AND rfDate BETWEEN jobHis.START_DATE AND jobHis.END_DATE")
		.append(" AND rfDate BETWEEN jobInfoHis.START_DATE AND jobInfoHis.END_DATE");
		// NtsStatement statement = new NtsStatement(builder.toString(), this.jdbcProxy());
		String sql = builder.toString();
		if(sql.contains("rfDate")) {
			sql = sql.replaceAll("rfDate", "'" + referenceDate.toString() + "'");
		}
		Query query = this.getEntityManager()
				.createNativeQuery(sql);

		@SuppressWarnings("unchecked")
		List<Object[]> queryRs = query.getResultList();
		
		for(Object[] res : queryRs) {
			results.add(new EmployeeDataSort(
					String.valueOf(res[0]),
					String.valueOf(res[1]),
					String.valueOf(res[2]),
					String.valueOf(res[3]),
					String.valueOf(res[4]),
					String.valueOf(res[5]),
					GeneralDateTime.legacyDateTime((Date) res[6]),
					String.valueOf(res[7]),
					String.valueOf(res[8])
					));
		}

		return results;
	}
	
	@AllArgsConstructor
	@Data
	public class EmployeeDataSort {
		private String sid;
		private String employmentCd;
		private String wplHierarchyCd;
		private String classificationCd;
		private String jobSeqDisp;
		private String jobCd;
		// hire date
		private GeneralDateTime comStrDate;
		private String businessNameKana;
		private String scd;

	}

	/**
	 * Gets the orders.
	 *
	 * @param systemType the system type
	 * @param nameType the name type
	 * @param sortConditions the sort conditions
	 * @return the orders
	 */
	private List<Order> getOrders(Integer systemType, Integer nameType, List<SortingConditionOrder> sortConditions) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<EmployeeDataView> cq = cb.createQuery(EmployeeDataView.class);
		Root<EmployeeDataView> root = cq.from(EmployeeDataView.class);

		sortConditions.sort((a,b) -> a.getOrder() - b.getOrder());
		Iterator<SortingConditionOrder> iterator = sortConditions.iterator();

		List<Order> orders = new ArrayList<>();

		while (iterator.hasNext()) {
			SortingConditionOrder cond = iterator.next();
			switch (cond.getType()) {
			case EMPLOYMENT: // EMPLOYMENT
				orders.add(cb.asc(root.get(EmployeeDataView_.empCd)));
				break;
			case DEPARTMENT: // DEPARTMENT
				// TODO: not covered
				break;
			case WORKPLACE: // WORKPLACE
				orders.add(cb.asc(root.get(EmployeeDataView_.wplHierarchyCode)));
				break;
			case CLASSIFICATION: // CLASSIFICATION
				orders.add(cb.asc(root.get(EmployeeDataView_.classificationCode)));
				break;
			case POSITION: // POSITION
				orders.add(cb.asc(root.get(EmployeeDataView_.jobSeqDisp)));
				orders.add(cb.asc(root.get(EmployeeDataView_.jobCd)));
				break;
			case HIRE_DATE: // HIRE_DATE
				orders.add(cb.asc(root.get(EmployeeDataView_.comStrDate)));
				break;
			case NAME: // NAME
				// 現在は、氏名の種類を選択する機能がないので、「ビジネスネーム日本語」固定で
				// => 「氏名カナ」 ＝ 「ビジネスネームカナ」
				orders.add(cb.asc(root.get(EmployeeDataView_.businessNameKana)));
				// TODO: orders.add(cb.asc(root.get(EmployeeDataView_.personNameKana)));
				break;
			}
		}

		// sort by worktype code
		if (systemType == CCG001SystemType.EMPLOYMENT.value) {
			// Katou said: その資料の記載は現在未対応の「並び替え機能」で使用するものです。
			// 現状は全システム区分で「社員コード（ASC）」でソートしてもらえませんでしょうか？
			// => hien tai chua doi ung cai nay
			// TODO: not covered
			// orders.add(cb.asc(root.get(EmployeeDataView_.workTypeCd)));
		}

		// always sort by employee code
		orders.add(cb.asc(root.get(EmployeeDataView_.scd)));
		return orders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.query.model.employee.RegulationInfoEmployeeRepository#
	 * findInfoBySIds(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegulationInfoEmployee> findInfoBySIds(List<String> sIds, GeneralDate referenceDate) {
		List<Object[]> persons = new ArrayList<>();

		CollectionUtil.split(sIds, MAX_WHERE_IN, (subList) -> {
			persons.addAll(this.getEntityManager().createQuery(FIND_EMPLOYEE).setParameter("listSid", subList)
					.getResultList());
		});

		// get list employee
		Map<String, RegulationInfoEmployee> employeeInfoList = persons.stream().map(item -> {
			BsymtEmployeeDataMngInfo e = (BsymtEmployeeDataMngInfo) item[0];
			BpsmtPerson p = (BpsmtPerson) item[1];

			return RegulationInfoEmployee.builder()
					.employeeID(e.bsymtEmployeeDataMngInfoPk.sId)
					.employeeCode(e.employeeCode)
					.name(Optional.ofNullable(p.businessName))
					.classificationCode(Optional.empty())
					.departmentCode(Optional.empty())
					.employmentCode(Optional.empty())
					.hireDate(Optional.empty())
					.jobTitleCode(Optional.empty())
					.workplaceHierarchyCode(Optional.empty())
					.workplaceCode(Optional.empty())
					.workplaceId(Optional.empty())
					.workplaceName(Optional.empty())
					.build();
		}).collect(Collectors.toMap(RegulationInfoEmployee::getEmployeeID, v -> v, (oldValue, newValue) -> newValue));

		// Get workplace
		List<Object[]> workplaces = new ArrayList<>();
		CollectionUtil.split(sIds, MAX_WHERE_IN, (subList) -> {
			workplaces.addAll(this.getEntityManager().createQuery(FIND_WORKPLACE).setParameter("listSid", subList)
					.setParameter("refDate", referenceDate).getResultList());
		});

		// set workplace
		employeeInfoList.keySet().forEach(empId -> {
			Optional<Object[]> workplace = workplaces.stream().filter(wpl -> {
				String id = (String) wpl[0];
				return id.equals(empId);
			}).findAny();

			if (workplace.isPresent()) {
				String workplaceCode = (String) workplace.get()[1];
				String workplaceId = (String) workplace.get()[2];
				String workplaceName = (String) workplace.get()[3];

				RegulationInfoEmployee employee = employeeInfoList.get(empId); 
				employee.setWorkplaceCode(Optional.ofNullable(workplaceCode));
				employee.setWorkplaceId(Optional.ofNullable(workplaceId));
				employee.setWorkplaceName(Optional.ofNullable(workplaceName));
			}
		});

		return employeeInfoList.values().stream().collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.query.model.employee.RegulationInfoEmployeeRepository
	 * #findBySid(java.lang.String, java.lang.String, nts.arc.time.GeneralDateTime)
	 */
	@Override
	public RegulationInfoEmployee findBySid(String comId, String sid, GeneralDateTime baseDate) {
		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<EmployeeDataView> cq = cb.createQuery(EmployeeDataView.class);
		Root<EmployeeDataView> root = cq.from(EmployeeDataView.class);
		
		// Constructing condition.
		List<Predicate> conditions = new ArrayList<Predicate>();

		// Add company condition
		conditions.add(cb.equal(root.get(EmployeeDataView_.cid), comId));

		// Add NOT_DELETED condition
		conditions.add(cb.equal(root.get(EmployeeDataView_.delStatusAtr), NOT_DELETED));
		
		// Where SID.
		conditions.add(cb.equal(root.get(EmployeeDataView_.sid), sid));
		
		// Where base date.
		conditions.add(cb.lessThanOrEqualTo(root.get(EmployeeDataView_.wplStrDate), baseDate));
		conditions.add(cb.greaterThanOrEqualTo(root.get(EmployeeDataView_.wplEndDate), baseDate));
		
		// Find fist.
		cq.where(conditions.toArray(new Predicate[] {}));
		List<EmployeeDataView> res = this.getEntityManager().createQuery(cq).getResultList();

		if (CollectionUtil.isEmpty(res)) {
			return null;
		}

		EmployeeDataView entity = res.get(0);
		
		// Convert.
		return RegulationInfoEmployee.builder()
				.classificationCode(Optional.ofNullable(entity.getClassificationCode())).employeeCode(entity.getScd())
				.employeeID(entity.getSid()).employmentCode(Optional.ofNullable(entity.getEmpCd()))
				.hireDate(Optional.ofNullable(entity.getComStrDate()))
				.jobTitleCode(Optional.ofNullable(entity.getJobCd()))
				.name(Optional.ofNullable(entity.getBusinessName()))
				.workplaceId(Optional.ofNullable(entity.getWorkplaceId()))
				.workplaceHierarchyCode(Optional.ofNullable(entity.getWplHierarchyCode()))
				.workplaceCode(Optional.ofNullable(entity.getWplCd()))
				.workplaceName(Optional.ofNullable(entity.getWplName()))
				.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.query.model.employee.RegulationInfoEmployeeRepository#
	 * sortEmployees(java.lang.String, java.util.List, java.lang.Integer,
	 * java.lang.Integer, java.lang.Integer, nts.arc.time.GeneralDateTime)
	 */
	@Override
	public List<String> sortEmployees(String comId, List<String> sIds, Integer systemType, Integer orderNo,
			Integer nameType, GeneralDateTime referenceDate) {
		if (sIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<EmployeeDataSort> resultList = new ArrayList<>();
		CollectionUtil.split(sIds, MAX_PARAMETERS, splitData -> {
			resultList.addAll(this.getCriteriaQueryOfSortingEmployees(comId, splitData, referenceDate));
		});

		List<SortingConditionOrder> sortConditions = this.getSortConditions(comId, systemType, orderNo);
		return this.sortByListConditions(resultList, sortConditions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.query.model.employee.RegulationInfoEmployeeRepository#
	 * sortEmployees(java.lang.String, java.util.List, java.util.List,
	 * nts.arc.time.GeneralDateTime)
	 */
	@Override
	public List<String> sortEmployees(String comId, List<String> sIds, List<SortingConditionOrder> orders,
			GeneralDateTime referenceDate) {
		if (sIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<EmployeeDataSort> resultList = new ArrayList<>();
		CollectionUtil.split(sIds, MAX_PARAMETERS, splitData -> {
			resultList.addAll(this.getCriteriaQueryOfSortingEmployees(comId, splitData, referenceDate));
		});

		return this.sortByListConditions(resultList, orders);
	}
	
	/**
	 * Execute query.
	 *
	 * @param getFilterByEmployment the get filter by employment
	 * @param splitEmploymentCodes the split employment codes
	 * @param getFilterByWorkplace the get filter by workplace
	 * @param splitWorkplaceCodes the split workplace codes
	 * @param getFilterByClassification the get filter by classification
	 * @param splitClassificationCodes the split classification codes
	 * @param getFilterByJobTitle the get filter by job title
	 * @param splitJobTitleCodes the split job title codes
	 * @param conditions the conditions
	 * @param cb the cb
	 * @param cq the cq
	 * @param comId the com id
	 * @param paramQuery the param query
	 * @param em the em
	 * @param root the root
	 * @return the list
	 */
	private List<EmployeeDataView> executeQuery(boolean getFilterByEmployment, List<String> splitEmploymentCodes, 
								boolean getFilterByWorkplace, List<String> splitWorkplaceCodes, 
								boolean getFilterByClassification, List<String> splitClassificationCodes, 
								boolean getFilterByJobTitle, List<String> splitJobTitleCodes, List<Predicate> conditions, CriteriaBuilder cb,
								CriteriaQuery<EmployeeDataView> cq, String comId, EmployeeSearchQuery paramQuery,
								EntityManager em, Root<EmployeeDataView> root
								) {
		int countFilterTrue = 0;
		
		// employment condition
		if (getFilterByEmployment) {
			if (splitEmploymentCodes.size() == 1 && splitEmploymentCodes.get(0).compareTo(EMPTY_LIST) == 0) {
				splitEmploymentCodes.clear();
			}
			conditions.add(root.get(EmployeeDataView_.empCd).in(splitEmploymentCodes));
			countFilterTrue++;
		}
		
		// workplace condition
		if (getFilterByWorkplace) {
			if (splitWorkplaceCodes.size() == 1 && splitWorkplaceCodes.get(0).compareTo(EMPTY_LIST) == 0) {
				splitWorkplaceCodes.clear();
			}
			conditions.add(root.get(EmployeeDataView_.workplaceId).in(splitWorkplaceCodes));
			countFilterTrue++;
		}
		
		// classification condition
		if (getFilterByClassification) {
			if (splitClassificationCodes.size() == 1 && splitClassificationCodes.get(0).compareTo(EMPTY_LIST) == 0) {
				splitClassificationCodes.clear();
			}
			conditions.add(root.get(EmployeeDataView_.classificationCode).in(splitClassificationCodes));
			countFilterTrue++;
		}
		
		// jobtitle condition
		if (getFilterByJobTitle) { 
			if (splitJobTitleCodes.size() == 1 && splitJobTitleCodes.get(0).compareTo(EMPTY_LIST) == 0) {
				splitJobTitleCodes.clear();
			}
			conditions.add(root.get(EmployeeDataView_.jobTitleId).in(splitJobTitleCodes));
			countFilterTrue++;
		}
		
		List<EmployeeDataView> resultListInFunc = new ArrayList<>();
		cq.where(conditions.toArray(new Predicate[] {}));

		// execute query & add to resultListInFunc
		resultListInFunc.addAll(em.createQuery(cq).getResultList());
		while (countFilterTrue > 0) {
			conditions.remove(conditions.size()-1);
			countFilterTrue--;
		}
		return resultListInFunc;
	}

	
	/**
	 * Sort by list conditions.
	 *
	 * @param resultList
	 *            the result list
	 * @param sortConditions
	 *            the sort conditions
	 * @return the list
	 */
	private List<String> sortByListConditions(List<EmployeeDataSort> resultList,
			List<SortingConditionOrder> sortConditions) {
		return resultList.stream().sorted((a, b) -> {
			Iterator<SortingConditionOrder> iterator = sortConditions.iterator();
			int comparator = 0;

			while (iterator.hasNext()) {
				if (comparator == 0) {
					SortingConditionOrder cond = iterator.next();
					switch (cond.getType()) {
					case EMPLOYMENT: // EMPLOYMENT
						String empCda = a.getEmploymentCd();
						String empCdb = b.getEmploymentCd();
						if (empCda != null && empCdb != null) {
							comparator = empCda.compareTo(empCdb);
						}
						break;
					case DEPARTMENT: // DEPARTMENT
						// TODO: not covered
						break;
					case WORKPLACE: // WORKPLACE
						String wplCda = a.getWplHierarchyCd();
						String wplCdb = b.getWplHierarchyCd();
						if (wplCda != null && wplCdb != null) {
							comparator = wplCda.compareTo(wplCdb);
						}
						break;
					case CLASSIFICATION: // CLASSIFICATION
						String clsCda = a.getClassificationCd();
						String clsCdb = b.getClassificationCd();
						if (clsCda != null && clsCdb != null) {
							comparator = clsCda.compareTo(clsCdb);
						}
						break;
					case POSITION: // POSITION
						String seqCda = a.getJobSeqDisp();
						String seqCdb = b.getJobSeqDisp();
						if (seqCda != null && seqCdb != null) {
							comparator = seqCda.compareTo(seqCdb);
						}
						if (comparator == 0) {
							String jobCda = a.getJobCd();
							String jobCdb = b.getJobCd();
							if (jobCda != null && jobCdb != null) {
								comparator = jobCda.compareTo(jobCdb);
							}
						}
						break;
					case HIRE_DATE: // HIRE_DATE
						GeneralDateTime comStrDa = a.getComStrDate();
						GeneralDateTime comStrDb = b.getComStrDate();
						if (comStrDa != null && comStrDb != null) {
							comparator = comStrDa.compareTo(comStrDb);
						}
						break;
					case NAME: // NAME
						// 現在は、氏名の種類を選択する機能がないので、「ビジネスネーム日本語」固定で
						// => 「氏名カナ」 ＝ 「ビジネスネームカナ」
						String businessNameA = a.getBusinessNameKana();
						String businessNameB = b.getBusinessNameKana();
						if (businessNameA != null && businessNameB != null) {
							comparator = businessNameA.compareTo(businessNameB);
						}
						// TODO:
						// orders.add(cb.asc(root.get(EmployeeDataView_.personNameKana)));
						break;
					}
				} else {
					break;
				}
			}
			if (comparator == 0) {
				comparator = a.getScd().compareTo(b.getScd());
			}
			return comparator;
		}).map(EmployeeDataSort::getSid).distinct().collect(Collectors.toList());
	}

}
