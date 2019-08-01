package nts.uk.screen.at.infra.dailyperformance.correction.searchemp;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.screen.at.app.dailyperformance.correction.searchemployee.DPEmployeeSearchData;
import nts.uk.screen.at.app.dailyperformance.correction.searchemployee.FindEmployeeBase;

@Stateless
public class JpaFindEmployeeBaseRepo extends JpaRepository implements FindEmployeeBase{

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
			+ " WHERE e.bsymtEmployeeDataMngInfoPk.sId = :employeeId "
			+ " AND e.companyId = :companyId ";
	
	@Override
	public Optional<DPEmployeeSearchData> findInAllEmployee(String employeeId, GeneralDate baseDate, String companyId) {
		Object[] resultQuery = null;
		try {
			resultQuery = (Object[]) this.getEntityManager().createQuery(SEARCH_QUERY_STRING)
				.setParameter("baseDate", baseDate)
				.setParameter("employeeId", employeeId)
				.setParameter("companyId", companyId)
				.getSingleResult();
		} catch (NoResultException e) {
			throw new RuntimeException(e);
		}
		
		// Convert query data.
		BsymtEmployeeDataMngInfo employee = (BsymtEmployeeDataMngInfo) resultQuery[0];
		BpsmtPerson person = (BpsmtPerson) resultQuery[1];
		BsymtWorkplaceInfo workplace = resultQuery[2] == null ? null : (BsymtWorkplaceInfo) resultQuery[2];
		//BsymtDepartmentInfo department = resultQuery[3] == null ? null : (BsymtDepartmentInfo) resultQuery[3];
		
			return Optional.of(DPEmployeeSearchData.builder()
					.employeeId(employee.bsymtEmployeeDataMngInfoPk.sId)
					.employeeCode(employee.employeeCode)
					.businessName(person.businessName)
					.orgName(workplace != null ? workplace.getWkpName() : null).build());
	}

}
