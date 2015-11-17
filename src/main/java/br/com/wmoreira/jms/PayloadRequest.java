package br.com.wmoreira.jms;

import java.util.Arrays;
import java.util.List;

public class PayloadRequest {

    private final String url;
    private final Integer port;
    private final String queue;
    private final String[] payload;

    private PayloadRequest(String url, Integer port, String queue, String ... payload) {
        this.url = url;
        this.port = port;
        this.queue = queue;
        this.payload = payload;
    }

    public static PayloadRequest.Builder builder() {
        return new PayloadRequest.Builder();
    }

    public static PayloadRequest.Builder builder(String url, Integer port, String queue, String[] payload) {
        return new PayloadRequest.Builder(url, port, queue, payload);
    }

    public String getUrl() {
        return url;
    }

    public Integer getPort() {
        return port;
    }

    public String getQueue() {
        return queue;
    }

    public String[] getPayload() {
        return payload;
    }

    public static class Builder {

        private String url;
        private Integer port;
        private String queue;
        private String[] payload;

        private Builder() {

        }

        private Builder(String url, Integer port, String queue, String[] payload) {
            this.url = url;
            this.port = port;
            this.queue = queue;
            this.payload = payload;
        }

        public PayloadRequest build() {
            return new PayloadRequest(url, port, queue, payload);
        }

        public String getUrl() {
            return url;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;

        }

        public String getQueue() {
            return queue;
        }

        public Builder setQueue(String queue) {
            this.queue = queue;
            return this;
        }

        public String[] getPayload() {
            return payload;
        }

        public Builder setPayload(String ... payloads) {
            List<String> cleanPayloads = Arrays.asList(payloads);
            for (int i = 0; i < cleanPayloads.size(); i++) {
                cleanPayloads.set(i,
                                  cleanPayloads.get(i).replaceAll("\n", "").replaceAll("\r", ""));
            }
            this.payload = cleanPayloads.toArray(new String[0]);
            return this;
        }
    }

}
