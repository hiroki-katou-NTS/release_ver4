package nts.uk.ctx.at.function.dom.commonform;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 基準日で社員の雇用と締め日を取得する
 */
@Stateless
public class GetClosureDateEmploymentDomainService {
    /**
     * [1] 取得する
     *
     * @param require
     * @param baseDate 基準日：年月日
     * @param listSid  List<社員ID>：社員ID
     * @return
     */
    public static List<ClosureDateEmployment> get(Require require, GeneralDate baseDate, List<String> listSid) {

        // 雇用を取得する
        val lstEmploymentInfo = require.getEmploymentInfor(listSid, baseDate);

        return lstEmploymentInfo.values().stream().map(i -> {
            // Call 社員に対応する処理締めを取得する
            val closureOptional = require.getClosureDataByEmployee(i.getEmployeeId(), baseDate);

            // ※締め．締め変更履歴 がリストの場合は基準日がある履歴のみ残して、他の履歴を無視する
            if (closureOptional.isPresent()) {
                val closure = closureOptional.get();
                val closureHistories = new ArrayList<ClosureHistory>();
                closureHistories.add(closure.getHistoryByBaseDate(baseDate));
                closure.setClosureHistories(closureHistories);
            }

            return new ClosureDateEmployment(
                    i.getEmployeeId(),
                    i.getEmploymentCode(),
                    i.getEmploymentName(),
                    closureOptional
            );
        }).collect(Collectors.toList());
    }

    public static interface Require {
        /**
         * [R-1] 社員ID（List）と指定期間から社員の雇用履歴を取得 (社員一覧, 期間)
         * nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter.findEmpHistoryVer2(String companyId, List<String> lstSID, GeneralDate baseDate)
         *
         * @param listSid
         * @param baseDate
         * @return
         */
        Map<String, BsEmploymentHistoryImport> getEmploymentInfor(List<String> listSid, GeneralDate baseDate);

        /**
         * [R-2] 社員に対応する処理締めを取得する（社員ID、基準日
         * nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService.getClosureDataByEmployee(RequireM3 require, CacheCarrier cacheCarrier, String employeeId, GeneralDate baseDate)
         *
         * @param employeeId
         * @param baseDate
         * @return
         */
        Optional<Closure> getClosureDataByEmployee(String employeeId, GeneralDate baseDate);
    }
}
