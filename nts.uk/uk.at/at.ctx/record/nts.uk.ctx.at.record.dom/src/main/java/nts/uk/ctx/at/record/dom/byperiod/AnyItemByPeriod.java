package nts.uk.ctx.at.record.dom.byperiod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AggregateAnyItem;

/**
 * 期間別の任意項目
 * @author shuichu_ishida
 */
@Getter
public class AnyItemByPeriod implements Cloneable {

	/** 任意項目値 */
	private Map<Integer, AggregateAnyItem> anyItemValues;
	
	/**
	 * コンストラクタ
	 */
	public AnyItemByPeriod(){
		
		this.anyItemValues = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param anyItemValues 任意項目値
	 * @return 期間別の任意項目
	 */
	public static AnyItemByPeriod of(
			List<AggregateAnyItem> anyItemValues){
		
		AnyItemByPeriod domain = new AnyItemByPeriod();
		for (val anyItemValue : anyItemValues){
			val anyItemNo = anyItemValue.getAnyItemNo();
			domain.anyItemValues.putIfAbsent(anyItemNo, anyItemValue);
		}
		return domain;
	}
	
	@Override
	public AnyItemByPeriod clone() {
		AnyItemByPeriod cloned = new AnyItemByPeriod();
		try {
			for (val value : this.anyItemValues.entrySet()){
				cloned.anyItemValues.putIfAbsent(value.getKey(), value.getValue().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("AnyItemByPeriod clone error.");
		}
		return cloned;
	}
}
