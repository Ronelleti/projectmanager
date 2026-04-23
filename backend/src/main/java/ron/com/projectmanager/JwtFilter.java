package ron.com.projectmanager;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class JwtFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                JwtUtil.validateToken(token);
            } catch (Exception e) {
                ((HttpServletResponse) response).sendError(401, "Invalid token");
                return;
            }
        } else if (!req.getRequestURI().contains("/auth/login")) {
            ((HttpServletResponse) response).sendError(401, "Missing token");
            return;
        }

        chain.doFilter(request, response);
    }
}