package nts.uk.ctx.at.record.infra.entity.workinformation;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt 日別実績の勤務情報
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAI_PER_WORK_INFO")
public class KrcdtDaiPerWorkInfo extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtDaiPerWorkInfoPK krcdtDaiPerWorkInfoPK;

	// 勤務実績の勤務情報. 勤務種類コード
	@Column(name = "RECORD_WORK_WORKTYPE_CODE")
	public String recordWorkWorktypeCode;

	// 勤務実績の勤務情報. 就業時間帯コード
	@Column(name = "RECORD_WORK_WORKTIME_CODE")
	public String recordWorkWorktimeCode;

	// 勤務予定の勤務情報. 勤務種類コード
	@Column(name = "SCHEDULE_WORK_WORKTYPE_CODE")
	public String scheduleWorkWorktypeCode;

	// 勤務予定の勤務情報. 勤務種類コード
	@Column(name = "SCHEDULE_WORK_WORKTIME_CODE")
	public String scheduleWorkWorktimeCode;

	@Column(name = "CALCULATION_STATE")
	public Integer calculationState;

	@Column(name = "GO_STRAIGHT_ATR")
	public Integer goStraightAttribute;

	@Column(name = "BACK_STRAIGHT_ATR")
	public Integer backStraightAttribute;
	
	@Column(name = "DAY_OF_WEEK")
	public Integer dayOfWeek;

	@OneToMany(mappedBy = "daiPerWorkInfo", cascade = CascadeType.ALL)
	@JoinColumn(nullable = true)
	public List<KrcdtWorkScheduleTime> scheduleTimes;

	public KrcdtDaiPerWorkInfo(KrcdtDaiPerWorkInfoPK krcdtDaiPerWorkInfoPK) {
		super();
		this.krcdtDaiPerWorkInfoPK = krcdtDaiPerWorkInfoPK;
	}
	
	@Override
	protected Object getKey() {
		return this.krcdtDaiPerWorkInfoPK;
	}

	public WorkInfoOfDailyPerformance toDomain() {
		WorkInfoOfDailyPerformance domain = new WorkInfoOfDailyPerformance(this.krcdtDaiPerWorkInfoPK.employeeId,
				new WorkInformation(this.recordWorkWorktimeCode, this.recordWorkWorktypeCode),
				new WorkInformation(this.scheduleWorkWorktimeCode, this.scheduleWorkWorktypeCode),
				EnumAdaptor.valueOf(this.calculationState, CalculationState.class),
				EnumAdaptor.valueOf(this.goStraightAttribute, NotUseAttribute.class),
				EnumAdaptor.valueOf(this.backStraightAttribute, NotUseAttribute.class), this.krcdtDaiPerWorkInfoPK.ymd,
				KrcdtWorkScheduleTime.toDomain(scheduleTimes));
		return domain;
	}

	public static KrcdtDaiPerWorkInfo toEntity(WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		return new KrcdtDaiPerWorkInfo(
				new KrcdtDaiPerWorkInfoPK(workInfoOfDailyPerformance.getEmployeeId(),
						workInfoOfDailyPerformance.getYmd()),
				workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode() !=null ? workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode().v() : null,
				workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTimeCode() != null ? workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTimeCode().v() : null,
				workInfoOfDailyPerformance.getScheduleWorkInformation().getWorkTypeCode() != null ? workInfoOfDailyPerformance.getScheduleWorkInformation().getWorkTypeCode().v() : null,
				workInfoOfDailyPerformance.getScheduleWorkInformation().getWorkTimeCode() != null ? workInfoOfDailyPerformance.getScheduleWorkInformation().getWorkTimeCode().v() : null,
				workInfoOfDailyPerformance.getCalculationState() != null ? workInfoOfDailyPerformance.getCalculationState().value : null,
				workInfoOfDailyPerformance.getGoStraightAtr() != null ? workInfoOfDailyPerformance.getGoStraightAtr().value : null,
				workInfoOfDailyPerformance.getBackStraightAtr() != null ? workInfoOfDailyPerformance.getBackStraightAtr().value : null,
				workInfoOfDailyPerformance.getDayOfWeek() != null ? workInfoOfDailyPerformance.getDayOfWeek().value : null,
				workInfoOfDailyPerformance.getScheduleTimeSheets() != null ? 
				workInfoOfDailyPerformance.getScheduleTimeSheets().stream().map(f -> KrcdtWorkScheduleTime
						.toEntity(workInfoOfDailyPerformance.getEmployeeId(), workInfoOfDailyPerformance.getYmd(), f))
						.collect(Collectors.toList()) : null);
	}

}
