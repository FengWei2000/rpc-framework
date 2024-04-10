package github.fw.provider.impl;

import github.fw.config.RpcServiceConfig;
import github.fw.enums.RpcErrorMessageEnum;
import github.fw.enums.ServiceRegistryEnum;
import github.fw.exception.RpcException;
import github.fw.extension.ExtensionLoader;
import github.fw.provider.ServiceProvider;
import github.fw.registry.ServiceRegistry;
import github.fw.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shuang.kou
 * @createTime 2020年05月13日 11:23:00
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        // 存储 serviceName-serviceImpl实现类对象 键值对
        serviceMap = new ConcurrentHashMap<>();
        // 包含已经add的名字
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension(ServiceRegistryEnum.ZK.getName());
    }

    /**
     * 将服务注册到serviceMap和registeredService
     * @param rpcServiceConfig rpc service related attributes
     */
    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    /**
     * 根据名称获得服务，直接从serviceMap中取出
     * @param rpcServiceName rpc service name
     * @return
     */
    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    /**
     * 发布服务
     * @param rpcServiceConfig rpc service related attributes
     */
    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            // 获取本机ip
            String host = InetAddress.getLocalHost().getHostAddress();
            // 加入Map和Set中
            this.addService(rpcServiceConfig);
            // 在ZooKeeper中创建对应节点
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
