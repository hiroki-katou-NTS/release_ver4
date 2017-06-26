/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.itemmaster;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 *
 * @author chinhbv
 */
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QCAMT_ITEM_SALARY")
@Entity
public class QcamtItemSalary extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcamtItemSalaryPK qcamtItemSalaryPK;
    
    @Basic(optional = false)
    @Column(name = "TAX_ATR")
    public int taxAtr;
    @Basic(optional = false)
    @Column(name = "SOCIAL_INS_ATR")
    public int socialInsAtr;
    @Basic(optional = false)
    @Column(name = "LABOR_INS_ATR")
    public int laborInsAtr;
    @Basic(optional = false)
    @Column(name = "FIX_PAY_ATR")
    public int fixPayAtr;
    @Basic(optional = false)
    @Column(name = "APPLY_FOR_ALL_EMP_FLG")
    public int applyForAllEmpFlg;
    @Basic(optional = false)
    @Column(name = "APPLY_FOR_MONTHLY_PAY_EMP")
    public int applyForMonthlyPayEmp;
    @Basic(optional = false)
    @Column(name = "APPLY_FOR_DAYMONTHLY_PAY_EMP")
    public int applyForDaymonthlyPayEmp;
    @Basic(optional = false)
    @Column(name = "APPLY_FOR_DAYLY_PAY_EMP")
    public int applyForDaylyPayEmp;
    @Basic(optional = false)
    @Column(name = "APPLY_FOR_HOURLY_PAY_EMP")
    public int applyForHourlyPayEmp;
    @Basic(optional = false)
    @Column(name = "AVE_PAY_ATR")
    public int avePayAtr;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_LOW_ATR")
    public int errRangeLowAtr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_LOW")
    public BigDecimal errRangeLow;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_HIGH_ATR")
    public int errRangeHighAtr;
    @Basic(optional = false)
    @Column(name = "ERR_RANGE_HIGH")
    public BigDecimal errRangeHigh;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_LOW_ATR")
    public int alRangeLowAtr;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_LOW")
    public BigDecimal alRangeLow;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_HIGH_ATR")
    public int alRangeHighAtr;
    @Basic(optional = false)
    @Column(name = "AL_RANGE_HIGH")
    public BigDecimal alRangeHigh;
    @Column(name = "MEMO")
    public String memo;
    @Basic(optional = false)
    @Column(name = "LIMIT_MNY_ATR")
    public int limitMnyAtr;
    @Column(name = "LIMIT_MNY_REF_ITEM_CD")
    public String limitMnyRefItemCd;
    @Column(name = "LIMIT_MNY")
    public BigDecimal limitMny;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcamtItemSalaryPK != null ? qcamtItemSalaryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcamtItemSalary)) {
            return false;
        }
        QcamtItemSalary other = (QcamtItemSalary) object;
        if ((this.qcamtItemSalaryPK == null && other.qcamtItemSalaryPK != null) || (this.qcamtItemSalaryPK != null && !this.qcamtItemSalaryPK.equals(other.qcamtItemSalaryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.itemmaster.QcamtItemSalary[ qcamtItemSalaryPK=" + qcamtItemSalaryPK + " ]";
    }
    
    @Override
    protected QcamtItemSalaryPK getKey() {
    	return this.qcamtItemSalaryPK;
    }
}
