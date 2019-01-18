package nts.uk.ctx.at.record.app.service.dailyimport;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ImportState {

	DATA_COLLECTING(0, "準備中 (1/2)"),
	BEFORE_DATA_DELETING(1, "準備中 (2/2)"),
	DATA_PROCESSING(2, "受入データ処理中"),
	PROCESS_COMPLETE(3, "受入完了");
	
	public int value;
	
	public String description;
	
}
