/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.screen.com.infra.query.employee;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.infra.entity.department.BsymtDepartmentInfo;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;

/**
 * The Class JpaEmployeeSearchQueryRepository.
 */
@Stateless
public class JpaEmployeeSearchQueryRepository extends JpaRepository implements EmployeeSearchQueryRepository{
	
	/** The Constant EmployeeDeletionAttr_NOTDELETED. */
	public static final int EmployeeDeletionAttr_NOTDELETED = 0;
	/** The Constant SEARCH_QUERY_STRING. */
	private static final String SEARCH_QUERY_STRING = "SELECT e, p, wp, d From BsymtEmployeeDataMngInfo e "
			+ " LEFT JOIN BpsmtPerson p ON e.bsymtEmployeeDataMngInfoPk.pId = p.bpsmtPersonPk.pId "
			+ " LEFT JOIN BsymtAffiWorkplaceHist h ON h.sid = e.bsymtEmployeeDataMngInfoPk.sId "
			+ "	AND h.strDate <= :baseDate "
			+ " AND h.endDate >= :baseDate "
			+ " LEFT JOIN BsymtAffiWorkplaceHistItem hi ON hi.hisId = h.hisId"
			+ " LEFT JOIN BsymtWorkplaceHist wphist ON wphist.bsymtWorkplaceHistPK.wkpid = hi.workPlaceId "
			+ " AND wphist.strD <= :baseDate "
			+ " AND wphist.endD >= :baseDate "				
			+ " LEFT JOIN BsymtWorkplaceInfo wp ON wphist.bsymtWorkplaceHistPK.hisId = wp.bsymtWorkplaceInfoPK.hisId"
			+ " AND wp.bsymtWorkplaceInfoPK.cid = e.companyId "
			+ " LEFT JOIN BsymtAffiDepartment ad ON ad.sid = e.bsymtEmployeeDataMngInfoPk.sId "
			+ " AND ad.strD <= :baseDate"
			+ " AND ad.endD >= :baseDate "
			+ " LEFT JOIN BsymtDepartmentHist dh ON dh.bsymtDepartmentHistPK.depId = ad.depId "
			+ " AND dh.strD <= :baseDate "
			+ " AND dh.endD >= :baseDate "
			+ " AND dh.bsymtDepartmentHistPK.cid = e.companyId "
			+ " LEFT JOIN BsymtDepartmentInfo d ON d.bsymtDepartmentInfoPK.depId = dh.bsymtDepartmentHistPK.depId "
			+ " AND d.bsymtDepartmentInfoPK.cid = e.companyId "
			+ " AND d.bsymtDepartmentInfoPK.histId = dh.bsymtDepartmentHistPK.histId "
			+ " INNER JOIN BsymtAffCompanyHist bach ON e.bsymtEmployeeDataMngInfoPk.sId = bach.bsymtAffCompanyHistPk.sId"
			+ " AND bach.startDate <= :baseDate "
			+ " AND bach.endDate >= :baseDate "
			+ " WHERE e.employeeCode = :empCode "
			+ " AND e.companyId = :companyId "
			+ " AND e.delStatus = :delStatus ";

	/* (non-Javadoc)
	 * @see nts.uk.screen.com.infra.query.employee.EmployeeSearchQueryRepository
	 * #findInAllEmployee(java.lang.String, nts.uk.screen.com.infra.query.employee.System, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<Kcp009EmployeeSearchData> findInAllEmployee(String code, System system, GeneralDate baseDate, String companyId) {
		Object[] resultQuery = null;
		try {
			resultQuery = (Object[]) this.getEntityManager().createQuery(SEARCH_QUERY_STRING)
				.setParameter("baseDate", baseDate)
				.setParameter("empCode", code)
				.setParameter("companyId", companyId)
				.setParameter("delStatus", EmployeeDeletionAttr_NOTDELETED)
				.getSingleResult();
		} catch (NoResultException e) {
			throw new RuntimeException(e);
		}
		
		// Convert query data.
		BsymtEmployeeDataMngInfo employee = (BsymtEmployeeDataMngInfo) resultQuery[0];
		BpsmtPerson person = (BpsmtPerson) resultQuery[1];
		BsymtWorkplaceInfo workplace = resultQuery[2] == null ? null : (BsymtWorkplaceInfo) resultQuery[2];
		BsymtDepartmentInfo department = resultQuery[3] == null ? null : (BsymtDepartmentInfo) resultQuery[3];
		
		switch (system) {
		case Employment:
			// Get work place.
			return Optional.of(Kcp009EmployeeSearchData.builder()
					.employeeId(employee.bsymtEmployeeDataMngInfoPk.sId)
					.employeeCode(employee.employeeCode)
					.businessName(person.businessName)
					.orgName(workplace != null ? workplace.getWkpName() : null)
					.build());
		default:
			// Get department
			return Optional.of(Kcp009EmployeeSearchData.builder()
					.employeeId(employee.bsymtEmployeeDataMngInfoPk.sId)
					.employeeCode(employee.employeeCode)
					.businessName(person.businessName)
					.orgName(department != null ? department.getName() : null)
					.build());
		}
	}

}
