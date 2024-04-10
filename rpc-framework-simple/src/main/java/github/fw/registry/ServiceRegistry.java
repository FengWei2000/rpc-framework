package github.fw.registry;

import github.fw.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration 服务注册
 *
 * @author shuang.kou
 * @createTime 2020年05月13日 08:39:00
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service 注册服务到注册中心
     *
     * @param rpcServiceName    rpc service name    完整的服务名称（class name+group+version）
     * @param inetSocketAddress service address     远程服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
