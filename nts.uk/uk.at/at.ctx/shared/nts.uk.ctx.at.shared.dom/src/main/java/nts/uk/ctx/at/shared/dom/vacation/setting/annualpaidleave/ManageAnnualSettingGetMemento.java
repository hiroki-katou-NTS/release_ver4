/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave;

/**
 * The Interface ManageAnnualSettingGetMemento.
 */
public interface ManageAnnualSettingGetMemento {

    /**
     * Gets the company id.
     *
     * @return the company id
     */
    String getCompanyId();
    
//    /**
//     * Gets the maximum day vacation.
//     *
//     * @return the maximum day vacation
//     */
//    AnnualLeaveGrantDay getMaxGrantDay();

    /**
     * Gets the half day manage.
     *
     * @return the half day manage
     */
    HalfDayManage getHalfDayManage();

    /**
     * Gets the checks if is work day calculate.
     *
     * @return the checks if is work day calculate
     */
    boolean getIsWorkDayCalculate();

    /**
     * Gets the remaining number setting.
     *
     * @return the remaining number setting
     */
    RemainingNumberSetting getRemainingNumberSetting();

    /**
     * Gets the display setting.
     *
     * @return the display setting
     */
    DisplaySetting getDisplaySetting();

    /**
     * Gets the yearly of day.
     * @return
     */
    YearLyOfNumberDays getYearLyOfDays();
}
