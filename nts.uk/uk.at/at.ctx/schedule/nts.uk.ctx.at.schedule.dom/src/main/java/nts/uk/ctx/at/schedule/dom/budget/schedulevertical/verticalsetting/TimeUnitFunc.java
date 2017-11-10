package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

@AllArgsConstructor
@Getter
public class TimeUnitFunc {
	/* 会社ID */
    private String companyId;
    
    /*コード*/
    private String verticalCalCd;
    
    /* 汎用縦計項目ID */
    private String verticalCalItemId;
    
    /* 順番 */
    private int dispOrder;
    
    /* 勤怠項目ID */
    private String attendanceItemId;
    
    /* 予定項目ID */
    private String presetItemId;
    
    /* 演算子区分 */
    private OperatorAtr operatorAtr;
    
    public static TimeUnitFunc createFromJavatype(String companyId, String verticalCalCd, String verticalCalItemId,
			int dispOrder, String attendanceItemId, String presetItemId,int operatorAtr) {
		return new TimeUnitFunc(companyId, verticalCalCd, verticalCalItemId, dispOrder,attendanceItemId,
				presetItemId, EnumAdaptor.valueOf(operatorAtr, OperatorAtr.class));
	}
}
