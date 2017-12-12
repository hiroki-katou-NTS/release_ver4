package nts.uk.ctx.pereg.infra.entity.person.info.ctg;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PPEMT_PER_INFO_CTG_CM")
public class PpemtPerInfoCtgCm extends UkJpaEntity implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    public PpemtPerInfoCtgCmPK ppemtPerInfoCtgCmPK;
    
    @Basic(optional = false)
    @Column(name = "CATEGORY_PARENT_CD")
    public String categoryParentCd;
    
    @Basic(optional = false)
    @Column(name = "CATEGORY_TYPE")
    public int categoryType;
    
    @Basic(optional = false)
    @Column(name = "PERSON_EMPLOYEE_TYPE")
    public int personEmployeeType;
    
    @Basic(optional = false)
    @Column(name = "FIXED_ATR")
    public int fixedAtr;
    
	@Override
	protected Object getKey() {
		return ppemtPerInfoCtgCmPK;
	}
}
