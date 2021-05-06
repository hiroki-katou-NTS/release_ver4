package nts.uk.screen.at.ws.kdw.kdw013;

import java.util.List;

import lombok.Getter;

/**
 * 
 * @author tutt
 *
 */
@Getter
public class AddAttendanceTimeZoneParam {
	/** 対象者 */
	private String employeeId;
	
	/** 編集状態<Enum.日別勤怠の編集状態> */
	private int editStateSetting;
	
	/** List<年月日,List<作業詳細>> */
	private List<WorkDetailDto> workDetails;
}
