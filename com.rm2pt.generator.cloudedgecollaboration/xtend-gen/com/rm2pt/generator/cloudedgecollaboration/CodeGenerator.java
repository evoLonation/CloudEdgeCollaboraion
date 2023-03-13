/**
 * RM2PT Generator Runtime
 * generated by RM2PT v1.3.0
 */
package com.rm2pt.generator.cloudedgecollaboration;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import net.mydreamy.requirementmodel.rEMODEL.Attribute;
import net.mydreamy.requirementmodel.rEMODEL.Entity;
import net.mydreamy.requirementmodel.rEMODEL.EntityType;
import net.mydreamy.requirementmodel.rEMODEL.EnumEntity;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveTypeCS;
import net.mydreamy.requirementmodel.rEMODEL.TypeCS;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class CodeGenerator extends AbstractGenerator {
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    Iterable<Entity> _filter = Iterables.<Entity>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Entity.class);
    for (final Entity e : _filter) {
      String _name = e.getName();
      String _plus = ("newentities/" + _name);
      String _plus_1 = (_plus + ".java");
      fsa.generateFile(_plus_1, this.compileEntity(e));
    }
    Processor processor = new Processor(resource, fsa);
    processor.process();
  }
  
  public CharSequence compileEntity(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package entities;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import services.impl.StandardOPs;");
    _builder.newLine();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("import java.util.LinkedList;");
    _builder.newLine();
    _builder.append("import java.util.ArrayList;");
    _builder.newLine();
    _builder.append("import java.util.Arrays;");
    _builder.newLine();
    _builder.append("import java.time.LocalDate;");
    _builder.newLine();
    _builder.append("import java.io.Serializable;");
    _builder.newLine();
    _builder.append("import java.lang.reflect.Method;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class ");
    String _name = entity.getName();
    _builder.append(_name);
    {
      Entity _superEntity = entity.getSuperEntity();
      boolean _tripleNotEquals = (_superEntity != null);
      if (_tripleNotEquals) {
        _builder.append(" extends ");
        String _name_1 = entity.getSuperEntity().getName();
        _builder.append(_name_1);
        _builder.append(" ");
      }
    }
    _builder.append(" implements Serializable {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/* all primary attributes */");
    _builder.newLine();
    {
      EList<Attribute> _attributes = entity.getAttributes();
      for(final Attribute attribute : _attributes) {
        _builder.append("\t");
        _builder.append("private ");
        String _compileType = this.compileType(attribute.getType());
        _builder.append(_compileType, "\t");
        _builder.append(" ");
        String _name_2 = attribute.getName();
        _builder.append(_name_2, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * For primary and enum type
   */
  public String compileType(final TypeCS type) {
    String _xifexpression = null;
    if ((type != null)) {
      String _switchResult = null;
      boolean _matched = false;
      if (type instanceof PrimitiveTypeCS) {
        _matched=true;
        String _switchResult_1 = null;
        boolean _matched_1 = false;
        String _name = ((PrimitiveTypeCS)type).getName();
        boolean _equals = Objects.equal(_name, "Boolean");
        if (_equals) {
          _matched_1=true;
          _switchResult_1 = "boolean";
        }
        if (!_matched_1) {
          String _name_1 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_1 = Objects.equal(_name_1, "String");
          if (_equals_1) {
            _matched_1=true;
            _switchResult_1 = "String";
          }
        }
        if (!_matched_1) {
          String _name_2 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_2 = Objects.equal(_name_2, "Real");
          if (_equals_2) {
            _matched_1=true;
            _switchResult_1 = "float";
          }
        }
        if (!_matched_1) {
          String _name_3 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_3 = Objects.equal(_name_3, "Integer");
          if (_equals_3) {
            _matched_1=true;
            _switchResult_1 = "int";
          }
        }
        if (!_matched_1) {
          String _name_4 = ((PrimitiveTypeCS)type).getName();
          boolean _equals_4 = Objects.equal(_name_4, "Date");
          if (_equals_4) {
            _matched_1=true;
            _switchResult_1 = "LocalDate";
          }
        }
        if (!_matched_1) {
          _switchResult_1 = "";
        }
        _switchResult = _switchResult_1;
      }
      if (!_matched) {
        if (type instanceof EnumEntity) {
          _matched=true;
          _switchResult = ((EnumEntity)type).getName();
        }
      }
      if (!_matched) {
        if (type instanceof EntityType) {
          _matched=true;
          _switchResult = ((EntityType)type).getEntity().getName();
        }
      }
      if (!_matched) {
        _switchResult = "";
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
}
