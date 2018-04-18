package nts.uk.ctx.at.record.infra.entity.remainingnumber.nursingcareleave;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name="KRCMT_CARE_HD_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrcmtCareHDData extends UkJpaEntity{
	
	//社員ID
	@Id
	@Column(name="SID")
	private String sId;
	
	@Column(name="CID")
	private String cId;
	
	//使用日数
	@Column(name="USED_DAYS")
	private int userDay;

	@Override
	protected Object getKey() {		
		return this.getSId();
	}
}
