package nts.uk.ctx.at.request.infra.repository.setting.company.applicationapprovalsetting.widrawalreqset;

import java.sql.PreparedStatement;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.task.AsyncTaskStatus;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WorkUse;
import nts.uk.ctx.at.request.infra.entity.setting.company.applicationapprovalsetting.withdrawalrequestset.KrqstWithDrawalReqSet;
import nts.uk.shr.com.context.AppContexts;
/**
 * Jpa With Drawal Request Setting Repository
 * @author yennth
 *
 */
@Stateless
public class JpaWithDrawalReqSetRepository extends JpaRepository implements WithDrawalReqSetRepository {

	@Override
	public Optional<WithDrawalReqSet> getWithDrawalReqSet() {
		String companyId = AppContexts.user().companyId();
		return this.queryProxy().find(companyId, KrqstWithDrawalReqSet.class).map(x -> toDomain(x));
	}

	private static WithDrawalReqSet toDomain(KrqstWithDrawalReqSet entity) {
		WithDrawalReqSet domain = WithDrawalReqSet.createFromJavaType(entity.getCompanyId(), 
				entity.getPermissionDivision(), 
				entity.getAppliDateContrac(),
				entity.getUseAtr(),
				entity.getCheckUpLimitHalfDayHD(),
				entity.getPickUpComment(),
				entity.getPickUpBold(),
				entity.getPickUpLettleColor(),
				entity.getDeferredComment(),
				entity.getDeferredBold(),
				entity.getDeferredLettleColor(),
				entity.getDeferredWorkTimeSelect(),
				entity.getSimulAppliReq(),
				entity.getLettleSuperLeave());
		return domain;
	}

	@Override
	public void updateWithDrawalReqSet(WithDrawalReqSet withDrawalReqSet) {
		KrqstWithDrawalReqSet entity = toEntity(withDrawalReqSet);
		KrqstWithDrawalReqSet oldEntity = this.queryProxy().find(entity.getCompanyId(), KrqstWithDrawalReqSet.class).get();
		
		oldEntity.setCompanyId(entity.getCompanyId());
		oldEntity.setPermissionDivision(entity.getPermissionDivision());
		oldEntity.setAppliDateContrac(entity.getAppliDateContrac());
		oldEntity.setUseAtr(entity.getUseAtr());
		oldEntity.setCheckUpLimitHalfDayHD(entity.getCheckUpLimitHalfDayHD());
		oldEntity.setPickUpComment(entity.getPickUpComment());
		oldEntity.setPickUpBold(entity.getPickUpBold());
		oldEntity.setPickUpLettleColor(entity.getPickUpLettleColor());
		oldEntity.setDeferredComment(entity.getDeferredComment());
		oldEntity.setDeferredBold(entity.getDeferredBold());
		oldEntity.setDeferredLettleColor(entity.getDeferredLettleColor());
		oldEntity.setDeferredWorkTimeSelect(entity.getDeferredWorkTimeSelect());
		oldEntity.setSimulAppliReq(entity.getSimulAppliReq());
		oldEntity.setLettleSuperLeave(entity.getLettleSuperLeave());
		
		this.commandProxy().update(oldEntity);
	}

	@Override
	public void addWithDrawalReqSet(WithDrawalReqSet withDrawalReqSet) {
		KrqstWithDrawalReqSet entity = toEntity(withDrawalReqSet);
		this.commandProxy().insert(entity);
	}

	private static KrqstWithDrawalReqSet toEntity(WithDrawalReqSet withDrawalReqSet) {
		val entity = new KrqstWithDrawalReqSet();
		entity.setCompanyId(withDrawalReqSet.getCompanyId());
		entity.setPermissionDivision(withDrawalReqSet.getPermissionDivision().value);
		entity.setAppliDateContrac(withDrawalReqSet.getAppliDateContrac().value);
		entity.setUseAtr(withDrawalReqSet.getUseAtr().value);
		entity.setCheckUpLimitHalfDayHD(withDrawalReqSet.getCheckUpLimitHalfDayHD().value);
		entity.setPickUpComment(withDrawalReqSet.getPickUpComment().isPresent() ? withDrawalReqSet.getPickUpComment().get().v() : null);
		entity.setPickUpBold(withDrawalReqSet.getPickUpBold().value);
		entity.setPickUpLettleColor(withDrawalReqSet.getPickUpLettleColor());
		entity.setDeferredComment(withDrawalReqSet.getDeferredComment().isPresent() ? withDrawalReqSet.getDeferredComment().get().v() : null);
		entity.setDeferredBold(withDrawalReqSet.getDeferredBold().value);
		entity.setDeferredLettleColor(withDrawalReqSet.getDeferredLettleColor());
		entity.setDeferredWorkTimeSelect(withDrawalReqSet.getDeferredWorkTimeSelect().value);
		entity.setSimulAppliReq(withDrawalReqSet.getSimulAppliReq().value);
		entity.setLettleSuperLeave(withDrawalReqSet.getLettleSuperLeave().value);
		return entity;
	}

	@Override
	@SneakyThrows
	public Optional<WorkUse> getDeferredWorkTimeSelect(String cid) {
		String sql = "SELECT DEFERRED_WORKTIME_SELECT "
				+ "FROM KRQST_WITHDRAWAL_REQ_SET "
				+ "WHERE CID = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)){
			stmt.setString(1, cid);
			return new NtsResultSet(stmt.executeQuery())
					.getSingle(r -> EnumAdaptor.valueOf(r.getInt("DEFERRED_WORKTIME_SELECT"),WorkUse.class));
		}
	}

}
