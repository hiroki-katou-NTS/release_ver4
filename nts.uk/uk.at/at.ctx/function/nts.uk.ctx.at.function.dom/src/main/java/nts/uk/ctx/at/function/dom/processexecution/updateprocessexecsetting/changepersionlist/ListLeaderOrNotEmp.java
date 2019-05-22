package nts.uk.ctx.at.function.dom.processexecution.updateprocessexecsetting.changepersionlist;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ListLeaderOrNotEmp {
	// ・社員ID（異動者、勤務種別変更者のみ）（List）
	List<String> leaderEmpIdList = new ArrayList<>();
	// ・社員ID（異動者、勤務種別変更者のみ）（List）
	List<String> noLeaderEmpIdList = new ArrayList<>();
}
