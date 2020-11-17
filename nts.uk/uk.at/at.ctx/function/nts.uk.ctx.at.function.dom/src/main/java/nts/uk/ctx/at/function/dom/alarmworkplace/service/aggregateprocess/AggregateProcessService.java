package nts.uk.ctx.at.function.dom.alarmworkplace.service.aggregateprocess;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.alarmlistworkplace.AggregateProcessAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternCode;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlace;
import nts.uk.ctx.at.function.dom.alarmworkplace.AlarmPatternSettingWorkPlaceRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.CheckCondition;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.AlarmCheckCdtWkpCtgRepository;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.AlarmCheckCdtWorkplaceCategory;
import nts.uk.ctx.at.function.dom.alarmworkplace.checkcondition.WorkplaceCategory;
// import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.basic.service.aggregateprocess.AggregateProcessMasterCheckBasicService;

import javax.ejb.Stateless;
import javax.inject.Inject;
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


    public void process(String cid, String alarmPatternCode, List<String> workplaceIds) {
        // パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定(職場別)」を取得する
        Optional<AlarmPatternSettingWorkPlace> patternOpt = alarmPatternSettingWorkPlaceRepo.getBy(cid, new AlarmPatternCode(alarmPatternCode));
        if (!patternOpt.isPresent()) {
            throw new RuntimeException("「アラームリストパターン設定(職場別) 」が見つかりません！");
        }

        // 取得した「アラームリストパターン設定(職場別)」．カテゴリ別チェック条件をループする
        List<CheckCondition> checkConList = patternOpt.get().getCheckConList();
        for (CheckCondition checkCdt : checkConList) {
            WorkplaceCategory category = checkCdt.getWorkplaceCategory();
            // ドメインモデル「カテゴリ別アラームチェック条件(職場別)」を取得する。
            List<AlarmCheckCdtWorkplaceCategory> conditionCtgs = alarmCheckCdtWkpCtgRepo.getBy(category,
                    checkCdt.getCheckConditionLis());
            DatePeriod period = new DatePeriod(GeneralDate.today(), GeneralDate.today());
            // ループ中のカテゴリをチェック
            switch (category) {
                case MASTER_CHECK_BASIC:
                    // アルゴリズム「マスタチェック(基本)の集計処理」を実行する
                    aggregateProcessAdapter.processMasterCheckBasic(cid, period, Collections.emptyList(), workplaceIds);
                    break;
                case MASTER_CHECK_DAILY:
                    // アルゴリズム「マスタチェック(日別)の集計処理」を実行する
                    break;
                case MASTER_CHECK_WORKPLACE:
                    // アルゴリズム「マスタチェック(職場)の集計処理」を実行する
                    break;
                case SCHEDULE_DAILY:
                    // アルゴリズム「スケジュール／日次の集計処理」を実行する
                    break;
                case MONTHLY:
                    // アルゴリズム「月次の集計処理」を実行する
                    break;
                case APPLICATION_APPROVAL:
                    // アルゴリズム「申請承認の集計処理」を実行する
                    break;
            }

            // List＜アラーム抽出結果（職場別）＞に返す値を追加
        }
    }
}
