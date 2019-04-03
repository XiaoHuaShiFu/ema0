package com.ema.service.impl;

import com.ema.service.IFileService;
import com.ema.util.FTPUtil;
import com.ema.util.PropertiesUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 描述: 上传文件的类，需要在配置文件中配置ftp服务器信息
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-03-13 17:08
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private FTPUtil ftpUtil;

    public FileServiceImpl() {
        PropertiesUtil.setProps("ema.properties");
        this.ftpUtil = new FTPUtil(
                PropertiesUtil.getProperty("ftp.server.ip"),
                Integer.parseInt(PropertiesUtil.getProperty("ftp.port", "21")),
                PropertiesUtil.getProperty("ftp.user"),
                PropertiesUtil.getProperty("ftp.pass")
        );
    }

    /**
     * 从ftp服务器中删除一个文件
     *
     * @param remotePath 所在的ftp的子文件夹
     * @param fileName 文件名
     * @return 返回成功修改的个数
     */
    public int delete(String remotePath, String fileName) {
        int count = 0;
        try {
            count = ftpUtil.deleteFile(remotePath, Lists.newArrayList(fileName));
        } catch (IOException e) {
            logger.info("删除文件出错", e);
        }
        return count;
    }


    /**
     * 上传文件到ftp服务器
     *
     * @param file 文件
     * @param path 本地暂存路径
     * @param remotePath 所在的ftp的子文件夹
     * @param fileName 文件名
     * @return 在文件夹下带扩展名的文件名
     */
    public String upload(MultipartFile file, String path, String remotePath, String fileName) {
        //获得原始文件名
        String originalFileName = file.getOriginalFilename();
        //获得扩展名
        String fileExtensionName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        //拼接在服务器的文件名
        fileName = fileName + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名是:{},上传的路径是:{},新文件名是:{}", originalFileName, path, fileName);

        //查看是否有创建本地暂存图片目录
        File fileDir = new File(path);
        if (! fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //本地暂存文件地址
        File targetFile = new File(path, fileName); //拼接绝对路径

        try {
            //保存文件到暂存目录
            file.transferTo(targetFile);
            //将targetFile上传到ftp服务器
            ftpUtil.uploadFile(remotePath, Lists.newArrayList(targetFile));
            //删除暂存目录里面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.info("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }

}
