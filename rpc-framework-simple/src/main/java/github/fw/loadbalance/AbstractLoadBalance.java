package github.fw.loadbalance;

import github.fw.remoting.dto.RpcRequest;
import github.fw.utils.CollectionUtil;

import java.util.List;

/**
 * Abstract class for a load balancing policy
 *
 * @author shuang.kou
 * @createTime 2020年06月21日 07:44:00
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceAddresses)) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        // 需要负载均衡时，调用doSelect()方法
        return doSelect(serviceAddresses, rpcRequest);
    }

    // 具体采用哪种实现依据resources/META-INF.extensions中的配置文件决定
    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);

}
