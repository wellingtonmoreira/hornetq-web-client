package br.com.wmoreira.servlet;

import br.com.wmoreira.helper.ZipHelper;
import br.com.wmoreira.jms.HornetQClient;
import br.com.wmoreira.jms.HornetQHelper;
import br.com.wmoreira.jms.PayloadRequest;

import javax.inject.Inject;
import javax.jms.Message;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.MessageFormat;

@WebServlet(value="/stressTestServlet", name="stressTestServlet")
@MultipartConfig
public class StressTestServlet extends HttpServlet {

    @Inject
    private ZipHelper zipHelper;

    @Inject
    private HornetQClient client;

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

        Part part = req.getPart("payload");
        if (part != null) {

            try (HornetQHelper helper = client.buildHelper(builder.build())) {
                int amountProcessed = zipHelper.processFilesContent(part.getInputStream(),
                        (s) -> {
                            Message message = helper.getSession().createTextMessage(s);
                            helper.getSender().send(message);
                        });
                req.setAttribute("successMsg", MessageFormat.format("{0} payloads successfully published", amountProcessed));
            } catch (Exception exc) {
                req.setAttribute("errorMsg", exc.getMessage());
            }
            req.getRequestDispatcher("/stress.jsp").forward(req, res);
            return;
        }
        req.setAttribute("errorMsg", "Y U NO upload a file?");
    }
}
