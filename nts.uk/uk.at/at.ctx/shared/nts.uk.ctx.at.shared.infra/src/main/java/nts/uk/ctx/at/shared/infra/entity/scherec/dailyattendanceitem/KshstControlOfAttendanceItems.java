package nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.ControlOfAttendanceItems;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.TimeInputUnit;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.primitivevalue.HeaderBackgroundColor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "KSHST_ATD_ITEM_CONTROL")
public class KshstControlOfAttendanceItems extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KshstControlOfAttendanceItemsPK kshstControlOfAttendanceItemsPK;
	
	@Column(name = "HEADER_BACKGROUND_COLOR")
	public String headerBgColorOfDailyPer;
	
	@Column(name = "TIME_INPUT_UNIT")
	public int inputUnitOfTimeItem;
	
	
	@Override
	protected Object getKey() {
		return kshstControlOfAttendanceItemsPK;
	}

	public KshstControlOfAttendanceItems(KshstControlOfAttendanceItemsPK kshstControlOfAttendanceItemsPK, String headerBgColorOfDailyPer, int inputUnitOfTimeItem) {
		super();
		this.kshstControlOfAttendanceItemsPK = kshstControlOfAttendanceItemsPK;
		this.headerBgColorOfDailyPer = headerBgColorOfDailyPer;
		this.inputUnitOfTimeItem = inputUnitOfTimeItem;
		
	}
	
	public static KshstControlOfAttendanceItems toEntity(ControlOfAttendanceItems domain) {
		return new  KshstControlOfAttendanceItems(
				new KshstControlOfAttendanceItemsPK(domain.getCompanyID(),domain.getItemDailyID()),
				domain.getHeaderBgColorOfDailyPer().isPresent()?domain.getHeaderBgColorOfDailyPer().get().v():null,
				domain.getInputUnitOfTimeItem().isPresent()?domain.getInputUnitOfTimeItem().get().value:null
				);
	}
	
	public ControlOfAttendanceItems toDomain() {
		return new  ControlOfAttendanceItems(
				this.kshstControlOfAttendanceItemsPK.companyID,
				this.kshstControlOfAttendanceItemsPK.itemDailyID,
				new HeaderBackgroundColor(this.headerBgColorOfDailyPer),
				EnumAdaptor.valueOf(this.inputUnitOfTimeItem, TimeInputUnit.class)
				);
	}



	

}
