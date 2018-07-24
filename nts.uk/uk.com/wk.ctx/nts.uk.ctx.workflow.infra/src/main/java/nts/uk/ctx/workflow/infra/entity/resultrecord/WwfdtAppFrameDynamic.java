package nts.uk.ctx.workflow.infra.entity.resultrecord;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_APP_FRAME_DYNAMIC")
public class WwfdtAppFrameDynamic extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpAppFrameDynamicPK pk;
	
	@Column(name="CONFIRM_ATR")
	private Integer confirmAtr;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="ROOT_ID",referencedColumnName="ROOT_ID"),
		@PrimaryKeyJoinColumn(name="PHASE_ORDER",referencedColumnName="PHASE_ORDER")
	})
	private WwfdtAppPhaseDynamic wwfdtAppPhaseDynamic;
	
	@OneToMany(targetEntity=WwfdtAppApproverDynamic.class, cascade = CascadeType.ALL, mappedBy = "wwfdtAppFrameDynamic", orphanRemoval = true)
	@JoinTable(name = "WWFDT_APP_APPROVE_DYNAMIC")
	public List<WwfdtAppApproverDynamic> listWwfdtAppApproverDynamic;

	@Override
	protected Object getKey() {
		return pk;
	}
	
}
