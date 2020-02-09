/**
 * CAPInteropServiceExSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

public interface CAPInteropServiceExSoap extends java.rmi.Remote {
    public java.lang.String importData(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String[] tables, java.lang.String entryParams, java.lang.String linkageFields, java.lang.String checkFields, java.lang.String unReturnFields, int logMode) throws java.rmi.RemoteException;
    public java.lang.String importDataEx(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String[] tables, java.lang.String entryParams, java.lang.String linkageFields, java.lang.String checkFields, java.lang.String unReturnFields, int importMode, int logMode) throws java.rmi.RemoteException;
    public java.lang.String[] getDataTableStruct(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String fields, boolean containsNotAllowEmpty, int logMode) throws java.rmi.RemoteException;
    public java.lang.Object executeProc(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String progId, java.lang.String methodName, java.lang.Object[] wParams, boolean ucoInvoke, int logMode) throws java.rmi.RemoteException;
    public java.lang.String getQueryData(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, java.lang.String currentCulture, java.lang.String progId, int tableIndex, java.lang.String selectedFields, java.lang.String whereClause, java.lang.String sortFields, int fetchCount, boolean isDistinct, int logMode) throws java.rmi.RemoteException;
    public java.lang.String getBillWorkflowInfo(java.lang.String groupId, java.lang.String language, java.lang.String userId, byte[] password, int sourceTag, java.lang.String[] wParams, int logMode) throws java.rmi.RemoteException;
    public java.lang.String getFlowPendingCount(java.lang.String groupId, java.lang.String userId, byte[] password, java.lang.String[] wParams, int logMode) throws java.rmi.RemoteException;
}
