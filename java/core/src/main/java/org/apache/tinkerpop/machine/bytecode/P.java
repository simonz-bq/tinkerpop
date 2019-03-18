/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.machine.bytecode;

import org.apache.tinkerpop.util.NumberHelper;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class P<S> {

    public enum Type {
        eq {
            public boolean test(final Object first, final Object second) {
                return null == first ? null == second : (first instanceof Number && second instanceof Number
                        ? NumberHelper.compare((Number) first, (Number) second) == 0
                        : first.equals(second));
            }
        }, neq {
            public boolean test(final Object first, final Object second) {
                return !eq.test(first, second);
            }
        }, lt {
            public boolean test(final Object first, final Object second) {
                return null != first && null != second && (
                        first instanceof Number && second instanceof Number
                                ? NumberHelper.compare((Number) first, (Number) second) < 0
                                : ((Comparable) first).compareTo(second) < 0);
            }
        }, gt {
            public boolean test(final Object first, final Object second) {
                return null != first && null != second && (
                        first instanceof Number && second instanceof Number
                                ? NumberHelper.compare((Number) first, (Number) second) > 0
                                : ((Comparable) first).compareTo(second) > 0);
            }
        }, lte {
            public boolean test(final Object first, final Object second) {
                return null == first ? null == second : (null != second && !gt.test(first, second));
            }
        }, gte {
            public boolean test(final Object first, final Object second) {
                return null == first ? null == second : (null != second && !lt.test(first, second));
            }
        }, regex {
            public boolean test(final Object first, final Object second) {
                return ((String) first).matches((String) second);
            }
        };

        public abstract boolean test(final Object first, final Object second);

        public static Type get(final Object object) {
            return P.Type.valueOf(object.toString());
        }

    }

    private final S object;
    private final Type type;

    private P(final Type type, final S object) {
        this.type = type;
        this.object = object;
    }

    public S object() {
        return this.object;
    }

    public Type type() {
        return this.type;
    }

    public static <S> P<S> eq(final Object object) {
        return new P<>(Type.eq, (S) object);
    }

    public static <S> P<S> neq(final Object object) {
        return new P<>(Type.neq, (S) object);
    }

    public static <S> P<S> lt(final Object object) {
        return new P<>(Type.lt, (S) object);
    }

    public static <S> P<S> gt(final Object object) {
        return new P<>(Type.gt, (S) object);
    }

    public static <S> P<S> lte(final Object object) {
        return new P<>(Type.lte, (S) object);
    }

    public static <S> P<S> gte(final Object object) {
        return new P<>(Type.gte, (S) object);
    }

    public static <S> P<S> regex(final Object object) {
        return new P<>(Type.regex, (S) object);
    }

    @Override
    public String toString() {
        return this.type.toString();
    }


}