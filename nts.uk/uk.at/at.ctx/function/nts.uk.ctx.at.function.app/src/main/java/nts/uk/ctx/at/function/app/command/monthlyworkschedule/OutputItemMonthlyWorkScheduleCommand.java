/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.command.monthlyworkschedule;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.function.dom.dailyworkschedule.RemarkInputContent;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.ItemSelectionEnum;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.MonthlyAttendanceItemsDisplay;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.MonthlyOutputItemSettingCode;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.MonthlyOutputItemSettingName;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkScheduleGetMemento;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.PrintSettingRemarksColumn;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.TextSizeCommonEnum;


// TODO: Auto-generated Javadoc
/**
 * The Class OutputItemMonthlyWorkScheduleCommand.
 */
public class OutputItemMonthlyWorkScheduleCommand implements OutputItemMonthlyWorkScheduleGetMemento {

	/** The item code. */
	private String itemCode;

	/** The item name. */
	private String itemName;

	/** The lst displayed attendance. */
	private List<AttendanceTobeDisplayCommand> lstDisplayedAttendance;

	/** The print setting remarks column. */
	private int printSettingRemarksColumn;
	
	/** The remark input no. */
	private int remarkInputNo;
	
	/** The Layout ID*/
	private String layoutID;
	
	/** The Employee ID. */
	private String employeeID;
	
	/** The text size */
	private int textSize;
	
	/** The Item selection type*/
	private int  itemType;
	/** The new mode. */
	// This variable used to know is new mode when save.

	/**
	 * Checks if is new mode.
	 *
	 * @return true, if is new mode
	 */
	@Getter
	private boolean newMode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleGetMemento#getCompanyID()
	 */
	@Override
	public String getCompanyID() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleGetMemento#getItemCode()
	 */
	@Override
	public MonthlyOutputItemSettingCode getItemCode() {

		return new MonthlyOutputItemSettingCode(this.itemCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleGetMemento#getItemName()
	 */
	@Override
	public MonthlyOutputItemSettingName getItemName() {

		return new MonthlyOutputItemSettingName(this.itemName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleGetMemento#getLstDisplayedAttendance()
	 */
	@Override
	public List<MonthlyAttendanceItemsDisplay> getLstDisplayedAttendance() {

		return lstDisplayedAttendance.stream().map(command -> {
			return new MonthlyAttendanceItemsDisplay(command.getSortBy(), command.getItemToDisplay());
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleGetMemento#getPrintSettingRemarksColumn()
	 */
	@Override
	public PrintSettingRemarksColumn getPrintSettingRemarksColumn() {
		// TODO Auto-generated method stub
		return PrintSettingRemarksColumn.valueOf(this.printSettingRemarksColumn);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkScheduleGetMemento#getRemarkInputNo()
	 */
	@Override
	public RemarkInputContent getRemarkInputNo() {
		return RemarkInputContent.valueOf(this.remarkInputNo);
	}

	@Override
	public String getLayoutID() {
		return this.layoutID;
	}

	@Override
	public String getEmployeeID() {
		return this.employeeID;
	}

	@Override
	public ItemSelectionEnum getItemSelectionEnum() {
		return ItemSelectionEnum.valueOf(this.itemType);
	}

	@Override
	public TextSizeCommonEnum getTextSize() {
		return TextSizeCommonEnum.valueOf(this.textSize);
	}
}
