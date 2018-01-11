package nts.uk.ctx.at.record.infra.entity.breakorgoout;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt 日別実績の休憩時間帯
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAI_BREAK_TIME")
public class KrcdtDaiBreakTime extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtDaiBreakTimePK krcdtDaiBreakTimePK;

	@Column(name = "STR_STAMP_TIME")
	public Integer startStampTime;

	@Column(name = "STR_STAMP_ROUDING_TIME_DAY")
	public Integer startStampRoundingTimeDay;

	@Column(name = "STR_STAMP_PLACE_CODE")
	public String startStampPlaceCode;

	@Column(name = "STR_STAMP_SOURCE_INFO")
	public Integer startStampSourceInfo;

	@Column(name = "END_STAMP_TIME")
	public Integer endStampTime;

	@Column(name = "END_STAMP_ROUDING_TIME_DAY")
	public Integer endStampRoundingTimeDay;

	@Column(name = "END_STAMP_PLACE_CODE")
	public String endStampPlaceCode;

	@Column(name = "END_STAMP_SOURCE_INFO")
	public Integer endStampSourceInfo;

	@Override
	protected Object getKey() {
		return this.krcdtDaiBreakTimePK;
	}

	public static List<KrcdtDaiBreakTime> toEntity(BreakTimeOfDailyPerformance breakTime) {
		return breakTime.getBreakTimeSheets().stream().map(c -> new KrcdtDaiBreakTime(
				new KrcdtDaiBreakTimePK(breakTime.getEmployeeId(), breakTime.getYmd(), breakTime.getBreakType().value, c.getBreakFrameNo().v()),
					c.getStartTime() == null || c.getStartTime().getTimeWithDay() == null ? null : c.getStartTime().getTimeWithDay().valueAsMinutes(),
					c.getStartTime() == null || c.getStartTime().getAfterRoundingTime() == null ? null : c.getStartTime().getAfterRoundingTime().valueAsMinutes(), 
					c.getStartTime() == null || c.getStartTime().getLocationCode() == null ? null : c.getStartTime().getLocationCode().v(),
					c.getStartTime() == null || c.getStartTime().getStampSourceInfo() == null ? null : c.getStartTime().getStampSourceInfo().value, 
					c.getEndTime() == null || c.getEndTime().getTimeWithDay() == null ? null : c.getEndTime().getTimeWithDay().valueAsMinutes(),
					c.getEndTime() == null || c.getEndTime().getAfterRoundingTime() == null ? null : c.getEndTime().getAfterRoundingTime().valueAsMinutes(), 
					c.getEndTime() == null || c.getEndTime().getLocationCode() == null ? null : c.getEndTime().getLocationCode().v(),
					c.getEndTime() == null || c.getEndTime().getLocationCode() == null ? null : c.getEndTime().getStampSourceInfo().value)).collect(Collectors.toList());
	}
}
