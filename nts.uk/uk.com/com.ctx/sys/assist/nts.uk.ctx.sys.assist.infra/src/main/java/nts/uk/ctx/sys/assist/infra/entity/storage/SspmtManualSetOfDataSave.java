package nts.uk.ctx.sys.assist.infra.entity.storage;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* データ保存の手動設定
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SSPMT_MANUAL_SET_OF_DATA_SAVE")
public class SspmtManualSetOfDataSave extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 会社ID
     */
     @Basic(optional = false)
     @Column(name = "CID")
     public String cid;
     
     /**
     * データ保存処理ID
     */
     @Id
     @Basic(optional = false)
     @Column(name = "STORE_PROCESSING_ID")
     public String storeProcessingId;
    /**
    * システム種類
    */
    @Basic(optional = false)
    @Column(name = "SYSTEM_TYPE")
    public int systemType;
    
    /**
    * パスワード有無
    */
    @Basic(optional = false)
    @Column(name = "PASSWORD_AVAILABILITY")
    public int passwordAvailability;
    
    /**
    * 保存セット名称
    */
    @Basic(optional = false)
    @Column(name = "SAVE_SET_NAME")
    public String saveSetName;
    
    /**
    * 基準日
    */
    @Basic(optional = false)
    @Column(name = "REFERENCE_DATE")
    public GeneralDate referenceDate;
    
    /**
    * 手動保存の圧縮パスワード
    */
    @Basic(optional = false)
    @Column(name = "COMPRESSED_PASSWORD")
    public String compressedPassword;
    
    /**
    * 実行日時
    */
    @Basic(optional = false)
    @Column(name = "EXECUTION_DATE_AND_TIME")
    public GeneralDateTime executionDateAndTime;
    
    /**
    * 日次保存終了日
    */
    @Basic(optional = false)
    @Column(name = "DAY_SAVE_END_DATE")
    public GeneralDate daySaveEndDate;
    
    /**
    * 日次保存開始日
    */
    @Basic(optional = false)
    @Column(name = "DAY_SAVE_START_DATE")
    public GeneralDate daySaveStartDate;
    
    /**
    * 月次保存終了日
    */
    @Basic(optional = false)
    @Column(name = "MONTH_SAVE_END_DATE")
    public GeneralDate monthSaveEndDate;
    
    /**
    * 月次保存開始日
    */
    @Basic(optional = false)
    @Column(name = "MONTH_SAVE_START_DATE")
    public GeneralDate monthSaveStartDate;
    
    /**
    * 補足説明
    */
    @Basic(optional = false)
    @Column(name = "SUPPLE_EXPLANATION")
    public String suppleExplanation;
    
    /**
    * 年次終了年
    */
    @Basic(optional = false)
    @Column(name = "END_YEAR")
    public int endYear;
    
    /**
    * 年次開始年
    */
    @Basic(optional = false)
    @Column(name = "START_YEAR")
    public int startYear;
    
    /**
    * 社員指定の有無
    */
    @Basic(optional = false)
    @Column(name = "PRESENCE_OF_EMPLOYEE")
    public int presenceOfEmployee;
    
    /**
    * 調査用保存の識別
    */
    @Basic(optional = false)
    @Column(name = "IDENT_OF_SURVEY_PRE")
    public int identOfSurveyPre;
    
    /**
    * 実行者
    */
    @Basic(optional = false)
    @Column(name = "PRACTITIONER")
    public String practitioner;
    
    @Override
    protected Object getKey()
    {
        return manualSetOfDataSavePk;
    }

}
