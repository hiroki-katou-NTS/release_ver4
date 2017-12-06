package nts.uk.ctx.at.schedule.dom.shift.specificdayset.workplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.primitives.SpecificDateItemNo;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.specificdate.primitives.SpecificName;

@Getter
@AllArgsConstructor
public class WorkplaceSpecificDateItem extends AggregateRoot {
	private String workplaceId;
	private GeneralDate specificDate;
	private SpecificDateItemNo specificDateItemNo;
	private SpecificName specificDateItemName;

	public static WorkplaceSpecificDateItem createFromJavaType(String workplaceId, GeneralDate specificDate, Integer specificDateItemNo, String specificDateItemName) {
		return new WorkplaceSpecificDateItem(
				workplaceId, 
				specificDate,
				new SpecificDateItemNo(specificDateItemNo),
				new SpecificName(specificDateItemName));
	}
}
