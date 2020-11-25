package nts.uk.ctx.pr.core.infra.entity.socialinsurance.healthinsurance;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 等級毎健康保険料: 主キー情報
 */
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QpbmtHealthInsurancePerGradeFeePk implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会社ID
     */
    @Basic(optional = false)
    @Column(name = "CID")
    public String cid;

    /**
     * 社会保険事業所コード
     */
    @Basic(optional = false)
    @Column(name = "SOCIAL_INSURANCE_OFFICE_CD")
    public String socialInsuranceOfficeCd;

    /**
     * 履歴ID
     */
    @Basic(optional = false)
    @Column(name = "HISTORY_ID")
    public String historyId;

    /**
     * 健康保険等級
     */
    @Basic(optional = false)
    @Column(name = "HEALTH_INSURANCE_GRADE")
    public int healthInsuranceGrade;
}