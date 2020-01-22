package nts.uk.ctx.hr.notice.app.find.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.hr.notice.app.find.report.regis.person.AttachPersonReportFileFinder;
import nts.uk.ctx.hr.notice.dom.report.PersonalReportClassification;
import nts.uk.ctx.hr.notice.dom.report.PersonalReportClassificationRepository;
import nts.uk.ctx.hr.notice.dom.report.RegisterPersonalReportItem;
import nts.uk.ctx.hr.notice.dom.report.RegisterPersonalReportItemRepository;
import nts.uk.ctx.hr.notice.dom.report.registration.person.DocumentSampleDto;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReport;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReportRepository;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportItem;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportItemRepository;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportStartSetting;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportStartSettingRepository;
import nts.uk.ctx.hr.notice.dom.report.valueImported.DateRangeItemImport;
import nts.uk.ctx.hr.notice.dom.report.valueImported.HumanItemPub;
import nts.uk.ctx.hr.notice.dom.report.valueImported.PerInfoItemDefImport;
import nts.uk.ctx.hr.notice.dom.report.valueImported.ctg.HumanCategoryPub;
import nts.uk.ctx.hr.notice.dom.report.valueImported.ctg.PerInfoCtgShowImport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ReportItemFinder {
	@Inject
	private PersonalReportClassificationRepository reportClsRepo;

	@Inject
	private RegisterPersonalReportItemRepository itemReportClsRepo;

	@Inject
	private HumanItemPub humanItemPub;

	@Inject
	private ReportItemRepository reportItemRepo;

	@Inject
	private HumanCategoryPub humanCtgPub;
	
	@Inject
	private ReportStartSettingRepository reportStartSettingRepo;
	
	@Inject
	private RegistrationPersonReportRepository registrationPersonReportRepo;

	@Inject
	private AttachPersonReportFileFinder attachPersonReportFileFinder;

	/**
	 * 
	 * @param reportClsId
	 * @return
	 */
	public ReportLayoutDto getDetailReportCls(ReportParams params) {

		String cid = AppContexts.user().companyId();

		Optional<RegistrationPersonReport> registrationPersonReport = this.registrationPersonReportRepo.getDomainByReportId(cid, params.getReportId() == null ? null : Integer.valueOf(params.getReportId()));

		// ドメインモデル「個別届出種類」、「個別届出の登録項目」をすべて取得する 。ドメイン「[個人情報項目定義]」を取得する。
		// (Get tất cả domain models「type đơn xin cá nhân」、「 Item dang ky don xin ca nhan」)
		Optional<PersonalReportClassification> reportClsOpt = Optional.empty();
		
		if(registrationPersonReport.isPresent()) {
			
			reportClsOpt = this.reportClsRepo.getDetailReportClsByReportClsID(cid,
					registrationPersonReport.get().getReportLayoutID());
			
		}else {
			
			reportClsOpt = this.reportClsRepo.getDetailReportClsByReportClsID(cid,
					params.getReportLayoutId());
			
			
		}
		 
		int reportLayoutId = getReportLayoutId(params, reportClsOpt);

		//届出IDのドメイン「人事届出の登録」「届出の開始設定」、「個別届出の登録項目」情報を取得
		Optional<ReportStartSetting> reportStartSetting = this.reportStartSettingRepo.getReportStartSettingByCid(cid);
		
		// ドメインモデル「個人情報項目定義」、「個別届出の登録項目」をすべて取得する (Get all domain model
		// 「個人情報項目定義」、「個別届出の登録項目」)
		List<RegisterPersonalReportItem> listItemCls = this.itemReportClsRepo.getAllItemBy(cid, reportLayoutId);
		
		//添付ファイル一覧を表示する(アルゴリズム[添付ファイル一覧を表示する]を実行する)
		List<DocumentSampleDto> documentSampleDtoLst = this.attachPersonReportFileFinder.findAll(reportLayoutId, params.getReportId() == null ? null : Integer.valueOf(params.getReportId()));

		List<LayoutReportClsDto> items = mapItemCls(params.getReportId(), listItemCls);

		List<LayoutReportClsDto> itemInter = new ArrayList<>();

		int itemSize = items.size();

		for (int i = 0; i < itemSize; i++) {

			if (items.get(i).getLayoutItemType() == LayoutReportItemType.SeparatorLine && (i + 1) < itemSize) {

				if (items.get(i + 1).getLayoutItemType() == LayoutReportItemType.SeparatorLine) {

					i = i + 1;

				}

				itemInter.add(items.get(i));

			} else {

				itemInter.add(items.get(i));

			}
		}
		return reportClsOpt.isPresent() == true
				? ReportLayoutDto.createFromDomain(reportClsOpt.get(), reportStartSetting, registrationPersonReport,
						itemInter, documentSampleDtoLst)
				: new ReportLayoutDto();
	}
	
	private int getReportLayoutId(ReportParams params , Optional<PersonalReportClassification> reportClsOpt) {
		if(params.getReportId() == null) {
			return reportClsOpt.get().getPReportClsId();
		}
		
		return params.getReportLayoutId();
	}
	

	/**
	 * 届出パネル内項目表示処理
	 * convert item để lấy thông tin kiểu dữ liệu của item đó
	 * 
	 * @param listItemCls
	 * @return
	 */
	private List<LayoutReportClsDto> mapItemCls(String reportId, List<RegisterPersonalReportItem> listItemCls) {
		List<LayoutReportClsDto> result = new ArrayList<>();

		String cid = AppContexts.user().companyId();

		String contractCd = AppContexts.user().contractCode();

		if (CollectionUtil.isEmpty(listItemCls))
			return new ArrayList<LayoutReportClsDto>();

		int order = 0;

		for (RegisterPersonalReportItem item : listItemCls) {

			LayoutReportClsDto classDto = new LayoutReportClsDto(item.getPReportClsId(), item.getLayoutOrder(),
					item.getCategoryId(), item.getItemType(), item.getCategoryCd(), 1, item.getCategoryName());

			switch (item.getItemType()) {

			case 0:// single item

			case 1:// list item

				if (order == item.getLayoutOrder())
					break;

				List<RegisterPersonalReportItem> itemsByOrder1 = listItemCls.stream()
						.filter(c -> c.getLayoutOrder() == item.getLayoutOrder()
								&& c.getPReportClsId() == item.getPReportClsId())
						.collect(Collectors.toList());

				order = item.getLayoutOrder();

				List<String> itemIds = itemsByOrder1.stream().map(c -> c.getItemId()).distinct()
						.collect(Collectors.toList());

				if (!CollectionUtil.isEmpty(itemIds)) {

					List<PerInfoItemDefImport> listItemDefDto = new ArrayList<PerInfoItemDefImport>();

					// ドメインモデル「個人情報項目定義」を取得する - Lấy định nghĩa item, điều kiện
					List<PerInfoItemDefImport> listItemDef = humanItemPub.getAll(itemIds);

					listItemDef.stream().forEach(c -> {

						c.setCategoryCode(item.getCategoryCd());

						c.setCategoryName(item.getCategoryName());
					});

					for (String id : itemIds) {

						List<PerInfoItemDefImport> dto = listItemDef.stream().filter(p -> p.getId().equals(id))
								.collect(Collectors.toList());

						if (!dto.isEmpty()) {

							listItemDefDto.add(dto.get(0));
						}
					}

					List<String> roots = listItemDefDto.stream()
							.filter(f -> f.getItemParentCode().equals("") || f.getItemParentCode() == null)
							.map(m -> m.getItemCode()).collect(Collectors.toList());

					if (roots.size() > 1) {

						classDto.setListItemDf(listItemDefDto);

					} else {

						if (listItemDefDto.size() > 1) {

							if (classDto.getLayoutItemType() == LayoutReportItemType.ITEM) {

								if (listItemDefDto.get(0).getItemTypeState().getItemType() == 1
										|| listItemDefDto.get(0).getItemTypeState().getItemType() == 3) {

									classDto.setListItemDf(listItemDefDto);

								} else {

									classDto.setListItemDf(new ArrayList<PerInfoItemDefImport>());

								}

							} else if (classDto.getLayoutItemType() == LayoutReportItemType.LIST) {

								classDto.setListItemDf(listItemDefDto);

							}

						} else if (listItemDefDto.size() == 1) {

							classDto.setListItemDf(listItemDefDto);

						} else {

							classDto.setListItemDf(new ArrayList<PerInfoItemDefImport>());

						}

						if (classDto.getLayoutItemType() == LayoutReportItemType.ITEM && !listItemDefDto.isEmpty()
								&& listItemDefDto.get(0) != null) {

							classDto.setClassName(listItemDefDto.get(0).getItemName());

						}
					}
				}

				if (classDto.getLayoutItemType() == LayoutReportItemType.LIST) {

					String catDto = this.humanItemPub.getCategoryName(cid, classDto.getCategoryCode());

					if (catDto != null) {

						classDto.setClassName(catDto);

					}
				}

				result.add(classDto);

				break;
			case 2:// SeparatorLine

				result.add(classDto);

				break;
			}

		}

		Map<String, List<ReportItem>> reportItems  = new HashMap<>();
		
		if(reportId != null) {
			
			reportItems.putAll(this.reportItemRepo.getDetailReport(cid, Integer.valueOf(reportId)).stream()
					.collect(Collectors.groupingBy(c -> c.getCtgCode())));
		}

		// GET DATA WITH EACH CATEGORY
		Map<String, List<LayoutReportClsDto>> classItemInCategoryMap = result.stream()
				.filter(classItem -> classItem.getLayoutItemType() != LayoutReportItemType.SeparatorLine)
				.collect(Collectors.groupingBy(LayoutReportClsDto::getPersonInfoCategoryID));

		String sid = AppContexts.user().employeeId();

		for (Entry<String, List<LayoutReportClsDto>> classItemsOfCategory : classItemInCategoryMap.entrySet()) {

			String categoryId = classItemsOfCategory.getKey();

			List<LayoutReportClsDto> classItemList = classItemsOfCategory.getValue();

			Optional<PerInfoCtgShowImport> ctgOpt = this.humanCtgPub.getCategoryByCidAndContractCd(categoryId,
					contractCd);

			if (ctgOpt.isPresent()) {

				PerInfoCtgShowImport ctg = ctgOpt.get();

				List<ReportItem> itemDatas = reportItems.get(ctg.getCategoryCode());

				switch (ctg.getCategoryType()) {
				case 1:

				case 3:

				case 4:

				case 5:

				case 6:

					// get data
					getDataforSingleItem(sid, ctg, GeneralDate.today(), itemDatas, classItemList);

					break;

				case 2:

					getDataforListItem(sid, ctg, GeneralDate.today(), classItemList.get(0), itemDatas);

					break;

				default:

					break;
				}
			}
		}

		return result;
	}

	private void mapListItemClass(List<ReportItem> itemDatas, List<LayoutReportClsDto> classItemList) {

		if (!CollectionUtil.isEmpty(itemDatas)) {

			classItemList.stream().forEach(c -> {

				if (!CollectionUtil.isEmpty(c.getItems())) {

					c.getItems().stream().forEach(i -> {

						Optional<ReportItem> itemDataOpt = itemDatas.stream()
								.filter(data -> data.getItemCd().equals(i.getItemCode())).findFirst();

						if (itemDataOpt.isPresent()) {

							i.setShowColor(false);

							ReportItem itemData = itemDataOpt.get();

							i.setRecordId(String.valueOf(itemData.getReportID()));

							if (itemData.getSaveDataAtr() == 1) {

								i.setValue(itemData.getStringVal());

							} else if (itemData.getSaveDataAtr() == 2) {

								i.setValue(itemData.getIntVal());

							} else {

								i.setValue(itemData.getDateVal());

							}
						}

					});
				}

			});

		}

	}

	private void matchOptionalItemData(String recordId, List<LayoutReportClsDto> classItemList,
			List<ReportItem> dataItems) {
		
		for (LayoutReportClsDto classItem : classItemList) {
			
			matchDataToValueItems(recordId, classItem.getItems(), dataItems);
			
		}
		
	}

	public void matchDataToValueItems(String recordId, List<LayoutHumanInfoValueDto> valueItems,
			List<ReportItem> dataItems) {
		
		for (LayoutHumanInfoValueDto valueItem : valueItems) {
			// recordId
			valueItem.setRecordId(recordId);

			valueItem.setShowColor(false);

			// data
			for (ReportItem dataItem : dataItems) {
				
				if (valueItem.getItemCode().equals(dataItem.getItemCd())) {
					
					if (dataItem.getSaveDataAtr() == 1) {
						
						valueItem.setValue(dataItem.getStringVal());
						
					} else if (dataItem.getSaveDataAtr() == 2) {
						
						valueItem.setValue(dataItem.getIntVal());
						
					} else if (dataItem.getSaveDataAtr() == 3) {
						
						valueItem.setValue(dataItem.getDateVal());
						
					}

				}
			}
		}
	}

	/**
	 * @param perInfoCategory
	 * @param authClassItem
	 * @param standardDate
	 * @param personId
	 * @param employeeId
	 * @param query
	 */
	private void getDataforSingleItem(String employeeId, PerInfoCtgShowImport perInfoCategory, GeneralDate standardDate,
			List<ReportItem> itemDatas, List<LayoutReportClsDto> classItemList) {

		cloneDefItemToValueItem(perInfoCategory, classItemList);

		GeneralDate comboBoxStandardDate = GeneralDate.today();
		
		switch (perInfoCategory.getCategoryType()) {
		
		case 1:
			
			getSingleOptionData(perInfoCategory, comboBoxStandardDate, classItemList, itemDatas);
			
			break;
			
		case 3:
			
		case 4:
			
		case 5:
			
		case 6:
			
			getPersDataHistoryType(perInfoCategory, comboBoxStandardDate, classItemList, itemDatas);
			
			break;
			
		default:
			
			break;
			
		}

		// For each classification item to get combo box list
		// layoutControlComboBox.getComboBoxListForSelectionItems(employeeId,
		// perInfoCategory, classItemList, comboBoxStandardDate);

		// set default value
		// initDefaultValue.setDefaultValue(classItemList);

	}

	private void getSingleOptionData(PerInfoCtgShowImport perInfoCategory, GeneralDate comboBoxStandardDate,
			List<LayoutReportClsDto> classItemList, List<ReportItem> itemDatas) {
		
		if (itemDatas != null) {

			mapListItemClass(itemDatas, classItemList);

			List<String> standardDateItemCodes = Arrays.asList("IS00020", "IS00077", "IS00082", "IS00119", "IS00781");
			
			for (String itemCode : standardDateItemCodes) {

				Optional<ReportItem> itemDataOpt = itemDatas.stream().filter(c -> c.getItemCd().equals(itemCode))
						.findFirst();

				if (itemDataOpt.isPresent()) {

					comboBoxStandardDate = itemDataOpt.get().getDateVal();

					break;
				}
			}
		}

	}

	/**
	 * @param perInfoCategoryId
	 * @param authClassItem
	 * @param personId
	 * @param stardardDate
	 *            Target: get data with history case. Person case
	 */
	private void getPersDataHistoryType(PerInfoCtgShowImport perInfoCategory, GeneralDate stardardDate,
			List<LayoutReportClsDto> classItemList, List<ReportItem> itemDatas) {
		
		DateRangeItemImport dateRangeItem = this.humanItemPub.getDateRangeItemByCtgId(perInfoCategory.getId());
		
		String startDateId = dateRangeItem.getStartDateItemId();
		
		String endDateId = dateRangeItem.getEndDateItemId();
		
		if (CollectionUtil.isEmpty(itemDatas))
			return;
		
		ReportItem reportItem = itemDatas.get(0);
		
		String recordId = String.valueOf(reportItem.getReportID());
		
		Optional<ReportItem> startDateOpt = itemDatas.stream().filter(c -> c.getItemId().equals(startDateId))
				.findFirst();
		
		Optional<ReportItem> endDateOpt = itemDatas.stream().filter(c -> c.getItemId().equals(endDateId)).findFirst();

		if (startDateOpt.isPresent() && endDateOpt.isPresent()) {
			
			if (stardardDate.afterOrEquals((GeneralDate) startDateOpt.get().getDateVal())
					&& stardardDate.beforeOrEquals((GeneralDate) endDateOpt.get().getDateVal())) {
				
				matchOptionalItemData(recordId, classItemList, itemDatas);
				
			}
		}
	}

	private void getDataforListItem(String employeeId, PerInfoCtgShowImport perInfoCategory, GeneralDate standardDate,
			LayoutReportClsDto classItem, List<ReportItem> itemDatas) {

		classItem.setItems(new ArrayList<>());

		// get option category data
		getMultiOptionData(employeeId, perInfoCategory, classItem, itemDatas);

		classItem.setListItemDf(null);

		classItem.setCategoryCode(perInfoCategory.getCategoryCode());

		// special process with category CS00069 item IS00779. change string length
		// stampCardLength.updateLength(perInfoCategory, Arrays.asList(classItem));
		// layoutControlComboBox.getComboBoxListForSelectionItems(employeeId,
		// perInfoCategory, Arrays.asList(classItem),
		// GeneralDate.today());

	}

	private List<LayoutHumanInfoValueDto> convertDefItem(PerInfoCtgShowImport perInfoCategory,
			List<PerInfoItemDefImport> listItemDf) {

		return listItemDf.stream().map(itemDef -> LayoutHumanInfoValueDto.cloneFromItemDef(perInfoCategory, itemDef))
				.collect(Collectors.toList());
	}

	private void getMultiOptionData(String employeeId, PerInfoCtgShowImport perInfoCategory,
			LayoutReportClsDto classItem, List<ReportItem> itemDatas) {

		if (CollectionUtil.isEmpty(itemDatas)) {

			classItem.getItems().addAll(convertDefItem(perInfoCategory, classItem.getListItemDf()));

			return;
		}

		// create new line data
		List<LayoutHumanInfoValueDto> valueItems = convertDefItem(perInfoCategory, classItem.getListItemDf());

		mapListItemClass(itemDatas, Arrays.asList(classItem));

		classItem.getItems().addAll(valueItems);

	}

	private void cloneDefItemToValueItem(PerInfoCtgShowImport perInfoCategory, List<LayoutReportClsDto> classItemList) {

		for (LayoutReportClsDto classItem : classItemList) {

			List<LayoutHumanInfoValueDto> items = new ArrayList<>();

			for (PerInfoItemDefImport itemDef : classItem.getListItemDf()) {

				items.add(LayoutHumanInfoValueDto.cloneFromItemDef(perInfoCategory, itemDef));

			}

			classItem.setCategoryCode(perInfoCategory.getCategoryCode());

			classItem.setCtgType(perInfoCategory.getCategoryType());

			classItem.setListItemDf(null);

			classItem.setItems(items);

		}
	}
}
