package nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCST_DAILY_REC_OPE_DISP")
public class KrcstDailyRecOpeDisp extends UkJpaEntity{
	
	@Id
	@Column(name = "CID")
	public String cid;
	
	@Column(name = "SAVING_YEAR_HD_DISP_RES_CHECK")
	public BigDecimal savingYearHdDispResCheck; 
	
	@Column(name = "SAVING_YEAR_HD_DISP_RES_ATR")
	public BigDecimal savingYearHdDispResAtr;
	
	@Column(name = "COM_HD_DISP_RES_CHECK")
	public BigDecimal comHdDispResCheck;
	
	@Column(name = "COM_HD_DISP_RES_ATR")
	public BigDecimal comHdDispResAtr;
	
	@Column(name = "SUB_HD_DISP_RES_CHECK")
	public BigDecimal subHdDispResCheck;
	
	@Column(name = "SUB_HD_DISP_RES_ATR")
	public BigDecimal subHdDispResAtr;
	
	@Column(name = "YEAR_HD_DISP_RES_CHECK")
	public BigDecimal yearHdDispResCheck;
	
	@Column(name = "YEAR_HD_DISP_RES_ATR")
	public BigDecimal yearHdDispResAtr;

	@Override
	protected Object getKey() {
		return this.cid;
	}

}
