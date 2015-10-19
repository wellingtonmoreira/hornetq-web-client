package com.sciensa;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;

/**
 * Created by sciensa on 19/10/15.
 */
public enum HornetQClient {
    INSTANCE;

    public void cadastrarPayload(PayloadRequest payload) {
        try {

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("host", payload.getUrl());
            params.put("port", payload.getPort());
            TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
            HornetQConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);
            QueueConnection queueConn = cf.createQueueConnection();
            QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue q = queueSession.createQueue(payload.getQueue());
            QueueSender sender = queueSession.createSender(q);

            try {
                Message message = queueSession.createTextMessage(payload.getPayload());
                sender.send(message);
                queueConn.start();
            } finally {
                sender.close();
                queueSession.close();
                queueConn.close();
                cf.close();
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar payload");
        }
    }
}
