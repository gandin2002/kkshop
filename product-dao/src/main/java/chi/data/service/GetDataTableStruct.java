/**
 * GetDataTableStruct.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetDataTableStruct  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1814091483250737969L;

	private java.lang.String groupId;

    private java.lang.String language;

    private java.lang.String userId;

    private byte[] password;

    private java.lang.String progId;

    private java.lang.String fields;

    private boolean containsNotAllowEmpty;

    private int logMode;

    public GetDataTableStruct() {
    }

    public GetDataTableStruct(
           java.lang.String groupId,
           java.lang.String language,
           java.lang.String userId,
           byte[] password,
           java.lang.String progId,
           java.lang.String fields,
           boolean containsNotAllowEmpty,
           int logMode) {
           this.groupId = groupId;
           this.language = language;
           this.userId = userId;
           this.password = password;
           this.progId = progId;
           this.fields = fields;
           this.containsNotAllowEmpty = containsNotAllowEmpty;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this GetDataTableStruct.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GetDataTableStruct.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the language value for this GetDataTableStruct.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this GetDataTableStruct.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the userId value for this GetDataTableStruct.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this GetDataTableStruct.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this GetDataTableStruct.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this GetDataTableStruct.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the progId value for this GetDataTableStruct.
     * 
     * @return progId
     */
    public java.lang.String getProgId() {
        return progId;
    }


    /**
     * Sets the progId value for this GetDataTableStruct.
     * 
     * @param progId
     */
    public void setProgId(java.lang.String progId) {
        this.progId = progId;
    }


    /**
     * Gets the fields value for this GetDataTableStruct.
     * 
     * @return fields
     */
    public java.lang.String getFields() {
        return fields;
    }


    /**
     * Sets the fields value for this GetDataTableStruct.
     * 
     * @param fields
     */
    public void setFields(java.lang.String fields) {
        this.fields = fields;
    }


    /**
     * Gets the containsNotAllowEmpty value for this GetDataTableStruct.
     * 
     * @return containsNotAllowEmpty
     */
    public boolean isContainsNotAllowEmpty() {
        return containsNotAllowEmpty;
    }


    /**
     * Sets the containsNotAllowEmpty value for this GetDataTableStruct.
     * 
     * @param containsNotAllowEmpty
     */
    public void setContainsNotAllowEmpty(boolean containsNotAllowEmpty) {
        this.containsNotAllowEmpty = containsNotAllowEmpty;
    }


    /**
     * Gets the logMode value for this GetDataTableStruct.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this GetDataTableStruct.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDataTableStruct)) return false;
        GetDataTableStruct other = (GetDataTableStruct) obj;
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
            ((this.progId==null && other.getProgId()==null) || 
             (this.progId!=null &&
              this.progId.equals(other.getProgId()))) &&
            ((this.fields==null && other.getFields()==null) || 
             (this.fields!=null &&
              this.fields.equals(other.getFields()))) &&
            this.containsNotAllowEmpty == other.isContainsNotAllowEmpty() &&
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
        if (getProgId() != null) {
            _hashCode += getProgId().hashCode();
        }
        if (getFields() != null) {
            _hashCode += getFields().hashCode();
        }
        _hashCode += (isContainsNotAllowEmpty() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getLogMode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDataTableStruct.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetDataTableStruct"));
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
        elemField.setFieldName("progId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "progId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containsNotAllowEmpty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "containsNotAllowEmpty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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
