package nts.uk.ctx.at.record.infra.entity.monthly.remarks;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerformance;
import nts.uk.ctx.at.record.dom.monthly.performance.enums.StateOfEditMonthly;
import nts.uk.ctx.at.record.dom.monthly.remarks.RecordRemarks;
import nts.uk.ctx.at.record.dom.monthly.remarks.RemarksMonthlyRecord;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 
 * @author phongtq
 *
 */
@Entity
@Table(name = "KRCDT_REMARK_MONTHRECORD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KrcdtRemarksMonthlyRecord extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/** プライマリキー */
	@EmbeddedId
	public KrcdtRemarksMonthlyRecordPK recordPK;
	
	/** 期間 - start */
	@Column(name = "STR_YMD")
	public GeneralDate startYmd;
	
	/** 期間 - end */
	@Column(name = "END_YMD")
	public GeneralDate endYmd;

	/** 備考 */
	@Column(name = "RECORD_REMARKS")
	public String recordRemarks;
	

	
	@Override
	protected Object getKey() {
		return recordPK;
	}
	
public RemarksMonthlyRecord toDomain(){
		
		return new RemarksMonthlyRecord(
				this.recordPK.employeeID,
				EnumAdaptor.valueOf(this.recordPK.closureID, ClosureId.class),
				this.recordPK.remarksNo,
				new YearMonth(this.recordPK.remarksYM),
				new DatePeriod(this.startYmd, 
						       this.endYmd),
				new RecordRemarks(this.recordRemarks),
				new ClosureDate(this.recordPK.closeDay, (true))
				);
	}

}
