package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.gul.serialize.binary.SerializableWithOptional;

/**
 * 年休使用数
 * @author shuichu_ishida
 */
@Getter
@Setter
public class AnnualLeaveUsedNumber implements Cloneable, SerializableWithOptional {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	/** 使用日数 */
	private AnnualLeaveUsedDays usedDays;
	
	/** 使用時間 */
	private Optional<AnnualLeaveUsedTime> usedTime;
	
	/**
	 * コンストラクタ
	 */
	public AnnualLeaveUsedNumber(){
		
		this.usedDays = new AnnualLeaveUsedDays();
		this.usedTime = Optional.empty();
	}

	/**
	 * ファクトリー
	 * @param usedDays 使用日数
	 * @param usedTime 使用時間
	 * @return 年休使用数
	 */
	public static AnnualLeaveUsedNumber of(
			AnnualLeaveUsedDays usedDays,
			Optional<AnnualLeaveUsedTime> usedTime){
		
		AnnualLeaveUsedNumber domain = new AnnualLeaveUsedNumber();
		domain.usedDays = usedDays;
		domain.usedTime = usedTime;
		return domain;
	}
	
	@Override
	public AnnualLeaveUsedNumber clone() {
		AnnualLeaveUsedNumber cloned = new AnnualLeaveUsedNumber();
		try {
			cloned.usedDays = this.usedDays.clone();
			if (this.usedTime.isPresent()){
				cloned.usedTime = Optional.of(this.usedTime.get().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("AnnualLeaveUsedNumber clone error.");
		}
		return cloned;
	}
	
	/**
	 * 日数を使用日数に加算する
	 */
	public void addUsedNumber(AnnualLeaveUsedNumber usedNumber){
		this.usedDays.addUsedDays(usedNumber.getUsedDays().getUsedDayNumber().v());
		if (this.usedTime.isPresent()){
			this.usedTime.get().getUsedTime().addMinutes(
					usedNumber.getUsedTime().get().getUsedTime().v());
		}
	}
	
	private void writeObject(ObjectOutputStream stream){		
		writeObjectWithOptional(stream);	
	}		
	private void readObject(ObjectInputStream stream){		
		readObjectWithOptional(stream);	
	}		

	
}

