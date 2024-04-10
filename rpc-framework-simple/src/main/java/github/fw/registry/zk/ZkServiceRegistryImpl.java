package github.fw.registry.zk;

import github.fw.registry.ServiceRegistry;
import github.fw.registry.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * service registration  based on zookeeper
 *
 * @author shuang.kou
 * @createTime 2020年05月31日 10:56:00
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        // 服务的节点路径
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        // 创建ZooKeeper客户端
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        // 创建服务的持久化节点
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
