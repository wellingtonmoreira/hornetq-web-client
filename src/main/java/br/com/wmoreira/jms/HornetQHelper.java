package br.com.wmoreira.jms;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;

import javax.jms.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;

public class HornetQHelper implements Closeable {
    private final HornetQConnectionFactory cf;
    private final QueueConnection queueConn;
    private final QueueSession queueSession;
    private final Queue q;
    private QueueReceiver receiver;
    private QueueSender sender;

    private HornetQHelper(PayloadRequest payloadReq) throws JMSException {
        cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF,
                buildTransportConfiguration(payloadReq.getUrl(), payloadReq.getPort()));
        queueConn = cf.createQueueConnection();
        queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        q = queueSession.createQueue(payloadReq.getQueue());

        queueConn.start();

    }

    public QueueSender getSender() throws JMSException {
        if (sender == null) {
            sender = queueSession.createSender(q);
        }
        return sender;
    }

    public QueueReceiver getReceiver() throws JMSException {
        if (receiver == null) {
            receiver = queueSession.createReceiver(q);
        }
        return receiver;
    }

    public QueueSession getSession() {
        return queueSession;
    }

    private TransportConfiguration buildTransportConfiguration(String host, Integer port) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("host", host);
        params.put("port", port);
        return new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
    }

    @Override
    public void close() throws IOException {
        try {
            if (receiver != null) {
                receiver.close();
            }

            if (sender != null) {
                sender.close();
            }

            queueSession.close();
            queueConn.close();
            cf.close();
        } catch (JMSException e) {
        }
    }

    public static class Builder {
        private final PayloadRequest payloadReq;

        private Builder(PayloadRequest payloadReq) {
            this.payloadReq = payloadReq;
        }

        public static Builder builder(PayloadRequest payload) {
            return new Builder(payload);
        }

        public HornetQHelper build() throws JMSException {
            return new HornetQHelper(payloadReq);
        }
    }
}