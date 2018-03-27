package raven;

import org.raven.core.wrappers.TList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

fun getWriter(path) {
    return getWriter(path, null);
}

fun getWriter(path, header) {
    var bw = BufferedWriter(OutputStreamWriter(FileOutputStream(path)));
    var writer = CSVWriter(bw, ",");

    if header != null
        writer.printRecord(header);

    return writer;
}

class CSVWriter(bufferedWriter, delim) {

    private var newRecord = true;

    fun write(arg) {
        if !newRecord
            bufferedWriter.write(str(delim));

        bufferedWriter.write(str(arg));
        newRecord = false;
    }

    fun printRecord() {
        printRecord([]);
    }

    fun printRecord(args) {
        if type(args) == TList.TYPE {
            for arg : args {
                write(arg);
            }
        } else {
            write(args);
        }
        bufferedWriter.newLine();
        newRecord = true;
    }

    fun close() {
        bufferedWriter.close();
    }
}
