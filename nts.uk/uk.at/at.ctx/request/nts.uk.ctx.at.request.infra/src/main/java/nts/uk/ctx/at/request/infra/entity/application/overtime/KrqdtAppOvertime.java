package nts.uk.ctx.at.request.infra.entity.application.overtime;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 残業申請
 * @author loivt
 */
@Entity
@Table(name = "KRQDT_APP_OVERTIME")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrqdtAppOvertime extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KrqdtAppOvertimePK krqdtAppOvertimePK;
    
    @Version
	@Column(name="EXCLUS_VER")
	public Long version;
   
    @Column(name = "OVERTIME_ATR")
    private int overtimeAtr;
    
    @Column(name = "WORK_TYPE_CODE")
    private String workTypeCode;
    
    @Column(name = "SIFT_CODE")
    private String siftCode;
    
    @Column(name = "WORK_CLOCK_FROM1")
    private Integer workClockFrom1;
    
    @Column(name = "WORK_CLOCK_TO1")
    private Integer workClockTo1;
    
    @Column(name = "WORK_CLOCK_FROM2")
    private Integer workClockFrom2;
    
    @Column(name = "WORK_CLOCK_TO2")
    private Integer workClockTo2;
    
    @Column(name = "DIVERGENCE_REASON")
    private String divergenceReason;
    
    @Column(name = "FLEX_EXCESS_TIME")
    private Integer flexExcessTime;
    
    @Column(name = "OVERTIME_SHIFT_NIGHT")
    private Integer overtimeShiftNight;
    
    @OneToMany(targetEntity=KrqdtOvertimeInput.class, mappedBy="appOvertime", cascade = CascadeType.ALL)
    @JoinTable(name = "KRQDT_OVERTIME_INPUT")
	public List<KrqdtOvertimeInput> overtimeInputs;

	@Override
	protected Object getKey() {
		return krqdtAppOvertimePK;
	}

	public KrqdtAppOvertime fromDomainValue(AppOverTime appOverTime){
		this.setVersion(appOverTime.getVersion());
		this.setWorkTypeCode(appOverTime.getWorkTypeCode() == null ? null : appOverTime.getWorkTypeCode().v());
		this.setSiftCode(appOverTime.getSiftCode() == null ? null : appOverTime.getSiftCode().v());
		this.setWorkClockFrom1(appOverTime.getWorkClockFrom1());
		this.setWorkClockTo1(appOverTime.getWorkClockTo1());
		this.setWorkClockFrom2(appOverTime.getWorkClockFrom2());
		this.setWorkClockTo2(appOverTime.getWorkClockTo2());
		this.setOvertimeAtr(appOverTime.getOverTimeAtr().value);
		this.setOvertimeShiftNight(appOverTime.getOverTimeShiftNight());
		this.setFlexExcessTime(appOverTime.getFlexExessTime());
		this.setDivergenceReason(appOverTime.getDivergenceReason());
		for(int i = 0; i<appOverTime.getOverTimeInput().size(); i++){
			OverTimeInput overtimeInput = appOverTime.getOverTimeInput().get(i);
			this.getOvertimeInputs().stream().filter(
					x -> x.krqdtOvertimeInputPK.getAttendanceId()==overtimeInput.getAttendanceType().value 
					&& x.krqdtOvertimeInputPK.getFrameNo()==overtimeInput.getFrameNo()
					&& x.krqdtOvertimeInputPK.getTimeItemTypeAtr()==overtimeInput.getTimeItemTypeAtr().value)
			.findAny()
			.map(x -> {
				x.fromDomainValue(overtimeInput);
				return Optional.ofNullable(null);
			}).orElseGet(()->{
				KrqdtOvertimeInput krqdtOvertimeInput = new KrqdtOvertimeInput(
						new KrqdtOvertimeInputPK(
							appOverTime.getCompanyID(),
							appOverTime.getAppID(),
							overtimeInput.getAttendanceType().value,
							overtimeInput.getFrameNo(),
							overtimeInput.getTimeItemTypeAtr().value
						), 
						overtimeInput.getStartTime() == null ? null : overtimeInput.getStartTime().v(), 
						overtimeInput.getEndTime() == null ? null : overtimeInput.getEndTime().v(), 
						overtimeInput.getApplicationTime().v());
				this.overtimeInputs.add(krqdtOvertimeInput);
				return null;
			});
		}
		return this;
	}
	
	public AppOverTime toDomain(){
		AppOverTime appOverTime = new AppOverTime(
				this.krqdtAppOvertimePK.getCid(), 
				this.krqdtAppOvertimePK.getAppId(), 
				this.getOvertimeAtr(), 
				this.getWorkTypeCode(), 
				this.getSiftCode(), 
				this.getWorkClockFrom1(), 
				this.getWorkClockTo1(), 
				this.getWorkClockFrom2(), 
				this.getWorkClockTo2(), 
				this.getDivergenceReason(), 
				this.getFlexExcessTime(), 
				this.getOvertimeShiftNight());
		appOverTime.setOverTimeInput(this.overtimeInputs.stream().map(x -> x.toDomain()).collect(Collectors.toList()));
		appOverTime.setVersion(this.version);
		return appOverTime;
	}
    
}

