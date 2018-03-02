package nts.uk.ctx.at.record.dom.monthly.excessoutside;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;

/**
 * 時間外超過　（内訳ごと）
 * @author shuichu_ishida
 */
@Getter
public class ExcessOutSideWorkEachBreakdown {

	/** 内訳NO */
	private int breakdownNo;
	/** 内訳 */
	private Map<Integer, ExcessOutsideWork> breakdown;
	
	/**
	 * コンストラクタ
	 * @param breakdownNo 内訳NO
	 */
	public ExcessOutSideWorkEachBreakdown(int breakdownNo){
		
		this.breakdownNo = breakdownNo;
		this.breakdown = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param breakdownNo 内訳NO
	 * @param breakdown 内訳
	 * @return 時間外超過　（内訳ごと）
	 */
	public static ExcessOutSideWorkEachBreakdown of(int breakdownNo, List<ExcessOutsideWork> breakdown){
		
		ExcessOutSideWorkEachBreakdown domain = new ExcessOutSideWorkEachBreakdown(breakdownNo);
		for (val excessOutsideWork : breakdown){
			if (excessOutsideWork.getBreakdownNo() != breakdownNo) continue;
			domain.breakdown.putIfAbsent(excessOutsideWork.getExcessNo(), excessOutsideWork);
		}
		return domain;
	}
}
