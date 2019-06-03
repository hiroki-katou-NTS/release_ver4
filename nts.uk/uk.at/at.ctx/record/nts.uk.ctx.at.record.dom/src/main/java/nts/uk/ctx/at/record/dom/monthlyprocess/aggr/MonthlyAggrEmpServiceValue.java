package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;

/**
 * 戻り値：ドメインサービス：月別集計　（社員の月別実績を集計する）
 * @author shuichi_ishida
 */
@Getter
public class MonthlyAggrEmpServiceValue {

	/** 出力した集計期間 */
	private List<AggrPeriodEachActualClosure> outAggrPeriod;
	/** 終了状態 */
	@Setter
	private ProcessState state;
	
	public MonthlyAggrEmpServiceValue(){
		this.outAggrPeriod = new ArrayList<>();
		this.state = ProcessState.SUCCESS;
	}
}
