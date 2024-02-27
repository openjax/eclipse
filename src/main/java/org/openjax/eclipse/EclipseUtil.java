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
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Change path of project in workspace.
 */
public class EclipseUtil {
  public static void main(final String[] args) throws IOException {
    final File workspaceDir = new File(args[0]);
    if (!workspaceDir.isDirectory())
      throw new FileNotFoundException(workspaceDir.getAbsolutePath());

    final File projectsDir = new File(workspaceDir, ".metadata/.plugins/org.eclipse.core.resources/.projects/");
    if (!projectsDir.isDirectory())
      throw new FileNotFoundException(projectsDir.getAbsolutePath());

    final File projectDir = new File(workspaceDir, ".metadata/.plugins/org.eclipse.core.resources/.projects/" + args[1]);
    if (!projectDir.isDirectory())
      throw new FileNotFoundException(projectDir.getAbsolutePath());

    final File locationFile = new File(projectDir, ".location");
    if (!locationFile.isFile())
      throw new FileNotFoundException(locationFile.getAbsolutePath());

    final File newPath = new File(args[2]);
    if (!newPath.isDirectory())
      throw new FileNotFoundException(newPath.getAbsolutePath());

    ChangeLocation.changeUri(locationFile, newPath);
  }
}