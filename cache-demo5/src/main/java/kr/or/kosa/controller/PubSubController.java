package kr.or.kosa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import kr.or.kosa.service.RedisPublisher;
import kr.or.kosa.service.RedisSubscriber;
import lombok.RequiredArgsConstructor;

@RequestMapping("/pubsub")
@RestController
@RequiredArgsConstructor
public class PubSubController {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private Map<String, ChannelTopic> channels;
    
    @PostConstruct
    public void init() {
        channels = new HashMap<>();
    }
    
    // 토픽 목록
    @GetMapping("/topics")
    public Set<String> getTopicAll() {
        return channels.keySet();
    }
    // 토픽 생성
    @PutMapping("/topics/{name}")
    public void createTopic(@PathVariable String name) {
        ChannelTopic channel = new ChannelTopic(name);
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        channels.put(name, channel);
    }
    // 메시지 발행
    @PostMapping("/topics/{name}")
    public void pushMessage(@PathVariable String name, @RequestParam String message) {
        ChannelTopic channel = channels.get(name);
        redisPublisher.publish(channel, message);
    }
    // 토픽 제거
    @DeleteMapping("/topics/{name}")
    public void deleteTopic(@PathVariable String name) {
        ChannelTopic channel = channels.get(name);
        redisMessageListener.removeMessageListener(redisSubscriber, channel);
        channels.remove(name);
    }
}