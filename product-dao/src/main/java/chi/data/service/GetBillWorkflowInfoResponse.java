/**
 * GetBillWorkflowInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetBillWorkflowInfoResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4398262787651147040L;
	private java.lang.String getBillWorkflowInfoResult;

    public GetBillWorkflowInfoResponse() {
    }

    public GetBillWorkflowInfoResponse(
           java.lang.String getBillWorkflowInfoResult) {
           this.getBillWorkflowInfoResult = getBillWorkflowInfoResult;
    }


    /**
     * Gets the getBillWorkflowInfoResult value for this GetBillWorkflowInfoResponse.
     * 
     * @return getBillWorkflowInfoResult
     */
    public java.lang.String getGetBillWorkflowInfoResult() {
        return getBillWorkflowInfoResult;
    }


    /**
     * Sets the getBillWorkflowInfoResult value for this GetBillWorkflowInfoResponse.
     * 
     * @param getBillWorkflowInfoResult
     */
    public void setGetBillWorkflowInfoResult(java.lang.String getBillWorkflowInfoResult) {
        this.getBillWorkflowInfoResult = getBillWorkflowInfoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetBillWorkflowInfoResponse)) return false;
        GetBillWorkflowInfoResponse other = (GetBillWorkflowInfoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getBillWorkflowInfoResult==null && other.getGetBillWorkflowInfoResult()==null) || 
             (this.getBillWorkflowInfoResult!=null &&
              this.getBillWorkflowInfoResult.equals(other.getGetBillWorkflowInfoResult())));
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
        if (getGetBillWorkflowInfoResult() != null) {
            _hashCode += getGetBillWorkflowInfoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetBillWorkflowInfoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetBillWorkflowInfoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getBillWorkflowInfoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GetBillWorkflowInfoResult"));
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
