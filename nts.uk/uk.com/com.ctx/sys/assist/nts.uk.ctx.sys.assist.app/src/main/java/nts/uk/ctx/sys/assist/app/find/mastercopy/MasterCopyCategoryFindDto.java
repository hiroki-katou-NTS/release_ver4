package nts.uk.ctx.sys.assist.app.find.mastercopy;

import lombok.Data;

/**
 * The Class MasterCopyCategoryFindDto.
 */
@Data
public class MasterCopyCategoryFindDto {
	
	/** The master copy id. */
	private String masterCopyId;
	
	/** The order. */
	private Integer order;
	
	/** The system type. */
	private String systemType;
	
	/** The master copy category. */
	private String masterCopyCategory;
	
	/**
	 * Instantiates a new master copy category find dto.
	 */
	public MasterCopyCategoryFindDto(){
		super();
	}
	
	/**
	 * Instantiates a new master copy category find dto.
	 *
	 * @param systemType the system type
	 * @param masterCopyCategory the master copy category
	 */
	public MasterCopyCategoryFindDto(String systemType, String masterCopyCategory, Integer order, String masterCopyId){
		this.systemType = systemType;
		this.masterCopyCategory = masterCopyCategory;
		this.order = order;
		this.masterCopyId = masterCopyId;
	}
}
