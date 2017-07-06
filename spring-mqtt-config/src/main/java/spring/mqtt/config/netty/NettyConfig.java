package spring.mqtt.config.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//加上注释@Component，可以直接其他地方使用@Autowired来创建其实例对象
@Component
@ConfigurationProperties(prefix = "mqtt")
public class NettyConfig {
    private int borkerId;
    private String host;
    private int port;
    private boolean sslEnable;
    private int sslPort;
    private String sslCertPath;
    private String sslkeyPath;
    private String sslKeyPassword;
    private int keepAlive;
    private int keepAliveMax;

    public int getBorkerId() {
        return borkerId;
    }

    public void setBorkerId(int borkerId) {
        this.borkerId = borkerId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSslEnable() {
        return sslEnable;
    }

    public void setSslEnable(boolean sslEnable) {
        this.sslEnable = sslEnable;
    }

    public int getSslPort() {
        return sslPort;
    }

    public void setSslPort(int sslPort) {
        this.sslPort = sslPort;
    }

    public String getSslCertPath() {
        return sslCertPath;
    }

    public void setSslCertPath(String sslCertPath) {
        this.sslCertPath = sslCertPath;
    }

    public String getSslkeyPath() {
        return sslkeyPath;
    }

    public void setSslkeyPath(String sslkeyPath) {
        this.sslkeyPath = sslkeyPath;
    }

    public String getSslKeyPassword() {
        return sslKeyPassword;
    }

    public void setSslKeyPassword(String sslKeyPassword) {
        this.sslKeyPassword = sslKeyPassword;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getKeepAliveMax() {
        return keepAliveMax;
    }

    public void setKeepAliveMax(int keepAliveMax) {
        this.keepAliveMax = keepAliveMax;
    }
}
