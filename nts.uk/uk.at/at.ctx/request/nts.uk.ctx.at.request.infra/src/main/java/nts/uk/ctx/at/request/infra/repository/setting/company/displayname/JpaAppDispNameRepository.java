package nts.uk.ctx.at.request.infra.repository.setting.company.displayname;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.infra.entity.setting.company.displayname.KrqmtAppDispName;
import nts.uk.ctx.at.request.infra.entity.setting.company.displayname.KrqmtAppDispNamePK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaAppDispNameRepository extends JpaRepository implements AppDispNameRepository{
	public static final String SELECT_BY_CID = "SELECT c FROM KrqmtAppDispName c WHERE c.krqmtAppDispNamePK.companyId = :companyId";
	/**
	 * convert from entity to domain
	 * @param entity
	 * @return
	 * @author yennth
	 */
	private static AppDispName toDomain(KrqmtAppDispName entity){
		AppDispName domain = AppDispName.createFromJavaType(entity.krqmtAppDispNamePK.companyId,
															entity.krqmtAppDispNamePK.appType, entity.dispName);
		return domain;
	}
	/**
	 * convert from domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
	private static KrqmtAppDispName toEntity(AppDispName domain){
		val entity = new KrqmtAppDispName();
		entity.krqmtAppDispNamePK = new KrqmtAppDispNamePK(domain.getCompanyId(), domain.getAppType().value);
		entity.dispName = domain.getDispName() == null ? null : domain.getDispName().v();
		return entity;
	}
	/**
	 * get all display name
	 * @author yennth
	 */
	@Override
	public List<AppDispName> getAll() {
		String companyId = AppContexts.user().companyId();
		return this.queryProxy().query(SELECT_BY_CID, KrqmtAppDispName.class)
				.setParameter("companyId", companyId)
				.getList(c -> toDomain(c));
	}
	/**
	 * get display name by app type
	 * @author yennth
	 */
	@Override
	public Optional<AppDispName> getDisplay(int appType) {
		String companyId = AppContexts.user().companyId();
		return this.queryProxy().find(new KrqmtAppDispNamePK(companyId, appType), KrqmtAppDispName.class)
				.map(x -> toDomain(x));
	}
	/**
	 * update app display name
	 * @author yennth
	 */
	@Override
	public void update(AppDispName display) {
		KrqmtAppDispName entity = toEntity(display);
		KrqmtAppDispName oldEntity = this.queryProxy().find(entity.krqmtAppDispNamePK, KrqmtAppDispName.class).get();
		oldEntity.dispName = entity.dispName;
		this.commandProxy().update(oldEntity);
	}
	/**
	 * insert app display name
	 * @author yennth
	 */
	@Override
	public void insert(AppDispName display) {
		KrqmtAppDispName entity = toEntity(display);
		this.commandProxy().insert(entity);
	}
}
