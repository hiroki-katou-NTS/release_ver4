package nts.uk.ctx.sys.assist.infra.repository.deletedata;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.OperatingCondition;
import nts.uk.ctx.sys.assist.infra.entity.deletedata.SspdtManagementDeletion;
import nts.uk.ctx.sys.assist.infra.entity.storage.SspmtDataStorageMng;

@Stateless
public class JpaManagementDeletionRepository extends JpaRepository implements ManagementDeletionRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspdtManagementDeletion f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.sspdtManagementDeletionPK.delId =:delId ";

	@Override
	public List<ManagementDeletion> getAllManagementDeletion() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspdtManagementDeletion.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<ManagementDeletion> getManagementDeletionById(String delId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, SspdtManagementDeletion.class)
				.setParameter("delId", delId).getSingle(c -> c.toDomain());
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void add(ManagementDeletion domain) {
		this.commandProxy().insert(SspdtManagementDeletion.toEntity(domain));

	}

	@Override
	public void updateTotalCatCount(String delId, int totalCategoryCount) {
		ManagementDeletion entity = this.getEntityManager().find(ManagementDeletion.class, delId);
		entity.delId = delId;
		entity.totalCategoryCount = totalCategoryCount;
		this.commandProxy().update(SspdtManagementDeletion.toEntity(entity));
	}

	@Override
	public void updateCatCountAnCond(String delId, int categoryCount, OperatingCondition operatingCondition) {
		ManagementDeletion entity = this.getEntityManager().find(ManagementDeletion.class, delId);
		entity.delId = delId;
		entity.categoryCount = categoryCount;
		entity.operatingCondition = operatingCondition;
		this.commandProxy().update(SspdtManagementDeletion.toEntity(entity));
	}

	@Override
	public void update(ManagementDeletion managementDeletion) {
		 this.commandProxy().update(SspdtManagementDeletion.toEntity(managementDeletion));
	}
	
	@Override
	public void updateCatCount(String delId, int categoryCount) {
		ManagementDeletion entity = this.getEntityManager().find(ManagementDeletion.class, delId);
		entity.delId = delId;
		entity.categoryCount = categoryCount;
		this.commandProxy().update(SspdtManagementDeletion.toEntity(entity));
	}

	@Override
	public void updateOperationCond(String delId, OperatingCondition operatingCondition) {
		ManagementDeletion entity = this.getEntityManager().find(ManagementDeletion.class, delId);
		entity.operatingCondition = operatingCondition;
		this.commandProxy().update(SspdtManagementDeletion.toEntity(entity));
	}
}
