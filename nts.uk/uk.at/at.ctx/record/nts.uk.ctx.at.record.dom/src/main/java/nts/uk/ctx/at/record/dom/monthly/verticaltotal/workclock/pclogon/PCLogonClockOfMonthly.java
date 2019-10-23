package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;

/**
 * 月別実績のPCログオン時刻
 * @author shuichu_ishida
 */
@Getter
public class PCLogonClockOfMonthly {

	/** PCログオン時刻 */
	private AggrPCLogonClock logonClock;
	/** PCログオフ時刻 */
	private AggrPCLogonClock logoffClock;
	
	/**
	 * コンストラクタ
	 */
	public PCLogonClockOfMonthly(){
		
		this.logonClock = new AggrPCLogonClock();
		this.logoffClock = new AggrPCLogonClock();
	}

	/**
	 * ファクトリー
	 * @param logonClock PCログオン時刻
	 * @param logoffClock PCログオフ時刻
	 * @return 月別実績のログオン時刻
	 */
	public static PCLogonClockOfMonthly of(
			AggrPCLogonClock logonClock,
			AggrPCLogonClock logoffClock){
		
		PCLogonClockOfMonthly domain = new PCLogonClockOfMonthly();
		domain.logonClock = logonClock;
		domain.logoffClock = logoffClock;
		return domain;
	}
	
	/**
	 * 集計
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 */
	public void aggregate(Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt){
		
		// ログオンの集計
		this.logonClock.aggregate(pcLogonInfoOpt, true);
		
		// ログオフの集計
		this.logoffClock.aggregate(pcLogonInfoOpt, false);
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(PCLogonClockOfMonthly target){
		
		this.logonClock.sum(target.logonClock);
		this.logoffClock.sum(target.logoffClock);
	}
}
