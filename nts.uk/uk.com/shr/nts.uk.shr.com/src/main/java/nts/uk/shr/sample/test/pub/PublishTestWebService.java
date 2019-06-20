package nts.uk.shr.sample.test.pub;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Produces(MediaType.APPLICATION_JSON)
@Path("/sample/test/pub")
public class PublishTestWebService {

	@POST
	@Path("test")
	@SneakyThrows
	public JavaTypeResult<Object> test() {
		Class<?> pubClass = Class.forName("nts.uk.ctx.bs.employee.pub.classification.SyClassificationPub");
		
		Class<?>[] paramClasses = new Class<?>[] { String.class, List.class };

		Object pubInstance = CDI.current().select(pubClass).get();
		Method method = pubInstance.getClass().getMethod("getClassificationMapCodeName", paramClasses);
		Object[] params = new Object[] { "000000000000-0001", Arrays.asList("0000000001", "0000000004") };
		
		Object result = method.invoke(pubInstance, params);
		return new JavaTypeResult<Object>(result);
	}
	
	@POST
	@Path("invoke")
	@SneakyThrows
	public JavaTypeResult<Object> invoke(InvokeParams params) {
		
		Class<?> pubClass = params.createPubClass();
		Object pubInstance = CDI.current().select(pubClass).get();
		Method method = params.createMethod(pubInstance.getClass());

		Object result = method.invoke(pubInstance, params.createParams());
		return new JavaTypeResult<Object>(result);
	}
	
	@Data
	public static class InvokeParams {
		String pubClass;
		String method;
		List<Param> params;
		
		public Class<?> createPubClass() {
			return classOf(this.pubClass);
		}
		
		@SneakyThrows
		public Method createMethod(Class<?> targetClass) {
			List<Class<?>> paramClasses = this.createParamClasses().stream()
					.map(c -> (Class<?>)c)
					.collect(Collectors.toList());
			Class<?>[] array = new Class<?>[paramClasses.size()];
			paramClasses.toArray(array);
			
			return targetClass.getMethod(this.method, array);
		}
		
		public Object[] createParams() {
			return params.stream().map(p -> p.createValue()).toArray();
		}
		
		private List<Class<?>> createParamClasses() {
			return this.params.stream()
					.map(p -> p.createClass())
					.collect(Collectors.toList());
		}
		
		@SneakyThrows
		private static Class<?> classOf(String name) {
			if (name.contains(".")) {
				return Class.forName(name);
			}
			
			switch (name.toLowerCase()) {
			case "list":
				return List.class;
			case "string":
				return String.class;
			case "int":
			case "integer":
				return Integer.class;
			case "boolean":
				return Boolean.class;
			default:
				throw new RuntimeException("not supported: " + name);
			}
		}
	}
	
	@Data
	public static class Param {
		String type;
		Object value;
		
		public Class<?> createClass() {
			switch (type.toLowerCase()) {
			case "generaldate":
				return GeneralDate.class;
			case "list":
				return List.class;
			case "string":
				return String.class;
			case "int":
			case "integer":
				return Integer.class;
			case "boolean":
				return Boolean.class;
			default:
				throw new RuntimeException("not supported: " + type);
			}
		}
		
		public Object createValue() {
			switch (type.toLowerCase()) {
			case "generaldate":
				return date(value.toString());
			case "dateperiod":
				val parts = value.toString().split("-");
				return new DatePeriod(date(parts[0]), date(parts[1]));
			default:
				return value;
			}
		}
		
		private static GeneralDate date(String dateString) {
			return GeneralDate.fromString(dateString, "yyyyMMdd");
		}
	}
}
