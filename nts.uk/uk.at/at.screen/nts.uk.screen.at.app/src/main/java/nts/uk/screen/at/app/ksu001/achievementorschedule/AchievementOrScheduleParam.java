package nts.uk.screen.at.app.ksu001.achievementorschedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.ksu001.displayinshift.ShiftMasterMapWithWorkStyle;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AchievementOrScheduleParam {
	public static final Integer SHIFT_MODE = 0; // シフト表示の場合
	public static final Integer WORK_MODE = 1; // 勤務表示
	public static final Integer ABBREVIATION_MODE = 2; // 略名表示の場合
	
	public GeneralDate startDate;            	 
	public GeneralDate endDate;    	
	
	public int unit;
	public String workplaceId;     	         
	public String workplaceGroupId;
	public List<String> sids;
	public List<ShiftMasterMapWithWorkStyle> listShiftMasterNotNeedGetNew;
	public boolean getActualData;

	
	// ・集計したい個人計：Optional<個人計カテゴリ>
	public Integer personalCounterOp;
	
	// ・集計したい職場計：Optional<職場計カテゴリ>
	
	public Integer workplaceCounterOp;
	
	// 締め日
	public int day;
	
	public int mode;
}
