package models;

import java.io.File;

public class Settings {
    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = System.getProperty("user.dir")+"/key/private.key";

    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = System.getProperty("user.dir")+"/key/public.key";

}
