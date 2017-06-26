package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutDetail;
import nts.uk.shr.infra.data.entity.AggregateTableEntity;

@Entity
@Table(name="QCAMT_ITEM")
public class QcamtItem_v1 {

	@EmbeddedId
    public QcamtItemPK_v1 qcamtItemPK;
	
	@Column(name = "ITEM_NAME")
	public String itemName;
	
	@Column(name = "ITEM_AB_NAME")
	public String itemAbName;
	
	@Column(name = "DISP_ATR")
	public int dispAtr;
	
	@Column(name = "UNITE_CD")
	public String uniteCd;
	
	@Column(name = "TAX_ATR")
	public int taxAtr;
	
	@Column(name = "LIMIT_MNY")
	public BigDecimal limitMny;
	
	@Column(name = "SOCIAL_INS_ATR")
	public int socialInsAtr;
	
	@Column(name = "LABOR_INS_ATR")
	public int laborInsAtr;
	
	@Column(name = "FIX_PAY_ATR")
	public int fixPayAtr;
	
	@Column(name = "AVE_PAY_ATR")
	public int avePayAtr;
	
	@Column(name = "DEDUCT_ATR")
	public int deductAtr;
	
	@Column(name = "ITEM_ATR")
	public int itemAtr;
	
	@Column(name = "ZERO_DISP_ATR")
	public int zeroDispAtr;
	
	@Column(name = "ITEM_DISP_ATR")
	public int itemDispAtr;
	
	@Column(name = "ERR_RANGE_LOW_ATR")
	public int errRangeLowAtr;
	
	@Column(name = "ERR_RANGE_LOW")
	public BigDecimal errRangeLow;
	
	@Column(name = "ERR_RANGE_HIGH_ATR")
	public int errRangeHighAtr;
	
	@Column(name = "ERR_RANGE_HIGH")
	public BigDecimal errRangeHigh;
	
	@Column(name = "AL_RANGE_LOW_ATR")
	public int alRangeLowAtr;
	
	@Column(name = "AL_RANGE_LOW")
	public BigDecimal alRangeLow;
	
	@Column(name = "AL_RANGE_HIGH_ATR")
	public int alRangeHighAtr;
	
	@Column(name = "AL_RANGE_HIGH")
	public BigDecimal alRangeHigh;
	
	@Column(name = "MEMO")
	public String memo;
	
	@Column(name = "WORK_DAYS_SCOPE_ATR")
	public int workDaysScopeAtr;
	
//	@OneToOne
//	@JoinColumns({
//        @JoinColumn(name="CCD", referencedColumnName="CCD", insertable = false, updatable = false),
//        @JoinColumn(name="ITEM_CD", referencedColumnName="ITEM_CD", insertable = false, updatable = false),
//        @JoinColumn(name="CTG_ATR", referencedColumnName="CTG_ATR", insertable = false, updatable = false)
//    })
//	public QstmtStmtLayoutDetail layoutDetail;
}
