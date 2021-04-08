package nts.uk.ctx.at.function.dom.alarm.alarmlist;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.gul.text.IdentifierUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor		
public class AlarmExtraValueWkReDto {
	
	private String guid;
	/**職場ID*/
	private String workplaceID;
	
	/**  階層コード*/
	private String hierarchyCd;
	
	/**職場名*/
	private String workplaceName;
	
	/**社員ID*/
	private String employeeID;
	
	private String employeeCode;
	
	private String employeeName;
	
	/**アラーム値日付*/
	private String alarmValueDate;

	/**カテゴリ*/
	private Integer category;
	
	private String categoryName;
	
	/**アラーム項目*/
	private String alarmItem;
	
	/**アラーム値メッセージ*/
	private String alarmValueMessage;
	
	/**コメント*/
	private String comment;
	
	/**チェック対象値*/
	private String checkedValue;
	/**アラーム値日付　終了日*/
	private String endDate;
	
	public AlarmExtraValueWkReDto(String workplaceID, String hierarchyCd, String workplaceName, String employeeID,
			String employeeCode, String employeeName, String alarmValueDate, int category, String categoryName,
			String alarmItem, String alarmValueMessage, String comment, String checkedValue) {
		super();
		this.guid= IdentifierUtil.randomUniqueId();
		this.workplaceID = workplaceID;
		this.hierarchyCd = hierarchyCd;
		this.workplaceName = workplaceName;
		this.employeeID = employeeID;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.alarmValueDate = alarmValueDate;
		this.category = category;
		this.categoryName = categoryName;
		this.alarmItem = alarmItem;
		this.alarmValueMessage = alarmValueMessage;
		this.comment = comment;
		this.checkedValue = checkedValue;
	}

	public List<String> employeeData() {
		
		return Arrays.asList(employeeCode, workplaceName, employeeName);
	}

	public List<String> erAlData() {
		
		return Arrays.asList(employeeCode, alarmValueDate, categoryName, alarmItem, comment, guid, alarmValueMessage);
	}
}
