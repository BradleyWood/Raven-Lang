package raven;

import org.raven.core.wrappers.TList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

fun getWriter(out) {
    return getWriter(out, null);
}

fun getWriter(out, header) {
    var bw = BufferedWriter(OutputStreamWriter(out));
    var writer = CSVWriter(bw, ",");

    if header != null
        writer.printRecord(header);

    return writer;
}

class CSVWriter(bufferedWriter, delim) {

    private var newRecord = true;

    fun write(arg) {
        if type(arg) == TList.TYPE {
            for (a : arg) {
                write(a);
            }
            return;
        }
        if !newRecord
            bufferedWriter.write(str(delim));
        bufferedWriter.write(str(arg));
        newRecord = false;
    }

    fun printRecord() {
        printRecord([]);
    }

    fun printRecord(args) {
        write(args);
        bufferedWriter.newLine();
        newRecord = true;
    }

    fun close() {
        bufferedWriter.close();
    }
}
