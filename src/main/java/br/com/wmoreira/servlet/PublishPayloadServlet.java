package br.com.wmoreira.servlet;

import br.com.wmoreira.jms.HornetQClient;
import br.com.wmoreira.jms.PayloadRequest;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value="/", name="publishPayLoadServlet")
public class PublishPayloadServlet extends HttpServlet {

    @Inject
    HornetQClient client;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
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
            client.cadastrarPayload(builder.build());
            req.setAttribute("successMsg", "Payload successfully published");
        } catch (Exception exc) {
            req.setAttribute("errorMsg", exc.getMessage());
        }
        req.getRequestDispatcher("/index.jsp").forward(req, res);
    }
}
