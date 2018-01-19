package org.toylang;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import org.toylang.compiler.Errors;
import org.toylang.core.Utility;

import java.util.HashMap;

// this file has been automatically generated. Do not edit
public class TestTLFiles {
    @BeforeClass
    public static void loadAnnotationTest() {
        TestRunner.loadClass("test/org/toylang/test/AnnotationTest.tl");
    }
    @Test
    public void AnnotationTest_testWrapped() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/AnnotationTest.tl", "testWrapped");
    }
    @Test
    public void AnnotationTest_testPrimitive() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/AnnotationTest.tl", "testPrimitive");
    }
    @BeforeClass
    public static void loadBigIntTest() {
        TestRunner.loadClass("test/org/toylang/test/BigIntTest.tl");
    }
    @Test
    public void BigIntTest_bigIntMultTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntMultTest");
    }
    @Test
    public void BigIntTest_bigIntDivTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntDivTest");
    }
    @Test
    public void BigIntTest_bigIntSubTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntSubTest");
    }
    @Test
    public void BigIntTest_bigIntAddTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntAddTest");
    }
    @Test
    public void BigIntTest_bigIntPowTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntPowTest");
    }
    @Test
    public void BigIntTest_bigIntModTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "bigIntModTest");
    }
    @Test
    public void BigIntTest_intAddOverflowTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "intAddOverflowTest");
    }
    @Test
    public void BigIntTest_intSubOverflowTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "intSubOverflowTest");
    }
    @Test
    public void BigIntTest_intMultOverflowTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "intMultOverflowTest");
    }
    @Test
    public void BigIntTest_intPowOverflowTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BigIntTest.tl", "intPowOverflowTest");
    }
    @BeforeClass
    public static void loadBooleanTest() {
        TestRunner.loadClass("test/org/toylang/test/BooleanTest.tl");
    }
    @Test
    public void BooleanTest_boolTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BooleanTest.tl", "boolTest1");
    }
    @Test
    public void BooleanTest_boolTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BooleanTest.tl", "boolTest2");
    }
    @Test
    public void BooleanTest_boolTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BooleanTest.tl", "boolTest3");
    }
    @Test
    public void BooleanTest_boolNotTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BooleanTest.tl", "boolNotTest");
    }
    @BeforeClass
    public static void loadBreakTest() {
        TestRunner.loadClass("test/org/toylang/test/BreakTest.tl");
    }
    @Test
    public void BreakTest_breakTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BreakTest.tl", "breakTest");
    }
    @Test
    public void BreakTest_breakTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BreakTest.tl", "breakTest2");
    }
    @Test
    public void BreakTest_breakTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BreakTest.tl", "breakTest3");
    }
    @BeforeClass
    public static void loadBuiltinTest() {
        TestRunner.loadClass("test/org/toylang/test/BuiltinTest.tl");
    }
    @Test
    public void BuiltinTest_intConversionTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "intConversionTest");
    }
    @Test
    public void BuiltinTest_realConversionTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "realConversionTest");
    }
    @Test
    public void BuiltinTest_sortTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "sortTest");
    }
    @Test
    public void BuiltinTest_lenTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "lenTest");
    }
    @Test
    public void BuiltinTest_typeTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "typeTest");
    }
    @Test
    public void BuiltinTest_sumTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "sumTest");
    }
    @Test
    public void BuiltinTest_revsereTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/BuiltinTest.tl", "revsereTest");
    }
    @BeforeClass
    public static void loadClassTest() {
        TestRunner.loadClass("test/org/toylang/test/ClassTest.tl");
    }
    @Test
    public void ClassTest_testOk() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ClassTest.tl", "testOk");
    }
    @Test
    public void ClassTest_testSubClass() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ClassTest.tl", "testSubClass");
    }
    @Test
    public void ClassTest_testInheritance() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ClassTest.tl", "testInheritance");
    }
    @Test
    public void ClassTest_testFieldAccess() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ClassTest.tl", "testFieldAccess");
    }
    @BeforeClass
    public static void loadContinueTest() {
        TestRunner.loadClass("test/org/toylang/test/ContinueTest.tl");
    }
    @Test
    public void ContinueTest_continueTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ContinueTest.tl", "continueTest");
    }
    @Test
    public void ContinueTest_continueTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ContinueTest.tl", "continueTest2");
    }
    @Test
    public void ContinueTest_continueTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ContinueTest.tl", "continueTest3");
    }
    @Test
    public void ContinueTest_continueTest4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ContinueTest.tl", "continueTest4");
    }
    @BeforeClass
    public static void loadDictTest() {
        TestRunner.loadClass("test/org/toylang/test/DictTest.tl");
    }
    @Test
    public void DictTest_sizeTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/DictTest.tl", "sizeTest");
    }
    @Test
    public void DictTest_getTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/DictTest.tl", "getTest");
    }
    @Test
    public void DictTest_putTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/DictTest.tl", "putTest");
    }
    @Test
    public void DictTest_dict2dTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/DictTest.tl", "dict2dTest");
    }
    @Test
    public void DictTest_dict2dTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/DictTest.tl", "dict2dTest2");
    }
    @BeforeClass
    public static void loadError() {
        TestRunner.loadClass("test/org/toylang/test/Error.tl");
    }
    @BeforeClass
    public static void loadForEachTest() {
        TestRunner.loadClass("test/org/toylang/test/ForEachTest.tl");
    }
    @Test
    public void ForEachTest_forEachTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForEachTest.tl", "forEachTest");
    }
    @Test
    public void ForEachTest_forEachTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForEachTest.tl", "forEachTest2");
    }
    @Test
    public void ForEachTest_forEachTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForEachTest.tl", "forEachTest3");
    }
    @BeforeClass
    public static void loadForTest() {
        TestRunner.loadClass("test/org/toylang/test/ForTest.tl");
    }
    @Test
    public void ForTest_testRange1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "testRange1");
    }
    @Test
    public void ForTest_testRange2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "testRange2");
    }
    @Test
    public void ForTest_testRange3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "testRange3");
    }
    @Test
    public void ForTest_testRange4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "testRange4");
    }
    @Test
    public void ForTest_forTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest1");
    }
    @Test
    public void ForTest_forTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest2");
    }
    @Test
    public void ForTest_forTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest3");
    }
    @Test
    public void ForTest_forTest4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest4");
    }
    @Test
    public void ForTest_forTest5() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest5");
    }
    @Test
    public void ForTest_forTest6() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest6");
    }
    @Test
    public void ForTest_forTest7() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest7");
    }
    @Test
    public void ForTest_testRange8() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "testRange8");
    }
    @Test
    public void ForTest_forTest9() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ForTest.tl", "forTest9");
    }
    @BeforeClass
    public static void loadFunctionTest() {
        TestRunner.loadClass("test/org/toylang/test/FunctionTest.tl");
    }
    @Test
    public void FunctionTest_testFunction1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/FunctionTest.tl", "testFunction1");
    }
    @Test
    public void FunctionTest_testFunction2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/FunctionTest.tl", "testFunction2");
    }
    @BeforeClass
    public static void loadGoTest() {
        TestRunner.loadClass("test/org/toylang/test/GoTest.tl");
    }
    @Test
    public void GoTest_goTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/GoTest.tl", "goTest1");
    }
    @BeforeClass
    public static void loadHashTest() {
        TestRunner.loadClass("test/org/toylang/test/HashTest.tl");
    }
    @Test
    public void HashTest_hashTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/HashTest.tl", "hashTest1");
    }
    @BeforeClass
    public static void loadIfTest() {
        TestRunner.loadClass("test/org/toylang/test/IfTest.tl");
    }
    @Test
    public void IfTest_ifTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IfTest.tl", "ifTest1");
    }
    @Test
    public void IfTest_ifTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IfTest.tl", "ifTest2");
    }
    @Test
    public void IfTest_ifTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IfTest.tl", "ifTest3");
    }
    @Test
    public void IfTest_ifTest4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IfTest.tl", "ifTest4");
    }
    @Test
    public void IfTest_ifTest5() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IfTest.tl", "ifTest5");
    }
    @BeforeClass
    public static void loadIntegerTest() {
        TestRunner.loadClass("test/org/toylang/test/IntegerTest.tl");
    }
    @Test
    public void IntegerTest_addTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "addTest");
    }
    @Test
    public void IntegerTest_subTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "subTest");
    }
    @Test
    public void IntegerTest_multTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "multTest");
    }
    @Test
    public void IntegerTest_divTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "divTest");
    }
    @Test
    public void IntegerTest_modTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "modTest");
    }
    @Test
    public void IntegerTest_powTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "powTest");
    }
    @Test
    public void IntegerTest_gtTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "gtTest");
    }
    @Test
    public void IntegerTest_gteTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "gteTest");
    }
    @Test
    public void IntegerTest_ltTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "ltTest");
    }
    @Test
    public void IntegerTest_lteTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "lteTest");
    }
    @Test
    public void IntegerTest_eqTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "eqTest");
    }
    @Test
    public void IntegerTest_neTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/IntegerTest.tl", "neTest");
    }
    @BeforeClass
    public static void loadInteropTest() {
        TestRunner.loadClass("test/org/toylang/test/InteropTest.tl");
    }
    @Test
    public void InteropTest_interopTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/InteropTest.tl", "interopTest1");
    }
    @Test
    public void InteropTest_interopTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/InteropTest.tl", "interopTest2");
    }
    @Test
    public void InteropTest_interopTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/InteropTest.tl", "interopTest3");
    }
    @BeforeClass
    public static void loadListTest() {
        TestRunner.loadClass("test/org/toylang/test/ListTest.tl");
    }
    @Test
    public void ListTest_sizeTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "sizeTest");
    }
    @Test
    public void ListTest_listEQTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "listEQTest");
    }
    @Test
    public void ListTest_listNETest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "listNETest");
    }
    @Test
    public void ListTest_appendTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "appendTest");
    }
    @Test
    public void ListTest_getTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "getTest");
    }
    @Test
    public void ListTest_setTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "setTest");
    }
    @Test
    public void ListTest_list2dTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "list2dTest");
    }
    @Test
    public void ListTest_list2dTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "list2dTest2");
    }
    @Test
    public void ListTest_list2dTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "list2dTest3");
    }
    @Test
    public void ListTest_sliceTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ListTest.tl", "sliceTest");
    }
    @BeforeClass
    public static void loadRealTest() {
        TestRunner.loadClass("test/org/toylang/test/RealTest.tl");
    }
    @Test
    public void RealTest_addTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "addTest");
    }
    @Test
    public void RealTest_subTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "subTest");
    }
    @Test
    public void RealTest_multTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "multTest");
    }
    @Test
    public void RealTest_divTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "divTest");
    }
    @Test
    public void RealTest_modTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "modTest");
    }
    @Test
    public void RealTest_powTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "powTest");
    }
    @Test
    public void RealTest_gtTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "gtTest");
    }
    @Test
    public void RealTest_gteTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "gteTest");
    }
    @Test
    public void RealTest_ltTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "ltTest");
    }
    @Test
    public void RealTest_lteTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "lteTest");
    }
    @Test
    public void RealTest_eqTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "eqTest");
    }
    @Test
    public void RealTest_neTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/RealTest.tl", "neTest");
    }
    @BeforeClass
    public static void loadReplTest() {
        TestRunner.loadClass("test/org/toylang/test/ReplTest.tl");
    }
    @Test
    public void ReplTest_replTestVar() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ReplTest.tl", "replTestVar");
    }
    @Test
    public void ReplTest_testVar2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ReplTest.tl", "testVar2");
    }
    @Test
    public void ReplTest_testFun() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ReplTest.tl", "testFun");
    }
    @BeforeClass
    public static void loadScopeTest() {
        TestRunner.loadClass("test/org/toylang/test/ScopeTest.tl");
    }
    @Test
    public void ScopeTest_scopeTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest1");
    }
    @Test
    public void ScopeTest_scopeTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest2");
    }
    @Test
    public void ScopeTest_scopeTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest3");
    }
    @Test
    public void ScopeTest_scopeTest4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest4");
    }
    @Test
    public void ScopeTest_scopeTest5() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest5");
    }
    @Test
    public void ScopeTest_scopeTest6() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/ScopeTest.tl", "scopeTest6");
    }
    @BeforeClass
    public static void loadStringTest() {
        TestRunner.loadClass("test/org/toylang/test/StringTest.tl");
    }
    @Test
    public void StringTest_strTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest1");
    }
    @Test
    public void StringTest_strTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest2");
    }
    @Test
    public void StringTest_strTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest3");
    }
    @Test
    public void StringTest_strTest4() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest4");
    }
    @Test
    public void StringTest_strTest5() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest5");
    }
    @Test
    public void StringTest_strTest6() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest6");
    }
    @Test
    public void StringTest_strTest7() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strTest7");
    }
    @Test
    public void StringTest_strAddTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strAddTest");
    }
    @Test
    public void StringTest_strAddTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strAddTest2");
    }
    @Test
    public void StringTest_strAddTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strAddTest3");
    }
    @Test
    public void StringTest_strGTTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strGTTest");
    }
    @Test
    public void StringTest_strLTTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strLTTest");
    }
    @Test
    public void StringTest_strGTETest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strGTETest");
    }
    @Test
    public void StringTest_strLTETest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strLTETest");
    }
    @Test
    public void StringTest_strEQTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strEQTest");
    }
    @Test
    public void StringTest_strNETest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strNETest");
    }
    @Test
    public void StringTest_strContainsTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strContainsTest");
    }
    @Test
    public void StringTest_strSubstringTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strSubstringTest");
    }
    @Test
    public void StringTest_strEndsWithTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strEndsWithTest");
    }
    @Test
    public void StringTest_strStarssWithTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strStarssWithTest");
    }
    @Test
    public void StringTest_strUppercaseTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strUppercaseTest");
    }
    @Test
    public void StringTest_strLowercaseTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strLowercaseTest");
    }
    @Test
    public void StringTest_strReplaceTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strReplaceTest");
    }
    @Test
    public void StringTest_strIndexOfTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "strIndexOfTest");
    }
    @Test
    public void StringTest_testEqualsIgnoreCase() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "testEqualsIgnoreCase");
    }
    @Test
    public void StringTest_testIsEmpty() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/StringTest.tl", "testIsEmpty");
    }
    @BeforeClass
    public static void loadWhileTest() {
        TestRunner.loadClass("test/org/toylang/test/WhileTest.tl");
    }
    @Test
    public void WhileTest_whileTest1() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/WhileTest.tl", "whileTest1");
    }
    @Test
    public void WhileTest_whileTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/WhileTest.tl", "whileTest2");
    }
    @Test
    public void WhileTest_whileTest3() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/WhileTest.tl", "whileTest3");
    }
    @Test
    public void WhileTest_doWhileTest() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/WhileTest.tl", "doWhileTest");
    }
    @Test
    public void WhileTest_doWhileTest2() throws Throwable {
        TestRunner.doTest("test/org/toylang/test/WhileTest.tl", "doWhileTest2");
    }

}
