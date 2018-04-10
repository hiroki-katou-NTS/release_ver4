package nts.uk.ctx.pereg.app.find.person.setting.selectionitem.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.pereg.app.find.common.ComboBoxRetrieveFactory;
import nts.uk.ctx.pereg.app.find.person.info.item.SelectionItemDto;
import nts.uk.ctx.pereg.app.find.person.setting.init.item.SelectionInitDto;
import nts.uk.ctx.pereg.dom.person.info.category.PersonEmployeeType;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.IPerInfoSelectionItemRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.PerInfoSelectionItem;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.Selection;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionItemOrder;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionItemOrderRepository;
import nts.uk.ctx.pereg.dom.person.setting.selectionitem.selection.SelectionRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ComboBoxObject;

/**
 * 
 * @author tuannv
 *
 */
@Stateless
public class SelectionFinder {

	@Inject
	private SelectionRepository selectionRepo;

	@Inject
	private SelectionItemOrderRepository selectionOrderRpo;

	@Inject
	private ComboBoxRetrieveFactory comboBoxFactory;

	// fix bug: 23.2.2018
	@Inject
	private IPerInfoSelectionItemRepository selectionItemRpo;

	// アルゴリズム「選択肢履歴選択時処理」を実行する(Thực thi xử lý chọn 選択肢履歴)
	public List<SelectionDto> getAllSelection() {
		String contractCode = AppContexts.user().contractCode();
		return this.selectionRepo.getAllSelectByHistId(contractCode).stream()
				.map(i -> SelectionDto.fromDomainSelection(i)).collect(Collectors.toList());
	}

	// check history ID:
	public List<SelectionItemOrderDto> getHistIdSelection(String histId) {
		List<SelectionItemOrderDto> orderList = new ArrayList<SelectionItemOrderDto>();

		// lay selection
		List<Selection> selectionList = this.selectionRepo.getAllSelectByHistId(histId);
		Optional<PerInfoSelectionItem> sltItemOpt = selectionItemRpo.getSelectionItemByHistId(histId);
		// kiem tra so luong item lay duoc
		if (selectionList.isEmpty() || !sltItemOpt.isPresent()) {
			return orderList;
		} else {
			String getByHisId = selectionList.get(0).getHistId();
			List<SelectionItemOrder> orderDomainlst = this.selectionOrderRpo.getAllOrderSelectionByHistId(getByHisId);
			PerInfoSelectionItem sltItem = sltItemOpt.get();
			if (!orderDomainlst.isEmpty()) {
				orderList = orderDomainlst.stream().map(i -> {
					Selection selection = selectionList.stream()
							.filter(s -> s.getSelectionID().equals(i.getSelectionID())).findFirst().orElse(null);
					return SelectionItemOrderDto.fromSelectionOrder(i, selection,
							sltItem.getFormatSelection().getSelectionCodeCharacter().value);
				}).collect(Collectors.toList());
			}

			return orderList;
		}

	}

	// Lanlt
	public List<SelectionInitDto> getAllSelectionByHistoryId(String selectionItemId, String baseDate) {
		GeneralDate baseDateConvert = GeneralDate.fromString(baseDate, "yyyy-MM-dd");
		return this.selectionRepo.getAllSelectionByHistoryId(selectionItemId, baseDateConvert).stream()
				.map(c -> SelectionInitDto.fromDomainSelection(c)).collect(Collectors.toList());

	}

	// ham nay su dung chu y selectionItemClsAtr co gia tri la 0 vs 1
	// con bang itemCommon thi co gia tri la 1 vs 2 ko map vs nhau
	// do do ma ham nay phai chuyen doi de co du lieu chinh xac
	public List<SelectionInitDto> getAllSelectionByHistoryId(SelectionInitQuery query) {
		GeneralDate today = GeneralDate.today();
		String companyId = AppContexts.user().companyId();
		String zeroCompanyId = AppContexts.user().zeroCompanyIdInContract();
		String selectionItemId = query.getSelectionItemId();
		List<Selection> selectionList = new ArrayList<>();
		if (query.isCps006() && query.getSelectionItemClsAtr() == PersonEmployeeType.EMPLOYEE.value) {
			selectionList = this.selectionRepo.getAllSelectionByHistoryId(companyId, selectionItemId, today, 1);
		} else {
			selectionList = this.selectionRepo.getAllSelectionByHistoryId(zeroCompanyId, selectionItemId, today, 0);
		}
		return selectionList.stream().map(c -> SelectionInitDto.fromDomainSelection(c)).collect(Collectors.toList());
	}

	// Lanlt
	/**
	 * for companyID
	 * 
	 * @param selectionItemId
	 * @param baseDate
	 * @return
	 */
	public List<SelectionInitDto> getAllSelectionByCompanyId(String selectionItemId, GeneralDate date, PersonEmployeeType perEmplType) {
		
		String companyId = AppContexts.user().companyId();
		
		if (perEmplType == PersonEmployeeType.PERSON){
			companyId = AppContexts.user().zeroCompanyIdInContract();
		}
		// Zero company
		List<SelectionInitDto> selectionLst = new ArrayList<>();
		List<Selection> domainLst = this.selectionRepo.getAllSelectionByCompanyId(companyId, selectionItemId, date);
		if (domainLst != null) {

			selectionLst = domainLst.stream().map(c -> SelectionInitDto.fromDomainSelection(c))
					.collect(Collectors.toList());
		}

		return selectionLst;

	}

	public List<ComboBoxObject> getAllComboxByHistoryId(SelectionQuery dto) {
		GeneralDate baseDateConvert = GeneralDate.fromString(dto.getBaseDate(), "yyyy-MM-dd");
		SelectionItemDto selectionItemDto = null;
		String companyId = AppContexts.user().companyId();
		if (dto.getSelectionItemRefType() == 2) {
			return this.selectionRepo.getAllSelectionByCompanyId(companyId, dto.getSelectionItemId(), baseDateConvert)
					.stream().map(c -> new ComboBoxObject(c.getSelectionID(), c.getSelectionName().toString()))
					.collect(Collectors.toList());
		} else if (dto.getSelectionItemRefType() == 1) {
			selectionItemDto = SelectionItemDto.createMasterRefDto(dto.getSelectionItemId(),
					dto.getSelectionItemRefType());
			return this.comboBoxFactory.getComboBox(selectionItemDto, AppContexts.user().employeeId(), baseDateConvert,
					true, PersonEmployeeType.EMPLOYEE);

		}
		return new ArrayList<>();

	}

}
