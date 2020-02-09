/**
 * GetFlowPendingCount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetFlowPendingCount  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5106822050896206069L;

	private java.lang.String groupId;

    private java.lang.String userId;

    private byte[] password;

    private java.lang.String[] wParams;

    private int logMode;

    public GetFlowPendingCount() {
    }

    public GetFlowPendingCount(
           java.lang.String groupId,
           java.lang.String userId,
           byte[] password,
           java.lang.String[] wParams,
           int logMode) {
           this.groupId = groupId;
           this.userId = userId;
           this.password = password;
           this.wParams = wParams;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this GetFlowPendingCount.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GetFlowPendingCount.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the userId value for this GetFlowPendingCount.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this GetFlowPendingCount.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this GetFlowPendingCount.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this GetFlowPendingCount.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the wParams value for this GetFlowPendingCount.
     * 
     * @return wParams
     */
    public java.lang.String[] getWParams() {
        return wParams;
    }


    /**
     * Sets the wParams value for this GetFlowPendingCount.
     * 
     * @param wParams
     */
    public void setWParams(java.lang.String[] wParams) {
        this.wParams = wParams;
    }


    /**
     * Gets the logMode value for this GetFlowPendingCount.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this GetFlowPendingCount.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetFlowPendingCount)) return false;
        GetFlowPendingCount other = (GetFlowPendingCount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.groupId==null && other.getGroupId()==null) || 
             (this.groupId!=null &&
              this.groupId.equals(other.getGroupId()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              java.util.Arrays.equals(this.password, other.getPassword()))) &&
            ((this.wParams==null && other.getWParams()==null) || 
             (this.wParams!=null &&
              java.util.Arrays.equals(this.wParams, other.getWParams()))) &&
            this.logMode == other.getLogMode();
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
        if (getGroupId() != null) {
            _hashCode += getGroupId().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getPassword() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPassword());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPassword(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWParams() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWParams());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWParams(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getLogMode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetFlowPendingCount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetFlowPendingCount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "groupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "wParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "logMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
