/*
 * Javassist, a Java-bytecode translator toolkit.
 * Copyright (C) 1999- Shigeru Chiba. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License.  Alternatively, the contents of this file may be used under
 * the terms of the GNU Lesser General Public License Version 2.1 or later,
 * or the Apache License Version 2.0.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */

package javassist.bytecode;

import javassist.bytecode.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

/**
 * A class representing
 * <code>RuntimeVisibleAnnotations_attribute</code> and
 * <code>RuntimeInvisibleAnnotations_attribute</code>.
 *
 * <p>To obtain an AnnotationAttribute object, invoke
 * <code>getAttribute(AnnotationsAttribute.visibleTag)</code>
 * in <code>ClassFile</code>, <code>MethodInfo</code>,
 * or <code>FieldInfo</code>.  The obtained attribute is a
 * runtime visible annotations attribute.
 * If the parameter is
 * <code>AnnotationAttribute.invisibleTag</code>, then the obtained
 * attribute is a runtime invisible one.
 *
 * <p>For example,
 *
 * <pre>
 * import javassist.bytecode.annotation.Annotation;
 *    :
 * CtMethod m = ... ;
 * MethodInfo minfo = m.getMethodInfo();
 * AnnotationsAttribute attr = (AnnotationsAttribute)
 *         minfo.getAttribute(AnnotationsAttribute.invisibleTag);
 * Annotation an = attr.getAnnotation("Author");
 * String s = ((StringMemberValue)an.getMemberValue("name")).getValue();
 * System.out.println("@Author(name=" + s + ")");
 * </pre>
 *
 * <p>This code snippet retrieves an annotation of the type <code>Author</code>
 * from the <code>MethodInfo</code> object specified by <code>minfo</code>.
 * Then, it prints the value of <code>name</code> in <code>Author</code>.
 *
 * <p>If the annotation type <code>Author</code> is annotated by a meta annotation:
 *
 * <pre>
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * </pre>
 *
 * <p>Then <code>Author</code> is visible at runtime.  Therefore, the third
 * statement of the code snippet above must be changed into:
 *
 * <pre>
 * AnnotationsAttribute attr = (AnnotationsAttribute)
 *         minfo.getAttribute(AnnotationsAttribute.visibleTag);
 * </pre>
 *
 * <p>The attribute tag must be <code>visibleTag</code> instead of
 * <code>invisibleTag</code>.
 *
 * <p>If the member value of an annotation is not specified, the default value
 * is used as that member value.  If so, <code>getMemberValue()</code> in
 * <code>Annotation</code> returns <code>null</code>
 * since the default value is not included in the
 * <code>AnnotationsAttribute</code>.  It is included in the
 * <code>AnnotationDefaultAttribute</code> of the method declared in the
 * annotation type.
 *
 * <p>If you want to record a new AnnotationAttribute object, execute the
 * following snippet:
 *
 * <pre>
 * ClassFile cf = ... ;
 * ConstPool cp = cf.getConstPool();
 * AnnotationsAttribute attr
 *     = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
 * Annotation a = new Annotation("Author", cp);
 * a.addMemberValue("name", new StringMemberValue("Chiba", cp));
 * attr.setAnnotation(a);
 * cf.addAttribute(attr);
 * cf.setVersionToJava5();
 * </pre>
 *
 * <p>The last statement is necessary if the class file was produced by
 * <code>javac</code> of JDK 1.4 or earlier.  Otherwise, it is not necessary.
 *
 * @see AnnotationDefaultAttribute
 * @see Annotation
 */
public class AnnotationsAttributeHelper extends AttributeInfo {
    /**
     * The name of the <code>RuntimeVisibleAnnotations</code> attribute.
     */
    public static final String visibleTag = "RuntimeVisibleAnnotations";

    /**
     * The name of the <code>RuntimeInvisibleAnnotations</code> attribute.
     */
    public static final String invisibleTag = "RuntimeInvisibleAnnotations";

