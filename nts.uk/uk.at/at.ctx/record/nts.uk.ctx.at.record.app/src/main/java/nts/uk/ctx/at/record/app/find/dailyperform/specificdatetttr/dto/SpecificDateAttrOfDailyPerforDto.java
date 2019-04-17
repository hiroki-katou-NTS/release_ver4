package nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.enums.SpecificDateAttr;
import nts.uk.ctx.at.record.dom.raisesalarytime.primitivevalue.SpecificDateItemNo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_SPECIFIC_DATE_ATTR_NAME)
public class SpecificDateAttrOfDailyPerforDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;

	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ATTRIBUTE, 
			listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<SpecificDateAttrDto> sepecificDateAttrs;

	public static SpecificDateAttrOfDailyPerforDto getDto(SpecificDateAttrOfDailyPerfor domain) {
		SpecificDateAttrOfDailyPerforDto dto = new SpecificDateAttrOfDailyPerforDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setSepecificDateAttrs(ConvertHelper.mapTo(domain.getSpecificDateAttrSheets(), (c) -> {
				return new SpecificDateAttrDto(
						c.getSpecificDateAttr() == null ? 0 : c.getSpecificDateAttr().value, 
						c.getSpecificDateItemNo().v().intValue());
			}));
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public SpecificDateAttrOfDailyPerforDto clone() {
		SpecificDateAttrOfDailyPerforDto dto = new SpecificDateAttrOfDailyPerforDto();
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		dto.setSepecificDateAttrs(sepecificDateAttrs == null ? null : sepecificDateAttrs.stream().map(c -> c.clone()).collect(Collectors.toList()));
		if (isHaveData()) {
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.ymd;
	}

	@Override
	public SpecificDateAttrOfDailyPerfor toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new SpecificDateAttrOfDailyPerfor(emp,
				ConvertHelper.mapTo(sepecificDateAttrs,
						(c) -> new SpecificDateAttrSheet(new SpecificDateItemNo(c.getNo()),
								c.getSpecificDate() == SpecificDateAttr.NOT_USE.value 
										? SpecificDateAttr.NOT_USE : SpecificDateAttr.USE)),
						date);
	}
}
