package nts.uk.ctx.sys.assist.infra.repository.deletedata;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.OperatingCondition;
import nts.uk.ctx.sys.assist.infra.entity.deletedata.SspdtManagementDeletion;
import nts.uk.ctx.sys.assist.infra.entity.deletedata.SspdtManagementDeletionPK;

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
		this.getEntityManager().flush();
	}

	@Override
	public void updateTotalCatCount(String delId, int totalCategoryCount) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> entityOpt = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (entityOpt.isPresent()) {
			SspdtManagementDeletion entity = entityOpt.get();
			entity.totalCategoryCount = totalCategoryCount;
			this.commandProxy().update(entity);
		}
	}

	@Override
	public void updateCatCountAnCond(String delId, int categoryCount, OperatingCondition operatingCondition) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> entityOpt = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (entityOpt.isPresent()) {
			SspdtManagementDeletion entity = entityOpt.get();
			entity.categoryCount = categoryCount;
			entity.operatingCondition = operatingCondition.value;
			this.commandProxy().update(entity);
		}
	}

	@Override
	public void update(ManagementDeletion managementDeletion) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(managementDeletion.getDelId());
		Optional<SspdtManagementDeletion> entityOpt = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (entityOpt.isPresent()) {
			SspdtManagementDeletion entity = entityOpt.get();
			entity.errorCount = managementDeletion.errorCount;
			this.commandProxy().update(entity);
		}
	}
	
	@Override
	public void updateCatCount(String delId, int categoryCount) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> entityOpt = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (entityOpt.isPresent()) {
			SspdtManagementDeletion entity = entityOpt.get();
			entity.categoryCount = categoryCount;
			this.commandProxy().update(entity);
		}
	}

	@Override
	public void updateOperationCond(String delId, OperatingCondition operatingCondition) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> entityOpt = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (entityOpt.isPresent()) {
			SspdtManagementDeletion entity = entityOpt.get();
			entity.operatingCondition = operatingCondition.value;
			this.commandProxy().update(entity);
		}
	}
	
	@Override
	public void setInterruptDeleting(String delId,int interruptedFlg,OperatingCondition operatingCondition ) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> optEntity = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (optEntity.isPresent()) {
			SspdtManagementDeletion entity = optEntity.get();
			entity.operatingCondition = operatingCondition.value;
			entity.isInterruptedFlg = interruptedFlg;
			this.commandProxy().update(entity);
		}
	}
	
	@Override
    public void remove(String delId) {
		SspdtManagementDeletionPK sspdtManagementDeletionPK = new  SspdtManagementDeletionPK(delId);
		Optional<SspdtManagementDeletion> optEntity = this.queryProxy().find(sspdtManagementDeletionPK, SspdtManagementDeletion.class);
		if (optEntity.isPresent()) {
			this.commandProxy().remove(SspdtManagementDeletion.class, sspdtManagementDeletionPK);
		}
    }
}
