package com.bai.zhou.week1;

import java.io.*;
import java.math.BigInteger;

public class HelloClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        File f = new File(name);
        if (f.exists()) {
            try {
                byte[] bytes = toByteArray(f);
                byte[] actualBytes = bytesToHex(bytes);
                return defineClass("Hello", actualBytes, 0, actualBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    public static byte[] toByteArray(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            in.close();
            bos.close();
        }
    }

    public static byte[] bytesToHex(byte[] bytes) {
        byte[] actBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            BigInteger originInt = new BigInteger(hex, 16);
            int convertInt = 255 - originInt.intValue();
            actBytes[i] = (byte) convertInt;
        }
        return actBytes;
    }

    public static void main(String[] args) throws Exception {
        HelloClassLoader helloClassLoader = new HelloClassLoader();
        Class<?> clazz = helloClassLoader.findClass("/Users/zhoubai/Downloads/Hello.xlass");
        Object object = clazz.newInstance();
        clazz.getMethod("hello").invoke(object);
    }
}
