//package nts.uk.ctx.bs.employee.infra.repository.workplace.master;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//
//import nts.arc.layer.infra.data.JpaRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfigurationRepository;
//import nts.uk.ctx.bs.employee.infra.entity.workplace.master.BsymtWorkplaceConfig;
//import nts.uk.ctx.bs.employee.infra.entity.workplace.master.BsymtWorkplaceConfigPk;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class JpaWorkplaceConfigurationRepository extends JpaRepository implements WorkplaceConfigurationRepository {
//
//	@Override
//	public Optional<WorkplaceConfiguration> getWkpConfig(String companyId) {
//		String query = "SELECT c FROM BsymtWorkplaceConfig c WHERE c.pk.companyId = :companyId";
//		List<BsymtWorkplaceConfig> listEntities = this.queryProxy().query(query, BsymtWorkplaceConfig.class)
//				.setParameter("companyId", companyId).getList();
//		return Optional.ofNullable(BsymtWorkplaceConfig.toDomain(listEntities));
//	}
//
//	@Override
//	public void addWorkplaceConfig(WorkplaceConfiguration workplaceConfig) {
//		this.commandProxy().insertAll(BsymtWorkplaceConfig.fromDomain(workplaceConfig));
//	}
//
//	@Override
//	public void updateWorkplaceConfig(WorkplaceConfiguration workplaceConfig) {
//		this.commandProxy().updateAll(BsymtWorkplaceConfig.fromDomain(workplaceConfig));
//	}
//
//	@Override
//	public void deleteWorkplaceConfig(String companyId, String workplaceHistoryId) {
//		this.commandProxy().remove(BsymtWorkplaceConfig.class,
//				new BsymtWorkplaceConfigPk(companyId, workplaceHistoryId));
//	}
//
//}
