package nts.uk.ctx.pr.report.infra.entity.printdata.socialinsurnoticreset;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.report.dom.printdata.socinsurnoticreset.SocialInsurNotiCreateSet;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;


/**
* 厚生年金種別情報
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QQSMT_SOC_INSU_NOTI_SET")
public class QqsmtSocInsuNotiSet extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QqsmtSocInsuNotiSetPk socInsuNotiSetPk;
    
    /**
    * 事業所情報
    */
    @Basic(optional = false)
    @Column(name = "OFFICE_INFORMATION")
    public int officeInformation;
    
    /**
    * 事業所整理記号
    */
    @Basic(optional = false)
    @Column(name = "BUSSINESS_ARR_SYMBOL")
    public int bussinessArrSymbol;
    
    /**
    * 出力順
    */
    @Basic(optional = false)
    @Column(name = "OUTPUT_ORDER")
    public int outputOrder;
    
    /**
    * 印刷個人番号
    */
    @Basic(optional = false)
    @Column(name = "PRINT_PERSON_NUMBER")
    public int printPersonNumber;
    
    /**
    * 提出氏名
    */
    @Basic(optional = false)
    @Column(name = "SUBMITTED_NAME")
    public int submittedName;
    
    /**
    * 被保険者整理番号
    */
    @Basic(optional = false)
    @Column(name = "INSURED_NUMBER")
    public int insuredNumber;
    
    /**
    * FD番号
    */
    @Basic(optional = true)
    @Column(name = "FD_NUMBER")
    public String fdNumber;
    
    /**
    * テキスト個人番号
    */
    @Basic(optional = true)
    @Column(name = "TEXT_PERSON_NUMBER")
    public Integer textPersonNumber;
    
    /**
    * 出力形式
    */
    @Basic(optional = true)
    @Column(name = "OUTPUT_FORMAT")
    public Integer outputFormat;
    
    /**
    * 改行コード
    */
    @Basic(optional = true)
    @Column(name = "LINE_FEED_CODE")
    public Integer lineFeedCode;
    
    @Override
    protected Object getKey()
    {
        return socInsuNotiSetPk;
    }

    public SocialInsurNotiCreateSet toDomain() {
       return new SocialInsurNotiCreateSet(
               this.socInsuNotiSetPk.userId,
               this.socInsuNotiSetPk.cid,
               this.officeInformation,
               this.bussinessArrSymbol,
               this.outputOrder,
               this.printPersonNumber,
               this.submittedName,
               this.insuredNumber,
               this.fdNumber,
               this.textPersonNumber,
               this.outputFormat,
               this.lineFeedCode);
    }
    public static QqsmtSocInsuNotiSet toEntity(SocialInsurNotiCreateSet domain) {
       return null;
    }

}
