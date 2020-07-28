package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.breakouting.reflectgoingoutandreturn;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.breakouting.GoingOutReason;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.common.TimeActualStamp;

/**
 * 時間帯枠（Temporary）
 * @author tutk
 *
 */
@Getter
@NoArgsConstructor
public class TimeFrame {
	/**
	 * 反映回数
	 */
	private int numberOfReflections;
	/**
	 * 枠No
	 */
	private int frameNo;

	/**　開始: 勤怠打刻(実打刻付き) */
	private Optional<TimeActualStamp> start;
	
	/**　終了: 勤怠打刻(実打刻付き) */
	private Optional<TimeActualStamp> end;
	
	/**
	 * 外出理由
	 */
	private Optional<GoingOutReason> goOutReason;

	public TimeFrame(int numberOfReflections, int frameNo, Optional<TimeActualStamp> start,
			Optional<TimeActualStamp> end, GoingOutReason goOutReason) {
		super();
		this.numberOfReflections = numberOfReflections;
		this.frameNo = frameNo;
		this.start = start;
		this.end = end;
		this.goOutReason = Optional.ofNullable(goOutReason);
	}

	public void setFrameNo(int frameNo) {
		this.frameNo = frameNo;
	}
	
}