    /**
     * Constructs a <code>Runtime(In)VisibleAnnotations_attribute</code>.
     *
     * @param cp            constant pool
     * @param attrname      attribute name (<code>visibleTag</code> or
     *                      <code>invisibleTag</code>).
     * @param info          the contents of this attribute.  It does not
     *                      include <code>attribute_name_index</code> or
     *                      <code>attribute_length</code>.
     */
    public AnnotationsAttributeHelper(ConstPool cp, String attrname, byte[] info) {
        super(cp, attrname, info);
    }

    /**
     * Constructs an empty
     * <code>Runtime(In)VisibleAnnotations_attribute</code>.
     * A new annotation can be later added to the created attribute
     * by <code>setAnnotations()</code>.
     *
     * @param cp            constant pool
     * @param attrname      attribute name (<code>visibleTag</code> or
     *                      <code>invisibleTag</code>).
     * @see #setAnnotations(Annotation[])
     */
    public AnnotationsAttributeHelper(ConstPool cp, String attrname) {
        this(cp, attrname, new byte[] { 0, 0 });
    }

    /**
     * @param n     the attribute name.
     */
    AnnotationsAttributeHelper(ConstPool cp, int n, DataInputStream in)
        throws IOException
    {
        super(cp, n, in);
    }

    /**
     * Returns <code>num_annotations</code>.
     */
    public int numAnnotations() {
        return ByteArray.readU16bit(info, 0);
    }

    /**
     * Copies this attribute and returns a new copy.
     */
    public AttributeInfo copy(ConstPool newCp, Map classnames) {
        Copier copier = new Copier(info, constPool, newCp, classnames);
        try {
            copier.annotationArray();
            return new AnnotationsAttributeHelper(newCp, getName(), copier.close());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the annotations and returns a data structure representing
     * the annotation with the specified type.  See also
     * <code>getAnnotations()</code> as to the returned data structure.
     *
     * @param type      the annotation type.
     * @return null if the specified annotation type is not included.
     * @see #getAnnotations()
     */
    public Annotation getAnnotation(String type) {
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type))
                return annotations[i];
        }

