package com.rm2pt.generator.cloudedgecollaboration;
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.factory.EntityFactory;
import com.rm2pt.generator.cloudedgecollaboration.factory.OperationBodyFactory;
import com.rm2pt.generator.cloudedgecollaboration.factory.ServiceFactory;
import com.rm2pt.generator.cloudedgecollaboration.generator.*;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import net.mydreamy.requirementmodel.rEMODEL.DomainModel;
import net.mydreamy.requirementmodel.rEMODEL.UseCaseModel;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;

public class Processor {
	private UseCaseModel useCaseModel;
	private DomainModel domainModel;
	public Processor(Resource resource, IFileSystemAccess2 fsa) {
		Generator.setFsa(fsa);
		resource.getAllContents().forEachRemaining(o -> {
			if(o instanceof UseCaseModel) {
				this.useCaseModel = (UseCaseModel)o;
			}
			if(o instanceof DomainModel) {
				this.domainModel = (DomainModel)o;
			}
		});
	}

    public void process(){
    	
    	System.out.println(Keyworder.camelToUnderScore("MyNameIsZzy"));
    	
        // 中间代码生成阶段
        EntityFactory entityFactory = new EntityFactory(domainModel.getEntity());
        entityFactory.factory();
		new EntityPackageGenerator(entityFactory.getEntityList()).generate();
		new DDLGenerator(entityFactory.getEntityList()).generate();

        ServiceFactory serviceFactory = new ServiceFactory(useCaseModel.getInteraction(), useCaseModel.getService(), useCaseModel.getContract(), entityFactory.getEntityMap());
//        serviceFactory.factory();
		new ServicePackageGenerator(serviceFactory.getServiceList());

        new ConfigPackageGenerator();



		// todo zzy
//        new RpcPackageGenerator(serviceFactory.getRpcServiceList());
//
//        new ListenPackageGenerator(serviceFactory.getNormalServiceList(), serviceFactory.getMqttServiceList(), serviceFactory.getHttpServiceList());


    }
}
