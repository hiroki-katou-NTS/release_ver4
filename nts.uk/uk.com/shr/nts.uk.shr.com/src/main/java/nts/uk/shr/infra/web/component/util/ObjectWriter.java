package nts.uk.shr.infra.web.component.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ResponseWriter;

import lombok.Value;
import lombok.val;

public class ObjectWriter {

	private final ResponseWriter rw;
	private final List<Item> items;
	
	private ObjectWriter(ResponseWriter rw) {
		this.rw = rw;
		this.items = new ArrayList<>();
	}
	
	public static ObjectWriter start(ResponseWriter rw) {
		return new ObjectWriter(rw);
	}
	
	public ObjectWriter put(String name, String value) {
		return this.putItem(name, "'" + value + "'");
	}
	
	public ObjectWriter put(String name, int value) {
		return this.putItem(name, Integer.toString(value));
	}
	
	public ObjectWriter put(String name, boolean value) {
		return this.putItem(name, value ? "true" : "false");
	}
	
	private ObjectWriter putItem(String name, String valueFormatted) {
		this.items.add(new Item(name, valueFormatted));
		return this;
	}
	
	public void finish() throws IOException {
		this.rw.write("{");
		
		boolean isFirst = true;
		for (val item : this.items) {
			if (!isFirst) {
				this.rw.write(",");
			}
			isFirst = false;
			
			this.rw.write(item.getName());
			this.rw.write(":");
			this.rw.write(item.getValue());
		}
		
		this.rw.write("}");
	}
	
	@Value
	private static class Item {
		private final String name;
		private final String value;
	}
	
}
