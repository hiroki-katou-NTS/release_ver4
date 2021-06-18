package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workclock.pclogon.AggrPCLogonClock;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workclock.pclogon.PCLogonClockOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績のPCログオン時刻 + 月別実績のPCログオン乖離 */
public class PCLogOnClockOfMonthly implements ItemConst, AttendanceItemDataGate {

	/** PCログオフ時刻: 集計PCログオン時刻 + PCログオフ乖離: 集計PCログオン乖離 */
	@AttendanceItemLayout(jpPropertyName = LOGOFF, layout = LAYOUT_A)
	private TotalPcLogonClock logOff;

	/** PCログオン時刻: 集計PCログオン時刻 + PCログオン乖離: 集計PCログオン乖離 */
	@AttendanceItemLayout(jpPropertyName = LOGON, layout = LAYOUT_B)
	private TotalPcLogonClock logOn;
	
	public static PCLogOnClockOfMonthly from(PCLogonClockOfMonthly domain){
		if(domain != null){
			return new PCLogOnClockOfMonthly(TotalPcLogonClock.from(domain.getLogoffClock()), 
											TotalPcLogonClock.from(domain.getLogonClock()));
		}
		return null;
	}
	
	public PCLogonClockOfMonthly toDomain(){
		return PCLogonClockOfMonthly.of(logOn == null ? new AggrPCLogonClock() : logOn.toDomain(), 
										logOff == null ? new AggrPCLogonClock() : logOff.toDomain());
	}

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		switch (path) {
		case LOGOFF:
		case LOGON:
			return new TotalPcLogonClock();
		default:
			return null;
		}
	}

	@Override
	public Optional<AttendanceItemDataGate> get(String path) {
		switch (path) {
		case LOGOFF:
			return Optional.ofNullable(logOff);
		case LOGON:
			return Optional.ofNullable(logOn);
		default:
			return Optional.empty();
		}
	}

	@Override
	public void set(String path, AttendanceItemDataGate value) {
		switch (path) {
		case LOGOFF:
			logOff = (TotalPcLogonClock) value; break;
		case LOGON:
			logOn = (TotalPcLogonClock) value; break;
		default:
		}
	}
}
