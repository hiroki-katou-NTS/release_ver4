package nts.uk.ctx.at.shared.dom.monthly.verticaltotal.worktime.goout;

import java.io.Serializable;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.times.AttendanceTimesMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.shortworktime.ChildCareAtr;

/**
 * 育児外出
 * @author shuichi_ishida
 */
@Getter
public class GoOutForChildCare implements Serializable{

	/** Serializable */
	private static final long serialVersionUID = 1L;

	/** 育児介護区分 */
	private ChildCareAtr childCareAtr;
	/** 回数 */
	private AttendanceTimesMonth times;
	/** 時間 */
	private AttendanceTimeMonth time;
	
	/**
	 * コンストラクタ
	 */
	public GoOutForChildCare(ChildCareAtr childCareAtr){
		
		this.childCareAtr = childCareAtr;
		this.times = new AttendanceTimesMonth(0);
		this.time = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param childCareAtr 育児介護区分
	 * @param times 回数
	 * @param time 時間
	 * @return 育児外出
	 */
	public static GoOutForChildCare of(
			ChildCareAtr childCareAtr,
			AttendanceTimesMonth times,
			AttendanceTimeMonth time){
		
		val domain = new GoOutForChildCare(childCareAtr);
		domain.times = times;
		domain.time = time;
		return domain;
	}
	
	/**
	 * 回数に加算する
	 * @param times 回数
	 */
	public void addTimes(int times){
		this.times = this.times.addTimes(times);
	}
	
	/**
	 * 時間に分を加算する
	 * @param minutes 分
	 */
	public void addMinutesToTime(int minutes){
		this.time = this.time.addMinutes(minutes);
	}
}
