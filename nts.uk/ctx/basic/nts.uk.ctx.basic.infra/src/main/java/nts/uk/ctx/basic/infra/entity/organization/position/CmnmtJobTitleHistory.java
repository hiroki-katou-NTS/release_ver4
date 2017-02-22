package nts.uk.ctx.basic.infra.entity.organization.position;


import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CMNMT_JOB_HIST")
public class CmnmtJobTitleHistory {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public CmnmtJobTitleHistoryPK cmnmtJobTitleHistoryPK;
	
	public CmnmtJobTitleHistoryPK geCmnmtJobTitleHistoryPK() {
		return cmnmtJobTitleHistoryPK;
	}
	
	public CmnmtJobTitleHistoryPK getCmnmtJobTitleHistoryPK() {
		return cmnmtJobTitleHistoryPK;
	}

	public void setCmnmtJobTitleHistoryPK(CmnmtJobTitleHistoryPK cmnmtJobTitleHistoryPK) {
		this.cmnmtJobTitleHistoryPK = cmnmtJobTitleHistoryPK;
	}

	public GeneralDate getStartDate() {
		return startDate;
	}

	public void setStartDate(GeneralDate startDate) {
		this.startDate = startDate;
	}

	public GeneralDate getEndDate() {
		return endDate;
	}

	public void setEndDate(GeneralDate endDate) {
		this.endDate = endDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Basic(optional = false)
	@Column(name ="STR_D")
	public GeneralDate startDate;

	@Basic(optional = false)
	@Column(name = "END_D")
	public GeneralDate endDate;
}
