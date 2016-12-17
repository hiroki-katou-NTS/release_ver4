package nts.uk.shr.web.component.filetag;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent(tagName = "stylefile", createTag = true)
public class StyleFile extends UIComponentBase {
    
    private static Set<String> FILES_BASIC = new LinkedHashSet<String>(Arrays.asList(new String[] {
            "/lib/generic/jqueryui/jquery-ui.css",
            "/lib/generic/igniteui/css/themes/infragistics/infragistics.theme.css",
            "/lib/generic/igniteui/css/structure/infragistics.css",
            "/lib/nittsu/ui/style/stylesheets/base.css",
    }));

    @SuppressWarnings("serial")
    private static Map<String, Set<String>> FILE_SETS = new HashMap<String, Set<String>>() {
        {
            this.put("BASIC", FILES_BASIC);
        }
    };
    
    /**
     * Return a family name
     * 
     * @return family name
     */
    @Override
    public String getFamily() {
        return this.getClass().getName();
    }

    /**
     * Render beginning of component
     * 
     * @param context FacesContext
     * @throws IOException IOException
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        ResponseWriter rw = context.getResponseWriter();
        
        String filePath = (String) this.getAttributes().get("path");
        
        if (filePath != null) {
        	writeTag(rw, FileTagsHelper.buildPath(context, filePath));
        } else {
            String fileSet = (String) this.getAttributes().get("set");
            writeTagSet(rw, context, fileSet);
        }
    }

    private static void writeTag(ResponseWriter rw, String filePath) {
        
        try {
            rw.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
            rw.write(filePath);
            rw.write("\" />\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void writeTagSet(ResponseWriter rw, FacesContext context, String fileSet) {
        
        FILE_SETS.get(fileSet).stream().forEach(
                filePath -> writeTag(rw, FileTagsHelper.buildPath(context, filePath)));
    }
}
