/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.infra.repository.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.query.model.classification.ClassificationModel;
import nts.uk.query.model.employee.EmployeeInformation;
import nts.uk.query.model.employee.EmployeeInformationQuery;
import nts.uk.query.model.employee.EmployeeInformationRepository;
import nts.uk.query.model.employement.EmploymentModel;
import nts.uk.query.model.position.PositionModel;
import nts.uk.query.model.workplace.WorkplaceModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaEmployeeInformationRepository.
 */
@Stateless
public class JpaEmployeeInformationRepository extends JpaRepository implements EmployeeInformationRepository {

	private static final String EMPLOYEE_QUERY = "SELECT e, p"
			+ " FROM BsymtEmployeeDataMngInfo e"
			+ " LEFT JOIN BpsmtPerson p ON p.bpsmtPersonPk.pId = e.bsymtEmployeeDataMngInfoPk.pId"
			+ " WHERE e.bsymtEmployeeDataMngInfoPk.sId IN :listSid";

	private static final String WORKPLACE_QUERY = "SELECT awh.sid, wi.wkpcd, wi.wkpGenericName, wi.wkpName, wi.bsymtWorkplaceInfoPK.wkpid"
			+ " FROM BsymtAffiWorkplaceHist awh"
			+ " LEFT JOIN BsymtAffiWorkplaceHistItem awhi ON awhi.hisId = awh.hisId AND awh.cid =:cid"
			+ " LEFT JOIN BsymtWorkplaceHist wh ON awhi.workPlaceId = wh.bsymtWorkplaceHistPK.wkpid AND wh.bsymtWorkplaceHistPK.cid =:cid"
			+ " LEFT JOIN BsymtWorkplaceInfo wi ON wi.bsymtWorkplaceInfoPK.historyId = wh.bsymtWorkplaceHistPK.historyId AND wi.bsymtWorkplaceInfoPK.cid =:cid"
			+ " WHERE awh.sid IN :listSid"
			+ " AND awh.strDate <= :refDate"
			+ " AND awh.endDate >= :refDate"
			+ " AND wh.strD <= :refDate"
			+ " AND wh.endD >= :refDate";

	private static final String POSITION_QUERY = "SELECT ajh.sid, ji.jobCd, ji.jobName, ji.bsymtJobInfoPK.jobId"
			+ " FROM BsymtAffJobTitleHist ajh"
			+ " LEFT JOIN BsymtAffJobTitleHistItem ajhi ON ajhi.hisId = ajh.hisId AND ajh.cid =:cid"
			+ " LEFT JOIN BsymtJobHist jh ON jh.bsymtJobHistPK.jobId = ajhi.jobTitleId AND jh.bsymtJobHistPK.cid =:cid"
			+ " LEFT JOIN BsymtJobInfo ji ON ji.bsymtJobInfoPK.histId = jh.bsymtJobHistPK.histId AND ji.bsymtJobInfoPK.cid =:cid"
			+ " WHERE ajh.sid IN :listSid"
			+ " AND ajh.strDate <= :refDate"
			+ " AND ajh.endDate >= :refDate"
			+ " AND jh.startDate <= :refDate"
			+ " AND jh.endDate >= :refDate";

	private static final String EMPLOYMENT_QUERY = "SELECT eh.sid, e.bsymtEmploymentPK.code, e.name "
			+ "FROM BsymtEmploymentHist eh "
			+ "LEFT JOIN BsymtEmploymentHistItem ehi ON ehi.hisId = eh.hisId AND eh.companyId =:cid "
			+ "LEFT JOIN BsymtEmployment e ON e.bsymtEmploymentPK.code = ehi.empCode AND  e.bsymtEmploymentPK.cid =:cid "
			+ "WHERE eh.sid IN :listSid "
			+ "AND eh.strDate <= :refDate "
			+ "AND eh.endDate >= :refDate";

	private static final String CLASSIFICATION_QUERY = "SELECT ach.sid, c.bsymtClassificationPK.clscd, c.clsname "
			+ "FROM BsymtAffClassHistory ach "
			+ "LEFT JOIN BsymtAffClassHistItem achi ON achi.historyId = ach.historyId AND ach.cid =:cid "
			+ "LEFT JOIN BsymtClassification c ON c.bsymtClassificationPK.clscd = achi.classificationCode AND c.bsymtClassificationPK.cid =:cid "
			+ "WHERE ach.sid IN :listSid "
			+ "AND ach.startDate <= :refDate "
			+ "AND ach.endDate >= :refDate";

