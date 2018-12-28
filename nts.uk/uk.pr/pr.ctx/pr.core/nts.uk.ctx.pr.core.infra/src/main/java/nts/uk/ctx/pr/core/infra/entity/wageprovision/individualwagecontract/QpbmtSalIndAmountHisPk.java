package nts.uk.ctx.pr.core.infra.entity.wageprovision.individualwagecontract;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 給与個人別金額履歴: 主キー情報
*/
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QpbmtSalIndAmountHisPk implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * 社員ID
    */
    @Basic(optional = false)
    @Column(name = "EMP_ID")
    public String empId;
    
    /**
    * 履歴ID
    */
    @Basic(optional = false)
    @Column(name = "HISTORY_ID")
    public String historyId;
    
    /**
    * 給与賞与区分
    */
    @Basic(optional = false)
    @Column(name = "SAL_BONUS_CATE")
    public int salBonusCate;
    
    /**
    * カテゴリ区分
    */
    @Basic(optional = false)
    @Column(name = "CATE_INDICATOR")
    public int cateIndicator;
    
    /**
    * 個人金額コード
    */
    @Basic(optional = false)
    @Column(name = "PER_VAL_CODE")
    public String perValCode;
    
}
