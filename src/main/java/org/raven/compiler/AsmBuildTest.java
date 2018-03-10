package org.raven.compiler;

//import jdk.internal.org.objectweb.asm.*;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//
//public class AsmBuildTest implements Opcodes {
//
//    public static byte[] dump () throws Exception {
//
//        ClassWriter cw = new ClassWriter(0);
//        FieldVisitor fv;
//        MethodVisitor mv;
//        AnnotationVisitor av0;
//
//        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "org/toylang/compiler/AsmTest", null, "java/lang/Object", null);
//
//        cw.visitInnerClass("java/lang/invoke/MethodHandles$Lookup", "java/lang/invoke/MethodHandles", "Lookup", ACC_PUBLIC + ACC_FINAL + ACC_STATIC);
//
//        {
//            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//            mv.visitCode();
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(1, 1);
//            mv.visitEnd();
//        }
//        {
//            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[] { "java/lang/Exception" });
//            mv.visitCode();
//
//            //
//            mv.visitTypeInsn(NEW, "org/toylang/core/wrappers/TList");
//            mv.visitInsn(DUP);
//            mv.visitMethodInsn(INVOKESPECIAL, "org/toylang/core/wrappers/TList", "<init>", "()V", false);
//            mv.visitTypeInsn(NEW, "org/toylang/core/wrappers/TInt");
//            mv.visitInsn(DUP);
//            mv.visitInsn(ICONST_5);
//            mv.visitMethodInsn(INVOKESPECIAL, "org/toylang/core/wrappers/TInt", "<init>", "(I)V", false);
//            mv.visitMethodInsn(INVOKEVIRTUAL, "org/toylang/core/wrappers/TList", "add", "(Lorg/toylang/core/wrappers/TObject;)Lorg/toylang/core/wrappers/TObject;", false);
//            //
//            mv.visitTypeInsn(CHECKCAST, "org/toylang/core/wrappers/TList");
//
//            mv.visitInvokeDynamicInsn("valueOf", "(Lorg/toylang/core/wrappers/TList;)Lorg/toylang/core/wrappers/TObject;",
//                    new Handle(Opcodes.H_INVOKESTATIC,
//                            "org/toylang/core/Intrinsics", "bootstrap",
//                            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;I)Ljava/lang/invoke/CallSite;"),
//                    Type.getType(String.class), 1);
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(4, 1);
//            mv.visitEnd();
//        }
//        {
//            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC, "lambda$main$0", "()V", null, null);
//            mv.visitCode();
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(0, 0);
//            mv.visitEnd();
//        }
//        cw.visitEnd();
//
//        return cw.toByteArray();
//    }
//
//    public static void main(String[] args) throws Exception {
//        FileOutputStream fos = new FileOutputStream("target/classes/org/toylang/compiler/AsmTest.class");
//        fos.write(dump());
//        fos.close();
//        System.err.println("Done");
//    }
//}