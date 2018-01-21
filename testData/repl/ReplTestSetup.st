
    private static final PrintStream sout = System.out;
    private static final PrintStream serr = System.out;

    @BeforeClass
    public static void before() {
        org.toylang.core.Application.REPL = true;
    }

    @AfterClass
    public static void after() {
        System.setOut(sout);
        System.setErr(serr);
        org.toylang.core.Application.REPL = false;
    }
