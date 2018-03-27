package nts.uk.ctx.at.record.app.command.workrecord.log;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContentRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutedMenu;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AddEmpCalSumAndTargetCommandAssembler {

	@Inject
	CaseSpecExeContentRepository caseSpecExeContentRepository;

	public EmpCalAndSumExeLog fromDTO(AddEmpCalSumAndTargetCommand command) {
		// ログインしている社員の社員IDを取得する (Lấy login EmployeeID)
		String employeeID = AppContexts.user().employeeId();
		// 実行ボタン押下時のシステム日付を取得する (lấy thời gian hệ thống)
		GeneralDate systemTime = GeneralDate.today();
		int yearMonth = systemTime.yearMonth().v();
		String empCalAndSumExecLogID = IdentifierUtil.randomUniqueId();

		EmpCalAndSumExeLog empCalAndSumExeLog = EmpCalAndSumExeLog.createFromJavaType(
				// empCalAndSumExecLogID
				empCalAndSumExecLogID,
				// companyID
				AppContexts.user().companyId(),
				// processingMonth
				new YearMonth(yearMonth),
				// executedMenu
				ExecutedMenu.SELECT_AND_RUN.value,
				// executionDate
				systemTime,
				// executionStatus
				ExeStateOfCalAndSum.PROCESSING.value,
				// employeeID
				employeeID,
				// closureID
				command.getClosureID(),
				// caseSpecExeContentID
				command.getCaseSpecExeContentID());
		
		if (command.getScreen().equals("J"))
			empCalAndSumExeLog.setExecutedMenu(ExecutedMenu.EXECUTION_BY_CASE);
		
		return empCalAndSumExeLog;

	}

}
