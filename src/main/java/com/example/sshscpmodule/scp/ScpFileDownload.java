package com.example.sshscpmodule.scp;

import com.jcraft.jsch.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class ScpFileDownload {

    @GetMapping("/scpTest")
    public void scpDownload() throws Exception {

        String username = "ubuntu";
        String host = "52.79.103.245";
        int port = 22;
        String privateKeyPath = "/Users/imsooyeon/Desktop/workspace/key/aws2-paasta-c3-sue-key.pem";

        System.out.println("==> Connecting to" + host);

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);
        session = jsch.getSession(username, host, port);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        System.out.println("==> Connecting to" + host);

        try {

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            channelSftp.cd("/home/ubuntu/workspace/user/sue/");//해당 디렉토리 이동
            inputStream = channelSftp.get("spring-music.war");  //파일을 받는다.

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            outputStream = new FileOutputStream(new File("/Users/imsooyeon/Desktop/workspace/jspring-music01.war"));
            int i;
            while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                outputStream.close();
                inputStream.close();
                channel.disconnect();
                session.disconnect();
                System.out.println("close");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
