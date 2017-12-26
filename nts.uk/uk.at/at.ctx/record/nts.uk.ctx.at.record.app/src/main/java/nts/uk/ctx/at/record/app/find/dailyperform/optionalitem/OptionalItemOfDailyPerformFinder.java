package nts.uk.ctx.at.record.app.find.dailyperform.optionalitem;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemOfDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemValueDto;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;

/** 日別実績の任意項目 Finder */
@Stateless
public class OptionalItemOfDailyPerformFinder extends FinderFacade {

	@Inject
	private AnyItemValueOfDailyRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public OptionalItemOfDailyPerformDto find(String employeeId, GeneralDate baseDate) {
		OptionalItemOfDailyPerformDto dto = new OptionalItemOfDailyPerformDto();
		AnyItemValueOfDaily domain = this.repo.find(employeeId, baseDate).orElse(null);
		if (domain != null) {
			dto.setOptionalItems(ConvertHelper.mapTo(domain.getItems(), (c) -> {
				Integer value = c.getAmount().isPresent() ? c.getAmount().get().v().intValue()
						: c.getTime().isPresent() ? c.getTime().get().valueAsMinutes() : c.getTimes().get().v();
				return new OptionalItemValueDto(value, c.getItemNo().v());
			}));
		}
		return dto;
	}

}
