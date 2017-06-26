package nts.uk.shr.infra.file.constraint.stereotypes;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.infra.file.constraint.FileStereo;

@Getter
public class ExampleStereoType implements FileStereo {
	private long limitedSize = 500000;
	private List<String> supportedExtension = new ArrayList<>();
	private List<String> downloadRights = new ArrayList<>();
	private List<String> uploadRights = new ArrayList<>();
	private List<String> deleteRights = new ArrayList<>();

}
