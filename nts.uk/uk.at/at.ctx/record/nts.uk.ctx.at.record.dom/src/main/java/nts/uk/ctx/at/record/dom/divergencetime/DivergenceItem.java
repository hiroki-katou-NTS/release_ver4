package nts.uk.ctx.at.record.dom.divergencetime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;

@Getter
@Setter
@AllArgsConstructor
public class DivergenceItem {
	/*会社ID*/
	private String companyId;
	/*乖離項目ID*/
	private int id;
	/*乖離項目名称*/
	private DivergenceTimeName name;
	/*内部ID*/
	private int dislayNumber;
	/*使用区分*/
	private UseSetting useAtr;
	/*乖離項目属性*/	
	private AttendanceAtr attendanceAtr;
	public static DivergenceItem createSimpleFromJavaType(String companyId,
			int divItemId,
			String divItemName,
			int dislayNumber,
			int useAtr,
			int attendanceAtr){
		return new DivergenceItem(companyId,
				divItemId,
				new DivergenceTimeName(divItemName),
				dislayNumber,
				EnumAdaptor.valueOf(useAtr,UseSetting.class),
				EnumAdaptor.valueOf(attendanceAtr,AttendanceAtr.class));
	}
}
