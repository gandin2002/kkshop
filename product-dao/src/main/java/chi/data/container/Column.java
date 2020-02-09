package chi.data.container;
import java.io.*;

//表头的每一个字段
public class Column implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int index;
	private String type="Object";
	private boolean NULL=false;
	private boolean isPrimaryKey=false;
	private int LENGHT=0;

	public Column(){

	}
	public Column(String name){
		this.name=name;
	}
	public Column(String name,int index){
		this.name=name;
		this.index=index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isNULL() {
		return NULL;
	}
	public void setNULL(boolean null1) {
		NULL = null1;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean single) {
		isPrimaryKey = single;
	}
	public int getLENGHT() {
		return LENGHT;
	}
	public void setLENGHT(int lenght) {
		LENGHT = lenght;
	}



}
