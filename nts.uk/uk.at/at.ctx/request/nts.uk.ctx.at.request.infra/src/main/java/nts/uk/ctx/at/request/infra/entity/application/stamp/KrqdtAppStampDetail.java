package nts.uk.ctx.at.request.infra.entity.application.stamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name="KRQDT_APP_STAMP_DETAILS")
@Builder
public class KrqdtAppStampDetail extends UkJpaEntity {
	
	@EmbeddedId
	public KrqdpAppStampDetail krqdpAppStampDetailsPK;
	
	@Version
	@Column(name="EXCLUS_VER")
	public Long version;

	@Column(name="GO_OUT_REASON_ATR")
	public Integer goOutReasonAtr;
	
	@Column(name="START_TIME")
	public Integer startTime;
	
	@Column(name="START_LOCATION_CD")
	public String startLocationCD;
	
	@Column(name="END_TIME")
	public Integer endTime;
	
	@Column(name="END_LOCATION_CD")
	public String endLocationCD;
	
	@Column(name="SUPPORT_CARD")
	public String supportCard;
	
	@Column(name="SUPPORT_LOCATION_CD")
	public String supportLocationCD;
	
	@Column(name="CANCEL_ATR")
	public Integer cancelAtr;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="CID",referencedColumnName="CID"), 
		@PrimaryKeyJoinColumn(name="APP_ID",referencedColumnName="APP_ID")
	})
	private KrqdtAppStamp krqdtAppStamp;
	
	@Override
	protected Object getKey() {
		return krqdpAppStampDetailsPK;
	}
	
}
