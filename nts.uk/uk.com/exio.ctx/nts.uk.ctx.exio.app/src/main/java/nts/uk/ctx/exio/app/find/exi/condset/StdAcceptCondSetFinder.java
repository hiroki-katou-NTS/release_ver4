package nts.uk.ctx.exio.app.find.exi.condset;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.infra.file.storage.StoredFileStreamService;
import nts.uk.ctx.exio.app.find.exi.category.ExAcpCategoryDto;
import nts.uk.ctx.exio.app.find.exi.category.ExAcpCtgItemDatDto;
import nts.uk.ctx.exio.dom.exi.condset.StdAcceptCondSet;
import nts.uk.ctx.exio.dom.exi.condset.StdAcceptCondSetRepository;
import nts.uk.ctx.exio.dom.exi.condset.SystemType;
import nts.uk.ctx.exio.dom.exi.service.FileUtil;
import nts.uk.shr.com.context.AppContexts;

@Stateless
/**
 * 受入条件設定（定型）
 */
public class StdAcceptCondSetFinder {

	@Inject
	private StdAcceptCondSetRepository stdConditionRepo;

	@Inject
	private StoredFileStreamService fileStreamService;

	public List<SystemTypeDto> getSystemTypes() {
		List<SystemTypeDto> result = new ArrayList<>();
		String employeeId = AppContexts.user().employeeId();
		// dummy request list #50: get system code by employee id
		LoginUserInCharge charge = new LoginUserInCharge(true, true, true, true, true);
		if (charge.isHumanResource()) {
			result.add(new SystemTypeDto(SystemType.PERSON_SYSTEM.value, SystemType.PERSON_SYSTEM.nameId));
		}
		if (charge.isOfficeHelper()) {
			result.add(new SystemTypeDto(SystemType.OFFICE_HELPER.value, SystemType.OFFICE_HELPER.nameId));
		}
		if (charge.isSalary()) {
			result.add(new SystemTypeDto(SystemType.PAYROLL_SYSTEM.value, SystemType.PAYROLL_SYSTEM.nameId));
		}
		return result;
	}

	public List<StdAcceptCondSetDto> getStdAcceptCondSetBySysType(int systemType) {
		String companyId = AppContexts.user().companyId();
		return stdConditionRepo.getStdAcceptCondSetBySysType(companyId, systemType).stream()
				.map(item -> StdAcceptCondSetDto.fromDomain(item)).collect(Collectors.toList());
	}

	public StdAcceptCondSetDto getStdAccCondSet(int sysType, String conditionSetCd) {
		String companyId = AppContexts.user().companyId();
		Optional<StdAcceptCondSet> optDomain = stdConditionRepo.getStdAcceptCondSetById(companyId, sysType,
				conditionSetCd);
		if (optDomain.isPresent())
			return StdAcceptCondSetDto.fromDomain(optDomain.get());
		else
			return null;

	}

	public boolean isCodeExist(int systemType, String conditionCode) {
		String companyId = AppContexts.user().companyId();
		return stdConditionRepo.isSettingCodeExist(companyId, systemType, conditionCode);
	}

	public int getNumberOfLine(String fileId) {
		int totalRecord = 0;
		try {
			// get input stream by fileId
			InputStream inputStream = this.fileStreamService.takeOutFromFileId(fileId);

			totalRecord = FileUtil.getNumberOfLine(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return totalRecord;
	}

	public List<String> getRecordByIndex(String fileId, int numOfCol, int index) {
		List<String> result;
		try {
			// get input stream by fileId
			InputStream inputStream = this.fileStreamService.takeOutFromFileId(fileId);

			result = FileUtil.getRecordByIndex(inputStream, numOfCol, index);
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Dummies Data category
	 * 
	 * @return
	 */
	public List<ExAcpCategoryDto> getAllCategory() {

		List<ExAcpCategoryDto> lstDataCategory = new ArrayList<ExAcpCategoryDto>();
		for (int i = 1; i <= 4; i++) {
			lstDataCategory.add(new ExAcpCategoryDto("1dfsdffs" + i, "カテゴリ名　" + i, 0L));
		}
		return lstDataCategory;
	}

	public List<ExAcpCtgItemDatDto> getCategoryItemData(String categoryId) {
		List<ExAcpCtgItemDatDto> lstCategoryItemData = new ArrayList<ExAcpCtgItemDatDto>();
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j < 11; j++) {
				lstCategoryItemData.add(new ExAcpCtgItemDatDto("" + i, j, "カテゴリ項目データ" + "" + i + "" + j, j, 1, 1, 1,
						"1", 1, 1, 1, "5", "5", "5", "5", j, 1, 0L));
			}
		}

		return lstCategoryItemData;
	}
}
