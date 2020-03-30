//package nts.uk.ctx.bs.employee.infra.repository.department.master;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//
//import nts.arc.layer.infra.data.JpaRepository;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfigurationRepository;
//import nts.uk.ctx.bs.employee.infra.entity.department.master.BsymtDepartmentConfig;
//import nts.uk.ctx.bs.employee.infra.entity.department.master.BsymtDepartmentConfigPk;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class JpaDepartmentConfigurationRepository extends JpaRepository implements DepartmentConfigurationRepository {
//
//	@Override
//	public Optional<DepartmentConfiguration> getDepConfig(String companyId) {
//		String query = "SELECT c FROM BsymtDepartmentConfig c WHERE c.pk.companyId = :companyId";
//		List<BsymtDepartmentConfig> listEntities = this.queryProxy().query(query, BsymtDepartmentConfig.class)
//				.setParameter("companyId", companyId).getList();
//		return Optional.ofNullable(BsymtDepartmentConfig.toDomain(listEntities));
//	}
//
//	@Override
//	public void addDepartmentConfig(DepartmentConfiguration depConfig) {
//		this.commandProxy().insertAll(BsymtDepartmentConfig.fromDomain(depConfig));
//	}
//
//	@Override
//	public void updateDepartmentConfig(DepartmentConfiguration depConfig) {
//		this.commandProxy().updateAll(BsymtDepartmentConfig.fromDomain(depConfig));
//	}
//
//	@Override
//	public void deleteDepartmentConfig(String companyId, String departmentHistoryId) {
//		this.commandProxy().remove(BsymtDepartmentConfig.class,
//				new BsymtDepartmentConfigPk(companyId, departmentHistoryId));
//	}
//
//}
