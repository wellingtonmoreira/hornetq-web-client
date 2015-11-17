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

@WebServlet(value="/clearQueueServlet", name="clearQueueServlet")
public class ClearQueueServlet extends HttpServlet {

    @Inject
    HornetQClient client;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
        try {
            client.limparFila(builder.build());
            req.setAttribute("successMsg", "Queue cleared");
        } catch (Exception exc) {
            req.setAttribute("errorMsg", exc.getMessage());
        }
        req.getRequestDispatcher("/limpar.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }
}
