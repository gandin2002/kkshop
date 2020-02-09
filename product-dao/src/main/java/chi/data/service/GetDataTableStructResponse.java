/**
 * GetDataTableStructResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetDataTableStructResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6603406641652586624L;
	private java.lang.String[] getDataTableStructResult;

    public GetDataTableStructResponse() {
    }

    public GetDataTableStructResponse(
           java.lang.String[] getDataTableStructResult) {
           this.getDataTableStructResult = getDataTableStructResult;
    }


    /**
     * Gets the getDataTableStructResult value for this GetDataTableStructResponse.
     * 
     * @return getDataTableStructResult
     */
    public java.lang.String[] getGetDataTableStructResult() {
        return getDataTableStructResult;
    }


    /**
     * Sets the getDataTableStructResult value for this GetDataTableStructResponse.
     * 
     * @param getDataTableStructResult
     */
    public void setGetDataTableStructResult(java.lang.String[] getDataTableStructResult) {
        this.getDataTableStructResult = getDataTableStructResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDataTableStructResponse)) return false;
        GetDataTableStructResponse other = (GetDataTableStructResponse) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getDataTableStructResult==null && other.getGetDataTableStructResult()==null) || 
             (this.getDataTableStructResult!=null &&
              java.util.Arrays.equals(this.getDataTableStructResult, other.getGetDataTableStructResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGetDataTableStructResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetDataTableStructResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetDataTableStructResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDataTableStructResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetDataTableStructResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getDataTableStructResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetDataTableStructResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
