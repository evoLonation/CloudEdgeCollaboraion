package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.CommonPackageTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonPackageGenerator extends Generator{
    private String projectName;
    private int outPort;
    private int processNum;
    private List<String> serviceList;
    private List<String> highOperationList;
    private Map<String, List<String>> operationParamMap;
    public CommonPackageGenerator(GlobalInfo globalInfo, List<ServiceInfo> serviceInfoList, List<OperationInfo> highOperationInfoList) {
        this.projectName = globalInfo.getProjectName();
        this.outPort = globalInfo.getOutPort();
        this.processNum = globalInfo.getProcessNum();
        this.serviceList = serviceInfoList.stream().map(ServiceInfo::getName).collect(Collectors.toList());
        this.highOperationList = highOperationInfoList.stream().map(OperationInfo::getName).collect(Collectors.toList());
        this.operationParamMap = highOperationInfoList.stream()
                .collect(Collectors.toMap(OperationInfo::getName,
                        o -> o.getInputParamList().stream().map(Variable::getName).collect(Collectors.toList())));
    }

    @Override
    public void generate() {
        generateFile("common/db.go", CommonPackageTemplate.generateDb());
        generateFile("common/http.go", CommonPackageTemplate.generateHttp());
        generateFile("common/redis.go", CommonPackageTemplate.generateRedis(projectName));
        generateFile("common/tool.go", CommonPackageTemplate.generateTool());
        generateFile("go.mod", CommonPackageTemplate.generateGoMod(projectName));
        generateFile("highpriority/common.go", CommonPackageTemplate.generateHighCommon(projectName));
        generateFile("highpriority/consumer.go", CommonPackageTemplate.generateConsumer(projectName));
        generateFile("highpriority/producer.go", CommonPackageTemplate.generateProducer(projectName));
        generateFile("build/main.yaml", CommonPackageTemplate.generateMainDeploy(projectName, processNum, outPort));
        generateFile("build/Dockerfile", CommonPackageTemplate.generateDockerfile());
        generateFile("build/deploy.sh", CommonPackageTemplate.generateDeploy(projectName, highOperationList).replaceAll("\r\n", "\n"));
        generateFile("build/undeploy.sh", CommonPackageTemplate.generateUndeploy(projectName, highOperationList).replaceAll("\r\n", "\n"));
        generateFile("main/server/main.go", CommonPackageTemplate.generateMain(projectName, serviceList));
        highOperationList.forEach(o -> {
            generateFile("main/highpriority/" + o.toLowerCase() + "/main.go",
                    CommonPackageTemplate.generateHighpriorityMain(projectName, o,
                            operationParamMap.get(o)));
        });
        highOperationList.forEach(o -> {
            generateFile("build/Dockerfile_highpriority_" + o.toLowerCase(),
                    CommonPackageTemplate.generateHighpriorityDockerfile(o));
        });
        generateFile("build/highpriority.yaml",
                CommonPackageTemplate.generateHighpriorityMainDeploy(projectName, highOperationList));
    }
}
