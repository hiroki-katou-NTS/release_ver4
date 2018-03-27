package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethod;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodRepository;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceType;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceTimeInputMethodFinder.
 */
@Stateless
public class DivergenceTimeInputMethodFinder {

	/** The div reason repo. */
	@Inject
	private DivergenceReasonInputMethodRepository divReasonRepo;

	/** The div time repo. */
	@Inject
	private DivergenceTimeRepository divTimeRepo;

	/**
	 * Gets the all div time.
	 *
	 * @return the all div time
	 */
	public List<DivergenceTimeInputMethodDto> getAllDivTime() {
		// get company id
		String companyId = AppContexts.user().companyId();

		// Get divergence time dto list
		List<DivergenceTimeDto> listdivTime = this.getDivTimeList();

		// Get divergence reason input method dto list
		List<DivergenceReasonInputMethodDto> listDivReason = this.getAllDivReasonList();

		// New Divergence time input method dto list
		List<DivergenceTimeInputMethodDto> listDivTimeInput = new ArrayList<DivergenceTimeInputMethodDto>();

		// Convert listdivTime and listDivReason to listDivTimeInput
		listdivTime.forEach(e -> {

			// Find divergenceTimeNo in istDivReason to get
			// DivergenceReasonInputMethodDto
			DivergenceReasonInputMethodDto object = (DivergenceReasonInputMethodDto) listDivReason.stream()
					.filter(a -> a.getDivergenceTimeNo() == e.getDivergenceTimeNo()).findFirst().get();
			String type = DivergenceType.valueOf(e.getDivType()).name().toString();
			listDivTimeInput.add(new DivergenceTimeInputMethodDto(e.getDivergenceTimeNo(), companyId,
					e.getDivergenceTimeUseSet(), e.getDivergenceTimeName(), type, e.isReasonInput(), e.isReasonSelect(),
					object.getDivergenceReasonInputed(), object.getDivergenceReasonSelected(), null));
		});

		// return
		return listDivTimeInput;

	}

	/**
	 * Gets the div time input method info.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the div time input method info
	 */
	public DivergenceTimeInputMethodDto getDivTimeInputMethodInfo(int divTimeNo) {

		// get DivergenceTimeDto
		DivergenceTimeDto divTimeInfo = this.getDivTimeInfo(divTimeNo);

		// get DivergenceReasonInputMethodDto
		DivergenceReasonInputMethodDto divReasonInputMethodInfo = this.getDivReasonInfo(divTimeNo);

		// get DivergenceAttendanceId list
		List<Integer> attendanceIdList = this.getAttendanceIds(divTimeNo);
//
//		String type = DivergenceTypeDto.valueOf(divTimeInfo.getDivType()).name().toString();
		String type = DivergenceTypeDto.valueOf(divTimeInfo.getDivType()).nameId.toString();

		// Convert DivergenceReasonInputMethodDto, DivergenceTimeDto and
		// DivergenceAttendanceId list to DivergenceTimeInputMethodDto

		DivergenceTimeInputMethodDto result = new DivergenceTimeInputMethodDto(divTimeInfo.getDivergenceTimeNo(),
				AppContexts.user().companyId(), divTimeInfo.getDivergenceTimeUseSet(),
				divTimeInfo.getDivergenceTimeName(), type, divTimeInfo.isReasonInput(), divTimeInfo.isReasonSelect(),
				divReasonInputMethodInfo.getDivergenceReasonInputed(),
				divReasonInputMethodInfo.getDivergenceReasonSelected(), attendanceIdList);
		return result;
	}

	/**
	 * Gets the div time info.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the div time info
	 */
	public DivergenceTimeDto getDivTimeInfo(int divTimeNo) {
		// get company id
		String companyId = AppContexts.user().companyId();

		// Get divergence time
		DivergenceTime divTimeInfo = divTimeRepo.getDivTimeInfo(companyId, divTimeNo);

		// Convert to Dto and return
		if (divTimeInfo != null) {
			DivergenceTimeDto divTimeDto = new DivergenceTimeDto();
			divTimeInfo.saveToMemento(divTimeDto);
			return divTimeDto;
		} else
			return new DivergenceTimeDto();

	}

	/**
	 * Gets the div reason info.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the div reason info
	 */
	public DivergenceReasonInputMethodDto getDivReasonInfo(int divTimeNo) {

		// get company id
		String companyId = AppContexts.user().companyId();

		// get divergence time
		DivergenceReasonInputMethod divReasonInfo = divReasonRepo.getDivTimeInfo(companyId, divTimeNo);

		// Convert to Dto and return
		if (divReasonInfo != null) {
			DivergenceReasonInputMethodDto divReasonDto = new DivergenceReasonInputMethodDto();
			divReasonInfo.saveToMemento(divReasonDto);
			return divReasonDto;
		} else
			return new DivergenceReasonInputMethodDto();

	}

	/**
	 * Gets the attendance ids.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the attendance ids
	 */
	public List<Integer> getAttendanceIds(int divTimeNo) {
		// get company id

		String companyId = AppContexts.user().companyId();

		// return
		return divTimeRepo.findAttendanceId(companyId, divTimeNo);
	}

	/**
	 * Gets the div time list.
	 *
	 * @return the div time list
	 */
	public List<DivergenceTimeDto> getDivTimeList() {
		// Get company id
		String companyId = AppContexts.user().companyId();

		// Get divergence time list
		List<DivergenceTime> listDivTime = this.divTimeRepo.getAllDivTime(companyId);

		// Check list empty
		if (listDivTime.isEmpty()) {
			return Collections.emptyList();
		}
		// Convert to Dto
		return listDivTime.stream().map(e -> {
			DivergenceTimeDto dto = new DivergenceTimeDto();
			e.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * Gets the all div reason list.
	 *
	 * @return the all div reason list
	 */
	List<DivergenceReasonInputMethodDto> getAllDivReasonList() {
		// Get company id

		String companyId = AppContexts.user().companyId();

		// Get divergence time list
		List<DivergenceReasonInputMethod> listDivTime = this.divReasonRepo.getAllDivTime(companyId);

		// Check list empty
		if (listDivTime.isEmpty()) {
			return Collections.emptyList();
		}
		// Convert domain to Dto
		return listDivTime.stream().map(e -> {
			DivergenceReasonInputMethodDto dto = new DivergenceReasonInputMethodDto();
			e.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

}
