package nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex;

import lombok.Getter;
import lombok.val;

/**
 * 集計時間設定
 * @author shuichu_ishida
 */
@Getter
public class AggregateTimeSetting {

	/** 集計設定 */
	private AggregateSetting aggregateSet;
	
	/**
	 * コンストラクタ
	 */
	public AggregateTimeSetting(){
		
		this.aggregateSet = AggregateSetting.INCLUDE_ALL_OUTSIDE_TIME_IN_FLEX_TIME;
	}
	
	/**
	 * ファクトリー
	 * @param aggregateSet 集計設定
	 * @return 集計時間設定
	 */
	public static AggregateTimeSetting of(
			AggregateSetting aggregateSet){
		
		val domain = new AggregateTimeSetting();
		domain.aggregateSet = aggregateSet;
		return domain;
	}
}
