package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.onemonth.AgreementOneMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の36協定時間 */
public class AgreementTimeOfMonthlyDto implements ItemConst {

	/** 36協定時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = AGREEMENT, layout = LAYOUT_A)
	private int agreementTime;

	/** 限度アラーム時間: ３６協定１ヶ月時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = LIMIT_ALARM, layout = LAYOUT_B)
	private int limitAlarmTime;

	/** 限度エラー時間: ３６協定１ヶ月時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = LIMIT_ERROR, layout = LAYOUT_C)
	private int limitErrorTime;

	/** AgreementTimeStatusOfMonthly */
	/** 状態: 月別実績の36協定時間状態 */
	@AttendanceItemValue(type = ValueType.ATTR)
	@AttendanceItemLayout(jpPropertyName = STATE, layout = LAYOUT_D)
	private int status;

	/** 特例限度アラーム時間: ３６協定１ヶ月時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = EXCEPTION +  LIMIT_ALARM, layout = LAYOUT_E)
	private Integer exceptionLimitAlarmTime;

	/** 特例限度エラー時間: ３６協定１ヶ月時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = EXCEPTION + LIMIT_ERROR, layout = LAYOUT_F)
	private Integer exceptionLimitErrorTime;

	public AgreementTimeOfMonthly toDomain() {
		return AgreementTimeOfMonthly.of(new AttendanceTimeMonth(agreementTime),
										new AgreementOneMonth(limitErrorTime),
										new AgreementOneMonth(limitAlarmTime),
										exceptionLimitErrorTime == null ? Optional.empty()
												: Optional.of(new AgreementOneMonth(exceptionLimitErrorTime)),
										exceptionLimitAlarmTime == null ? Optional.empty()
												: Optional.of(new AgreementOneMonth(exceptionLimitAlarmTime)),
										ConvertHelper.getEnum(status, AgreementTimeStatusOfMonthly.class));
	}
	
	public static AgreementTimeOfMonthlyDto from(AgreementTimeOfMonthly domain) {
		AgreementTimeOfMonthlyDto dto = new AgreementTimeOfMonthlyDto();
		if(domain != null) {
			dto.setAgreementTime(domain.getAgreementTime() == null ? 0 : domain.getAgreementTime().valueAsMinutes());
			dto.setExceptionLimitAlarmTime(from(domain.getExceptionLimitAlarmTime().orElse(null)));
			dto.setExceptionLimitErrorTime(from(domain.getExceptionLimitErrorTime().orElse(null)));
			dto.setLimitAlarmTime(from(domain.getLimitAlarmTime()));
			dto.setLimitErrorTime(from(domain.getLimitErrorTime()));
			dto.setStatus(domain.getStatus() == null ? 0 : domain.getStatus().value);
		}
		return dto;
	}
	
	private static Integer from(AgreementOneMonth domain) {
		return domain == null ? 0 : domain.valueAsMinutes();
	}
}
