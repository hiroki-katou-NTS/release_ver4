/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.overtime.setting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.command.overtime.OvertimeSaveDto;
import nts.uk.ctx.at.shared.app.command.overtime.breakdown.OvertimeBRDItemSaveDto;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTCalMed;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingGetMemento;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.OvertimeNote;

/**
 * The Class OvertimeSettingSaveDto.
 */
@Getter
@Setter
public class OvertimeSettingSaveDto implements OutsideOTSettingGetMemento{

	/** The note. */
	private String note;
	
    /** The calculation method. */
    private Integer calculationMethod;
    
    /** The breakdown items. */
    private List<OvertimeBRDItemSaveDto> breakdownItems; 
    
    /** The overtimes. */
    private List<OvertimeSaveDto> overtimes;
    
    /** The company id. */
    private String companyId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingGetMemento#
	 * getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.companyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingGetMemento#
	 * getNote()
	 */
	@Override
	public OvertimeNote getNote() {
		return new OvertimeNote(this.note);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingGetMemento#
	 * getCalculationMethod()
	 */
	@Override
	public OutsideOTCalMed getCalculationMethod() {
		return OutsideOTCalMed.valueOf(this.calculationMethod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingGetMemento#
	 * getBreakdownItems()
	 */
	@Override
	public List<OutsideOTBRDItem> getBreakdownItems() {
		return this.breakdownItems.stream().map(dto -> new OutsideOTBRDItem(dto))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingGetMemento#
	 * getOvertimes()
	 */
	@Override
	public List<Overtime> getOvertimes() {
		return this.overtimes.stream().map(dto->new Overtime(dto)).collect(Collectors.toList());
	}

	

}
