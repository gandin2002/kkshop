package chi.data.container;

import java.util.*;

//表头，没个行都有其的引用
public class Meta {
	private Map<String, Column> columns;
	private Map<Integer, String> index$name;
	private List<Integer> primaryKeys;

	public Meta() {
		columns = new HashMap<String, Column>();
		index$name = new HashMap<Integer, String>();
		primaryKeys = new ArrayList<Integer>();
	}

	public Column get(String name) throws DataException {
		Column result = columns.get(name);
		if (null == result) {
			int count = this.getCount();
			Column col;
			for (int i = 0; i < count; i++) {
				col = this.get(i);
				if (null == col)
					continue;
				if (col.getName().equalsIgnoreCase(name)) {
					result=col;break;
				}
			}
		}

		if (null == result) {
			throw new DataException("The Column ["+name+"]"+ " is not found");
		}
		return result;
	}

	public Column get(int index) {
		return columns.get(index$name.get(index));
	}

	public void addColumn(int index, Column col) {
		this.index$name.put(index, col.getName());
		this.columns.put(col.getName(), col);
		if (col.isPrimaryKey()) {
			this.primaryKeys.add(index);
		}
	}

	public boolean hasPrimaryKeys() {
		return 0 == this.primaryKeys.size();
	}

	public List<Integer> getPrimaryKeys() {
		return this.primaryKeys;
	}

	public int getCount() {
		return columns.size();
	}
}