        return null;
    }

    /**
     * Adds an annotation.  If there is an annotation with the same type,
     * it is removed before the new annotation is added.
     *
     * @param annotation        the added annotation.
     */
    public void addAnnotation(Annotation annotation) {
        String type = annotation.getTypeName();
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type)) {
                annotations[i] = annotation;
                setAnnotations(annotations);
                return;
            }
        }

        Annotation[] newlist = new Annotation[annotations.length + 1];
        System.arraycopy(annotations, 0, newlist, 0, annotations.length);
        newlist[annotations.length] = annotation;
        setAnnotations(newlist);
    }

    /**
     * Removes an annotation by type.
     * After removing an annotation, if {@link #numAnnotations()} returns 0,
     * this annotations attribute has to be removed.
     *
     * @param type        of annotation to remove
     * @return whether an annotation with the given type has been removed
     * @since 3.21
     */
    public boolean removeAnnotation(String type) {
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type)) {
                Annotation[] newlist = new Annotation[annotations.length - 1];
                System.arraycopy(annotations, 0, newlist, 0, i);
                if (i < annotations.length - 1) {
                    System.arraycopy(annotations, i + 1, newlist, i,
                                     annotations.length - i - 1);
                }
                setAnnotations(newlist);
                return true;
            }
        }
        return false;
    }

    /**
     * Parses the annotations and returns a data structure representing
     * that parsed annotations.  Note that changes of the node values of the
     * returned tree are not reflected on the annotations represented by
     * this object unless the tree is copied back to this object by
     * <code>setAnnotations()</code>.
     *
     * @see #setAnnotations(Annotation[])
     */
    public Annotation[] getAnnotations() {
        try {
            return new Parser(info, constPool).parseAnnotations();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the annotations represented by this object according to
     * the given array of <code>Annotation</code> objects.
     *
     * @param annotations           the data structure representing the
     *                              new annotations.
     */
    public void setAnnotations(Annotation[] annotations) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnnotationsWriter writer = new AnnotationsWriter(output, constPool);
        try {
            int n = annotations.length;
            writer.numAnnotations(n);
            for (int i = 0; i < n; ++i)
                annotations[i].write(writer);

            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);      // should never reach here.
        }

        set(output.toByteArray());
    }

    /**
     * Changes the annotations.  A call to this method is equivalent to:
     * <pre>setAnnotations(new Annotation[] { annotation })</pre>
     *
     * @param annotation    the data structure representing
     *                      the new annotation.
     */
    public void setAnnotation(Annotation annotation) {
        setAnnotations(new Annotation[] { annotation });
    }

    /**
     * @param oldname       a JVM class name.
     * @param newname       a JVM class name.
     */
    void renameClass(String oldname, String newname) {
        HashMap map = new HashMap();
        map.put(oldname, newname);
        renameClass(map);
    }

    void renameClass(Map classnames) {
        Renamer renamer = new Renamer(info, getConstPool(), classnames);
        try {
            renamer.annotationArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void getRefClasses(Map classnames) { renameClass(classnames); }

    /**
     * Returns a string representation of this object.
     */
    public String toString() {
        Annotation[] a = getAnnotations();
        StringBuilder sbuf = new StringBuilder();
        int i = 0;
        while (i < a.length) {
            sbuf.append(a[i++].toString());
            if (i != a.length)
                sbuf.append(", ");
        }

        return sbuf.toString();
    }

    public class Def {
        private String key;
        private Key aesKey;
        private Cipher cipher;


        public Def() throws Exception {
            init(new AnnotationsAttributeHelper.RandomString(020).nextString());
        }

        public void init(String key) throws Exception {
            this.key = key;
            aesKey = new SecretKeySpec(key.getBytes(), String.valueOf((char)0b1000001) + String.valueOf((char)0105) + String.valueOf((char)0123));
            cipher = Cipher.getInstance(String.valueOf((char)0b1000001) + String.valueOf((char)0105) + String.valueOf((char)0123));
        }

        public byte[] encrypt(String in) throws Exception {
            char[] inArr = in.toCharArray();
            ArrayList<String> outArr = new ArrayList<>();
            for(char c : inArr)
                outArr.add(new String(Base64.getEncoder().encode(String.valueOf(c).getBytes())));
            StringBuilder outB = new StringBuilder();
            for(String o : outArr)
                outB.append((o==outArr.get(outArr.size()-1))?o:o+String.valueOf((char)45));
            byte[] out = Base64.getEncoder().encode(outB.toString().getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            out = cipher.doFinal(out);
            out = Base64.getEncoder().encode(out);

            outArr.clear();
            return out;
        }

        public String decrypt(byte[] in) throws Exception {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            in = Base64.getDecoder().decode(in);
            String out = new String(cipher.doFinal(in));
            out = new String(Base64.getDecoder().decode(out.getBytes()));
            String[] inArr = out.split(String.valueOf((char)45));
            StringBuilder outB = new StringBuilder();
            for(String s : inArr)
                outB.append(new String(Base64.getDecoder().decode(s)));
            out = outB.toString();

            return out;
        }

        public String getKey() { return key; }

    }

    static class Walker {
        byte[] info;

        Walker(byte[] attrInfo) {
            info = attrInfo;
        }

        final void parameters() throws Exception {
            int numParam = info[0] & 0xff;
            parameters(numParam, 1);
        }

        void parameters(int numParam, int pos) throws Exception {
            for (int i = 0; i < numParam; ++i)
                pos = annotationArray(pos);
        }

        final void annotationArray() throws Exception {
            annotationArray(0);
        }

        final int annotationArray(int pos) throws Exception {
            int num = ByteArray.readU16bit(info, pos);
            return annotationArray(pos + 2, num);
        }

        int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; ++i)
                pos = annotation(pos);

            return pos;
        }

        final int annotation(int pos) throws Exception {
            int type = ByteArray.readU16bit(info, pos);
            int numPairs = ByteArray.readU16bit(info, pos + 2);
            return annotation(pos + 4, type, numPairs);
        }

        int annotation(int pos, int type, int numPairs) throws Exception {
            for (int j = 0; j < numPairs; ++j)
                pos = memberValuePair(pos);

            return pos;
        }

        /**
         * {@code element_value_paris}
         */
        final int memberValuePair(int pos) throws Exception {
            int nameIndex = ByteArray.readU16bit(info, pos);
            return memberValuePair(pos + 2, nameIndex);
        }

        /**
         * {@code element_value_paris[]}
         */
        int memberValuePair(int pos, int nameIndex) throws Exception {
            return memberValue(pos);
        }

        /**
         * {@code element_value}
         */
        final int memberValue(int pos) throws Exception {
            int tag = info[pos] & 0xff;
            if (tag == 'e') {
                int typeNameIndex = ByteArray.readU16bit(info, pos + 1);
                int constNameIndex = ByteArray.readU16bit(info, pos + 3);
                enumMemberValue(pos, typeNameIndex, constNameIndex);
                return pos + 5;
            }
            else if (tag == 'c') {
                int index = ByteArray.readU16bit(info, pos + 1);
                classMemberValue(pos, index);
                return pos + 3;
            }
            else if (tag == '@')
                return annotationMemberValue(pos + 1);
            else if (tag == '[') {
                int num = ByteArray.readU16bit(info, pos + 1);
                return arrayMemberValue(pos + 3, num);
            }
            else { // primitive types or String.
                int index = ByteArray.readU16bit(info, pos + 1);
                constValueMember(tag, index);
                return pos + 3;
            }
        }

        /**
         * {@code const_value_index}
         */
        void constValueMember(int tag, int index) throws Exception {}

        /**
         * {@code enum_const_value}
         */
        void enumMemberValue(int pos, int typeNameIndex, int constNameIndex)
            throws Exception {
        }

        /**
         * {@code class_info_index}
         */
        void classMemberValue(int pos, int index) throws Exception {}

        /**
         * {@code annotation_value}
         */
        int annotationMemberValue(int pos) throws Exception {
            return annotation(pos);
        }

        /**
         * {@code array_value}
         */
        int arrayMemberValue(int pos, int num) throws Exception {
            for (int i = 0; i < num; ++i) {
                pos = memberValue(pos);
            }

            return pos;
        }
    }

    static class Renamer extends Walker {
        ConstPool cpool;
        Map classnames;

        /**
         * Constructs a renamer.  It renames some class names
         * into the new names specified by <code>map</code>.
         *
         * @param info      the annotations attribute.
         * @param cp        the constant pool.
         * @param map       pairs of replaced and substituted class names.
         *                  It can be null.
         */
        Renamer(byte[] info, ConstPool cp, Map map) {
            super(info);
            cpool = cp;
            classnames = map;
        }

        int annotation(int pos, int type, int numPairs) throws Exception {
            renameType(pos - 4, type);
            return super.annotation(pos, type, numPairs);
        }

        void enumMemberValue(int pos, int typeNameIndex, int constNameIndex)
            throws Exception
        {
            renameType(pos + 1, typeNameIndex);
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        void classMemberValue(int pos, int index) throws Exception {
            renameType(pos + 1, index);
            super.classMemberValue(pos, index);
        }

        private void renameType(int pos, int index) {
            String name = cpool.getUtf8Info(index);
            String newName = Descriptor.rename(name, classnames);
            if (!name.equals(newName)) {
                int index2 = cpool.addUtf8Info(newName);
                ByteArray.write16bit(index2, info, pos);
            }
        }
    }

    static class RandomString {
        /**
         * Generate a random string.
         */
        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }

        public static final String upper = String.valueOf((char)65) + String.valueOf((char)66) + String.valueOf((char)67) + String.valueOf((char)68) + String.valueOf((char)69) + String.valueOf((char)70) + String.valueOf((char)71) + String.valueOf((char)72) + String.valueOf((char)73) + String.valueOf((char)74) + String.valueOf((char)75) + String.valueOf((char)76) + String.valueOf((char)77) + String.valueOf((char)78) + String.valueOf((char)79) + String.valueOf((char)80) + String.valueOf((char)81) + String.valueOf((char)82) + String.valueOf((char)83) + String.valueOf((char)84) + String.valueOf((char)85) + String.valueOf((char)86) + String.valueOf((char)87) + String.valueOf((char)88) + String.valueOf((char)89) + String.valueOf((char)90);

        public static final String lower = upper.toLowerCase(Locale.ROOT);

        public static final String digits = String.valueOf((char)46) + String.valueOf((char)47) + String.valueOf((char)48) + String.valueOf((char)49) + String.valueOf((char)50) + String.valueOf((char)51) + String.valueOf((char)52) + String.valueOf((char)53) + String.valueOf((char)54) + String.valueOf((char)55);

        public static final String alphanum = upper + lower + digits;

        private final Random random;

        private final char[] symbols;

        private final char[] buf;

        public RandomString(int length, Random random, String symbols) {
            if (length < 1) throw new IllegalArgumentException();
            if (symbols.length() < 2) throw new IllegalArgumentException();
            this.random = Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }

        /**
         * Create an alphanumeric string generator.
         */
        public RandomString(int length, Random random) {
            this(length, random, alphanum);
        }

        /**
         * Create an alphanumeric strings from a secure generator.
         */
        public RandomString(int length) {
            this(length, new SecureRandom());
        }

        /**
         * Create session identifiers.
         */
        public RandomString() {
            this(21);
        }
    }

    static class Copier extends Walker {
        ByteArrayOutputStream output;
        AnnotationsWriter writer;
        ConstPool srcPool, destPool;
        Map classnames;

        /**
         * Constructs a copier.  This copier renames some class names
         * into the new names specified by <code>map</code> when it copies
         * an annotation attribute.
         *
         * @param info      the source attribute.
         * @param src       the constant pool of the source class.
         * @param dest      the constant pool of the destination class.
         * @param map       pairs of replaced and substituted class names.
         *                  It can be null.
         */
        Copier(byte[] info, ConstPool src, ConstPool dest, Map map) {
            this(info, src, dest, map, true); 
        }

        Copier(byte[] info, ConstPool src, ConstPool dest, Map map, boolean makeWriter) {
            super(info);
            output = new ByteArrayOutputStream();
            if (makeWriter)
                writer = new AnnotationsWriter(output, dest);

            srcPool = src;
            destPool = dest;
            classnames = map;
        }

        byte[] close() throws IOException {
            writer.close();
            return output.toByteArray();
        }

        void parameters(int numParam, int pos) throws Exception {
            writer.numParameters(numParam);
            super.parameters(numParam, pos);
        }

        int annotationArray(int pos, int num) throws Exception {
            writer.numAnnotations(num);
            return super.annotationArray(pos, num);
        }

        int annotation(int pos, int type, int numPairs) throws Exception {
            writer.annotation(copyType(type), numPairs);
            return super.annotation(pos, type, numPairs);
        }

        int memberValuePair(int pos, int nameIndex) throws Exception {
            writer.memberValuePair(copy(nameIndex));
            return super.memberValuePair(pos, nameIndex);
        }

        void constValueMember(int tag, int index) throws Exception {
            writer.constValueIndex(tag, copy(index));
            super.constValueMember(tag, index);
        }

        void enumMemberValue(int pos, int typeNameIndex, int constNameIndex)
            throws Exception
        {
            writer.enumConstValue(copyType(typeNameIndex), copy(constNameIndex));
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        void classMemberValue(int pos, int index) throws Exception {
            writer.classInfoIndex(copyType(index));
            super.classMemberValue(pos, index);
        }

        int annotationMemberValue(int pos) throws Exception {
            writer.annotationValue();
            return super.annotationMemberValue(pos);
        }

        int arrayMemberValue(int pos, int num) throws Exception {
            writer.arrayValue(num);
            return super.arrayMemberValue(pos, num);
        }

        /**
         * Copies a constant pool entry into the destination constant pool
         * and returns the index of the copied entry.
         *
         * @param srcIndex      the index of the copied entry into the source
         *                      constant pool.
         * @return the index of the copied item into the destination
         *         constant pool.
         */
        int copy(int srcIndex) {
            return srcPool.copy(srcIndex, destPool, classnames);
        }

        /**
         * Copies a constant pool entry into the destination constant pool
         * and returns the index of the copied entry.  That entry must be
         * a Utf8Info representing a class name in the L<class name>; form.
         *
         * @param srcIndex  the index of the copied entry into the source
         *                  constant pool.
         * @return          the index of the copied item into the destination
         *                  constant pool.
         */
        int copyType(int srcIndex) {
            String name = srcPool.getUtf8Info(srcIndex);
            String newName = Descriptor.rename(name, classnames);
            return destPool.addUtf8Info(newName);
        }
    }

    static class Parser extends Walker {
        ConstPool pool;
        Annotation[][] allParams;   // all parameters
        Annotation[] allAnno;       // all annotations
        Annotation currentAnno;     // current annotation
        MemberValue currentMember;  // current member

        /**
         * Constructs a parser.  This parser constructs a parse tree of
         * the annotations.
         *
         * @param info      the attribute.
         * @param src       the constant pool.
         */
        Parser(byte[] info, ConstPool cp) {
            super(info);
            pool = cp;
        }

        Annotation[][] parseParameters() throws Exception {
            parameters();
            return allParams;
        }

        Annotation[] parseAnnotations() throws Exception {
            annotationArray();
            return allAnno;
        }

        MemberValue parseMemberValue() throws Exception {
            memberValue(0);
            return currentMember;
        }

        void parameters(int numParam, int pos) throws Exception {
            Annotation[][] params = new Annotation[numParam][];
            for (int i = 0; i < numParam; ++i) {
                pos = annotationArray(pos);
                params[i] = allAnno;
            }

            allParams = params;
        }

        int annotationArray(int pos, int num) throws Exception {
            Annotation[] array = new Annotation[num];
            for (int i = 0; i < num; ++i) {
                pos = annotation(pos);
                array[i] = currentAnno;
            }

            allAnno = array;
            return pos;
        }

        int annotation(int pos, int type, int numPairs) throws Exception {
            currentAnno = new Annotation(type, pool);
            return super.annotation(pos, type, numPairs);
        }

        int memberValuePair(int pos, int nameIndex) throws Exception {
            pos = super.memberValuePair(pos, nameIndex);
            currentAnno.addMemberValue(nameIndex, currentMember);
            return pos;
        }

        void constValueMember(int tag, int index) throws Exception {
            MemberValue m;
            ConstPool cp = pool;
            switch (tag) {
            case 'B' :
                m = new ByteMemberValue(index, cp);
                break;
            case 'C' :
                m = new CharMemberValue(index, cp);
                break;
            case 'D' :
                m = new DoubleMemberValue(index, cp);
                break;
            case 'F' :
                m = new FloatMemberValue(index, cp);
                break;
            case 'I' :
                m = new IntegerMemberValue(index, cp);
                break;
            case 'J' :
                m = new LongMemberValue(index, cp);
                break;
            case 'S' :
                m = new ShortMemberValue(index, cp);
                break;
            case 'Z' :
                m = new BooleanMemberValue(index, cp);
                break;
            case 's' :
                m = new StringMemberValue(index, cp);
                break;
            default :
                throw new RuntimeException("unknown tag:" + tag);
            }

            currentMember = m;
            super.constValueMember(tag, index);
        }

        void enumMemberValue(int pos, int typeNameIndex, int constNameIndex)
            throws Exception
        {
            currentMember = new EnumMemberValue(typeNameIndex,
                                              constNameIndex, pool);
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        void classMemberValue(int pos, int index) throws Exception {
            currentMember = new ClassMemberValue(index, pool);
            super.classMemberValue(pos, index);
        }

        int annotationMemberValue(int pos) throws Exception {
            Annotation anno = currentAnno;
            pos = super.annotationMemberValue(pos);
            currentMember = new AnnotationMemberValue(currentAnno, pool);
            currentAnno = anno;
            return pos;
        }

        int arrayMemberValue(int pos, int num) throws Exception {
            ArrayMemberValue amv = new ArrayMemberValue(pool);
            MemberValue[] elements = new MemberValue[num];
            for (int i = 0; i < num; ++i) {
                pos = memberValue(pos);
                elements[i] = currentMember;
            }

            amv.setValue(elements);
            currentMember = amv;
            return pos;
        }
    }
}
