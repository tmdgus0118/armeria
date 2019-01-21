/*
 * Copyright 2019 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.linecorp.armeria.server.cors;

import java.util.function.Function;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Service;
import com.linecorp.armeria.server.annotation.Cors;
import com.linecorp.armeria.server.annotation.DecoratorFactoryFunction;

public final class CorsFactoryFunction implements DecoratorFactoryFunction<Cors> {

    @Override
    public Function<Service<HttpRequest, HttpResponse>, ? extends Service<HttpRequest, HttpResponse>>
    newDecorator(Cors parameter) {
        final boolean anyOrigin = parameter.origins().length > 0 && "*".equals(parameter.origins()[0]);
        final CorsServiceBuilder cb;
        if (anyOrigin) {
            cb = CorsServiceBuilder.forAnyOrigin();
        } else {
            cb = CorsServiceBuilder.forOrigins(parameter.origins());
        }
        if (parameter.shortCircuit()) {
            cb.shortCircuit();
        }
        cb.setConfig(parameter);
        return cb.newDecorator();
    }
}
