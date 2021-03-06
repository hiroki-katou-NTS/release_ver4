package nts.uk.ctx.pr.core.infra.entity.wageprovision.statementlayout;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.PaymentItemDetailSet;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
* 支給項目明細設定
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_PAY_ITEM_DETAIL_SET")
public class QpbmtPayItemDetailSet extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QpbmtPayItemDetailSetPk payItemDetailSetPk;
    
    /**
    * 合計対象
    */
    @Basic(optional = false)
    @Column(name = "TOTAL_OBJ")
    public int totalObj;
    
    /**
    * 按分区分
    */
    @Basic(optional = false)
    @Column(name = "PROPORTIONAL_ATR")
    public int proportionalAtr;
    
    /**
    * 按分方法
    */
    @Basic(optional = false)
    @Column(name = "PROPORTIONAL_METHOD")
    public Integer proportionalMethod;
    
    /**
    * 計算方法
    */
    @Basic(optional = false)
    @Column(name = "CALC_METHOD")
    public int calcMethod;
    
    /**
    * 計算式コード
    */
    @Basic(optional = true)
    @Column(name = "CALC_FORMULA_CD")
    public String calcFormulaCd;
    
    /**
    * 個人金額コード
    */
    @Basic(optional = true)
    @Column(name = "PERSON_AMOUNT_CD")
    public String personAmountCd;
    
    /**
    * 共通金額
    */
    @Basic(optional = false)
    @Column(name = "COMMON_AMOUNT")
    public Long commonAmount;
    
    /**
    * 賃金テーブルコード
    */
    @Basic(optional = true)
    @Column(name = "WAGE_TABLE_CD")
    public String wageTblCd;
    
    /**
    * 通勤区分
    */
    @Basic(optional = true)
    @Column(name = "COMMUT_ATR")
    public Integer workingAtr;
    
    @Override
    protected Object getKey()
    {
        return payItemDetailSetPk;
    }

    public PaymentItemDetailSet toDomain(){
        return new PaymentItemDetailSet(this.payItemDetailSetPk.histId, this.payItemDetailSetPk.cid, this.payItemDetailSetPk.statementCd, this.payItemDetailSetPk.itemNameCd, this.totalObj,this.proportionalAtr,this.proportionalMethod,this.calcMethod,this.calcFormulaCd,this.personAmountCd,this.commonAmount,this.wageTblCd,this.workingAtr);
    }


    public  static QpbmtPayItemDetailSet toEntity(PaymentItemDetailSet domain){
        String cid = AppContexts.user().companyId();

        QpbmtPayItemDetailSet entity = new QpbmtPayItemDetailSet();
        entity.payItemDetailSetPk = new QpbmtPayItemDetailSetPk(cid, domain.getStatementCode().v(), domain.getHistId(), domain.getItemNameCd());
        entity.totalObj = domain.getTotalObj().value;
        entity.proportionalAtr = domain.getProportionalAtr().value;
        entity.proportionalMethod = domain.getProportionalMethod().map(i->i.value).orElse(0);
        entity.calcMethod = domain.getCalcMethod().value;
        entity.calcFormulaCd = domain.getCalcFomulaCd().map(i->i.v()).orElse(null);
        entity.personAmountCd = domain.getPersonAmountCd().map(i->i.v()).orElse(null);
        entity.commonAmount = domain.getCommonAmount().map(i->i.v()).orElse(0L);
        entity.wageTblCd = domain.getWageTblCode().map(i->i.v()).orElse(null);
        entity.workingAtr = domain.getWorkingAtr().map(i->i.value).orElse(null);
        return entity;
    }

}
