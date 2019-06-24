package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.ControlOfMonthlyDto;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemAtr;
import nts.uk.screen.at.app.dailyperformance.correction.dto.Constraint;
import nts.uk.screen.at.app.monthlyperformance.correction.param.PAttendanceItem;
import nts.uk.shr.com.i18n.TextResource;

/**
 * TODO
 */
@Getter
@Setter
@NoArgsConstructor
public class MPHeaderDto {
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

	private List<MPHeaderDto> group;

	private Constraint constraint;
	
	private String headerCssClass;

	private Boolean grant;
	
	private String columnCssClass;

	private static final String ADD_CHARACTER = "A";
	private static final String PX = "px";

	public MPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
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
		this.grant = false;
	}
	
	public MPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
			String ntsControl, Boolean changedByOther, Boolean changedByYou, String headerCssClass) {
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
		this.headerCssClass = headerCssClass;
		this.grant = false;
	}
	
	public MPHeaderDto(String headerText, String key, String dataType, String width, String color, boolean hidden,
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


	public static List<MPHeaderDto> GenerateFixedHeader() {
		List<MPHeaderDto> lstHeader = new ArrayList<>();

		lstHeader.add(new MPHeaderDto("ID", "id", "String", "30px", "", true, "Label", true, true));
		// G_1 状態
		// lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_21"),
		// "state", "String", "30px", "", false, "FlexImage", true, true));
		lstHeader.add(new MPHeaderDto("状<br>態", "state", "String", "30px", "", false, "FlexImage", true, true));
		String name = TextResource.localize("KMW003_22");
		String newName = name.replace("\n", "<br>");
		// G_2 アラーム/エラー
		lstHeader.add(new MPHeaderDto(newName, "error", "String", "60px", "", false, "Label", true, true));
		// G_3 社員コード
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_23"), "employeeCode", "String", "85px", "", false,
				"Label", true, true));
		// G_4 社員名
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_24"), "employeeName", "String", "102px", "", false,
				"Label", true, true));
		// G_5 個人プロフィール
		lstHeader.add(new MPHeaderDto("", "picture-person", "String", "10px", "", false, "Image", true, true));
		// G_6 本人確認
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_25"), "identify", "boolean", "35px", "", false,
				"Checkbox", true, true));
		// G_7
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_26"), "approval", "boolean", "35px", "", false,
				"Checkbox", true, true));
		// G_8 日別確認
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_27"), "dailyconfirm", "String", "64px", "", false,
				"Label", true, true));
		// G_9 日別実績の修正
		lstHeader.add(new MPHeaderDto(TextResource.localize("KMW003_28"), "dailyperformace", "String", "85px", "",
				false, "Button", true, true));
		return lstHeader;
	}

	public void setHeaderText(MPAttendanceItem param) {
		if (param.getLineBreakPosition() > 0) {
			this.headerText = param.getName() != null ? param.getName().substring(0, param.getLineBreakPosition())
					+ "<br/>" + param.getName().substring(param.getLineBreakPosition(), param.getName().length()) : "";
		} else {
			this.headerText = param.getName() != null ? param.getName() : "";
		}
	}

	public void setHeaderColor(MPAttendanceItemControl param) {
		this.color = param.getHeaderBackgroundColor();
	}

	public static MPHeaderDto createSimpleHeader(PAttendanceItem item, ControlOfMonthlyDto ctrOfMonthlyDto,
			MonthlyAttendanceItemDto maiDto) {
		String key = mergeString(ADD_CHARACTER, String.valueOf(item.getId()));
		String width = String.valueOf(item.getColumnWidth() == null ? 100 : item.getColumnWidth()) + PX;
		MPHeaderDto dto = new MPHeaderDto("", key, "String", width, "#CFF1A5", false, "", false, false);
		// set constraint
		// if (maiDto != null && maiDto.getPrimitive() != null) {
		// dto.setConstraint(new Constraint("Primitive", false,
		// getPrimitiveAllName(maiDto.getPrimitive())));
		// } else {
		int attendanceAtr = item.getAttendanceAtr();
		if (attendanceAtr == MonthlyAttendanceItemAtr.AMOUNT.value) {
			// dto.setNtsControl("TextEditorNumberSeparated");
			//dto.setConstraint(new Constraint("Currency", false, ""));
			dto.setGrant(true);
			if (maiDto.getPrimitive() != null && maiDto.getPrimitive() == 54) {
				dto.setConstraint(new Constraint("Currency", false, "").createMinMax("-999999", "999999"));
			} else if (maiDto.getPrimitive() != null && maiDto.getPrimitive() == 55) {
				dto.setConstraint(new Constraint("Currency", false, "").createMinMax("-999999999", "999999999"));
			} else {
				dto.setConstraint(new Constraint("Currency", false, "").createMinMax("-999999999", "999999999"));
			}
		} else if(attendanceAtr == MonthlyAttendanceItemAtr.CODE.value){
			List<MPHeaderDto> groups = new ArrayList<>();
			int withChild = Integer.parseInt(width.substring(0, width.length() - 2)) / 2;
			MPHeaderDto dtoG = new MPHeaderDto("コード", "Code" + item.getId(), "String", String.valueOf(withChild) + "px",
					"", false, "", "code_"+"Name"+ item.getId(), "search", false, false);
			dtoG.setConstraint(new Constraint("Primitive", isRequired(item), getPrimitiveAllName(maiDto.getPrimitive())));
			dtoG.setColor(dto.getColor());
			groups.add(dtoG);
			groups.add(new MPHeaderDto("名称", "Name" + item.getId(), "String", String.valueOf(withChild) + "px", dto.getColor(),
					false, "Link2", false, false, "center-align"));
			dto.setGroup(groups);
			dto.setConstraint(new Constraint("Primitive", false, ""));
		} else {
			dto.setConstraint(new Constraint("Primitive", false, getPrimitiveAllName(maiDto.getPrimitive())));
		}
		//
		// else if (attendanceAtr == MonthlyAttendanceItemAtr.TIME.value) {
		// // dto.setNtsControl("TextEditorTimeShortHM");
		// dto.setConstraint(new Constraint("Clock", false, ""));
		// dto.setGrant(true);
		// } else if (attendanceAtr == MonthlyAttendanceItemAtr.NUMBER.value) {
		// dto.setConstraint(new Constraint("Integer", false, ""));
		// dto.setGrant(true);
		// } else if (attendanceAtr == MonthlyAttendanceItemAtr.DAYS.value) {
		// dto.setConstraint(new Constraint("HalfInt", false, ""));
		// dto.setGrant(true);
		// }
		// else if (attendanceAtr == DailyAttendanceAtr.TimeOfDay.value) {
		// dto.setConstraint(new Constraint("TimeWithDay", false, ""));
		// }
		// }
		setShowZero(item, dto);
		// Set header text
		if (null != item.getLineBreakPosition() && item.getLineBreakPosition() > 0) {
			dto.headerText = item.getName() != null ? item.getName().substring(0, item.getLineBreakPosition()) + "<br/>"
					+ item.getName().substring(item.getLineBreakPosition(), item.getName().length()) : "";
		} else {
			dto.headerText = item.getName() != null ? item.getName() : "";
		}
		if (null != ctrOfMonthlyDto) {
			dto.setColor(ctrOfMonthlyDto.getHeaderBgColorOfMonthlyPer());
		}
		
		setAlignData(dto, attendanceAtr);
		return dto;
	}

	private static String mergeString(String... x) {
		return StringUtils.join(x);
	}

	private static String getPrimitiveAllName(Integer primitive) {
		if (primitive == null)
			return "";
		return PrimitiveValueMonthly.mapValuePrimitive.get(primitive);
	}

	private static void setShowZero(PAttendanceItem item, MPHeaderDto dto) {
		int attendanceAtr = item.getAttendanceAtr();
		if (attendanceAtr == MonthlyAttendanceItemAtr.AMOUNT.value) {
			dto.setGrant(true);
		} else if (attendanceAtr == MonthlyAttendanceItemAtr.TIME.value) {
			dto.setGrant(true);
		} else if (attendanceAtr == MonthlyAttendanceItemAtr.NUMBER.value) {
			dto.setGrant(true);
		} else if (attendanceAtr == MonthlyAttendanceItemAtr.DAYS.value) {
			dto.setGrant(true);
		}
	}
	
	private static boolean isRequired(PAttendanceItem item){
		if(MPText.ITEM_CODE_LINK.contains(item.getId())) return true;
		return false;
	}
	
	private static void setAlignData(MPHeaderDto dto, int attendanceAtr) {
		switch (MonthlyAttendanceItemAtr.valueOf(attendanceAtr)) {
		case TIME:
		case NUMBER:
		case DAYS:
		case AMOUNT:
			dto.setColumnCssClass("halign-right");
			break;

		default:
			dto.setColumnCssClass("halign-left");
			break;
		}
	}
}
