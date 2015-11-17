package br.com.wmoreira.jms;

import javax.enterprise.context.RequestScoped;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@RequestScoped
public class HornetQClient {
    public Integer cadastrarPayload(PayloadRequest payloadReq) {
        try {
            try (HornetQHelper helper = HornetQHelper.Builder.builder(payloadReq).build()) {
                for (String payload : payloadReq.getPayload()) {
                    Message message = helper.getSession().createTextMessage(payload);
                    helper.getSender().send(message);
                }
            }
            return payloadReq.getPayload() == null ? 0 : payloadReq.getPayload().length;
        } catch (Exception e) {
            throw new RuntimeException("Error while publishing payload");
        }
    }

    public String consumirPayload(PayloadRequest payloadReq) {
        try {

            try (HornetQHelper helper = HornetQHelper.Builder.builder(payloadReq).build()) {
                Message message = helper.getReceiver().receive(2000);

                if (message == null) {
                    return null;
                }

                message.acknowledge();

                return ((TextMessage) message).getText();
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro while receiving payload");
        }
    }

    public void limparFila(PayloadRequest payloadReq) {
        try {
            try (HornetQHelper helper = HornetQHelper.Builder.builder(payloadReq).build()) {
                Message message;
                do {
                    message= helper.getReceiver().receive(2000);
                    if (message != null) {
                        message.acknowledge();
                    }
                } while (message != null);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro while receiving payload");
        }
    }

    public HornetQHelper buildHelper(PayloadRequest payloadReq) throws JMSException {
        return HornetQHelper.Builder.builder(payloadReq).build();
    }
}
