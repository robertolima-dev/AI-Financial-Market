package com.mali.crypfy.auth.core.template;

import com.mali.crypfy.auth.core.template.exception.HtmlTemplateBuilderException;

import java.util.Map;

public interface HtmlTemplateBuilder {
    public String buildTemplate(String templateFile, Map<String,String> params) throws HtmlTemplateBuilderException;
}
