package nts.uk.ctx.pereg.app.command.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting.UseAtr;
import nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.excessleave.PaymentMethod;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.UpperLimitSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.HourlyPaymentAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.ManageAtr;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.pereg.dom.person.info.singleitem.DataTypeValue;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.ItemValueType;

@Stateless
public class FacadeUtils {
	
	private static final List<String> historyCategoryCodeList = Arrays.asList("CS00003", "CS00004", "CS00014",
			"CS00016", "CS00017", "CS00018", "CS00019", "CS00020", "CS00021", "CS00070");

	private static final Map<String, String> startDateItemCodes;
	static {
		Map<String, String> aMap = new HashMap<>();
		// 所属会社履歴
		aMap.put("CS00003", "IS00020");
		// 分類１
		aMap.put("CS00004", "IS00026");
		// 雇用
		aMap.put("CS00014", "IS00066");
		// 職位本務
		aMap.put("CS00016", "IS00077");
		// 職場
		aMap.put("CS00017", "IS00082");
		// 休職休業
		aMap.put("CS00018", "IS00087");
		// 短時間勤務
		aMap.put("CS00019", "IS00102");
		// 労働条件
		aMap.put("CS00020", "IS00119");
		// 勤務種別
		aMap.put("CS00021", "IS00255");
		// 労働条件２
		aMap.put("CS00070", "IS00781");

		startDateItemCodes = Collections.unmodifiableMap(aMap);
	}

	private static final Map<String, String> endDateItemCodes;
	static {
		Map<String, String> aMap = new HashMap<>();
		// 所属会社履歴
		aMap.put("CS00003", "IS00021");
		// 分類１
		aMap.put("CS00004", "IS00027");
		// 雇用
		aMap.put("CS00014", "IS00067");
		// 職位本務
		aMap.put("CS00016", "IS00078");
		// 職場
		aMap.put("CS00017", "IS00083");
		// 休職休業
		aMap.put("CS00018", "IS00088");
		// 短時間勤務
		aMap.put("CS00019", "IS00103");
		// 労働条件
		aMap.put("CS00020", "IS00120");
		// 勤務種別
		aMap.put("CS00021", "IS00256");
		// 労働条件２
		aMap.put("CS00070", "IS00782");

		endDateItemCodes = Collections.unmodifiableMap(aMap);
	}
	
	@Inject 
	private AffCompanyHistRepository affCompanyHistRepository;
	
	private static final String FUNCTION_NAME = "getListDefault";
	
