package chi.data.container;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

//在行提交的时候有唯一约束检查
public class Table {

	private String name;
	private List<Row> rows;
	private Meta meta;
	private String schema;

	public Table() {
		if (this.rows == null) {
			this.rows = new ArrayList<Row>();
		}

	}

	public Table(String name, String xmlSchema) {
		this.name = name;
		this.schema = xmlSchema;
		if (this.rows == null) {
			this.rows = new ArrayList<Row>();
		}
	}

	public Table(String name, Meta meta) {
		this.name = name;
		this.meta = meta;
		if (this.rows == null) {
			this.rows = new ArrayList<Row>();
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Meta getMeta() {
		return this.meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Row newRow() {
		Row row = new Row();
		row.setMeta(this.meta);
		return row;
	}

	public void addRow(Row row) throws DataException {
		if (null != this.meta) {
			int hitCount = 0;
			int colIndex = 0;
			if (this.meta.hasPrimaryKeys()) {
				int pkCount = this.meta.getPrimaryKeys().size();
				for (int j = 0; j < this.rows.size(); j++) {
					{
						for (int i = 0; i < pkCount; i++) {
							colIndex = this.meta.getPrimaryKeys().get(i);
							if (rows.get(j).get(colIndex) == row.get(colIndex)) {
								hitCount++;
								if (hitCount == pkCount) {
									throw new DataException(
											"duplicate primary keys");
								}
							}
						}
					}
				}
			}
			this.rows.add(row);
		}
	}

	public void clear() {
		this.rows.clear();
	}

	public void removeAt(int index) {
		this.rows.remove(index);
	}

	public Row getRow(int index) {
		return this.rows.get(index);
	}

	public int getRowCount() {
		return this.rows.size();
	}

	public int getColumnCount() {
		return this.meta.getCount();
	}

	private static String getAttributeValue(Element item, String name) {
		String result = null;
		if (null != item && null != name) {
			result = item.attribute(name).getValue();
		}
		return result;
	}

	private static Column elementToColumn(Element item) {
		Column result = null;
		if (null != item) {
			result = new Column();
			result.setName(item.getName());
			String isPK = item.attributeValue("IsPK");
			if (null != isPK && "True".equalsIgnoreCase(isPK)) {
				result.setPrimaryKey(true);
			}
			result.setType(String.class.getName());
		}
		return result;
	}

	private static String getTableSchema(Document doc) {
		String result = null;
		if (null != doc) {
			Element root = doc.getRootElement();
			int count = null == root ? 0 : root.elements().size();
			if (1 < count) {
				for (int i = 1; i < root.elements().size();) {
					root.remove((Element) root.elements().get(i));
					i = 1;
				}
			}
			result = doc.asXML();
		}

		return result;
	}

	private static void addDataRow(Table table, Element root) throws Exception {
		int count = null == root ? 0 : root.elements().size();
		int colCount = 0;
		Row row = null;
		for (int i = 1; i < count; i++) {
			Element data = (Element) root.elements().get(i);
			colCount = null == data ? 0 : data.elements().size();
			Element col;
			row = table.newRow();
			for (int j = 0; j < colCount; j++) {
				col = (Element) data.elements().get(j);
				row.set(col.getName(), col.getText());
			}
			table.addRow(row);
		}
	}

	public static Table loadXml(String xml) throws Exception {
		Table result = null;
		try {
			Document document = DocumentHelper.parseText(xml);
			if (document.hasContent()) {

				Element root = document.getRootElement();
				if ("Table" == root.getName()) {
					String tableName = getAttributeValue(root, "name");
					int count = root.elements().size();
					if (0 < count) {
						// 克隆出来一份schema，供asXML使用
						Document cloneItem = (Document) document.clone();
						result = new Table(tableName, getTableSchema(cloneItem));

						Element columns = (Element) root.elements().get(0);
						Column col;
						Meta meta = new Meta();
						int colCount = columns.elements().size();
						for (int i = 0; i < colCount; i++) {
							Element item = (Element) columns.elements().get(i);
							col = elementToColumn(item);
							col.setIndex(i);
							meta.addColumn(i, col);
						}

						result.setMeta(meta);
						addDataRow(result, root);
					}
				}
			}

		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	public String asXML() throws Exception {
		String result = null;
		if (null != this.schema) {
			Document doc = DocumentHelper.parseText(this.schema);
			Element root = doc.getRootElement();
			int count = this.getRowCount();
			int colCount = this.getColumnCount();
			String colName;
			Element data;
			Object value;
			for (int i = 0; i < count; i++) {
				data = root.addElement("Row");
				if (this.rows.get(i).IsDeleted()) {
					data.addAttribute("Deleted", "true");
				}
				for (int j = 0; j < colCount; j++) {
					colName = this.meta.get(j).getName();
					Element col = data.addElement(colName);
					value = this.rows.get(i).get(j);
					if (null != value)
						col.setText(value.toString());
				}
			}

			String encoding = "UTF-8";
			StringWriter writer = new StringWriter();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			XMLWriter xmlwriter = new XMLWriter(writer, format);
			xmlwriter.write(doc);
			xmlwriter.flush();
			result = writer.toString();
		}
		return result;
	}
}