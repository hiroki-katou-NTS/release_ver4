package nts.uk.ctx.at.record.app.find.dailyperform.remark.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.remarks.RecordRemarks;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.remarks.RemarksOfDailyAttd;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_REMARKS_NAME)
public class RemarksOfDailyDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = FAKED, listMaxLength = 5, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<DailyRemarkDto> remarks = new ArrayList<>();
	
	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.ymd;
	}

	@Override
	public List<RemarksOfDailyPerform> toDomain(String employeeId, GeneralDate date) {
		if (this.isHaveData()) {
			return remarks.stream().map(c -> new RemarksOfDailyPerform(employeeId, date, new RecordRemarks(c.getRemark()), c.getNo()))
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public static RemarksOfDailyDto getDto(List<RemarksOfDailyPerform> domain) {
		RemarksOfDailyDto dto = new RemarksOfDailyDto();
		if(domain != null && !domain.isEmpty()){
			dto.setEmployeeId(domain.get(0).getEmployeeId());
			dto.setYmd(domain.get(0).getYmd());
			dto.setRemarks(domain.stream().map(c -> new DailyRemarkDto(c.getRemarks().v(), c.getRemarkNo()))
											.collect(Collectors.toList()));
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public RemarksOfDailyDto clone() {
		RemarksOfDailyDto dto = new RemarksOfDailyDto();
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		dto.setRemarks(remarks.stream().map(c -> new DailyRemarkDto(c.getRemark(), c.getNo())).collect(Collectors.toList()));
		if(isHaveData()){
			dto.exsistData();
		}
		return dto;
	}
}
