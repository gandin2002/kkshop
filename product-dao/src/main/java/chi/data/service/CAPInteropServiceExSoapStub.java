/**
 * CAPInteropServiceExSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package chi.data.service;

import java.util.Enumeration;

import javax.xml.namespace.QName;

import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.utils.JavaUtils;

public class CAPInteropServiceExSoapStub extends org.apache.axis.client.Stub implements CAPInteropServiceExSoap {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector<QName> cachedSerQNames = new java.util.Vector<QName>();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static OperationDesc[] _operations;

	static {
		_operations = new OperationDesc[7];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("ImportData");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "progId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "tables"), ParameterDesc.IN, new QName("http://tempuri.org/", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "entryParams"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "linkageFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "checkFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "unReturnFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "ImportDataResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new OperationDesc();
		oper.setName("ImportDataEx");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "progId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "tables"), ParameterDesc.IN, new QName("http://tempuri.org/", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "entryParams"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "linkageFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "checkFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "unReturnFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "importMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "ImportDataExResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new OperationDesc();
		oper.setName("GetDataTableStruct");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "progId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "fields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "containsNotAllowEmpty"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://tempuri.org/", "ArrayOfString"));
		oper.setReturnClass(String[].class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GetDataTableStructResult"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new QName("http://tempuri.org/", "string"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

		oper = new OperationDesc();
		oper.setName("ExecuteProc");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "progId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "methodName"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "wParams"), ParameterDesc.IN, new QName("http://tempuri.org/", "ArrayOfAnyType"), Object[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "anyType"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "ucoInvoke"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "anyType"));
		oper.setReturnClass(Object.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "ExecuteProcResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[3] = oper;

		oper = new OperationDesc();
		oper.setName("GetQueryData");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "currentCulture"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "progId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "tableIndex"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "selectedFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "whereClause"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "sortFields"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "fetchCount"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "isDistinct"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GetQueryDataResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[4] = oper;

		oper = new OperationDesc();
		oper.setName("GetBillWorkflowInfo");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "language"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "sourceTag"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "wParams"), ParameterDesc.IN, new QName("http://tempuri.org/", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GetBillWorkflowInfoResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[5] = oper;

		oper = new OperationDesc();
		oper.setName("GetFlowPendingCount");
		param = new ParameterDesc(new QName("http://tempuri.org/", "groupId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "userId"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "password"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "wParams"), ParameterDesc.IN, new QName("http://tempuri.org/", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new QName("http://tempuri.org/", "string"));
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "logMode"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "GetFlowPendingCountResult"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[6] = oper;

	}

	public CAPInteropServiceExSoapStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public CAPInteropServiceExSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public CAPInteropServiceExSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new Service();
		} else {
			super.service = service;
		}
		((Service) super.service).setTypeMappingVersion("1.2");
		Class<?> cls;
		QName qName;
		QName qName2;
		Class<BeanSerializerFactory> beansf = BeanSerializerFactory.class;
		Class<BeanDeserializerFactory> beandf = BeanDeserializerFactory.class;
		qName = new QName("http://tempuri.org/", ">ExecuteProc");
		cachedSerQNames.add(qName);
		cls = ExecuteProc.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">ExecuteProcResponse");
		cachedSerQNames.add(qName);
		cls = ExecuteProcResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetBillWorkflowInfo");
		cachedSerQNames.add(qName);
		cls = GetBillWorkflowInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetBillWorkflowInfoResponse");
		cachedSerQNames.add(qName);
		cls = GetBillWorkflowInfoResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetDataTableStruct");
		cachedSerQNames.add(qName);
		cls = GetDataTableStruct.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetDataTableStructResponse");
		cachedSerQNames.add(qName);
		cls = GetDataTableStructResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetFlowPendingCount");
		cachedSerQNames.add(qName);
		cls = GetFlowPendingCount.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetFlowPendingCountResponse");
		cachedSerQNames.add(qName);
		cls = GetFlowPendingCountResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetQueryData");
		cachedSerQNames.add(qName);
		cls = GetQueryData.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">GetQueryDataResponse");
		cachedSerQNames.add(qName);
		cls = GetQueryDataResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">ImportData");
		cachedSerQNames.add(qName);
		cls = ImportData.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">ImportDataEx");
		cachedSerQNames.add(qName);
		cls = ImportDataEx.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">ImportDataExResponse");
		cachedSerQNames.add(qName);
		cls = ImportDataExResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", ">ImportDataResponse");
		cachedSerQNames.add(qName);
		cls = ImportDataResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("http://tempuri.org/", "ArrayOfAnyType");
		cachedSerQNames.add(qName);
		cls = Object[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
		qName2 = new QName("http://tempuri.org/", "anyType");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

		qName = new QName("http://tempuri.org/", "ArrayOfString");
		cachedSerQNames.add(qName);
		cls = String[].class;
		cachedSerClasses.add(cls);
		qName = new QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = new QName("http://tempuri.org/", "string");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

	}

	protected Call createCall() throws java.rmi.RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			Enumeration<Object> keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						QName qName = (QName) cachedSerQNames.get(i);
						Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) cachedSerFactories.get(i);
							Class df = (Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public String importData(String groupId, String language, String userId, byte[] password, String progId, String[] tables, String entryParams, String linkageFields, String checkFields, String unReturnFields,
			int logMode) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ImportData");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "ImportData"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, language, userId, password, progId, tables, entryParams, linkageFields, checkFields, unReturnFields, new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String importDataEx(String groupId, String language, String userId, byte[] password, String progId, String[] tables, String entryParams, String linkageFields, String checkFields,
			String unReturnFields, int importMode, int logMode) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ImportDataEx");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "ImportDataEx"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call
					.invoke(new Object[] { groupId, language, userId, password, progId, tables, entryParams, linkageFields, checkFields, unReturnFields, new Integer(importMode), new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String[] getDataTableStruct(String groupId, String language, String userId, byte[] password, String progId, String fields, boolean containsNotAllowEmpty, int logMode)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetDataTableStruct");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "GetDataTableStruct"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, language, userId, password, progId, fields, new Boolean(containsNotAllowEmpty), new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String[]) _resp;
				} catch (Exception _exception) {
					return (String[]) JavaUtils.convert(_resp, String[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public Object executeProc(String groupId, String language, String userId, byte[] password, String progId, String methodName, Object[] wParams, boolean ucoInvoke, int logMode)
			throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ExecuteProc");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "ExecuteProc"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, language, userId, password, progId, methodName, wParams, new Boolean(ucoInvoke), new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (Object) _resp;
				} catch (Exception _exception) {
					return (Object) JavaUtils.convert(_resp, Object.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String getQueryData(String groupId, String language, String userId, byte[] password, String currentCulture, String progId, int tableIndex, String selectedFields, String whereClause,
			String sortFields, int fetchCount, boolean isDistinct, int logMode) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetQueryData");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "GetQueryData"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, language, userId, password, currentCulture, progId, new Integer(tableIndex), selectedFields, whereClause, sortFields, new Integer(fetchCount),
					new Boolean(isDistinct), new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String getBillWorkflowInfo(String groupId, String language, String userId, byte[] password, int sourceTag, String[] wParams, int logMode) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[5]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetBillWorkflowInfo");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "GetBillWorkflowInfo"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, language, userId, password, new Integer(sourceTag), wParams, new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String getFlowPendingCount(String groupId, String userId, byte[] password, String[] wParams, int logMode) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[6]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/GetFlowPendingCount");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "GetFlowPendingCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { groupId, userId, password, wParams, new Integer(logMode) });

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}