	private static final String EMPCLS_QUERY = "SELECT wc.kshmtWorkingCondPK.sid, wci.laborSys "
			+ "FROM KshmtWorkingCond wc "
			+ "LEFT JOIN KshmtWorkingCondItem wci ON wci.historyId = wc.kshmtWorkingCondPK.historyId "
			+ "WHERE wc.kshmtWorkingCondPK.sid IN :listSid "
			+ "AND wc.strD <= :refDate "
			+ "AND wc.endD >= :refDate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.model.employee.EmployeeInformationRepository#find(nts.uk.
	 * query.model.employee.EmployeeInformationQuery)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeeInformation> find(EmployeeInformationQuery param) {
		List<Object[]> persons = new ArrayList<>();

		CollectionUtil.split(param.getEmployeeIds(), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			persons.addAll(this.getEntityManager().createQuery(EMPLOYEE_QUERY).setParameter("listSid", subList)
					.getResultList());
		});

		// get list employee
		Map<String, EmployeeInformation> employeeInfoList = persons.stream().map(item -> {
			BsymtEmployeeDataMngInfo e = (BsymtEmployeeDataMngInfo) item[0];
			BpsmtPerson p = (BpsmtPerson) item[1];
			return EmployeeInformation.builder()
					.employeeId(e.bsymtEmployeeDataMngInfoPk.sId)
					.employeeCode(e.employeeCode)
					.businessName(p.businessName)
					.workplace(Optional.empty())
					.classification(Optional.empty())
					.department(Optional.empty())
					.employment(Optional.empty())
					.employmentCls(Optional.empty())
					.position(Optional.empty())
					.build();
		}).collect(Collectors.toMap(EmployeeInformation::getEmployeeId, v -> v));

		// set workplace name
		if (param.isToGetWorkplace()) {
			List<Object[]> workplaces = this.getOptionalResult(param, WORKPLACE_QUERY, false);

			employeeInfoList.keySet().forEach(empId -> {
				Optional<Object[]> workplace = workplaces.stream().filter(wpl -> {
					String id = (String) wpl[0];
					return id.equals(empId);
				}).findAny();

				if (workplace.isPresent()) {
					String workplaceCode = (String) workplace.get()[1];
					String workplaceGenericName = (String) workplace.get()[2];
					String workplaceName = (String) workplace.get()[3];
					String workplaceId = (String) workplace.get()[4];
					employeeInfoList.get(empId).setWorkplace(Optional.of(WorkplaceModel.builder()
							.workplaceId(workplaceId)
							.workplaceCode(workplaceCode)
							.workplaceGenericName(workplaceGenericName)
							.workplaceName(workplaceName)
							.build()));
				}
			});
		}

		// set position
		if (param.isToGetPosition()) {
			List<Object[]> positions = this.getOptionalResult(param, POSITION_QUERY, false);
			
			employeeInfoList.keySet().forEach(empId -> {
				Optional<Object[]> job = positions.stream().filter(pos -> {
					String id = (String) pos[0];
					return id.equals(empId);
				}).findAny();
				
				if (job.isPresent()) {
					String jobCode = (String) job.get()[1];
					String jobName = (String) job.get()[2];
					String jobId = (String) job.get()[3];
					employeeInfoList.get(empId).setPosition(Optional.of(PositionModel.builder()
							.positionCode(jobCode)
							.positionName(jobName)
							.positionId(jobId)
							.build()));
				}
			});
		}

		// set employment
		if (param.isToGetEmployment()) {
			List<Object[]> employments = this.getOptionalResult(param, EMPLOYMENT_QUERY, false);
			
			employeeInfoList.keySet().forEach(empId -> {
				Optional<Object[]> em = employments.stream().filter(e -> {
					String id = (String) e[0];
					return id.equals(empId);
				}).findAny();
				
				if (em.isPresent()) {
					String empCode = (String) em.get()[1];
					String empName = (String) em.get()[2];
					employeeInfoList.get(empId).setEmployment(Optional.of(EmploymentModel.builder()
							.employmentCode(empCode)
							.employmentName(empName)
							.build()));
				}
			});
		}

		// set classification
		if (param.isToGetClassification()) {
			List<Object[]> classifications = this.getOptionalResult(param, CLASSIFICATION_QUERY, false);
			
			employeeInfoList.keySet().forEach(empId -> {
				Optional<Object[]> cls = classifications.stream().filter(c -> {
					String id = (String) c[0];
					return id.equals(empId);
				}).findAny();
				
				if (cls.isPresent()) {
					String clsCode = (String) cls.get()[1];
					String clsName = (String) cls.get()[2];
					employeeInfoList.get(empId).setClassification(Optional.of(ClassificationModel.builder()
							.classificationCode(clsCode)
							.classificationName(clsName)
							.build()));
				}
			});
		}

		// set Employment classification của working condition
		if (param.isToGetEmploymentCls()) {
			List<Object[]> listEmpCls = this.getOptionalResult(param, EMPCLS_QUERY, true);
			
			employeeInfoList.keySet().forEach(empId -> {
				Optional<Object[]> cls = listEmpCls.stream().filter(c -> {
					String id = (String) c[0];
					return id.equals(empId);
				}).findAny();
				
				if (cls.isPresent()) {
					Integer laborSystem = (Integer) cls.get()[1];
					employeeInfoList.get(empId).setEmploymentCls(Optional.of(laborSystem));
				}
			});
		}
		
		return employeeInfoList.values().stream().collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getOptionalResult(EmployeeInformationQuery param, String query, boolean isWorkingCondition) {
		String cid = AppContexts.user().companyId();
		List<Object[]> results = new ArrayList<>();
		
		CollectionUtil.split(param.getEmployeeIds(), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			if (isWorkingCondition) {
				results.addAll(this.getEntityManager().createQuery(query).setParameter("listSid", subList)
						.setParameter("refDate", param.getReferenceDate()).getResultList());
			} else {
				results.addAll(this.getEntityManager().createQuery(query).setParameter("listSid", subList)
						.setParameter("refDate", param.getReferenceDate())
						.setParameter("cid", cid)
						.getResultList());
			}
		});
		return results;
	}

}
