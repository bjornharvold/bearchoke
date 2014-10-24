/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bearchoke.platform.server.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
@Slf4j
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
        String body = outputStream.toString(request.getCharacterEncoding());

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

