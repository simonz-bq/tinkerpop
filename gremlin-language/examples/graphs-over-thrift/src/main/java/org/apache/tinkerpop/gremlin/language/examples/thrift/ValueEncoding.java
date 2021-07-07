package org.apache.tinkerpop.gremlin.language.examples.thrift;

import java.io.IOException;

public interface ValueEncoding {
    String getTypeName();

    Class getJavaClass();

    Object decode(String encoded) throws IOException;

    String encode(Object decoded) throws IOException;
}