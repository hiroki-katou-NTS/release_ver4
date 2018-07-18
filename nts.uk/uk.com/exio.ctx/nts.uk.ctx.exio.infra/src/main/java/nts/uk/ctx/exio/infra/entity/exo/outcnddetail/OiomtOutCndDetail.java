package nts.uk.ctx.exio.infra.entity.exo.outcnddetail;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetail;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 出力条件詳細(定型)
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_OUT_CND_DETAIL")
public class OiomtOutCndDetail extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public OiomtOutCndDetailPk outCndDetailPk;
    
    /**
    * 条件SQL
    */
    @Basic(optional = false)
    @Column(name = "EXTER_OUT_CDN_SQL")
    public String exterOutCdnSql;
    
<<<<<<< HEAD
//    @OneToOne
//    @JoinColumns({ 
//    	@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
//		@JoinColumn(name = "CONDITION_SETTING_CD", referencedColumnName = "CONDITION_SETTING_CD", insertable = false, updatable = false),
//		})
//    public OiomtOutCndDetailItem oiomtOutCndDetailItem;
=======
    /*@OneToOne
    @JoinColumns({ 
    	@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
		@JoinColumn(name = "CONDITION_SETTING_CD", referencedColumnName = "CONDITION_SETTING_CD", insertable = false, updatable = false),
		})*/
    //public OiomtOutCndDetailItem oiomtOutCndDetailItem;
>>>>>>> 12f74b76bd70d3bd0992a471a28bb9d938ee01b7
    
    @Override
    protected Object getKey()
    {
        return outCndDetailPk;
    }

    public OutCndDetail toDomain() {
        return new OutCndDetail(this.outCndDetailPk.cid, this.outCndDetailPk.conditionSettingCd, this.exterOutCdnSql);
    }

	public OiomtOutCndDetail(String cid,String conditionSettingCd, String exterOutCdnSql) {
		this.outCndDetailPk = new  OiomtOutCndDetailPk(cid, conditionSettingCd);
		this.exterOutCdnSql = exterOutCdnSql;
		
	}
}

