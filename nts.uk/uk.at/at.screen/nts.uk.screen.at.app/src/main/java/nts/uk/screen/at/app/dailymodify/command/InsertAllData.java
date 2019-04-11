package nts.uk.screen.at.app.dailymodify.command;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommandHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.audittrail.DPAttendanceItemRC;
import nts.uk.ctx.at.record.app.command.dailyperform.month.UpdateMonthDailyParam;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class InsertAllData {

	@Inject
	private DailyRecordWorkCommandHandler hander;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void handlerInsertAllDaily(List<DailyRecordWorkCommand> commandNew, List<IntegrationOfDaily> domainDailyNew,
			List<DailyRecordWorkCommand> commandOld, List<DailyItemValue> dailyItems, boolean isUpdate, UpdateMonthDailyParam month, Map<Integer, DPAttendanceItemRC> itemAtr) {
		hander.handlerInsertAllDaily(commandNew, domainDailyNew, commandOld, dailyItems, isUpdate, month, itemAtr);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void handlerInsertAllMonth(List<IntegrationOfMonthly> lstMonthDomain, UpdateMonthDailyParam month) {
		hander.handlerInsertAllMonth(lstMonthDomain, month); 
	}
	
}
