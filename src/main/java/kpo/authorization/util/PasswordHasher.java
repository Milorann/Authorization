package kpo.authorization.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {
    public static String hashPassword(final String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
        return argon2.hash(2, 15 * 1024, 1, password.toCharArray());
    }
    public static boolean verify(final String hash, final String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
        return argon2.verify(hash, password.toCharArray());
    }
}
