package nts.uk.ctx.pereg.ac.specialholiday;

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

import nts.arc.enums.EnumAdaptor;
import nts.arc.i18n.I18NResources;
import nts.arc.layer.dom.event.DomainEventSubscriber;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayCode;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayName;
import nts.uk.ctx.at.shared.dom.specialholiday.event.SpecialHolidayEvent;
import nts.uk.ctx.pereg.dom.person.info.category.CategoryName;
import nts.uk.ctx.pereg.dom.person.info.category.IsAbolition;
import nts.uk.ctx.pereg.dom.person.info.category.PerInfoCategoryRepositoty;
import nts.uk.ctx.pereg.dom.person.info.category.PersonInfoCategory;
import nts.uk.ctx.pereg.dom.person.info.item.PerInfoItemDefRepositoty;
import nts.uk.ctx.pereg.dom.person.info.item.PersonInfoItemDefinition;
import nts.uk.ctx.pereg.dom.specialholiday.SHDomainEventTest;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class SphdHolidayEvenSubcriber implements DomainEventSubscriber<SpecialHolidayEvent>, SHDomainEventTest{

	@Inject
	private PerInfoCategoryRepositoty ctgRepo;
	
	@Inject
	private PerInfoItemDefRepositoty itemRepo;
	
	@Inject
	private I18NResources resources;
	
	@Override
	public Class<SpecialHolidayEvent> subscribedToEventType() {
		return SpecialHolidayEvent.class;
	}
	
	private static final List<String> lstCtgCd1 = Arrays.asList(new String[]{"CS00025", "CS00026", "CS00027",
			"CS00028", "CS00029", "CS00030", "CS00031", "CS00032", "CS00033", "CS00034", "CS00049", "CS00050",
			"CS00051", "CS00052", "CS00053", "CS00054", "CS00055"});
	private static final List<String> lstCtgCd2 = Arrays.asList(new String[]{"CS00039", "CS00040", "CS00041", "CS00042"
			, "CS00043", "CS00044", "CS00045", "CS00046", "CS00047", "CS00048", "CS00059", "CS00060", "CS00061", "CS00062"
			, "CS00063", "CS00064", "CS00065", "CS00066", "CS00067", "CS00068"});
	
	@Override
	public void handle(SpecialHolidayEvent domainEvent) {	
		int spcHdCode = domainEvent.getSpecialHolidayCode().v();
		List<String> ctgCds = getCtgCds(spcHdCode);
		String loginCompanyId = AppContexts.user().companyId();
		List<PersonInfoCategory> ctgLst = ctgRepo.getPerCtgByListCtgCd(ctgCds, loginCompanyId);
		if(ctgLst.size() > 0){
			updateCtg(domainEvent, ctgLst, loginCompanyId, ctgCds);		
		}
	}
	
	private void updateCtg(SpecialHolidayEvent domainEvent, List<PersonInfoCategory> ctgLst, String loginCompanyId, List<String> ctgCds){
		List<PersonInfoCategory> ctgUpdateList = new ArrayList<>();
		List<PersonInfoItemDefinition> updateItems = new ArrayList<>();
		String contractCd = AppContexts.user().contractCode();
		if(domainEvent.isEffective()){
			for(PersonInfoCategory x : ctgLst){
				String name = "";
				if(lstCtgCd1.contains(x.getCategoryCode().v())){
					name = domainEvent.getSpecialHolidayName().v() + resources.localize("CPS001_133").get();
				}else if(lstCtgCd2.contains(x.getCategoryCode().v())){
					name = domainEvent.getSpecialHolidayName().v() + resources.localize("CPS001_134").get();
				}
				x.setDomainNameAndAbolition(new CategoryName(name), 0);
				ctgUpdateList.add(x);
				updateItems.addAll(getUpdateItems(domainEvent.getSpecialHolidayName().v(), x.getCategoryCode().v(), contractCd, true, loginCompanyId));
			}
		}else{
			String updateCompanyId = AppContexts.user().zeroCompanyIdInContract();
			List<PersonInfoCategory> ctgLstComZero = ctgRepo.getPerCtgByListCtgCd(ctgCds, updateCompanyId);
			
			for(PersonInfoCategory x : ctgLst){
				PersonInfoCategory ctgInComZero = ctgLstComZero.stream().filter(c -> {
					return c.getCategoryCode().v().equals(x.getCategoryCode().v());
				}).collect(Collectors.toList()).get(0);
				x.setDomainNameAndAbolition(ctgInComZero.getCategoryName(), 1);
				ctgUpdateList.add(x);
				updateItems.addAll(getUpdateItems(domainEvent.getSpecialHolidayName().v(), x.getCategoryCode().v(), contractCd, true, loginCompanyId, updateCompanyId));
			}
			
		}
		ctgRepo.updateAbolition(ctgUpdateList, loginCompanyId);
		itemRepo.updateItemDefNameAndAbolition(updateItems, loginCompanyId);
	}
	
	private List<PersonInfoItemDefinition> getUpdateItems(String spHDName, String ctgId, String contractCode, boolean isEffective, String... companyId){
		List<PersonInfoItemDefinition> lstItem = itemRepo.getItemDefByCtgCdAndComId(ctgId, companyId[0]);
		List<PersonInfoItemDefinition> lstReturn = new ArrayList<>();
		if(isEffective){
			for( PersonInfoItemDefinition x : lstItem) {
				Optional<String> newItemName = getNewItemName(x.getItemCode().v(), spHDName);
				if(newItemName.isPresent()){
					x.setIsAbolition(EnumAdaptor.valueOf(0, IsAbolition.class));
					x.setItemName(newItemName.get());	
					lstReturn.add(x);
				}				
			}
		}else{
			Map<String, String> mapItemNameInZeroCom = itemRepo.getItemDefByCtgCdAndComId(ctgId, companyId[1])
					.stream().collect(Collectors.toMap(x -> x.getItemCode().v(), x -> x.getItemName().v()));
			for( PersonInfoItemDefinition x : lstItem) {
				String itemCode = x.getItemCode().v();
				if(mapItemNameInZeroCom.containsKey(itemCode)){
					x.setIsAbolition(EnumAdaptor.valueOf(1, IsAbolition.class));
					x.setItemName(mapItemNameInZeroCom.get(itemCode));	
					lstReturn.add(x);
				}				
			}
		}
		return lstReturn;
	}

	private List<String> getCtgCds(int spcHdCode){
		List<String> ctgCds = null;
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		map.put(1, Arrays.asList(new String[]{"CS00025", "CS00039"}));
		map.put(2, Arrays.asList(new String[]{"CS00026", "CS00040"}));
		map.put(3, Arrays.asList(new String[]{"CS00027", "CS00041"}));
		map.put(4, Arrays.asList(new String[]{"CS00028", "CS00042"}));
		map.put(5, Arrays.asList(new String[]{"CS00029", "CS00043"}));
		map.put(6, Arrays.asList(new String[]{"CS00030", "CS00044"}));
		map.put(7, Arrays.asList(new String[]{"CS00031", "CS00045"}));
		map.put(8, Arrays.asList(new String[]{"CS00032", "CS00046"}));
		map.put(9, Arrays.asList(new String[]{"CS00033", "CS00047"}));
		map.put(10, Arrays.asList(new String[]{"CS00034", "CS00048"}));
		map.put(11, Arrays.asList(new String[]{"CS00049", "CS00059"}));
		map.put(12, Arrays.asList(new String[]{"CS00050", "CS00060"}));
		map.put(13, Arrays.asList(new String[]{"CS00051", "CS00061"}));
		map.put(14, Arrays.asList(new String[]{"CS00052", "CS00062"}));
		map.put(15, Arrays.asList(new String[]{"CS00053", "CS00063"}));
		map.put(16, Arrays.asList(new String[]{"CS00054", "CS00064"}));
		map.put(17, Arrays.asList(new String[]{"CS00055", "CS00065"}));
		map.put(18, Arrays.asList(new String[]{"CS00056", "CS00066"}));
		map.put(19, Arrays.asList(new String[]{"CS00057", "CS00067"}));
		map.put(20, Arrays.asList(new String[]{"CS00058", "CS00068"}));
		
		ctgCds = map.get(spcHdCode);
		return ctgCds;		
	}
	
	private Optional<String> getNewItemName(String itemCode, String name){
		if(mapText == null){
			mapText = new HashMap<>();
			mapText.put(1, name + resources.localize("CPS001_139").get());
			mapText.put(2, name + resources.localize("CPS001_140").get());
			mapText.put(3, name + resources.localize("CPS001_141").get());
			mapText.put(4, name + resources.localize("CPS001_142").get());
			mapText.put(5, name + resources.localize("CPS001_143").get());
			mapText.put(6, name + resources.localize("CPS001_144").get());
			mapText.put(7, name + resources.localize("CPS001_145").get());
			mapText.put(8, name + resources.localize("CPS001_135").get());
			mapText.put(9, name + resources.localize("CPS001_136").get());
			mapText.put(10, name + resources.localize("CPS001_137").get());
			mapText.put(11, name + resources.localize("CPS001_138").get());
		}
		if(mapICdFull.containsKey(itemCode)){
			int key = mapICdFull.get(itemCode);
			return Optional.of(mapText.get(key));
		}
		return Optional.empty();
	}
	private Map<Integer, String> mapText = null;
	private static Map<String, Integer> mapICdFull = new HashMap<>();
	static {
		Map<String, Integer> mapICd = new HashMap<>();
		mapICd.put("IS00295", 1);mapICd.put("IS00296", 2);mapICd.put("IS00297", 3);mapICd.put("IS00298", 4);mapICd.put("IS00299", 5);mapICd.put("IS00300", 6);mapICd.put("IS00301", 7);
		mapICd.put("IS00409", 8);mapICd.put("IS00410", 9);mapICd.put("IS00411", 10);mapICd.put("IS00412", 11);
		
		mapICd.put("IS00302", 1);mapICd.put("IS00303", 2);mapICd.put("IS00304", 3);mapICd.put("IS00305", 4);mapICd.put("IS00306", 5);mapICd.put("IS00307", 6);mapICd.put("IS00308", 7);
		mapICd.put("IS00424", 8);mapICd.put("IS00425", 9);mapICd.put("IS00426", 10);mapICd.put("IS00427", 11);
		
		mapICd.put("IS00309", 1);mapICd.put("IS00310", 2);mapICd.put("IS00311", 3);mapICd.put("IS00312", 4);mapICd.put("IS00313", 5);mapICd.put("IS00314", 6);mapICd.put("IS00315", 7);
		mapICd.put("IS00439", 8);mapICd.put("IS00440", 9);mapICd.put("IS00441", 10);mapICd.put("IS00442", 11);
		
		mapICd.put("IS00316", 1);mapICd.put("IS00317", 2);mapICd.put("IS00318", 3);mapICd.put("IS00319", 4);mapICd.put("IS00320", 5);mapICd.put("IS00321", 6);mapICd.put("IS00322", 7);
		mapICd.put("IS00454", 8);mapICd.put("IS00455", 9);mapICd.put("IS00456", 10);mapICd.put("IS00457", 11);
		
		mapICd.put("IS00323", 1);mapICd.put("IS00324", 2);mapICd.put("IS00325", 3);mapICd.put("IS00326", 4);mapICd.put("IS00327", 5);mapICd.put("IS00328", 6);mapICd.put("IS00329", 7);
		mapICd.put("IS00469", 8);mapICd.put("IS00470", 9);mapICd.put("IS00471", 10);mapICd.put("IS00472", 11);
		
		mapICd.put("IS00330", 1);mapICd.put("IS00331", 2);mapICd.put("IS00332", 3);mapICd.put("IS00333", 4);mapICd.put("IS00334", 5);mapICd.put("IS00335", 6);mapICd.put("IS00336", 7);
		mapICd.put("IS00484", 8);mapICd.put("IS00485", 9);mapICd.put("IS00486", 10);mapICd.put("IS00487", 11);
		
		mapICd.put("IS00337", 1);mapICd.put("IS00338", 2);mapICd.put("IS00339", 3);mapICd.put("IS00340", 4);mapICd.put("IS00341", 5);mapICd.put("IS00342", 6);mapICd.put("IS00343", 7);
		mapICd.put("IS00499", 8);mapICd.put("IS00500", 9);mapICd.put("IS00501", 10);mapICd.put("IS00502", 11);
		
		mapICd.put("IS00344", 1);mapICd.put("IS00345", 2);mapICd.put("IS00346", 3);mapICd.put("IS00347", 4);mapICd.put("IS00348", 5);mapICd.put("IS00349", 6);mapICd.put("IS00350", 7);
		mapICd.put("IS00514", 8);mapICd.put("IS00515", 9);mapICd.put("IS00516", 10);mapICd.put("IS00517", 11);
		
		mapICd.put("IS00351", 1);mapICd.put("IS00352", 2);mapICd.put("IS00353", 3);mapICd.put("IS00354", 4);mapICd.put("IS00355", 5);mapICd.put("IS00356", 6);mapICd.put("IS00357", 7);
		mapICd.put("IS00529", 8);mapICd.put("IS00530", 9);mapICd.put("IS00531", 10);mapICd.put("IS00532", 11);
		
		mapICd.put("IS00358", 1);mapICd.put("IS00359", 2);mapICd.put("IS00360", 3);mapICd.put("IS00361", 4);mapICd.put("IS00362", 5);mapICd.put("IS00363", 6);mapICd.put("IS00364", 7);
		mapICd.put("IS00544", 8);mapICd.put("IS00545", 9);mapICd.put("IS00546", 10);mapICd.put("IS00547", 11);
		
		mapICd.put("IS00559", 1);mapICd.put("IS00560", 2);mapICd.put("IS00561", 3);mapICd.put("IS00562", 4);mapICd.put("IS00563", 5);mapICd.put("IS00564", 6);mapICd.put("IS00565", 7);
		mapICd.put("IS00629", 8);mapICd.put("IS00630", 9);mapICd.put("IS00631", 10);mapICd.put("IS00632", 11);
		
		mapICd.put("IS00566", 1);mapICd.put("IS00567", 2);mapICd.put("IS00568", 3);mapICd.put("IS00569", 4);mapICd.put("IS00570", 5);mapICd.put("IS00571", 6);mapICd.put("IS00572", 7);
		mapICd.put("IS00644", 8);mapICd.put("IS00645", 9);mapICd.put("IS00646", 10);mapICd.put("IS00647", 11);
		
		mapICd.put("IS00573", 1);mapICd.put("IS00574", 2);mapICd.put("IS00575", 3);mapICd.put("IS00576", 4);mapICd.put("IS00577", 5);mapICd.put("IS00578", 6);mapICd.put("IS00579", 7);
		mapICd.put("IS00659", 8);mapICd.put("IS00660", 9);mapICd.put("IS00661", 10);mapICd.put("IS00662", 11);
		
		mapICd.put("IS00580", 1);mapICd.put("IS00581", 2);mapICd.put("IS00582", 3);mapICd.put("IS00583", 4);mapICd.put("IS00584", 5);mapICd.put("IS00585", 6);mapICd.put("IS00586", 7);
		mapICd.put("IS00674", 8);mapICd.put("IS00675", 9);mapICd.put("IS00676", 10);mapICd.put("IS00677", 11);
		
		mapICd.put("IS00587", 1);mapICd.put("IS00588", 2);mapICd.put("IS00589", 3);mapICd.put("IS00590", 4);mapICd.put("IS00591", 5);mapICd.put("IS00592", 6);mapICd.put("IS00593", 7);
		mapICd.put("IS00689", 8);mapICd.put("IS00690", 9);mapICd.put("IS00691", 10);mapICd.put("IS00692", 11);
		
		mapICd.put("IS00594", 1);mapICd.put("IS00595", 2);mapICd.put("IS00596", 3);mapICd.put("IS00597", 4);mapICd.put("IS00598", 5);mapICd.put("IS00599", 6);mapICd.put("IS00600", 7);
		mapICd.put("IS00704", 8);mapICd.put("IS00705", 9);mapICd.put("IS00706", 10);mapICd.put("IS00707", 11);
		
		mapICd.put("IS00601", 1);mapICd.put("IS00602", 2);mapICd.put("IS00603", 3);mapICd.put("IS00604", 4);mapICd.put("IS00605", 5);mapICd.put("IS00606", 6);mapICd.put("IS00607", 7);
		mapICd.put("IS00719", 8);mapICd.put("IS00720", 9);mapICd.put("IS00721", 10);mapICd.put("IS00722", 11);
		
		mapICd.put("IS00608", 1);mapICd.put("IS00609", 2);mapICd.put("IS00610", 3);mapICd.put("IS00611", 4);mapICd.put("IS00612", 5);mapICd.put("IS00613", 6);mapICd.put("IS00614", 7);
		mapICd.put("IS00719", 8);mapICd.put("IS00720", 9);mapICd.put("IS00721", 10);mapICd.put("IS00722", 11);
		
		mapICd.put("IS00615", 1);mapICd.put("IS00616", 2);mapICd.put("IS00617", 3);mapICd.put("IS00618", 4);mapICd.put("IS00619", 5);mapICd.put("IS00620", 6);mapICd.put("IS00621", 7);
		mapICd.put("IS00749", 8);mapICd.put("IS00750", 9);mapICd.put("IS00751", 10);mapICd.put("IS00752", 11);
		
		mapICd.put("IS00622", 1);mapICd.put("IS00623", 2);mapICd.put("IS00624", 3);mapICd.put("IS00625", 4);mapICd.put("IS00626", 5);mapICd.put("IS00627", 6);mapICd.put("IS00628", 7);
		mapICd.put("IS00764", 8);mapICd.put("IS00765", 9);mapICd.put("IS00766", 10);mapICd.put("IS00767", 11);
		
		mapICdFull = Collections.unmodifiableMap(mapICd);
	}
	@Override
	public void handlerTest(boolean isEffective, String name, int code) {
		handle(new SpecialHolidayEvent(isEffective, new SpecialHolidayCode(code), new SpecialHolidayName(name)));
		
	}
	
}
