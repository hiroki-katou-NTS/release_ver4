package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChangeSPR {
	private boolean change31;
	private boolean change34;

	private String rowId31;

	private String rowId34;

	private boolean showPrincipal;

	private boolean showSupervisor;
	
	private String messageIdError;
	
	public ChangeSPR(boolean change31, boolean change34) {
		super();
		this.change31 = change31;
		this.change34 = change34;
	}

	public ChangeSPR setRow31(String rowId31) {
		this.rowId31 = rowId31;
		this.rowId34 = rowId31;
		return this;
	}

	public ChangeSPR setRow34(String rowId34) {
		this.rowId34 = rowId34;
		this.rowId31 = rowId34;
		return this;
	}
	
	public ChangeSPR setPrincipal(boolean showPrincipal) {
		this.showPrincipal = showPrincipal;
		return this;
	}

	public ChangeSPR setSupervisor(boolean showSupervisor) {
		this.showSupervisor = showSupervisor;
		return this;
	}


}
