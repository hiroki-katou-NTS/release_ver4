/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.temporaryabsence.frame;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;


@Getter
@Setter
/**
 * The Class TempAbsenceFrame.
 */
//休職休業枠
public class TempAbsenceFrame extends AggregateRoot{
	
	/** The company id. */
	// 会社ID
	private String companyId;
	
	/** The temp absence fr no. */
	//休職休業枠NO
	private TempAbsenceFrameNo tempAbsenceFrNo;
	
	/** The use classification. */
	//使用区分
	private NotUseAtr useClassification;
	
	/** The temp absence fr name. */
	//休職休業枠名称
	private TempAbsenceFrameName tempAbsenceFrName;
	
	/**
	 * Instantiates a new temp absence frame.
	 *
	 * @param memento the memento
	 */
	public TempAbsenceFrame(TempAbsenceFrameGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.tempAbsenceFrNo = memento.getTempAbsenceFrameNo();
		this.useClassification = memento.getUseClassification();
		this.tempAbsenceFrName = memento.getTempAbsenceFrameName();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(TempAbsenceFrameSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setTempAbsenceFrameNo(this.tempAbsenceFrNo);
		memento.setUseClassification(this.useClassification);
		memento.setTempAbsenceFrameName(this.tempAbsenceFrName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((tempAbsenceFrNo == null) ? 0 : tempAbsenceFrNo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempAbsenceFrame other = (TempAbsenceFrame) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		
		if (tempAbsenceFrNo == null) {
			if (other.tempAbsenceFrNo != null)
				return false;
		} else if (!tempAbsenceFrNo.equals(other.tempAbsenceFrNo))
			return false;
		
		return true;
	}

	public TempAbsenceFrame(String companyId, int tempAbsenceFrNo, int useClassification,
			String tempAbsenceFrName) {
		super();
		this.companyId = companyId;
		this.tempAbsenceFrNo   = new TempAbsenceFrameNo(new BigDecimal(tempAbsenceFrNo));
		this.useClassification = NotUseAtr.valueOf(useClassification) ;
		this.tempAbsenceFrName = new TempAbsenceFrameName(tempAbsenceFrName);
	}
	
	
}
