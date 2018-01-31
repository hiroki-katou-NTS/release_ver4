package nts.uk.ctx.at.request.infra.repository.setting.workplace;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachWorkplace;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachWorkplaceRepository;
import nts.uk.ctx.at.request.infra.entity.setting.workplace.KrqstWpAppConfig;
import nts.uk.ctx.at.request.infra.entity.setting.workplace.KrqstWpAppConfigPK;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaRequestOfEarchWorkplaceRepository extends JpaRepository implements RequestOfEachWorkplaceRepository {

	private static final String FIND = "SELECT c " + "FROM KrqstWpAppConfig c "
			+ "WHERE c.krqstWpAppConfigPK.companyId = :companyId "
			+ "AND c.krqstWpAppConfigPK.workplaceId = :workplaceId";

	private static final String FIND_ALL = "SELECT c FROM KrqstWpAppConfig c WHERE c.krqstWpAppConfigPK.companyId = :companyId ";

	/**
	 * get request of earch workplace by app type
	 */
	@Override
	public Optional<RequestOfEachWorkplace> getRequestByWorkplace(String companyId, String workplaceId) {
		return this.queryProxy().query(FIND, KrqstWpAppConfig.class).setParameter("companyId", companyId)
				.setParameter("workplaceId", workplaceId).getSingle(c -> c.toDomain());
	}

	@Override
	public Optional<ApprovalFunctionSetting> getFunctionSetting(String companyId, String workplaceId, Integer appType) {
		Optional<RequestOfEachWorkplace> requestOfEachWorkplace = this.queryProxy().query(FIND, KrqstWpAppConfig.class)
				.setParameter("companyId", companyId).setParameter("workplaceId", workplaceId)
				.getSingle(c -> c.toDomain());
		if (!requestOfEachWorkplace.isPresent()) {
			return Optional.empty();
		}
		return requestOfEachWorkplace.get().getListApprovalFunctionSetting().stream()
				.filter(x -> x.getAppUseSetting().getAppType().value == appType).findAny();
	}

	@Override
	public List<RequestOfEachWorkplace> getAll() {
		return this.queryProxy().query(FIND_ALL, KrqstWpAppConfig.class)
				.setParameter("companyId", AppContexts.user().companyId()).getList(c -> c.toDomain());
	}

	@Override
	public void add(RequestOfEachWorkplace domain) {
		this.commandProxy().insert(KrqstWpAppConfig.fromDomain(domain));
	}

	@Override
	public void update(RequestOfEachWorkplace domain) {
		KrqstWpAppConfig targetEntity = this.queryProxy().find(new KrqstWpAppConfigPK(domain.getCompanyID(), domain.getWorkPlaceID()), KrqstWpAppConfig.class).get();
		KrqstWpAppConfig updateEntity = KrqstWpAppConfig.fromDomain(domain);
		targetEntity.krqstWpAppConfigDetails = updateEntity.krqstWpAppConfigDetails;
		targetEntity.selectOfApproversFlg = updateEntity.selectOfApproversFlg;
		this.commandProxy().update(targetEntity);
	}

}
