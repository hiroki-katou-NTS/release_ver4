package nts.uk.ctx.sys.assist.infra.repository.storage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageMng;
import nts.uk.ctx.sys.assist.dom.storage.DataStorageMngRepository;
import nts.uk.ctx.sys.assist.dom.storage.OperatingCondition;
import nts.uk.ctx.sys.assist.infra.entity.storage.SspmtDataStorageMng;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class JpaDataStorageMngRepository extends JpaRepository implements DataStorageMngRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspmtDataStorageMng f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.storeProcessingId =:storeProcessingId ";

	@Override
	public List<DataStorageMng> getAllDataStorageMng() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspmtDataStorageMng.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<DataStorageMng> getDataStorageMngById(String storeProcessingId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, SspmtDataStorageMng.class)
				.setParameter("storeProcessingId", storeProcessingId).getSingle(c -> c.toDomain());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.assist.dom.storage.DataStorageMngRepository#add(nts.uk.ctx
	 * .sys.assist.dom.storage.DataStorageMng)
	 */
	@Override
	public void add(DataStorageMng domain) {
		this.commandProxy().insert(SspmtDataStorageMng.toEntity(domain));

	}

	@Override
	public boolean update(String storeProcessingId, int categoryTotalCount, int categoryCount,
			OperatingCondition operatingCondition) {
		SspmtDataStorageMng entity = this.getEntityManager().find(SspmtDataStorageMng.class, storeProcessingId);
		if (Objects.isNull(entity)) {
			return false;
		}
		entity.categoryTotalCount = categoryTotalCount;
		entity.categoryCount = categoryCount;
		entity.operatingCondition = operatingCondition.value;
		this.commandProxy().update(entity);
		return true;
	}

	@Override
	public boolean increaseCategoryCount(String storeProcessingId) {
		SspmtDataStorageMng entity = this.getEntityManager().find(SspmtDataStorageMng.class, storeProcessingId);
		if (Objects.isNull(entity)) {
			return false;
		}
		entity.categoryCount += 1;
		this.commandProxy().update(entity);
		return true;
	}
	
	@Override
	public boolean update(String storeProcessingId, OperatingCondition operatingCondition) {
		SspmtDataStorageMng entity = this.getEntityManager().find(SspmtDataStorageMng.class, storeProcessingId);
		if (Objects.isNull(entity)) {
			return false;
		}
		entity.operatingCondition = operatingCondition.value;
		this.commandProxy().update(entity);
		return true;
	}
	
	@Override
    public void update(String storeProcessingId, NotUseAtr doNotInterrupt){
		SspmtDataStorageMng entity = this.getEntityManager().find(SspmtDataStorageMng.class, storeProcessingId);
        entity.doNotInterrupt = doNotInterrupt.value;
		this.commandProxy().update(entity);
    }
	
	@Override
    public void remove(String storeProcessingId){
        this.commandProxy().remove(SspmtDataStorageMng.class, storeProcessingId); 
    }

}
