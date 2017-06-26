package nts.uk.shr.infra.file.report.aspose.cells;

import java.util.Map;

import com.aspose.cells.ICellsDataTable;

public class SingleMapDataSource implements ICellsDataTable {
	
    private final Map<String, Object> source;
    private final String[] columns;
    private boolean hasFetched;
    
    public SingleMapDataSource(Map<String, Object> source) {
    	this.source = source;
    	this.columns = new String[source.size()];
    	source.keySet().toArray(this.columns);
    	
    	this.hasFetched = false;
	}
    
	public String[] getColumns() {
		return this.columns;
	}
	
	public int size() {
		return 1;
	}
	
	public void beforeFirst() {
		this.hasFetched = false;
	}
	
	public Object get(int columnIndex) {
		if(columnIndex > this.columns.length) {
			return null;
		}
		
		return get(this.columns[columnIndex]);
	}
	
	public boolean next() {
		boolean canFetch = !this.hasFetched;
		this.hasFetched = true;
		return canFetch;
	}
	
    public Object get(String columnName) {
    	return this.source.get(columnName);
    }
    
    public int getCount() {
        return 1;
    }
    
}
