package nts.uk.ctx.at.request.dom.application.timeleaveapplication.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.calendar.period.DatePeriod;

import java.util.ArrayList;
import java.util.List;

/**
 * 時間休暇残数
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeLeaveRemaining {
    // 60H超休の残時間
    private Integer super60HRemainingTime = 0;

    // 介護の残日数
    private Integer careRemainingDays = 0;

    // 介護の残時間
    private Integer careRemainingTime = 0;

    // 子看護の残日数
    private Integer childCareRemainingDays = 0;

    // 子看護の残時間
    private Integer childCareRemainingTime = 0;

    // 時間代休の残時間
    private Integer subTimeLeaveRemainingTime = 0;

    // 時間年休の残日数
    private Double annualTimeLeaveRemainingDays = 0.0;

    // 時間年休の残時間
    private Integer annualTimeLeaveRemainingTime = 0;

    // 時間特別休暇残数
    private List<TimeSpecialVacationRemaining> specialTimeFrames = new ArrayList<>();

    // 残数期間
    private DatePeriod remainingPeriod;

    public static TimeVacationRemainingOutput setDataOutput(TimeLeaveRemaining dto) {

        return new TimeVacationRemainingOutput(
            dto.super60HRemainingTime,
            dto.careRemainingDays,
            dto.careRemainingTime,
            dto.childCareRemainingDays,
            dto.childCareRemainingTime,
            dto.subTimeLeaveRemainingTime,
            dto.careRemainingDays,
            dto.annualTimeLeaveRemainingTime,
            dto.specialTimeFrames,
            dto.remainingPeriod
        );
    }
}
