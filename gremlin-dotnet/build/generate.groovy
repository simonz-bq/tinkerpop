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

import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.apache.tinkerpop.gremlin.process.traversal.translator.DotNetTranslator
import org.apache.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine
import org.apache.tinkerpop.gremlin.groovy.jsr223.ast.VarAsBindingASTTransformation
import org.apache.tinkerpop.gremlin.groovy.jsr223.ast.RepeatASTTransformationCustomizer
import org.apache.tinkerpop.gremlin.groovy.jsr223.GroovyCustomizer
import org.codehaus.groovy.control.customizers.CompilationCustomizer
import org.apache.tinkerpop.gremlin.language.corpus.FeatureReader

import javax.script.SimpleBindings

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal

// file is overwritten on each generation
radishGremlinFile = new File("${projectBaseDir}/gremlin-dotnet/test/Gremlin.Net.IntegrationTest/Gherkin/Gremlin.cs")

// assumes globally unique scenario names for keys with list of Gremlin traversals as they appear
gremlins = FeatureReader.parse("${projectBaseDir}")

gremlinGroovyScriptEngine = new GremlinGroovyScriptEngine(new GroovyCustomizer() {
    public CompilationCustomizer create() {
        return new RepeatASTTransformationCustomizer(new VarAsBindingASTTransformation())
    }
})
translator = DotNetTranslator.of('g')
g = traversal().withGraph(EmptyGraph.instance())
bindings = new SimpleBindings()
bindings.put('g', g)

radishGremlinFile.withWriter('UTF-8') { Writer writer ->
    writer.writeLine('#region License\n' +
            '\n' +
            '/*\n' +
            ' * Licensed to the Apache Software Foundation (ASF) under one\n' +
            ' * or more contributor license agreements.  See the NOTICE file\n' +
            ' * distributed with this work for additional information\n' +
            ' * regarding copyright ownership.  The ASF licenses this file\n' +
            ' * to you under the Apache License, Version 2.0 (the\n' +
            ' * "License"); you may not use this file except in compliance\n' +
            ' * with the License.  You may obtain a copy of the License at\n' +
            ' *\n' +
            ' *     http://www.apache.org/licenses/LICENSE-2.0\n' +
            ' *\n' +
            ' * Unless required by applicable law or agreed to in writing,\n' +
            ' * software distributed under the License is distributed on an\n' +
            ' * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n' +
            ' * KIND, either express or implied.  See the License for the\n' +
            ' * specific language governing permissions and limitations\n' +
            ' * under the License.\n' +
            ' */\n' +
            '\n' +
            '#endregion\n')

    writer.writeLine("\n\n//********************************************************************************")
    writer.writeLine("//* Do NOT edit this file directly - generated by build/generate.groovy")
    writer.writeLine("//********************************************************************************\n\n")

    writer.writeLine('using System;\n' +
                     'using System.Collections.Generic;\n' +
                     'using Gremlin.Net.Structure;\n' +
                     'using Gremlin.Net.Process.Traversal;\n' +
                     'using Gremlin.Net.Process.Traversal.Strategy.Decoration;\n')
    writer.writeLine('namespace Gremlin.Net.IntegrationTest.Gherkin\n' +
            '{\n' +
            '    public class Gremlin\n' +
            '    {\n' +
            '        public static void InstantiateTranslationsForTestRun()\n' +
            '        {\n' +
            '            // We need to copy the fixed translations as we remove translations from the list after using them\n' +
            '            // so we can enumerate through the translations while evaluating a scenario.\n' +
            '            _translationsForTestRun =\n' +
            '                new Dictionary<string, List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>>>(\n' +
            '                    FixedTranslations.Count);\n' +
            '            foreach (var (traversal, translations) in FixedTranslations)\n' +
            '            {\n' +
            '                _translationsForTestRun.Add(traversal,\n' +
            '                    new List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>>(translations));\n' +
            '            }\n' +
            '        }\n')
    writer.writeLine(
            '        private static IDictionary<string, List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>>>\n' +
            '            _translationsForTestRun;\n')
    writer.writeLine(
            '        private static readonly IDictionary<string, List<Func<GraphTraversalSource, IDictionary<string, object>,ITraversal>>> FixedTranslations = \n' +
            '            new Dictionary<string, List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>>>\n' +
            '            {')

    gremlins.each { k,v ->
        writer.write("               {\"")
        writer.write(k)
        writer.write("\", new List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>> {")
        def collected = v.collect{
            def t = gremlinGroovyScriptEngine.eval(it, bindings)
            [t, t.bytecode.bindings.keySet()]
        }

        def gremlinItty = collected.iterator()
        while (gremlinItty.hasNext()) {
            def t = gremlinItty.next()[0]
            writer.write("(g,p) =>")
            writer.write(translator.translate(t.bytecode).script.
                    replace("xx1", "p[\"xx1\"]").
                    replace("xx2", "p[\"xx2\"]").
                    replace("xx3", "p[\"xx3\"]").
                    replace("v1", "(Vertex) p[\"v1\"]").
                    replace("v2", "(Vertex) p[\"v2\"]").
                    replace("v3", "(Vertex) p[\"v3\"]").
                    replace("v4", "(Vertex) p[\"v4\"]").
                    replace("v5", "(Vertex) p[\"v5\"]").
                    replace("v6", "(Vertex) p[\"v6\"]").
                    replace("vid1", "p[\"vid1\"]").
                    replace("vid2", "p[\"vid2\"]").
                    replace("vid3", "p[\"vid3\"]").
                    replace("vid4", "p[\"vid4\"]").
                    replace("vid5", "p[\"vid5\"]").
                    replace("vid6", "p[\"vid6\"]").
                    replace("e7", "p[\"e7\"]").
                    replace("e10", "p[\"e10\"]").
                    replace("e11", "p[\"e11\"]").
                    replace("eid7", "p[\"eid7\"]").
                    replace("eid10", "p[\"eid10\"]").
                    replace("eid11", "p[\"eid11\"]").
                    replace("l1", "(IFunction) p[\"l1\"]").
                    replace("l2", "(IFunction) p[\"l2\"]").
                    replace("pred1", "(IPredicate) p[\"pred1\"]").
                    replace("c1", "(IComparator) p[\"c1\"]").
                    replace("c2", "(IComparator) p[\"c2\"]"))
            if (gremlinItty.hasNext())
                writer.write(', ')
            else
                writer.write("}")
        }
        writer.writeLine('}, ')
    }
    writer.writeLine('            };\n')

    writer.writeLine(
            '        public static ITraversal UseTraversal(string scenarioName, GraphTraversalSource g, IDictionary<string, object> parameters)\n' +
            '        {\n' +
            '            List<Func<GraphTraversalSource, IDictionary<string, object>, ITraversal>> list = _translationsForTestRun[scenarioName];\n' +
            '            Func<GraphTraversalSource, IDictionary<string, object>, ITraversal> f = list[0];\n' +
            '            list.RemoveAt(0);\n' +
            '            return f.Invoke(g, parameters);\n' +
            '        }\n' +
            '    }\n' +
            '}\n')
}


