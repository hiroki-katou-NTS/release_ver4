package nts.uk.ctx.exio.infra.entity.exi.dataformat;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
* 日付型データ形式設定: 主キー情報
*/
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OiomtDateDataFormSetPk implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * 会社ID
    */
    @Basic(optional = false)
    @Column(name = "CID")
    public String cid;
    
    /**
    * 条件設定コード
    */
    @Basic(optional = false)
    @Column(name = "CONDITION_SET_CD")
    public String conditionSetCd;
    
    /**
    * 受入項目番号
    */
    @Basic(optional = false)
    @Column(name = "ACCEPT_ITEM_NUM")
    public int acceptItemNum;
    
}
