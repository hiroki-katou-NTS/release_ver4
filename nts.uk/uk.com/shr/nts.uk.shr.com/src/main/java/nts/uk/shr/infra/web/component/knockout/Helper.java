package nts.uk.shr.infra.web.component.knockout;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

final class Helper {
	
	static void writeBegin(String tag, String bind, ResponseWriter rw) throws IOException {
		rw.write("<!-- ko ");
        rw.write(tag);
        rw.write(": ");
        rw.write(bind);
        rw.write(" -->");
	}

	static void writeEnd(ResponseWriter rw) throws IOException {
		rw.write("<!-- /ko -->");
	}
	
}
