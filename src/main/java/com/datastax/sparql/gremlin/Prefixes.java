/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.datastax.sparql.gremlin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
public class Prefixes {
    // TODO erase everything that's not being used
    public final static String BASE_URI = "http://www.tinkerpop.com/traversal/";

    final static List<String> PREFIXES = Arrays.asList("edge", "property", "value");

    final static String PREFIX_DEFINITIONS;
    
    public final static String NODE              = "n";
    public final static String EDGE              = "e";
    public final static String NODE_PROPERTY     = "np";
    public final static String EDGE_PROPERTY     = "ep";
    public final static String METAPROPERTY      = "meta";
    public final static String NODE_URI          = BASE_URI + "node";
    public final static String EDGE_URI          = BASE_URI + "edge";
    public final static String NODE_PROPERTY_URI = NODE_URI + "/property";
    public final static String EDGE_PROPERTY_URI = EDGE_URI + "/property";
    public final static String METAPROPERTY_URI  = NODE_URI + "/metaproperty";
    final static String PREFIXES_PREAMBLE;
    
    static {
        final StringBuilder builder = new StringBuilder();
        PREFIXES.forEach((prefix) -> {
            builder.append("PREFIX ").append(prefix.substring(0, 1)).append(": <").append(getURI(prefix)).
                    append(">").append(System.lineSeparator());
        });
        PREFIX_DEFINITIONS = builder.toString();
    }
    
    static{ //new prefixes
        HashMap<String, String> prefixesMap = new HashMap<>();
        prefixesMap.put(NODE, NODE_URI);
        prefixesMap.put(EDGE, EDGE_URI);
        prefixesMap.put(NODE_PROPERTY, NODE_PROPERTY_URI);
        prefixesMap.put(EDGE_PROPERTY, EDGE_PROPERTY_URI);
        prefixesMap.put(METAPROPERTY, METAPROPERTY_URI);
        final StringBuilder builder = new StringBuilder();
        prefixesMap.entrySet().forEach((kv) -> {
            builder.append("PREFIX ")
                    .append(kv.getKey())
                    .append(": <")
                    .append(kv.getValue())
                    .append("#")
                    .append(">")
                    .append(System.lineSeparator());
        });
        PREFIXES_PREAMBLE = builder.toString();
    }
    
    public static String preamblePrepend(String query){
        return PREFIXES_PREAMBLE + query;
    }

    public static String getURI(final String prefix) {
        return BASE_URI + prefix + "#";
    }

    public static String getURIValue(final String uri) {
        return uri.substring(uri.indexOf("#") + 1);
    }

    public static String getPrefix(final String uri) {
        final String tmp = uri.substring(0, uri.indexOf("#"));
        return tmp.substring(tmp.lastIndexOf("/") + 1);
    }

    public static String prepend(final String script) {
        return PREFIX_DEFINITIONS + script;
    }

    public static StringBuilder prepend(final StringBuilder scriptBuilder) {
        return scriptBuilder.insert(0, PREFIX_DEFINITIONS);
    }
}
