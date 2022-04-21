package com.example.sshscpmodule.ssh;

import com.jcraft.jsch.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class SshResponseTestVsphere {

    Session session = null;
    Channel channel = null;
    StringBuilder response = null;

    private void connectSSH() throws JSchException{

        String username = "ubuntu";
        String host = "10.0.10.11";
        int port = 22;
        String privateKeyPath = "D:\\sue-workspace\\key\\vsphere-paasta-c3-pem\\vsphere-paasta-c3-pem.key";

        System.out.println("==> Connecting to" + host);

        // 세션 객체 생성 (사용자 이름, 접속할 호스트, 포트를 인자로 준다.)
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
    }

    @GetMapping("/sshvsphere")
    public String sshTest01() throws Exception {

        try {
            System.out.println("sshTest01");
            connectSSH();
            // sftp 채널 열기
            channel = session.openChannel("exec");

            // 채널을 SSH용 채널 객체로 캐스팅
            ChannelExec channelExec = (ChannelExec) channel;

            channelExec.setPty(true);
            channelExec.setCommand("whoami; source ~/.profile; bosh vms;");

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            byte[] buffer = new byte[8192];
            int decodedLength;
            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
                response.append(new String(buffer, 0, decodedLength));
                System.out.println(response.toString());

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {

            disConnectSSH();
            sshTest02();
        }

        return response.toString();

    }
    public String sshTest02() throws Exception {

        try {

            System.out.println("sshTest02");
            // sftp 채널 열기
            channel = session.openChannel("exec");

            // 채널을 SSH용 채널 객체로 캐스팅
            ChannelExec channelExec = (ChannelExec) channel;

            channelExec.setPty(true);
            channelExec.setCommand( "source ~/.profile; bosh vms; bosh vms;");

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            byte[] buffer = new byte[8192];
            int decodedLength;
            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
                response.append(new String(buffer, 0, decodedLength));
            System.out.println(response.toString());

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {

            disConnectSSH();
            sshTest03();

        }

        return response.toString();

    }

    public String sshTest03() throws Exception {

        try {

            System.out.println("sshTest03");
            // sftp 채널 열기
            channel = session.openChannel("exec");

            // 채널을 SSH용 채널 객체로 캐스팅
            ChannelExec channelExec = (ChannelExec) channel;

            channelExec.setPty(true);
            channelExec.setCommand("source ~/.profile; bosh vms; bosh vms;");

            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            byte[] buffer = new byte[8192];
            int decodedLength;
            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0)
                response.append(new String(buffer, 0, decodedLength));
            System.out.println(response.toString());

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {

            disConnectSSHSession();

        }

        return response.toString();

    }

    private void disConnectSSH() {
        if (channel != null) channel.disconnect();
        System.out.println("close");

    }
    private void disConnectSSHSession() {
        if (channel != null) channel.disconnect();
        if (session != null) session.disconnect();
        System.out.println("close");

    }
}
