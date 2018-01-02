/*
 * Copyright 2016-2018 Talsma ICT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.talsmasoftware.umldoclet.model;

import nl.talsmasoftware.umldoclet.rendering.Renderer;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

/**
 * Class representing a type name.
 * <p>
 * This is less simple than it sounds: A type basically has a 'qualified name' and a 'simple name'
 * (these may be equal).
 * <p>
 * Also, if the type is a generic type, the actual type parameters can be seen as 'part' of the name:
 * The names of {@code List<String>} and {@code List<Integer>} are different, while in Java the actual
 * types are equal (due to erasure of the generic type).
 *
 * @author Sjoerd Talsma
 */
public class TypeName implements Renderer, Comparable<TypeName>, Serializable {
    public final String simple, qualified;
    private final TypeName[] generics;

    public enum Display {
        NONE,
        SIMPLE,
        QUALIFIED,
        QUALIFIED_GENERICS
    }

    public TypeName(String simpleName, String qualifiedName, TypeName... generics) {
        this.simple = simpleName;
        this.qualified = qualifiedName;
        this.generics = generics.clone();
    }

    public TypeName[] getGenerics() {
        return generics.clone();
    }

    @Override
    public <A extends Appendable> A writeTo(A output) {
        return writeTo(output, Display.SIMPLE);
    }

//    protected <A extends Appendable> A writeTo(A output, boolean qualified, Boolean qualifiedGenerics) {
//        try {
//            output.append(qualified ? this.qualified : simple);
//            if (qualifiedGenerics != null) writeGenericsTo(output, qualifiedGenerics);
//            return output;
//        } catch (IOException ioe) {
//            throw new IllegalStateException("I/O error writing type name \"" + qualified + "\" to the output: "
//                    + ioe.getMessage(), ioe);
//        }
//    }

    protected <A extends Appendable> A writeTo(A output, Display typeConfig) {
        if (!Display.NONE.equals(typeConfig)) try {

            if (Display.QUALIFIED.equals(typeConfig) || Display.QUALIFIED_GENERICS.equals(typeConfig)) {
                output.append(this.qualified);
            } else {
                output.append(this.simple);
            }
            writeGenericsTo(output, Display.QUALIFIED.equals(typeConfig) ? Display.SIMPLE : typeConfig);

        } catch (IOException ioe) {
            throw new IllegalStateException("I/O error writing type name \"" + qualified + "\" to the output: "
                    + ioe.getMessage(), ioe);
        }
        return output;
    }

    private <A extends Appendable> A writeGenericsTo(A output, Display typeConfig) throws IOException {
        if (generics.length > 0) {
            String sep = "<";
            for (TypeName generic : generics) {
                generic.writeTo(output.append(sep), typeConfig);
                sep = ", ";
            }
            output.append('>');
        }
        return output;
    }

    @Override
    public int compareTo(TypeName other) {
        requireNonNull(other, "Cannot compare with type name <null>.");
        return comparing((TypeName type) -> type.qualified.toLowerCase())
                .thenComparing(type -> type.qualified)
                .compare(this, other);
    }

    @Override
    public int hashCode() {
        return qualified.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof TypeName && this.compareTo((TypeName) other) == 0);
    }

    @Override
    public String toString() {
        return writeTo(new StringWriter()).toString();
    }

    public static class Array extends TypeName {
        private final TypeName delegate;

        private Array(TypeName delegate) {
            super(delegate.simple, delegate.qualified, delegate.generics);
            this.delegate = delegate;
        }

        public static Array of(TypeName delegate) {
            return new Array(requireNonNull(delegate, "Component type of array is <null>."));
        }

        protected <A extends Appendable> A writeTo(A output, Display typeConfig) {
            try {
                delegate.writeTo(output, typeConfig);
                output.append("[]");
            } catch (IOException ioe) {
                throw new IllegalStateException("I/O error writing array type \"" + qualified + "\": " + ioe.getMessage(), ioe);
            }
            return output;
        }
    }

}
