package nts.uk.screen.at.app.dailyperformance.correction.loadupdate.onlycheckbox;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalConfirmCache;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpAndDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadVerData {
   List<EmpAndDate> lstDataChange;
   DateRange dateRange;
   List<DailyRecordDto> lstDomainOld;
   int  displayFormat;
   ApprovalConfirmCache approvalConfirmCache;
}
