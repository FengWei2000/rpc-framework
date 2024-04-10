package github.fw.remoting.transport.socket;

import github.fw.factory.SingletonFactory;
import github.fw.remoting.dto.RpcRequest;
import github.fw.remoting.dto.RpcResponse;
import github.fw.remoting.handler.RpcRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 09:18:00
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {
    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;


    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        // 单例模式，创建一个RpcRequestHandler
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        // 一个线程执行服务
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            // 读取RPC请求
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 执行远程过程调用
            Object result = rpcRequestHandler.handle(rpcRequest);
            // 将结果返回给连接的客户端
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        } finally {

        }
    }

}
