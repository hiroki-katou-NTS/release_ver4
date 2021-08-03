package nts.uk.ctx.at.function.app.find.alarm.mailsettings;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * アラームリスト実行メール設定設定済みか
 * */
@Data
@AllArgsConstructor
public class MailSettingDto {
    //設定済み
    private boolean isPreConfigured = false;
}
