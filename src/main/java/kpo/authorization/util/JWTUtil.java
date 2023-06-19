package kpo.authorization.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kpo.authorization.domain.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

public class JWTUtil {
    @Value("${jwt.secret}")
    private static String secret;

    public static String generateJWT(User user) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 7);
        return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(date).setExpiration(c.getTime())
                .claim("role", user.getRole()).signWith(
                SignatureAlgorithm.HS256,
                "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes(StandardCharsets.UTF_8)
        ).compact();
    }
}
