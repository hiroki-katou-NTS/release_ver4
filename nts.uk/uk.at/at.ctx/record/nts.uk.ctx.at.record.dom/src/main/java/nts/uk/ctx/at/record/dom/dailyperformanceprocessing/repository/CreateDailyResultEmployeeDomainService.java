package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author nampt 作成処理 日別作成Mgrクラス . アルゴリズム . 社員の日別実績を作成する. ⑤社員の日別実績を作成する
 *
 */
public interface CreateDailyResultEmployeeDomainService {

	ProcessState createDailyResultEmployee(AsyncCommandHandlerContext asyncContext, String employeeId,
			DatePeriod periodTimes, String companyId, String empCalAndSumExecLogID, Optional<ExecutionLog> executionLog,
			boolean reCreateWorkType);

}
