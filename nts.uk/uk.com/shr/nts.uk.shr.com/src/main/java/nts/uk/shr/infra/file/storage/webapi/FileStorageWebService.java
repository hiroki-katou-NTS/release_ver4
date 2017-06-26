package nts.uk.shr.infra.file.storage.webapi;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import lombok.val;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.file.storage.StoredFileInfoRepository;
import nts.arc.layer.infra.file.storage.StoredFileStreamService;
import nts.gul.web.URLEncode;

@Path("/shr/infra/file/storage")
public class FileStorageWebService {

	@Inject
	private StoredFileInfoRepository fileInfoRepository;

	@Inject
	private StoredFileStreamService fileStreamService;

	@GET
	@Path("get/{fileid}")
	public Response download(@PathParam("fileid") String fileId) {

		return this.fileInfoRepository.find(fileId).map(fileInfo -> this.buildFileResponse(fileInfo))
				.orElseThrow(() -> new RuntimeException("stored file info is not found."));
	}

	private Response buildFileResponse(StoredFileInfo fileInfo) {

		val fileInputStream = this.getInputStream(fileInfo);

		return Response.ok(fileInputStream, fileInfo.getMimeType()).encoding("UTF-8")
				.header("Content-Disposition", contentDisposition(fileInfo)).build();
	}

	private InputStream getInputStream(StoredFileInfo fileInfo) {
		if (fileInfo.isTemporary()) {
			this.fileInfoRepository.delete(fileInfo.getId());
			return this.fileStreamService.takeOutDeleteOnClosed(fileInfo);
		} else {
			return this.fileStreamService.takeOut(fileInfo);
		}
	}

	static String contentDisposition(StoredFileInfo fileInfo) {
		String encodedName = URLEncode.encodeAsUtf8(fileInfo.getOriginalName());
		return String.format("attachment; filename=\"%s\"", encodedName);
	}

	@GET
	@Path("liveview/{fileid}")
	public Response liveview(@PathParam("fileid") String fileId) {

		return this.fileInfoRepository.find(fileId).map(fileInfo -> this.buildFileResponseInLine(fileInfo))
				.orElseThrow(() -> new RuntimeException("stored file info is not found."));
	}

	private Response buildFileResponseInLine(StoredFileInfo fileInfo) {

		val fileInputStream = this.getInputStream(fileInfo);

		return Response.ok(fileInputStream, fileInfo.getMimeType()).encoding("UTF-8")
				.header("Content-Disposition", "inline").build();
	}
}
