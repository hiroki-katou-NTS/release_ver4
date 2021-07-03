package nts.uk.ctx.at.request.app.find.application.lateorleaveearly;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.request.app.find.application.common.AppDispInfoStartupDto;
import nts.uk.ctx.at.request.app.find.application.lateleaveearly.LateEarlyCancelAppSetDto;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.ArrivedLateLeaveEarlyInfoOutput;
//遅刻早退取消申請起動時の表示情報
@Data
@AllArgsConstructor
public class ArrivedLateLeaveEarlyInfoDto {
//	取り消す初期情報
//	private List<LateOrEarlyInfoDto> earlyInfos;
//	申請表示情報
	private AppDispInfoStartupDto appDispInfoStartupOutput;
//	遅刻早退取消申請設定
	private LateEarlyCancelAppSetDto lateEarlyCancelAppSet;
//	エアー情報
	private String info;
//	遅刻早退取消申請
	private ArrivedLateLeaveEarlyDto arrivedLateLeaveEarly;
	
	public static ArrivedLateLeaveEarlyInfoDto convertDto(ArrivedLateLeaveEarlyInfoOutput value) {
		
		return new ArrivedLateLeaveEarlyInfoDto(
				AppDispInfoStartupDto.fromDomain(value.getAppDispInfoStartupOutput()),
				LateEarlyCancelAppSetDto.fromDomain(value.getLateEarlyCancelAppSet()),
				value.getInfo().isPresent() ? value.getInfo().get() : null,
				value.getArrivedLateLeaveEarly().isPresent() ? ArrivedLateLeaveEarlyDto.convertDto(value.getArrivedLateLeaveEarly().get()): null );
	}
	
	public ArrivedLateLeaveEarlyInfoOutput toDomain() {
		return new ArrivedLateLeaveEarlyInfoOutput(
				this.appDispInfoStartupOutput.toDomain(),
				this.lateEarlyCancelAppSet == null ? null : this.lateEarlyCancelAppSet.toDomain(),
				this.info == null ? Optional.empty() : Optional.of(this.info),
				this.arrivedLateLeaveEarly == null ? Optional.empty() : Optional.of(this.arrivedLateLeaveEarly.convertDomain()));
	}
}
