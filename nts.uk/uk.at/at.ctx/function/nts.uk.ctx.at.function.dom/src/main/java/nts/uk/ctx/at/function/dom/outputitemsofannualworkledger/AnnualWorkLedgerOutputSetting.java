package nts.uk.ctx.at.function.dom.outputitemsofannualworkledger;

import lombok.AllArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.function.dom.attendancerecord.export.setting.ExportSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;

/**
 * AggregateRoot: 年間勤務台帳の出力設定
 * @author chinh.hm
 */
@AllArgsConstructor
public class AnnualWorkLedgerOutputSetting extends AggregateRoot{
    //ID -> GUID
    private String id;

    //コード
    private ExportSettingCode code;

    // 名称
    private OutputItemSettingName name;

    // 定型自由区分
    private SettingClassificationCommon standardFreeDivision;

    // 日次出力項目リスト
    private DailyOutputItemsAnnualWorkLedger dailyOutputItemList;

    // 出力項目
    private OutputItem outputItemList;

    //社員ID
    private String employeeId;

    // 	[C-0] 年間勤務台帳の出力設定を作成する

    // [1]　定型選択の重複をチェックする
    boolean checkDuplicateStandardSelection(Require require,OutputItemSettingCode code,String cid){
        return require.checkTheStandard(code,cid);
    }
    // [2]　自由設定の重複をチェックする
    boolean checkDuplicateFreeSettings(Require require,OutputItemSettingCode code,String cid,String employeeId){
        return require.checkFreedom(code,cid,employeeId);
    }

    public interface Require{
        // 	[R-1]　定型をチェックする
        // 	年間勤務台帳の出力項目Repository. exist(コード、ログイン会社ID)
        boolean checkTheStandard(OutputItemSettingCode code,String cid);
        //	[R-2]  自由をチェックする
        //  年間勤務台帳の出力項目Repository. exist(コード、ログイン会社ID、ログイン社員ID)
        boolean checkFreedom(OutputItemSettingCode code, String cid, String employeeId);
    }
}
