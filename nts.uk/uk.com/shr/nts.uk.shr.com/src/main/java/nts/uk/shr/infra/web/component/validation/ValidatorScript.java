package nts.uk.shr.infra.web.component.validation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import lombok.val;
import nts.arc.primitive.constraint.PrimitiveValueConstraintPackage;
import nts.uk.shr.infra.web.component.internal.TagContentsUtil;

/**
 * ValidatorScript
 * 
 * @author hoanld
 */
@FacesComponent(tagName = "ValidatorScript", createTag = true)
public class ValidatorScript extends UIComponentBase {
	
	private static final String JS_VAR_NAME = "__viewContext.primitiveValueConstraints";

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

        val primitives = TagContentsUtil.readMultipleLinesString(this.getChildren().get(0).toString());
        for(String fqnOfPrimitiveValueClass : primitives) {
        	writePrimitiveValueConstraints(rw, fqnOfPrimitiveValueClass);
        }

        rw.write("</script>");
        
        // if not cleared, content strings is shown on browser...
        this.getChildren().clear();
    }
	
	private static void writePrimitiveValueConstraints(ResponseWriter rw, String fqnOfPrimitiveValueClass) throws IOException {
		
		val pvClass = TagContentsUtil.findClass(fqnOfPrimitiveValueClass)
				.orElseThrow(() -> new RuntimeException("PrimitiveValue not found: " + fqnOfPrimitiveValueClass));
		String pvName = pvClass.getSimpleName();
		
		rw.append("\n\t");
        rw.write(JS_VAR_NAME);
        rw.write(".");
		rw.append(pvName);
		rw.append(" = {");
		
		rw.append("\n\t\tvalueType: '");
		rw.append(Helper.getValueType(pvClass));
		rw.append("',");
		writeConstraints(rw, pvClass);
		
		rw.append("\n\t};");
	}

	private static void writeConstraints(ResponseWriter rw, Class<?> pvClass) {
		
		annotationsStream(pvClass)
			.map(a -> a.toString())
			.filter(r -> r.contains(PrimitiveValueConstraintPackage.NAME))
	        .forEach(representationOfAnnotation -> {
	        	String constraintName = Helper.getAnnotationName(representationOfAnnotation);
	        	String parametersString = Helper.getAnnotationParametersString(representationOfAnnotation);
				writeConstraint(rw, constraintName, parametersString);
	        });
	}
	
	private static void writeConstraint(ResponseWriter rw, String constraintName, String parametersString) {
		
		if (Helper.CONSTRAINTS_SIGNLE_PARAM.containsKey(constraintName)) {
			String jsName = Helper.CONSTRAINTS_SIGNLE_PARAM.get(constraintName);
			String jsValue = Helper.parseSingleParameterValue(constraintName, parametersString);
			
			writeConstraintParameter(rw, jsName, jsValue);
			
		} else if (Helper.CONSTRAINTS_MAX_MIN_PARAM.contains(constraintName)) {
			val paramsMap = Helper.parseMultipleParametersString(parametersString);

			writeConstraintParameter(rw, "max", paramsMap.get("max"));
			writeConstraintParameter(rw, "min", paramsMap.get("min"));
		}
	}

	private static void writeConstraintParameter(ResponseWriter rw, String jsName, String jsValue) {
		
		try {
			rw.write("\n\t\t");
			rw.write(jsName);
			rw.write(": ");
			if (jsValue.contains(":")) {
				rw.write("'");
				rw.write(jsValue);
				rw.write("'");
			} else {
				rw.write(jsValue);
			}
			rw.write(",");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get annotations stream of pvClass and its super class.
	 * @param pvClass pvClass
	 * @return annotations stream
	 */
	private static Stream<Annotation> annotationsStream(Class<?> pvClass) {
		return Stream.concat(Arrays.asList(pvClass.getDeclaredAnnotations()).stream(), 
							Arrays.asList(pvClass.getSuperclass().getDeclaredAnnotations()).stream());
	}
}
