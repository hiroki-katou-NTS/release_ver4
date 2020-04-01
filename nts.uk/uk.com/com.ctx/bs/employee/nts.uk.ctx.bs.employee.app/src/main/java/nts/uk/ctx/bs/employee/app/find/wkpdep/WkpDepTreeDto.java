//package nts.uk.ctx.bs.employee.app.find.wkpdep;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lombok.Value;
//
///**
// * 
// * @author HungTT
// *
// */
//@Value
//public class WkpDepTreeDto {
//
//	private String id;
//	private String code;
//	private String name;
//	private String hierarchyCode;
//	private List<WkpDepTreeDto> children;
//
//
//	public static WkpDepTreeDto toTreeDto(InformationDto dto) {
//		return new WkpDepTreeDto(dto.getId(), dto.getCode(), dto.getName(), dto.getHierarchyCode(), new ArrayList<>());
//	}
//}
