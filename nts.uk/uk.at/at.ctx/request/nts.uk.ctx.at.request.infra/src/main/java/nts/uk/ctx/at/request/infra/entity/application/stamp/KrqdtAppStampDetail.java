package nts.uk.ctx.at.request.infra.entity.application.stamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
@Table(name="KRQDT_APP_STAMP_DETAILS")
public class KrqdtAppStampDetail extends UkJpaEntity {
	
	@EmbeddedId
	public KrqdpAppStampDetail krqdpAppStampDetails;

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
		@PrimaryKeyJoinColumn(name="APP_ID",referencedColumnName="APP_ID"),
		@PrimaryKeyJoinColumn(name="STAMP_REQUEST_MODE",referencedColumnName="STAMP_REQUEST_MODE")
	})
	private KrqdpAppStamp krqdpAppStamp;
	
	@Override
	protected Object getKey() {
		return krqdpAppStampDetails;
	}
	
}
