
    private static final java.io.PrintStream sout = System.out;
    private static final java.io.PrintStream serr = System.out;

    @BeforeClass
    public static void before() {
        org.toylang.util.Utility.buildBuiltins();
        org.toylang.core.Application.REPL = true;
    }

    @AfterClass
    public static void after() {
        System.setOut(sout);
        System.setErr(serr);
        org.toylang.core.Application.REPL = false;
    }
