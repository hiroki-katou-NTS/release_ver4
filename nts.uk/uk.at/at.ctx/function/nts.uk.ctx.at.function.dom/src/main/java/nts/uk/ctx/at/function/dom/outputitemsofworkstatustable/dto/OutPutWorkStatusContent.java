package nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutPutWorkStatusContent {
    // 日付
    private List<ExportExcelDto> excelDtoList;

    private DatePeriod period;
    private ClosureDate closureDate;
    private int mode;
    private String title;
    private String companyName;
    private boolean pageBreak;
    //「６」ゼロ表示区分
    private boolean isZeroDisplay;
}
