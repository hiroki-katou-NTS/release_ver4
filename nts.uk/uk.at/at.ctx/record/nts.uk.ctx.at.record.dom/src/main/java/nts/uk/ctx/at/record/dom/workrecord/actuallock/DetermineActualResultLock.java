package nts.uk.ctx.at.record.dom.workrecord.actuallock;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
/**
 * 実績ロックされているか判定する
 */
@Stateless
public class DetermineActualResultLock {
	/** 実績ロック */
	@Inject
	private ActualLockRepository actualLockRepository;
	/** 締め  */
	@Inject
	private ClosureRepository closureRepository;
	@Inject
	private ClosureService closureSevice;
	/**
	 * 実績ロックされているか判定する
	 * 
	 * @param companyId 会社ID：会社ID 
	 * @param processingDate 基準日：年月日
	 * @param closureId 締めID：締めID
	 * @param performaceType 実績の種類：月別or日別
	 * @return 実績のロック状態 ロックorアンロック
	 */
	public LockStatus getDetermineActualLocked(String companyId, GeneralDate baseDate, Integer closureId, PerformanceType type) {
		LockStatus currentLockStatus;
		//ドメインモデル「当月の実績ロック」を取得する
		Optional<ActualLock> actualLock = this.actualLockRepository.findById(companyId, closureId);		
		if (!actualLock.isPresent()) {
			return LockStatus.UNLOCK;
		}
		//パラメータ「実績の種類」をチェックする
		if(type.equals(PerformanceType.DAILY)){
			//日のロック状態を判定する
			currentLockStatus = actualLock.get().getDailyLockState();
		} else {
			// 月のロック状態を判定する
			currentLockStatus = actualLock.get().getMonthlyLockState();

		}
		//ロック状態をチェックする
		if (currentLockStatus.equals(LockStatus.UNLOCK)) {
			return LockStatus.UNLOCK;
		}
		//ドメインモデル「締め」を取得する
		Optional<Closure> closure = closureRepository.findById(companyId, closureId);
		
		// exist closure data
		if (closure.isPresent()) {		
			//基準日が当月に含まれているかチェックする
			//Optional<ClosurePeriod> closurePeriod = closure.get().getClosurePeriodByYmd(baseDate);
			if (closureSevice.includeDate(baseDate, closure.get())) {
				//基準日が締め期間に含まれている
				return LockStatus.LOCK;
			}else{
				//基準日が締め期間に含まれていない
				return LockStatus.UNLOCK;
			}
		}
		//Default Unlock status
		return LockStatus.UNLOCK;
	}
}
