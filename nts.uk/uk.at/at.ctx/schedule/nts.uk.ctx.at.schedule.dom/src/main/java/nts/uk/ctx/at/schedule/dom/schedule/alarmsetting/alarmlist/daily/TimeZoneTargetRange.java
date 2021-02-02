package nts.uk.ctx.at.schedule.dom.schedule.alarmsetting.alarmlist.daily;

import lombok.AllArgsConstructor;

/**
 * 時間帯チェック対象の範囲
 */
@AllArgsConstructor
public enum TimeZoneTargetRange {
    // 選択
    CHOICE (0),
    // 選択以外
    OTHER(1);

    public final int value;
}
