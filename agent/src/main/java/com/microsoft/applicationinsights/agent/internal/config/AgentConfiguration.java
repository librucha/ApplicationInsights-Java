/*
 * ApplicationInsights-Java
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the ""Software""), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.microsoft.applicationinsights.agent.internal.config;

import java.util.Map;

import com.microsoft.applicationinsights.agent.internal.config.builder.BuiltInInstrumentationBuilder;

public class AgentConfiguration {

    private Map<String, ClassInstrumentationData> classesToInstrument;
    private BuiltInInstrumentation builtInInstrumentation = new BuiltInInstrumentationBuilder().create();

    public void setClassesToInstrument(Map<String, ClassInstrumentationData> classesToInstrument) {
        this.classesToInstrument = classesToInstrument;
    }

    public Map<String, ClassInstrumentationData> getClassesToInstrument() {
        return classesToInstrument;
    }

    public BuiltInInstrumentation getBuiltInInstrumentation() {
        return builtInInstrumentation;
    }

    public void setBuiltInData(BuiltInInstrumentation builtInInstrumentation) {
        this.builtInInstrumentation = builtInInstrumentation;
    }
}
