package nts.uk.ctx.at.record.dom.divergencetime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DivergenceItemSet {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離項目ID*/
	private int divergenceItemId;
	
	public static DivergenceItemSet createSimpleFromJavaType(String companyId,
					int divTimeId,
					int divergenceItemId){
		return new DivergenceItemSet(companyId, divTimeId, divergenceItemId);
	}
}
