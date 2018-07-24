package nts.uk.screen.at.app.dailyperformance.correction.dto.primitive;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrimitiveValueDaily {
	
	AttendanceTime(1, "勤怠時間", "AttendanceTime"),
	
	AttendanceTimeOfExistMinus(2, "勤怠時間（マイナス有り）", "AttendanceTimeOfExistMinus"),
	
	WorkTimes(3, "勤務回数", "WorkTimes"),
	
	WorkTypeCode(4, "勤務種類コード", "WorkTypeCode"),
	
	WorkTimeCode(5, "就業時間帯コード", "WorkTimeCode"),
	
	WorkLocationCD(6, "勤務場所コード", "WorkLocationCD"),
	
	EmploymentCode(7, "雇用区分コード", "EmploymentCode"),
	
	ClassificationCode(8, "分類コード", "ClassificationCode"),
	
	JobTitleCode(9, "職位コード", "JobTitleCode"),
	
	WorkplaceCode(10, "職場コード", "WorkplaceCode"),
	
	DivergenceReasonContent(11, "乖離理由", "DivergenceReasonContent"),
	
	BreakTimeGoOutTimes(12, "休憩外出回数", "BreakTimeGoOutTimes"),
	
	RecordRemarks(13, "実績の備考", "RecordRemarks"),
	
	DiverdenceReasonCode(14, "選択肢コード", "DiverdenceReasonCode"),
	
	TimeWithDayAttr(15, "時刻（日区分付き）", "TimeWithDayAttr");
	
	public final int value;
	
	public final String name;
	
	public final String primitive;
	
	public final static PrimitiveValueDaily[] values = PrimitiveValueDaily.values();
	
	public final static Map<Integer, String> mapValuePrimitive =  IntStream.range(0, values.length).boxed().collect(Collectors.toMap(x -> values[x].value, x -> values[x].primitive));
	
}
