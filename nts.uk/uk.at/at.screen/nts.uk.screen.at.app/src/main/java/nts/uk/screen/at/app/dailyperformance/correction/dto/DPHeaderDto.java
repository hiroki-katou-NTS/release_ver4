/**
 * 2:20:39 PM Sep 5, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;
import nts.uk.screen.at.app.dailyperformance.correction.dto.primitive.PrimitiveValueDaily;
import nts.uk.screen.at.app.dailyperformance.correction.dto.type.TypeLink;
import nts.uk.shr.com.i18n.TextResource;

/**
 * @author hungnm
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
	private String headerCssClass;

	private DPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
			String ntsControl, Boolean changedByOther, Boolean changedByYou, String headerCss) {
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
		this.headerCssClass = headerCss;
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

	public static DPHeaderDto createSimpleHeader(String companyId, String key, String width,
			Map<Integer, DPAttendanceItem> mapDP) {
		DPHeaderDto dto = new DPHeaderDto("", key, "String", width, "", false, "", false, false, "center-align");
		// optionalRepo.findByListNos(companyId, optionalitemNos)
		DPAttendanceItem item = mapDP.get(Integer.parseInt(getCode(key)));
		int attendanceAtr = item.getAttendanceAtr();
		if (attendanceAtr == DailyAttendanceAtr.Code.value) {
			List<DPHeaderDto> groups = new ArrayList<>();
			int withChild = Integer.parseInt(width.substring(0, width.length() - 2)) / 2;
			DPHeaderDto dtoG = new DPHeaderDto("コード", "Code" + getCode(key), "String", String.valueOf(withChild) + "px",
					"", false, "", "code_"+"Name"+ getCode(key), "search", false, false);
			dtoG.setConstraint(new Constraint("Primitive", false, getPrimitiveAllName(item)));
			groups.add(dtoG);
			groups.add(new DPHeaderDto("名称", "Name" + getCode(key), "String", String.valueOf(withChild) + "px", "",
					false, "Link2", false, false, "center-align"));
			dto.setGroup(groups);
			dto.setConstraint(new Constraint("Primitive", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.Classification.value) {
			List<DPHeaderDto> groups = new ArrayList<>();
			int withChild = Integer.parseInt(width.substring(0, width.length() - 2)) / 2;
			groups.add(new DPHeaderDto("NO", "NO" + getCode(key), "number", String.valueOf(withChild) + "px", "", false,
					"", "comboCode_"+"Name"+ getCode(key), "", false, false));
			if (item.getTypeGroup() == TypeLink.CALC.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "number",
						String.valueOf(withChild) + "px", "", false, "ComboboxCalc", false, false, "center-align");
				groups.get(0).setConstraint(new Constraint("Integer", true, "2"));
				groups.add(dtoG);
			}
			if (item.getTypeGroup() == TypeLink.REASON_GO_OUT.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "number",
						String.valueOf(withChild) + "px", "", false, "ComboboxReason", false, false, "center-align");
				groups.add(dtoG);
				groups.get(0).setConstraint(new Constraint("Integer", true, "3"));
			}
			if (item.getTypeGroup() == TypeLink.DOWORK.value) {
				DPHeaderDto dtoG = new DPHeaderDto("名称", "Name" + getCode(key), "number",
						String.valueOf(withChild) + "px", "", false, "ComboboxDoWork", false, false, "center-align");
				groups.add(dtoG);
				groups.get(0).setConstraint(new Constraint("Integer", true, "1"));
			}
			dto.setGroup(groups);
			dto.setConstraint(new Constraint("Combo", true, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.AmountOfMoney.value) {
			dto.setConstraint(new Constraint("Currency", false, ""));
		} else if (attendanceAtr == DailyAttendanceAtr.Time.value) {
			if(item.getPrimitive() != null && item.getPrimitive() == 1){
				dto.setConstraint(new Constraint("Clock", false, "").createMinMax("00:00", "48:00"));
			}else{
				dto.setConstraint(new Constraint("Clock", false, "").createMinMax("-48:00", "48:00"));
			}
			//dto.setConstraint(new Constraint("Primitive", false, getPrimitiveAllName(item)));
		} else if (attendanceAtr == DailyAttendanceAtr.NumberOfTime.value) {
			dto.setConstraint(new Constraint("Primitive", false, getPrimitiveAllName(item)));
		} else if (attendanceAtr == DailyAttendanceAtr.TimeOfDay.value) {
			dto.setConstraint(new Constraint("TimeWithDay", false, ""));
		} else if(attendanceAtr == DailyAttendanceAtr.Charater.value){
			dto.setConstraint(new Constraint("Primitive", false, getPrimitiveAllName(item)));
		}
		return dto;
	}

	public static DPHeaderDto addHeaderApplication() {
		return new DPHeaderDto(TextResource.localize("KDW003_63"), "Application", "String", "90px", "", false, "Button",
				false, false, "center-align");
	}

	public static DPHeaderDto addHeaderSubmitted() {
		return new DPHeaderDto(TextResource.localize("KDW003_62"), "Submitted", "String", "90px", "", false, "Label",
				false, false, "center-align");
	}

	public static DPHeaderDto addHeaderApplicationList() {
		return new DPHeaderDto(TextResource.localize("KDW003_110"), "ApplicationList", "String", "90px", "", false,
				"ButtonList", false, false, "center-align");
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
		lstHeader.add(new DPHeaderDto("ID", "id", "String", "30px", "", true, "Label", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto("状<br/>態", "state", "String", "30px", "", false, "FlexImage", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto("ER/AL", "error", "String", "60px", "", false, "Label", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_41"), "date", "String", "90px", "", false, "Label",
				true, true, "center-align"));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_42"), "sign", "boolean", "35px", "", false,
				"Checkbox", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_32"), "employeeCode", "String", "120px", "", false,
				"Label", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto(TextResource.localize("KDW003_33"), "employeeName", "String", "190px", "", false,
				"Label", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto("", "picture-person", "String", "35px", "", false, "Image", true, true, "center-align"));
		lstHeader.add(new DPHeaderDto(TextResource.localize("承認"), "approval", "boolean", "35px", "", false, "Checkbox",
				true, true, "center-align"));
		return lstHeader;
	}

//	private static String getPrimitiveName(DPAttendanceItem item) {
//		if (item.getTypeGroup() != null) {
//			switch (item.getTypeGroup()) {
//			case 1:
//				return "WorkTypeCode";
//			case 2:
//				return "WorkTimeCode";
//			case 3:
//				return "WorkLocationCD";
//			case 4:
//				return "DiverdenceReasonCode";
//			case 5:
//				return "WorkplaceCode";
//			case 6:
//				return "ClassificationCode";
//			case 7:
//				return "JobTitleCode";
//			case 8:
//				return "EmploymentCode";
//			default:
//				return "";
//			}
//		} else {
//			return "WorkTypeCode";
//		}
//	}

	private static String getPrimitiveAllName(DPAttendanceItem item) {
		if(item.getPrimitive() == null) return "";
		return PrimitiveValueDaily.mapValuePrimitive.get(item.getPrimitive());
	}
}
