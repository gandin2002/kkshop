/**
 * ImportData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class ImportData  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4614580075702866050L;

	private java.lang.String groupId;

    private java.lang.String language;

    private java.lang.String userId;

    private byte[] password;

    private java.lang.String progId;

    private java.lang.String[] tables;

    private java.lang.String entryParams;

    private java.lang.String linkageFields;

    private java.lang.String checkFields;

    private java.lang.String unReturnFields;

    private int logMode;

    public ImportData() {
    }

    public ImportData(
           java.lang.String groupId,
           java.lang.String language,
           java.lang.String userId,
           byte[] password,
           java.lang.String progId,
           java.lang.String[] tables,
           java.lang.String entryParams,
           java.lang.String linkageFields,
           java.lang.String checkFields,
           java.lang.String unReturnFields,
           int logMode) {
           this.groupId = groupId;
           this.language = language;
           this.userId = userId;
           this.password = password;
           this.progId = progId;
           this.tables = tables;
           this.entryParams = entryParams;
           this.linkageFields = linkageFields;
           this.checkFields = checkFields;
           this.unReturnFields = unReturnFields;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this ImportData.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this ImportData.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the language value for this ImportData.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this ImportData.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the userId value for this ImportData.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this ImportData.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this ImportData.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ImportData.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the progId value for this ImportData.
     * 
     * @return progId
     */
    public java.lang.String getProgId() {
        return progId;
    }


    /**
     * Sets the progId value for this ImportData.
     * 
     * @param progId
     */
    public void setProgId(java.lang.String progId) {
        this.progId = progId;
    }


    /**
     * Gets the tables value for this ImportData.
     * 
     * @return tables
     */
    public java.lang.String[] getTables() {
        return tables;
    }


    /**
     * Sets the tables value for this ImportData.
     * 
     * @param tables
     */
    public void setTables(java.lang.String[] tables) {
        this.tables = tables;
    }


    /**
     * Gets the entryParams value for this ImportData.
     * 
     * @return entryParams
     */
    public java.lang.String getEntryParams() {
        return entryParams;
    }


    /**
     * Sets the entryParams value for this ImportData.
     * 
     * @param entryParams
     */
    public void setEntryParams(java.lang.String entryParams) {
        this.entryParams = entryParams;
    }


    /**
     * Gets the linkageFields value for this ImportData.
     * 
     * @return linkageFields
     */
    public java.lang.String getLinkageFields() {
        return linkageFields;
    }


    /**
     * Sets the linkageFields value for this ImportData.
     * 
     * @param linkageFields
     */
    public void setLinkageFields(java.lang.String linkageFields) {
        this.linkageFields = linkageFields;
    }


    /**
     * Gets the checkFields value for this ImportData.
     * 
     * @return checkFields
     */
    public java.lang.String getCheckFields() {
        return checkFields;
    }


    /**
     * Sets the checkFields value for this ImportData.
     * 
     * @param checkFields
     */
    public void setCheckFields(java.lang.String checkFields) {
        this.checkFields = checkFields;
    }


    /**
     * Gets the unReturnFields value for this ImportData.
     * 
     * @return unReturnFields
     */
    public java.lang.String getUnReturnFields() {
        return unReturnFields;
    }


    /**
     * Sets the unReturnFields value for this ImportData.
     * 
     * @param unReturnFields
     */
    public void setUnReturnFields(java.lang.String unReturnFields) {
        this.unReturnFields = unReturnFields;
    }


    /**
     * Gets the logMode value for this ImportData.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this ImportData.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ImportData)) return false;
        ImportData other = (ImportData) obj;
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
            ((this.tables==null && other.getTables()==null) || 
             (this.tables!=null &&
              java.util.Arrays.equals(this.tables, other.getTables()))) &&
            ((this.entryParams==null && other.getEntryParams()==null) || 
             (this.entryParams!=null &&
              this.entryParams.equals(other.getEntryParams()))) &&
            ((this.linkageFields==null && other.getLinkageFields()==null) || 
             (this.linkageFields!=null &&
              this.linkageFields.equals(other.getLinkageFields()))) &&
            ((this.checkFields==null && other.getCheckFields()==null) || 
             (this.checkFields!=null &&
              this.checkFields.equals(other.getCheckFields()))) &&
            ((this.unReturnFields==null && other.getUnReturnFields()==null) || 
             (this.unReturnFields!=null &&
              this.unReturnFields.equals(other.getUnReturnFields()))) &&
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
        if (getTables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEntryParams() != null) {
            _hashCode += getEntryParams().hashCode();
        }
        if (getLinkageFields() != null) {
            _hashCode += getLinkageFields().hashCode();
        }
        if (getCheckFields() != null) {
            _hashCode += getCheckFields().hashCode();
        }
        if (getUnReturnFields() != null) {
            _hashCode += getUnReturnFields().hashCode();
        }
        _hashCode += getLogMode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ImportData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ImportData"));
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
        elemField.setFieldName("tables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "tables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "entryParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkageFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "linkageFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "checkFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unReturnFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "unReturnFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
