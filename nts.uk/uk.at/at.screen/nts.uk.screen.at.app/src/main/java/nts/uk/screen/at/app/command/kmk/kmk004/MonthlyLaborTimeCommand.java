package nts.uk.screen.at.app.command.kmk.kmk004;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.MonthlyEstimateTime;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyLaborTime;

/**
 * 
 * @author sonnlb
 *
 */
@Data
@NoArgsConstructor
public class MonthlyLaborTimeCommand {
	/** 法定労働時間 */
	private Integer legalLaborTime;

	/** 所定労働時間 */
	/** 勤務区分がフレックスの場合、必ず所定労働時間と週平均時間が存在する */
	private Integer withinLaborTime;

	/** 週平均時間 */
	/** 勤務区分がフレックスの場合、必ず所定労働時間と週平均時間が存在する */
	private Integer weekAvgTime;

	public MonthlyLaborTime toDomain() {
		return MonthlyLaborTime.of(new MonthlyEstimateTime(this.legalLaborTime == null ? 0
				: this.legalLaborTime),
				Optional.ofNullable(
						this.withinLaborTime == null ? null : new MonthlyEstimateTime(this.withinLaborTime)),
				Optional.ofNullable(this.weekAvgTime == null ? null : new MonthlyEstimateTime(this.weekAvgTime)));
	}

	public MonthlyLaborTimeCommand(int legalLaborTime, Integer withinLaborTime, Integer weekAvgTime) {
		super();
		this.legalLaborTime = legalLaborTime;
		this.withinLaborTime = withinLaborTime;
		this.weekAvgTime = weekAvgTime;
	}

}
