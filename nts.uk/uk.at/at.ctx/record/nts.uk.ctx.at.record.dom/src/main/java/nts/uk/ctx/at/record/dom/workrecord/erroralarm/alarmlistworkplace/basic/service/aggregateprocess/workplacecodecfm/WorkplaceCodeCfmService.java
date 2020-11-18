package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.basic.service.aggregateprocess.workplacecodecfm;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.adapter.workplace.EmployeeInfoImported;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.basic.BasicCheckName;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.extractresult.ExtractResultDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.勤務実績.勤務実績.勤務実績のエラーアラーム設定.アラームリスト（職場）.マスタチェック(基本).アルゴリズム.マスタチェック(基本)の集計処理.職場コード確認
 *
 * @author Le Huu Dat
 */
@Stateless
public class WorkplaceCodeCfmService {

    /**
     * 職場コード確認
     *
     * @param cid            会社ID
     * @param name           アラーム項目名
     * @param displayMessage 表示するメッセージ.
     * @param empInfoMap     Map＜職場ID、List＜社員情報＞＞
     * @param period         期間
     * @return List＜抽出結果＞
     */
    public List<ExtractResultDto> confirm(String cid, BasicCheckName name, DisplayMessage displayMessage,
                                          Map<String, List<EmployeeInfoImported>> empInfoMap, DatePeriod period) {
        // 空欄のリスト「抽出結果」を作成する。
        List<ExtractResultDto> results = new ArrayList<>();

        // 期間から職位情報を取得
        // TODO Q&A 36422

        for (Map.Entry<String, List<EmployeeInfoImported>> empInfo : empInfoMap.entrySet()) {
            List<EmployeeInfoImported> empInfos = empInfo.getValue();
            List<String> employeeIds = empInfos.stream().map(EmployeeInfoImported::getSid)
                    .collect(Collectors.toList());
            // 期間とList<社員ID＞から職位を取得する。
            // TODO Q&A 36423
        }

        // リスト「抽出結果」を返す。
        return results;
    }


}
