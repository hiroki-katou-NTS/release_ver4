/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.scherec.totaltimes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalSubjects;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.UseAtr;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.WorkTypeAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SaveTotalTimesCommandHandler.
 */
@Stateless
public class SaveTotalTimesCommandHandler extends CommandHandler<TotalTimesCommand> {

	/** The total times repo. */
	@Inject
	private TotalTimesRepository totalTimesRepo;

	/** The work time setting repository. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	/** The work type repository. */
	@Inject
	private WorkTypeRepository workTypeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<TotalTimesCommand> context) {
		// Get context info
		String companyId = AppContexts.user().companyId();

		// Get command
		TotalTimesCommand command = context.getCommand();

		// Find details
		Optional<TotalTimes> result = this.totalTimesRepo.getTotalTimesDetail(companyId,
				command.getTotalCountNo());

		// Check exist
		if (!result.isPresent()) {
			throw new BusinessException("Msg_3");
		}
		// ignore data when choose 使用しない
		this.ignoredDataIfNeed(command, result.get());

		// Convert to domain
		TotalTimes totalTimes = command.toDomain(companyId);

		// valid list total subjects
		this.validTotalSubject(companyId, totalTimes);

		// Alway has 30 items and allow update only
		this.totalTimesRepo.update(totalTimes);
	}

	/**
	 * Valid total subject.
	 *
	 * @param totalTime
	 *            the total time
	 */
	private void validTotalSubject(String companyId, TotalTimes totalTime) {
		totalTime.getTotalSubjects().stream()
				.filter(item -> item.getWorkTypeAtr().equals(WorkTypeAtr.WORKTYPE))
				.forEach(item -> {
					if (!workTypeRepository.findByPK(companyId, item.getWorkTypeCode().v())
							.isPresent()) {
						throw new BusinessException("Msg_216", "KMK009_8");
					}
				});

		totalTime.getTotalSubjects().stream()
				.filter(item -> item.getWorkTypeAtr().equals(WorkTypeAtr.WORKINGTIME))
				.forEach(item -> {
					if (!workTimeSettingRepository.findByCode(companyId, item.getWorkTypeCode().v())
							.isPresent()) {
						throw new BusinessException("Msg_216", "KMK009_9");
					}
				});
	}

	/**
	 * Ignored data if need.
	 *
	 * @param command
	 *            the command
	 * @param totalTimeDb
	 *            the total time db
	 */
	private void ignoredDataIfNeed(TotalTimesCommand command, TotalTimes totalTimeDb) {
		/*
		 * In case UseAtr.Use.value or uncheck both LowerLimitSettingAtr and
		 * UpperLimitSettingAtr will value in DB
		 */
		if (command.getTotalCondition().getLowerLimitSettingAtr() == 0
				&& command.getTotalCondition().getUpperLimitSettingAtr() == 0) {
			command.getTotalCondition()
					.setAttendanceItemId(totalTimeDb.getTotalCondition().getAtdItemId());
		}

		if (command.getUseAtr() == UseAtr.Use.value) {
			return;
		}

		command.setTotalCountNo(totalTimeDb.getTotalCountNo());
		command.setCountAtr(totalTimeDb.getCountAtr().value);
		command.setTotalTimesName(totalTimeDb.getTotalTimesName().v());
		command.setTotalTimesABName(totalTimeDb.getTotalTimesABName().v());
		command.setSummaryAtr(totalTimeDb.getSummaryAtr().value);

		TotalConditionDto totalConditionDto = new TotalConditionDto();
		totalConditionDto.setUpperLimitSettingAtr(
				totalTimeDb.getTotalCondition().getUpperLimitSettingAtr().value);
		totalConditionDto.setLowerLimitSettingAtr(
				totalTimeDb.getTotalCondition().getLowerLimitSettingAtr().value);
		totalConditionDto.setThresoldUpperLimit(
				totalTimeDb.getTotalCondition().getThresoldUpperLimit().v().longValue());
		totalConditionDto.setThresoldLowerLimit(
				totalTimeDb.getTotalCondition().getThresoldLowerLimit().v().longValue());
		totalConditionDto.setAttendanceItemId(totalTimeDb.getTotalCondition().getAtdItemId());
		command.setTotalCondition(totalConditionDto);

		List<TotalSubjectsDto> listTotalSubjects = new ArrayList<>();
		for (TotalSubjects totalObj : totalTimeDb.getTotalSubjects()) {
			TotalSubjectsDto dto = new TotalSubjectsDto();
			dto.setWorkTypeAtr(totalObj.getWorkTypeAtr().value);
			dto.setWorkTypeCode(totalObj.getWorkTypeCode().v());
			listTotalSubjects.add(dto);
		}
		command.setListTotalSubjects(listTotalSubjects);
	}
}
