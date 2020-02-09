/**
 * GetBillWorkflowInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetBillWorkflowInfo  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2113150308228535624L;

	private java.lang.String groupId;

    private java.lang.String language;

    private java.lang.String userId;

    private byte[] password;

    private int sourceTag;

    private java.lang.String[] wParams;

    private int logMode;

    public GetBillWorkflowInfo() {
    }

    public GetBillWorkflowInfo(
           java.lang.String groupId,
           java.lang.String language,
           java.lang.String userId,
           byte[] password,
           int sourceTag,
           java.lang.String[] wParams,
           int logMode) {
           this.groupId = groupId;
           this.language = language;
           this.userId = userId;
           this.password = password;
           this.sourceTag = sourceTag;
           this.wParams = wParams;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this GetBillWorkflowInfo.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GetBillWorkflowInfo.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the language value for this GetBillWorkflowInfo.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this GetBillWorkflowInfo.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the userId value for this GetBillWorkflowInfo.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this GetBillWorkflowInfo.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this GetBillWorkflowInfo.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this GetBillWorkflowInfo.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the sourceTag value for this GetBillWorkflowInfo.
     * 
     * @return sourceTag
     */
    public int getSourceTag() {
        return sourceTag;
    }


    /**
     * Sets the sourceTag value for this GetBillWorkflowInfo.
     * 
     * @param sourceTag
     */
    public void setSourceTag(int sourceTag) {
        this.sourceTag = sourceTag;
    }


    /**
     * Gets the wParams value for this GetBillWorkflowInfo.
     * 
     * @return wParams
     */
    public java.lang.String[] getWParams() {
        return wParams;
    }


    /**
     * Sets the wParams value for this GetBillWorkflowInfo.
     * 
     * @param wParams
     */
    public void setWParams(java.lang.String[] wParams) {
        this.wParams = wParams;
    }


    /**
     * Gets the logMode value for this GetBillWorkflowInfo.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this GetBillWorkflowInfo.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetBillWorkflowInfo)) return false;
        GetBillWorkflowInfo other = (GetBillWorkflowInfo) obj;
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
            ((this.language==null && other.getLanguage()==null) || 
             (this.language!=null &&
              this.language.equals(other.getLanguage()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              java.util.Arrays.equals(this.password, other.getPassword()))) &&
            this.sourceTag == other.getSourceTag() &&
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
        if (getLanguage() != null) {
            _hashCode += getLanguage().hashCode();
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
        _hashCode += getSourceTag();
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
        new org.apache.axis.description.TypeDesc(GetBillWorkflowInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetBillWorkflowInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "groupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("language");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "language"));
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
        elemField.setFieldName("sourceTag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sourceTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
