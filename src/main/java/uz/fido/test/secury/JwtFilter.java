package uz.fido.test.secury;
// TOKEN
// eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrdWwiLCJpYXQiOjE2NTI1MjkxNzAsImV4cCI6MTY1MjU2NTE3MH0.A8FASvy2YqXHOhgsSAw0-RvFgKrMb-hWDl61IcLOZ5IHhlLxch_4qUHHfnh3R6jAkLFHgGdo4826grQYr3GLlA

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.fido.test.service.MyAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtFilter: har bir request controllirga borishdan oldin <br>
 * JwtFilterga kelsin HEADERIDAN TOKENNI OLIB TEKSHIRAMIZ  <br>
 *  VA VALIDETIONDAN OTSA SYSYIMAGA KIRSIN DEYMIZ
 */
 // har bitta requestni filter qilishimiz uchun OncePreRequestFilter dan extance olish kk
@Component // Component bu programmadan asoiy rol oynashi yani bean qilib oldik bean qildikkkk.
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MyAuthService myAuthService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // REQUESDAN TOKENNI OLISH
        String token = request.getHeader("Authorization"); // SESSIONGA USHLAB OLMASLIGINI TEKSHIRAMIZ ALT+F8 BN

        // TOKEN BORLIGINI VA TOKENNING BOSHLANISH BEARER BOLISHINI TEKSHIRADI
        if (token !=null && token.startsWith("Bearer")){

            // AYNANA TOKENNI OZINI OLDIK
            token = token.substring(7);

            // TOKENNI VALEDATSIYADAN OTQAZDIK (TOKEN BUZULMAGANLIGINI, MUDDAT OTMAGANLIGINI NA H.K)
            boolean validateToken = jwtProvider.validateToken(token);
            if(validateToken){

                    /**
                     *  Endi userni systemaga kiritishimiz kk. <br>
                     *  Biz hozir tokinni tekshirdik. <br>
                     *  Endi tokendan userni kimligini bilishimiz kk<br>
                     */


                // TOKEN ICHIDAN USERNAME OLDIK
                String username = jwtProvider.getUserNameFromToken(token);

                // USER_DETAILS ORQALI AUTHENTICATION YARATIB OLAMIZ
                UserDetails userDetails = myAuthService.loadUserByUsername(username);

                /**
                 * userDetails nimaga kerak bizga kerak boladihan asosiy narsasi <br>
                 * shu token bilan kelgan user kirdi deyishimiz kk <br>
                 * Authentication tipiddagi object yaratib beradi
                 */
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                /**
                 * 3 tanlikdan foydalansak systimaga kirdi derkan <br>
                 * 2 talikdan foydalansak parol loginlarni solishtirarkan
                 */


                System.out.println(SecurityContextHolder.getContext().getAuthentication());


                /**
                 * bu sistemaga kirdi deb elon qiladi kimligimizni aytadi secutityga
                 */
                // SISTEMAGA KIM KIRGANLIGINI ORNATDIK
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


                System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            }
        }


        // BOSHQA FILTELA ISHLATADI OZINI FILTIRINI QIL OZIMIZNIKINI ISHLATIB BOLDIK DEDIK
        filterChain.doFilter(request,response);
    }
}
