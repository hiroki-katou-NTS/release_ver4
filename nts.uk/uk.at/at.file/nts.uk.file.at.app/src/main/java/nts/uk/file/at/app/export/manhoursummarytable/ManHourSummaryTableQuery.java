package nts.uk.file.at.app.export.manhoursummarytable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.workrecord.workmanagement.manhoursummarytable.ManHourSummaryTableFormat;
import nts.uk.ctx.at.record.dom.workrecord.workmanagement.manhoursummarytable.ManHourSummaryTableOutputContent;
import nts.uk.screen.at.app.kha003.b.ManHourPeriod;

@AllArgsConstructor
@Getter
public class ManHourSummaryTableQuery {
    /** 工数集計表フォーマット */
    private ManHourSummaryTableFormat summaryTableFormat;

    /** 工数集計表出力内容 */
    private ManHourSummaryTableOutputContent outputContent;

    private ManHourPeriod period;
}
