/* Copyright (c) 2023 OpenJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.openjax.eclipse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

import org.jaxsb.runtime.Bindings;
import org.libj.util.Comparators;
import org.openjax.www.eclipse.classpath.xAA;
import org.openjax.xml.dom.DOMStyle;
import org.openjax.xml.dom.DOMs;
import org.xml.sax.SAXException;

public class Format {
  private static final String[] order = {
    "src/main/java",
    "src/main/resources",
    "src/test/java",
    "src/test/resources",
    "target/generated-sources",
    "target/generated-test-sources",
    "org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER",
    "org.eclipse.jdt.launching.JRE_CONTAINER/",
    "target/classes"
  };

  private static final Comparator<xAA.Classpath.Classpathentry> pathComparator = Comparators.newFixedOrderComparator(
    (final xAA.Classpath.Classpathentry c) -> c.getPath$().text(),
    (final String o1, final String o2) -> o1.startsWith(o2) ? 0 : -1, order);

  public static void main(final String[] args) throws IOException, SAXException {
    for (final String arg : args) { // [A]
      final File file = new File(arg);
      Files.copy(file.toPath(), new File(file + "." + (System.currentTimeMillis() / 1000)).toPath(), StandardCopyOption.REPLACE_EXISTING);

      final xAA.Classpath classpath = (xAA.Classpath)Bindings.parse(file, "http://www.openjax.org/eclipse/classpath.xsd");
      classpath.getClasspathentry().sort(pathComparator);
      final String xml = DOMs.domToString(classpath.toDOM(), DOMStyle.INDENT, DOMStyle.OMIT_NAMESPACES);
      Files.write(file.toPath(), xml.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
  }
}