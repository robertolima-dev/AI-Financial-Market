package com.mali.crypfy.indexmanager.core.template;

import com.mali.crypfy.indexmanager.core.template.exception.HtmlTemplateBuilderException;

import java.util.Map;

public interface HtmlTemplateBuilder {
    public String buildTemplate(String templateFile, Map<String,String> params) throws HtmlTemplateBuilderException;
    public String buildTemplateGeneric(String templateFile, Map<String,Object> params) throws HtmlTemplateBuilderException;
}