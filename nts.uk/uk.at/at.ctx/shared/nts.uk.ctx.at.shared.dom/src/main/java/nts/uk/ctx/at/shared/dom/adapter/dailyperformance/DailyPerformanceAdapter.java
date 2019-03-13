package nts.uk.ctx.at.shared.dom.adapter.dailyperformance;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface DailyPerformanceAdapter {
  
  boolean checkDataApproveed(GeneralDate startDate, GeneralDate endDate, String approverID, Integer rootType, String companyID);
  
  /** 対応するImported「（就業．勤務実績）承認対象者の月別実績の承認状況」をすべて取得する*/
  List<ApproveRootStatusForEmpImport> getApprovalByListEmplAndListApprovalRecordDate(List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType);
 
  boolean isDataExist(String approverID, DatePeriod period, Integer rootType);
  
  boolean dataMonth(String approverID,DatePeriod period, YearMonth yearMonth);
  
  AppEmpStatusImport appEmpStatusExport(String employeeID, DatePeriod period, Integer rootType);
}
