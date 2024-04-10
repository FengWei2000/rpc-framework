package github.fw.registry;

import github.fw.extension.SPI;
import github.fw.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * service discovery    服务发现
 *
 * @author shuang.kou
 * @createTime 2020年06月01日 15:16:00
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName 获取远程服务地址
     *
     * @param rpcRequest rpc service pojo   完整的服务名称（class name+group+version）
     * @return service address              远程服务地址
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
