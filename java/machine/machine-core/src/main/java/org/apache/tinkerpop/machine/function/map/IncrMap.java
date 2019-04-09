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
package org.apache.tinkerpop.machine.function.map;

import org.apache.tinkerpop.machine.bytecode.Instruction;
import org.apache.tinkerpop.machine.coefficient.Coefficient;
import org.apache.tinkerpop.machine.function.AbstractFunction;
import org.apache.tinkerpop.machine.function.MapFunction;
import org.apache.tinkerpop.machine.traverser.Traverser;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class IncrMap<C> extends AbstractFunction<C> implements MapFunction<C, Long, Long> {

    private IncrMap(final Coefficient<C> coefficient, final String label) {
        super(coefficient, label);
    }

    @Override
    public Long apply(final Traverser<C, Long> traverser) {
        return traverser.object() + 1L;
    }

    @Override
    public IncrMap<C> clone() {
        return (IncrMap<C>) super.clone();
    }

    public static <C> IncrMap<C> compile(final Instruction<C> instruction) {
        return new IncrMap<>(instruction.coefficient(), instruction.label());
    }

}