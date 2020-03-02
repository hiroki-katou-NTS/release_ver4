package nts.uk.ctx.pr.shared.infra.entity.salgenpurposeparam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import nts.arc.time.GeneralDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.shared.dom.salgenpurposeparam.*;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 給与汎用パラメータ年月日履歴
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QQSMT_SAL_GEN_PR_DATE_HIS")
public class QqsmtSalGenPrDateHis extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @EmbeddedId
    public QqsmtSalGenPrDateHisPk salGenPrDateHisPk;

    /**
    * 開始日
    */
    @Basic(optional = false)
    @Column(name = "START_DATE")
    public GeneralDate startDate;

    /**
    * 終了日
    */
    @Basic(optional = false)
    @Column(name = "END_DATE")
    public GeneralDate endDate;
    /*Gộp domain  QQSMT_SAL_GEN_PARAM_VALUE給与汎用給与汎用パラメータ識別パラメータ値*/

    /**
     * 有効区分
     */
    @Basic(optional = false)
    @Column(name = "AVAILABLE_ATR")
    public int availableAtr;

    /**
     * 値（数値）
     */
    @Basic(optional = true)
    @Column(name = "NUMBER_VALUE")
    public BigDecimal numberValue;

    /**
     * 値（文字）
     */
    @Basic(optional = true)
    @Column(name = "CHARACTER_VALUE")
    public String characterValue;

    /**
     * 値（時間）
     */
    @Basic(optional = true)
    @Column(name = "TIME_VALUE")
    public Integer timeValue;

    /**
     * 対象区分
     */
    @Basic(optional = true)
    @Column(name = "TARGET_ATR")
    public Integer targetAtr;
    /**
     * 選択肢
     */
    @Basic(optional = true)
    @Column(name = "OPTION_NO")
    public Integer selection;

    @Override
    protected Object getKey()
    {
        return salGenPrDateHisPk;
    }


    public static QqsmtSalGenPrDateHis toEntity(DateHistoryItem domain, String paraNo, String cId,
                                                Optional<Integer> selection,
                                                ParaAvailableValue availableAtr,
                                                Optional<ParamNumber> numValue,
                                                Optional<ParamCharacter> charValue,
                                                Optional<ParamTime> timeValue,
                                                Optional<ParaTargetAtr> targetAtr ) {
        return new QqsmtSalGenPrDateHis(new QqsmtSalGenPrDateHisPk(paraNo, cId,domain.identifier()),
                domain.start(),
                domain.end(),
                availableAtr.value,
                numValue.map(i -> i.v()).orElse(null),
                charValue.map(i -> i.v()).orElse(null),
                timeValue.map(i -> i.v()).orElse(null),
                targetAtr.map(i -> i.value).orElse(null),
                selection.map(i -> i.intValue()).orElse(null));
    }
    public SalGenParaValue toDomain() {
        return new SalGenParaValue(this.salGenPrDateHisPk.hisId, this.selection, this.availableAtr, this.numberValue, this.characterValue, this.timeValue, this.targetAtr);
    }


}
