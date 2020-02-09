/**
 * GetFlowPendingCountResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetFlowPendingCountResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4400841243612984949L;
	private java.lang.String getFlowPendingCountResult;

    public GetFlowPendingCountResponse() {
    }

    public GetFlowPendingCountResponse(
           java.lang.String getFlowPendingCountResult) {
           this.getFlowPendingCountResult = getFlowPendingCountResult;
    }


    /**
     * Gets the getFlowPendingCountResult value for this GetFlowPendingCountResponse.
     * 
     * @return getFlowPendingCountResult
     */
    public java.lang.String getGetFlowPendingCountResult() {
        return getFlowPendingCountResult;
    }


    /**
     * Sets the getFlowPendingCountResult value for this GetFlowPendingCountResponse.
     * 
     * @param getFlowPendingCountResult
     */
    public void setGetFlowPendingCountResult(java.lang.String getFlowPendingCountResult) {
        this.getFlowPendingCountResult = getFlowPendingCountResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFlowPendingCountResponse)) return false;
        GetFlowPendingCountResponse other = (GetFlowPendingCountResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getFlowPendingCountResult==null && other.getGetFlowPendingCountResult()==null) || 
             (this.getFlowPendingCountResult!=null &&
              this.getFlowPendingCountResult.equals(other.getGetFlowPendingCountResult())));
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
        if (getGetFlowPendingCountResult() != null) {
            _hashCode += getGetFlowPendingCountResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFlowPendingCountResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFlowPendingCountResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getFlowPendingCountResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetFlowPendingCountResult"));
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
