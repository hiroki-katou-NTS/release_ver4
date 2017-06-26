package nts.uk.ctx.pr.core.infra.entity.rule.employment.layout;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QSTMT_STMT_LAYOUT_DETAIL")
public class QstmtStmtLayoutDetail implements Serializable {
	
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtLayoutDetailPK qstmtStmtLayoutDetailPk;

	@Basic(optional = false)
	@Column(name ="AUTO_LINE_ID")
	public String autoLineId;

	@Basic(optional = false)
	@Column(name ="ITEM_POS_COLUMN")
	public int itemPosColumn;

	@Basic(optional = false)
	@Column(name ="DISP_SET")
	public int dispAtr;

	@Basic(optional = false)
	@Column(name ="SUM_SCOPE_ATR")
	public int sumScopeAtr;

	@Basic(optional = false)
	@Column(name ="CALC_METHOD")
	public int calcMethod;

	@Basic(optional = false)
	@Column(name ="P_WAGE_CD")
	public String pWageCd;

	@Column(name ="FORMULA_CD")
	public String formulaCd;

	@Column(name ="WAGE_TABLE_CD")
	public String wageTableCd;

	@Column(name ="COMMON_MNY")
	public BigDecimal commonMny;

	@Basic(optional = false)
	@Column(name ="SETOFF_ITEM_CD")
	public String setoffItemCd;

	@Basic(optional = false)
	@Column(name ="COMMUTE_ATR")
	public int commuteAtr;

	@Basic(optional = false)
	@Column(name ="DISTRIBUTE_SET")
	public int distributeSet;

	@Basic(optional = false)
	@Column(name ="DISTRIBUTE_WAY")
	public int distributeWay;

	@Basic(optional = false)
	@Column(name ="ERR_RANGE_LOW_ATR")
	public int errRangeLowAtr;

	@Basic(optional = false)
	@Column(name ="ERR_RANGE_LOW")
	public BigDecimal errRangeLow;

	@Basic(optional = false)
	@Column(name ="ERR_RANGE_HIGH_ATR")
	public int errRangeHighAtr;

	@Basic(optional = false)
	@Column(name ="ERR_RANGE_HIGH")
	public BigDecimal errRangeHigh;

	@Basic(optional = false)
	@Column(name ="AL_RANGE_LOW_ATR")
	public int alRangeLowAtr;

	@Basic(optional = false)
	@Column(name ="AL_RANGE_LOW")
	public BigDecimal alRangeLow;

	@Basic(optional = false)
	@Column(name ="AL_RANGE_HIGH_ATR")
	public int alRangeHighAtr;

	@Basic(optional = false)
	@Column(name ="AL_RANGE_HIGH")
	public BigDecimal alRangeHigh;
	
//	@OneToMany(cascade=CascadeType.ALL,mappedBy="layoutDetail")
//	public List<QstmtStmtLayoutLines> layoutLines;
//	
//	@OneToOne(optional=false)
//	@JoinColumns({
//		@JoinColumn(name="CCD", referencedColumnName="CCD", insertable = false, updatable = false),
//        @JoinColumn(name="ITEM_CD", referencedColumnName="ITEM_CD", insertable = false, updatable = false),
//        @JoinColumn(name="CTG_ATR", referencedColumnName="CTG_ATR", insertable = false, updatable = false)
//    })
//	public List<QcamtItem> items;
//	
//	@ManyToOne
//	@JoinColumns({
//        @JoinColumn(name="CCD", referencedColumnName="CCD", insertable = false, updatable = false),
//        @JoinColumn(name="STMT_CD", referencedColumnName="STMT_CD", insertable = false, updatable = false),
//        @JoinColumn(name="STR_YM", referencedColumnName="STR_YM", insertable = false, updatable = false),
//        @JoinColumn(name="CTG_ATR", referencedColumnName="CTG_ATR", insertable = false, updatable = false),
//        @JoinColumn(name="AUTO_LINE_ID", referencedColumnName="AUTO_LINE_ID", insertable = false, updatable = false)
//    })
//	public QstmtStmtLayoutLines layoutLine;
}
