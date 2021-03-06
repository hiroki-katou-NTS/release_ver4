package nts.uk.file.at.app.export.alarm.checkcondition;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyReportData {
	//1: コード
	private String code;
	//2: 名称
	private String name;
	//4: チェック対象範囲 雇用
	private String filterEmp;
	//5: チェック対象範囲 雇用対象
	private Optional<String> employees;
	//6: チェック対象範囲 分類
	private String filterClas;
	//7: チェック対象範囲 分類
	private Optional<String> classifications;
	//8: チェック対象範囲 職位
	private String filterJobTitles;
	//9: チェック対象範囲 職位対象
	private Optional<String> jobtitles;
	//10: チェック対象範囲 勤務種別
	private String filterWorkType;
	//11: チェック対象範囲 勤務種別対象
	private Optional<String> worktypeselections;
	//12: 日別実績のエラーアラーム
	private Optional<String> dailyErrorAlarms;
	
	private Optional<Integer> useAtrCond;
	//13: チェック条件 NO
	//14: チェック条件 名称
	private Optional<String> nameCond;
	//15: チェック条件 チェック項目
	private Optional<String> checkItem;
	//16: チェック条件 対象とする勤務種類(TargetServiceType.java)
	private Optional<String> targetServiceType;
	//17: チェック条件 勤務種類
	private Optional<String> worktypes;
	//18: チェック条件 チェック条件
	private Optional<String> targetAttendances;
	
	private Optional<Integer> conditionAtr;
	private Optional<Integer> conditionType;
	//19: チェック条件 条件
	private Optional<Integer> compareAtrInt;
	private Optional<String> compareAtr;
	//20: チェック条件 値１
	private Optional<Integer> startValue;
	//21: チェック条件 値２
	private Optional<Integer> endValue;
	//22: チェック条件 複合条件 グループ１
	private Optional<String> conditionOperatorGroup1;
	//23:チェック条件 複合条件 計算式
	//23_1
	private Optional<String> targetAttendances1Group1;
	//23_2
	private Optional<Integer> compareAtr1Group1;
	//23_3
	private Optional<Integer> conditionAtr1Group1;
	//23-4	
	private Optional<Integer> start1Group1;
	//23-5
	private Optional<Integer> end1Group1;
	//23-6
	private Optional<Integer> conditionType1Group1;
	//23-7
	private Optional<Integer> attendanceItem1Group1;
	
	//24:チェック条件 複合条件 計算式
	// 24_1
	private Optional<String> targetAttendances2Group1;
	// 24_2
	private Optional<Integer> compareAtr2Group1;
	// 24_3
	private Optional<Integer> conditionAtr2Group1;
	// 24-4
	private Optional<Integer> start2Group1;
	// 24-5
	private Optional<Integer> end2Group1;
	// 24-6
	private Optional<Integer> conditionType2Group1;
	// 24-7
	private Optional<Integer> attendanceItem2Group1;
	
	//25:チェック条件 複合条件 計算式
	// 25_1
	private Optional<String> targetAttendances3Group1;
	// 25_2
	private Optional<Integer> compareAtr3Group1;
	// 25_3
	private Optional<Integer> conditionAtr3Group1;
	// 25-4
	private Optional<Integer> start3Group1;
	// 25-5
	private Optional<Integer> end3Group1;
	// 25-6
	private Optional<Integer> conditionType3Group1;
	// 25-7
	private Optional<Integer> attendanceItem3Group1;
	
	//26: チェック条件 複合条件 グループ２
	private Optional<Integer> group2UseAtrInt;
	private Optional<String> group2UseAtr;
	//27: チェック条件 複合条件 グループ2
	private Optional<String> conditionOperatorGroup2;
	// 28:チェック条件 複合条件 計算式
	// 28_1
	private Optional<String> targetAttendances1Group2;
	// 28_2
	private Optional<Integer> compareAtr1Group2;
	// 28_3
	private Optional<Integer> conditionAtr1Group2;
	// 28-4
	private Optional<Integer> start1Group2;
	// 28-5
	private Optional<Integer> end1Group2;
	// 28-6
	private Optional<Integer> conditionType1Group2;
	// 28-7
	private Optional<Integer> attendanceItem1Group2;

	// 29:チェック条件 複合条件 計算式
	// 29_1
	private Optional<String> targetAttendances2Group2;
	// 29_2
	private Optional<Integer> compareAtr2Group2;
	// 29_3
	private Optional<Integer> conditionAtr2Group2;
	// 29-4
	private Optional<Integer> start2Group2;
	// 29-5
	private Optional<Integer> end2Group2;
	// 29-6
	private Optional<Integer> conditionType2Group2;
	// 29-7
	private Optional<Integer> attendanceItem2Group2;

	// 30:チェック条件 複合条件 計算式
	// 30_1
	private Optional<String> targetAttendances3Group2;
	// 30_2
	private Optional<Integer> compareAtr3Group2;
	// 30_3
	private Optional<Integer> conditionAtr3Group2;
	// 30-4
	private Optional<Integer> start3Group2;
	// 30-5
	private Optional<Integer> end3Group2;
	// 30-6
	private Optional<Integer> conditionType3Group2;
	// 30-7
	private Optional<Integer> attendanceItem3Group2;
	
	private String insDate;
	//31: チェック条件 複合条件 グループ1とグループ2の条件
	private Optional<String> operatorBetweenGroups;
	//32: チェック条件 表示するメッセージ
	private Optional<String> message;
	//33: 固定チェック条件
	private Optional<String> fixedCheckCond;
}
