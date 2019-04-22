package nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.at.function.dom.alarm.checkcondition.multimonth.doevent.MulMonCheckCondDomainEventDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface CheckActualResultMulMonth {
	boolean checkMulMonthCheckCond(DatePeriod period, String companyId, String string,
			List<MonthlyRecordValueImport> result, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto);
	
	// fix tạm lấy số lần 
	Double sumMulMonthCheckCond(DatePeriod period, String companyId, String string,
			List<MonthlyRecordValueImport> result, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto);

	boolean checkMulMonthCheckCondContinue(DatePeriod period, String companyId, String string,
			List<MonthlyRecordValueImport> result, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto);

	ArrayList<Integer> checkMulMonthCheckCondCosp(DatePeriod period, String companyId, String string,
			List<MonthlyRecordValueImport> result, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto);

	ResultCheckMulMonthCheckCondAverage checkMulMonthCheckCondAverage(DatePeriod period, String companyId, String string,
			List<MonthlyRecordValueImport> result, MulMonCheckCondDomainEventDto erAlAtdItemConAdapterDto);
}
