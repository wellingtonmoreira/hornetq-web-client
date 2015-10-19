package com.sciensa;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

@WebServlet(value="/", name="payLoadServlet")
public class PayloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Object successMsg = req.getAttribute("successMsg");
        Object errorMsg = req.getAttribute("errorMsg");

        res.getWriter().println("<html>");
        res.getWriter().println("   <head>");
        res.getWriter().println("       <meta charset=\"UTF-8\">");
        res.getWriter().println("   </head>");
        res.getWriter().println("   <body style=\"text-align:center;\">");
        res.getWriter().println("       <form method=\"POST\" action=\"\">");
        res.getWriter().println("           <h3>Client web HornetQ</h3></br>");
        res.getWriter().println("           <span>URL HornetQ: </span><input type=\"text\" name=\"url\"/></br>");
        res.getWriter().println("           <span>Nome Fila:</span><input type=\"text\" name=\"queue\"></br>");
        res.getWriter().println("           <span>Payload:</span><textarea name=\"payload\"></textarea></br>");
        res.getWriter().println(MessageFormat.format("<span style=\"color:green\">{0}</span>", successMsg == null ? "" : successMsg));
        res.getWriter().println(MessageFormat.format("<span style=\"color:red\">{0}</span></br>", errorMsg == null ? "" : errorMsg));
        res.getWriter().println("           <input type=\"submit\" value=\"Enviar\"></input>");
        res.getWriter().println("       </form>");
        res.getWriter().println("   </body>");
        res.getWriter().println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PayloadRequest.Builder builder = PayloadRequest.builder();
        String[] url = (req.getParameter("url").replace("http://", "").replace("https://", "")).split(":");
        if (url.length == 2) {
            builder.setUrl(url[0].replace(":".concat(url[1]), ""));
            builder.setPort(Integer.valueOf(url[1]));
        } else {
            builder.setUrl(url[0]);
            builder.setPort(80);
        }
        builder.setQueue(req.getParameter("queue"));
        builder.setPayload(req.getParameter("payload"));
        try {
            HornetQClient.INSTANCE.cadastrarPayload(builder.build());
            req.setAttribute("successMsg", "Payload adicionado com sucesso");
        } catch (Exception exc) {
            req.setAttribute("errorMsg", exc.getMessage());
        }
        doGet(req, res);
    }
}
