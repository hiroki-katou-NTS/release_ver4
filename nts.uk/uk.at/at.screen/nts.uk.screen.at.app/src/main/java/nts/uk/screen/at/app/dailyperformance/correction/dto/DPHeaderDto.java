/**
 * 2:20:39 PM Sep 5, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.shared.dom.attendance.AttendanceAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;
import nts.uk.screen.at.app.dailyperformance.correction.dto.type.TypeLink;
import nts.uk.shr.com.i18n.TextResource;

/**
 * @author hungnm
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DPHeaderDto {

	private String headerText;

	private String key;

	private String dataType;

	private String width;

	private String color;

	private boolean hidden;

	private String ntsControl;

	private String ntsType;

	private String onChange;

	private Boolean changedByOther;

	private Boolean changedByYou;

	private List<DPHeaderDto> group;

	private Constraint constraint;

	private DPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
			String ntsControl, Boolean changedByOther, Boolean changedByYou) {
		super();
		this.headerText = headerText;
		this.key = key;
		this.dataType = dataType;
		this.width = width;
		this.color = color;
		this.hidden = hidden;
		this.ntsControl = ntsControl;
		this.changedByOther = changedByOther;
		this.changedByYou = changedByYou;
		this.group = new ArrayList<>();
	}

	private DPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
			String ntsControl, String ntsType, String onChange, Boolean changedByOther, Boolean changedByYou) {
		super();
		this.headerText = headerText;
		this.key = key;
		this.dataType = dataType;
		this.width = width;
		this.color = color;
		this.hidden = hidden;
		this.ntsControl = ntsControl;
		this.ntsType = ntsType;
		this.onChange = onChange;
		this.changedByOther = changedByOther;
		this.changedByYou = changedByYou;
		this.group = new ArrayList<>();
	}

	public static DPHeaderDto createSimpleHeader(String key, String width, Map<Integer, DPAttendanceItem> mapDP) {
		DPHeaderDto dto = new DPHeaderDto("", key, "String", width, "", false, "", false, false);
		DPAttendanceItem item = mapDP.get(Integer.parseInt(getCode(key)));
		int attendanceAtr = item.getAttendanceAtr();
		if (attendanceAtr == DailyAttendanceAtr.Code.value) {
			List<DPHeaderDto> groups = new ArrayList<>();
			int withChild = Integer.parseInt(width.substring(0, width.length() - 2)) / 2;
			DPHeaderDto dtoG = new DPHeaderDto("コード", "Code" + getCode(key), "String", String.valueOf(withChild) + "px",
					"", false, "", "code", "search", false, false);
			dtoG.setConstraint(new Constraint("Primitive", false, getPrimitiveName(item)));
			groups.add(dtoG);
			groups.add(new DPHeaderDto("名称", "Name" + getCode(key), "String", String.valueOf(withChild) + "px", "",
					false, "Link2", false, false));
			dto.setGroup(groups);
			dto.setConstraint(new Constraint("Primitive", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.Classification.value) {
			List<DPHeaderDto> groups = new ArrayList<>();
			int withChild = Integer.parseInt(width.substring(0, width.length() - 2)) / 2;
			groups.add(new DPHeaderDto("NO", "NO" + getCode(key), "number", String.valueOf(withChild) + "px", "", false,
					"", "comboCode", "", false, false));
			if (item.getTypeGroup() == TypeLink.CALC.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "String",
						String.valueOf(withChild) + "px", "", false, "ComboboxCalc", false, false);
				groups.get(0).setConstraint(new Constraint("Integer", false, "2"));
				groups.add(dtoG);
			}
			if (item.getTypeGroup() == TypeLink.REASON_GO_OUT.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "String",
						String.valueOf(withChild) + "px", "", false, "ComboboxReason", false, false);
				groups.add(dtoG);
				groups.get(0).setConstraint(new Constraint("Integer", false, "3"));
			}
			if (item.getTypeGroup() == TypeLink.DOWORK.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "String",
						String.valueOf(withChild) + "px", "", false, "ComboboxDoWork", false, false);
				groups.add(dtoG);
				groups.get(0).setConstraint(new Constraint("Integer", false, "1"));
			}
			dto.setGroup(groups);
			dto.setConstraint(new Constraint("Combo", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.AmountOfMoney.value) {
			// dto.setNtsControl("TextEditorNumberSeparated");
			dto.setConstraint(new Constraint("Currency", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.Time.value) {
			// dto.setNtsControl("TextEditorTimeShortHM");
			dto.setConstraint(new Constraint("Clock", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.NumberOfTime.value) {
			dto.setConstraint(new Constraint("Integer", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.TimeOfDay.value) {
			dto.setConstraint(new Constraint("TimeWithDay", false, ""));
		}
		return dto;
	}

	public static DPHeaderDto addHeaderApplication() {
		return new DPHeaderDto(TextResource.localize("KDW003_63"), "Application", "String", "90px", "", false, "Button",
				false, false);
	}

	public static DPHeaderDto addHeaderSubmitted() {
		return new DPHeaderDto(TextResource.localize("KDW003_62"), "Submitted", "String", "90px", "", false, "Label",
				false, false);
	}

	private static String getCode(String key) {
		return key.trim().substring(1, key.trim().length());
	}

	public void setHeaderText(DPAttendanceItem param) {
		if (param.getLineBreakPosition() > 0) {
			this.headerText = param.getName() != null ? param.getName().substring(0, param.getLineBreakPosition())
					+ "<br/>" + param.getName().substring(param.getLineBreakPosition(), param.getName().length()) : "";
		} else {
			this.headerText = param.getName() != null ? param.getName() : "";
		}
	}

	public void setHeaderColor(DPAttendanceItemControl param) {
		this.color = param.getHeaderBackgroundColor();
	}

	public static List<DPHeaderDto> GenerateFixedHeader() {
		List<DPHeaderDto> lstHeader = new ArrayList<>();
		lstHeader.add(new DPHeaderDto("ID", "id", "String", "30px", "", false, "Label", true, true));
		lstHeader.add(new DPHeaderDto("状<br/>態", "state", "String", "30px", "", false, "FlexImage", true, true));
		lstHeader.add(new DPHeaderDto("ER/AL", "error", "String", "60px", "", false, "Label", true, true));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_41"), "date", "String", "90px", "", false, "Label",
				true, true));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_42"), "sign", "boolean", "35px", "", false,
				"Checkbox", true, true));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_32"), "employeeCode", "String", "120px", "", false,
				"Label", true, true));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_33"), "employeeName", "String", "190px", "", false,
				"Label", true, true));
		lstHeader.add(new DPHeaderDto("", "picture-person", "String", "35px", "", false, "Image", true, true));
		return lstHeader;
	}

	private static String getPrimitiveName(DPAttendanceItem item){
		switch (item.getTypeGroup()) {
		case 1:
			return "WorkTypeCode";
		case 2:
			return "WorkTimeCode";
		case 3:
			return "WorkLocationCD";
		case 4:
			return "DiverdenceReasonCode";
		case 5:
			return "WorkplaceCode";
		case 6:
			return "ClassificationCode";
		case 7:
			return "JobTitleCode";
		case 8:
			return "EmploymentCode";
		default:
			return "";
		}
	}

}
