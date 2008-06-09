/*
 * This file is part of the Alitheia system, developed by the SQO-OSS
 * consortium as part of the IST FP6 SQO-OSS project, number 033331.
 *
 * Copyright 2007-2008 by the SQO-OSS consortium members <info@sqo-oss.eu>
 * Copyright 2007-2008 Georgios Gousios <gousiosg@gmail.com>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package eu.sqooss.impl.service.metricactivator;

import java.util.List;

import eu.sqooss.service.pa.PluginInfo;

/**
 * Class that calculates plug-in dependencies and can answer queries
 * metric calculation order. Maintains several internal dependency trees
 * 
 */
public class MetricDependencyCalculator {
    
    private class DependencyTree<T> {
        
        protected List<DependencyTree<T>> children;
        protected DependencyTree<T> parent;
        protected DependencyTree<T> node;
        
        DependencyTree(DependencyTree<T> pi, List<DependencyTree<T>> t) {
            children = t;
            node = pi;
        }
        
        /**
         * Append a child at the appropriate location of the tree. 
         */
        public boolean addDependency(T node) {
            
            return false;
        }
        
        /**
         * Nicely formatted plug-in dependency tree
         */
        public String toString() {
            return toString(0);
        }
        
        private String toString(int indentation) {
            String result = "";
            String indent = "";
            for (int i=0; i < indentation; ++i)
                    indent = indent + "  ";
            
            result = result + indent + "->" + node.toString() + "\n";
            
            for (DependencyTree<T> dt : children) {
                    result = result + dt.toString(indentation + 1);
            }
            
            return result;
        }
    }  
    
    private DependencyTree<PluginInfo> t;
    
    public MetricDependencyCalculator() {
        //t = 
    }
}

//vi: ai nosi sw=4 ts=4 expandtab