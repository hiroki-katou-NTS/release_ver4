package nts.uk.cnv.app.cnv.dto;

import lombok.Data;

@Data
public class ExportToFileDto {
	String path;
	String type;
	boolean withComment;
	boolean oneFile;
}
