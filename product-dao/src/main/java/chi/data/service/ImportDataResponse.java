/**
 * ImportDataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class ImportDataResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7482148968198348047L;
	
	
	private java.lang.String importDataResult;

    public ImportDataResponse() {
    }

    public ImportDataResponse(
           java.lang.String importDataResult) {
           this.importDataResult = importDataResult;
    }


    /**
     * Gets the importDataResult value for this ImportDataResponse.
     * 
     * @return importDataResult
     */
    public java.lang.String getImportDataResult() {
        return importDataResult;
    }


    /**
     * Sets the importDataResult value for this ImportDataResponse.
     * 
     * @param importDataResult
     */
    public void setImportDataResult(java.lang.String importDataResult) {
        this.importDataResult = importDataResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImportDataResponse)) return false;
        ImportDataResponse other = (ImportDataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.importDataResult==null && other.getImportDataResult()==null) || 
             (this.importDataResult!=null &&
              this.importDataResult.equals(other.getImportDataResult())));
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
        if (getImportDataResult() != null) {
            _hashCode += getImportDataResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImportDataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ImportDataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importDataResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ImportDataResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
