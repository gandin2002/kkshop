/**
 * CAPInteropServiceExLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public class CAPInteropServiceExLocator extends org.apache.axis.client.Service implements CAPInteropServiceEx {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2654203353501341958L;

	public CAPInteropServiceExLocator() {
    }


    public CAPInteropServiceExLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CAPInteropServiceExLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CAPInteropServiceExSoap
    private java.lang.String CAPInteropServiceExSoap_address = "http://localhost:8080/WebService/CAPInteropServiceEx.asmx";

    public java.lang.String getCAPInteropServiceExSoapAddress() {
        return CAPInteropServiceExSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CAPInteropServiceExSoapWSDDServiceName = "CAPInteropServiceExSoap";

    public java.lang.String getCAPInteropServiceExSoapWSDDServiceName() {
        return CAPInteropServiceExSoapWSDDServiceName;
    }

    public void setCAPInteropServiceExSoapWSDDServiceName(java.lang.String name) {
        CAPInteropServiceExSoapWSDDServiceName = name;
    }

    public CAPInteropServiceExSoap getCAPInteropServiceExSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CAPInteropServiceExSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCAPInteropServiceExSoap(endpoint);
    }

    public CAPInteropServiceExSoap getCAPInteropServiceExSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CAPInteropServiceExSoapStub _stub = new CAPInteropServiceExSoapStub(portAddress, this);
            _stub.setPortName(getCAPInteropServiceExSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCAPInteropServiceExSoapEndpointAddress(java.lang.String address) {
        CAPInteropServiceExSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CAPInteropServiceExSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                CAPInteropServiceExSoapStub _stub = new CAPInteropServiceExSoapStub(new java.net.URL(CAPInteropServiceExSoap_address), this);
                _stub.setPortName(getCAPInteropServiceExSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CAPInteropServiceExSoap".equals(inputPortName)) {
            return getCAPInteropServiceExSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "CAPInteropServiceEx");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "CAPInteropServiceExSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CAPInteropServiceExSoap".equals(portName)) {
            setCAPInteropServiceExSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
