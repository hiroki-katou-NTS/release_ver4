package nts.uk.ctx.at.function.app.command.modifyanyperiod;

import lombok.Data;

@Data
public class ModifyAnyPeriodDetailDto {
    /**
     * 日次表示項目一覧
     */
    private int attendanceItemId;

    /**
     * 並び順
     */
    private int order;
}
