package nts.uk.file.at.app.export.annualworkledger;

import lombok.*;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureDateDto;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class AnnualWorkLedgerFileQuery {
    // ExcelPdf区分: 1 - PDF, 2 - EXCEL
    private int mode;

    // A2_3 期間入力 - 開始月
    private int startMonth;

    // A2_3 期間入力 - 終了月
    private int endMonth;

    // A3_2 社員リスト
    private List<String> lstEmpIds;

    // 定型自由区分
    private int settingClassification;

    // A5_3_2 OR A5_4_2 設定ID
    private String settingId;

    // A6_2 ゼロ表示区分選択肢
    private boolean isZeroDisplay;

    // 締め日
    private int  closureId;;
}
