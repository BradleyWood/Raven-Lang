package <package>;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import org.toylang.compiler.Errors;
import org.toylang.core.Utility;

import java.util.HashMap;

// this file has been automatically generated. Do not edit
public class Test<name> {

    @BeforeClass
    public static void buildBuiltins() {
        try {
            Class.forName("toylang.Builtin");
        } catch (ClassNotFoundException e) {
                try {
                Errors.reset();
                HashMap\<String, byte[]> gg = Utility.compile("/src/main/toylang/toylang/", true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
<body>
}
