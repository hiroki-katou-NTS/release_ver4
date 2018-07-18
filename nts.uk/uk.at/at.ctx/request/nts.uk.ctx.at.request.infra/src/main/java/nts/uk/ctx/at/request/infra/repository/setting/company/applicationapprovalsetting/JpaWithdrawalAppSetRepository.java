package nts.uk.ctx.at.request.infra.repository.setting.company.applicationapprovalsetting;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.WithdrawalAppSetRepository;
import nts.uk.ctx.at.request.infra.entity.setting.company.applicationapprovalsetting.hdworkapplicationsetting.KrqstWithDrawalAppSet;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaWithdrawalAppSetRepository extends JpaRepository implements WithdrawalAppSetRepository{
	/**
	 * convert from entity to domain
	 * @param entity
	 * @return
	 * @author yennth
	 */
	private WithdrawalAppSet toDomain(KrqstWithDrawalAppSet entity){
		WithdrawalAppSet domain = WithdrawalAppSet.createFromJavaType(entity.companyId, entity.prePerflex, 
				entity.breakTime, entity.workTime, entity.checkHdTime, entity.typePaidLeave, entity.workChange, 
				entity.timeInit, entity.checkOut, entity.prefixLeave, entity.unitTime, entity.appSimul, 
				entity.bounSeg, entity.directDivi, entity.restTime, entity.overrideSet, entity.calStampMiss);
		return domain;
	}
	/**
	 * convert from domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
	private static KrqstWithDrawalAppSet toEntity(WithdrawalAppSet domain){
		val entity = new KrqstWithDrawalAppSet();
		entity.companyId = domain.getCompanyId();
		entity.appSimul = domain.getAppSimul();
		entity.checkHdTime = domain.getCheckHdTime().value;
		entity.checkOut = domain.getCheckOut().value;
		entity.directDivi = domain.getDirectDivi().value;
		entity.prePerflex = domain.getPrePerflex().value;
		entity.restTime = domain.getRestTime().value;
		entity.timeInit = domain.getTimeInit().value;
		entity.typePaidLeave = domain.getTypePaidLeave().value;
		entity.unitTime = domain.getUnitTime().value;
		entity.workChange = domain.getWorkChange().value;
		entity.calStampMiss = domain.getCalStampMiss().value;
		entity.overrideSet = domain.getOverrideSet().value;
		entity.restTime = domain.getRestTime().value;
		entity.workTime = domain.getWorkTime().value;
		entity.breakTime = domain.getBreakTime().value;
		return entity;
	}
	/**
	 * get with drawal app set
	 * @author yennth
	 */
	@Override
	public Optional<WithdrawalAppSet> getWithDraw() {
		String companyId = AppContexts.user().companyId();
		return this.queryProxy().find(companyId, KrqstWithDrawalAppSet.class).map(c -> toDomain(c));
	}
	/**
	 * update with drawal app set
	 * @author yennth
	 */
	@Override
	public void update(WithdrawalAppSet with) {
		KrqstWithDrawalAppSet entity = toEntity(with);
		Optional<KrqstWithDrawalAppSet> oldEntity = this.queryProxy().find(entity.companyId, KrqstWithDrawalAppSet.class);
		
		if (oldEntity.isPresent()) {
			KrqstWithDrawalAppSet wdAppSet = oldEntity.get();
			wdAppSet.workTime = entity.workTime;
			wdAppSet.restTime = entity.restTime;
			wdAppSet.checkHdTime = entity.checkHdTime;
			wdAppSet.typePaidLeave = entity.typePaidLeave;
			wdAppSet.workChange = entity.workChange;
			wdAppSet.unitTime = entity.unitTime;
			wdAppSet.checkOut = entity.checkOut;
			wdAppSet.directDivi = entity.directDivi;
			wdAppSet.overrideSet = entity.overrideSet;
			wdAppSet.calStampMiss = entity.calStampMiss;
			wdAppSet.breakTime = entity.breakTime;
			this.commandProxy().update(wdAppSet);
		} else {
			this.commandProxy().insert(entity);
		}
	}
	/**
	 * insert with drawal app set
	 * @author yennth
	 */
	@Override
	public void insert(WithdrawalAppSet with) {
		KrqstWithDrawalAppSet entity = toEntity(with);
		this.commandProxy().insert(entity);
	}

}
