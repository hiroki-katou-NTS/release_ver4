package nts.uk.ctx.at.function.dom.alarmworkplace.service.aggregateprocess;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkPlaceInforExport;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.alarmlistworkplace.AggregateProcessAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternCode;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlace;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlaceRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.CheckCondition;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.AlarmCheckCdtWkpCtgRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.AlarmCheckCdtWorkplaceCategory;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.WorkplaceCategory;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.applicationapproval.AlarmAppApprovalCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.basic.AlarmMasterBasicCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.daily.AlarmMasterDailyCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.monthly.AlarmMonthlyCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.schedule.AlarmScheduleCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.workplace.AlarmMasterWkpCheckCdt;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractresult.AlarmListExtractInfoWorkplace;
import nts.uk.ctx.at.function.dom.alarmworkplace.extractresult.AlarmListExtractInfoWorkplaceRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.就業機能.アラーム_職場別.アルゴリズム.集計処理.集計処理
 * 集計処理
 */
@Stateless
public class AggregateProcessService {

    @Inject
    private AlarmPatternSettingWorkPlaceRepository alarmPatternSettingWorkPlaceRepo;
    @Inject
    private AlarmCheckCdtWkpCtgRepository alarmCheckCdtWkpCtgRepo;
    @Inject
    private AggregateProcessAdapter aggregateProcessAdapter;
    @Inject
    private AlarmListExtractInfoWorkplaceRepository alarmListExtractInfoWorkplaceRepo;
    @Inject
    private WorkplaceAdapter workplaceAdapter;

    /**
     * 集計処理
     *
     * @param cid              会社ID
     * @param alarmPatternCode パターンコード
     * @param workplaceIds     List<職場ID>
     * @param periods          List<カテゴリ別期間>
     */
    public void process(String cid, String alarmPatternCode, List<String> workplaceIds, List<PeriodByAlarmCategory> periods) {
        // パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定(職場別)」を取得する
        Optional<AlarmPatternSettingWorkPlace> patternOpt = alarmPatternSettingWorkPlaceRepo.getBy(cid, new AlarmPatternCode(alarmPatternCode));
        if (!patternOpt.isPresent()) {
            throw new RuntimeException("「アラームリストパターン設定(職場別) 」が見つかりません！");
        }

        List<AlarmListExtractInfoWorkplace> alExtractInfos = new ArrayList<>();

        // 取得した「アラームリストパターン設定(職場別)」．カテゴリ別チェック条件をループする
        List<CheckCondition> checkConList = patternOpt.get().getCheckConList();
        for (CheckCondition checkCdt : checkConList) {
            WorkplaceCategory category = checkCdt.getWorkplaceCategory();
            // ドメインモデル「カテゴリ別アラームチェック条件(職場別)」を取得する。
            Optional<AlarmCheckCdtWorkplaceCategory> conditionCtgOpt = alarmCheckCdtWkpCtgRepo.getBy(category,
                    checkCdt.getCheckConditionLis());

            if (!conditionCtgOpt.isPresent()) continue;
            AlarmCheckCdtWorkplaceCategory conditionCtg = conditionCtgOpt.get();

            // Dùng hàm trong inteface
            List<String> alarmCheckWkpId = conditionCtg.getCondition().getAlarmCheckWkpID();
            List<String> optionalIds = conditionCtg.getCondition().getListOptionalIDs();

            // ループ中のカテゴリをチェック
            switch (category) {
                case MASTER_CHECK_BASIC:
                    // アルゴリズム「マスタチェック(基本)の集計処理」を実行する
                    alExtractInfos.addAll(aggregateProcessAdapter.processMasterCheckBasic(cid, getDatePeriod(category, periods),
                            alarmCheckWkpId, workplaceIds));
                    break;
                case MASTER_CHECK_DAILY:
                    alExtractInfos.addAll(aggregateProcessAdapter.processMasterCheckDaily(cid, getDatePeriod(category, periods),
                            alarmCheckWkpId, workplaceIds));
                    // アルゴリズム「マスタチェック(日別)の集計処理」を実行する
                    break;
                case MASTER_CHECK_WORKPLACE:
                    // アルゴリズム「マスタチェック(職場)の集計処理」を実行する
                    alExtractInfos.addAll(aggregateProcessAdapter.processMasterCheckWorkplace(cid, getDatePeriod(category, periods),
                            alarmCheckWkpId, workplaceIds));
                    break;
                case SCHEDULE_DAILY:
                    // アルゴリズム「スケジュール／日次の集計処理」を実行する
                    alExtractInfos.addAll(aggregateProcessAdapter.processMasterCheckSchedule(cid, getDatePeriod(category, periods),
                            alarmCheckWkpId, optionalIds, workplaceIds));
                    break;
                case MONTHLY:
                    // アルゴリズム「月次の集計処理」を実行する
                    DatePeriod periodMonthly = getDatePeriod(category, periods);
                    alExtractInfos.addAll(aggregateProcessAdapter.processMasterCheckMonthly(cid, YearMonth.of(periodMonthly.start().year(), periodMonthly.start().month()),
                            alarmCheckWkpId, optionalIds, workplaceIds));
                    break;
                case APPLICATION_APPROVAL:
                    // アルゴリズム「申請承認の集計処理」を実行する
                    break;
            }

            // List＜アラーム抽出結果（職場別）＞に返す値を追加
            alarmListExtractInfoWorkplaceRepo.addAll(alExtractInfos);

            // 抽出処理停止フラグが立っているかチェックする
            // TODO
        }
    }

    private DatePeriod getDatePeriod(WorkplaceCategory category, List<PeriodByAlarmCategory> periods) {
        Optional<PeriodByAlarmCategory> periodOpt = periods.stream()
                .filter(x -> x.getCategory() == category.value).findFirst();

        if (!periodOpt.isPresent()) return null;

        PeriodByAlarmCategory period = periodOpt.get();
        return new DatePeriod(period.startDate, period.endDate);
    }

    private YearMonth getYm(WorkplaceCategory category, List<PeriodByAlarmCategory> periods) {
        Optional<PeriodByAlarmCategory> period = periods.stream()
                .filter(x -> x.getCategory() == category.value).findFirst();

        if (!period.isPresent()) return null;

        GeneralDate start = period.get().startDate;
        return YearMonth.of(start.year(), start.month());
    }
}
