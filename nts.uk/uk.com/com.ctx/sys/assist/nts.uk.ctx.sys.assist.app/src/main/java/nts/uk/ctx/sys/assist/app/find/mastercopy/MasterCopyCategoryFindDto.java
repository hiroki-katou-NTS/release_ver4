package nts.uk.ctx.sys.assist.app.find.mastercopy;

import lombok.Data;

/**
 * The Class MasterCopyCategoryFindDto.
 */
@Data
public class MasterCopyCategoryFindDto {
	
	/** The system type. */
	private Integer systemType;
	
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
	public MasterCopyCategoryFindDto(Integer systemType, String masterCopyCategory){
		this.systemType = systemType;
		this.masterCopyCategory = masterCopyCategory;
	}
}
