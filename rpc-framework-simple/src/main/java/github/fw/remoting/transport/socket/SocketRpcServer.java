package github.fw.remoting.transport.socket;

import github.fw.config.CustomShutdownHook;
import github.fw.config.RpcServiceConfig;
import github.fw.factory.SingletonFactory;
import github.fw.provider.ServiceProvider;
import github.fw.provider.impl.ZkServiceProviderImpl;
import github.fw.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static github.fw.remoting.transport.netty.server.NettyRpcServer.PORT;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 08:01:00
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;


    public SocketRpcServer() {
        // 根据配置创建线程池
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        // 服务提供者
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    // 注册服务
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();  // 单例对象，清空注册中心
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                // 监听到一个连接就将其放入线程池中
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }

}
