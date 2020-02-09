/**
 * ExecuteProc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class ExecuteProc  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8735201909765890076L;

	private java.lang.String groupId;

    private java.lang.String language;

    private java.lang.String userId;

    private byte[] password;

    private java.lang.String progId;

    private java.lang.String methodName;

    private java.lang.Object[] wParams;

    private boolean ucoInvoke;

    private int logMode;

    public ExecuteProc() {
    }

    public ExecuteProc(
           java.lang.String groupId,
           java.lang.String language,
           java.lang.String userId,
           byte[] password,
           java.lang.String progId,
           java.lang.String methodName,
           java.lang.Object[] wParams,
           boolean ucoInvoke,
           int logMode) {
           this.groupId = groupId;
           this.language = language;
           this.userId = userId;
           this.password = password;
           this.progId = progId;
           this.methodName = methodName;
           this.wParams = wParams;
           this.ucoInvoke = ucoInvoke;
           this.logMode = logMode;
    }


    /**
     * Gets the groupId value for this ExecuteProc.
     * 
     * @return groupId
     */
    public java.lang.String getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this ExecuteProc.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the language value for this ExecuteProc.
     * 
     * @return language
     */
    public java.lang.String getLanguage() {
        return language;
    }


    /**
     * Sets the language value for this ExecuteProc.
     * 
     * @param language
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }


    /**
     * Gets the userId value for this ExecuteProc.
     * 
     * @return userId
     */
    public java.lang.String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this ExecuteProc.
     * 
     * @param userId
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }


    /**
     * Gets the password value for this ExecuteProc.
     * 
     * @return password
     */
    public byte[] getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ExecuteProc.
     * 
     * @param password
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }


    /**
     * Gets the progId value for this ExecuteProc.
     * 
     * @return progId
     */
    public java.lang.String getProgId() {
        return progId;
    }


    /**
     * Sets the progId value for this ExecuteProc.
     * 
     * @param progId
     */
    public void setProgId(java.lang.String progId) {
        this.progId = progId;
    }


    /**
     * Gets the methodName value for this ExecuteProc.
     * 
     * @return methodName
     */
    public java.lang.String getMethodName() {
        return methodName;
    }


    /**
     * Sets the methodName value for this ExecuteProc.
     * 
     * @param methodName
     */
    public void setMethodName(java.lang.String methodName) {
        this.methodName = methodName;
    }


    /**
     * Gets the wParams value for this ExecuteProc.
     * 
     * @return wParams
     */
    public java.lang.Object[] getWParams() {
        return wParams;
    }


    /**
     * Sets the wParams value for this ExecuteProc.
     * 
     * @param wParams
     */
    public void setWParams(java.lang.Object[] wParams) {
        this.wParams = wParams;
    }


    /**
     * Gets the ucoInvoke value for this ExecuteProc.
     * 
     * @return ucoInvoke
     */
    public boolean isUcoInvoke() {
        return ucoInvoke;
    }


    /**
     * Sets the ucoInvoke value for this ExecuteProc.
     * 
     * @param ucoInvoke
     */
    public void setUcoInvoke(boolean ucoInvoke) {
        this.ucoInvoke = ucoInvoke;
    }


    /**
     * Gets the logMode value for this ExecuteProc.
     * 
     * @return logMode
     */
    public int getLogMode() {
        return logMode;
    }


    /**
     * Sets the logMode value for this ExecuteProc.
     * 
     * @param logMode
     */
    public void setLogMode(int logMode) {
        this.logMode = logMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExecuteProc)) return false;
        ExecuteProc other = (ExecuteProc) obj;
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
            ((this.methodName==null && other.getMethodName()==null) || 
             (this.methodName!=null &&
              this.methodName.equals(other.getMethodName()))) &&
            ((this.wParams==null && other.getWParams()==null) || 
             (this.wParams!=null &&
              java.util.Arrays.equals(this.wParams, other.getWParams()))) &&
            this.ucoInvoke == other.isUcoInvoke() &&
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
        if (getMethodName() != null) {
            _hashCode += getMethodName().hashCode();
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
        _hashCode += (isUcoInvoke() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getLogMode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecuteProc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", ">ExecuteProc"));
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
        elemField.setFieldName("methodName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "methodName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "wParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "anyType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ucoInvoke");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ucoInvoke"));
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
