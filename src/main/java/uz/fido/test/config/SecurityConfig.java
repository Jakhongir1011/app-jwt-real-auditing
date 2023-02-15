package uz.fido.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.fido.test.secury.JwtFilter;
import uz.fido.test.service.MyAuthService;

/**
 *@EnableWebSecurity bu bizni classimizni security ekanligini aytadi va shuni ichida sozlamalar yozamiz <br>
 * @Configuration bu bean qilib berai va class ichida bean yaratsa boladi
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter bu ctrl+o ishlatishimizga va methodlarni ovrride qilishimizga kk

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    JwtFilter jwtFilter;

    /**
     * Bu userlarni sistemaga kiritish uchun(ma'lumot myAuthService dan keladi)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(myAuthService); // user bn ishlovchi class
    }



    /**
     * Yollarga ruhsat berish
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * GET DAN BOSHQASIGA HAM RUHSAT BER DEGANI
                 */
                .csrf().disable()// post or put xatolik beradi ...
                .authorizeRequests()
                .antMatchers("/","/api/auth/login").permitAll()
                .anyRequest().authenticated();

                 /**
                  * Biz aytyapmizki httpga JwtFilter ishlasin <br>
                  * (hali parol loginlani solishtirmasda sistemaga kirmasdan) <br>
                  * UsernamePasswordAuthenticationFilter.class dan oldin dedik
                  */
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        /**
         * SPRING SECURITYGA SESSIONGA USHLAB OLMASLIGINI BUYIRYAPMIZ
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    /**
     *Passwordga EnCoder bolishi kk
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}








