package toylang.lang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

// Returns a list containing each line
public fun readFile(path) {
    var fis = FileInputStream(path);
    var reader = InputStreamReader(fis);
    var br = BufferedReader(reader);
    var list = [];
    var line = br.readLine();
    while(line != null) {
        list = list + line;
        line = br.readLine();
    }
    return list;
}