	// CS00020
	public List<ItemValue> getListDefaultCS00020(){
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		
		String[][] cs00020Item = { { "IS00253", numberType, "0" },
				{ "IS00248", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00247", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00258", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00259", numberType, String.valueOf(HourlyPaymentAtr.OOUTSIDE_TIME_PAY.value) },
				{ "IS00121", numberType, String.valueOf(ManageAtr.USE.value) } };
		return FacadeUtils.createListItems(cs00020Item);
	}
	
	// CS00025
	public List<ItemValue> getListDefaultCS00025(){
		String[][] cs00025Item = {
				{ "IS00296", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00025Item);
	}
	
	// CS00026
	public List<ItemValue> getListDefaultCS00026() {
		String[][] cs00026Item = {
				{ "IS00303", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00026Item);
	}
	
	// CS00027
	public List<ItemValue> getListDefaultCS00027() {
		String[][] cs00027Item = {
				{ "IS00310", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00027Item);
	}

	public List<ItemValue> getListDefaultCS00028() {
		String[][] cs00028Item = {
				{ "IS00317", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00028Item);
	}

	public List<ItemValue> getListDefaultCS00029() {
		String[][] cs00029Item = {
				{ "IS00324", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00029Item);
	}

	public List<ItemValue> getListDefaultCS00030() {
		String[][] cs00030Item = {
				{ "IS00331", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00030Item);
	}

	public List<ItemValue> getListDefaultCS00031() {
		String[][] cs00031Item = {
				{ "IS00338", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00031Item);
	}

	public List<ItemValue> getListDefaultCS00032() {
		String[][] cs00032Item = {
				{ "IS00345", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00032Item);
	}

	public List<ItemValue> getListDefaultCS00033() {
		String[][] cs00033Item = {
				{ "IS00352", String.valueOf(ItemValueType.NUMERIC.value), String.valueOf(UseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00033Item);
	}

	public List<ItemValue> getListDefaultCS00034() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00034Item = { { "IS00359", numberType,String.valueOf(NotUseAtr.NOT_USE.value) } };
		return FacadeUtils.createListItems(cs00034Item);
	}
	
	public List<ItemValue> getListDefaultCS00035() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00035Item = {
				{ "IS00369",numberType, "0" },
				{ "IS00370", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00371", numberType,  "0" },
				{ "IS00372", numberType, String.valueOf(PaymentMethod.VACATION_OCCURRED.value) } };
		return FacadeUtils.createListItems(cs00035Item);
	}
	
	public List<ItemValue> getListDefaultCS00036() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00036Item = { { "IS00375", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00376", numberType, String.valueOf(UpperLimitSetting.FAMILY_INFO.value) },
				{ "IS00380", numberType, String.valueOf(NotUseAtr.NOT_USE.value) },
				{ "IS00381", numberType, String.valueOf(UpperLimitSetting.FAMILY_INFO.value) }};
		return FacadeUtils.createListItems(cs00036Item);
	}
	
	public List<ItemValue> getListDefaultCS00037() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00037Item = { { "IS00387", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00037Item);
	}
	
	public List<ItemValue> getListDefaultCS00038() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00038Item = { { "IS00400", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00038Item);
	}

	public List<ItemValue> getListDefaultCS00039() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00039Item = { { "IS00411", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00039Item);
	}
	
	public List<ItemValue> getListDefaultCS00040() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00040Item = { { "IS00426", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00040Item);
	}
	
	public List<ItemValue> getListDefaultCS00041() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00041Item = { { "IS00441", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00041Item);
	}
	
	public List<ItemValue> getListDefaultCS00042() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00042Item = { { "IS00456", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00042Item);
	}
	
	public List<ItemValue> getListDefaultCS00043() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00043Item = { { "IS00471", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00043Item);
	}
	
	public List<ItemValue> getListDefaultCS00044() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00044Item = { { "IS00486", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00044Item);
	}
	
	public List<ItemValue> getListDefaultCS00045() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00045Item = { { "IS00501", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00045Item);
	}

	
	public List<ItemValue> getListDefaultCS00046() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00046Item = { { "IS00516", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00046Item);
	}

	public List<ItemValue> getListDefaultCS00047() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00047Item = { { "IS00531", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00047Item);
	}
	
	public List<ItemValue> getListDefaultCS00048() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00048Item = { { "IS00546", numberType, String.valueOf(LeaveExpirationStatus.AVAILABLE.value) }};
		return FacadeUtils.createListItems(cs00048Item);
	}

	public List<ItemValue> getListDefaultCS00049() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00049Item = { { "IS00560", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00049Item);
	}
	
	public List<ItemValue> getListDefaultCS00050() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00050Item = { { "IS00567", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00050Item);
	}
	
	public List<ItemValue> getListDefaultCS00051() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00051Item = { { "IS00574", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00051Item);
	}
	
	public List<ItemValue> getListDefaultCS00052() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00052Item = { { "IS00581", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00052Item);
	}
	
	public List<ItemValue> getListDefaultCS00053() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00053Item = { { "IS00588", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00053Item);
	}
	
	public List<ItemValue> getListDefaultCS00054() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00054Item = { { "IS00595", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00054Item);
	}
	
	public List<ItemValue> getListDefaultCS00055() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00055Item = { { "IS00602", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00055Item);
	}
	
	public List<ItemValue> getListDefaultCS00056() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00056Item = { { "IS00609", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00056Item);
	}
	
	public List<ItemValue> getListDefaultCS0007() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00057Item = { { "IS00616", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00057Item);
	}
	
	public List<ItemValue> getListDefaultCS00058() {
		String numberType = String.valueOf(ItemValueType.NUMERIC.value);
		String[][] cs00058Item = { { "IS00623", numberType, String.valueOf(NotUseAtr.NOT_USE.value) }};
		return FacadeUtils.createListItems(cs00058Item);
	}
	
	/**
	 * Create item
	 * @param itemCode
	 * @param type
	 * @param itemValue
	 * @return
	 */
	public static ItemValue createItem(String itemCode, int type, String itemValue){
		return new ItemValue("",itemCode,"",itemValue,"",itemValue, itemValue, type, type);
	}
	
	/**
	 * Create list items
	 * @param listItem
	 * @return
	 */
	public static List<ItemValue> createListItems(String[][] listItem){
		List<ItemValue> listItemResult = new ArrayList<>();
		for (int i = 0; i < listItem.length;i++){
			ItemValue item= FacadeUtils.createItem(listItem[i][0], Integer.parseInt(listItem[i][1]), listItem[i][2]);
			listItemResult.add(item);
		}
		return listItemResult;
	}

	/**
	 * Get list Default item exclude item in screen
	 * @param listCategoryCode
	 * @param listItemCodeInScreen
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public List<ItemValue> getListDefaultItem(String categoryCode,
			List<String> listItemCodeInScreen,String sid) {
		
		List<ItemValue> listItemResult = new ArrayList<>();
		try {
			Method method = FacadeUtils.class.getMethod(FUNCTION_NAME + categoryCode);
			@SuppressWarnings("unchecked")
			List<ItemValue> value = (List<ItemValue>) method.invoke(new FacadeUtils());
			listItemResult.addAll(value);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		listItemResult.addAll(processHistoryPeriod(categoryCode,listItemCodeInScreen,sid));		
		
		return listItemResult.stream().filter(i-> !listItemCodeInScreen.contains(i.itemCode())).collect(Collectors.toList());
	}
	
	/**
	 * Get history item for CPS007
	 * @param categoryCode
	 * @return
	 */
	private List<String> getListHistoryItem(String categoryCode){
		List<String> result = new ArrayList<>();
		if (historyCategoryCodeList.contains(categoryCode)) {
			result.add(startDateItemCodes.get(categoryCode));
			result.add(endDateItemCodes.get(categoryCode));
		}
		return result;
	}
	
	/**
	 * Set default item for history category
	 * @param categoryCode
	 * @param listItemCodeInScreen
	 * @param sid
	 * @return
	 */
	public List<ItemValue> processHistoryPeriod(String categoryCode,List<String> listItemCodeInScreen, String sid) {
		List<ItemValue> listItemResult = new ArrayList<>();
		int dataType = DataTypeValue.DATE.value;
		
		Optional<GeneralDate> hireDate = getHireDate(sid);
		
		if (!hireDate.isPresent()){
			return listItemResult;
		}
		
		if (historyCategoryCodeList.contains(categoryCode)) {
			String startDateItemCode = startDateItemCodes.get(categoryCode);
			String endDateItemCode = endDateItemCodes.get(categoryCode);

			if (!listItemCodeInScreen.stream().anyMatch(item -> item.equals(startDateItemCode))) {
				listItemResult
						.add(createItem(startDateItemCode, dataType, hireDate.get().toString()));
			}
			if (!listItemCodeInScreen.stream().anyMatch(item -> item.equals(endDateItemCode))) {
				listItemResult
						.add(createItem(endDateItemCode, dataType, GeneralDate.max().toString()));
			}

		}
		return listItemResult;
	}
	
	/**
	 * Get hire date
	 * @param sid
	 * @return
	 */
	public Optional<GeneralDate> getHireDate(String sid){
		AffCompanyHist affcom = affCompanyHistRepository.getAffCompanyHistoryOfEmployee(AppContexts.user().companyId(),sid);
		AffCompanyHistByEmployee hist = affcom.getAffCompanyHistByEmployee(sid);
		if (hist.getHistory().isPresent()){
			return Optional.of(hist.getHistory().get().start());
		}
		return Optional.empty();
	}
	
	/**
	 * Get list Default item for CPS007
	 * @param listCategoryCode
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public List<String> getListDefaultItem(List<String> listCategoryCode) {
		
		List<String> listItemResult = new ArrayList<>();
		listCategoryCode.forEach(category -> {
			try {
				Method method = FacadeUtils.class.getMethod(FUNCTION_NAME + category);
				@SuppressWarnings("unchecked")
				List<ItemValue> value = (List<ItemValue>) method.invoke(new FacadeUtils());
				listItemResult.addAll(value.stream().map(i->i.itemCode()).collect(Collectors.toList()));
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
			
			listItemResult.addAll(getListHistoryItem(category));		
		});
		return listItemResult;
	}
	
}
