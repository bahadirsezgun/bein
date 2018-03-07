package com.beinplanner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.beinplanner.security.handler.RESTAuthenticationEntryPoint;
import com.beinplanner.security.handler.RESTAuthenticationFailureHandler;
import com.beinplanner.security.service.UserSecurityService;

import tr.com.beinplanner.login.session.LoginSession;



@EntityScan(basePackages={"tr.com.beinplanner"})
@EnableJpaRepositories("tr.com.beinplanner")
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	    @Autowired
	    UserSecurityService userSecurityService;

	    @Autowired
		LoginSession loginSession;
		
		

	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    	auth.userDetailsService(userSecurityService)
	        .passwordEncoder(getPasswordEncoder());
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {

	    	  // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        http.csrf().disable();
	        
	        
	        http
            .authorizeRequests()
                .antMatchers("/homerlib/**","/login.html", "/app/**", "/jslib/**","/index.html","/firmRegister","/register/*","**/marketBein.json","/register","/lock","/lock.html","**/*.js","**/*.css").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/error.html")
                .and()
                .formLogin()
                        .loginPage("/login").failureUrl("/lock.html")
                        .loginProcessingUrl("/login").permitAll().defaultSuccessUrl("/beincloud",true)
                        //.failureHandler(new RESTAuthenticationFailureHandler())
                        .failureForwardUrl("/lock")
                .and()                
                .logout().logoutSuccessUrl("/lock.html")
                .permitAll();
	        
	      
	                
	    }
	    
	    


	    private PasswordEncoder getPasswordEncoder() {
	        return new PasswordEncoder() {
	            @Override
	            public String encode(CharSequence charSequence) {
	                return charSequence.toString();
	            }

	            @Override
	            public boolean matches(CharSequence charSequence, String s) {
	                
	            	   String p=charSequence.toString();
	            	
	            	   if(p.equals(s))	            	
	            	       return true;
	            	   else {
	            		   loginSession=null;
	            		   return false;
	            	   }
	            		   
	            }
	        };
	    }
	
	
}
