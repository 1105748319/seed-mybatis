package com.czy.seed.mybatis.sql.template;

import com.czy.seed.mybatis.config.exception.TemplateNotExistException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Comup on 2017/02/21.
 */
class FreeMarkerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerUtil.class);

    private static final Configuration CONFIGURATION = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    private static final Map<String, Template> TEMPLATE_MAPS = Collections.synchronizedMap(new HashMap<String, Template>());

    private static final StringWriter STRING_WRITER = new StringWriter();

    private FreeMarkerUtil() {
    }

    static {
        //转码，以防止路径内有空格
        CONFIGURATION.setDefaultEncoding("UTF-8");
        CONFIGURATION.setClassForTemplateLoading(FreeMarkerUtil.class, "ftl");
    }

    public static synchronized String process(String templateName, Object params) {

        //转码，以防止路径内有空格
        try {
            TEMPLATE_MAPS.put(templateName, CONFIGURATION.getTemplate(templateName + ".ftl"));
        } catch (Exception e) {
            LOGGER.error("error occurred while load ftl file : " + templateName, e);
            throw new RuntimeException("error occurred while load ftl file : " + templateName, e);
        }
        if (TEMPLATE_MAPS.containsKey(templateName)) {
            STRING_WRITER.getBuffer().delete(0, STRING_WRITER.getBuffer().length());
            try {
                TEMPLATE_MAPS.get(templateName).process(params, STRING_WRITER);
            } catch (Exception e) {
                LOGGER.error("error occurred while process a ftl file", e);
                throw new RuntimeException("error occurred while process a ftl file", e);
            }
            STRING_WRITER.flush();
            return STRING_WRITER.toString();
        } else {
            throw new TemplateNotExistException(" can't find the template: " + templateName
                    + ", maybe the method:loadTemplateToCache is needed to run first to load template first");
        }
    }
}
