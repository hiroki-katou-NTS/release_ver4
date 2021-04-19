package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.adapter.workplace.EmployeeInfoImported;
import nts.uk.ctx.at.record.dom.adapter.workschedule.budgetcontrol.budgetperformance.ExBudgetDailyImport;
import nts.uk.ctx.at.record.dom.adapter.workschedule.budgetcontrol.budgetperformance.ExtBudgetType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.FixedExtractionDayCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.FixedExtractionDayItems;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.leave.LeaveCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.newemployee.NewEmployeeCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.personeligibleannualleave.PersonEligibleAnnualLeaveCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.personnoteligible.PersonNotEligibleCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.planunregister.PlanUnregisterCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.retiree.RetireeCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.stampafterretirement.StampAfterRetirementCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.stampbeforejoin.StampBeforeJoinCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.service.check.stampunregister.StampUnregisterCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.extractresult.AlarmListExtractionInfoWorkplaceDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.extractresult.ExtractResultDto;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.shared.dom.adapter.employee.PersonEmpBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.temporaryabsence.TempAbsenceImport;
import nts.uk.ctx.at.shared.dom.scherec.alarm.alarmlistactractionresult.MessageDisplay;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.勤務実績.勤務実績.勤務実績のエラーアラーム設定.アラームリスト（職場）.マスタチェック（日別）.アルゴリズム.マスタチェック(日別)の集計処理.チェック処理
 *
 * @author Le Huu Dat
 */
@Stateless
public class DailyCheckService {

    @Inject
    private NewEmployeeCheckService newEmployeeCheckService;
    @Inject
    private LeaveCheckService leaveCheckService;
    @Inject
    private RetireeCheckService retireeCheckService;
    @Inject
    private PersonEligibleAnnualLeaveCheckService personEligibleAnnualLeaveCheckService;
    @Inject
    private PersonNotEligibleCheckService personNotEligibleCheckService;
    @Inject
    private PlanUnregisterCheckService planUnregisterCheckService;
    @Inject
    private StampUnregisterCheckService stampUnregisterCheckService;
    @Inject
    private StampBeforeJoinCheckService stampBeforeJoinCheckService;
    @Inject
    private StampAfterRetirementCheckService stampAfterRetirementCheckService;

