package nts.uk.ctx.at.record.infra.repository.monthlyaggrmethod.flex;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.KrcstMonsetWkpRegAggrPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.workplace.KrcstMonsetWkpFlxAggr;
import nts.uk.ctx.at.shared.dom.monthlyaggrmethod.flex.AggrSettingMonthlyOfFlx;
import nts.uk.ctx.at.shared.dom.monthlyaggrmethod.flex.AggrSettingMonthlyOfFlxForWkpRepository;

/**
 * リポジトリ実装：職場のフレックス時間勤務の月の集計設定
 * @author shuichi_ishida
 */
@Stateless
public class JpaAggrSettingMonthlyOfFlxForWkp extends JpaRepository implements AggrSettingMonthlyOfFlxForWkpRepository {
	
	/** 更新 */
	@Override
	public void update(String companyId, String workplaceId, AggrSettingMonthlyOfFlx aggrSettingMonthlyOfFlx) {
		this.toUpdate(companyId, workplaceId, aggrSettingMonthlyOfFlx);
	}
	
	/**
	 * データ更新
	 * @param companyId キー値：会社ID
	 * @param workplaceId キー値：職場ID
	 * @param domain ドメイン：フレックス時間勤務の月の集計設定
	 */
	private void toUpdate(String companyId, String workplaceId, AggrSettingMonthlyOfFlx domain){
		
		// キー
		val key = new KrcstMonsetWkpRegAggrPK(companyId, workplaceId);
		
		// フレックス不足設定
		val shortageSet = domain.getShortageSet();
		// 法定内集計設定
		val legalAggrSet = domain.getLegalAggregateSet();
		// 集計時間設定
		val aggrTimeSet = legalAggrSet.getAggregateTimeSet();
		// 時間外超過設定
		val excessOutsideTimeSet = legalAggrSet.getExcessOutsideTimeSet();
		// 36協定集計方法
		val aggrMethod36Agreement = domain.getArrgMethod36Agreement();
		
		KrcstMonsetWkpFlxAggr entity = this.getEntityManager().find(KrcstMonsetWkpFlxAggr.class, key);
		if (entity == null) return;
		entity.setValue.aggregateMethod = domain.getAggregateMethod().value;
		entity.setValue.includeOverTime = (domain.isIncludeOverTime() ? 1 : 0);
		entity.setValue.carryforwardSet = shortageSet.getCarryforwardSet().value;
		entity.setValue.aggregateSet = aggrTimeSet.getAggregateSet().value;
		entity.setValue.excessOutsideTimeTargetSet = excessOutsideTimeSet.getExcessOutsideTimeTargetSet().value;
		entity.setValue.aggregateMethodOf36AgreementTime = aggrMethod36Agreement.getAggregateMethod().value;
	}
}
