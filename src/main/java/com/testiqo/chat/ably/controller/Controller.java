package com.testiqo.chat.ably.controller;

import com.testiqo.chat.ably.model.Notification;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.realtime.ConnectionState;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.ClientOptions;
import io.ably.lib.types.ErrorInfo;
import io.ably.lib.types.Message;
import io.ably.lib.types.MessageExtras;
import io.ably.lib.util.JsonUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final AblyRealtime ablyRealtime;

    public Controller() throws AblyException {
        ClientOptions options = new ClientOptions("w55wHw._jFnzw:U5ulkXa0kAkbK2x0k9DY67ShRZG8EsJsEV842dmgrCM");
        ablyRealtime = new AblyRealtime(options);
    }

    @PostMapping("/connect")
    public void connect() {
        ablyRealtime.connection.on(ConnectionState.connected, state -> {
            System.out.println("Connected!");
        });
    }

    @PostMapping("/subscribe")
    public void subscribe() throws AblyException {
        Channel channel = ablyRealtime.channels.get("quickstart");
        channel.subscribe("greeting", message -> System.out.println("Received a greeting message in realtime: " + message.data));
    }

    @PostMapping("/send")
    public void send(@RequestParam String message) throws AblyException {
        Channel channel = ablyRealtime.channels.get("quickstart");
        channel.publish("greeting", message, new CompletionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Message sent");
            }

            @Override
            public void onError(ErrorInfo reason) {
                System.out.println("Message not sent, error occurred: " + reason.message);
            }
        });
    }

    @PostMapping("/close")
    public void close() {
        ablyRealtime.connection.close();
        ablyRealtime.connection.on(ConnectionState.closed, state -> {
            System.out.println("New state is " + state.current.name());
            switch (state.current) {
                case closed:
                    System.out.println("Closed the connection to Ably.");
                    break;
                case failed:
                    System.out.println("Failed to Closed the connection to Ably.");
                    break;
            }
        });
    }

    @PostMapping("/notification")
    public void notification(@RequestBody Notification notification) throws AblyException {
        Channel channel = ablyRealtime.channels.get("quickstart");
        Message message = new Message();
        message.name = notification.name();
        message.data = notification.body();
        var extras = JsonUtils.object()
                .add("push", JsonUtils.object()
                        .add("notification", JsonUtils.object()
                                .add("title", notification.title())
                                .add("body", notification.description()))).toJson();
        message.extras = new MessageExtras(extras);
        channel.publish(message);
    }

}
