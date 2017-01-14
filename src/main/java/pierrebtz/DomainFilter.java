package pierrebtz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DomainFilter extends OncePerRequestFilter {
    @Value("${frontend.domain.name}")
    private String frontEndDomain;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String domain = request.getRemoteHost();

        if(isValidDomain(domain)){
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, domain + " is not authorized to access this resource");
        }
    }

    private boolean isValidDomain(String domain){
        return frontEndDomain.equals(domain);
    }

}