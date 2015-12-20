/*
 * Copyright (c) 2015. Bearchoke
 */

package com.bearchoke.platform.server.common.web.filter;

import lombok.extern.log4j.Log4j2;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * Wraps a HttpServletRequest that has had it's request body turned into a JSON object.
 *
 * N.B. this only really works well with simple JSON objects - ie ones where the properties are only one level deep:
 *
 * <pre><code class="language-javascript">
 * { foo: "bar" }
 * </code></pre>
 *
 * and not
 *
 * <pre><code class="language-javascript">
 * { foo: { bar : "baz" } }
 * </code></pre>
 */
@Log4j2
public class JsonHttpServletRequest extends HttpServletRequestWrapper {
    private JSONObject jsonObject;

    /**
     * @param request
     * @throws IOException
     */
    public JsonHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), outputStream);

        String charset = request.getCharacterEncoding();
        if (log.isDebugEnabled()) {
            log.debug("Charset is: " + charset);
        }
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        String body = outputStream.toString(charset);

        if(!StringUtils.isBlank(body)) {
            jsonObject = JSONObject.fromObject(body);
        }
    }

    /**
     * @inheritdoc
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        if(jsonObject == null) {
            return super.getParameter(name);
        }

        if(!jsonObject.containsKey(name)) {
            return null;
        }

        return jsonObject.getString(name);
    }

    /**
     * @inheritdoc
     * @return
     */
    @Override
    public Enumeration<String> getParameterNames() {
        if(jsonObject == null) {
            return super.getParameterNames();
        }

        final Iterator<String> iterator = jsonObject.keySet().iterator();

        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

    /**
     * @inheritdoc
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        if(jsonObject == null) {
            return super.getParameterValues(name);
        }

        if(!jsonObject.containsKey(name)) {
            return null;
        }

        return new String[] {jsonObject.getString(name)};
    }

    /**
     * @inheritdoc
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if(jsonObject == null) {
            return super.getParameterMap();
        }

        return jsonObject;
    }
}

