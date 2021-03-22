package nts.uk.ctx.at.shared.infra.entity.workrule.shiftmaster;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ColorCodeChar6;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.Remarks;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterDisInfor;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterName;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@Entity
@NoArgsConstructor
@Table(name = "KSHMT_SHIFT_MASTER")
@Builder
public class KshmtShiftMater extends ContractUkJpaEntity {

	@EmbeddedId
	public KshmtShiftMaterPK kshmtShiftMaterPK;

	@Column(name = "NAME")
	public String name;

	@Column(name = "COLOR")
	public String color;
	
	@Column(name = "COLOR_MOBILE")
	public String colorMobile;

	@Column(name = "NOTE")
	public String remarks;

	@Column(name = "WORKTYPE_CD")
	public String workTypeCd;

	@Column(name = "WORKTIME_CD")
	public String workTimeCd;

	protected Object getKey() {
		return kshmtShiftMaterPK;
	}

	public KshmtShiftMater(KshmtShiftMaterPK kshmtShiftMaterPK, String name, String color,String colorMobile, String remarks,
			String workTypeCd, String workTimeCd) {
		super();
		this.kshmtShiftMaterPK = kshmtShiftMaterPK;
		this.name = name;
		this.color = color;
		this.colorMobile = colorMobile;
		this.remarks = remarks;
		this.workTypeCd = workTypeCd;
		this.workTimeCd = workTimeCd;
	}

	public ShiftMaster toDomain() {
		return new ShiftMaster(kshmtShiftMaterPK.companyId, new ShiftMasterCode(kshmtShiftMaterPK.shiftMaterCode),
				new ShiftMasterDisInfor(new ShiftMasterName(this.name), new ColorCodeChar6(this.color),new ColorCodeChar6(this.colorMobile),
						this.remarks == null ? null : new Remarks(this.remarks)),
				this.workTypeCd, this.workTimeCd);
	}

	public static KshmtShiftMater toEntity(ShiftMaster domain) {
		
		KshmtShiftMater entity = KshmtShiftMater.builder()
								.kshmtShiftMaterPK(new KshmtShiftMaterPK(domain.getCompanyId(), domain.getShiftMasterCode().v()))
								.name(domain.getDisplayInfor().getName().v())
								.color(domain.getDisplayInfor().getColor().v())
								.colorMobile(domain.getDisplayInfor().getColorSmartPhone().v())
								.remarks(domain.getDisplayInfor().getRemarks().isPresent() ? domain.getDisplayInfor().getRemarks().get().v() : null)
								.workTypeCd(domain.getWorkTypeCode().v())
								.workTimeCd(domain.getWorkTimeCode() == null ? null:domain.getWorkTimeCode().v())
								.build();
								
		return entity;
	}
}
