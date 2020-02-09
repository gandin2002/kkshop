package chi.data.container;

//数据存储的行，每个数据单元在设置值的时候都有类型检查
public class Row {
	private Meta meta;
	private Object[] values;
	private boolean isDeleted = false;

	public Row() {
	}

	public Row(Meta meta) {
		this.meta = meta;
		if (values == null) {
			this.values = new Object[meta.getCount()];
		}
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
		if (values == null) {
			this.values = new Object[meta.getCount()];
		}
	}

	public Meta getMeta() {
		return this.meta;
	}

	public Column getColumn(String name) throws DataException {
		return meta.get(name);
	}

	public Column getColumn(int index) {
		return meta.get(index);
	}

	public void set(int index, Object value) throws DataException {

		Column col = getColumn(index);
		if (!col.isNULL() && value == null) {
			throw new DataException("the column[" + col.getName()
					+ "]can not be null!");
		}
		if (value != null && TypeCompare.typeCompare(value, col.getType())) {
			this.values[index] = value;
		} else if (value == null) {
			this.values[index] = null;
		} else {
			throw new DataException("The Type of Column[" + col.getName()
					+ "] is " + col.getType().toString()
					+ ",It is not compareable with [" + value.toString() + "]!");
		}
	}

	public void set(String name, Object value) throws DataException {
		this.set(this.meta.get(name).getIndex(), value);
	}

	public Object get(int index) {
		return this.values[index];
	}

	public Object get(String name) throws DataException {
		return this.values[this.meta.get(name).getIndex()];
	}

	public boolean IsDeleted() {
		return this.isDeleted;
	}

	public void Delete() {
		this.isDeleted = true;
	}
}
