package nts.uk.ctx.at.function.dom.processexecution.updateprocessexecsetting.changepersionlist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.updateprocessreexeccondition.refinementprocess.RefinementProcess;

/**
 * 異動者・勤務種別変更者リスト作成処理
 * 
 * @author tutk
 *
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class ChangePersionList {

	@Inject
	private RefinementProcess refinementProcess;

	public ListLeaderOrNotEmp createProcessForChangePerOrWorktype(String companyId, List<String> empIds,
			GeneralDate startDate, ProcessExecution procExec) {
		// 期間を計算
		// GeneralDate p = this.calculatePeriod(closureId, period, companyId);
		List<String> newEmpIdList = new ArrayList<>();
		// ・社員ID（異動者、勤務種別変更者のみ）（List）
		Set<String> setEmpIds = new HashSet<String>();
		// ・社員ID（異動者、勤務種別変更者のみ）（List）
		List<String> noLeaderEmpIdList = empIds;
		refinementProcess.refinementProcess(companyId, empIds, setEmpIds, newEmpIdList, startDate, procExec);
		setEmpIds.addAll(newEmpIdList);
		noLeaderEmpIdList.removeAll(new ArrayList<>(newEmpIdList));
		return new ListLeaderOrNotEmp(new ArrayList<>(setEmpIds), noLeaderEmpIdList);
	}

}
