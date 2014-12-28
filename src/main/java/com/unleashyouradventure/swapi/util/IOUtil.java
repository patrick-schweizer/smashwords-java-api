package com.unleashyouradventure.swapi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.unleashyouradventure.swapi.load.PageLoader.ProgressCallback;

/**
 * Most of the functions herein are re-implementations of the ones in apache io
 * IOUtils. The reason for re-implementing this is that the functions are fairly
 * simple and using my own implementation saves the inclusion of a 200Kb jar
 * file.
 */
public class IOUtil {

    public static final int IO_COPY_BUFFER_SIZE = 1024 * 4;

    /**
     * Returns the contents of the InputStream as a byte[]
     * 
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in, ProgressCallback callback, long expectedContentLength)
            throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(in, result, callback, expectedContentLength);
        result.flush();
        return result.toByteArray();
    }

    /**
     * if totalNrRead < 0 then totalNrRead is returned, if (nrRead +
     * totalNrRead) < Integer.MAX_VALUE then nrRead + totalNrRead is returned,
     * -1 otherwise.
     * 
     * @param nrRead
     * @param totalNrNread
     * @return
     */
    protected static int calcNewNrReadSize(int nrRead, int totalNrNread) {
        if (totalNrNread < 0) {
            return totalNrNread;
        }
        if (totalNrNread > (Integer.MAX_VALUE - nrRead)) {
            return -1;
        } else {
            return (totalNrNread + nrRead);
        }
    }

    /**
     * Copies the contents of the InputStream to the OutputStream.
     * 
     * @param in
     * @param out
     * @return the nr of bytes read, or -1 if the amount > Integer.MAX_VALUE
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out, ProgressCallback callback, long expectedContentLength)
            throws IOException {
        byte[] buffer = new byte[IO_COPY_BUFFER_SIZE];
        int readSize = -1;
        int result = 0;
        while ((readSize = in.read(buffer)) >= 0) {
            out.write(buffer, 0, readSize);
            result = calcNewNrReadSize(readSize, result);
            callback.setProgress(calculateProgress(expectedContentLength, result));
        }
        out.flush();
        return result;
    }

    private static int calculateProgress(long all, long current) {
        return (int) (((double) current / (double) all) * 100);
    }
}
