package com.mio.stream;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@EnableBinding(Sink.class)
@IntegrationComponentScan
@MessageEndpoint
@SpringBootApplication
public class MsStreamRecieverApplication {

    @ServiceActivator(inputChannel=Sink.INPUT)
    public void accept(Map<String, Object> msg){
        System.out.println(msg.get("msg").toString() + ":" + msg.get("name"));
    }
    
	public static void main(String[] args) {
		SpringApplication.run(MsStreamRecieverApplication.class, args);
	}
}
