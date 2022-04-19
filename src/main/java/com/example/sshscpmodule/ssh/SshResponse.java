package com.example.sshscpmodule.ssh;

import com.jcraft.jsch.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class SshResponse {
    @GetMapping("/sshTest03")
    public static String sshTest() throws Exception {
        String username = "ubuntu";
        String host = "52.79.103.245";
        int port = 22;
        String privateKeyPath = "/Users/imsooyeon/Desktop/workspace/key/aws2-paasta-c3-sue-key.pem";

        System.out.println("==> Connecting to" + host);
        Session session = null;
        Channel channel = null;

        // 세션 객체 생성 (사용자 이름, 접속할 호스트, 포트를 인자로 준다.)
        StringBuilder response = null;
        try {
            // JSch 객체 생성
            JSch jsch = new JSch();

            // key 설정
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(username, host, port);

            // 세션과 관련된 정보를 설정
            java.util.Properties config = new java.util.Properties();
            // 호스트 정보를 검사하지 않음
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 접속
            session.connect();

            // sftp 채널 열기
            channel = session.openChannel("exec");

            // 채널을 SSH용 채널 객체로 캐스팅
            ChannelExec channelExec = (ChannelExec) channel;

            System.out.println("==> Connected to" + host);

            channelExec.setPty(true);
        channelExec.setCommand("ls -al ~/workspace");

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            System.out.println("==> Connected to" + host);

            byte[] buffer = new byte[8192];
            int decodedLength;
            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
                response.append(new String(buffer, 0, decodedLength));
                System.out.println(response.toString());

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                System.out.println("close");
                channel.disconnect();
            }
            if (session != null) {
                System.out.println("close");
                session.disconnect();
            }
        }

        return response.toString();
    }


}
