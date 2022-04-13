package com.dogsong.rpc.registry;

import com.dogsong.rpc.common.Constants;
import com.dogsong.rpc.exception.ZkConnectException;
import com.dogsong.rpc.model.ProviderInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 服务发现
 *
 * @author <a href="mailto:dogsong99@gmail.com">dogsong</a>
 * @since 2021/8/2
 */
public class ServiceDiscovery {

    private final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    private volatile List<ProviderInfo> dataList = Lists.newArrayList();

    /**
     * 服务发现构造函数
     *
     * @param registryAddress 服务地址
     * @param timeOut 超时时间
     */
    public ServiceDiscovery(String registryAddress, int timeOut) throws ZkConnectException {
        try {
            // 获取 zk 连接
            ZooKeeper zooKeeper = new ZooKeeper(registryAddress, timeOut,
                    watchedEvent -> logger.info("consumer connect zk success!"));
            // 监听节点
            watchNode(zooKeeper);
        } catch (Exception e) {
            throw new ZkConnectException("connect to zk exception," + e.getMessage(), ExceptionUtils.getCause(e));
        }
    }

    /**
     * 监听节点，初始化提供方数据
     *
     * @param zk zk连接
     */
    public void watchNode(final ZooKeeper zk) {
        try {
            // 判断节点是否存在
            if (zk.exists(Constants.ZK_ROOT_DIR, false) == null) {
                return;
            }
            // 获取 hikari-rpc 子节点
            List<String> zkChildren = zk.getChildren(Constants.ZK_ROOT_DIR, event -> {
                // 节点是否改变，或者是否有服务上下线
                if (event.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)) {
                    watchNode(zk);
                }
            });
            // 如果子节点是空，则直接返回
            // 说明没有服务注册
            if (zkChildren.isEmpty()) {
                return;
            }
            List<ProviderInfo> providerInfos = new ArrayList<>(zkChildren.size());
            // 循环子节点，获取服务信息
            for (String node : zkChildren) {
                byte[] bytes = zk.getData(Constants.ZK_ROOT_DIR + "/" + node, false, null);
                String[] providerInfo = new String(bytes).split(",");
                if (providerInfo.length == 2) {
                    providerInfos.add(new ProviderInfo(providerInfo[0], providerInfo[1]));
                }
            }
            this.dataList = providerInfos;
            logger.info("获取服务端列表成功：{}", this.dataList);
        } catch (Exception e) {
            logger.error("watch error, {}.", ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 获取一个服务信息
     *
     * @param providerName 服务名
     * @return {@link ProviderInfo}
     */
    public ProviderInfo discover(String providerName) {
        if (dataList.isEmpty()) {
            return null;
        }
        List<ProviderInfo> providerInfos = dataList.stream()
                .filter(providerInfo -> providerName.equals(providerInfo.getName()))
                .collect(Collectors.toList());
        if (providerInfos.isEmpty()) {
            return null;
        }
        return providerInfos.get(ThreadLocalRandom.current()
                .nextInt(providerInfos.size()));
    }
}
