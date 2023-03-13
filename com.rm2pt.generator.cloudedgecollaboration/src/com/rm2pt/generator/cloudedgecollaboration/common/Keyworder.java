package com.rm2pt.generator.cloudedgecollaboration.common;

import java.util.regex.Pattern;

public class Keyworder {
    static public String camelToUnderScore(String line){
        if(line == null || "".equals(line)){
            return "";
        }
        var line2 = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        var sb = new StringBuffer();
        var pattern =  Pattern.compile("[A-Z]([a-z\\d]+)?");
        var matcher = pattern.matcher(line2);
        while(matcher.find()){
            var word=matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end()==line2.length()?"":"_");
        }
        return sb.toString();
    }
    static public String camelToDivider(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        var line2 = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        var sb=new StringBuffer();
        var pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        var matcher=pattern.matcher(line2);
        while(matcher.find()){
            var word=matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end()==line2.length()?"":"-");
        }
        return sb.toString();
    }

    static public String firstLowerCase(String name) {
        return name.toLowerCase().substring(0, 1) + name.substring(1);
    }
    static public String firstUpperCase(String name){
        return name.toUpperCase().substring(0, 1) + name.substring(1);
    }
}
