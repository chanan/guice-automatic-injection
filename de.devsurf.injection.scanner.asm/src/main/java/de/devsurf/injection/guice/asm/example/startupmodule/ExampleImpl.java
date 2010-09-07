/*******************************************************************************
 * Copyright 2010, Daniel Manzke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * 
 ******************************************************************************/
package de.devsurf.injection.guice.asm.example.startupmodule;

import de.devsurf.injection.guice.annotations.AutoBind;
import de.devsurf.injection.guice.asm.VirtualClasspathReader;
import de.devsurf.injection.guice.scanner.ClasspathScanner;

/**
 * This class implements the Example interface and uses the {@link AutoBind}-
 * Annotation, so it will be recognized by the {@link ClasspathScanner} and
 * bound to the Name "Example". In this Example the
 * {@link VirtualClasspathReader} is used.
 * 
 * @author Daniel Manzke
 * 
 */
@AutoBind
public class ExampleImpl implements Example {
    @Override
    public String sayHello() {
	return "yeahhh!!!";

    }
}
