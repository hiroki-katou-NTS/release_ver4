package nts.uk.ctx.at.schedule.dom.budget.schedulevertical.verticalsetting;

import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;

/**
 * TanLV
 *
 */
@Getter
public class VerticalCalSet extends AggregateRoot {
	/* 会社ID */
    private String companyId;
    
    /*コード*/
    private VerticalCalCd verticalCalCd;
    
    /*名称*/
    private VerticalCalName verticalCalName;
    
    /*単位*/
    private Unit unit;
    
    /*利用区分*/
    private UseAtr useAtr;
    
    /*応援集計区分*/
    private AssistanceTabulationAtr assistanceTabulationAtr;
    
    private List<VerticalCalItem> verticalCalItems;
    
    public void validate(){
    	
    }

	public VerticalCalSet(String companyId, VerticalCalCd verticalCalCd, VerticalCalName verticalCalName, Unit unit, UseAtr useAtr,
			AssistanceTabulationAtr assistanceTabulationAtr, List<VerticalCalItem> verticalCalItems) {
		
		this.companyId = companyId;
		this.verticalCalCd = verticalCalCd;
		this.verticalCalName = verticalCalName;
		this.unit = unit;
		this.useAtr = useAtr;
		this.assistanceTabulationAtr = assistanceTabulationAtr;
		this.verticalCalItems = verticalCalItems;
	}
    
	public static VerticalCalSet createFromJavaType(String companyId, String verticalCalCd, String verticalCalName, int unit, int useAtr,
			int assistanceTabulationAtr, List<VerticalCalItem> verticalCalItems) {
		return new VerticalCalSet(companyId, new VerticalCalCd(verticalCalCd), new VerticalCalName(verticalCalName),
				EnumAdaptor.valueOf(unit, Unit.class),
				EnumAdaptor.valueOf(useAtr, UseAtr.class),
				EnumAdaptor.valueOf(assistanceTabulationAtr, AssistanceTabulationAtr.class),
				verticalCalItems);
	}
}
