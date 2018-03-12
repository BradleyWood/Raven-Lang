package raven;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// Returns a list containing each line
// fully reads file
fun readLines(path) {
    var fis = FileInputStream(path);
    var reader = InputStreamReader(fis);
    var br = BufferedReader(reader);
    var list = [];
    var line = br.readLine();
    while(line != null) {
        list = list + line;
        line = br.readLine();
    }
    br.close();
    return list;
}

// write each line to the file;
fun writeLines(path, lines) {
    var fos = FileOutputStream(path);
    var writer = OutputStreamWriter(fos);
    var bw = BufferedWriter(writer);

    for i range 0 to len(lines) {
        bw.write(lines[i]);
        bw.newLine();
    }
    bw.close();
}
