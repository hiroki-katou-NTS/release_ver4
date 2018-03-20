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
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.YourselfConfirmError;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 本人確認処理の利用設定
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_IDENTITY_PROCESS")
public class KrcmtIdentityProcess extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public KrcmtIdentityProcessPk identityProcessPk;
    
    /**
    * 本人確認を利用する
    */
    @Basic(optional = false)
    @Column(name = "USE_CONFIRM_BY_YOURSELF")
    public int useConfirmByYourself;
    
    /**
    * 月の本人確認を利用する
    */
    @Basic(optional = false)
    @Column(name = "USE_IDENTITY_OF_MONTH")
    public int useIdentityOfMonth;
    
    /**
    * エラーがある場合の本人確認
    */
    @Basic(optional = true)
    @Column(name = "YOURSELF_CONFIRM_ERROR")
    public int yourselfConfirmError;
    
    @Override
    protected Object getKey()
    {
        return identityProcessPk;
    }

    public IdentityProcess toDomain() {
        return new IdentityProcess(this.identityProcessPk.cid, this.useConfirmByYourself, this.useIdentityOfMonth, EnumAdaptor.valueOf(this.yourselfConfirmError, YourselfConfirmError.class));
    }
    public static KrcmtIdentityProcess toEntity(IdentityProcess domain) {
        return new KrcmtIdentityProcess(new KrcmtIdentityProcessPk(domain.getCid()), domain.getUseConfirmByYourself(), domain.getUseIdentityOfMonth(), domain.getYourselfConfirmError().value);
    }

}
