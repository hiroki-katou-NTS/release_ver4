package nts.uk.ctx.sys.gateway.infra.entity.mail;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.url.UrlTaskIncre;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SGWMT_URL_TASK_INCRE")
public class SgwmtUrlTaskIncre extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public SgwmtUrlTaskIncrePk urlTaskIncrePk;
    
    /**
    * キー
    */
    @Basic(optional = false)
    @Column(name = "TASK_INCRE_KEY")
    public String taskIncreKey;
    
    /**
    * 値
    */
    @Basic(optional = false)
    @Column(name = "TASK_INCRE_VALUE")
    public String taskIncreValue;
    
    @ManyToOne
    @PrimaryKeyJoinColumns({
    	@PrimaryKeyJoinColumn(name="CID", referencedColumnName="CID"),
    	@PrimaryKeyJoinColumn(name="EMBEDDED_ID", referencedColumnName="EMBEDDED_ID")
    })
    private SgwmtUrlExecInfo urlExecInfo;
    
    @Override
    protected Object getKey()
    {
        return urlTaskIncrePk;
    }

    public UrlTaskIncre toDomain() {
        return new UrlTaskIncre(this.urlTaskIncrePk.embeddedId, this.urlTaskIncrePk.cid, this.urlTaskIncrePk.taskIncreId, this.taskIncreKey, this.taskIncreValue);
    }
    public static SgwmtUrlTaskIncre toEntity(UrlTaskIncre domain) {
        return new SgwmtUrlTaskIncre(new SgwmtUrlTaskIncrePk(domain.getEmbeddedId(), domain.getCid(), domain.getTaskIncreId()), domain.getTaskIncreKey(), domain.getTaskIncreValue(), null);
    }

}
