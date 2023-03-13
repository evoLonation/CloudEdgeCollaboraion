package com.rm2pt.generator.cloudedgecollaboration.generator;

import org.eclipse.xtext.generator.IFileSystemAccess2;

public abstract class Generator {
	static private IFileSystemAccess2 fsa;
	static public void setFsa(IFileSystemAccess2 fsa) {
		Generator.fsa = fsa;
	}
    protected void generateFile(String filePath, String content ) {
        fsa.generateFile(filePath, content);
    }
    protected abstract void generate();
}
