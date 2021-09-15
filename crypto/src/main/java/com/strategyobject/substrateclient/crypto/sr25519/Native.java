package com.strategyobject.substrateclient.crypto.sr25519;

import com.google.common.base.Strings;
import com.strategyobject.substrateclient.crypto.NativeException;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.nio.file.Files;

class Native {
    public static final int SIGNATURE_LENGTH = 64;
    public static final int SECRET_KEY_KEY_LENGTH = 32;
    public static final int SECRET_KEY_NONCE_LENGTH = 32;
    public static final int SECRET_KEY_LENGTH = SECRET_KEY_KEY_LENGTH + SECRET_KEY_NONCE_LENGTH;
    public static final int PUBLIC_KEY_LENGTH = 32;
    public static final int KEYPAIR_LENGTH = SECRET_KEY_LENGTH + PUBLIC_KEY_LENGTH;
    public static final int CHAIN_CODE_LENGTH = 32;

    private static final String LIB_PATH = "SUBSTRATE_CLIENT_CRYPTO_LIB_PATH";
    private static final String LIB_NAME = "jni_crypto";
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS_NAME.contains("win");
    private static final boolean IS_MAC = OS_NAME.contains("mac");
    private static final boolean IS_LINUX = OS_NAME.contains("linux");

    static {
        try {
            var libPath = System.getenv(LIB_PATH);
            libPath = Strings.isNullOrEmpty(libPath)
                    ? copyLibraryFromResourcesToTempDir()
                    : libPath;

            System.load(libPath);
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    private static String copyLibraryFromResourcesToTempDir() throws IOException {
        String osDir = null;
        if (IS_WINDOWS) {
            osDir = "windows";
        } else if (IS_LINUX) {
            osDir = "linux";
        } else if (IS_MAC) { // TODO 'MasOS support problem'
            osDir = "macos";
        } else {
            throw new RuntimeException("JNI library can't be loaded because OS wasn't detected as supported.");
        }

        val fileName = System.mapLibraryName(LIB_NAME);
        val sourcePath = "/include/" + osDir + "/" + fileName;

        try (val sourceStream = Native.class.getResourceAsStream(sourcePath)) {
            if (sourceStream == null) {
                throw new RuntimeException("Library wasn't found.");
            }

            val tempDir = Files.createTempDirectory(LIB_NAME + "_lib");
            tempDir.toFile().deleteOnExit();
            val libPath = tempDir.resolve(fileName);
            libPath.toFile().deleteOnExit(); // It doesn't work recursively

            Files.copy(sourceStream, libPath);

            return libPath.toAbsolutePath().toString();
        }
    }

    static native byte[] deriveKeyPairHard(byte[] keyPair, byte[] chainCode) throws NativeException;

    static native byte[] deriveKeyPairSoft(byte[] keyPair, byte[] chainCode) throws NativeException;

    static native byte[] derivePublicSoft(byte[] publicKey, byte[] chainCode) throws NativeException;

    static native byte[] fromSeed(byte[] seed) throws NativeException;

    static native byte[] fromPair(byte[] keyPair) throws NativeException;

    static native byte[] sign(byte[] publicKey, byte[] secretKey, byte[] message) throws NativeException;

    static native boolean verify(byte[] signature, byte[] message, byte[] publicKey) throws NativeException;

    static native byte[] agree(byte[] publicKey, byte[] secretKey) throws NativeException;
}
