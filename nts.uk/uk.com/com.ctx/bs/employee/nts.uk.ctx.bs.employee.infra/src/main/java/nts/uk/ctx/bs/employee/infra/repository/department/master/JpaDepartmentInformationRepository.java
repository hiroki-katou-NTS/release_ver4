package nts.uk.ctx.bs.employee.infra.repository.department.master;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformationRepository;
import nts.uk.ctx.bs.employee.infra.entity.department.master.BsymtDepartmentInfor;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaDepartmentInformationRepository extends JpaRepository implements DepartmentInformationRepository {

	private static final String FIND_ALL_DEP_BY_COMPANY_AND_HIST = "SELECT i FROM BsymtDepartmentInfor i "
			+ "WHERE i.pk.companyId = :companyId " + "AND i.pk.departmentHistoryId = :depHistId";

	private static final String FIND_ALL_ACTIVE_DEP_BY_COMPANY_AND_HIST = "SELECT i FROM BsymtDepartmentInfor i "
			+ "WHERE i.pk.companyId = :companyId " + "AND i.pk.departmentHistoryId = :depHistId AND i.deleteFlag = 0";

	@Override
	public List<DepartmentInformation> getAllDepartmentByCompany(String companyId, String depHistId) {
		return this.queryProxy().query(FIND_ALL_DEP_BY_COMPANY_AND_HIST, BsymtDepartmentInfor.class)
				.setParameter("companyId", companyId).setParameter("depHistId", depHistId).getList(i -> i.toDomain());
	}

	@Override
	public List<DepartmentInformation> getAllActiveDepartmentByCompany(String companyId, String depHistId) {
		return this.queryProxy().query(FIND_ALL_ACTIVE_DEP_BY_COMPANY_AND_HIST, BsymtDepartmentInfor.class)
				.setParameter("companyId", companyId).setParameter("depHistId", depHistId).getList(i -> i.toDomain());
	}

	@Override
	public void addDepartment(DepartmentInformation department) {
		this.commandProxy().insert(BsymtDepartmentInfor.fromDomain(department));
	}

	@Override
	public void addDepartments(List<DepartmentInformation> listDepartment) {
		List<BsymtDepartmentInfor> listEntity = listDepartment.stream().map(d -> BsymtDepartmentInfor.fromDomain(d))
				.collect(Collectors.toList());
		this.commandProxy().insertAll(listEntity);
	}

	@Override
	public void deleteDepartmentInforOfHistory(String companyId, String depHistId) {
		List<BsymtDepartmentInfor> listEntity = this.queryProxy()
				.query(FIND_ALL_DEP_BY_COMPANY_AND_HIST, BsymtDepartmentInfor.class)
				.setParameter("companyId", companyId).setParameter("depHistId", depHistId).getList();
		this.commandProxy().removeAll(listEntity);
	}

	@Override
	public List<DepartmentInformation> getDepartmentByDepIds(String companyId, String depHistId,
			List<String> listDepartmentId) {
		if (listDepartmentId.isEmpty())
			return Collections.emptyList();
		String query = "SELECT i FROM BsymtDepartmentInfor i WHERE i.pk.companyId = :companyId "
				+ "AND i.pk.departmentHistoryId = :depHistId AND i.pk.departmentId IN :listDepId";
		List<DepartmentInformation> result = new ArrayList<>();
		CollectionUtil.split(listDepartmentId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subListId -> {
			result.addAll(this.queryProxy().query(query, BsymtDepartmentInfor.class)
					.setParameter("companyId", companyId).setParameter("depHistId", depHistId)
					.setParameter("listDepId", subListId).getList(i -> i.toDomain()));
		});
		return result;
	}

}
