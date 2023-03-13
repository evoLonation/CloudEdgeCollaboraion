package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import net.mydreamy.requirementmodel.rEMODEL.Contract;
import net.mydreamy.requirementmodel.rEMODEL.Interaction;
import net.mydreamy.requirementmodel.rEMODEL.Service;

import java.util.List;
import java.util.Map;

/**
 * 需要通过顺序图和服务相关的语法树，以及上一步生成出来的EntitiInfo来得到服务及其操作的签名
 * 遍历Service的Operation的时候，需要同时扫描顺序图，检测每个Operation实际上是Operation还是Logic
 * 1、生成出所有的Service
 * 2、Operation只需要签名，不用生成Body部分
 * 3、Logic需要生成出签名和流程
 */
public class ServiceFactory {

    public ServiceFactory(List<Interaction> interactionList, List<Service> serviceList, List<Contract> contractList, Map<String, EntityInfo> entityMap) {

    }

    public void factory(){

    }
    public List<ServiceInfo> getServiceList(){
        throw new UnsupportedOperationException();
    }
    public Map<String, ServiceInfo> getServiceMap(){
        throw new UnsupportedOperationException();
    }
    public List<ServiceInfo> getMqttServiceList(){
        throw new UnsupportedOperationException();
    }
    public List<ServiceInfo> getRpcServiceList(){
        throw new UnsupportedOperationException();
    }
    public List<ServiceInfo> getHttpServiceList(){
        throw new UnsupportedOperationException();
    }
    public List<ServiceInfo> getNormalServiceList(){
        throw new UnsupportedOperationException();
    }


}
