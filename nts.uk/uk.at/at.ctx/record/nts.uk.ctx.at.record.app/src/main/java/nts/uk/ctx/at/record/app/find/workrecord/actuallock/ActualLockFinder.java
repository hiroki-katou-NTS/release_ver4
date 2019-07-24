/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.workrecord.actuallock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLock;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistory;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockHistoryRepository;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockRepository;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ActualLockFinder.
 */
@Stateless
public class ActualLockFinder {

	/** The actual lock repo. */
	@Inject
	private ActualLockRepository actualLockRepo;

	/** The closure repo. */
	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private ClosureService closureService;

	/** The actual lock hist repo. */
	@Inject
	private ActualLockHistoryRepository actualLockHistRepo;

	@Inject
	private EmpEmployeeAdapter employeeAdapter;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<ActualLockFinderDto> findAll() {
		// Get LoginUserContext
		LoginUserContext loginUserContext = AppContexts.user();
		// CompanyId
		String companyId = loginUserContext.companyId();

		// Target List
		List<ActualLockFinderDto> actualList = new ArrayList<>();

		// FindAll Closure
		List<Closure> closureList = this.closureRepo.findAll(companyId);

		List<Closure> filtedList = closureList.stream().filter(closure -> {
			Optional<ClosureHistory> closureHistOpt = this.closureRepo.findBySelectedYearMonth(companyId,
					closure.getClosureId().value, closure.getClosureMonth().getProcessingYm().v());
			// Check exist
			if (!closureHistOpt.isPresent()) {
				return false;
			} else {
				return (closure.getUseClassification().value == UseClassification.UseClass_Use.value);
			}
		}).collect(Collectors.toList());

		// Check isEmpty
		if (CollectionUtil.isEmpty(filtedList)) {
			throw new BusinessException("Msg_183");
		} else {
			filtedList.stream().forEach(c -> {
				ActualLockFinderDto dto = new ActualLockFinderDto();
				// Find ClosureHistory
				Optional<ClosureHistory> closureHistOpt = this.closureRepo.findBySelectedYearMonth(companyId,
						c.getClosureId().value, c.getClosureMonth().getProcessingYm().v());
				// Find ActualLock
				Optional<ActualLock> actualLockOpt = this.actualLockRepo.findById(companyId, c.getClosureId().value);

				// Get ClosurePeriod to get StartDate, EndDate
				DatePeriod datePeriod = closureService.getClosurePeriod(c.getClosureId().value,
						c.getClosureMonth().getProcessingYm());
				
				// Set ActualLockFinderDto
				dto.setClosureId(c.getClosureId());
				dto.setClosureName(closureHistOpt.get().getClosureName().v());
				dto.setYearMonth(c.getClosureMonth().getProcessingYm().v());
				// Set Period: StartDate, EndDate
				dto.setStartDate(datePeriod.start().toString());
				dto.setEndDate(datePeriod.end().toString());

				dto.setDailyLockState(
						actualLockOpt.isPresent() ? actualLockOpt.get().getDailyLockState() : LockStatus.UNLOCK);
				dto.setMonthlyLockState(
						actualLockOpt.isPresent() ? actualLockOpt.get().getMonthlyLockState() : LockStatus.UNLOCK);

				// Add ActualLockFinderDto to Target List
				actualList.add(dto);
			});
		}

		return actualList;
	}

	/**
	 * Find by id.
	 *
	 * @param closureId
	 *            the closure id
	 * @return the actual lock find dto
	 */
	public ActualLockFindDto findById(int closureId) {
		// Get LoginUserContext
		LoginUserContext loginUserContext = AppContexts.user();

		// CompanyId
		String companyId = loginUserContext.companyId();

		ActualLockFindDto dto = new ActualLockFindDto();

		// Find ActualLock By ClosureId
		Optional<ActualLock> actualLockOpt = this.actualLockRepo.findById(companyId, closureId);
		if (!actualLockOpt.isPresent()) {
			return ActualLockFindDto.builder().closureId(closureId).dailyLockState(LockStatus.UNLOCK.value)
					.monthlyLockState((LockStatus.UNLOCK.value)).build();
		}
		// Save To Memento
		actualLockOpt.get().saveToMemento(dto);
		return dto;
	}

	/**
	 * Find hist by target YM.
	 *
	 * @param closureId
	 *            the closure id
	 * @param targetMonth
	 *            the target month
	 * @return the list
	 */
	public List<ActualLockHistFindDto> findHistByTargetYM(int closureId, int targetMonth) {
		// Get LoginUserContext
		LoginUserContext loginUserContext = AppContexts.user();

		// CompanyId
		String companyId = loginUserContext.companyId();

		// FindAll ActualLock By ClosureId
		List<ActualLockHistory> actualLockHistList = this.actualLockHistRepo.findByTargetMonth(companyId, closureId,
				targetMonth);
		// Check Empty
		if (CollectionUtil.isEmpty(actualLockHistList)) {
			return new ArrayList<>();
		}
		// Convert to Dto
		return actualLockHistList.stream().map(actualLockHist -> {
			ActualLockHistFindDto dto = new ActualLockHistFindDto();
			// Save To Memento
			actualLockHist.saveToMemento(dto);
			// Get Updater Name
			EmployeeImport empImport = this.employeeAdapter.findByEmpId(actualLockHist.getUpdater());
			// Set Updater Name to Dto
			dto.setUpdater(empImport.getEmployeeName());
			return dto;
		}).collect(Collectors.toList());

	}
}
