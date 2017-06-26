package nts.uk.shr.infra.web.component.enums;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.uk.shr.infra.web.component.internal.TagContentsUtil;

@FacesComponent(tagName = "EnumScript", createTag = true)
public class EnumScript extends UIComponentBase {

	private static final String JS_VAR_NAME = "__viewContext.enums";
	
	@Override
	public String getFamily() {
		return this.getClass().getName();
	}

	@Override
    public void encodeBegin(FacesContext context) throws IOException {
		
        val rw = context.getResponseWriter();
        
        rw.write("<script>");
        rw.write("\t");
        rw.write(JS_VAR_NAME);
        rw.write(" = ");
        rw.write(JS_VAR_NAME);
        rw.write(" || {};");

        val enumFqns = TagContentsUtil.readMultipleLinesString(this.getChildren().get(0).toString());
        for(String fqn : enumFqns) {
        	writeEnumConstant(rw, fqn);
        }

        rw.write("</script>");
        
        // if not cleared, content strings is shown on browser...
        this.getChildren().clear();
    }
	
	private static void writeEnumConstant(ResponseWriter rw, String enumFqn) throws IOException {
		
		@SuppressWarnings("unchecked")
		val enumClass = (Class<Enum<?>>) TagContentsUtil.findClass(enumFqn).get();
		String enumName = enumClass.getSimpleName();
		
		rw.append("\n\t");
        rw.write(JS_VAR_NAME);
        rw.write(".");
		rw.append(enumName);
		rw.append(" = [");
		
		val enumFields = EnumAdaptor.convertToValueNameList(enumClass);
		for (val field : enumFields) {
			rw.append("\n\t\t{");
			rw.append("value:");
			rw.append(String.valueOf(field.getValue()));
			rw.append(",name:'");
			rw.append(field.getLocalizedName());
			rw.append("'},");
		}
		
		rw.append("\n\t];");
	}
}
