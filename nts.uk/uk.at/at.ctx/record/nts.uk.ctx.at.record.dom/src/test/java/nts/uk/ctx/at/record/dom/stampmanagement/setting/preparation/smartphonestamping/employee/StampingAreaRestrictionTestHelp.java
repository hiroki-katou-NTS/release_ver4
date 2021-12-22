package nts.uk.ctx.at.record.dom.stampmanagement.setting.preparation.smartphonestamping.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.gul.location.GeoCoordinate;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.RadiusAtr;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.StampMobilePossibleRange;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocation;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocationName;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkplacePossible;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkLocationCD;
import nts.uk.shr.com.net.Ipv4Address;

/**
 * Test 打刻エリア制限 
 * 
 * @author vandv_NWS
 *
 */
public class StampingAreaRestrictionTestHelp {
	
	public static final String CID = "000000000004-0004";
	public static final String SID = "000000000001";
	public static final String CONTRACTCD = "000000000004"; 
	
	//エリア外の打刻
	public static StampingAreaRestriction iseUseLocation() {
		NotUseAtr notUseAtr = NotUseAtr.NOT_USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.NO_AREA_RESTRICTION;			
		return new StampingAreaRestriction(notUseAtr ,areaLimit);
	}
	//エリアが特定できない
	public static StampingAreaRestriction isUserGeoCoordinate () {
		NotUseAtr notUseAtr = NotUseAtr.USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.ALLOWED_ONLY_WITHIN_THE_AREA ;
		return new StampingAreaRestriction(notUseAtr,areaLimit);
	}
	public static StampingAreaRestriction useOnlyWorkplaceAndLoacation() {
		NotUseAtr notUseAtr = NotUseAtr.USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.ONLY_THE_WORKPLACE_BELONG_ALLOWED ;
		return new StampingAreaRestriction(notUseAtr,areaLimit);
	}
	public static StampingAreaRestriction useWithinAreaAndLoacation() {
		NotUseAtr notUseAtr = NotUseAtr.USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.ALLOWED_ONLY_WITHIN_THE_AREA ;
		return new StampingAreaRestriction(notUseAtr,areaLimit);
	}

	public static StampingAreaRestriction notUseLoacationAndUseOnlyWorkplace() {
		NotUseAtr notUseAtr = NotUseAtr.NOT_USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.ONLY_THE_WORKPLACE_BELONG_ALLOWED ;
		return new StampingAreaRestriction(notUseAtr,areaLimit);
	}
	public static StampingAreaRestriction notUseAreanAndUseOnlyWorkplace() {
		NotUseAtr notUseAtr = NotUseAtr.USE;
		StampingAreaLimit areaLimit = StampingAreaLimit.NO_AREA_RESTRICTION ;
		return new StampingAreaRestriction(notUseAtr,areaLimit);
	}
	
	public static WorkLocation createWorklocation(String contractCode, String workLocationCD,String workLocationName) {
		
		ContractCode contractCd = new ContractCode(contractCode);
		WorkLocationCD workLocaCD = new WorkLocationCD(workLocationCD);
		WorkLocationName nameWorkLocation = new WorkLocationName(workLocationName);
		RadiusAtr atr = RadiusAtr.M_100;
		GeoCoordinate geoCoordinate = getGeoCoordinateDefault();
		StampMobilePossibleRange stampRange = new StampMobilePossibleRange(atr, geoCoordinate);
		List<Ipv4Address> listIPAddress = new ArrayList<>();
		Optional<WorkplacePossible> workplace = Optional.of(new WorkplacePossible("dummy","dummy"));
		WorkLocation location = new WorkLocation(contractCd, workLocaCD, nameWorkLocation, stampRange, listIPAddress, workplace);
		return location;
	}
	
	public static List<WorkLocation> createDataForFindAll(String contractCd, String workLocaCD,String workLocaName) {
		ContractCode contracttCd = new ContractCode(contractCd);
		WorkLocationCD workLocationCD = new WorkLocationCD(workLocaCD);
		RadiusAtr atr = RadiusAtr.M_100;
		WorkLocationName workLocationName = new WorkLocationName(workLocaName);
		GeoCoordinate geoCoordinate = getGeoCoordinateDefault();
		StampMobilePossibleRange stampRange = new StampMobilePossibleRange(atr, geoCoordinate);
		Optional<WorkplacePossible> workplace = Optional.of(new WorkplacePossible("dummy","dummy"));
		List<Ipv4Address> listIPAddress = new ArrayList<>();
		List<WorkLocation>listWork = new ArrayList<>();	
		WorkLocation location = new WorkLocation(contracttCd, workLocationCD,workLocationName,stampRange, listIPAddress, workplace);
		listWork.add(location);
		return listWork;
	}
	
	public static GeoCoordinate getGeoCoordinateDefault() {
		return new GeoCoordinate(1, 2);
	}
	public static GeoCoordinate getGeoCoordinateDefaultNo() {
		return null;
	}
	
}
