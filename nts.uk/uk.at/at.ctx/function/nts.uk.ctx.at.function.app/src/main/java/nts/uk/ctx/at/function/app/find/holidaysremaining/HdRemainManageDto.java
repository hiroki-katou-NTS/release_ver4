package nts.uk.ctx.at.function.app.find.holidaysremaining;

import lombok.Value;
import nts.uk.ctx.at.function.dom.holidaysremaining.HolidaysRemainingManagement;

import java.util.List;

/**
 * 休暇残数管理表の出力項目設定
 */
@Value
public class HdRemainManageDto {

    /**
     * 出力レイアウトID
     */
    private String layoutId;

    /**
     * 項目選択区分
     */
    private int itemSelType;

    /**
     *
     */
    private String sid;

    /**
     * 会社ID
     */
    private String cid;

    /**
     * コード
     */
    private String cd;

    /**
     * 名称
     */
    private String name;

    /**
     * 年休の項目出力する
     */
    private boolean yearlyHoliday;

    /**
     * ★内半日年休を出力する
     */
    private boolean insideHalfDay;

    /**
     * 内時間年休残数を出力する
     */
    private boolean insideHours;

    /**
     * 積立年休の項目を出力する
     */
    private boolean yearlyReserved;

    /**
     * 代休の項目を出力する
     */
    private boolean outputItemSubstitute;

    /**
     * 代休未消化出力する
     */
    private boolean representSubstitute;

    /**
     * 代休残数を出力する
     */
    private boolean remainingChargeSubstitute;

    /**
     * 振休の項目を出力する
     */
    private boolean pauseItem;

    /**
     * 振休未消化を出力する
     */
    private boolean unDigestedPause;

    /**
     * 振休残数を出力する
     */
    private boolean numberRemainingPause;
    /**
     * 時間外超過項目を出力する
     */
    private boolean hd60HItem;



    /**
     * 時間外超過項目を出力する
     */
    private boolean hd60HRemain;

    /**
     * 時間外超過項目を出力する
     */
    private boolean hd60HUndigested;

    /**
     * 公休の項目を出力する
     */
    private boolean outputItemsHolidays;

    /**
     * 公休繰越数を出力する
     */
    private boolean outputHolidayForward;

    /**
     * 公休月度残を出力する
     */
    private boolean monthlyPublic;

    /**
     * 子の看護休暇の項目を出力する
     */
    private boolean childNursingLeave;

    /**
     * 介護休暇の項目を出力する
     */
    private boolean nursingLeave;

    private List<Integer> listSpecialHoliday;

    public static HdRemainManageDto fromDomain(HolidaysRemainingManagement domain) {
        return new HdRemainManageDto(

                domain.getLayOutId(),
                domain.getItemSelectionCategory().value,
                domain.getEmployeeId().isPresent()?domain.getEmployeeId().get():null,
                domain.getCompanyID(), domain.getCode().v(), domain.getName().v(),
                domain.getListItemsOutput().getAnnualHoliday().isYearlyHoliday(),
                domain.getListItemsOutput().getAnnualHoliday().isInsideHalfDay(),
                domain.getListItemsOutput().getAnnualHoliday().isInsideHours(),
                domain.getListItemsOutput().getYearlyReserved().isYearlyReserved(),
                domain.getListItemsOutput().getSubstituteHoliday().isOutputItemSubstitute(),
                domain.getListItemsOutput().getSubstituteHoliday().isRepresentSubstitute(),
                domain.getListItemsOutput().getSubstituteHoliday().isRemainingChargeSubstitute(),
                domain.getListItemsOutput().getPause().isPauseItem(),
                domain.getListItemsOutput().getPause().isUndigestedPause(),
                domain.getListItemsOutput().getPause().isNumberRemainingPause(),
                domain.getListItemsOutput().getOutOfTime().isOvertimeItem() ,
                domain.getListItemsOutput().getOutOfTime().isOvertimeRemaining(),
                domain.getListItemsOutput().getOutOfTime().isOvertimeOverUndigested(),
                domain.getListItemsOutput().getHolidays().isOutputItemsHolidays(),
                domain.getListItemsOutput().getHolidays().isOutputHolidayForward(),
                domain.getListItemsOutput().getHolidays().isMonthlyPublic(),
                domain.getListItemsOutput().getChildNursingVacation().isChildNursingLeave(),
                domain.getListItemsOutput().getNursingcareLeave().isNursingLeave(),
                domain.getListItemsOutput().getSpecialHoliday());

    }

    private HdRemainManageDto(
            String layoutId,
            int itemSelType,
            String sid,
            String cid,
            String cd,
            String name,
            boolean yearlyHoliday,
            boolean insideHalfDay,
            boolean insideHours,
            boolean yearlyReserved,
            boolean outItemSub,
            boolean representSub,
            boolean remainChargeSub,
            boolean pauseItem,
            boolean undigestedPause,
            boolean numRemainPause,
            boolean hd60HItem,
            boolean hd60HRemain,
            boolean hd60HUndigested,
            boolean outputItemsHolidays,
            boolean outputHolidayForward,
            boolean monthlyPublic,
            boolean childCareLeave,
            boolean nursingCareLeave,
            List<Integer> specHolidays) {
        super();
        this.cid = cid;
        this.cd = cd;
        this.layoutId = layoutId;
        this.itemSelType = itemSelType;
        this.sid = sid;
        this.hd60HItem = hd60HItem;
        this.hd60HUndigested = hd60HUndigested;
        this.hd60HRemain = hd60HRemain;
        this.name = name;
        this.yearlyHoliday = yearlyHoliday;
        this.insideHalfDay = insideHalfDay;
        this.insideHours = insideHours;
        this.yearlyReserved = yearlyReserved;
        this.outputItemSubstitute = outItemSub;
        this.representSubstitute = representSub;
        this.remainingChargeSubstitute = remainChargeSub;
        this.pauseItem = pauseItem;
        this.unDigestedPause = undigestedPause;
        this.numberRemainingPause = numRemainPause;
        this.outputItemsHolidays = outputItemsHolidays;
        this.outputHolidayForward = outputHolidayForward;
        this.monthlyPublic = monthlyPublic;
        this.childNursingLeave = childCareLeave;
        this.nursingLeave = nursingCareLeave;
        this.listSpecialHoliday = specHolidays;
    }

}
