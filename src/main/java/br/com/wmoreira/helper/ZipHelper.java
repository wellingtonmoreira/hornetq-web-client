package br.com.wmoreira.helper;

import br.com.wmoreira.Handler;
import org.apache.commons.io.IOUtils;

import javax.enterprise.context.RequestScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RequestScoped
public class ZipHelper {

    public int processFilesContent(ZipFile file, Handler handler) throws Exception {
        List<String> contents = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = file.entries();
        int amountProcessed = 0;

        try {
            do {
                ZipEntry entry = entries.nextElement();
                InputStream stream = file.getInputStream(entry);
                handler.handle(IOUtils.toString(stream, "UTF-8"));
                amountProcessed++;
            } while (entries.hasMoreElements());
        } catch (Exception exc) {
            throw exc;
        }

        return amountProcessed;
    }

    public int processFilesContent(InputStream in, Handler handler) throws Exception {
        File tempFile = File.createTempFile("tmpzip" + new Date().getTime(), ".zip");
        FileOutputStream out = new FileOutputStream(tempFile);
        try {
            IOUtils.copy(in, out);
            ZipFile zipFile = new ZipFile(tempFile);
            return processFilesContent(zipFile, handler);
        } finally {
            tempFile.delete();
        }
    }
}
