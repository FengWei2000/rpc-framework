package github.fw;

import github.fw.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @author smile2coder
 */
@Component
public class HelloController {

    // 会在bean初始化之后由postProcessAfterInitialization赋值为一个RpcClientProxy.getProxy()代理对象
    // 实现方式为检查是否被RpcReference注解，并通过反射赋值
    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        Thread.sleep(12000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222*" + i)));
        }
    }
}
