package nts.uk.ctx.at.request.infra.entity.application.overtime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceID;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.TimeItemTypeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.primitivevalue.OvertimeAppPrimitiveTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRQDT_OVERTIME_INPUT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrqdtOvertimeInput extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    protected KrqdtOvertimeInputPK krqdtOvertimeInputPK;
	
	@Version
	@Column(name="EXCLUS_VER")
	public Long version;
	
	@Column(name = "START_TIME")
    private int startTime;
	
    @Column(name = "END_TIME")
    private int endTime;
    
    @Column(name = "APPLICATION_TIME")
    private int applicationTime;

    @ManyToOne
    @PrimaryKeyJoinColumns({
    	@PrimaryKeyJoinColumn(name="CID", referencedColumnName="CID"),
    	@PrimaryKeyJoinColumn(name="APP_ID", referencedColumnName="APP_ID")
    })
	public KrqdtAppOvertime appOvertime;
    
    public KrqdtOvertimeInput(KrqdtOvertimeInputPK pk , int startTime, int endTime, int appTime){
    	this.krqdtOvertimeInputPK = pk;
    	this.startTime = startTime;
    	this.endTime = endTime;
    	this.applicationTime = appTime;
    }
	@Override
	protected Object getKey() {
		return krqdtOvertimeInputPK;
	}
	
	public KrqdtOvertimeInput fromDomainValue(OverTimeInput overTimeInput){
		this.startTime = overTimeInput.getStartTime().v();
		this.endTime = overTimeInput.getEndTime().v();
		this.applicationTime = overTimeInput.getApplicationTime().v();
		return this;
	}
	
	public OverTimeInput toDomain(){
		return OverTimeInput.createSimpleFromJavaType(
				this.krqdtOvertimeInputPK.getCid(), 
				this.krqdtOvertimeInputPK.getAppId(), 
				this.krqdtOvertimeInputPK.getAttendanceId(), 
				this.krqdtOvertimeInputPK.getFrameNo(), 
				this.startTime, 
				this.endTime, 
				this.applicationTime, 
				this.krqdtOvertimeInputPK.getTimeItemTypeAtr());
	} 

}
