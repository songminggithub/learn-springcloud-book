package pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackfactory.client;

 import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pri.holysu.springcloud.microserviceconsumermoviefeignhystrixfallbackfactory.entity.User;

 import java.util.ArrayList;
 import java.util.List;

/**
 *  通过 fallbackFactory  声明回退策略
 * */
@FeignClient(name = "microservice-provider-user", fallbackFactory = FeignClientFallbackFactory.class)
public interface FeignUserClient {


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User findById(@PathVariable("id") Long id);

    @GetMapping("/user/all")
    List<User> findAll();

}

@Component
class FeignClientFallbackFactory implements FallbackFactory<FeignUserClient>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

    @Override
    public FeignUserClient create(Throwable cause) {
        return new FeignUserClient(){

            @Override
            public User findById(Long id) {
                // 记录日志，以帮助分析问题
                FeignClientFallbackFactory.LOGGER.info("fallback; reason was: ", cause);
                User user = new User();
                user.setId(-1L);
                user.setUsername("default user");
                return user;
            }

            @Override
            public List<User> findAll() {
                FeignClientFallbackFactory.LOGGER.info("fallback; reason was: ", cause);
                List<User> userList = new ArrayList<>();
                User user = new User();
                user.setId(-1L);
                user.setUsername("default user");
                userList.add(user);
                return userList;
            }
        };
    }
}