    /**
     * チェック処理
     *
     * @param cid                         会社ID
     * @param empInfosByWpMap             Map＜職場ID、List＜社員情報＞＞
     * @param personInfos                 List＜個人社員基本情報＞
     * @param tempAbsence                 List<休職休業履歴，休職休業履歴項目の名称>
     * @param unregistedStampCardsByWpMap Map＜職場ID、List＜打刻日、未登録打刻カード＞＞
     * @param exBudgetDailies             List＜日次の外部予算実績＞
     * @param period                      期間
     * @param fixedExtractDayCons         List＜アラームリスト（職場）日別の固定抽出条件＞
     * @param fixedCheckDayItems          List＜アラームリスト（職場）日別の固定抽出項目＞
     * @param stampsByEmpMap              Map＜社員ID、List＜打刻＞＞
     * @return List＜アラームリスト抽出情報（職場）＞
     */
    public List<AlarmListExtractionInfoWorkplaceDto> process(String cid,
                                                             Map<String, List<EmployeeInfoImported>> empInfosByWpMap,
                                                             List<PersonEmpBasicInfoImport> personInfos,
                                                             TempAbsenceImport tempAbsence,
                                                             Map<String, List<Object>> unregistedStampCardsByWpMap,
                                                             List<ExBudgetDailyImport> exBudgetDailies,
                                                             DatePeriod period,
                                                             List<FixedExtractionDayCon> fixedExtractDayCons,
                                                             List<FixedExtractionDayItems> fixedCheckDayItems,
                                                             Map<String, List<Stamp>> stampsByEmpMap) {
        List<AlarmListExtractionInfoWorkplaceDto> alarmListResults = new ArrayList<>();

        // Input．List＜アラームリスト（職場）日別の固定抽出条件＞をループする
        for (FixedExtractionDayCon fixedExtractDayCon : fixedExtractDayCons) {
            List<ExtractResultDto> extractResults = new ArrayList<>();
            Optional<FixedExtractionDayItems> itemOpt = fixedCheckDayItems.stream()
                    .filter(x -> x.getFixedCheckDayItems().equals(fixedExtractDayCon.getFixedCheckDayItems())).findFirst();
            if (!itemOpt.isPresent()) continue;
            FixedExtractionDayItems item = itemOpt.get();

            // Input．Map＜職場ID、List＜社員情報＞＞をループする
            for (Map.Entry<String, List<EmployeeInfoImported>> empInfosByWp : empInfosByWpMap.entrySet()) {
                List<ExtractResultDto> results = new ArrayList<>();
                // Noをチェックする
                switch (item.getFixedCheckDayItems()) {
                    case NEW_EMPLOYEE:
                        // 1.入社者をチェック
                        results = newEmployeeCheckService.check(personInfos, period);
                        break;
                    case LEAVE:
                        // 2.休職・休業者をチェック
                        results = leaveCheckService.check(tempAbsence, period, empInfosByWp.getValue());
                        break;
                    case RETIREE:
                        // 3.退職者をチェック
                        results = retireeCheckService.check(personInfos, period);
                        break;
                    case PERSONS_ELIGIBLE_ANNUAL_LEAVE:
                        // 4.年休付与対象者をチェック
                        results = personEligibleAnnualLeaveCheckService.check(cid,
                                getPersonInfosByWp(personInfos, empInfosByWp.getValue()), period);
                        break;
                    case PERSONS_NOT_ELIGIBLE:
                        // 5.年休未付与者（本年=0の人）をチェック
                        results = personNotEligibleCheckService.check(cid,
                                getPersonInfosByWp(personInfos, empInfosByWp.getValue()), period);
                        break;
                    case PLAN_NOT_REGISTERED_PEOPLE:
                        // 6.計画データ未登録をチェック
                        results = planUnregisterCheckService.check(period, exBudgetDailies, ExtBudgetType.NUM_PERSON);
                        break;
                    case PLAN_NOT_REGISTERED_TIME:
                        // 6.計画データ未登録をチェック
                        results = planUnregisterCheckService.check(period, exBudgetDailies, ExtBudgetType.TIME);
                        break;
                    case PLAN_NOT_REGISTERED_AMOUNT:
                        // 6.計画データ未登録をチェック
                        results = planUnregisterCheckService.check(period, exBudgetDailies, ExtBudgetType.MONEY);
                        break;
                    case CARD_UNREGISTERD_STAMP:
                        for (Map.Entry<String, List<Object>> unregistedStampCardsByWp : unregistedStampCardsByWpMap.entrySet()) {
                            // 9.カード未登録打刻をチェック
                            results.addAll(stampUnregisterCheckService.check(unregistedStampCardsByWp.getValue()));
                        }
                        break;
                    case STAMPING_BEFORE_JOINING:
                        // 10.入社日よりも前の打刻をチェック
                        results = stampBeforeJoinCheckService.check(getPersonInfosByWp(personInfos, empInfosByWp.getValue()),
                                stampsByEmpMap, period);
                        break;
                    case STAMPING_AFTER_RETIREMENT:
                        // 11.退職日よりも後の打刻をチェック
                        results = stampAfterRetirementCheckService.check(getPersonInfosByWp(personInfos, empInfosByWp.getValue()),
                                stampsByEmpMap, period);
                        break;
                }

                // 取得したList＜抽出結果＞を総合List＜抽出結果＞に追加
                results.forEach(x -> {
                    x.setAlarmItemName(item.getDailyCheckName());
                    x.setComment(Optional.of(new MessageDisplay(fixedExtractDayCon.getMessageDisp().v())));
                    x.setWorkplaceId(empInfosByWp.getKey());
                });
                extractResults.addAll(results);
            }

            // 「アラームリスト抽出情報（職場）」を作成してList＜アラームリスト抽出情報（職場）＞に追加
            List<AlarmListExtractionInfoWorkplaceDto> results = extractResults.stream().map(x ->
                    new AlarmListExtractionInfoWorkplaceDto(fixedExtractDayCon.getErrorAlarmWorkplaceId(), 2, x))
                    .collect(Collectors.toList());
            alarmListResults.addAll(results);
        }

        // List＜アラーム抽出結果（職場別）＞を返す
        return alarmListResults;
    }

    private List<PersonEmpBasicInfoImport> getPersonInfosByWp(List<PersonEmpBasicInfoImport> personInfos,
                                                              List<EmployeeInfoImported> empInfos) {
        return personInfos.stream().filter(x -> empInfos.stream().anyMatch(c -> c.getSid().equals(x.getEmployeeId())))
                .collect(Collectors.toList());
    }
}
