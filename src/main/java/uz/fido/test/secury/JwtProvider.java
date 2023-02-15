package uz.fido.test.secury;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // bu bean qilib beradi
public class JwtProvider {

    static final String secret = "letNoOneKnowTheSecretWork1011";
    static final long expireTime = 36_000_000;
    /**
     *
     * @param username username orqali token yasaymiz. <br> Username cleant tomonidan keladi cleantni username
     * @return tokenni qaytaramiz parol login togri bolsa
     */
    static public String generateToken(String username){
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String compact = Jwts
                .builder() // tokenni qurish
                .setSubject(username) //takrorlanmas narsasi userni
                .setIssuedAt(new Date()) // tokenni berilgan vaqti
                .setExpiration(expireDate) //qacha vaqt bermoqchisiz
                .signWith(SignatureAlgorithm.HS512, secret) // nima bn shifirlashni aytamiz
                .compact(); // String qaytaradi
        return compact;
    }




    /**
     *
     * @param token KELADI JwtFilterdan yuboramiz
     * @return TOKENNI VALEDATSIYADAN OTQAZDIK (TOKEN BUZULMAGANLIGINI, MUDDAT OTMAGANLIGINI NA H.K) true or folse qaytadi
     */
     public boolean validateToken(String token){
        try{
            Jwts
                    .parser() // san manga keyni ber mahfiy sozni ber
                    .setSigningKey(secret) //
                    .parseClaimsJws(token); //tokenni pars qil
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
         return false;
     }




    /**
     *
     * @param token keladi JwtFilterdan yuboramiz
     * @return Token ichidan username olamiz va username qaytadi
     */

    public String getUserNameFromToken(String token){
        String username = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }
}
