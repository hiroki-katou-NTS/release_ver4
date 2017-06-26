package nts.uk.shr.infra.web.component.knockout;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * Knockout.js コンテナレステンプレートのforeachを生成する
 */
@FacesComponent(tagName = "ko-foreach", createTag = true)
public class KoForeach extends UIComponentBase {

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

        Helper.writeBegin(
        		"foreach",
        		(String) getAttributes().get("bind"),
        		context.getResponseWriter());
    }

    /**
     * Render ending of component
     *
     * @param context FacesContext
     * @throws IOException IOException
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
    	
        Helper.writeEnd(context.getResponseWriter());
    }
}
