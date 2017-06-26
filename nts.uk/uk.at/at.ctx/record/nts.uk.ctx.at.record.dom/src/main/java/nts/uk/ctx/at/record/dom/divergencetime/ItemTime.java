package nts.uk.ctx.at.record.dom.divergencetime;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

@Getter
public class ItemTime extends AggregateRoot {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*乖離時間名称*/
	private int divTimeName;
	
	public ItemTime(String companyId, int divTimeId, int divTimeName) {
		super();
		this.companyId = companyId;
		this.divTimeId = divTimeId;
		this.divTimeName = divTimeName;
	}

}
