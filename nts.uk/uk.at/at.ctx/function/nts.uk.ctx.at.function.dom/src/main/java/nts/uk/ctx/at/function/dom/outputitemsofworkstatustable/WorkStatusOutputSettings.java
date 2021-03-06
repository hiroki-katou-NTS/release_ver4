package nts.uk.ctx.at.function.dom.outputitemsofworkstatustable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;


import java.util.List;

/**
 * AggregateRoot: 勤務状況の出力設定
 * @author : chinh.hm
 */
@AllArgsConstructor
@Getter
public class WorkStatusOutputSettings extends AggregateRoot {

    // 	設定ID 	(GUID)
    private final String settingId;

    //  設定表示コード ->(出力項目設定コード)
    private OutputItemSettingCode settingDisplayCode;

    // 	設定名称 ->(出力項目設定名称)
    private OutputItemSettingName settingName;

    // 	社員ID
    private String employeeId;

    // 	定型自由区分
    private SettingClassificationCommon standardFreeDivision;

    // 	出力項目リスト
    private List<OutputItem> outputItem;

    //  [C-0] 勤怠状況の出力設定を作成する

    // 	[1]　定型選択の重複をチェックする
    public static boolean  checkDuplicateStandardSelections(Require require,String outputItemSettingCode){
        return require.checkTheStandard(outputItemSettingCode);
    }

    // [2]　自由設定の重複をチェックする
    public static boolean checkDuplicateFreeSettings(Require require,String outputItemSettingCode,String employeeId){
        return require.checkFreedom(outputItemSettingCode,employeeId);
    }

    public interface Require{
        // 	[R-1]　定型をチェックする-> 勤務状況表の出力項目Repository.	exist(コード、ログイン会社ID)
        boolean checkTheStandard(String code);
        // 	[R-2]  自由をチェックする->	勤務状況表の出力項目Repository.	exist(コード、ログイン会社ID、ログイン社員ID)
        boolean checkFreedom(String code,String employeeId);

    }

}
