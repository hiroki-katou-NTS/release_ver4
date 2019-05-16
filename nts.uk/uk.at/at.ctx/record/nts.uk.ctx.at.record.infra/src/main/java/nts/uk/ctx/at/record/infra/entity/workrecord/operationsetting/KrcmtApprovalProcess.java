package nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.YourselfConfirmError;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 承認処理の利用設定
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_BOSS_CHECK_SET")
public class KrcmtApprovalProcess extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public static final JpaEntityMapper<KrcmtApprovalProcess> MAPPER = new JpaEntityMapper<>(KrcmtApprovalProcess.class);
    
    /**
    * ID
    */
    @EmbeddedId
    public KrcmtApprovalProcessPk approvalProcessPk;
    
    /**
    * 職位ID
    */
    @Basic(optional = true)
    @Column(name = "JOB_TITLE_NOT_BOSS_CHECK")
    public String jobTitleId;
    
    /**
    * 日の承認者確認を利用する
    */
    @Basic(optional = false)
    @Column(name = "USE_DAILY_BOSS_CHECK")
    public int useDailyBossChk;
    
    /**
    * 月の承認者確認を利用する
    */
    @Basic(optional = false)
    @Column(name = "USE_MONTHLY_BOSS_CHECK")
    public int useMonthBossChk;
    
    /**
    * エラーがある場合の上司確認
    */
    @Basic(optional = true)
    @Column(name = "SUPERVISOR_CONFIRM_ERROR")
    public Integer supervisorConfirmError;
    
    @Override
    protected Object getKey()
    {
        return approvalProcessPk;
    }

    public ApprovalProcess toDomain() {
        return new ApprovalProcess(this.approvalProcessPk.cid, this.jobTitleId, 
					        		this.useDailyBossChk, this.useMonthBossChk, 
					        		supervisorConfirmError == null ? null : EnumAdaptor.valueOf(this.supervisorConfirmError, YourselfConfirmError.class));
    }
    public static KrcmtApprovalProcess toEntity(ApprovalProcess domain) {
        return new KrcmtApprovalProcess(new KrcmtApprovalProcessPk(domain.getCid()), domain.getJobTitleId(), 
							        		domain.getUseDailyBossChk(), 
							        		domain.getUseMonthBossChk(), 
							        		domain.getSupervisorConfirmError() == null ? null : domain.getSupervisorConfirmError().value);
    }

}
