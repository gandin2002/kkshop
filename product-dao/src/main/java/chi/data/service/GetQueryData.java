/**
 * GetQueryData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class GetQueryData  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5740346497196944630L;

	private java.lang.String groupId;

    private java.lang.String language;

    private java.lang.String userId;

    private byte[] password;

    private java.lang.String currentCulture;

    private java.lang.String progId;

    private int tableIndex;

    private java.lang.String selectedFields;

    private java.lang.String whereClause;

    private java.lang.String sortFields;

    private int fetchCount;

    private boolean isDistinct;

    private int logMode;

    public GetQueryData() {
    }

    public GetQueryData(
           java.lang.String groupId,
           java.lang.String language,
           java.lang.String userId,
           byte[] password,
           java.lang.String currentCulture,
           java.lang.String progId,
           int tableIndex,
           java.lang.String selectedFields,
           java.lang.String whereClause,
           java.lang.String sortFields,
           int fetchCount,
           boolean isDistinct,
           int logMode) {
           this.groupId = groupId;
           this.language = language;
           this.userId = userId;
           this.password = password;
           this.currentCulture = currentCulture;
           this.progId = progId;
           this.tableIndex = tableIndex;
           this.selectedFields = selectedFields;
           this.whereClause = whereClause;
           this.sortFields = sortFields;
           this.fetchCount = fetchCount;
           this.isDistinct = isDistinct;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this GetQueryData.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GetQueryData.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the language value for this GetQueryData.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this GetQueryData.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the userId value for this GetQueryData.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this GetQueryData.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this GetQueryData.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this GetQueryData.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the currentCulture value for this GetQueryData.
     * 
     * @return currentCulture
     */
    public java.lang.String getCurrentCulture() {
        return currentCulture;
    }


    /**
     * Sets the currentCulture value for this GetQueryData.
     * 
     * @param currentCulture
     */
    public void setCurrentCulture(java.lang.String currentCulture) {
        this.currentCulture = currentCulture;
    }


    /**
     * Gets the progId value for this GetQueryData.
     * 
     * @return progId
     */
    public java.lang.String getProgId() {
        return progId;
    }


    /**
     * Sets the progId value for this GetQueryData.
     * 
     * @param progId
     */
    public void setProgId(java.lang.String progId) {
        this.progId = progId;
    }


    /**
     * Gets the tableIndex value for this GetQueryData.
     * 
     * @return tableIndex
     */
    public int getTableIndex() {
        return tableIndex;
    }


    /**
     * Sets the tableIndex value for this GetQueryData.
     * 
     * @param tableIndex
     */
    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }


    /**
     * Gets the selectedFields value for this GetQueryData.
     * 
     * @return selectedFields
     */
    public java.lang.String getSelectedFields() {
        return selectedFields;
    }


    /**
     * Sets the selectedFields value for this GetQueryData.
     * 
     * @param selectedFields
     */
    public void setSelectedFields(java.lang.String selectedFields) {
        this.selectedFields = selectedFields;
    }


    /**
     * Gets the whereClause value for this GetQueryData.
     * 
     * @return whereClause
     */
    public java.lang.String getWhereClause() {
        return whereClause;
    }


    /**
     * Sets the whereClause value for this GetQueryData.
     * 
     * @param whereClause
     */
    public void setWhereClause(java.lang.String whereClause) {
        this.whereClause = whereClause;
    }


    /**
     * Gets the sortFields value for this GetQueryData.
     * 
     * @return sortFields
     */
    public java.lang.String getSortFields() {
        return sortFields;
    }


    /**
     * Sets the sortFields value for this GetQueryData.
     * 
     * @param sortFields
     */
    public void setSortFields(java.lang.String sortFields) {
        this.sortFields = sortFields;
    }


    /**
     * Gets the fetchCount value for this GetQueryData.
     * 
     * @return fetchCount
     */
    public int getFetchCount() {
        return fetchCount;
    }


    /**
     * Sets the fetchCount value for this GetQueryData.
     * 
     * @param fetchCount
     */
    public void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }


    /**
     * Gets the isDistinct value for this GetQueryData.
     * 
     * @return isDistinct
     */
    public boolean isIsDistinct() {
        return isDistinct;
    }


    /**
     * Sets the isDistinct value for this GetQueryData.
     * 
     * @param isDistinct
     */
    public void setIsDistinct(boolean isDistinct) {
        this.isDistinct = isDistinct;
    }


    /**
     * Gets the logMode value for this GetQueryData.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this GetQueryData.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetQueryData)) return false;
        GetQueryData other = (GetQueryData) obj;
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
            ((this.currentCulture==null && other.getCurrentCulture()==null) || 
             (this.currentCulture!=null &&
              this.currentCulture.equals(other.getCurrentCulture()))) &&
            ((this.progId==null && other.getProgId()==null) || 
             (this.progId!=null &&
              this.progId.equals(other.getProgId()))) &&
            this.tableIndex == other.getTableIndex() &&
            ((this.selectedFields==null && other.getSelectedFields()==null) || 
             (this.selectedFields!=null &&
              this.selectedFields.equals(other.getSelectedFields()))) &&
            ((this.whereClause==null && other.getWhereClause()==null) || 
             (this.whereClause!=null &&
              this.whereClause.equals(other.getWhereClause()))) &&
            ((this.sortFields==null && other.getSortFields()==null) || 
             (this.sortFields!=null &&
              this.sortFields.equals(other.getSortFields()))) &&
            this.fetchCount == other.getFetchCount() &&
            this.isDistinct == other.isIsDistinct() &&
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
        if (getCurrentCulture() != null) {
            _hashCode += getCurrentCulture().hashCode();
        }
        if (getProgId() != null) {
            _hashCode += getProgId().hashCode();
        }
        _hashCode += getTableIndex();
        if (getSelectedFields() != null) {
            _hashCode += getSelectedFields().hashCode();
        }
        if (getWhereClause() != null) {
            _hashCode += getWhereClause().hashCode();
        }
        if (getSortFields() != null) {
            _hashCode += getSortFields().hashCode();
        }
        _hashCode += getFetchCount();
        _hashCode += (isIsDistinct() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getLogMode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetQueryData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">GetQueryData"));
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
        elemField.setFieldName("currentCulture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "currentCulture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("tableIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "tableIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "selectedFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("whereClause");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "whereClause"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sortFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "sortFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fetchCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "fetchCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDistinct");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "isDistinct"));
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
