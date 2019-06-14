package nts.uk.ctx.pr.core.infra.entity.wageprovision.statementlayout.itemrangeset;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 明細書項目範囲設定: 主キー情報
*/
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QpbmtStateItemRangeSetPk implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 給/**
     * 会社ID
     */
    @Basic(optional = false)
    @Column(name = "CID")
    public String cid;

    /**
     * 明細書コード
     */
    @Basic(optional = false)
    @Column(name = "STATEMENT_CD")
    public String statementCd;

    /**
     * 履歴ID
     */
    @Basic(optional = false)
    @Column(name = "HIST_ID")
    public String histId;

    /**
     * カテゴリ区分
     */
    @Basic(optional = false)
    @Column(name="CATEGORY_ATR")
    public int categoryAtr;

    /**
     * 給与項目ID
     */
    @Basic(optional = false)
    @Column(name = "ITEM_NAME_CD")
    public String itemNameCd;

}
