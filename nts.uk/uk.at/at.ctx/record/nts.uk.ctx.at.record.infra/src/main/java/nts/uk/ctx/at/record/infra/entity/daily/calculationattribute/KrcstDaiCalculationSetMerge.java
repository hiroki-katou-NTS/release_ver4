package nts.uk.ctx.at.record.infra.entity.daily.calculationattribute;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCST_DAI_CALCULATION_SET_MERGE")
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class KrcstDaiCalculationSetMerge extends UkJpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public KrcstDaiCalculationSetMergePK krcstDaiCalculationSetMergePK;
    //自動計算設定
    @Basic(optional = false)
    @NotNull
    @Column(name = "BONUS_PAY_NORMAL_CAL_SET")
    public int bonusPayNormalCalSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BONUS_PAY_SPE_CAL_SET")
    public int bonusPaySpeCalSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEAVE_LATE_SET")
    public int leaveLateSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEAVE_EARLY_SET")
    public int leaveEarlySet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIVERGENCE_TIME")
    public int divergenceTime;
    //自動計算設定
    
    //残業の自動計算設定
    @Basic(optional = false)
    @NotNull
    @Column(name = "EARLY_OVER_TIME_CAL_ATR")
    public int earlyOverTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EARLY_OVER_TIME_LIMIT_SET")
    public int earlyOverTimeLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EARLY_MID_OT_CAL_ATR")
    public int earlyMidOtCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EARLY_MID_OT_LIMIT_SET")
    public int earlyMidOtLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NORMAL_OVER_TIME_CAL_ATR")
    public int normalOverTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NORMAL_OVER_TIME_LIMIT_SET")
    public int normalOverTimeLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NORMAL_MID_OT_CAL_ATR")
    public int normalMidOtCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NORMAL_MID_OT_LIMIT_SET")
    public int normalMidOtLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEGAL_OVER_TIME_CAL_ATR")
    public int legalOverTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEGAL_OVER_TIME_LIMIT_SET")
    public int legalOverTimeLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEGAL_MID_OT_CAL_ATR")
    public int legalMidOtCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEGAL_MID_OT_LIMIT_SET")
    public int legalMidOtLimitSet;
    //残業の自動計算設定
    
    //フレックスの自動計算設定
    @Basic(optional = false)
    @NotNull
    @Column(name = "FLEX_EXCESS_TIME_CAL_ATR")
    public int flexExcessTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FLEX_EXCESS_LIMIT_SET")
    public int flexExcessLimitSet;
    //フレックスの自動計算設定
    
    //休出の自動計算設定
    @Basic(optional = false)
    @NotNull
    @Column(name = "HOL_WORK_TIME_CAL_ATR")
    public int holWorkTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HOL_WORK_TIME_LIMIT_SET")
    public int holWorkTimeLimitSet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATE_NIGHT_TIME_CAL_ATR")
    public int lateNightTimeCalAtr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATE_NIGHT_TIME_LIMIT_SET")
    public int lateNightTimeLimitSet;
    //休出の自動計算設定
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (krcstDaiCalculationSetMergePK != null ? krcstDaiCalculationSetMergePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KrcstDaiCalculationSetMerge)) {
            return false;
        }
        KrcstDaiCalculationSetMerge other = (KrcstDaiCalculationSetMerge) object;
        if ((this.krcstDaiCalculationSetMergePK == null && other.krcstDaiCalculationSetMergePK != null) || (this.krcstDaiCalculationSetMergePK != null && !this.krcstDaiCalculationSetMergePK.equals(other.krcstDaiCalculationSetMergePK))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entities.KrcstDaiCalculationSetMerge[ krcstDaiCalculationSetMergePK=" + krcstDaiCalculationSetMergePK + " ]";
    }
    
	@Override
	protected Object getKey() {
		return krcstDaiCalculationSetMergePK;
	}
}
