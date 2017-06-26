package nts.uk.shr.infra.file.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.file.upload.command.FileUploadCommand;
import nts.arc.layer.infra.file.upload.command.IFileUpload;
import nts.arc.layer.infra.file.upload.command.UploadedFile;

public class DefaultFileUploadInvoker implements IFileUpload {
	private FileUploadCommand command;

	public DefaultFileUploadInvoker(FileUploadCommand command) {
		this.command = command;
	}

	@Override
	public List<StoredFileInfo> upload(MultipartFormDataInput input) {
		try {

			List<StoredFileInfo> uploadedFiles = new ArrayList<>();
			Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
			File uploadedFile = getUploadedFile(uploadForm);
			String uploadedFileName = getUploadedFileName(uploadForm);
			String fileStereoType = getFileStereoType(uploadForm);

			UploadedFile uploadedFileInfor = new UploadedFile(uploadedFileName, fileStereoType, uploadedFile);
			uploadedFiles.add(command.upload(uploadedFileInfor));
			return uploadedFiles;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getFileStereoType(Map<String, List<InputPart>> uploadForm) throws IOException {
		List<InputPart> inputParts = uploadForm.get("stereotype");

		for (InputPart inputPart : inputParts) {
			return inputPart.getBodyAsString();
		}
		return Strings.EMPTY;

	}

	private File getUploadedFile(Map<String, List<InputPart>> uploadForm) throws IOException {
		List<InputPart> inputParts = uploadForm.get("userfile");

		for (InputPart inputPart : inputParts) {
			File inputFile = inputPart.getBody(File.class, null);
			return inputFile;
		}
		throw new RuntimeException("file not found");
	}

	private String getUploadedFileName(Map<String, List<InputPart>> uploadForm) throws IOException {
		List<InputPart> inputParts = uploadForm.get("filename");
		for (InputPart inputPart : inputParts) {
			String fileName = inputPart.getBodyAsString();
			if (!Strings.isEmpty(fileName)) {
				String unicodeFileName = new String(fileName.getBytes("ISO-8859-1"),"UTF-8");
				return unicodeFileName;
			}
		}
		return "unknow";

	}

	/**
	 * can not get file name from content-disposition ,because of bug:
	 * https://issues.jboss.org/browse/RESTEASY-1214
	 * https://issues.jboss.org/browse/RESTEASY-546
	 * 
	 * 
	 * private String getFileName(MultipartFormDataInput input) {
	 * List<InputPart> inputParts = input.getFormDataMap().get("userfile"); for
	 * (InputPart header : inputParts) { String[] contentDisposition =
	 * header.getHeaders().getFirst("Content-Disposition").split(";");
	 * 
	 * for (String filename : contentDisposition) { if
	 * ((filename.trim().startsWith("filename"))) {
	 * 
	 * String[] name = filename.split("=");
	 * 
	 * String finalFileName = name[1].trim().replaceAll("\"", ""); return
	 * finalFileName; } } }
	 * 
	 * return "unknown"; }
	 */

}
