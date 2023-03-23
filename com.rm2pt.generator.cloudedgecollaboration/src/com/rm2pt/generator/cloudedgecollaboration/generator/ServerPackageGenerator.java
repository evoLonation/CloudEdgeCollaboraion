package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.generator.common.TypeGenerator;
import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.ServerTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerPackageGenerator extends Generator{

    private List<ServiceInfo> serviceInfoList;
    private List<OperationInfo> operationInfoList;
    private String projectName;
    public ServerPackageGenerator(List<ServiceInfo> serviceInfoList, GlobalInfo globalInfo) {
        this.serviceInfoList = serviceInfoList;
        this.operationInfoList = new ArrayList<>();
        serviceInfoList.forEach(serviceInfo -> operationInfoList.addAll(serviceInfo.getOperationList()));
        this.projectName = globalInfo.getProjectName();
    }

    @Override
    public void generate() {
        List<String> reqList = operationInfoList.stream().map(this::generateReq).collect(Collectors.toList());
        List<String> resList = operationInfoList.stream().map(this::generateRes).collect(Collectors.toList());



        List<String> hpFragmentList = new ArrayList<>();
        List<String> fragmentList = new ArrayList<>();
        serviceInfoList.forEach(serviceInfo -> {
            serviceInfo.getOperationList().forEach(operationInfo -> {
            	List<String> paramList = operationInfo.getInputParamList().stream().map(Variable::getName).collect(Collectors.toList());
                if(operationInfo.getConcurrencyType() == OperationInfo.ConcurrencyType.HIGHPRIORITY){
                    hpFragmentList.add(ServerTemplate.generateStartFuncFragmentHP(serviceInfo.getName(), operationInfo.getName(), paramList));
                }
                fragmentList.add(ServerTemplate.generateStartFuncFragment(serviceInfo.getName(), operationInfo.getName(), paramList));
            });
        });
        String startFunc = ServerTemplate.generateStartFunc(serviceInfoList.stream().map(ServiceInfo::getName).collect(Collectors.toList()),
                Stream.concat(fragmentList.stream(), hpFragmentList.stream()).collect(Collectors.toList()));
        generateFile("server/server.go", ServerTemplate.generateServer(projectName, reqList, resList, startFunc));
    }

    private String generateReq(OperationInfo operationInfo){
        List<String> paramNameList = operationInfo.getInputParamList().stream().map(Variable::getName).collect(Collectors.toList());
        List<String> paramTypeList = operationInfo.getInputParamList().stream().map(v -> TypeGenerator.generateGoType(v.getType())).collect(Collectors.toList());
        return ServerTemplate.generateReq(operationInfo.getName(), paramNameList, paramTypeList);
    }
    private String generateRes(OperationInfo operationInfo){
        return ServerTemplate.generateRes(operationInfo.getName(), TypeGenerator.generateGoType(operationInfo.getReturnType()));
    }
}
