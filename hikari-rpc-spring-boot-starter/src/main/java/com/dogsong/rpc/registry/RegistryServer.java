package com.dogsong.rpc.registry;

import com.dogsong.rpc.common.Constants;
import com.dogsong.rpc.exception.ZkConnectException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册服务
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryServer {

    private final Logger logger = LoggerFactory.getLogger(RegistryServer.class);

    /** zk的地址 */
    private String addr;

    /** 超时时间 */
    private int timeout;

    /** 服务名 */
    private String serverName;

    /** 主机 */
    private String host;

    /** netty 端口 */
    private int port;

    /**
     * 注册 zookeeper
     *
     */
    public void register() throws ZkConnectException {
        try {
            // 获取 zookeep 连接
            ZooKeeper zooKeeper = new ZooKeeper(
                    addr, timeout,
                    event -> logger.info("registry zk connect success...")
            );
            // 判断节点是否存在
            if (zooKeeper.exists(Constants.ZK_ROOT_DIR, false) == null) {
                // 创建 hikari-rpc 跟节点
                zooKeeper.create(Constants.ZK_ROOT_DIR, Constants.ZK_ROOT_DIR.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        // 持久化节点
                        CreateMode.PERSISTENT
                );
            }
            zooKeeper.create(Constants.ZK_ROOT_DIR + "/" + serverName,
                    (serverName + ","+ host + ":" + port).getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    // 临时节点
                    CreateMode.EPHEMERAL_SEQUENTIAL
            );
            logger.info("provider register success {}", serverName);
        } catch (Exception e) {
            throw new ZkConnectException("register to zk exception," + e.getMessage(), e.getCause());
        }
    }

}
