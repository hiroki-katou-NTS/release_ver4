package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.List;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author nampt
 * 日別実績処理
 * 作成処理
 * アルゴリズム : ③日別実績の作成処理
 *
 */
public interface CreateDailyResultDomainService {

	ProcessState createDailyResult(AsyncCommandHandlerContext asyncContext, List<String> emloyeeIds, DatePeriod periodTime, ExecutionAttr executionAttr, String companyId, String empCalAndSumExecLogID);
	
}
