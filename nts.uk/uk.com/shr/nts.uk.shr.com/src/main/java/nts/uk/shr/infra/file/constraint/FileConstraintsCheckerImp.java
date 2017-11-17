package nts.uk.shr.infra.file.constraint;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.file.constraint.FileConstraintsChecker;
import nts.arc.layer.infra.file.constraint.FileStereo;

@Stateless
public class FileConstraintsCheckerImp implements FileConstraintsChecker {

	@Override
	public void canDownload(String stereoTypeName) {
	}

	@Override
	public void canUpload(StoredFileInfo fileInfor) {
		if (!Objects.nonNull(fileInfor)) return;

		Optional<FileStereo> stereo = FileStereoFactory.of(fileInfor.getFileType());
		if (stereo.isPresent()) {
			FileStereo fileStereo = stereo.get();
			// check size
			if (fileInfor.getOriginalSize() > fileStereo.getLimitedSize()) {
				throw new BusinessException(
						I18NText.main("Msg_70").addRaw(fileStereo.getLimitedSize() / (1024.0 * 1024.0)).build());
			}
			// authorization
			// TODO:
			// check extension
			if (!fileStereo.getSupportedExtension().isEmpty()) {
				if (fileStereo.getSupportedExtension().stream().map(x -> x.toLowerCase())
						.filter(x -> x.equals(getFileExtension(fileInfor.getOriginalName()))).count() == 0)
					throw new BusinessException(I18NText.main("Msg_77")
							.addRaw(fileStereo.getSupportedExtension().stream().collect(Collectors.joining(",")))
							.build());
			}
		}
	}

	// ignore case sensitive
	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1 || index == fileName.length() - 1) {
			return Strings.EMPTY;
		}

		return fileName.substring(index + 1).toLowerCase();
	}

	@Override
	public void canDelete(String stereoTypeName) {
	}
}
