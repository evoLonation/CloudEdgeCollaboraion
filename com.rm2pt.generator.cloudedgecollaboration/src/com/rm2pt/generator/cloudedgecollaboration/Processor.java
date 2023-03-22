package com.rm2pt.generator.cloudedgecollaboration;
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.factory.EntityFactory;
import com.rm2pt.generator.cloudedgecollaboration.factory.GlobalInfoBuilder;
import com.rm2pt.generator.cloudedgecollaboration.factory.ServiceFactory;
import com.rm2pt.generator.cloudedgecollaboration.generator.*;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import net.mydreamy.requirementmodel.rEMODEL.DomainModel;
import net.mydreamy.requirementmodel.rEMODEL.UseCaseModel;

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

		GlobalInfo globalInfo = new GlobalInfoBuilder().build();
        // 中间代码生成阶段
        EntityFactory entityFactory = new EntityFactory(domainModel.getEntity());
        entityFactory.factory();
		new EntityPackageGenerator(entityFactory.getEntityList()).generate();
		new MysqlGenerator(entityFactory.getEntityList(), globalInfo).generate();
		ServiceFactory serviceFactory = new ServiceFactory(useCaseModel.getInteraction(), useCaseModel.getService(), useCaseModel.getContract(), entityFactory.getEntityMap());
        serviceFactory.factory();
		new ServicePackageGenerator(serviceFactory.getServiceList(),
				serviceFactory.getOperation(OperationInfo.ConcurrencyType.HIGHPRIORITY)).generate();

		// todo configpackage, redisdeploy, commonpackage, serverpackage, highprioritypackage, etcpackage 's generate


    }
}
