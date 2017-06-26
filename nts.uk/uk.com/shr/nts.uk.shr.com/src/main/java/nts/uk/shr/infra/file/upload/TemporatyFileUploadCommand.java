package nts.uk.shr.infra.file.upload;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.storage.FileStorage;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.file.upload.command.FileUploadCommand;
import nts.arc.layer.infra.file.upload.command.UploadedFile;
import nts.gul.text.IdentifierUtil;
import nts.uk.shr.infra.file.upload.qualifier.SaveTemporary;

@Stateless
@SaveTemporary
public class TemporatyFileUploadCommand implements FileUploadCommand {
	@Inject
	private FileStorage fileStorage;

	@Override
	public StoredFileInfo upload(UploadedFile uploadedFile) throws IOException {

		StoredFileInfo fileInfor = fileStorage.storeTemporary(IdentifierUtil.randomUniqueId(),
				uploadedFile.getInputFile().toPath(), uploadedFile.getFileName());

		return fileInfor;
	}

}
