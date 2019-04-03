package com.ema.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 */

//    logger.info("开始连接FTP服务器");
//    boolean result = ftpUtil.uploadFile("img", fileList);
//    logger.info("结束连接FTP服务器, 结束上传,上传结果:{}", result);
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private String ip;

    private int port;

    private String user;

    private String pwd;

    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 批量删除ftp服务器中的文件
     * @param remotePath
     * @param fileNameList
     * @return
     * @throws IOException
     */
    public int deleteFile(String remotePath, List<String> fileNameList) throws IOException {
        int count = 0;
        if (connectServer(ip, port, user, pwd)) {
            ftpClient.changeWorkingDirectory(remotePath);
            for (String fileName : fileNameList) {
                if (ftpClient.deleteFile(fileName)) {
                    count++;
                }
            }
            ftpClient.disconnect();
        }
        return count;
    }


    /**
     * 上传文件
     *
     * @param remotePath 上传的目录，最外层是ftp的主目录，如ftpfile/，然后再进入相应的子目录
     * @param fileList 文上传的件列表
     * @return 是否上传成功
     * @throws IOException
     */
    public boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(ip, port, user, pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath); //切换工作路径
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode(); //设置被动模式

                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                logger.info("上传文件异常", e);
                uploaded = false;
            } finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * 连接FTP服务器
     * @param ip ip
     * @param port 端口
     * @param user 用户名
     * @param pwd 密码
     * @return 是否连接成功
     */
    // TODO: 2019/3/13 这里可以更改为定时清理FTPClient而不是每次都new一 这样很浪费资源
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.info("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}