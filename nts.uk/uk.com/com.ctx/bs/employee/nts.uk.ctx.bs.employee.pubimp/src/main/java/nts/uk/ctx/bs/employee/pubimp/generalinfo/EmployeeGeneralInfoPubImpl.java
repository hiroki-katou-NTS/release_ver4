package nts.uk.ctx.bs.employee.pubimp.generalinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItem;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItemRepository;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistory;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistoryRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistory;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.pub.generalinfo.EmployeeGeneralInfoDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.EmployeeGeneralInfoPub;
import nts.uk.ctx.bs.employee.pub.generalinfo.classification.ExClassificationHistItemDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.classification.ExClassificationHistoryDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.employment.ExEmploymentHistItemDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.employment.ExEmploymentHistoryDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.jobtitle.ExJobTitleHistItemDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.jobtitle.ExJobTitleHistoryDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.workplace.ExWorkPlaceHistoryDto;
import nts.uk.ctx.bs.employee.pub.generalinfo.workplace.ExWorkplaceHistItemDto;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmployeeGeneralInfoPubImpl implements EmployeeGeneralInfoPub {

	@Inject
	private EmploymentHistoryRepository employmentHistoryRepo;

	@Inject
	private EmploymentHistoryItemRepository employmentHistItemRepo;

	@Inject
	private AffClassHistoryRepository affClassHistRepo;

	@Inject
	private AffClassHistItemRepository affClassHistItemRepo;

	@Inject
	private AffJobTitleHistoryRepository affJobTitleRepo;

	@Inject
	private AffJobTitleHistoryItemRepository affJobTitleItemRepo;

	@Inject
	private AffWorkplaceHistoryRepository workplaceHistoryRepo;

	@Inject
	private AffWorkplaceHistoryItemRepository workplaceHistItemRepo;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public EmployeeGeneralInfoDto getPerEmpInfo(List<String> employeeIds, DatePeriod period, boolean checkEmployment,
			boolean checkClassification, boolean checkJobTitle, boolean checkWorkplace, boolean checkDepartment) {

		// employment - 雇用
		List<ExEmploymentHistoryDto> employmentDto = getEmploymentDto(checkEmployment, employeeIds, period);

		// classification - 分類
		List<ExClassificationHistoryDto> classificationDto = getClassificationDto(checkClassification, employeeIds,
				period);

		// job-title - 職位
		List<ExJobTitleHistoryDto> jobTitleDto = getJobTitleDto(checkJobTitle, employeeIds, period);

		// work-place 職場
		List<ExWorkPlaceHistoryDto> workplaceDtoList = getWorkplaceDto(checkWorkplace, employeeIds, period);

		return new EmployeeGeneralInfoDto(employmentDto, classificationDto, jobTitleDto, workplaceDtoList);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<ExEmploymentHistoryDto> getEmploymentDto(boolean checkEmployment, List<String> employeeIds,
			DatePeriod period) {
		if (!checkEmployment) {
			return Collections.emptyList();
		}
		List<EmploymentHistory> employmentHistoryList = employmentHistoryRepo.getByListSid(employeeIds, period);
		List<String> historyIds = new ArrayList<>();
		employmentHistoryList.forEach(eh -> historyIds.addAll(eh.getHistoryIds()));

		Map<String, EmploymentHistoryItem> ehItemList = employmentHistItemRepo.getByListHistoryId(historyIds).stream()
				.collect(Collectors.toMap(x -> x.getHistoryId(), x -> x));
		List<ExEmploymentHistoryDto> resultList = new ArrayList<>();

		for (EmploymentHistory eh : employmentHistoryList) {
			List<ExEmploymentHistItemDto> employmentItems = eh.getHistoryItems().stream().map(ehItem -> {
				String historyId = ehItem.identifier();
				String employmentCode = ehItemList.get(historyId).getEmploymentCode().v();
				return new ExEmploymentHistItemDto(historyId, new DatePeriod(ehItem.start(), ehItem.end()),
						employmentCode);
			}).collect(Collectors.toList());
			resultList.add(new ExEmploymentHistoryDto(eh.getEmployeeId(), employmentItems));
		}
		return resultList;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<ExClassificationHistoryDto> getClassificationDto(boolean checkClassification, List<String> employeeIds,
			DatePeriod period) {
		if (!checkClassification) {
			return Collections.emptyList();
		}
		List<AffClassHistory> classHitoryList = affClassHistRepo.getByEmployeeListWithPeriod(employeeIds, period);

		List<String> historyIds = new ArrayList<>();
		classHitoryList.forEach(ch -> historyIds.addAll(ch.getHistoryIds()));

		Map<String, AffClassHistItem> chItemList = affClassHistItemRepo.getByHistoryIds(historyIds).stream()
				.collect(Collectors.toMap(x -> x.getHistoryId(), x -> x));
		List<ExClassificationHistoryDto> resultList = new ArrayList<>();
		for (AffClassHistory ch : classHitoryList) {
			List<ExClassificationHistItemDto> classificationItems = ch.getPeriods().stream().map(chItem -> {
				String historyId = chItem.identifier();
				String classificationCode = chItemList.get(historyId).getClassificationCode().v();
				return new ExClassificationHistItemDto(historyId, new DatePeriod(chItem.start(), chItem.end()),
						classificationCode);
			}).collect(Collectors.toList());
			resultList.add(new ExClassificationHistoryDto(ch.getEmployeeId(), classificationItems));
		}
		return resultList;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<ExJobTitleHistoryDto> getJobTitleDto(boolean checkJobTitle, List<String> employeeIds,
			DatePeriod period) {
		if (!checkJobTitle) {
			return Collections.emptyList();
		}
		List<AffJobTitleHistory> jobTitleHistoryList = affJobTitleRepo.getByEmployeeListPeriod(employeeIds, period);

		List<String> historyIds = new ArrayList<>();
		jobTitleHistoryList.forEach(ch -> historyIds.addAll(ch.getHistoryIds()));

		Map<String, AffJobTitleHistoryItem> jtItemList = affJobTitleItemRepo.findByHitoryIds(historyIds).stream()
				.collect(Collectors.toMap(x -> x.getHistoryId(), x -> x));
		List<ExJobTitleHistoryDto> resultList = new ArrayList<>();

		for (AffJobTitleHistory jh : jobTitleHistoryList) {
			List<ExJobTitleHistItemDto> jobTitleItems = jh.getHistoryItems().stream().map(jhItem -> {
				String historyId = jhItem.identifier();
				String jobTitleId = jtItemList.get(historyId).getJobTitleId();
				return new ExJobTitleHistItemDto(historyId, new DatePeriod(jhItem.start(), jhItem.end()), jobTitleId);
			}).collect(Collectors.toList());
			resultList.add(new ExJobTitleHistoryDto(jh.getEmployeeId(), jobTitleItems));
		}
		return resultList;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<ExWorkPlaceHistoryDto> getWorkplaceDto(boolean checkWorkplace, List<String> employeeIds,
			DatePeriod period) {
		if (!checkWorkplace) {
			return Collections.emptyList();
		}
		List<AffWorkplaceHistory> workplaceHistoryList = workplaceHistoryRepo.findByEmployeesWithPeriod(employeeIds,
				period);
		List<String> historyIds = new ArrayList<>();
		workplaceHistoryList.forEach(wh -> historyIds.addAll(wh.getHistoryIds()));
		Map<String, AffWorkplaceHistoryItem> whItemList = workplaceHistItemRepo.findByHistIds(historyIds).stream()
				.collect(Collectors.toMap(x -> x.getHistoryId(), x -> x));

		List<ExWorkPlaceHistoryDto> resultList = new ArrayList<>();
		for (AffWorkplaceHistory wh : workplaceHistoryList) {

			List<ExWorkplaceHistItemDto> workplaceItems = wh.getHistoryItems().stream().map(dhItem -> {
				String historyId = dhItem.identifier();
				String workplaceId = whItemList.get(historyId).getWorkplaceId();
				return new ExWorkplaceHistItemDto(historyId, new DatePeriod(dhItem.start(), dhItem.end()), workplaceId);
			}).collect(Collectors.toList());

			resultList.add(new ExWorkPlaceHistoryDto(wh.getEmployeeId(), workplaceItems));
		}

		return resultList;
	}

}
