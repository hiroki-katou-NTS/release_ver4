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
     * 履歴ID
     */
    @Basic(optional = false)
    @Column(name = "HIST_ID")
    public String histId;

    /**
     * カテゴリ区分
     */
    @Basic(optional = false)
    @Column(name="CTG_ATR")
    public int categoryAtr;

    /**
     * 行番号
     */
    @Basic(optional = false)
    @Column(name="LINE_NUM")
    public int lineNumber;

    /**
     * 終了日
     */
    @Basic(optional = false)
    @Column(name = "ITEM_POSITION")
    public int itemPosition;
    
}
