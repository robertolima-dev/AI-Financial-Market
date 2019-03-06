package com.mali.crypfy.moneymanager.core.template.impl;

import com.mali.crypfy.moneymanager.core.template.HtmlTemplateBuilder;
import com.mali.crypfy.moneymanager.core.template.exception.HtmlTemplateBuilderException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Implementation of Html Template Builder
 */
@Component
public class HtmlTemplateBuilderFreemarker implements HtmlTemplateBuilder {

    final static Logger logger = LoggerFactory.getLogger(HtmlTemplateBuilderFreemarker.class);
    private Configuration cfg;

    /**
     * Init Free Marker Configs
     */
    @PostConstruct
    private void init() {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(getClass(),"/templates/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }


    @Override
    public String buildTemplate(String templateFile, Map<String, String> params) throws HtmlTemplateBuilderException {
        try {
            Template template = cfg.getTemplate(templateFile);
            Writer out = new StringWriter();
            template.process(params,out);
            return out.toString();
        } catch (IOException e) {
            logger.error("io exception",e);
            throw new HtmlTemplateBuilderException("ocorreu um erro ao gerar o template");
        } catch (freemarker.template.TemplateException e) {
            logger.error("freemarker error exception");
            throw new HtmlTemplateBuilderException("ocorreu um erro ao gerar o template");
        }
    }
}
