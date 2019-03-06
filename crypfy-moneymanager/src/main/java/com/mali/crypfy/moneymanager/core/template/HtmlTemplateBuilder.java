package com.mali.crypfy.moneymanager.core.template;

import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;

import java.util.Map;

/**
 * Responsible for build htmls templates
 */
public interface HtmlTemplateBuilder {

    /**
     * Build a Html template and returns string as output (Html)
     * @param templateFile
     * @param params
     * @return
     * @throws HtmlTemplateBuilderException
     */
    public String buildTemplate(String templateFile, Map<String,String> params) throws HtmlTemplateBuilderException;
}
