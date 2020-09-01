package nts.uk.ctx.at.schedule.dom.shift.workcycle.domainservice;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.shift.weeklywrkday.WeeklyWorkDayPattern;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHoliday;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.WorkCycle;
import nts.uk.ctx.at.schedule.dom.shift.workcycle.WorkCycleInfo;
import nts.uk.ctx.at.shared.dom.WorkInformation;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  勤務サイクルの適用イメージを作成する
 */
@Stateless
public class CreateWorkCycleAppImage {

    /**
     * [1] 作成する
     * @param require
     * @param period
     * @param config
     * @return $反映イメージ.年月日順序のリストを返す()
     */
    public static List<RefImageEachDay> create(Require require, DatePeriod period, WorkCycleRefSetting config) {
        ReflectionImage reflectionImage = ReflectionImage.create();
        config.getRefOrder().forEach(i -> {
            switch (i) {
                case WEEKLY_WORK:
                    createImageWeekly(require, reflectionImage, period, config);
                    break;
                case PUB_HOLIDAY:
                    createImageHoliday(require, reflectionImage, period, config);
                    break;
                case WORK_CYCLE:
                    createImageInWorkCycle(require, reflectionImage, period, config);
                    break;
            }
        });
        return reflectionImage.getListRefOrdByDate();
    }

    /**
     * [prv-1] 週間勤務でイメージを作成する
     * @param require
     * @param reflectionImage
     * @param createPeriod
     * @param config
     */
    private static void createImageWeekly(Require require, ReflectionImage reflectionImage, DatePeriod createPeriod, WorkCycleRefSetting config) {
        val weeklyWorkSet = require.getWeeklyWorkSetting();
        createPeriod.stream().forEach(i -> {
            val workdayDivision = weeklyWorkSet.get().getWorkingDayCtgOfTagertDay(i);
            if (workdayDivision == null) return;
            switch (workdayDivision){
                case WORKINGDAYS:
                    return;
                case NON_WORKINGDAY_INLAW:
                    reflectionImage.addByWeeklyWorking(i, new WorkInformation("", config.getLegalHolidayCd().get().v()));
                    break;
                case NON_WORKINGDAY_EXTRALEGAL:
                    reflectionImage.addByWeeklyWorking(i, new WorkInformation("", config.getNonStatutoryHolidayCd().get().v()));
                    break;
            }
        });
    }

    /**
     * [prv-2] 祝日でイメージを作成する
     * @param require
     * @param reflectionImage
     * @param createPeriod
     * @param config
     */
    private static void createImageHoliday(Require require, ReflectionImage reflectionImage, DatePeriod createPeriod, WorkCycleRefSetting config) {
        val holidayList = require.getpHolidayWhileDate(createPeriod.start(), createPeriod.end());
        for (PublicHoliday pubHoliday : holidayList) {
			reflectionImage.addHolidays(pubHoliday.getDate(), new WorkInformation("", config.getHolidayCd().isPresent()?config.getHolidayCd().get().v(): null));
        }
    }

    /**
     * [prv-3] 勤務サイクルでイメージを作成する
     * @param require
     * @param reflectionImage
     * @param createPeriod
     * @param config
     */
    private static void createImageInWorkCycle(Require require, ReflectionImage reflectionImage, DatePeriod createPeriod, WorkCycleRefSetting config) {
        Optional<WorkCycle> workCycle = require.getWorkCycle(config.getWorkCycleCode().v());
        AtomicInteger position = new AtomicInteger(1);
        createPeriod.stream().forEach( i -> {
            WorkCycleInfo workCycleInfo = workCycle.get().getWorkInfo(position.intValue(), config.getNumOfSlideDays());
            if (reflectionImage.addInWorkCycle(i, workCycleInfo.getWorkInformation())) {
                position.addAndGet(1);
            }
        });
    }

    public interface Require {

        Optional<WeeklyWorkDayPattern> getWeeklyWorkSetting();

        List<PublicHoliday> getpHolidayWhileDate(GeneralDate strDate, GeneralDate endDate);

        Optional<WorkCycle> getWorkCycle(String code);

    }

}
