package nts.uk.ctx.at.record.dom.stamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
//import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationName;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
/**
 * 打刻
 * @author dudt
 *
 */

@AllArgsConstructor
@Getter
public class StampItem extends AggregateRoot {
	/**
	 * カード番号
	 */
	private CardNumber cardNumber;
	/**
	 * 勤怠時刻
	 */
	@Setter
	private AttendanceTime attendanceTime;
	/**
	 * 打刻組み合わせ区分
	 */
	private StampCombinationAtr stampCombinationAtr;
	/**
	 * 就業時間帯コード
	 */
	private SiftCd SiftCd;
	/**
	 * 打刻方法
	 */
	private StampMethod stampMethod;
	/**
	 * 打刻区分
	 */
	private StampAtr stampAtr;
	/**
	 * 勤務場所コード
	 */
	private WorkLocationCD workLocationCd;
	//null
	private WorkLocationName workLocationName;
	/**
	 * 外出理由
	 */
	private GoOutReason goOutReason;
	/**
	 * 年月日
	 */
	private GeneralDateTime date;
	/**
	 * 
	 */
	private String employeeId;
	/**
	 * 反映済み区分
	 */
	private ReflectedAtr reflectedAtr;
	public static StampItem createFromJavaType(String cardNumber, int attendanceTime,
			int stampCombinationAtr, String SiftCd, int stampMethod, int stampAtr, String workLocationCd,String workLocationName,
			int stampReason, GeneralDateTime date,String personId, Integer reflected ) {
		return new StampItem(new CardNumber(cardNumber), 
				new AttendanceTime(attendanceTime),
				EnumAdaptor.valueOf(stampCombinationAtr, StampCombinationAtr.class), 
				new SiftCd(SiftCd),
				EnumAdaptor.valueOf(stampMethod, StampMethod.class), 
				EnumAdaptor.valueOf(stampAtr, StampAtr.class),
				new WorkLocationCD(workLocationCd), 
				new WorkLocationName(workLocationName),
				EnumAdaptor.valueOf(stampReason, GoOutReason.class), 
				date,
				personId,
				EnumAdaptor.valueOf(reflected, ReflectedAtr.class));
	}
}
