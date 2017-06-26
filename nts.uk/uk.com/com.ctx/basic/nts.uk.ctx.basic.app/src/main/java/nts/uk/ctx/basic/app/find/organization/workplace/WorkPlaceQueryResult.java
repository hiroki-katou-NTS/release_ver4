package nts.uk.ctx.basic.app.find.organization.workplace;

import java.util.List;

import lombok.Data;

@Data
public class WorkPlaceQueryResult {

	private List<WorkPlaceHistoryDto> histories;
	
	private List<WorkPlaceDto> workPlaces;
	
	private WorkPlaceMemoDto memo;

}
