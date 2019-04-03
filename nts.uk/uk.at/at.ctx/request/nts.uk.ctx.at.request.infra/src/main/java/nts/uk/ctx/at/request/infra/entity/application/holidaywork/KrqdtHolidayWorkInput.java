package nts.uk.ctx.at.request.infra.entity.application.holidaywork;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRQDT_HOLIDAY_WORK_INPUT")
public class KrqdtHolidayWorkInput extends UkJpaEntity implements Serializable {
private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    protected KrqdtHolidayWorkInputPK krqdtHolidayWorkInputPK;
	
	@Version
	@Column(name="EXCLUS_VER")
	public Long version;
	
	/**
	 * 休憩開始時刻
	 */
	@Column(name = "START_TIME")
    private Integer startTime;
	
    /**
     * 休憩終了時刻
     */
    @Column(name = "END_TIME")
    private Integer endTime;
    
    /**
     * 申請時間
     */
    @Column(name = "APPLICATION_TIME")
    private Integer applicationTime;
    
    /**
     * 勤怠項目ID
     */
    @Column(name = "ATTENDANCE_ID")
    private Integer attendanceId;

    @ManyToOne
    @PrimaryKeyJoinColumns({
    	@PrimaryKeyJoinColumn(name="CID", referencedColumnName="CID"),
    	@PrimaryKeyJoinColumn(name="APP_ID", referencedColumnName="APP_ID")
    })
	public KrqdtAppHolidayWork appHolidayWork;
    
    public KrqdtHolidayWorkInput(KrqdtHolidayWorkInputPK pk , Integer startTime, Integer endTime, Integer appTime){
    	this.krqdtHolidayWorkInputPK = pk;
    	this.startTime = startTime;
    	this.endTime = endTime;
    	this.applicationTime = appTime;
    }
	@Override
	protected Object getKey() {
		return krqdtHolidayWorkInputPK;
	}
	
	public KrqdtHolidayWorkInput fromDomainValue(HolidayWorkInput holidayWorkInput){
		this.startTime = holidayWorkInput.getStartTime()== null ? null : holidayWorkInput.getStartTime().v();
		this.endTime = holidayWorkInput.getEndTime() == null ? null :holidayWorkInput.getEndTime().v();
		this.applicationTime = holidayWorkInput.getApplicationTime().v();
		return this;
	}
	
	public HolidayWorkInput toDomain(){
		return HolidayWorkInput.createSimpleFromJavaType(
				this.krqdtHolidayWorkInputPK.getCid(), 
				this.krqdtHolidayWorkInputPK.getAppId(), 
				this.krqdtHolidayWorkInputPK.getAttendanceType(), 
				this.krqdtHolidayWorkInputPK.getFrameNo(), 
				this.startTime, 
				this.endTime, 
				this.applicationTime);
	} 

}
