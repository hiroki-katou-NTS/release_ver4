package nts.uk.ctx.at.request.dom.setting.company.request.applicationsetting.apptypesetting;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申請受付制限日数
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public enum AppAcceptLimitDay {
	
	/**
	 * 当日 
	 */
	THATDAY(0, "当日"),
	
	/**
	 * 1日前
	 */
	ONEDAYAGO(1, "1日前"),
	
	/**
	 * 2日前
	 */
	TWODAYAGO(2, "2日前"),
	
	/**
	 * 3日前
	 */
	THREEDAYAGO(3, "3日前"),
	
	/**
	 * 4日前
	 */
	FOURDAYAGO(4, "4日前"),
	
	/**
	 * 5日前
	 */
	FIVEDAYAGO(5, "5日前"),
	
	/**
	 * 6日前
	 */
	SIXDAYAGO(6, "6日前"),
	
	/**
	 * 7日前
	 */
	SEVENDAYAGO(7, "7日前");

	public final Integer value;
	
	public final String name;
	
}
