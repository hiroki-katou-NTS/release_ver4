package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.checkprocessed;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.organization.EmploymentHistoryImported;

/**
 * 処理すべきかをチェックする
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.日別実績作成処理.作成処理.日別作成Mgrクラス.アルゴリズム.社員の日別実績を作成する.処理すべきかをチェックする
 * 
 * @author tutk
 *
 */
@Stateless
public class CheckProcessed {

    public OutputCheckProcessed getCheckProcessed(GeneralDate date,
            List<EmploymentHistoryImported> listEmploymentHisOptional) {

        if (listEmploymentHisOptional.isEmpty()) {
            return new OutputCheckProcessed(StatusOutput.NEXT_EMPLOYEE, null);
        }
        if (listEmploymentHisOptional.size() == 1) {
            if (date.after(listEmploymentHisOptional.get(0).getPeriod().end())) {
                return new OutputCheckProcessed(StatusOutput.NEXT_EMPLOYEE, null);

                // 履歴。期間。開始日<=処理日<=履歴。期間。終了日
            } else if (date.afterOrEquals(listEmploymentHisOptional.get(0).getPeriod().start())
                    && date.beforeOrEquals(listEmploymentHisOptional.get(0).getPeriod().end())) {
                return new OutputCheckProcessed(StatusOutput.PROCESS, listEmploymentHisOptional.get(0));
            }
            return new OutputCheckProcessed(StatusOutput.NEXT_DAY, null);
        }

        Optional<EmploymentHistoryImported> employmentHistoryImported = listEmploymentHisOptional.stream()
                .filter(c -> date.afterOrEquals(c.getPeriod().start()) && date.beforeOrEquals(c.getPeriod().end()))
                .findFirst();
        if (employmentHistoryImported.isPresent()) {
            return new OutputCheckProcessed(StatusOutput.PROCESS, employmentHistoryImported.get());
        }
        employmentHistoryImported = listEmploymentHisOptional.stream()
                .filter(c -> date.beforeOrEquals(c.getPeriod().start())).findFirst();
        if (employmentHistoryImported.isPresent()) {
            return new OutputCheckProcessed(StatusOutput.NEXT_DAY, null);
        }
        return new OutputCheckProcessed(StatusOutput.NEXT_EMPLOYEE, null);

    }

}