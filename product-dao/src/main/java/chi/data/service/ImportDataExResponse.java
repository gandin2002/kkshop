/**
 * ImportDataExResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class ImportDataExResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3570024647942235093L;
	
	private java.lang.String importDataExResult;

    public ImportDataExResponse() {
    }

    public ImportDataExResponse(
           java.lang.String importDataExResult) {
           this.importDataExResult = importDataExResult;
    }


    /**
     * Gets the importDataExResult value for this ImportDataExResponse.
     * 
     * @return importDataExResult
     */
    public java.lang.String getImportDataExResult() {
        return importDataExResult;
    }


    /**
     * Sets the importDataExResult value for this ImportDataExResponse.
     * 
     * @param importDataExResult
     */
    public void setImportDataExResult(java.lang.String importDataExResult) {
        this.importDataExResult = importDataExResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImportDataExResponse)) return false;
        ImportDataExResponse other = (ImportDataExResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.importDataExResult==null && other.getImportDataExResult()==null) || 
             (this.importDataExResult!=null &&
              this.importDataExResult.equals(other.getImportDataExResult())));
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
        if (getImportDataExResult() != null) {
            _hashCode += getImportDataExResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImportDataExResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ImportDataExResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importDataExResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ImportDataExResult"));
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
