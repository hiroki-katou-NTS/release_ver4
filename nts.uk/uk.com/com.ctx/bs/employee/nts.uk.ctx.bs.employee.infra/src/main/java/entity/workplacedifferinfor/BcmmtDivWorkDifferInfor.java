package entity.workplacedifferinfor;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "BCMMT_DIV_WK_DIF_INFOR")
public class BcmmtDivWorkDifferInfor extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	public BcmmtDivWorkDifferInforPK bcmmtDivWorkDifferInforPK;
	
	/** 職場登録区分 */
	@Column(name = "REGIS_WORK_DIVISION")
	public int regWorkDiv;
	
	@Override
	protected Object getKey() {
		return bcmmtDivWorkDifferInforPK;
	}
	
	public BcmmtDivWorkDifferInfor(BcmmtDivWorkDifferInforPK bcmmtDivWorkDifferInforPK){
		super();
		this.bcmmtDivWorkDifferInforPK = bcmmtDivWorkDifferInforPK;
	}
}
