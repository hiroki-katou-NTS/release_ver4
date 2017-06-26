package nts.uk.shr.infra.file.upload;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.arc.layer.infra.file.upload.command.FileUploadCommand;
import nts.arc.layer.infra.file.upload.command.IFileUpload;
import nts.uk.shr.infra.file.upload.qualifier.SavePermanetly;

@Path("/ntscommons/arc/filegate")
public class FileUploadWebService {

	@Inject
	@SavePermanetly
	private FileUploadCommand command;

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public List<StoredFileInfo> upload(@MultipartForm MultipartFormDataInput input) {

		IFileUpload fileUpload = new DefaultFileUploadInvoker(command);
		return  fileUpload.upload(input);
	}

}
