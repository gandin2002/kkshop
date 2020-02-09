/**
 * ExecuteProcResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class ExecuteProcResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5350172154989095751L;
	
	private java.lang.Object executeProcResult;

    public ExecuteProcResponse() {
    }

    public ExecuteProcResponse(
           java.lang.Object executeProcResult) {
           this.executeProcResult = executeProcResult;
    }


    /**
     * Gets the executeProcResult value for this ExecuteProcResponse.
     * 
     * @return executeProcResult
     */
    public java.lang.Object getExecuteProcResult() {
        return executeProcResult;
    }


    /**
     * Sets the executeProcResult value for this ExecuteProcResponse.
     * 
     * @param executeProcResult
     */
    public void setExecuteProcResult(java.lang.Object executeProcResult) {
        this.executeProcResult = executeProcResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExecuteProcResponse)) return false;
        ExecuteProcResponse other = (ExecuteProcResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.executeProcResult==null && other.getExecuteProcResult()==null) || 
             (this.executeProcResult!=null &&
              this.executeProcResult.equals(other.getExecuteProcResult())));
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
        if (getExecuteProcResult() != null) {
            _hashCode += getExecuteProcResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteProcResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ExecuteProcResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executeProcResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ExecuteProcResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
