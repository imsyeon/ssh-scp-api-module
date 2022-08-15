package com.example.sshscpmodule.ssh;

import com.jcraft.jsch.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SshRequeest {
    @GetMapping("/sshRequeest")
    public static String sshRequeest() throws Exception {
        String username = "ubuntu";
        String host = "IP";
        int port = 22;
        String privateKeyPath = "KEY_PATH";

        System.out.println("==> Connecting to" + host);
        Session session = null;
        Channel channel = null;

        // 세션 객체 생성 (사용자 이름, 접속할 호스트, 포트를 인자로 준다.)
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

            channelExec.setCommand("touch ~/workspace/jschTest01.txt");
            channelExec.connect();

            System.out.println("==> Connected to" + host);

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return "ok";
    }


}
