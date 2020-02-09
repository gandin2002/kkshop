package chi.data.service;

public class CAPInteropServiceExSoapProxy implements CAPInteropServiceExSoap {
  private String _endpoint = null;
  private CAPInteropServiceExSoap cAPInteropServiceExSoap = null;
  
  public CAPInteropServiceExSoapProxy() {
    _initCAPInteropServiceExSoapProxy();
  }
  
  public CAPInteropServiceExSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCAPInteropServiceExSoapProxy();
  }
  
  private void _initCAPInteropServiceExSoapProxy() {
    try {
      cAPInteropServiceExSoap = (new CAPInteropServiceExLocator()).getCAPInteropServiceExSoap();
      if (cAPInteropServiceExSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cAPInteropServiceExSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cAPInteropServiceExSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cAPInteropServiceExSoap != null)
      ((javax.xml.rpc.Stub)cAPInteropServiceExSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public CAPInteropServiceExSoap getCAPInteropServiceExSoap() {
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap;
  }
  
  public java.lang.String importData(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String[] tables, java.lang.String entryParams, java.lang.String linkageFields, java.lang.String checkFields, java.lang.String unReturnFields, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.importData(groupId, language, userId, password, progId, tables, entryParams, linkageFields, checkFields, unReturnFields, logMode);
  }
  
  public java.lang.String importDataEx(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String[] tables, java.lang.String entryParams, java.lang.String linkageFields, java.lang.String checkFields, java.lang.String unReturnFields, int importMode, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.importDataEx(groupId, language, userId, password, progId, tables, entryParams, linkageFields, checkFields, unReturnFields, importMode, logMode);
  }
  
  public java.lang.String[] getDataTableStruct(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String fields, boolean containsNotAllowEmpty, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.getDataTableStruct(groupId, language, userId, password, progId, fields, containsNotAllowEmpty, logMode);
  }
  
  public java.lang.Object executeProc(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String methodName, java.lang.Object[] wParams, boolean ucoInvoke, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.executeProc(groupId, language, userId, password, progId, methodName, wParams, ucoInvoke, logMode);
  }
  
  public java.lang.String getQueryData(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String currentCulture, java.lang.String progId, int tableIndex, java.lang.String selectedFields, java.lang.String whereClause, java.lang.String sortFields, int fetchCount, boolean isDistinct, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.getQueryData(groupId, language, userId, password, currentCulture, progId, tableIndex, selectedFields, whereClause, sortFields, fetchCount, isDistinct, logMode);
  }
  
  public java.lang.String getBillWorkflowInfo(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, int sourceTag, java.lang.String[] wParams, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.getBillWorkflowInfo(groupId, language, userId, password, sourceTag, wParams, logMode);
  }
  
  public java.lang.String getFlowPendingCount(java.lang.String groupId, java.lang.String userId, byte[] password, java.lang.String[] wParams, int logMode) throws java.rmi.RemoteException{
    if (cAPInteropServiceExSoap == null)
      _initCAPInteropServiceExSoapProxy();
    return cAPInteropServiceExSoap.getFlowPendingCount(groupId, userId, password, wParams, logMode);
  }
  
  
}