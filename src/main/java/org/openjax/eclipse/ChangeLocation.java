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
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ChangeLocation {
  public static void changeUri(final File locationFile, final File newPath) throws IOException {
    final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    final Path backupPath = new File(locationFile.getParentFile(), locationFile.getName() + "." + timestamp).toPath();
    final Path locationPath = locationFile.toPath();
    Files.copy(locationPath, backupPath);

    final byte[] data = Files.readAllBytes(locationPath);
    final int length = data[16] * 256 + data[17];
    final String uri = new String(data, 18, length);
    final String newUri = "URI//file:" + newPath.getAbsolutePath();
    System.out.println("Changing from " + uri + " to " + newUri);

    System.out.println("Continue?");
    try (final Scanner input = new Scanner(System.in)) {
      input.nextLine().trim();
    }

    final int newLength = newUri.length();
    final byte[] newData = new byte[data.length + newLength - length];

    int y = 0;
    int x = 0;

    // header
    while (y < 16)
      newData[y++] = data[x++];

    // length
    newData[16] = (byte)(newLength / 256);
    newData[17] = (byte)(newLength % 256);

    y += 2;
    x += 2;

    // Uri
    for (int i = 0; i < newLength; ++i) // [N]
      newData[y++] = (byte)newUri.charAt(i);

    x += length;

    // footer
    while (y < newData.length)
      newData[y++] = data[x++];

    if (y != newData.length)
      throw new IndexOutOfBoundsException();

    if (x != data.length)
      throw new IndexOutOfBoundsException();

    Files.write(locationPath, newData);
  }
}