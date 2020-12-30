package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.monthly.service.beforemonthlyaggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.adapter.workplace.EmployeeInfoImported;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureResultDto;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class MonthlyCheckDataDto {
    /**
     * Map＜職場ID、List＜社員情報＞＞
     */
    private Map<String, List<EmployeeInfoImported>> empInfosByWpMap;

    /**
     * List＜締め＞
     */
    private List<ClosureInfo> closures;

    /**
     * List＜月別実績の勤怠時間＞
     */
    private List<AttendanceTimeOfMonthly> attendanceTimeOfMonthlies;
}
