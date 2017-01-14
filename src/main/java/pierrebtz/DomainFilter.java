package pierrebtz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Profile("!dev")
@Component
public class DomainFilter extends OncePerRequestFilter {
    @Value("${frontend.origin}")
    private String frontendOrigin;
    @Value("${frontend.referrer}")
    private String frontendReferrer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isValidDomain(request.getHeader("origin"), request.getHeader("referer"))){
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized to access this resource");
        }
    }

    // This can easily be spoofed, but idea here is not to make something secure (no authentication).
    private boolean isValidDomain(String origin, String referrer){
        return frontendOrigin.equals(origin) && frontendReferrer.equals(referrer);
    }

}